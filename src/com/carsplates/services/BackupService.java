/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.services;


import com.carsplates.backup.Backup;
import com.carsplates.smart.records.User;
import com.myz.connection.ConnectionContext;
import com.myz.connection.arConnectionInfo;
import com.myz.log.logWriter;
import java.util.Calendar;


/**
 *
 * @author Montazar Hamoud
 */
public class BackupService extends Thread
{
    //Class member
    public static final int HOUR_OF_BAKCUP = 6 ;
    
    //Methods
    @Override
    public void run()
    {
        User user = new User(arConnectionInfo.getConnectionContext());
        user.logIn("admin", "admin");
        
        while(true)
        {
            Calendar instance = Calendar.getInstance();
            int       hour    = instance.get(Calendar.HOUR_OF_DAY);

            if(hour == HOUR_OF_BAKCUP)
            {
                //Do backup
                Backup backup = new Backup();
                if( backup.scheduledBuckup() )
                {
                    System.out.println("backup done");
                }
            }
            try
            {
                sleep(1000 * 60 * 60 * 4);
            }
            catch(Exception ex)
            {
                logWriter.write(ex);
            }
            
        }
    }
    
    public static void main(String [] args)
    {
        BackupService backupService = new BackupService();
        backupService.start();
    } 
   
}
