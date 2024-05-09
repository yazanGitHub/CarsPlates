/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.frames;

import com.carsplates.main.Main;
import com.myz.component.myzButton;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 *
 * @author Montazar Hamoud
 */
public class MainFrame extends FramesParent
{
    
    //Constructor
    public MainFrame()
    {
        super("الصفحة الرئيسية ");
        setCenterOrientation(NodeOrientation.RIGHT_TO_LEFT);
        super.initFrame(false, false, false);
    }
    ScrollPane   m_scrollPane           = null ; 
    myzButton    m_addPlateButton       =  new myzButton("ادخال لوحة")
    {
        @Override
        public void buttonPressed()
        {
            Main.SCENE.setRoot(new CarsPlatesFrame( CarsPlatesFrame.MODE_ADD ));    
        }
    };
    myzButton    m_searchButton         = new myzButton("استعلام")
    {
      @Override
      public void buttonPressed()
      {
        Main.SCENE.setRoot(new CarsPlatesFrame( CarsPlatesFrame.MODE_SEARCH ));
      }
    };
    myzButton   m_printReceiptButton    = new myzButton("طباعة وصل استلام")
    {
      @Override
      public void buttonPressed()
      {
        Main.SCENE.setRoot(new PrintReceiptFrame() );
      }
    };
//    myzButton  m_extending_platesButton = new myzButton("تبدلات لوحة")
//    {
//      @Override
//      public void buttonPressed()
//      {
//        Main.SCENE.setRoot(new PlateOperationFrame() );
//      }
//    };
    myzButton  m_userManage             = new myzButton("إدارة المستخدمين ")
    {
      @Override
      public void buttonPressed()
      {
        Main.SCENE.setRoot(new UsersFrame());
      }
    };
    myzButton  m_backupButton           = new myzButton("النسخ الإحتياطي ")
    {
        @Override
        public void buttonPressed()
        {
            Main.SCENE.setRoot(new BackupFrame());
        }
    };
    myzButton  m_reportButton           = new myzButton("التقارير ")
    {
        @Override
        public void buttonPressed()
        {
            Main.SCENE.setRoot(new ReportFrame());
        }
    };
    myzButton  m_advanceSettingsButton  = new myzButton("إعدادات متقدمة ")
    {
        @Override
        public void buttonPressed()
        {
            Main.SCENE.setRoot(new AdvanceSettingsFrame());
        }
    };
    myzButton  m_notificationsButton    = new myzButton("الإخطارات ")
    {
        @Override
        public void buttonPressed()
        {
            Main.SCENE.setRoot(new NotificationsFrame());
        }
    };
    myzButton  m_logOutButton           = new myzButton()
    {
      @Override
      public void buttonPressed()
      {
        Main.USER.logOut();
      }
    };
    
    
    @Override
    public void initCenter()
    {
 

        
    }
    
    @Override
    public void initHeader()
    {
        m_logOutButton.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/logout.png") ) ) );
        Region region1  = new Region();
        Region region12 = new Region();
        
        HBox.setHgrow(region1, Priority.ALWAYS);
        HBox.setHgrow(region12, Priority.ALWAYS);
        
        getHeader().getChildren().add(0 , m_logOutButton);
        getHeader().getChildren().add(1 , region1);
        getHeader().getChildren().add(3 , region12);
        
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
        m_addPlateButton.setMinSize(150,50);
        m_addPlateButton.setParentPane(getCenterPane());
        m_addPlateButton.setReSizeOnParentSize(true);
                
        m_searchButton.setMinSize(150,50);
        m_searchButton.setParentPane(getCenterPane());
        m_searchButton.setReSizeOnParentSize(true);
        
        m_printReceiptButton.setMinSize(150,50);
        m_printReceiptButton.setParentPane(getCenterPane());
        m_printReceiptButton.setReSizeOnParentSize(true);
        
//        m_extending_platesButton.setMinSize(150,50);
//        m_extending_platesButton.setParentPane(getCenterPane());
//        m_extending_platesButton.setReSizeOnParentSize(true);
        
        m_userManage.setMinSize(150,50);
        m_userManage.setParentPane(getCenterPane());
        m_userManage.setReSizeOnParentSize(true);
        
        m_backupButton.setMinSize(150,50);
        m_backupButton.setParentPane(getCenterPane());
        m_backupButton.setReSizeOnParentSize(true);
        
        m_reportButton.setMinSize(150,50);
        m_reportButton.setParentPane(getCenterPane());
        m_reportButton.setReSizeOnParentSize(true);
        
        m_advanceSettingsButton.setMinSize(150,50);
        m_advanceSettingsButton.setParentPane(getCenterPane());
        m_advanceSettingsButton.setReSizeOnParentSize(true);
               
        m_notificationsButton.setMinSize(150,50);
        m_notificationsButton.setParentPane(getCenterPane());
        m_notificationsButton.setReSizeOnParentSize(true);
        
        m_addPlateButton.setDisable(!Main.USER.canAdd());
        m_searchButton.setDisable(!Main.USER.canSearch());
        m_printReceiptButton.setDisable(!Main.USER.canPrint());
        m_userManage.setDisable(!Main.USER.canManageUsers());
        m_backupButton.setDisable(!Main.USER.canMakeBackup());
        m_reportButton.setDisable(!Main.USER.canMakeReport());
//        m_extending_platesButton.setDisable(!Main.USER.canExtendingPlate());
        m_advanceSettingsButton.setDisable(!Main.USER.canMakeAdvanceSettings());
        m_notificationsButton.setDisable(!Main.USER.canDisplayNotifications());
        
        addToRight(m_addPlateButton);
        addToRight(m_searchButton);
//        addToRight(m_extending_platesButton);
        addToRight(m_printReceiptButton);
        addToRight(m_userManage);
        addToRight(m_backupButton);
        addToRight(m_reportButton);
        addToRight(m_advanceSettingsButton);
        addToRight(m_notificationsButton);
        
        getRightSideBar().setMaxSize(150 , 50);
    }

    @Override
    public void initFrameBasicData() 
    {
        //Do nothing
    }
}
