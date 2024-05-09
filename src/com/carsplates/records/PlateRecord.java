package com.carsplates.records ;

import com.carsplates.main.Main;
import com.myz.connection.ConnectionContext;
import com.myz.log.logWriter;
import com.myz.record.DbTools;
import com.myz.record.Record;
 import  java.sql.ResultSet ;


 
 
 /* 
 * --------------------------------------------------------------------------------
 *                                M   Y   Z                                        
 * --------------------------------------------------------------------------------
 * this file is generated automatically using the JavaRecordsBuilder .             
 * licensed to PRIMASYS company.                                                   
 * --------------------------------------------------------------------------------
 *    represents the table  Ps_PLATES in database.                        
 * --------------------------------------------------------------------------------
 * @version 2.0                                                                    
 * 
 */
 
 public class PlateRecord extends Record
 {
 
    //Constructor 
    public PlateRecord ( ConnectionContext connection ) 
    {
       super( "PLATE" , connection ) ;
       setLastUser(Main.USER.getUserName());
    }
 
 ///////////////////////////////////////////////////////////////////////
 //                          Data Members                             //
 ///////////////////////////////////////////////////////////////////////
    protected String     m_plateNo;
    protected String     m_plateStartDate;
    protected String     m_plateEndDate;
    protected int        m_pnrPlateCity;
    protected int        m_pnrPlateType;
    protected int        m_pnrPlateReturn;//TODO change to belong
    protected int        m_deliveredCounter ; 
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
          m_pnr              =   DbTools.getInt(  rs ,  "PNR"  );
          m_plateNo          =   DbTools.getString(  rs ,  "PLATE_NO"  );
          m_plateStartDate   =   DbTools.getString(  rs ,  "PLATE_START_DATE"  );
          m_plateEndDate     =   DbTools.getString(  rs ,  "PLATE_END_DATE"  );
          m_pnrPlateCity     =   DbTools.getInt(  rs ,  "PNR_PLATE_CITY"  );
          m_pnrPlateType     =   DbTools.getInt(  rs ,  "PNR_PLATE_TYPE"  );
          m_pnrPlateReturn   =   DbTools.getInt(  rs ,  "PNR_PLATE_RETURN"  );
          m_deliveredCounter =   DbTools.getInt(  rs ,  "DELIVERED_COUNTER"  );
          m_lastTime         =   DbTools.getLong(  rs ,  "LAST_TIME"  );
          m_lastUser         =   DbTools.getString(  rs ,  "LAST_USER"  );
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "PlateRecord.get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
    }
 
 @Override
    public boolean insert ( int type )
    {
 
       try 
       {
          if ( m_pnr < 1 ) 
          {
             String SQL =  " INSERT INTO  PLATE    ( PLATE_NO , PLATE_START_DATE , PLATE_END_DATE , PNR_PLATE_CITY , PNR_PLATE_TYPE , PNR_PLATE_RETURN , DELIVERED_COUNTER , LAST_TIME , LAST_USER  )  VALUES    (  " +  DbTools.setDB (m_plateNo  ) + " , " + DbTools.setDB( m_plateStartDate ) + " , " + DbTools.setDB( m_plateEndDate )+  " ,  " +  DbTools.setDB (m_pnrPlateCity  ) +   " ,  " +  DbTools.setDB (m_pnrPlateType  ) +   " ,  " +  DbTools.setDB (m_pnrPlateReturn  ) +   " ,  " + DbTools.setDB( m_deliveredCounter ) + " , " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
             m_pnr = m_connection.executeSQL( SQL , ConnectionContext.MODE_LAST_ID);
             return m_pnr >= 1 ;
       
          }
 
          else 
          {
             String SQL =  " INSERT INTO  PLATE    ( PNR , PLATE_NO , PLATE_START_DATE , PLATE_END_DATE , PNR_PLATE_CITY , PNR_PLATE_TYPE , PNR_PLATE_RETURN , DELIVERED_COUNTER , LAST_TIME , LAST_USER  )  VALUES    ( " + DbTools.setDB ( m_pnr ) + " ,   " +  DbTools.setDB (m_plateNo  ) + " , " + DbTools.setDB( m_plateStartDate ) + " , " + DbTools.setDB(m_plateEndDate)+   " ,  " +  DbTools.setDB (m_pnrPlateCity  ) +   " ,  " +  DbTools.setDB (m_pnrPlateType  ) +   " ,  " +  DbTools.setDB (m_pnrPlateReturn  ) +  " , " + DbTools.setDB( m_deliveredCounter ) + " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
 
             return m_connection.executeSQL( SQL);
          }
       }
 
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "PlateRecord .Insert( int type )" + ex.toString()); 
       }
       return false ;
    }
 @Override
    public boolean update( int type )
    {
 
       try 
       {
          String SQL =  " UPDATE   PLATE  Set  "  +  " PLATE_NO =  "  + DbTools.setDB (m_plateNo ) + " , PLATE_START_DATE = " + DbTools.setDB( m_plateStartDate ) + " , PLATE_END_DATE = " + DbTools.setDB(m_plateEndDate) +  " , PNR_PLATE_CITY =  "  + DbTools.setDB (m_pnrPlateCity ) +  " ,  PNR_PLATE_TYPE =  "  + DbTools.setDB (m_pnrPlateType ) +  " ,  PNR_PLATE_RETURN =  "  + DbTools.setDB (m_pnrPlateReturn ) +  " , DELIVERED_COUNTER = " + DbTools.setDB( m_deliveredCounter ) +  " ,  LAST_TIME =  "  + DbTools.setDB ( m_lastTime ) +  " ,  LAST_USER =  "  + DbTools.setDB ( m_lastUser ) + " WHERE PNR = "  + m_pnr;
          return m_connection.executeSQL( SQL ) ;
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "PlateRecord .get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
       return false ;
    }
 
 
    // Setter & Getter Methods 
     public void setPlateNo(String  platesNo)
    {
       m_plateNo   =   platesNo;
    }
     public String getPlateNo()
    {
       return m_plateNo;
    }
    
    public void setPlateStartDate(String plateStartDate)
    {
       m_plateStartDate = plateStartDate ;
    }
    public String getPlateStartDate()
    {
       return m_plateStartDate ;
    }
    
    public void setPlateEndDate(String plateEndDate)
    {
        m_plateEndDate = plateEndDate ;
    }
    public String getPlateEndDate()
    {
        return m_plateEndDate ;
    }
    
     public void setPnrPlateCity(int  pnrPlatesCity)
    {
       m_pnrPlateCity   =   pnrPlatesCity;
    }
     public int getPnrPlateCity()
    {
       return m_pnrPlateCity;
    }
 
     public void setPnrPlateType(int  pnrPlatesType)
    {
       m_pnrPlateType   =   pnrPlatesType;
    }
     public int getPnrPlateType()
    {
       return m_pnrPlateType;
    }
 
     public void setPnrPlateReturn(int  pnrPlatesReturn)
    {
       m_pnrPlateReturn   =   pnrPlatesReturn;
    }
     public int getPnrPlateReturn()
    {
       return m_pnrPlateReturn;
    }
 
    public void setDeliveredCounter(int deliveredCounter)
    {
        m_deliveredCounter = deliveredCounter ;
    }
    public int getDeliveredCounter()
    {
        return m_deliveredCounter;
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
 
 