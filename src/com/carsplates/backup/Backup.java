/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.backup;

import com.carsplates.smart.records.DocFiles;
import com.carsplates.smart.records.Syscfg;
import com.myz.connection.ConnectionContext;
import com.myz.connection.arConnectionInfo;
import com.myz.log.logWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import myzTools.Tools;


/**
 *
 * @author Montazar Hamoud
 */
public class Backup 
{
    
    //Constructor
    public Backup()
    {
        m_connection = arConnectionInfo.getConnectionContext();
    }
    
    //Members
    ConnectionContext m_connection ; 
    
    //Methods    
    public boolean sqlBackup(File file)
    {
        Properties connectionProperties = new Properties();
        try 
        {
            connectionProperties.load( new FileInputStream( "connection.Properties" ) );
        } 
        catch (IOException e) 
        {
            try
            {
                //Try to find this file inside my JAR 
                connectionProperties.load( this.getClass().getResourceAsStream("/" + "connection.Properties") );
            }
            catch(Exception ex)
            {
                logWriter.write(e);
                logWriter.write(ex);
            }
        }

        String    SQL     = "SELECT @@basedir ";
        String    baseDir = "";
        ResultSet rs      = null ;
        Statement st      = null ;
        Process   p       = null;
        
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            while(rs.next())
            {
                baseDir = rs.getString(1);
            }
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }
        
        String  user       = connectionProperties.getProperty( "user" );
        String  password   = connectionProperties.getProperty( "password" );
        String  database   = connectionProperties.getProperty( "database" );
        
        String  toolDir    = baseDir + "bin\\mysqldump";
        //User backup path
        String  backupPath = file.getPath() ;
        String  exec       = toolDir ;
                exec      += " -u" + user ;
                exec      += " -p" + password ; 
                exec      += " --add-drop-database -B " + database ;  
                exec      += " -r " + "\"" + backupPath + "\""  ;
        Runtime runtime = Runtime.getRuntime();
        try 
        {
            System.out.println(exec);
            p = runtime.exec(exec);
            int processComplete = p.waitFor();

            return processComplete == 0;

        }
        catch (IOException | InterruptedException ex) 
        {
            logWriter.write(ex);
        }
        return false ;
    }
    
    public boolean fullBackup(File zipFile)
    {
        File           sqlBackupFile = new File(zipFile.getParent() + File.separator + new Date(System.currentTimeMillis()) + ".sql");        
        DocFiles       df            = new DocFiles(m_connection);
        List<File>     files         = new ArrayList<>() ;
        List<DocFiles> docFiles      = df.getAllDocFiles();
        
        //If sql backup failed
        if(!sqlBackup(sqlBackupFile))
        {
            return false;
        }
        else
        {
            files.add(sqlBackupFile);
        }
        //IF no docFiles to backup then sql backup is enough
        if(docFiles.isEmpty() )
        {
            File zip = Tools.zip(files , zipFile);
            sqlBackupFile.delete();
            return zip != null;
        }
        
        docFiles.stream().forEach((docFile) -> 
        {
            files.add(docFile.checkOut());
        });
        
        File zip = Tools.zip(files , zipFile);
        sqlBackupFile.delete();

        return zip != null ;
    }
    
    
    public boolean scheduledBuckup()
    {
        Syscfg syscfg  = new Syscfg(m_connection);
        syscfg.get(1);

        String         backupPath    = syscfg.getBackupPath();
        File           sqlBackupFile = new File(backupPath + File.separator + new Date(System.currentTimeMillis()) + ".sql");        
        File           zipFile       = new File(backupPath + File.separator + new Date(System.currentTimeMillis()) + ".zip"); 
        DocFiles       df            = new DocFiles(m_connection);
        List<File>     files         = new ArrayList<>() ;
        List<DocFiles> docFiles      = df.getUnBackupedDocFiles();
        
        //If sql backup failed
        if(!sqlBackup(sqlBackupFile))
        {
            return false;
        }
        else
        {
            files.add(sqlBackupFile);
        }
        //IF no docFiles to backup then sql backup is enough
        if(docFiles.isEmpty() )
        {
            File zip = Tools.zip(files , zipFile);
            sqlBackupFile.delete();
            return zip != null;
        }
        
        docFiles.stream().forEach((docFile) -> 
        {
            files.add(docFile.checkOut());
        });
        
        File zip = Tools.zip(files , zipFile);
        sqlBackupFile.delete();

        if(zip != null )
        {
            docFiles.stream().forEach((docFile) -> 
            {
                docFile.setBackuped(true);
                docFile.save();
            });
            return true ;
        }
        else
            return false;
    }
    
    
    public static void main (String [] args)
    {
        
    }
    

    
    
    
}
