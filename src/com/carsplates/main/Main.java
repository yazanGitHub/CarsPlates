/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.main;

import com.carsplates.frames.LoginFrame;
import com.carsplates.smart.records.Syscfg;
import com.carsplates.smart.records.User;
import com.myz.component.myzScene;
import com.myz.connection.ConnectionContext;
import com.myz.connection.arConnectionInfo;
import com.myz.log.logWriter;
import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Vector;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import myzMessage.myzMessage;


/**
 *
 * @author Montazar Hamoud
 */
public class Main extends Application 
{
    //Class members
    public static myzScene  SCENE ;
    public static User      USER ;
    public static Stage     PRIMARY_STAGE  ;  
    public static Syscfg    SYS_CFG ;
     
    
    //Data members
    LoginFrame m_loginFrame   = new LoginFrame();    

    
    @Override
    public void start(Stage primaryStage) throws AWTException 
    {
        //Init system configration 
//        SYS_CFG = new Syscfg(arConnectionInfo.getConnectionContext() , "System");
//        SYS_CFG.get(1);
        //Open log file 
        //logWriter.open(SYS_CFG.getServerPath() + "/" + logWriter.LOG_BASE_DIR_NAME , "" , "");

        //Set scene dimensions depend on user device
        GraphicsDevice gd     = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int            width  = gd.getDisplayMode().getWidth();
        int            height = gd.getDisplayMode().getHeight();
        SCENE          = new myzScene(m_loginFrame , width, height);
        //Load css file 
        SCENE.getStylesheets().add(getClass().getClassLoader().getResource("Main.css").toExternalForm());
        
        
        PRIMARY_STAGE = primaryStage;
        
        PRIMARY_STAGE.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logo.png")));
        PRIMARY_STAGE.setMaximized(true);
        PRIMARY_STAGE.setTitle("إدارة لوحات المركبات");
        PRIMARY_STAGE.setScene(SCENE);
        PRIMARY_STAGE.show();
        PRIMARY_STAGE.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent widowEvent) 
            {    
                closeMe(PRIMARY_STAGE , widowEvent );
            }
        });
        
    }

    
    public void closeMe(Stage primaryStage , Event windoEvent )
    {        
        boolean answer = myzMessage.confirmMessage("هل تريد إغلاق البرنامج ؟ " );
        if (answer)
        {
            primaryStage.close();
        }
        else
            windoEvent.consume();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        launch(args);
    }
    
}
