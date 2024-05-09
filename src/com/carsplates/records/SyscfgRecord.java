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
 * --------------------------------------------------------------------------------
 *    represents the table  SYSCFG in database.                        
 * --------------------------------------------------------------------------------
 * @version 2.0                                                                    
 * 
 */
 
 public class SyscfgRecord extends Record
 {
 
    //Constructor 
    public SyscfgRecord (  ConnectionContext connection ) 
    {
       super( "SYSCFG" , connection ) ;
       setLastUser(Main.USER.getUserName());
    }
    public SyscfgRecord ( ConnectionContext connection , String lastUser)
    {
       super( "SYSCFG" , connection ) ;
       setLastUser(lastUser);
    }
 ///////////////////////////////////////////////////////////////////////
 //                          Data Members                             //
 ///////////////////////////////////////////////////////////////////////
    protected String    m_backupPath;
    protected String    m_serverIp;
    protected String    m_serverPath;
    protected String    m_ftpUser;
    protected String    m_ftpPassword;
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
          m_pnr          =   DbTools.getInt(  rs ,  "PNR"  );
          m_backupPath   =   DbTools.getString(  rs ,  "BACKUP_PATH"  );
          m_serverIp     =   DbTools.getString(  rs ,  "SERVER_IP"  );
          m_serverPath   =   DbTools.getString(  rs ,  "SERVER_PATH"  );
          m_ftpUser      =   DbTools.getString(  rs ,  "FTP_USER"  );
          m_ftpPassword  =   DbTools.getString(  rs ,  "FTP_PASSWORD"  );
          m_lastTime     =   DbTools.getLong(  rs ,  "LAST_TIME"  );
          m_lastUser     =   DbTools.getString(  rs ,  "LAST_USER"  );
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "SyscfgRecord.get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
    }
 
 @Override
    public boolean insert ( int type )
    {
 
       try 
       {
          if ( m_pnr < 1 ) 
          {
             String SQL =  " INSERT INTO  SYSCFG    ( BACKUP_PATH , SERVER_IP , SERVER_PATH , FTP_USER , FTP_PASSWORD , LAST_TIME , LAST_USER  )  VALUES    (  " +  DbTools.setDB ( m_backupPath  ) +   " ,  " +  DbTools.setDB ( m_serverIp  ) +   " ,  " +  DbTools.setDB ( m_serverPath  ) +   " ,  " +  DbTools.setDB ( m_ftpUser  ) +   " ,  " +  DbTools.setDB ( m_ftpPassword  ) +   " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
             m_pnr = m_connection.executeSQL( SQL , ConnectionContext.MODE_LAST_ID);
             return m_pnr >= 1 ;
       
          }
 
          else 
          {
             String SQL =  " INSERT INTO  SYSCFG    ( PNR , BACKUP_PATH , SERVER_IP , SERVER_PATH , FTP_USER , FTP_PASSWORD , LAST_TIME , LAST_USER  )  VALUES    ( " + DbTools.setDB ( m_pnr ) + " ,   " +  DbTools.setDB ( m_backupPath  ) +   " ,  " +  DbTools.setDB ( m_serverIp  ) +   " ,  " +  DbTools.setDB ( m_serverPath  ) +   " ,  " +  DbTools.setDB ( m_ftpUser  ) +   " ,  " +  DbTools.setDB ( m_ftpPassword  ) +   " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
 
             return m_connection.executeSQL( SQL);
          }
       }
 
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "SyscfgRecord .Insert( int type )" + ex.toString()); 
       }
       return false ;
    }
 @Override
    public boolean update( int type )
    {
 
       try 
       {
          String SQL =  " UPDATE   SYSCFG  Set  "  +  " BACKUP_PATH =  "  + DbTools.setDB ( m_backupPath ) +  " , SERVER_IP =  "  + DbTools.setDB ( m_serverIp ) +  " , SERVER_PATH =  "  + DbTools.setDB ( m_serverPath ) +  " , FTP_USER =  "  + DbTools.setDB ( m_ftpUser ) +  " , FTP_PASSWORD =  "  + DbTools.setDB ( m_ftpPassword ) +  " , LAST_TIME =  "  + DbTools.setDB ( m_lastTime ) +  " , LAST_USER =  "  + DbTools.setDB ( m_lastUser )  + "  WHERE PNR = " + m_pnr  ;
          return m_connection.executeSQL( SQL ) ;
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "SyscfgRecord .get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
       return false ;
    }
 
 
    // Setter & Getter Methods 
     public void setBackupPath(String  backupPath)
    {
       m_backupPath   =   backupPath;
    }
     public String getBackupPath()
    {
       return m_backupPath;
    }
 
     public void setServerIp(String  serverIp)
    {
       m_serverIp   =   serverIp;
    }
     public String getServerIp()
    {
       return m_serverIp;
    }
 
     public void setServerPath(String  serverPath)
    {
       m_serverPath   =   serverPath;
    }
     public String getServerPath()
    {
       return m_serverPath;
    }
 
     public void setFtpUser(String  ftpUser)
    {
       m_ftpUser   =   ftpUser;
    }
     public String getFtpUser()
    {
       return m_ftpUser;
    }
 
     public void setFtpPassword(String  ftpPassword)
    {
       m_ftpPassword   =   ftpPassword;
    }
     public String getFtpPassword()
    {
       return m_ftpPassword;
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
 
 