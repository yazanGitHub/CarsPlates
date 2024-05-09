package com.carsplates.records;
import com.carsplates.main.Main;
 import  com.myz.connection.ConnectionContext ;
import com.myz.log.logWriter;
 import  com.myz.record.DbTools ;
 import  com.myz.record.Record;
 import  java.sql.ResultSet ;
 
 
 /* 
 * --------------------------------------------------------------------------------
 *                                M   Y   Z                                        
 * --------------------------------------------------------------------------------
 * this file is generated automatically using the JavaRecordsBuilder .             
 * licensed to PRIMASYS company.                                                   
 * --------------------------------------------------------------------------------
 *    represents the table  ARCHIVED_MOVEMENT in database.                        
 * --------------------------------------------------------------------------------
 * @version 2.0                                                                    
 * 
 */
 
 public class ArchivedMovementRecord extends Record
 {
 
    //Constructor 
    public ArchivedMovementRecord (  ConnectionContext connection ) 
    {
       super( "ARCHIVED_MOVEMENT", connection ) ;
       setLastUser(Main.USER.getUserName());
    }
 
 ///////////////////////////////////////////////////////////////////////
 //                          Data Members                             //
 ///////////////////////////////////////////////////////////////////////
    protected int        m_pnrPlate;
    protected int        m_pnrVehicle;
    protected int        m_pnrVehicleCard;
    protected int        m_pnrDeliveredInfo;
    protected String     m_note;
    protected int        m_plateStatus;
    protected int        m_status;
    protected String     m_date;
    protected long       m_lastTime;
    protected String     m_lastUser;
 
 
 ///////////////////////////////////////////////////////////////////////
 //                              Methods                              //
 ///////////////////////////////////////////////////////////////////////
 @Override
    public void get( ResultSet rs , int type )
    {
 
       try 
       {
          m_pnr               =   DbTools.getInt(  rs ,  "PNR"  );
          m_pnrPlate          =   DbTools.getInt(  rs ,  "PNR_PLATE"  );
          m_pnrVehicle        =   DbTools.getInt(  rs ,  "PNR_VEHICLE"  );
          m_pnrVehicleCard    =   DbTools.getInt(  rs ,  "PNR_VEHICLE_CARD"  );
          m_pnrDeliveredInfo  =   DbTools.getInt(  rs ,  "PNR_DELIVERED_INFO"  );
          m_note              =   DbTools.getString(  rs ,  "NOTE"  );
          m_plateStatus       =   DbTools.getInt(  rs ,  "PLATE_STATUS"  );
          m_status            =   DbTools.getInt(  rs ,  "STATUS"  );
          m_date              =   DbTools.getString(  rs ,  "DATE"  );
          m_lastTime          =   DbTools.getLong(  rs ,  "LAST_TIME"  );
          m_lastUser          =   DbTools.getString(  rs ,  "LAST_USER"  );
       }
        catch (Exception ex) 
       {
          logWriter.write(ex);
          System.out.println( "ArchivedMovementRecord.get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
    }
 
 @Override
    public boolean insert ( int type )
    {
 
       try 
       {
          if ( m_pnr < 1 ) 
          {
             String SQL =  " INSERT INTO  ARCHIVED_MOVEMENT    ( PNR_PLATE , PNR_VEHICLE , PNR_VEHICLE_CARD , PNR_DELIVERED_INFO , NOTE , PLATE_STATUS , STATUS , DATE , LAST_TIME , LAST_USER  )  VALUES    (  " +  DbTools.setDB ( m_pnrPlate  ) +   " ,  " +  DbTools.setDB ( m_pnrVehicle  ) +   " ,  " +  DbTools.setDB ( m_pnrVehicleCard  ) +   " ,  " +  DbTools.setDB (m_pnrDeliveredInfo  ) +   " ,  " +  DbTools.setDB ( m_note  ) +   " ,  " +  DbTools.setDB ( m_plateStatus  ) +   " ,  " +  DbTools.setDB ( m_status  ) +   " ,  " +  DbTools.setDB ( m_date  ) +   " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
             m_pnr = m_connection.executeSQL( SQL , ConnectionContext.MODE_LAST_ID);
             return m_pnr >= 1 ;
          }
 
          else 
          {
             String SQL =  " INSERT INTO  ARCHIVED_MOVEMENT    ( PNR , PNR_PLATE , PNR_VEHICLE , PNR_VEHICLE_CARD , PNR_DELIVERED_INFO , NOTE , PLATE_STATUS , STATUS , DATE , LAST_TIME , LAST_USER  )  VALUES    ( " + DbTools.setDB ( m_pnr ) + " ,   " +  DbTools.setDB ( m_pnrPlate  ) +   " ,  " +  DbTools.setDB ( m_pnrVehicle  ) +   " ,  " +  DbTools.setDB ( m_pnrVehicleCard  ) +   " ,  " +  DbTools.setDB (m_pnrDeliveredInfo  ) +   " ,  " +  DbTools.setDB ( m_note  ) +   " ,  " +  DbTools.setDB ( m_plateStatus  ) +   " ,  " +  DbTools.setDB ( m_status  ) +   " ,  " +  DbTools.setDB ( m_date  ) +   " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
 
             return m_connection.executeSQL( SQL);
          }
       }
 
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "ArchivedMovementRecord .Insert( int type )" + ex.toString()); 
       }
       return false ;
    }
 @Override
    public boolean update( int type )
    {
 
       try 
       {
          String SQL =  " UPDATE   ARCHIVED_MOVEMENT  Set  "  +  " PNR_PLATE =  "  + DbTools.setDB ( m_pnrPlate ) +  " , PNR_VEHICLE =  "  + DbTools.setDB ( m_pnrVehicle ) +  " , PNR_VEHICLE_CARD =  "  + DbTools.setDB ( m_pnrVehicleCard ) +  " , PNR_DELIVERED_INFO =  "  + DbTools.setDB (m_pnrDeliveredInfo ) +  " , NOTE =  "  + DbTools.setDB ( m_note ) +  " , PLATE_STATUS =  "  + DbTools.setDB ( m_plateStatus ) +  " , STATUS =  "  + DbTools.setDB ( m_status ) +  " , DATE =  "  + DbTools.setDB ( m_date ) +  " , LAST_TIME =  "  + DbTools.setDB ( m_lastTime ) +  " , LAST_USER =  "  + DbTools.setDB ( m_lastUser )  + " WHERE PNR = " + m_pnr  ;
          return m_connection.executeSQL( SQL ) ;
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "ArchivedMovementRecord .get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
       return false ;
    }
 
 
    // Setter & Getter Methods 
     public void setPnrPlate(int  pnrPlate)
    {
       m_pnrPlate   =   pnrPlate;
    }
     public int getPnrPlate()
    {
       return m_pnrPlate;
    }
 
     public void setPnrVehicle(int  pnrVehicle)
    {
       m_pnrVehicle   =   pnrVehicle;
    }
     public int getPnrVehicle()
    {
       return m_pnrVehicle;
    }
 
     public void setPnrVehicleCard(int  pnrVehicleCard)
    {
       m_pnrVehicleCard   =   pnrVehicleCard;
    }
     public int getPnrVehicleCard()
    {
       return m_pnrVehicleCard;
    }
 
     public void setPnrDeliveredInfo(int  pnrReceivedInfo)
    {
       m_pnrDeliveredInfo   =   pnrReceivedInfo;
    }
     public int getPnrDeliveredInfo()
    {
       return m_pnrDeliveredInfo;
    }
 
     public void setNote(String  note)
    {
       m_note   =   note;
    }
     public String getNote()
    {
       return m_note;
    }
 
     public void setPlateStatus(int  plateStatus)
    {
       m_plateStatus   =   plateStatus;
    }
     public int getPlateStatus()
    {
       return m_plateStatus;
    }
 
     public void setStatus(int  status)
    {
       m_status   =   status;
    }
     public int getStatus()
    {
       return m_status;
    }
 
     public void setDate(String  date)
    {
       m_date   =   date;
    }
     public String getDate()
    {
       return m_date;
    }
 
     public void setLastTime(long  lastTime)
    {
       m_lastTime   =   lastTime;
    }
     public long getLastTime()
    {
       return m_lastTime;
    }
 
     public void setLastUser(String  lastUser)
    {
       m_lastUser   =   lastUser;
    }
     public String getLastUser()
    {
       return m_lastUser;
    }
 
 
 }
 
 