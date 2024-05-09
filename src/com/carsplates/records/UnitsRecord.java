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
 *    represents the table  UNITS in database.                        
 * --------------------------------------------------------------------------------
 * @version 2.0                                                                    
 * 
 */
 
 public class UnitsRecord extends Record
 {
 
    //Constructor 
    public UnitsRecord (ConnectionContext connection ) 
    {
       super( "UNITS" , connection ) ;
       setLastUser(Main.USER.getUserName());
    }
 
 ///////////////////////////////////////////////////////////////////////
 //                          Data Members                             //
 ///////////////////////////////////////////////////////////////////////
    protected String    m_tName;
    protected long      m_lastTime;
    protected String    m_lastUser;
 
 
 ///////////////////////////////////////////////////////////////////////
 //                              Methods                              //
 ///////////////////////////////////////////////////////////////////////
 @Override
    public void get( ResultSet rs , int type )
    {
 
       try 
       {
          m_pnr        =   DbTools.getInt(  rs ,  "PNR"  );
          m_tName      =   DbTools.getString(  rs ,  "T_NAME"  );
          m_lastTime   =   DbTools.getLong(  rs ,  "LAST_TIME"  );
          m_lastUser   =   DbTools.getString(  rs ,  "LAST_USER"  );
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "UnitsRecord.get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
    }
 
 @Override
    public boolean insert ( int type )
    {
 
       try 
       {
          if ( m_pnr < 1 ) 
          {
             String SQL =  " INSERT INTO  UNITS    ( T_NAME , LAST_TIME , LAST_USER  )  VALUES    (  " +  DbTools.setDB ( m_tName  ) +   " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
             m_pnr = m_connection.executeSQL( SQL , ConnectionContext.MODE_LAST_ID);
             return m_pnr >= 1 ;
       
          }
 
          else 
          {
             String SQL =  " INSERT INTO  UNITS    ( PNR , T_NAME , LAST_TIME , LAST_USER  )  VALUES    ( " + DbTools.setDB ( m_pnr ) + " ,   " +  DbTools.setDB ( m_tName  ) +   " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
 
             return m_connection.executeSQL( SQL);
          }
       }
 
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "UnitsRecord .Insert( int type )" + ex.toString()); 
       }
       return false ;
    }
 @Override
    public boolean update( int type )
    {
 
       try 
       {
          String SQL =  " UPDATE   UNITS  Set  "  +  " T_NAME =  "  + DbTools.setDB ( m_tName ) +  " , LAST_TIME =  "  + DbTools.setDB ( m_lastTime ) +  " , LAST_USER =  "  + DbTools.setDB ( m_lastUser )  + "  WHERE PNR = " + m_pnr  ;
          return m_connection.executeSQL( SQL ) ;
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "UnitsRecord .get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
       return false ;
    }
 
 
    // Setter & Getter Methods 
     public void setTName(String  tName)
    {
       m_tName   =   tName;
    }
     public String getTName()
    {
       return m_tName;
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
 
 