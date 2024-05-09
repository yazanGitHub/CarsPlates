/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.frames;

import com.carsplates.smart.records.User;
import com.myz.component.myzButton;
import com.myz.component.myzCheckBox;
import com.myz.component.myzComboBox;
import com.myz.component.myzComponent;
import com.myz.component.myzLabel;
import com.myz.component.myzPasswordField;
import com.myz.component.myzTextField;
import com.myz.connection.arConnectionInfo;
import com.myz.log.logWriter;
import com.myz.record.DbTools;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import myzMessage.myzMessage;

/**
 *
 * @author Montazar Hamoud
 */
public class UsersFrame extends FramesParent
{
    //Constructor
    public UsersFrame()
    {
        super("إدارة المستخدمين ");
        setCenterOrientation(NodeOrientation.RIGHT_TO_LEFT);
        super.initFrame(false, true, false);
    }
    
    //Class member
    public static final int ADD              = 1 ;
    public static final int SEARCH           = 2 ;
    public static final int PRINT            = 4 ;
    public static final int EXTENDING        = 8 ;
    public static final int MANAGE_USERS     = 16 ;
    public static final int BACKUP           = 32 ;
    public static final int REPORT           = 64 ;
    public static final int EDIT             = 128 ;
    public static final int ADVANCE_SETTINGS = 256 ;
    public static final int NOTIFICATIONS    = 512 ;
    
    //Data members
    User   m_user  = new User( arConnectionInfo.getConnectionContext() ); 
    Vector m_vUser = m_user.getUsers() ;
    
    /////////////////////////////////////////////////////////////
    //                          Center                         //
    /////////////////////////////////////////////////////////////
    myzLabel         m_userNameLabel   = new myzLabel("اسم المستخدم ");
    myzTextField     m_userName        = new myzTextField();
    myzLabel         m_passwordLabel   = new myzLabel("كلمة المرور ");
    myzPasswordField m_password        = new myzPasswordField();
    
    myzCheckBox      m_userEnable      = new myzCheckBox("الحساب مفعل ");
    myzCheckBox      m_add             = new myzCheckBox("ادخال ");
    myzCheckBox      m_search          = new myzCheckBox("استعلام ");
    myzCheckBox      m_print           = new myzCheckBox("طباعة وصل استلام ");
    myzCheckBox      m_extending       = new myzCheckBox("تمديد لوحة ");
    myzCheckBox      m_manageUsers     = new myzCheckBox("ادارة المستخدمين ");
    myzCheckBox      m_backup          = new myzCheckBox("النسخ الاحتياطي ");
    myzCheckBox      m_report          = new myzCheckBox("طباعة تقارير ");
    myzCheckBox      m_edit            = new myzCheckBox("تعديل ");
    myzCheckBox      m_advanceSettings = new myzCheckBox("إعدادات متقدمة ");
    myzCheckBox      m_notifications   = new myzCheckBox("الإخطارات ");
    
    //////////////////////////////////////////////////////////////////
    //                          Left sidebar                        // 
    //////////////////////////////////////////////////////////////////
    myzLabel     m_usersLabel   = new myzLabel("المستخدمين ");
    
    myzComboBox  m_users        = new myzComboBox("SELECT * FROM T_USER" , "USER_NAME" , false)
    {
        @Override
        public void selectionChange()
        {
            if(getItemValue() == null)
                return;
            
            int       userPnr  = getItemValue().getkey();
            String    SQL      = "SELECT * FROM T_USER WHERE PNR = " + DbTools.setDB(userPnr) ;
            Statement st       = null ;
            ResultSet rs       = null ;
            try
            {
                st = arConnectionInfo.getConnectionContext().m_connection.createStatement();
                rs = st.executeQuery(SQL);
                if(rs.next())
                {
                    m_user = new User(arConnectionInfo.getConnectionContext()) ;
                    m_user.get(rs);
                    load();
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
        }
        
    };
    
    myzButton    m_newButton    = new myzButton()
    {
        @Override
        public void buttonPressed()
        {
            //Remove frame values
            m_user  = new User( arConnectionInfo.getConnectionContext() ); 
            removeFrameData();
        }
        
    };
    
    myzButton    m_saveButton   = new myzButton()
    {
        @Override
        public void buttonPressed()
        {            
            String userName = m_userName.getText();
            String password = m_password.getText();
            
            if(userName != null && userName.length() < 1 || password != null && password.length() < 1 )
            {
                myzMessage.alertMessage("الرجاء التأكد من كلمة المرور و اسم المستخدم "  );
                return ;
            }
            if(!m_user.checkUserNameValid(userName))
            {
                myzMessage.alertMessage("اسم المستخدم موجود مسبقا"  );
                return ;
            }
            
            m_user.setUserName( userName );
            m_user.setUserAuth( String.valueOf( createUserAuth() )  );
            m_user.setUserEnable(m_userEnable.isSelected());
            //To prevent to save the encrypted password as new password on save 
            if(!(password).equals(m_user.getPassword()) )
                m_user.setPassword( password );
            
            if(m_user.save())
                myzMessage.noteMessage("تم حفظ معلومات المستخدم بنجاح");
            
            //Reload the saved record
            int pnr = m_user.getPnr() ;
            m_user.get(pnr);
            load();
            //Reload combobox data 
            m_users.refreshData();
        }
        
    };
    
    myzButton    m_cancelButton = new myzButton()
    {
        @Override
        public void buttonPressed()
        {
            m_user  = new User( arConnectionInfo.getConnectionContext() ); 
            removeFrameData();
        }
        
    };
    
    myzButton    m_deleteButton = new myzButton()
    {
        @Override
        public void buttonPressed()
        {
            if(!myzMessage.confirmMessage("هل تريد تأكيد حذف هذا المستخدم ؟"))
                return ;
            
            if(m_user.delete())
            {
                myzMessage.noteMessage("تم حذف هذا المستخدم بنجاح ");
                m_user  = new User( arConnectionInfo.getConnectionContext() ); 
                m_users.refreshData();
                removeFrameData();  
            }
            else
            {
                myzMessage.alertMessage("حدث خطأ ما لم يتم حذف هذا المستخدم");
            }
        }
    };
    

    
    
    //Methods    
    private void load ()
    {
        if(m_user == null)
            return;
        m_userName.setText(m_user.getUserName());
        m_password.setText(m_user.getPassword());
        
        m_add.setSelected( (Integer.parseInt(m_user.getUserAuth())             & ADD          ) > 0);
        m_search.setSelected( (Integer.parseInt(m_user.getUserAuth())          & SEARCH       ) > 0);
        m_print.setSelected( (Integer.parseInt(m_user.getUserAuth())           & PRINT        ) > 0);
        m_extending.setSelected( (Integer.parseInt(m_user.getUserAuth())       & EXTENDING    ) > 0);
        m_manageUsers.setSelected( (Integer.parseInt(m_user.getUserAuth())     & MANAGE_USERS ) > 0);
        m_backup.setSelected( (Integer.parseInt(m_user.getUserAuth())          & BACKUP       ) > 0);
        m_report.setSelected( (Integer.parseInt(m_user.getUserAuth())          & REPORT       ) > 0);
        m_edit.setSelected( (Integer.parseInt(m_user.getUserAuth())            & EDIT       ) > 0);
        m_advanceSettings.setSelected( (Integer.parseInt(m_user.getUserAuth()) & ADVANCE_SETTINGS) > 0);
        m_notifications.setSelected( (Integer.parseInt(m_user.getUserAuth()) & NOTIFICATIONS) > 0);
        m_userEnable.setSelected(m_user.getUserEnable());
    }
    
    public int createUserAuth()
    {
        int userAuth = 0 ;
        if(m_add.isSelected())
            userAuth += ADD ;
        if(m_search.isSelected())
            userAuth += SEARCH ;
        if(m_print.isSelected())
            userAuth += PRINT ;
        if(m_extending.isSelected())
            userAuth += EXTENDING ;
        if(m_manageUsers.isSelected())
            userAuth += MANAGE_USERS ;
        if(m_backup.isSelected())
            userAuth += BACKUP ;
        if(m_report.isSelected())
            userAuth += REPORT ;
        if(m_edit.isSelected())
            userAuth += EDIT ;
        if(m_advanceSettings.isSelected())
            userAuth += ADVANCE_SETTINGS ;
        if(m_notifications.isSelected())
            userAuth += NOTIFICATIONS ;
        
        return userAuth ;
    }
    
    public void removeFrameData()
    {
        m_user = new User(arConnectionInfo.getConnectionContext()) ; 
        try
        {
            Field []     fields   = UsersFrame.class.getDeclaredFields();
            myzComponent component = null ;
            for( Field field : fields)
            {
                Class className = field.getType() ;
                if ( Class.forName("com.myz.component.myzComponent").isAssignableFrom(className) )
                {
                    component = (myzComponent)field.get(this);
                    component.removeData();
                }
            }
        }
        catch(SecurityException | ClassNotFoundException | IllegalArgumentException | IllegalAccessException ex)
        {
            logWriter.write(ex);
        }
    }
    
    @Override
    public void initLeftSidebar ()
    {
        m_newButton.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/new.png") ) ) );
        m_saveButton.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/save.png") ) ) );
        m_cancelButton.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/cancel.png") ) ) );
        m_deleteButton.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/deleteUser.png") ) ) );
        
        getLeftSideBar().setAlignment(Pos.CENTER);
        
        addToLeft(m_usersLabel);
        addToLeft(m_users);
        addToLeft(m_newButton);
        addToLeft(m_saveButton);
        addToLeft(m_deleteButton);
        addToLeft(m_cancelButton);
    }

    @Override
    public void initCenter() 
    {
        getCenterPane().setHgap(10);
        getCenterPane().setVgap(10);
        
        int column = 0 ;
        int row    = 0 ;
        
        getCenterPane().add(m_userNameLabel , column++ , row);
        getCenterPane().add(m_userName      , column++ , row);
        getCenterPane().add(m_passwordLabel , column++ , row);
        getCenterPane().add(m_password      , column++ , row);
        
        column = 0 ; 
        row++ ; 
        
        getCenterPane().add(m_add             , column++ , row);
        getCenterPane().add(m_search          , column++ , row);
        
        column = 0 ; 
        row++ ;
        
        getCenterPane().add(m_print           , column++ , row);
        getCenterPane().add(m_extending       , column++ , row);
        
        column = 0 ; 
        row++ ;
        
        getCenterPane().add(m_manageUsers     , column++ , row);
        getCenterPane().add(m_backup          , column++ , row);

        column = 0 ; 
        row++ ;
        
        getCenterPane().add(m_report          , column++ , row);
        getCenterPane().add(m_edit            , column++ , row);
       
        column = 0 ; 
        row++ ;
        
        getCenterPane().add(m_advanceSettings , column++ , row);
        getCenterPane().add(m_notifications   , column++ , row);
        getCenterPane().add(m_userEnable      , column++ , row);
    }

    @Override
    public void initHeader() 
    {
        //Do nothing
    }

    @Override
    public void initFooter() 
    {
        //Do nothing
    }

    @Override
    public void initRightSidebar() 
    {
        //Do nothing
    }

    @Override
    public void initFrameBasicData() 
    {
        //Do nothing
    }
}
