/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.smart.records;

import com.carsplates.records.ArchivedMovementRecord;
import com.myz.component.myzComboBoxItem;
import com.myz.connection.ConnectionContext;
import com.myz.log.logWriter;
import com.myz.record.DbTools;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Montazar Hamoud
 */
public class ArchivedMovement extends ArchivedMovementRecord  
{
    
    public ArchivedMovement(ConnectionContext connection) 
    {
        super(connection);
    }

    //Class member
    public static final int STATUS_NEW_PLATE            = 1 ;
    public static final int STATUS_RECEIVED_PLATE       = 2 ;
    public static final int STATUS_DELIVERED_PLATE      = 3 ;
    public static final int STATUS_DROPPED_PLATE        = 4 ;
    public static final int STATUS_NEW_CARD             = 5 ;
    public static final int STATUS_RENEW_CARD           = 6 ; 
    public static final int STATUS_NEW_VEHICLE          = 7 ;    
    public static final int STATUS_CHANGE_VEHICLE       = 8 ;    
    public static final int STATUS_SEPARATE_VEHICLE     = 9 ;    
    
    public static final String S_STATUS_NEW_PLATE        = "لوحة جديدة" ;
    public static final String S_STATUS_RECEIVED_PLATE   = "استلام لوحة" ;
    public static final String S_STATUS_DELIVERED_PLATE  = "تسليم لوحة" ;
    public static final String S_STATUS_DROPPED_PLATE    = "إسقاط لوحة" ;
    public static final String S_STATUS_NEW_CARD         = "بطاقة جديدة" ;
    public static final String S_STATUS_RENEW_CARD       = "تجديد بطاقة" ; 
    public static final String S_STATUS_NEW_VEHICLE      = "الية جديدة" ;    
    public static final String S_STATUS_CHANGE_VEHICLE   = "إضافة مشخصات" ;    
    public static final String S_STATUS_SEPARATE_VEHICLE = "إزالة مشخصات" ;    
    
    
    
    //Data member
    VehicleCard   m_vehicleCard   = new VehicleCard(m_connection);
    Vehicle       m_vehicle       = new Vehicle(m_connection);
    Plate         m_plate         = new Plate(m_connection);
    DeliveredInfo m_DeliveredInfo = new DeliveredInfo(m_connection);
    
    
    //Methods
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
    
    public void getByPnrPlateAndstatus(int pnrPlate , int status)
    {
        String SQL  = "SELECT * FROM ARCHIVED_MOVEMENT WHERE PNR_PLATE = " + DbTools.setDB(pnrPlate);
               SQL += " AND STATUS = " + DbTools.setDB(status);
   
        ResultSet rs = null ;
        Statement st = null ;
        
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if( rs.next() ){ get(rs); }
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
    }
    
    public void getByLastVehicleCard(int pnrPlate  )
    {
        String SQL  = "SELECT * FROM ARCHIVED_MOVEMENT WHERE PNR_PLATE = " + DbTools.setDB(pnrPlate);
               SQL += " AND STATUS IN ( " + STATUS_NEW_CARD + " , " + STATUS_RENEW_CARD + " ) ";
               SQL += " ORDER BY DATE DESC , PNR DESC ";

        ResultSet rs = null ;
        Statement st = null ;
        
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if( rs.next() ){ get(rs); }
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
    }
    
    public void getByLastVehicle(int pnrPlate )
    {
        String SQL  = "SELECT * FROM ARCHIVED_MOVEMENT WHERE PNR_PLATE = " + DbTools.setDB(pnrPlate) ; 
               SQL += " AND STATUS IN " ;
               SQL += "( " + STATUS_NEW_VEHICLE + " , " + STATUS_CHANGE_VEHICLE + " , " + STATUS_SEPARATE_VEHICLE + " )";
               SQL += " ORDER BY PNR DESC ";
        ResultSet rs = null ;
        Statement st = null ;
        
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if( rs.next() ){ get(rs); }
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
    }
    
    public void getByLastDeliveredInfo(int pnrPlate)
    {
            String SQL  = "SELECT * FROM ARCHIVED_MOVEMENT WHERE PNR_PLATE = " + DbTools.setDB(pnrPlate) ; 
                   SQL += " AND STATUS IN ( " + STATUS_DELIVERED_PLATE + " )" ;
                   SQL += " ORDER BY PNR DESC ";
        ResultSet rs = null ;
        Statement st = null ;
        
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if( rs.next() ){ get(rs); }
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
    }
    
    public void getByLastRecived(int pnrPlate)
    {
        String SQL  = "SELECT * FROM ARCHIVED_MOVEMENT WHERE PNR_PLATE = " + DbTools.setDB(pnrPlate) ; 
               SQL += " AND STATUS IN ( " + STATUS_RECEIVED_PLATE + " )" ;
               SQL += " ORDER BY PNR DESC ";
               
        ResultSet rs = null ;
        Statement st = null ;
        
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if( rs.next() ){ get(rs); }
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
    }
    
    public void getByDropped(int pnrPlate)
    {
        String SQL  = "SELECT * FROM ARCHIVED_MOVEMENT WHERE PNR_PLATE = " + DbTools.setDB(pnrPlate) ; 
               SQL += " AND STATUS IN ( " + STATUS_DROPPED_PLATE + " )" ;
               SQL += " ORDER BY PNR DESC ";
               
        ResultSet rs = null ;
        Statement st = null ;
        
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if( rs.next() ){ get(rs); }
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
    }
    
    public static String getArchivedStatusString(int status)
    {
        if (status == STATUS_NEW_PLATE)
            return S_STATUS_NEW_PLATE;
        else if (status == STATUS_RECEIVED_PLATE)
            return S_STATUS_RECEIVED_PLATE;
        else if (status == STATUS_DELIVERED_PLATE)
            return S_STATUS_DELIVERED_PLATE;
        else if (status == STATUS_DROPPED_PLATE)
            return S_STATUS_DROPPED_PLATE;
        else if (status == STATUS_NEW_CARD)
            return S_STATUS_NEW_CARD;
        else if (status == STATUS_RENEW_CARD)
            return S_STATUS_RENEW_CARD;
        else if (status == STATUS_NEW_VEHICLE)
            return S_STATUS_NEW_VEHICLE;
        else if (status == STATUS_CHANGE_VEHICLE)
            return S_STATUS_CHANGE_VEHICLE;
        else if (status == STATUS_SEPARATE_VEHICLE)
            return S_STATUS_SEPARATE_VEHICLE;
        return "";
    }
    
    public static Vector getArchivedStatusVec()
    {
        Vector status = new Vector();
        myzComboBoxItem item = null;
        item = new myzComboBoxItem(S_STATUS_NEW_PLATE, STATUS_NEW_PLATE);
        status.add(item);
        item = new myzComboBoxItem(S_STATUS_RECEIVED_PLATE, STATUS_RECEIVED_PLATE);
        status.add(item);
        item = new myzComboBoxItem(S_STATUS_DELIVERED_PLATE, STATUS_DELIVERED_PLATE);
        status.add(item);
        item = new myzComboBoxItem(S_STATUS_DROPPED_PLATE, STATUS_DROPPED_PLATE);
        status.add(item);
        item = new myzComboBoxItem(S_STATUS_NEW_CARD, STATUS_NEW_CARD);
        status.add(item);
        item = new myzComboBoxItem(S_STATUS_RENEW_CARD, STATUS_RENEW_CARD);
        status.add(item);
        item = new myzComboBoxItem(S_STATUS_NEW_VEHICLE, STATUS_NEW_VEHICLE);
        status.add(item);
        item = new myzComboBoxItem(S_STATUS_CHANGE_VEHICLE, STATUS_CHANGE_VEHICLE);
        status.add(item);
        item = new myzComboBoxItem(S_STATUS_SEPARATE_VEHICLE, STATUS_SEPARATE_VEHICLE);
        status.add(item);
        return status;
        
    }

    public Vector<ArchivedMovement> getAllMovements(int pnrPlate)
    {
        String SQL  = "SELECT * FROM ARCHIVED_MOVEMENT WHERE PNR_PLATE = " + DbTools.setDB(pnrPlate);
               SQL += " ORDER BY DATE ASC , PNR ASC ";
               
        Vector <ArchivedMovement>   vMovement = new Vector();
        ArchivedMovement            movement  = null ;
        Statement st = null ;
        ResultSet rs = null ;
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            while(rs.next())
            {
                movement = new ArchivedMovement(m_connection);
                movement.get(rs);
                vMovement.add(movement);
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
                if(st != null){st.close();}
                if(rs != null){rs.close();}
            }
            catch(Exception ex)
            {
                logWriter.write(ex);
            }
        }
        return vMovement;
    }
    
    public void recalculateArchivedMovements(int pnrPlate)
    {
        Vector <ArchivedMovement> vMovement = getAllMovements(pnrPlate);
        
        if(vMovement.isEmpty())
            return;
        int pnrVehicle       = -1;
        int pnrVehicleCard   = -1;
        int pnrDeliveredInfo = -1;
        
        for(ArchivedMovement movement : vMovement)
        {
            switch(movement.getStatus())
            {
                case STATUS_NEW_VEHICLE      :
                case STATUS_CHANGE_VEHICLE   :
                case STATUS_SEPARATE_VEHICLE :
                    pnrVehicle = movement.getPnrVehicle();
                    break ;
                case STATUS_NEW_CARD         :
                case STATUS_RENEW_CARD       :
                    pnrVehicleCard = movement.getPnrVehicleCard();
                    break ;
                case STATUS_DELIVERED_PLATE  :
                    pnrDeliveredInfo = movement.getPnrDeliveredInfo();
                    break;
                
            }
            movement.setPnrVehicle(pnrVehicle);
            movement.setPnrVehicleCard(pnrVehicleCard);
            movement.setPnrDeliveredInfo(pnrDeliveredInfo);
            movement.save();
                    
        }
    }

}
