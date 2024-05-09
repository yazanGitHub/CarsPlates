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
 *    represents the table  Ps_CARS in database.                        
 * --------------------------------------------------------------------------------
 * @version 2.0                                                                    
 * 
 */
 
 public class VehicleRecord extends Record
 {
 
    //Constructor 
    public VehicleRecord (  ConnectionContext connection ) 
    {
       super( "VEHICLE" , connection ) ;
       setLastUser(Main.USER.getUserName());
    }
 
 ///////////////////////////////////////////////////////////////////////
 //                          Data Members                             //
 ///////////////////////////////////////////////////////////////////////
    protected String     m_chassisNo ;
    protected String     m_engineNo ;
    protected String     m_productionYear;
    protected int        m_type;
    protected int        m_pnrCarManufacturer;
    protected int        m_pnrCarColor;
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
          m_pnr                  =   DbTools.getInt(  rs ,  "PNR"  );
          m_chassisNo            =   DbTools.getString(  rs ,  "CHASSIS_NO"  );
          m_engineNo             =   DbTools.getString(  rs ,  "ENGINE_NO"  );
          m_productionYear       =   DbTools.getString(  rs ,  "PRODUCTION_YEAR"  );
          m_type                 =   DbTools.getInt(     rs ,  "TYPE"  );
          m_pnrCarManufacturer   =   DbTools.getInt(     rs ,  "PNR_VEHICLE_MANUFACTURER"  );
          m_pnrCarColor          =   DbTools.getInt(     rs ,  "PNR_VEHICLE_COLOR"  );
          m_lastTime             =   DbTools.getLong(    rs ,  "LAST_TIME"  );
          m_lastUser             =   DbTools.getString(  rs ,  "LAST_USER"  );
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "CarsRecord.get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
    }
 
 @Override
    public boolean insert ( int type )
    {
 
        try 
        {
          if ( m_pnr < 1 ) 
          {
            String SQL =  " INSERT INTO  VEHICLE    ( CHASSIS_NO  , ENGINE_NO  , PRODUCTION_YEAR , TYPE , PNR_VEHICLE_MANUFACTURER , PNR_VEHICLE_COLOR , LAST_TIME , LAST_USER  )  VALUES    (  " +  DbTools.setDB ( m_chassisNo   ) +   " ,  " +  DbTools.setDB ( m_engineNo   ) +   " ,  " +  DbTools.setDB (m_productionYear  ) +   " ,  " +  DbTools.setDB ( m_type  ) +   " ,  " +  DbTools.setDB ( m_pnrCarManufacturer  ) +   " ,  " +  DbTools.setDB ( m_pnrCarColor  ) +   " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
            m_pnr = m_connection.executeSQL( SQL , ConnectionContext.MODE_LAST_ID);
            return m_pnr >= 1  ;
          }

          else 
          {
            String SQL =  " INSERT INTO  VEHICLE    ( PNR , CHASSIS_NO  , ENGINE_NO  , PRODUCTION_YEAR , TYPE , PNR_VEHICLE_MANUFACTURER , PNR_VEHICLE_COLOR , LAST_TIME , LAST_USER  )  VALUES    ( " + DbTools.setDB ( m_pnr ) + " ,   " +  DbTools.setDB ( m_chassisNo   ) +   " ,  " +  DbTools.setDB ( m_engineNo   ) +   " ,  " +  DbTools.setDB (m_productionYear  ) +   " ,  " +  DbTools.setDB ( m_type  ) +   " ,  " +  DbTools.setDB ( m_pnrCarManufacturer  ) +   " ,  " +  DbTools.setDB ( m_pnrCarColor  ) +   " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
            return m_connection.executeSQL( SQL);

          }
        }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "CarsRecord .Insert( int type )" + ex.toString()); 
       }
       return false ;
    }
 @Override
    public boolean update( int type )
    {
 
       try 
       {
          String SQL =  " UPDATE   VEHICLE  Set  "  +  " CHASSIS_NO  =  "  + DbTools.setDB ( m_chassisNo  ) +  " , ENGINE_NO  =  "  + DbTools.setDB ( m_engineNo  ) +  " , PRODUCTION_YEAR =  "  + DbTools.setDB (m_productionYear ) +  " , TYPE =  "  + DbTools.setDB ( m_type ) +  " , PNR_VEHICLE_MANUFACTURER =  "  + DbTools.setDB ( m_pnrCarManufacturer ) +  " , PNR_VEHICLE_COLOR =  "  + DbTools.setDB ( m_pnrCarColor ) +  " , LAST_TIME =  "  + DbTools.setDB ( m_lastTime ) +  " , LAST_USER =  "  + DbTools.setDB ( m_lastUser ) + " WHERE PNR = "  + m_pnr;
          return m_connection.executeSQL( SQL ) ;
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "CarsRecord .get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
       return false ;
    }
 
 
    // Setter & Getter Methods 
     public void setChassisNo (String  chassisNo )
    {
       m_chassisNo    =   chassisNo ;
    }
     public String getChassisNo ()
    {
       return m_chassisNo ;
    }
 
     public void setEngineNo (String  engineNo )
    {
       m_engineNo    =   engineNo ;
    }
     public String getEngineNo ()
    {
       return m_engineNo ;
    }
 
     public void setProductionYear(String  productionDate)
    {
       m_productionYear   =   productionDate;
    }
     public String getProductionYear()
    {
       return m_productionYear;
    }
 
     public void setType(int  type)
    {
       m_type   =   type;
    }
     public int getType()
    {
       return m_type;
    }
 
     public void setPnrCarManufacturer(int  pnrCarManufacturer)
    {
       m_pnrCarManufacturer   =   pnrCarManufacturer;
    }
     public int getPnrCarManufacturer()
    {
       return m_pnrCarManufacturer;
    }
 
     public void setPnrCarColor(int  pnrCarColor)
    {
       m_pnrCarColor   =   pnrCarColor;
    }
     public int getPnrCarColor()
    {
       return m_pnrCarColor;
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
 
 