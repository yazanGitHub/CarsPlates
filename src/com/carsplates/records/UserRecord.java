package com.carsplates.records ;

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
 *    represents the table  T_USER in database.                        
 * --------------------------------------------------------------------------------
 * @version 2.0                                                                    
 * 
 */
 
 public class UserRecord extends Record
 {

    public UserRecord(String tableName, ConnectionContext connection) 
    {
        super(tableName, connection);
    }
 
    //Constructor 
    public UserRecord (  ConnectionContext connection ) 
    {
       super( "T_USER" , connection ) ;
    }
 
 ///////////////////////////////////////////////////////////////////////
 //                          Data Members                             //
 ///////////////////////////////////////////////////////////////////////
    protected String     m_tName;
    protected String     m_userName;
    protected String     m_password;
    protected String     m_userAuth;
    protected long       m_lastTime;
    protected String     m_lastUser;
    protected String     m_salt    ;
    protected boolean    m_enable ;
 
 
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
          m_userName   =   DbTools.getString(  rs ,  "USER_NAME"  );
          m_password   =   DbTools.getString(  rs ,  "PASSWORD"  );
          m_userAuth   =   DbTools.getString(  rs ,  "USER_AUTH"  );
          m_lastTime   =   DbTools.getLong(  rs ,  "LAST_TIME"  );
          m_lastUser   =   DbTools.getString(  rs ,  "LAST_USER"  );
          m_salt       =   DbTools.getString(  rs ,  "SALT"  );
          m_enable     =   DbTools.getBoolean(rs ,  "ENABLE"  );
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "UserRecord.get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
    }
 
 @Override
    public boolean insert ( int type )
    {
 
       try 
       {
          if ( m_pnr < 1 ) 
          {
             String SQL =  " INSERT INTO  T_USER    (  T_NAME , USER_NAME , PASSWORD , SALT , USER_AUTH , ENABLE , LAST_TIME , LAST_USER  )  VALUES    (  " +  DbTools.setDB ( m_tName  ) +   " ,  " +  DbTools.setDB ( m_userName  ) +   " ,  " +  DbTools.setDB ( m_password  ) +   " ,  " + DbTools.setDB( m_salt ) +   " ,  " + DbTools.setDB ( m_userAuth  ) +   " ,  " + DbTools.setDB(m_enable) + " , "+  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
             m_pnr      = m_connection.executeSQL( SQL , ConnectionContext.MODE_LAST_ID);
             return m_pnr >= 1 ;
          }
 
          else 
          {
             String SQL =  " INSERT INTO  T_USER    (  PNR , T_NAME , USER_NAME , PASSWORD , SALT , USER_AUTH , ENABLE , LAST_TIME , LAST_USER  )  VALUES    (  " +  DbTools.setDB ( m_pnr  ) +   " ,  " +  DbTools.setDB ( m_tName  ) +   " ,  " +  DbTools.setDB ( m_userName  ) +   " ,  " +  DbTools.setDB ( m_password  ) + DbTools.setDB( m_salt ) +   " ,  " +  " ,  " +  DbTools.setDB ( m_userAuth  ) +   " ,  " + DbTools.setDB(m_enable ) + " , "+  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
 
             return m_connection.executeSQL( SQL);
          }
       }
 
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "UserRecord .Insert( int type )" + ex.toString()); 
       }
       return false ;
    }
 @Override
    public boolean update( int type )
    {
 
       try 
       {
          String SQL =  " UPDATE   T_USER  Set  "  + "  T_NAME =  "  + DbTools.setDB ( m_tName ) +  " , USER_NAME =  "  + DbTools.setDB ( m_userName ) +  " , PASSWORD =  "  + DbTools.setDB ( m_password ) + " , SALT = " + DbTools.setDB( m_salt ) +   " , USER_AUTH =  "  + DbTools.setDB ( m_userAuth ) + " , ENABLE = " + DbTools.setDB(m_enable) +   " , LAST_TIME =  "  + DbTools.setDB ( m_lastTime ) +  " , LAST_USER =  "  + DbTools.setDB ( m_lastUser ) + " WHERE PNR = " + DbTools.setDB( m_pnr );
          return m_connection.executeSQL( SQL ) ;
          
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "UserRecord.get ( ResultSet rs , int type )" + ex.toString()); 
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
 
     public void setUserName(String  userName)
    {
       m_userName   =   userName;
    }
    @Override
     public String getUserName()
    {
       return m_userName;
    }
 
     public void setPassword(String  password)
    {
       m_password   =   password;
    }
     public String getPassword()
    {
       return m_password;
    }
 
     public void setUserAuth(String  userAuth)
    {
       m_userAuth   =   userAuth;
    }
     public String getUserAuth()
    {
       return m_userAuth;
    }
     
     public void setUserEnable(boolean enable)
     {
         m_enable = enable ;
     }
     public boolean getUserEnable()
     {
         return m_enable ;
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
     
     public void setSalt(String salt)
     {
         m_salt = salt ;
     }
     public String getSalt()
     {
         return m_salt ;
     }
 
 
 }
 
 