/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.smart.records;

import com.carsplates.records.CurrentMovementRecord;
import com.myz.connection.ConnectionContext;
import com.myz.log.logWriter;
import com.myz.record.DbTools;
import com.myz.record.Record;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Montazar Hamoud
 */
public class CurrentMovement extends CurrentMovementRecord
{
    //Constructor
    public CurrentMovement(ConnectionContext connection) 
    {
        super(connection);
    }
    
    //Data member
    VehicleCard   m_vehicleCard   = new VehicleCard(m_connection);
    Vehicle       m_vehicle       = new Vehicle(m_connection);
    Plate         m_plate         = new Plate(m_connection);
    DeliveredInfo m_DeliveredInfo = new DeliveredInfo(m_connection);
    
    //Methods
    public boolean getByPlateNoAndCity(String platesNo , int pnrCity)
    {
        String    SQL  = "SELECT * FROM CURRENT_MOVEMENT WHERE PNR_PLATE = (SELECT PNR FROM PLATE WHERE PLATE_NO = " + DbTools.setDB(platesNo)  ;
                  SQL += " AND PNR_PLATE_CITY = " + DbTools.setDB(pnrCity) + " )";
        Statement st  = null ;
        ResultSet rs  = null ;

        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if(rs.next())
            {
                get(rs);
                //Load sub records
                m_plate.get(m_pnrPlate);
                m_vehicle.get(m_pnrVehicle);
                m_vehicleCard.get(m_pnrVehicleCard);
                m_DeliveredInfo.get(m_pnrDeliveredInfo);
                return true ;
            }
            
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }
        finally
        {
            try
            {
                if(rs != null){rs.close();}
                if(st != null){st.close();}
                
            }
            catch(Exception ex)
            {
                logWriter.write(ex);
            }                
        }
        return false ;
    }
    
    public VehicleCard getVehicleCard()
    {
        return m_vehicleCard ;
    }
    
    public Vehicle getVehicle()
    {
        return m_vehicle ;
    }
    
    public Plate getPlate()
    {
        return m_plate ;
    }
    
    public DeliveredInfo getDeliveredInfo()
    {
        return m_DeliveredInfo ;
    }
    
     public String saveCurrentMovement()
    {
        boolean allSaved      = true ;
        String  result        = "";
        
        int pnrPlateB         = m_plate.getPnr();
        int pnrVehicleB       = m_vehicle.getPnr();
        int pnrVehicleCardB   = m_vehicleCard.getPnr();
        
       if( !m_plate.save())
       {
           allSaved  = false ;
           result   += " لم يتم حفظ اللوحة";
       }
        // added by yazan 2020-12-05
        if ( m_vehicle.getChassisNo() != null && m_vehicle.getChassisNo().length() > 0)
            if(!m_vehicle.save())
            {
               allSaved  = false ;
               result   += "لم يتم حفظ الالية ";
            }
            
                
        int pnrPlateA         = m_plate.getPnr();
        int pnrVehicleA       = m_vehicle.getPnr();
        //Save pnr plate at vehicleCard
        m_vehicleCard.setPnrPlate(pnrPlateA);
        if(!m_vehicleCard.save())
        {
           allSaved = false ;
           result   = "لم يتم حفظ البطاقة ";
        }
        
        int pnrVehicleCardA   = m_vehicleCard.getPnr();
        
        
        //We compare pnr after and before to know if it's update do not save archive record
        setPnrPlate(pnrPlateA);
        if(pnrPlateB != pnrPlateA)
        {
            ArchivedMovement archivedMovement = new ArchivedMovement(m_connection);
            
            archivedMovement.setPnrPlate(pnrPlateA);
            
            archivedMovement.setStatus(ArchivedMovement.STATUS_NEW_PLATE); 
            archivedMovement.setDate(getPlate().getPlateStartDate());
            archivedMovement.setPlateStatus(getPlateStatus());
            
            archivedMovement.save();
        }
        //Update archived movement 
        else
        {
            //No need for set archive status & plate status
            ArchivedMovement archivedMovement = new ArchivedMovement(m_connection);
            archivedMovement.getByPnrPlateAndstatus(pnrPlateA , ArchivedMovement.STATUS_NEW_PLATE);
            archivedMovement.setDate(getPlate().getPlateStartDate());
            
            archivedMovement.save();
        }
        
        setPnrVehicleCard(pnrVehicleCardA);
        if(pnrVehicleCardA != pnrVehicleCardB)
        {
            ArchivedMovement archivedMovement = new ArchivedMovement(m_connection);

            archivedMovement.setPnrPlate(pnrPlateA);
            archivedMovement.setPnrVehicleCard(pnrVehicleCardA);
            
            archivedMovement.setStatus(ArchivedMovement.STATUS_NEW_CARD); 
            archivedMovement.setDate(getPlate().getPlateStartDate()); 
            archivedMovement.setPlateStatus(getPlateStatus());
            
            archivedMovement.save();
            archivedMovement.recalculateArchivedMovements(pnrPlateA);
        }
        //Update archived movement 
        else
        {
            ArchivedMovement archivedMovement = new ArchivedMovement(m_connection);
            archivedMovement.getByLastVehicleCard(pnrPlateA);
            
            archivedMovement.save();
            archivedMovement.recalculateArchivedMovements(pnrPlateA); 
        }
        
        setPnrVehicle(pnrVehicleA);
        if(pnrVehicleB != pnrVehicleA)
        {
            ArchivedMovement archivedMovement = new ArchivedMovement(m_connection);
            
            archivedMovement.setPnrPlate(pnrPlateA);
            archivedMovement.setPnrVehicle(pnrVehicleA);
            archivedMovement.setPnrVehicleCard(pnrVehicleCardA);
            
            archivedMovement.setStatus(ArchivedMovement.STATUS_NEW_VEHICLE); 
            archivedMovement.setDate(getPlate().getPlateStartDate());  
            archivedMovement.setPlateStatus(getPlateStatus());
            
            archivedMovement.save();
            archivedMovement.recalculateArchivedMovements(pnrPlateA);
        }    
        //Update archived movement ( there is no mandatory to save vehicle )
        else if (pnrVehicleA != -1)
        {
            ArchivedMovement archivedMovement = new ArchivedMovement(m_connection);
            archivedMovement.getByLastVehicle(pnrPlateA);
            
            archivedMovement.setPnrVehicle(pnrVehicleA);
            archivedMovement.save();
            archivedMovement.recalculateArchivedMovements(pnrPlateA); 
        }

        if(!save())
        {
           allSaved = false ;
           result   = "لم يتم حفظ التسجيلة الحالية ";
        }
        
        if(allSaved)
            return "تم الحفظ بنجاح";
        else
            return result;
    }
    
    public void initCurrentMovement()
    {
        m_plate.get(m_pnrPlate);
        m_vehicle.get(m_pnrVehicle);
        m_vehicleCard.get(m_pnrVehicleCard);
        m_DeliveredInfo.get(m_pnrDeliveredInfo);
    }
    
    public boolean archiveMovement(int status , String date, Record record)
    {
        boolean res = true ;
        switch (status) 
        {
            case ArchivedMovement.STATUS_DELIVERED_PLATE:
                {
                    int recordPnrB = record.getPnr();
                    res &=  record.save();
                    int recordPnrA = record.getPnr();
                    
                    //in case the record does not saved
                    if(recordPnrA < 0 )
                        return false;
                    
                    //Set current movement data
                    setPnrDeliveredInfo(record.getPnr());
                    setPlateStatus(Plate.PLATE_STATUS_DELIVERED);
                    res &= save();
                    
                    //Set vehicle card data
                    getVehicleCard().setCardStatus(VehicleCard.CARD_STATUS_DELIVERED);
                    res &= getVehicleCard().save();
                    
                    //Insert new record
                    if(recordPnrB != recordPnrA)
                    {
                        //Set archive movement data
                        ArchivedMovement archiveRec = new ArchivedMovement(m_connection);
                        archiveRec.setStatus(status);
                        archiveRec.setDate( date );
                        archiveRec.setPlateStatus(getPlateStatus());
                        archiveRec.setPnrPlate(getPnrPlate());
                        archiveRec.setPnrVehicle(getPnrVehicle());
                        archiveRec.setPnrVehicleCard(getPnrVehicleCard());
                        archiveRec.setPnrDeliveredInfo(record.getPnr());
                        res &= archiveRec.save();
                        archiveRec.recalculateArchivedMovements(getPnrPlate());
                        
                    }
                    //Update
                    else
                    {
                        //Set archive movement data
                        ArchivedMovement archiveRec = new ArchivedMovement(m_connection);
                        archiveRec.getByLastDeliveredInfo(getPnrPlate());
                        archiveRec.setDate( date );
                        res &= archiveRec.save();
                        archiveRec.recalculateArchivedMovements(getPnrPlate()); 
                    }
                    return res ;

                }
            case ArchivedMovement.STATUS_RECEIVED_PLATE:
                {
                    DeliveredInfo deliveredInfo = (DeliveredInfo) record;
                    String receivedDate = deliveredInfo.getActualReceivedDate();
                    
                    deliveredInfo.setActualReceivedDate(date);
                    
                    res &=  deliveredInfo.save();
                    
                    //in case the record does not saved
                    if(record.getPnr() < 0 )
                        return false;
                    
                    setPnrDeliveredInfo(record.getPnr());
                    setPlateStatus(Plate.PLATE_STATUS_EXISTED);
                    res &= save();
                    
                    
                    //Update
                    if(receivedDate != null && receivedDate.length() > 0  )
                    {
                        ArchivedMovement archiveRec = new ArchivedMovement(m_connection);
                        archiveRec.getByLastRecived(getPnrPlate());
                        archiveRec.setDate( date );
                        res &= archiveRec.save();
                        archiveRec.recalculateArchivedMovements(getPnrPlate()); 
                    }
                    //Insert new record
                    else
                    {
                        ArchivedMovement archiveRec = new ArchivedMovement(m_connection);
                        archiveRec.setStatus(status);
                        archiveRec.setDate( date );
                        archiveRec.setPlateStatus(getPlateStatus());
                        archiveRec.setPnrPlate(getPnrPlate());
                        archiveRec.setPnrVehicle(getPnrVehicle());
                        archiveRec.setPnrVehicleCard(getPnrVehicleCard());
                        archiveRec.setPnrDeliveredInfo(record.getPnr());
                        res &= archiveRec.save();
                        archiveRec.recalculateArchivedMovements(getPnrPlate()); 
                        
                    }
                    
                    return res ;
                }
            case ArchivedMovement.STATUS_DROPPED_PLATE:
                {
                    res &=  record.save();
                    
                    //in case the record does not saved
                    if(record.getPnr() < 0 )
                        return false;
                    
                    setPnrPlate(record.getPnr());
                    setPlateStatus(Plate.PLATE_STATUS_DROPPED);
                    res &= save();
                    
                    ArchivedMovement archiveRec = new ArchivedMovement(m_connection);
                    archiveRec.getByDropped(getPnrPlate());
                    archiveRec.setStatus(status);
                    archiveRec.setDate( date );
                    archiveRec.setPlateStatus(getPlateStatus());
                    archiveRec.setPnrPlate(record.getPnr());
                    archiveRec.setPnrVehicle(getPnrVehicle());
                    archiveRec.setPnrVehicleCard(getPnrVehicleCard());
                    archiveRec.setPnrDeliveredInfo(getPnrDeliveredInfo());
                    res &= archiveRec.save();
                    archiveRec.recalculateArchivedMovements(getPnrPlate()); 

                    break;
                }
            case ArchivedMovement.STATUS_RENEW_CARD:
                {
                    //Change card status to dropped
                    int pnrVehicleCard      = getPnrVehicleCard() ;
                    VehicleCard vehicleCard = new VehicleCard(m_connection);
                    vehicleCard.get(pnrVehicleCard);
                    //Get old card status to set to the new card
                    int oldVehicleCardStatus = vehicleCard.getCardStatus();
                    ((VehicleCard)record).setCardStatus(oldVehicleCardStatus);
                    //Set old card status = dropped
                    vehicleCard.setCardStatus(VehicleCard.CARD_STATUS_DROPPED);
                    //Save pnr plate at vehicleCard
                    vehicleCard.setPnrPlate(getPnrPlate());
                    res &= vehicleCard.save();
                    
                    res &= record.save();
                    if(record.getPnr() < 0)
                        return false ;
                    
                    setPnrVehicleCard(record.getPnr());
                    res &= save();
                    
                    ArchivedMovement archiveRec = new ArchivedMovement(m_connection);
                    archiveRec.setStatus(status);
                    archiveRec.setDate(date);
                    archiveRec.setPlateStatus(getPlateStatus());
                    archiveRec.setPnrPlate(getPnrPlate());
                    archiveRec.setPnrVehicle(getPnrVehicle());
                    archiveRec.setPnrVehicleCard(record.getPnr());
                    archiveRec.setPnrDeliveredInfo( getPnrDeliveredInfo() );
                    res &= archiveRec.save();
                    return res ;
                }
            case ArchivedMovement.STATUS_CHANGE_VEHICLE:
                {
                    res &= record.save();
                    if(record.getPnr() < 0)
                        return false;
                    
                    setPnrVehicle(record.getPnr());
                    res &= save();
                    
                    ArchivedMovement archiveRec = new ArchivedMovement(m_connection);
                    archiveRec.setStatus(status);
                    archiveRec.setDate(date);
                    archiveRec.setPlateStatus(getPlateStatus());
                    archiveRec.setPnrPlate(getPnrPlate());
                    archiveRec.setPnrVehicle(record.getPnr());
                    archiveRec.setPnrVehicleCard(getPnrVehicleCard());
                    archiveRec.setPnrDeliveredInfo( getPnrDeliveredInfo() );
                    res &= archiveRec.save();
                    return res ;
                }
            case ArchivedMovement.STATUS_SEPARATE_VEHICLE:
            {
                setPnrVehicle(-1);
                res &= save();
                
                ArchivedMovement archiveRec = new ArchivedMovement(m_connection);
                archiveRec.setStatus(status);
                archiveRec.setDate(date);
                archiveRec.setPlateStatus(getPlateStatus());
                archiveRec.setPnrPlate(getPnrPlate());
                archiveRec.setPnrVehicle(-1);
                archiveRec.setPnrVehicleCard(getPnrVehicleCard());
                archiveRec.setPnrDeliveredInfo( getPnrDeliveredInfo() );
                res &= archiveRec.save();
                return res ;
            }
            default:
                return false ;
        }
        return res;
    }
            
            
            
}
