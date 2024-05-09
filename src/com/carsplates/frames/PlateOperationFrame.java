/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.frames;

import com.carsplates.main.Main;
import com.carsplates.smart.records.CurrentMovement;
import com.carsplates.smart.records.Plate;
import com.carsplates.smart.records.VehicleCard;
import com.myz.component.myzButton;
import com.myz.component.myzComboBox;
import com.myz.component.myzLabel;
import com.myz.component.myzTextField;
import com.myz.connection.ConnectionContext;
import com.myz.connection.arConnectionInfo;
import javafx.geometry.NodeOrientation;
import myzMessage.myzMessage;

/**
 *
 * @author Montazar Hamoud
 */
public class PlateOperationFrame extends FramesParent
{
    //Consrtuctor
    public PlateOperationFrame() 
    {
        super("تبدلات اللوحة ");
        setCenterOrientation(NodeOrientation.RIGHT_TO_LEFT);
        super.initFrame(false, true, false);
    }
    //Class member
    public static final int MODE_DELIVERED_PLATE = 1 ;
    public static final int MODE_RECEIVED_PLATE  = 2 ;
    public static final int MODE_DROPPED_PLATE   = 3 ;
    public static final int MODE_RENEW_CARD      = 5 ;
    public static final int MODE_CHANGE_VEHICLE  = 6 ;
    
    
    //Data member
    myzLabel          m_plateNoLabel    = new myzLabel("رقم اللوحة ");
    myzTextField      m_plateNo         = new myzTextField();
    myzLabel          m_plateCityLabel  = new myzLabel("المحافظة ");
    myzComboBox       m_plateCity       = new myzComboBox("SELECT * FROM PLATE_CITY", "T_NAME" , false);
    ConnectionContext m_connection      = arConnectionInfo.getConnectionContext();
    Plate             m_plate           = new Plate(m_connection);
    CurrentMovement   m_currentMovement = new CurrentMovement(m_connection);
    VehicleCard       m_vehicleCard     = new VehicleCard(m_connection);
    
    //////////////////////////////////////////////////////////////////
    //                          Left sidebar                        // 
    //////////////////////////////////////////////////////////////////

    myzButton m_receivedFrom = new myzButton("استلام ")
    {
      @Override
      public void buttonPressed()
      {
        if(loadToReceived())
        {
            Main.SCENE.setRoot(new OperationFrame("استلام لوحة " , m_plate , m_currentMovement , MODE_RECEIVED_PLATE   ));
        }
      }
    };
    
    myzButton m_deliveredTo = new myzButton("تسليم ")
    {
      @Override
      public void buttonPressed()
      {
        if( loadToDelivered() )
        {
            Main.SCENE.setRoot(new OperationFrame( "تسليم لوحة " , m_plate , m_currentMovement , MODE_DELIVERED_PLATE   ));
        }
      }
    };
    
    myzButton m_transfer = new myzButton("إسقاط لوحة ")
    {
      @Override
      public void buttonPressed()
      {
        if(loadToDropped())
        {
            Main.SCENE.setRoot(new OperationFrame( "تسليم لوحة " , m_plate , m_currentMovement , MODE_DROPPED_PLATE ) );
        }
      }
    };
    
    
    myzButton m_renewVehicleCard = new myzButton("تجديد بطاقة ")
    {
      @Override
      public void buttonPressed()
      {
        if(loadToRenewVehicleCard())
        {
           Main.SCENE.setRoot(new OperationFrame( "تجديد بطاقة " , m_plate , m_currentMovement , MODE_RENEW_CARD ) );
        }
      }
    };
    
    myzButton m_changeVehicle = new myzButton(" تغير الية")
    {
      @Override
      public void buttonPressed()
      {
        if(loadToChangeVehicle())
        {
           Main.SCENE.setRoot(new OperationFrame( " تغير الية" , m_plate , m_currentMovement , MODE_CHANGE_VEHICLE ) );
        }
      }
    };
    
    

    
    public boolean load()
    {
        String plateNo      = m_plate.getPlateNo();
        int    pnrPlateCity = m_plate.getPnrPlateCity();
        if(m_plateNo.getText().equals("") || m_plateCity.getIntValue() == -1)
        {
            myzMessage.alertMessage("الرجاء تعبئة الحقول " );
            return false ;
        }
        if(m_plateNo.getText().equals(plateNo) && m_plateCity.getIntValue() == pnrPlateCity)
            return true;
        m_plate      = new Plate(m_connection);
        
        plateNo      = m_plateNo.getText();
        pnrPlateCity = m_plateCity.getIntValue();
        if(!m_plate.getByNoAndCity(plateNo, pnrPlateCity))
        {
            myzMessage.alertMessage("اللوحة غير موجودة مسبقا "  );
            return false;
        }
        m_currentMovement.getByPlateNoAndCity(plateNo, pnrPlateCity);
        
        return true;
    }
    
    public boolean loadToDelivered()
    {
        if(!load())
            return false ;
        if(m_currentMovement.getPlateStatus() == Plate.PLATE_STATUS_DELIVERED)
        {
            myzMessage.alertMessage(" اللوحة مسلمة لايمكن تسليمها مرة أخرى");
            return false ;
        }
        else if (m_currentMovement.getPlateStatus() == Plate.PLATE_STATUS_DROPPED)
        {
            myzMessage.alertMessage(" اللوحة مسقطة لا يمكن تسليمها");
            return false ;
        }
        return true ;
    }
    
    public boolean loadToReceived()
    {
        if(!load())
            return false ;
        if(m_currentMovement.getPlateStatus() == Plate.PLATE_STATUS_EXISTED)
        {
            myzMessage.alertMessage(" اللوحة موجودة بالمخزن لايمكن استلامها");
            return false ;
        }
        else if (m_currentMovement.getPlateStatus() == Plate.PLATE_STATUS_DROPPED)
        {
            myzMessage.alertMessage(" اللوحة مسقطة لا يمكن استلامها");
            return false ;
        }
        return true ;
    }
    
    public boolean loadToDropped()
    {
        if(!load())
            return false ;
        
        if(m_currentMovement.getPlateStatus() == Plate.PLATE_STATUS_DELIVERED)
        {
            myzMessage.alertMessage(" اللوحة مسلمة لايمكن إسقاطها");
            return false ;
        }
        else if (m_currentMovement.getPlateStatus() == Plate.PLATE_STATUS_DROPPED)
        {
            myzMessage.alertMessage(" اللوحة مسقطة لايمكن إسقاطها مرة أخرى");
            return false ;
        }
        return true ;
    }
    
    public boolean loadToRenewVehicleCard()
    {
        return load() ;
    }
    
    public boolean loadToExtendVehicleCard()
    {
        return load();
    }
    
    public boolean loadToChangeVehicle()
    {
        if(!load())
            return false;
        if(m_currentMovement.getPnrVehicle() <= 0)
        {
            myzMessage.alertMessage(" لايوجد الية لتغيرها الرجاء ادخال الية ");
            return false ;
        }
        return true ;
    }


    @Override
    public void initCenter()
    {
        m_plateCity.setPrefWidth(120);
        
        m_receivedFrom.setMinSize(120,50);
        m_receivedFrom.setParentPane(getCenterPane());

        m_deliveredTo.setMinSize(120,50);
        m_deliveredTo.setParentPane(getCenterPane());

        m_transfer.setMinSize(120,50);
        m_transfer.setParentPane(getCenterPane());
        
        m_changeVehicle.setMinSize(120 ,50);
        m_changeVehicle.setParentPane(getCenterPane());
        
        
        m_renewVehicleCard.setMinSize(120 ,50);
        m_renewVehicleCard.setParentPane(getCenterPane());
        
        getCenterPane().setHgap(5);
        getCenterPane().setVgap(5);
        
        int column = 3 ;
        int row    = 3 ;
        
        // رقم اللوحة والمحافظة 
        getCenterPane().add(m_plateNoLabel    , column++ , row );
        getCenterPane().add(m_plateNo         , column++ , row++ );
        
        column = 3;
        
        getCenterPane().add(m_plateCityLabel  , column++ , row );
        getCenterPane().add(m_plateCity       , column++ , row++ );
        
    }
    
    @Override
    public void initLeftSidebar()
    {
        addToLeft(m_receivedFrom);   
        addToLeft(m_deliveredTo);   
        addToLeft(m_transfer);   
        addToLeft(m_changeVehicle);    
        addToLeft(m_renewVehicleCard);   
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
