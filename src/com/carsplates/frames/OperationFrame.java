/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.frames;

import static com.carsplates.frames.CarsPlatesFrame.PERMANENT;
import static com.carsplates.frames.CarsPlatesFrame.TEMP;
import static com.carsplates.frames.PlateOperationFrame.MODE_CHANGE_VEHICLE;
import static com.carsplates.frames.PlateOperationFrame.MODE_DELIVERED_PLATE;
import static com.carsplates.frames.PlateOperationFrame.MODE_DROPPED_PLATE;
import static com.carsplates.frames.PlateOperationFrame.MODE_RECEIVED_PLATE;
import static com.carsplates.frames.PlateOperationFrame.MODE_RENEW_CARD;
import com.carsplates.smart.records.ArchivedMovement;
import com.carsplates.smart.records.CurrentMovement;
import com.carsplates.smart.records.DeliveredInfo;
import com.carsplates.smart.records.Plate;
import com.carsplates.smart.records.VehicleCard;
import com.carsplates.tools.myzAttachPanel;
import com.myz.component.myzButton;
import com.myz.component.myzComboBox;
import com.myz.component.myzComponent;
import com.myz.component.myzDateField;
import com.myz.component.myzIntegerField;
import com.myz.component.myzLabel;
import com.myz.component.myzRadioButton;
import com.myz.component.myzTextField;
import com.myz.connection.ConnectionContext;
import com.myz.connection.arConnectionInfo;
import java.lang.reflect.Field;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.carsplates.smart.records.Vehicle;
import com.myz.component.myzTextArea;
import com.myz.log.logWriter;
import javafx.geometry.NodeOrientation;
import myzMessage.myzMessage;
import static myzMessage.myzMessage.alertMessage;
import static myzMessage.myzMessage.alertMessage;


/**
 *
 * @author Montazar Hamoud
 */
public class OperationFrame extends FramesParent
{
    //Constructor
    public OperationFrame(String frameName ,Plate plate ,CurrentMovement currentMovement ,int mode) 
    {
        super(frameName);
        setCenterOrientation(NodeOrientation.RIGHT_TO_LEFT);
        
        m_plateNo.setText(plate.getPlateNo());
        m_plateNo.setDisable(true);
        
        m_plateCity.select(plate.getPnrPlateCity());
        m_plateCity.setDisable(true);
        
        m_mode            = mode ;
        m_plate           = plate ;
        m_currentMovement = currentMovement;
        m_note.setText(m_currentMovement.getNote());
        
        super.initFrame(false, true, false);
    }
    
    //Data member
    int               m_mode           ;
    ConnectionContext m_connection      = arConnectionInfo.getConnectionContext();
    Plate             m_plate           = new Plate(ConnectionContext.m_staticConnection);
    CurrentMovement   m_currentMovement = new CurrentMovement(ConnectionContext.m_staticConnection);
    DeliveredInfo     m_deliveredInfo   = new DeliveredInfo(m_connection);
    
    ////////////////////////////////////////////////////////////////////////////
    ///                            CENTER                                     //
    ////////////////////////////////////////////////////////////////////////////
    myzLabel          m_plateNoLabel   = new myzLabel("رقم اللوحة ");
    myzTextField      m_plateNo        = new myzTextField();
    myzLabel          m_plateCityLabel = new myzLabel("المحافظة ");
    myzComboBox       m_plateCity      = new myzComboBox("SELECT * FROM PLATE_CITY", "T_NAME" , false);
    
    //Delivered Mode
    myzLabel       m_deliveredUnitLabel        = new myzLabel("الوحدة المستلمة ");
    myzComboBox    m_deliveredUnit             = new myzComboBox("SELECT * FROM UNITS", "T_NAME" , true);
    myzLabel       m_receiverNameLabel         = new myzLabel("اسم المستلم ");
    myzTextField   m_receiverName              = new myzTextField();
    
    myzLabel        m_reciverPhoneLabel        = new myzLabel("هاتف المستلم ");
    myzIntegerField m_receiverPhone            = new myzIntegerField() ;
    myzIntegerField m_reciverCellular          = new myzIntegerField() ;
    myzLabel        m_deliveredDateLabel       = new myzLabel("تاريخ التسليم ");
    myzDateField    m_deliveredDate            = new myzDateField(); 
    myzLabel        m_deliveryPeriodLabel      = new myzLabel("نوع التسليم ");
    ToggleGroup     m_deliveredPeriodGroup     = new ToggleGroup();
    myzRadioButton  m_tempDeliveredRadio       = new myzRadioButton("مؤقت ");
    myzRadioButton  m_permanentDeliveredRadio  = new myzRadioButton("دائم ");
    myzLabel        m_deliveredEndDateLabel    = new myzLabel("إنتهاء التسليم ");
    myzDateField    m_deliveredEndDate         = new myzDateField();
    myzAttachPanel  m_attachPanel              = new myzAttachPanel(myzAttachPanel.MODE_DISABLE);
    
    //Received Mode
    myzLabel        m_actualDeliveredDateLabel = new myzLabel("تاريخ الاستلام ");
    myzDateField    m_actualReceivedDate      = new myzDateField();
    //Dropped Mode
    myzLabel        m_droppedDateLabel         = new myzLabel("تاريخ الإسقاط ");
    myzDateField    m_droppedDate              = new myzDateField();
    
    //Renew Card Mode 
    myzLabel       m_cardNoLabel               = new myzLabel("رقم البطاقة ");
    myzTextField   m_cardNo                    = new myzTextField();
    myzLabel       m_cardCodeLabel             = new myzLabel("كود البطاقة ");
    myzTextField   m_cardCode                  = new myzTextField();
    myzLabel       m_startDateLabel            = new myzLabel("تاريخ الصدور");
    myzDateField   m_startDate                 = new myzDateField(); 
    myzLabel       m_endDateLabel              = new myzLabel("تاريخ الإنتهاء");
    myzDateField   m_endDate                   = new myzDateField();
    myzLabel       m_cardPeriodTypeLabel       = new myzLabel("نوع البطاقة ") ;
    ToggleGroup    m_cardPeriodGroup           = new ToggleGroup();
    myzRadioButton m_tempCardRadio             = new myzRadioButton("مؤقتة ");
    myzRadioButton m_permanentCardRadio        = new myzRadioButton("دائمة ");
    myzLabel       m_renewDateLabel            = new myzLabel("تاريخ التجديد");
    myzDateField   m_renewDate                 = new myzDateField();
    
    //Change Vehcile Mode
    ToggleGroup    m_typeGroup                 = new ToggleGroup();
    myzRadioButton m_carRadio                  = new myzRadioButton("سيارة");
    myzRadioButton m_bickRadio                 = new myzRadioButton("دراجة نارية");
    
    myzLabel       m_vehicleManufacturerLabel = new myzLabel("نوع المركبة ");
    myzComboBox    m_vehicleManufacturer      = new myzComboBox("SELECT * FROM VEHICLE_MANUFACTURER", "T_NAME" , true);
    myzLabel       m_vehicleColorLabel        = new myzLabel("لون المركبة ");
    myzComboBox    m_vehicleColor             = new myzComboBox("SELECT * FROM VEHICLE_COLOR", "T_NAME" , false);
    myzLabel       m_engineNoLabel            = new myzLabel("رقم المحرك ");
    myzTextField   m_engineNo                 = new myzTextField();
    myzLabel       m_chassisNoLabel           = new myzLabel(" رقم الشاسيه");
    myzTextField   m_chassisNo                = new myzTextField();
    myzLabel       m_productionDateLabel      = new myzLabel(" تاريخ الصنع");
    myzTextField   m_productionYear           = new myzTextField();
    myzLabel       m_changeVehicleDateLabel   = new myzLabel("تاريخ التغيير");
    myzDateField   m_changeVehicleDate        = new myzDateField();
    
    myzLabel        m_noteLabel               = new myzLabel("الملاحظات ");
    myzTextArea     m_note                    = new myzTextArea();
    
    ////////////////////////////////////////////////////////////////////////////
    //                          LEFT SIDEBAR                                  // 
    ////////////////////////////////////////////////////////////////////////////

    myzButton m_saveButton         = new myzButton()
    {
        @Override
        public void buttonPressed()
        {
            save();
        }
        
    };
    
    myzButton m_printReceiptButton = new myzButton("طباعة وصل استلام ")
    {
        @Override
        public void buttonPressed()
        {
            PrintReceiptFrame.print(m_plate);
        }
    };
    
    //Method   
    //return true if there is mandatory is empty
    public boolean checkEmptyAndMandatory()
    {
        try
        {
            Field []     fields   = OperationFrame.class.getDeclaredFields();
            myzComponent component = null ;
            boolean      result    = false ; 
            for( Field field : fields)
            {
                Class className = field.getType() ;
                if ( Class.forName("com.myz.component.myzComponent").isAssignableFrom(className) )
                {
                    component = (myzComponent)field.get(this);
                    if(component.checkEmptyAndMandatory())
                    {
                        result = true ;
                    }
                    else
                    {
                        component.resetStyle();
                    }

                }
            }
            return result ;
        }
        catch(SecurityException | ClassNotFoundException | IllegalArgumentException | IllegalAccessException ex)
        {
            logWriter.write(ex);
        }
        return false ;
    }
    
    public void save()
    {
        //Check mandatory fields
        if( checkEmptyAndMandatory())
        {
            alertMessage("الرجاء تعبئة الحقول الإجبارية "  );
            return ;
        }
        //TODO
        m_currentMovement.setNote(m_note.getText());
        switch (m_mode) 
        { 
            case MODE_DELIVERED_PLATE:
                saveDeliveredInfo();
                break;
            case MODE_RECEIVED_PLATE:
                saveReceivedInfo();
                break;
            case MODE_DROPPED_PLATE:
                saveDropped();
                break;
            case MODE_RENEW_CARD :
                saveRenewVehicleCard();
                break;
            case MODE_CHANGE_VEHICLE:
                saveChangeVehicle();
                break;
            
            default:
                break;
        }
    }
    
    public void load()
    {
       switch (m_mode) 
        { 
            case MODE_DELIVERED_PLATE:
                loadDeliveredInfo();
                break;
            case MODE_RECEIVED_PLATE:
                loadRecivedInfo();
                break;
            case MODE_DROPPED_PLATE:
                loadDroppedInfo();
                break;
                
            default:
                break;
        } 
    }
    
    public void loadDeliveredInfo()
    {
        int pnrDeliveredInfo = m_currentMovement.getPnrDeliveredInfo();
        
        if(pnrDeliveredInfo < 1 )
            return;
        
        m_deliveredInfo.get(pnrDeliveredInfo);
        
        m_deliveredUnit.select(m_deliveredInfo.getPnrDeliveredUnit());
        m_receiverName.setText(m_deliveredInfo.getReceiverName());
        m_receiverPhone.setText(m_deliveredInfo.getReceiverPhone());
        m_reciverCellular.setText(m_deliveredInfo.getReceiverCellular());
        m_deliveredDate.setDate(m_deliveredInfo.getDeliveredDate());
        m_deliveredEndDate.setDate(m_deliveredInfo.getDeliveredEndDate());
        
        if(m_deliveredInfo.getDeliveredTerm() == TEMP )
            m_tempDeliveredRadio.setSelected(true);
        else
            m_permanentDeliveredRadio.setSelected(true);

        m_attachPanel.initAttachPanelEvents();
        m_attachPanel.setPnrDeliveredInfo(pnrDeliveredInfo);
        m_attachPanel.loadFiles();
    }
    
    public void loadRecivedInfo()
    {
        int pnrDeliveredInfo = m_currentMovement.getPnrDeliveredInfo();
        
        m_deliveredInfo.get(pnrDeliveredInfo);
        
        if(pnrDeliveredInfo < 1 )
            return;
        m_actualReceivedDate.setDate(m_deliveredInfo.getActualReceivedDate());
        
    }
    
    public void loadDroppedInfo()
    {
        m_droppedDate.setDate(m_currentMovement.getPlate().getPlateEndDate());
    }
    
    public void saveDeliveredInfo()
    {
        //Fill ReceivedInfo data
        String        startDate     = m_deliveredDate.getDate();
        String        endDate       = m_deliveredEndDate.getDate();
        
        try
        {
            if(!"".equals(startDate) && !"".equals(endDate)  )
                if(endDate.compareTo(startDate) < 0 )
                {
                    alertMessage("تاريخ الاستلام قبل تاريخ التسليم"  );
                    return ;
                }
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }
        m_deliveredInfo.setDeliveredDate(startDate);
        m_deliveredInfo.setDeliveredEndDate(endDate);
        m_deliveredInfo.setActualDeliveredDate(startDate);
        m_deliveredInfo.setPnrDeliveredUnit(m_deliveredUnit.getIntValue());
        //مدة التسليم مؤقت  أو دائم 
        int receivedTerm = 0 ;
        if(m_tempDeliveredRadio.isSelected())
            receivedTerm = TEMP ;
        if(m_permanentDeliveredRadio.isSelected())
            receivedTerm = PERMANENT ;
        m_deliveredInfo.setDeliveredTerm(receivedTerm);
        m_deliveredInfo.setReceiverCellular(m_reciverCellular.getText());
        m_deliveredInfo.setReceiverPhone(m_receiverPhone.getText());
        m_deliveredInfo.setReceiverName(m_receiverName.getText());
        
        boolean res = m_currentMovement.archiveMovement(ArchivedMovement.STATUS_DELIVERED_PLATE , startDate , m_deliveredInfo);
        if(res)
        {
            //Increase delivered counter at plate record
            int deliveredCounter = m_plate.getDeliveredCounter();
            m_plate.setDeliveredCounter(++deliveredCounter);
            m_plate.save();
            
            //ُEnable drag and drop docFiles
            m_attachPanel.initAttachPanelEvents();
            m_attachPanel.setPnrDeliveredInfo(m_deliveredInfo.getPnr());
            myzMessage.noteMessage("تم الحفظ بنجاح "  );
        }
    }
    
    //ReceivedInfo is virtual we save it's data at deliveredInfo and update plate status
    public void saveReceivedInfo()
    {
        DeliveredInfo deliveredInfo    = new DeliveredInfo(m_connection);
        int           pnrDeliveredInfo = m_currentMovement.getPnrDeliveredInfo() ;
        String        date             = m_actualReceivedDate.getDate() ;
        
        deliveredInfo.get(pnrDeliveredInfo);
        
        boolean res = m_currentMovement.archiveMovement(ArchivedMovement.STATUS_RECEIVED_PLATE , date , deliveredInfo);
        if(res)
        {
            myzMessage.noteMessage("تم الحفظ بنجاح "  );
        }
    }
    
    public void saveDropped()
    {
        String date = m_droppedDate.getDate() ;
        m_plate.setPlateEndDate(date );
        
        boolean res = m_currentMovement.archiveMovement(ArchivedMovement.STATUS_DROPPED_PLATE , date , m_plate);
        if(res)
        {
            myzMessage.noteMessage("تم الحفظ بنجاح "  );
//            m_saveButton.setDisable(true);
        }
        
    }
    
    public void saveRenewVehicleCard()
    {
        String startDate     = m_startDate.getDate();
        String endDate       = m_endDate.getDate();
        try
        {
            if(!"".equals(startDate) && !"".equals(endDate)  )
                if(endDate.compareTo(startDate) < 0 )
                {
                    alertMessage("تاريخ انتهاء البطاقة قبل تاريخ الصدور "  );
                    return ;
                }
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }   
        boolean res                = true ;
        boolean described          = false ;
        int     pnrCurrentMovement = m_currentMovement.getPnr();
        Vehicle vehicle            = new Vehicle(m_connection);
        
        if( checkAllVehicleDataInserted())
        {
            //Fill vehicle data 
            vehicle.setChassisNo(m_chassisNo.getText());
            vehicle.setEngineNo(m_engineNo.getText());
            vehicle.setPnrCarColor(m_vehicleColor.getIntValue());
            vehicle.setPnrCarManufacturer(m_vehicleManufacturer.getIntValue());
            vehicle.setProductionYear(m_productionYear.getText());

            int vehicleType = 0 ;
            if(m_bickRadio.isSelected())
                vehicleType = Vehicle.BICK ;
            if(m_carRadio.isSelected())
                vehicleType = Vehicle.CAR ;
            vehicle.setType(vehicleType);

            //Check if the vehicle already exist and used if exist but does not used then load it
            if(vehicle.isUsed( pnrCurrentMovement ))
            {
                alertMessage("رقم الشاسيه مستخدم حاليا "  );
                return ;
            }
            
            if ( vehicle.getChassisNo() != null && vehicle.getChassisNo().length() > 0)
            {
                described = true ;
            }
        }
        else
            return;
        
        ////////////////////////////////
        VehicleCard vehicleCard    = new VehicleCard(m_connection);
        vehicleCard.setCardCode(m_cardCode.getText());
        vehicleCard.setCardNo(m_cardNo.getText());
        vehicleCard.setStartDate(startDate);
        vehicleCard.setEndDate(endDate);
        vehicleCard.setCardDescribed(described);
        
        // The card status will came from the old card 
        //vehicleCard.setCardStatus(VehicleCard.CARD_STATUS_EXISTED);
        
        int cardTerm = 0 ;
        if(m_tempCardRadio.isSelected())
            cardTerm = TEMP ;
        if(m_permanentCardRadio.isSelected())
            cardTerm = PERMANENT ;
        m_currentMovement.getVehicleCard().setCardTerm(cardTerm);

        if(vehicleCard.isExist( ))
        {
            alertMessage("رقم البطاقة موجود مسبقا "  );
            return ;
        }
        
        String date = m_renewDate.getDate() ;
        //Save the new card information
        res = m_currentMovement.archiveMovement(ArchivedMovement.STATUS_RENEW_CARD , date  , vehicleCard);
        
        if ( vehicle.getChassisNo() != null && vehicle.getChassisNo().length() > 0)
        {
            res  &= m_currentMovement.archiveMovement(ArchivedMovement.STATUS_CHANGE_VEHICLE , date , vehicle);
        }
        else
        {
            res  &= m_currentMovement.archiveMovement(ArchivedMovement.STATUS_SEPARATE_VEHICLE , date , vehicle);
        }
        
        
        
        if(res)
        {
            myzMessage.noteMessage("تم الحفظ بنجاح "  );
            m_saveButton.setDisable(true);
        } 
    }
    
    
    public void saveChangeVehicle()
    {
        int pnrCurrentMovement = m_currentMovement.getPnr();
        
        Vehicle vehicle = new Vehicle(m_connection);
        //Fill vehicle data 
        vehicle.setChassisNo(m_chassisNo.getText());
        vehicle.setEngineNo(m_engineNo.getText());
        vehicle.setPnrCarColor(m_vehicleColor.getIntValue());
        vehicle.setPnrCarManufacturer(m_vehicleManufacturer.getIntValue());
        vehicle.setProductionYear(m_productionYear.getText());
        
        int vehicleType = 0 ;
        if(m_bickRadio.isSelected())
            vehicleType = Vehicle.BICK ;
        if(m_carRadio.isSelected())
            vehicleType = Vehicle.CAR ;
        vehicle.setType(vehicleType);
        //Check if the vehicle already exist
        if(vehicle.isUsed( pnrCurrentMovement ))
        {
            alertMessage("رقم الشاسيه مستخدم حاليا "  );
            return ;
        }
        String date = m_changeVehicleDate.getDate();
        boolean res = m_currentMovement.archiveMovement(ArchivedMovement.STATUS_CHANGE_VEHICLE , date , vehicle);
        if(res)
        {
            myzMessage.noteMessage("تم الحفظ بنجاح "  );
            m_saveButton.setDisable(true);
        }
    }

    public boolean checkAllVehicleDataInserted()
    {
        boolean haveSomeData = false;
        if ( ( m_engineNo.getValue()  != null )
             ||( m_vehicleColor.getIntValue() > 0 )
             ||( m_vehicleManufacturer.getIntValue() > 0 )
             ||( m_productionYear.getValue() != null) )
        {
            haveSomeData = true;
        }
        if ( haveSomeData && (
                               ( m_chassisNo.getValue() == null || m_chassisNo.getText().length() < 1 )
                              ))
        {
            alertMessage("الرجاء استكمال بيانات المركبة" + "!!" );
            return false;
        }
        return true;
    }

    @Override
    public void initCenter()
    {
        getCenterPane().setHgap(10);
        getCenterPane().setVgap(10);
        getCenterPane().setAlignment(Pos.TOP_CENTER);
        
        int column = 0 ;
        int row    = 0 ;

        switch (m_mode) 
        {
            case MODE_DELIVERED_PLATE:
                //Set mandatory fields
                m_receiverName.setIsMandatory(true);
                m_deliveredUnit.setIsMandatory(true);
                m_deliveredDate.setIsMandatory(true);
                m_deliveredEndDate.setIsMandatory(true);
                
                // اسم المستلم و الجهة المستلمة
                getCenterPane().add(m_receiverNameLabel    , column++ , row );
                getCenterPane().add(m_receiverName         , column++ , row );
                getCenterPane().add(m_deliveredUnitLabel   , column++ , row );
                getCenterPane().add(m_deliveredUnit        , column++ , row );
                
                // هاتف المستلم أرضي و محمول
                getCenterPane().add(m_reciverPhoneLabel    , column++ , row );
                getCenterPane().add(m_reciverCellular      , column++ , row );
                getCenterPane().add(m_receiverPhone        , column++ , row );
                
                column = 0 ;
                row++ ;
                
                // تاريخ الاستلام والتسليم
                getCenterPane().add(m_deliveredDateLabel   , column++ , row );
                getCenterPane().add(m_deliveredDate        , column++ , row );
                getCenterPane().add(m_deliveredEndDateLabel, column++ , row );
                getCenterPane().add(m_deliveredEndDate     , column++ , row );
                
                // مدة التسليم
                getCenterPane().add(m_deliveryPeriodLabel     , column++ , row );
                getCenterPane().add(m_permanentDeliveredRadio , column++ , row );
                getCenterPane().add(m_tempDeliveredRadio      , column++ , row );
                
                m_tempDeliveredRadio.setToggleGroup(m_deliveredPeriodGroup);
                m_permanentDeliveredRadio.setToggleGroup(m_deliveredPeriodGroup);
                //To hide the delivery Period Date if it's permanent period
                m_deliveredEndDate.setVisible(true);
                m_tempDeliveredRadio.setSelected(true);
                m_tempDeliveredRadio.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean wasPreviouslySelected, Boolean isNowSelected) ->
                {
                    if(isNowSelected)
                    {
                        m_deliveredEndDate.setVisible(true);
                        m_deliveredEndDate.setIsMandatory(true);
                        
                    }
                    else
                    {
                        m_deliveredEndDate.setVisible(false);
                        m_deliveredEndDate.setIsMandatory(false);
                    }
                }); //Add attachPanel 
                initFooter();
                break;
            case MODE_RECEIVED_PLATE:
                m_actualReceivedDate.setIsMandatory(true);
                getCenterPane().add(m_actualDeliveredDateLabel , column++ , row);
                getCenterPane().add(m_actualReceivedDate      , column++ , row);
                break;
            case MODE_DROPPED_PLATE:
                m_droppedDate.setIsMandatory(true);
                getCenterPane().add(m_droppedDateLabel , column++ , row);
                getCenterPane().add(m_droppedDate      , column++ , row);
                break;
            case MODE_RENEW_CARD:
                m_endDate.setIsMandatory(true);
                m_startDate.setIsMandatory(true);
                m_cardNo.setIsMandatory(true);
                m_renewDate.setIsMandatory(true);
               
                // رقم البطاقة وكود البطاقة
                getCenterPane().add(m_cardNoLabel        , column++ , row );
                getCenterPane().add(m_cardNo             , column++ , row );
                getCenterPane().add(m_cardCodeLabel      , column++ , row );
                getCenterPane().add(m_cardCode           , column++ , row );
              
                // تاريخ الصدور و تاريخ الانتهاء  للبطاقة
                getCenterPane().add(m_startDateLabel     , column++ , row );
                getCenterPane().add(m_startDate          , column++ , row );
                getCenterPane().add(m_endDateLabel       , column++ , row );
                getCenterPane().add(m_endDate            , column++ , row );
                
                column = 0 ;
                row++;
                
                // نوع البطاقة دائمة او مؤقتة
                getCenterPane().add(m_renewDateLabel      , column++ , row );
                getCenterPane().add(m_renewDate           , column++ , row );
                getCenterPane().add(m_cardPeriodTypeLabel , column++ , row );
                getCenterPane().add(m_permanentCardRadio  , column++ , row );
                getCenterPane().add(m_tempCardRadio       , column++ , row );
                m_tempCardRadio.setSelected(true);
                m_permanentCardRadio.setToggleGroup(m_cardPeriodGroup ) ;
                m_tempCardRadio.setToggleGroup(m_cardPeriodGroup ) ;
                m_tempCardRadio.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean wasPreviouslySelected, Boolean isNowSelected) ->
                {
                    if(isNowSelected)
                    {
                        m_endDate.setVisible(true);
                        m_endDate.setIsMandatory(true);
                    }
                    else
                    {
                        m_endDate.setVisible(false);
                        m_endDate.setIsMandatory(false);
                    }
                }); 
                
                column = 0 ;
                row++;
                
                getCenterPane().add(m_engineNoLabel            , column++ , row);
                getCenterPane().add(m_engineNo                 , column++ , row);
                getCenterPane().add(m_vehicleColorLabel        , column++ , row);
                getCenterPane().add(m_vehicleColor             , column++ , row);
                getCenterPane().add(m_vehicleManufacturerLabel , column++ , row);
                getCenterPane().add(m_vehicleManufacturer      , column++ , row);
               
                column = 0 ;
                row++ ;
                // نوع الية النقل سيارة او دراجة نارية
                getCenterPane().add(m_carRadio                 , column++ , row );
                getCenterPane().add(m_bickRadio                , column++ , row );
                m_carRadio.setSelected(true);
                m_carRadio.setToggleGroup(m_typeGroup);
                m_bickRadio.setToggleGroup(m_typeGroup);
                getCenterPane().add(m_productionDateLabel      , column++ , row);
                getCenterPane().add(m_productionYear           , column++ , row);
                getCenterPane().add(m_chassisNoLabel           , column++ , row);
                getCenterPane().add(m_chassisNo                , column++ , row);
                break;
            case MODE_CHANGE_VEHICLE:
               
                column = 0 ;
                getCenterPane().add(m_engineNoLabel            , column++ , row);
                getCenterPane().add(m_engineNo                 , column++ , row);
                getCenterPane().add(m_vehicleColorLabel        , column++ , row);
                getCenterPane().add(m_vehicleColor             , column++ , row);
                getCenterPane().add(m_vehicleManufacturerLabel , column++ , row);
                getCenterPane().add(m_vehicleManufacturer      , column++ , row);
               
                column = 0 ;
                row++ ;
                // نوع الية النقل سيارة او دراجة نارية
                getCenterPane().add(m_carRadio                 , column++ , row );
                getCenterPane().add(m_bickRadio                , column++ , row );
                m_carRadio.setToggleGroup(m_typeGroup);
                m_bickRadio.setToggleGroup(m_typeGroup);
                getCenterPane().add(m_productionDateLabel      , column++ , row);
                getCenterPane().add(m_productionYear           , column++ , row);
                getCenterPane().add(m_chassisNoLabel           , column++ , row);
                getCenterPane().add(m_chassisNo                , column++ , row);
                break;
            default:
                break;
        }
        
        row++;
        column = 0 ;
        
        // الملاحظات
        getCenterPane().add(m_noteLabel            , column++ , row , 1 , 1);
        getCenterPane().add(m_note                 , column++ , row , 4 , 1);
    }
    
    @Override
    public void initHeader() 
    {
        //Do nothing
    }

    @Override
    public void initRightSidebar()
    {
        if(m_mode == MODE_RENEW_CARD)
            return;
        
        // رقم اللوحة والمحافظة 
        addToRight(m_plateNoLabel);
        addToRight(m_plateNo);
        addToRight(m_plateCityLabel);
        addToRight(m_plateCity);
        if(m_mode == MODE_DELIVERED_PLATE)
        {
            addToRight(m_printReceiptButton);
        }
    }

    @Override
    public void initFrameBasicData() 
    {
        //Do nothing
    }
    
    @Override
    public void initLeftSidebar()
    {
        m_saveButton.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/save.png") ) ) );
        
        addToLeft(m_saveButton);    
    }
    
    @Override
    public void initFooter()
    {
        //To make the attachPanel scrollable
        Node node    = m_attachPanel;
        ScrollPane scrollPane = new ScrollPane(node);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setMinSize(300 , 150);
        setBottom(scrollPane);
    }
}
