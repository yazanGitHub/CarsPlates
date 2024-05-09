/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.smart.records;

import com.carsplates.frames.LoginFrame;
import static com.carsplates.frames.UsersFrame.ADD;
import static com.carsplates.frames.UsersFrame.ADVANCE_SETTINGS;
import static com.carsplates.frames.UsersFrame.BACKUP;
import static com.carsplates.frames.UsersFrame.EDIT;
import static com.carsplates.frames.UsersFrame.EXTENDING;
import static com.carsplates.frames.UsersFrame.MANAGE_USERS;
import static com.carsplates.frames.UsersFrame.NOTIFICATIONS;
import static com.carsplates.frames.UsersFrame.PRINT;
import static com.carsplates.frames.UsersFrame.REPORT;
import static com.carsplates.frames.UsersFrame.SEARCH;
import com.carsplates.main.Main;
import com.carsplates.records.UserRecord;
import com.carsplates.tools.PasswordUtils;
import static com.carsplates.tools.PasswordUtils.verifyUserPassword;
import com.myz.connection.ConnectionContext;
import com.myz.log.logWriter;
import com.myz.record.DbTools;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Montazar Hamoud
 */
public class User extends UserRecord 
{
    //Constructor
    public User(ConnectionContext connection) 
    {
        super(connection);
    }
    
    //Class member
    public static final int    LOGIN_STATUS_SUCCESS = 1 ;
    public static final int    LOGIN_STATUS_ERROR   = 2 ;
    public static final int    LOGIN_STATUS_DISABLE = 3 ;
    public static final String SYSTEM_ADMIN         = "ADMIN";
    //TODO save it at database 
    public static int LOGIN_ERROR_COUNT    = 0 ;
    public static int LOGIN_MAX_TRY        = 3 ;
    
    
    //Overrid setPassword to encrypt the user password 
    @Override
    public void setPassword(String password)
    {
        // Generate Salt. The generated value can be stored in DB. 
        String salt             = PasswordUtils.getSalt(10);
        // Protect user's password. The generated value can be stored in DB.
        String mySecurePassword = PasswordUtils.generateSecurePassword(password, salt);
        m_password              = mySecurePassword ;
        m_salt                  = salt ;
    }


    
    public boolean checkUserNameValid(String userName)
    {
        String    SQL  = "SELECT * FROM T_USER WHERE USER_NAME = " + DbTools.setDB(userName);
                  SQL += " AND PNR != " + DbTools.setDB(m_pnr);
        Statement st  = null ;
        ResultSet rs  = null ;
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if(rs.next())
                return false;
            
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
        return true ;
    }
    
    public Vector<User> getUsers()
    {
        Vector<User> vUser = new Vector();
        String    SQL  = "SELECT * FROM T_USER " ;
        Statement st   = null ;
        ResultSet rs   = null ;
        User      user = null ;
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            while(rs.next())
            {
                user = new User(m_connection) ;
                user.get(rs);
                vUser.addElement(user);
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
        return vUser ;
    }

    public int logIn(String userName , String password)
    {
        String    SQL    = "SELECT * FROM T_USER WHERE USER_NAME = " + DbTools.setDB(userName);
        Statement st     = null ;
        ResultSet rs     = null ;
        //In case system admin do not lock the user
        if(userName.equalsIgnoreCase(SYSTEM_ADMIN)){LOGIN_ERROR_COUNT = 0 ;}
        
        int       status = LOGIN_STATUS_ERROR ;
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if(rs.next())
            {
                get(rs);
                if(verifyUserPassword(password , getPassword() ,  getSalt() ) )
                {
                    if(getUserEnable())
                    {
                        status = LOGIN_STATUS_SUCCESS;
                        Main.USER = this ;
                    }
                    else
                        status = LOGIN_STATUS_DISABLE;     
                }
                else
                {
                    if(LOGIN_ERROR_COUNT < LOGIN_MAX_TRY)
                        LOGIN_ERROR_COUNT++ ;
                    else
                    {
                        setUserEnable(false);
                        save();
                        status = LOGIN_STATUS_DISABLE;
                    }
                }
                return status ;
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
        
        return status ;
    }
    
    public void logOut()
    {
        Main.SCENE.setRoot(new LoginFrame() );
    }
    
    public boolean canAdd()
    {
        return ( Integer.parseInt( getUserAuth() ) & ADD) > 0  ;
    }
    public boolean canSearch()
    {
        return ( Integer.parseInt( getUserAuth() ) & SEARCH) > 0 ;
    }
    public boolean canPrint()
    {
        return ( Integer.parseInt( getUserAuth() ) & PRINT) > 0 ;
    }
    public boolean canExtendingPlate()
    {
        return ( Integer.parseInt( getUserAuth() ) & EXTENDING) > 0 ;
    }
    public boolean canManageUsers()
    {
        return ( Integer.parseInt( getUserAuth() ) & MANAGE_USERS) > 0 ;
    }
    public boolean canMakeBackup()
    {
        return ( Integer.parseInt( getUserAuth() ) & BACKUP) > 0 ;
    }
    public boolean canMakeReport()
    {
        return ( Integer.parseInt( getUserAuth() ) & REPORT) > 0 ;
    }
    public boolean canEdit()
    {
        return ( Integer.parseInt( getUserAuth() ) & EDIT) > 0 ;
    }
    public boolean canMakeAdvanceSettings()
    {
        return ( Integer.parseInt( getUserAuth() ) & ADVANCE_SETTINGS) > 0 ;
    }
    public boolean canDisplayNotifications()
    {
        return ( Integer.parseInt( getUserAuth()) & NOTIFICATIONS) > 0 ;
    }
}
