/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.frames;

import com.carsplates.main.Main;
import com.carsplates.smart.records.Syscfg;
import com.carsplates.smart.records.Units;
import com.carsplates.smart.records.VehicleColor;
import com.carsplates.smart.records.VehicleManufacturer;
import com.carsplates.tables.UnitsTable;
import com.carsplates.tables.VehicleColorTable;
import com.carsplates.tables.VehicleManufacturerTable;
import com.myz.component.myzButton;
import com.myz.component.myzLabel;
import com.myz.component.myzTextField;
import com.myz.connection.ConnectionContext;
import com.myz.connection.arConnectionInfo;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Montazar Hamoud
 */
public class AdvanceSettingsFrame extends FramesParent 
{
    
    public AdvanceSettingsFrame() 
    {
        super("إعدادات متقدمة ");
        setCenterOrientation(NodeOrientation.RIGHT_TO_LEFT);
        super.initFrame(false, true, false);
    }
    
    //Data members
    ////////////////////////////////////////////////////////////////////////////
    //                                 CENTER                                 // 
    ////////////////////////////////////////////////////////////////////////////
    TabPane m_tabPane          = new TabPane();
    Tab     m_settingsTab      = new Tab("إعدادت عامة ");
    Tab     m_colorTab         = new Tab("ألوان الاليات ");
    Tab     m_manufacturerTab  = new Tab("شركات الاليات ");
    Tab     m_unitTab          = new Tab("الوحدات المستلمة ");
    
    //Tables
    VehicleColorTable        m_vehicleColorTable        = new VehicleColorTable();
    VehicleManufacturerTable m_vehicleManufacturerTable = new VehicleManufacturerTable();
    UnitsTable               m_unitsTable               = new UnitsTable();
            
     
    /*_______________________At color tab_____________________________________*/
    myzButton    m_addColorButton          = new myzButton("إضافة")
    {
      @Override
      public void buttonPressed()
      {
          String colorName = m_colorNameField.getText() ;
          m_colorNameField.clear();
          if(colorName != null && !"".equals(colorName))
          {
              VehicleColor color = new VehicleColor(arConnectionInfo.getConnectionContext());
              color.setTName(colorName);
              color.save();
              
              m_vehicleColorTable.reload();
          }
      }
    };
    
    myzTextField m_colorNameField          = new myzTextField();
    
    
    /*_______________________At Manufacturer tab______________________________*/
    myzButton    m_addManufacturerButton   = new myzButton("إضافة")
    {
      @Override
      public void buttonPressed()
      {
          String manufacturerName = m_colorManufacturerField.getText() ;
          m_colorManufacturerField.clear();
          if(manufacturerName != null && !"".equals(manufacturerName))
          {
              VehicleManufacturer vehicleManufacturer = new VehicleManufacturer(arConnectionInfo.getConnectionContext());
              vehicleManufacturer.setTName(manufacturerName);
              vehicleManufacturer.save();
              
              m_vehicleManufacturerTable.reload();
          }
      }
    };
    
    myzTextField m_colorManufacturerField  = new myzTextField();
    
    /*_______________________At Unit tab______________________________________*/
    myzButton    m_addUnitButton           = new myzButton("إضافة")
    {
      @Override
      public void buttonPressed()
      {
          String manufacturerName = m_UnitField.getText() ;
          m_UnitField.clear();
          if(manufacturerName != null && !"".equals(manufacturerName))
          {
              Units unit = new Units(arConnectionInfo.getConnectionContext());
              unit.setTName(manufacturerName);
              unit.save();
              
              m_unitsTable.reload();
          }
      }
    };
    
    myzTextField m_UnitField               = new myzTextField();

    
    ////////////////////////////////////////////////////////////////////////////
    //                          LEFT SIDEBAR                                  // 
    ////////////////////////////////////////////////////////////////////////////
        
    public void initSettingsTab()
    {
        ConnectionContext connection  = arConnectionInfo.getConnectionContext();
        Syscfg            syscfg      = new Syscfg(connection);
        syscfg.get(1);
        
        myzLabel     serverIpLabel    = new myzLabel("IP          ");
        myzTextField serverIp         = new myzTextField();
        myzLabel     serverPathLabel  = new myzLabel("مسار الخادم ");
        myzTextField serverPath       = new myzTextField();
        myzLabel     FTPUserLabel     = new myzLabel("FTP User    ");
        myzTextField FTPUser          = new myzTextField();
        myzLabel     FTPPasswordLabel = new myzLabel("FTP Password");
        myzTextField FTPPassword      = new myzTextField();
        myzLabel     backupPathLabel  = new myzLabel("مسار النسخ الإحتياطي");
        myzTextField backupPath       = new myzTextField();
        backupPath.setMinWidth(200);
        
        myzButton    saveButton       = new myzButton("حفظ ")
        {
            @Override
            public void buttonPressed()
            {
                syscfg.setServerIp(serverIp.getText());
                syscfg.setServerPath(serverPath.getText());
                syscfg.setFtpUser(FTPUser.getText());
                syscfg.setFtpPassword(FTPPassword.getText());
                syscfg.setBackupPath(backupPath.getText());
                syscfg.save();
            }
        };
        
        //Set Field data
        serverIp.setText(syscfg.getServerIp());
        serverPath.setText(syscfg.getServerPath());
        FTPUser.setText(syscfg.getFtpUser());
        FTPPassword.setText(syscfg.getFtpPassword());
        backupPath.setText(syscfg.getBackupPath());
       
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        int      row      = 0 ;

        gridPane.addRow(row++ , serverIp    , serverIpLabel , serverPath  , serverPathLabel);
        gridPane.addRow(row++ , FTPUser     , FTPUserLabel  , FTPPassword , FTPPasswordLabel);
        gridPane.addRow(row++ , backupPath  , backupPathLabel );
        gridPane.addRow(row++ , saveButton);
        
        
        m_settingsTab.setContent(gridPane);
        
    }
    
    @Override
    public void initCenter()
    {
        setCenter(m_tabPane);
        ((TabPane) getCenter()).getTabs().addAll(m_settingsTab , m_colorTab , m_unitTab , m_manufacturerTab);
        
        //Add tables for each tab 
        VBox colorPane = new VBox();
        colorPane.getChildren().addAll(m_vehicleColorTable , m_addColorButton , m_colorNameField  );
        m_colorTab.setContent(colorPane);
       
        VBox manufacturerPane = new VBox();
        manufacturerPane.getChildren().addAll(m_vehicleManufacturerTable , m_addManufacturerButton , m_colorManufacturerField);
        m_manufacturerTab.setContent(manufacturerPane);

        VBox unitPane = new VBox();
        unitPane.getChildren().addAll(m_unitsTable , m_addUnitButton , m_UnitField);
        m_unitTab.setContent(unitPane);
        
        
        
        //Prevent tabs from closing
        m_settingsTab.setClosable(false);
        m_colorTab.setClosable(false);
        m_unitTab.setClosable(false);
        m_manufacturerTab.setClosable(false);
        
        initSettingsTab();
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
