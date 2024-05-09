/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.frames;

import com.carsplates.main.Main;
import com.carsplates.smart.records.User;
import com.myz.component.myzButton;
import com.myz.component.myzLabel;
import com.myz.connection.arConnectionInfo;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 *
 * @author user
 */
public class LoginFrame extends FramesParent
{
    
    //Constructor
    public LoginFrame()
    {
        super("تسجيل الدخول ");
        setCenterOrientation(NodeOrientation.RIGHT_TO_LEFT);
        super.initFrame(false, false, false);
        //Set on enter key pressed login 
        setOnKeyPressed(e -> 
        {
            if (e.getCode() == KeyCode.ENTER)
            {
               String userName    = m_usrName.getText();
               String password    = m_password.getText();
               User   user        = new User(arConnectionInfo.getConnectionContext());
               int    loginStatus = user.logIn(userName , password);
                switch (loginStatus) 
                {
                    case User.LOGIN_STATUS_SUCCESS:
                        Main.SCENE.setRoot(new MainFrame());
                        break;
                    case User.LOGIN_STATUS_ERROR:
                        m_message.setText("خطأ في اسم المستخدم أو كلمة المرور غير صحيحة ");
                        m_message.setTextFill(Color.RED);
                        break;
                    case User.LOGIN_STATUS_DISABLE:
                        m_message.setText("تم قفل حسابك الرجاء مراجة مدير النظام");
                        m_message.setTextFill(Color.RED);
                        break;
                    default:
                        break;
                }
            }
        });
        
    }
    
    
    //Data member
    /////////////////////////////////////////////////////////
    //                  Center                             //
    ////////////////////////////////////////////////////////
    myzLabel      m_userNameLabel = new myzLabel("اسم المستخدم ");
    TextField     m_usrName       = new TextField();
    myzLabel      m_passwordLabel = new myzLabel("كلمة المرور ");
    PasswordField m_password      = new PasswordField();
    myzButton     m_loginButton   = new myzButton("تسجيل دخول ");
    myzLabel      m_message       = new myzLabel();
    


    
    
    @Override
    public void initCenter()
    {     
        m_loginButton.setId("btnLogin");
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        getCenterPane().setEffect(r);
        getCenterPane().setPadding(new Insets(20,20,20,20));
        getCenterPane().setHgap(5);
        getCenterPane().setVgap(5);
        getCenterPane().setAlignment(Pos.CENTER);
        
        int column = 0 ; 
        int row    = 0 ;

        //Adding Nodes to GridPane layout
        getCenterPane().add(m_userNameLabel  , column++ , row);
        getCenterPane().add(m_usrName        , column++ , row);
        
        column = 0 ;
        row++ ;
        
        getCenterPane().add(m_passwordLabel  , column++ , row);
        getCenterPane().add(m_password       , column++ , row);
        
        column = 1 ;
        row++ ;
        
        getCenterPane().add(m_loginButton    , column , row++);
        
        column = 0 ;
        row++ ;
        
        getCenterPane().add(m_message, column, row, 4, 4);

        //Action for btnLogin
        m_loginButton.setOnAction(new EventHandler() 
        {
            @Override
            public void handle(Event event) 
            {
               String userName    = m_usrName.getText();
               String password    = m_password.getText();
               User   user        = new User(arConnectionInfo.getConnectionContext());
               int    loginStatus = user.logIn(userName , password);
                switch (loginStatus)
                {
                    case User.LOGIN_STATUS_SUCCESS:
                        Main.USER = user ;
                        Main.SCENE.setRoot(new MainFrame());
                        break;
                    case User.LOGIN_STATUS_ERROR:
                        m_message.setText("خطأ في اسم المستخدم أو كلمة المرور غير صحيحة ");
                        m_message.setTextFill(Color.RED);
                        break;
                    case User.LOGIN_STATUS_DISABLE:
                        m_message.setText("تم قفل حسابك الرجاء مراجة مدير النظام");
                        m_message.setTextFill(Color.RED);
                        break;
                    default:
                        break;
                }
            }
        });

        
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
    public void initLeftSidebar() 
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
