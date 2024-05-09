/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.smart.records;

import com.carsplates.main.Main;
import com.carsplates.records.DocFilesRecord;
import com.myz.connection.ConnectionContext;
import com.myz.log.logWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Montazar Hamoud
 */
public class DocFiles extends DocFilesRecord
{
    //Constructor
    public DocFiles(ConnectionContext connection) 
    {
        super(connection);
    }
    //Class member
    public static final String DOC_FILES_BASE_DIR_NAME = "Images";
    //Methods
    public Vector<DocFiles> getVDocFiles(int pnrDeliveredInfo)
    {
        Vector <DocFiles> vDocFiles = new Vector();
        DocFiles  df  = null ;
        String    SQL = "";
        ResultSet rs  = null ;
        Statement st  = null ;
        
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            
            while(rs.next())
            {
                df = new DocFiles(m_connection);
                df.get(rs);
                vDocFiles.addElement(df);
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
        
        return vDocFiles;
    }
    
    
    public File checkOut()
    {
        File file = null ; 
        //TODO Determine storage type first
        
        file = new File(getFilePath());
        return file; 
    }
    
    public boolean checkIn(File file)
    {
        String serverPath = Main.SYS_CFG.getServerPath();
        String fileName   = file.getName();
        String fileExti   = file.getName().split("\\.")[1] ;
        long   millSec    = System.currentTimeMillis();
        File   dir        = new File(serverPath  + "/" + DOC_FILES_BASE_DIR_NAME);
        String filePath   = dir.getPath().replace("\\", "/") + "/" + millSec + "." + fileExti  ;
        
        setFileName(fileName);
        setFilePath(filePath);

        try
        {
            if(!dir.exists())
            {
                dir.mkdirs();
            }
            
            InputStream  is = new FileInputStream(file);
            OutputStream os = new FileOutputStream( filePath );
            int  length = 0 ;
            byte [] bytes = new byte [1024] ;
            
            while ((length = is.read(bytes)) != -1) 
            {
              os.write(bytes, 0, length);
            }
            
            if(is != null){is.close();}
            if(os != null){os.close();}
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
            return false ;
        }
        return true ;
    }
    
    public boolean deletePhysicalFile()
    {
        File file = new File(getFilePath());
        return file.delete();
    }
    public List<DocFiles> getUnBackupedDocFiles()
    {
        String     SQL   = "SELECT * FROM DOC_FILES WHERE BACKUPED != 1 ";
        Statement  st    = null ;
        ResultSet  rs    = null ;
        DocFiles   df    = null ;
        
        List<DocFiles> docFiles = new ArrayList<>() ;
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            while(rs.next())
            {
                df = new DocFiles(m_connection);
                df.get(rs);
                docFiles.add(df);
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
        
        return docFiles ;
    }
    
    public List<DocFiles> getAllDocFiles()
    {
        String     SQL   = "SELECT * FROM DOC_FILES ";
        Statement  st    = null ;
        ResultSet  rs    = null ;
        DocFiles   df    = null ;
        
        List<DocFiles> docFiles = new ArrayList<>() ;
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            while(rs.next())
            {
                df = new DocFiles(m_connection);
                df.get(rs);
                docFiles.add(df);
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
        
        return docFiles ;
    }
    
}
