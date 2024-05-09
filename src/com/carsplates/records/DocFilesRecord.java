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
 *    represents the table  DOC_FILES in database.                        
 * --------------------------------------------------------------------------------
 * @version 2.0                                                                    
 * 
 */
 
 public class DocFilesRecord extends Record
 {
 
    //Constructor 
    public DocFilesRecord ( ConnectionContext connection ) 
    {
       super( "DOC_FILES" , connection ) ;
       setLastUser(Main.USER.getUserName());
    }
 
 ///////////////////////////////////////////////////////////////////////
 //                          Data Members                             //
 ///////////////////////////////////////////////////////////////////////
    protected int        m_pnrDeliveredInfo;
    protected String     m_filePath;
    protected int        m_fileType;
    protected String     m_fileName;
    protected int        m_storageType;
    protected boolean    m_backuped;
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
          m_pnr                =   DbTools.getInt(  rs ,  "PNR"  );
          m_pnrDeliveredInfo   =   DbTools.getInt(  rs ,  "PNR_DELIVERED_INFO"  );
          m_filePath           =   DbTools.getString(  rs ,  "FILE_PATH"  );
          m_fileType           =   DbTools.getInt(  rs ,  "FILE_TYPE"  );
          m_fileName           =   DbTools.getString(  rs ,  "FILE_NAME"  );
          m_storageType        =   DbTools.getInt(  rs ,  "STORAGE_TYPE"  );
          m_backuped           =   DbTools.getBoolean(rs ,  "BACKUPED"  );
          m_lastTime           =   DbTools.getLong(  rs ,  "LAST_TIME"  );
          m_lastUser           =   DbTools.getString(  rs ,  "LAST_USER"  );
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "DocFilesRecord.get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
    }
 
 @Override
    public boolean insert ( int type )
    {
 
       try 
       {
          if ( m_pnr < 1 ) 
          {
             String SQL =  " INSERT INTO  DOC_FILES    ( PNR_DELIVERED_INFO , FILE_PATH , FILE_TYPE , FILE_NAME , STORAGE_TYPE , BACKUPED , LAST_TIME , LAST_USER  )  VALUES    (  " +  DbTools.setDB ( m_pnrDeliveredInfo  ) +   " ,  " +  DbTools.setDB ( m_filePath  ) +   " ,  " +  DbTools.setDB ( m_fileType  ) +   " ,  " +  DbTools.setDB ( m_fileName  ) +   " ,  " +  DbTools.setDB ( m_storageType  ) + " , " + DbTools.setDB(m_backuped)+   " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
             m_pnr = m_connection.executeSQL( SQL , ConnectionContext.MODE_LAST_ID);
             return m_pnr > 0 ;
       
          }
 
          else 
          {
             String SQL =  " INSERT INTO  DOC_FILES    ( PNR , PNR_DELIVERED_INFO , FILE_PATH , FILE_TYPE , FILE_NAME , STORAGE_TYPE , BACKUPED , LAST_TIME , LAST_USER  )  VALUES    ( " + DbTools.setDB ( m_pnr ) + " ,   " +  DbTools.setDB ( m_pnrDeliveredInfo  ) +   " ,  " +  DbTools.setDB ( m_filePath  ) +   " ,  " +  DbTools.setDB ( m_fileType  ) +   " ,  " +  DbTools.setDB ( m_fileName  ) +   " ,  " +  DbTools.setDB ( m_storageType  ) + " , " + DbTools.setDB( m_backuped )+   " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
 
            return m_connection.executeSQL( SQL);
          }
       }
 
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "DocFilesRecord .Insert( int type )" + ex.toString()); 
       }
       return false ;
    }
 @Override
    public boolean update( int type )
    {
 
       try 
       {
          String SQL =  " UPDATE   DOC_FILES  Set  "  +  " PNR_DELIVERED_INFO =  "  + DbTools.setDB ( m_pnrDeliveredInfo ) +  " , FILE_PATH =  "  + DbTools.setDB ( m_filePath ) +  " , FILE_TYPE =  "  + DbTools.setDB ( m_fileType ) +  " , FILE_NAME =  "  + DbTools.setDB ( m_fileName ) +  " , STORAGE_TYPE =  "  + DbTools.setDB ( m_storageType ) + " , BACKUPED = " + DbTools.setDB(m_backuped) +  " , LAST_TIME =  "  + DbTools.setDB ( m_lastTime ) +  " , LAST_USER =  "  + DbTools.setDB ( m_lastUser )  + "  WHERE PNR = " + m_pnr  ;
          return m_connection.executeSQL( SQL ) ;
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "DocFilesRecord .get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
       return false ;
    }
 
 
    // Setter & Getter Methods 
     public void setPnrDeliveredInfo(int  pnrDeliveredInfo)
    {
       m_pnrDeliveredInfo   =   pnrDeliveredInfo;
    }
     public int getPnrDeliveredInfo()
    {
       return m_pnrDeliveredInfo;
    }
 
     public void setFilePath(String  filePath)
    {
       m_filePath   =   filePath;
    }
     public String getFilePath()
    {
       return m_filePath;
    }
 
     public void setFileType(int  fileType)
    {
       m_fileType   =   fileType;
    }
     public int getFileType()
    {
       return m_fileType;
    }
 
     public void setFileName(String  fileName)
    {
       m_fileName   =   fileName;
    }
     public String getFileName()
    {
       return m_fileName;
    }
 
     public void setStorageType(int  storageType)
    {
       m_storageType   =   storageType;
    }
     public int getStorageType()
    {
       return m_storageType;
    }

    public void setBackuped(boolean backuped)
    {
        m_backuped = backuped ;
    }
    
    public boolean getBackuped()
    {
        return m_backuped ;
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
 
 