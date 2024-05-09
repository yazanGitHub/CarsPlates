/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.frames;

import static com.carsplates.frames.PlateOperationFrame.MODE_DELIVERED_PLATE;
import static com.carsplates.frames.PlateOperationFrame.MODE_DROPPED_PLATE;
import static com.carsplates.frames.PlateOperationFrame.MODE_RECEIVED_PLATE;
import static com.carsplates.frames.PlateOperationFrame.MODE_RENEW_CARD;
import com.carsplates.main.Main;
import com.carsplates.smart.records.CurrentMovement;
import com.carsplates.smart.records.DeliveredInfo;
import com.carsplates.smart.records.VehicleCard;
import com.carsplates.smart.records.Plate;
import com.carsplates.smart.records.Vehicle;
import static com.carsplates.smart.records.Vehicle.BICK;
import static com.carsplates.smart.records.Vehicle.CAR;
import com.carsplates.tools.myzAttachPanel;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import com.myz.component.myzButton;
import com.myz.component.myzComboBox;
import com.myz.component.myzComboBoxItem;
import com.myz.component.myzComponent;
import com.myz.component.myzDateField;
import com.myz.component.myzIntegerField;
import com.myz.component.myzLabel;
import com.myz.component.myzRadioButton;
import com.myz.component.myzTextArea;
import com.myz.component.myzTextField;
import com.myz.connection.ConnectionContext;
import com.myz.connection.arConnectionInfo;
import com.myz.log.logWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import myzMessage.myzMessage;
import myzTools.ScreenSnapshot;
import static myzMessage.myzMessage.confirmMessage;
import static myzMessage.myzMessage.alertMessage;

/**
 *
 * @author Montazar Hamoud
 */
public class CarsPlatesFrame extends FramesParent
{
    //Constructor
    public CarsPlatesFrame(int mode)
    {
        super("إدخال لوحة ");
        setCenterOrientation(NodeOrientation.RIGHT_TO_LEFT);

        //To load frame in different mode
        m_mode = mode ;
        super.initFrame(false, true, false);
        
        // Check the plate is exist on plate city combo leave 
        if(m_mode == MODE_ADD)
        {
            m_plateCity.focusedProperty().addListener(new ChangeListener<Boolean>() 
            {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) 
                {
                    //If next move is to search button do nothing
                    boolean isSearch = m_searchButton.focusedProperty().getValue();
                    if(oldValue && !isSearch)
                    {
                       int    plateCity  = m_plateCity.getIntValue();
                        String plateNo   = m_plateNo.getText();
                        Plate  plate     = new Plate(m_connection);

                        plate.setPlateNo(plateNo);
                        plate.setPnrPlateCity(plateCity);
                        if (plate.isExist()) 
                        {
                            alertMessage(" اللوحة موجودة مسبقا");
                        }
                    }
                }
            });
        }

    }
    
    //Class members   
    public static final int PERMANENT   = 1 ;
    public static final int TEMP        = 2 ;
    
    public static final int EXIST       = 1 ;
    public static final int NOT_EXIST   = 2 ;
    
    //Frame Modes
    public static final int MODE_ADD    = 1 ;
    public static final int MODE_SEARCH = 2 ;
    public static final int MODE_SHOW   = 3 ;
    
 
    //Data members
    ConnectionContext       m_connection       = arConnectionInfo.getConnectionContext();
    CurrentMovement         m_currentMovement  = new CurrentMovement(m_connection);
    Vector<CurrentMovement> m_vCurrentMovement = null;
    int                     m_currentMovementC = 0 ; 
    int                     m_mode             = 0 ;
    
    ////////////////////////////////////////////////////////////////////////////
    //                                   CENTER                               //
    ////////////////////////////////////////////////////////////////////////////
    
    /* ________________________________Plates fields__________________________*/
    myzLabel       m_plateNoLabel        = new myzLabel("رقم اللوحة");
    myzTextField   m_plateNo             = new myzTextField();
    myzLabel       m_plateCityLabel      = new myzLabel(" المحافظة");
    myzComboBox    m_plateCity           = new myzComboBox("SELECT * FROM PLATE_CITY" , "T_NAME" , false);
    myzLabel       m_plateStatusLabel    = new myzLabel("وضع اللوحة ");
    ToggleGroup    m_plateStatusGroup    = new ToggleGroup();
    myzRadioButton m_statusExist         = new myzRadioButton("موجود ");
    myzRadioButton m_statusDelivered     = new myzRadioButton("مستخدم ");
    myzRadioButton m_statusReturned      = new myzRadioButton("مسقطة");
    myzLabel       m_plateTypeLabel      = new myzLabel("نوع اللوحة ");
    myzComboBox    m_plateType           = new myzComboBox("SELECT * FROM PLATE_TYPE" , "T_NAME" , false);
    myzLabel       m_plateReturnLabel    = new myzLabel("عائدية اللوحة ");
    myzComboBox    m_plateReturn         = new myzComboBox("SELECT * FROM PLATE_RETURN" , "T_NAME" , false);
    myzLabel       m_plateStartDateLabel = new myzLabel("تاريخ الإدخال ");
    myzDateField   m_plateStartDate      = new myzDateField();
    
    /* _______________________________Card fields_____________________________*/
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
    
    
    /* _______________________________Vehicle fields__________________________*/
    ToggleGroup     m_typeGroup                = new ToggleGroup();
    myzRadioButton  m_carRadio                 = new myzRadioButton("سيارة");
    myzRadioButton  m_bickRadio                = new myzRadioButton("دراجة نارية");
    
    myzLabel        m_vehicleManufacturerLabel = new myzLabel("نوع المركبة ");
    myzComboBox     m_vehicleManufacturer      = new myzComboBox("SELECT * FROM VEHICLE_MANUFACTURER", "T_NAME" , true);
    myzLabel        m_vehicleColorLabel        = new myzLabel("لون المركبة ");
    myzComboBox     m_vehicleColor             = new myzComboBox("SELECT * FROM VEHICLE_COLOR", "T_NAME" , true);
    myzLabel        m_engineNoLabel            = new myzLabel("رقم المحرك ");
    myzTextField    m_engineNo                 = new myzTextField();
    myzLabel        m_chassisNoLabel           = new myzLabel(" رقم الشاسيه");
    myzTextField    m_chassisNo                = new myzTextField();
    myzLabel        m_productionDateLabel      = new myzLabel(" تاريخ الصنع");
    myzTextField    m_productionYear           = new myzTextField();
    
    myzLabel        m_noteLabel                = new myzLabel("الملاحظات ");
    myzTextArea     m_note                     = new myzTextArea();
    
    /*____________________________Component at search mode____________________*/
    myzLabel       m_deliveredUnitLabel        = new myzLabel("الوحدة المستلمة ");
    myzComboBox    m_deliveredUnit             = new myzComboBox("SELECT * FROM UNITS", "T_NAME" , false);
    myzLabel       m_receiverNameLabel         = new myzLabel("اسم المستلم ");
    myzTextField   m_receiverName              = new myzTextField();
    
    myzLabel        m_reciverPhoneLabel        = new myzLabel("هاتف المستلم ");
    myzIntegerField m_receiverPhone            = new myzIntegerField() ;
    myzIntegerField m_reciverCellular          = new myzIntegerField() ;
    myzLabel        m_deliveredDateLabel       = new myzLabel("تاريخ التسليم ");
    myzDateField    m_deliveredDate            = new myzDateField(); 
    myzLabel        m_deliveryPeriodLabel      = new myzLabel("مدة التسليم ");
    ToggleGroup     m_deliveredPeriodGroup     = new ToggleGroup();
    myzRadioButton  m_tempDeliveredRadio       = new myzRadioButton("مؤقت ");
    myzRadioButton  m_permanentDeliveredRadio  = new myzRadioButton("دائم ");
    myzDateField    m_deliveredEndDate         = new myzDateField();
    
    ////////////////////////////////////////////////////////////////////////////
    //                                 FOOTER                                 //
    ////////////////////////////////////////////////////////////////////////////
    myzAttachPanel m_attachPanel = new myzAttachPanel(myzAttachPanel.MODE_ENABLE);
    
    ////////////////////////////////////////////////////////////////////////////
    //                              LEFT SIDEBAR                              // 
    ////////////////////////////////////////////////////////////////////////////
    myzButton    m_newButton    = new myzButton()
    {
        @Override
        public void buttonPressed()
        {
            m_currentMovement = new CurrentMovement(m_connection);
            removeFrameData();
        }
        
    };
    
    myzButton    m_saveButton   = new myzButton()
    {
        @Override
        public void buttonPressed()
        {
            save();
        }
        
    };
    
    myzButton    m_cancelButton = new myzButton()
    {
        @Override
        public void buttonPressed()
        {
            m_currentMovement = new CurrentMovement(m_connection);
            
            //Lock search result buttons
            m_nextRecord.setDisable(true);
            m_previouseRecord.setDisable(true);
            m_recordsCount.setText("");
           
            //Unlock new button & search button
            m_newButton.setDisable(false);
            m_searchButton.setDisable(false);
            
            removeFrameData();
        }
    };

    myzButton    m_searchButton = new myzButton()
    {
      @Override
      public void buttonPressed()
      {
        if(search())
        {
            int vSize = m_vCurrentMovement.size() ;
            
            m_nextRecord.setDisable(vSize == 1);
            m_previouseRecord.setDisable(true);
            
            m_recordsCount.setText( "1/" + vSize);
            m_currentMovement = m_vCurrentMovement.get(m_currentMovementC);
           
            //Lock new button & search button
            m_newButton.setDisable(true);
            m_searchButton.setDisable(true);
            
            load();
        }
        else
        {
            m_nextRecord.setDisable(true);
            m_previouseRecord.setDisable(true);
            m_recordsCount.setText("");
            removeFrameData();
        }
      }
    };
    
    //TODO
    myzButton m_screenSnapshot  = new myzButton()
    {
      @Override
      public void buttonPressed()
      {
        FileChooser  fileChooser  = new FileChooser(); 
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Pdf Files (*.pdf)", "*.pdf"));
        fileChooser.setInitialFileName(new Date(System.currentTimeMillis()).toString());

                  
        File file     = fileChooser.showSaveDialog(getScene().getWindow());
        File tempFile = new File(file.getPath() + ".png");
        
        try (FileOutputStream out = new FileOutputStream( tempFile ) ) 
        {
            javax.imageio.ImageIO.write(ScreenSnapshot.centerSnapshot(CarsPlatesFrame.this) , "png", out);
            out.flush();
            out.close();
        } 
        catch (IOException ex)
        {
          alertMessage(ex.getMessage());
          logWriter.write(ex);
        }
        
        try
        {
            com.lowagie.text.Image  image  = com.lowagie.text.Image.getInstance(file.getPath() + ".png");
            image.scalePercent(1);
            Document doc = new Document(new com.lowagie.text.Rectangle(image.getScaledWidth(), image.getScaledHeight()));
            FileOutputStream fos = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fos);
            doc.open();
            doc.newPage();
            image.setAbsolutePosition(0, 0);
            doc.add(image);
            fos.flush();
            doc.close();
            fos.close();
            //Delete the temp image 
            tempFile.delete();
            ReportFrame.browse(file, false);
        }
        catch(IOException | DocumentException | InterruptedException ex)
        {
            alertMessage(ex.getMessage());
            logWriter.write(ex);
        }
      }
    };
    
    myzButton m_advanceSettingsButton = new myzButton()
    {
        @Override
        public void buttonPressed()
        {
            Main.SCENE.setRoot(new AdvanceSettingsFrame());
        }
    };
    
    ////////////////////////////////////////////////////////////////////////////
    //                                 HEADER                                 //
    ////////////////////////////////////////////////////////////////////////////
    myzButton m_nextRecord      = new myzButton()
    {
      @Override
      public void buttonPressed()
      {
            m_currentMovementC++;
            int vSize = m_vCurrentMovement.size() ;
            if( vSize > m_currentMovementC)
            {
                m_currentMovement = m_vCurrentMovement.get(m_currentMovementC);
                m_previouseRecord.setDisable(false);
                m_recordsCount.setText( ( m_currentMovementC + 1 ) + "/" + m_vCurrentMovement.size());
                load();
            }
            //If this is the last record then disable next button
            m_nextRecord.setDisable(vSize == m_currentMovementC + 1);
      }
    };
    
    myzButton m_previouseRecord = new myzButton()
    {
      @Override
      public void buttonPressed()
      {
            m_currentMovementC--;
            if( m_currentMovementC >= 0)
            {
                m_currentMovement = m_vCurrentMovement.get(m_currentMovementC);
                m_nextRecord.setDisable(false);
                m_recordsCount.setText( ( m_currentMovementC + 1 ) + "/" + m_vCurrentMovement.size());
                load();
            }
            //If this is the first record then disable previouse button
            m_previouseRecord.setDisable(m_currentMovementC - 1 < 0 );
      }
    };
    
    myzLabel m_recordsCount     = new myzLabel();
       
    ////////////////////////////////////////////////////////////////////////////
    //                               RIGHT SIDBAR                             //
    ////////////////////////////////////////////////////////////////////////////
    myzButton m_receivedFromButton      = new myzButton("استلام لوحة")
    {
      @Override
      public void buttonPressed()
      {
        OperationFrame receivedPlateFrame ;
        if(m_currentMovement.getPlateStatus() == Plate.PLATE_STATUS_EXISTED)
        {
            if(!myzMessage.confirmMessage(" اللوحة موجودة بالمخزن لايمكن استلامها هل تريد الدخول للتعديل ؟"))
                return;
            else
            {
                receivedPlateFrame = new OperationFrame("استلام لوحة " , m_currentMovement.getPlate()
                                    , m_currentMovement , MODE_RECEIVED_PLATE );
                receivedPlateFrame.load();
                Main.SCENE.setRoot( receivedPlateFrame );
                return;
            }
        }
        else if (m_currentMovement.getPlateStatus() == Plate.PLATE_STATUS_DROPPED)
        {
            if(!myzMessage.confirmMessage(" اللوحة مسقطة لا يمكن استلامها هل تريد الدخول للتعديل ؟"))
                return  ;
            else
            {
                receivedPlateFrame = new OperationFrame("استلام لوحة " , m_currentMovement.getPlate()
                                    , m_currentMovement , MODE_RECEIVED_PLATE );
                receivedPlateFrame.load();
                Main.SCENE.setRoot( receivedPlateFrame );
                return;
            }
        }

        receivedPlateFrame = 
        new OperationFrame("استلام لوحة " , m_currentMovement.getPlate(), m_currentMovement , MODE_RECEIVED_PLATE );
        Main.SCENE.setRoot( receivedPlateFrame );
      }
    };
    
    myzButton m_deliveredToButton       = new myzButton("تسليم لوحة")
    {
      @Override
      public void buttonPressed()
      {
        OperationFrame deliveredPlateFrame ;
        if(m_currentMovement.getPlateStatus() == Plate.PLATE_STATUS_DELIVERED)
        {
            if(!myzMessage.confirmMessage(" اللوحة مسلمة لايمكن تسليمها مرة أخرى هل تريد الدخول للتعديل ؟"))
                return  ;
            else
            {
                deliveredPlateFrame = new OperationFrame( "تسليم لوحة " , m_currentMovement.getPlate() 
                        , m_currentMovement , MODE_DELIVERED_PLATE );
                deliveredPlateFrame.load();
                Main.SCENE.setRoot(deliveredPlateFrame);
                return;
            }
 
        }
        else if (m_currentMovement.getPlateStatus() == Plate.PLATE_STATUS_DROPPED)
        {
            if(!myzMessage.confirmMessage(" اللوحة مسقطة لا يمكن تسليمها هل تريد الدخول للتعديل ؟"))
                return  ;
            else
            {
                deliveredPlateFrame = new OperationFrame( "تسليم لوحة " , m_currentMovement.getPlate() 
                        , m_currentMovement , MODE_DELIVERED_PLATE );
                deliveredPlateFrame.load();
                Main.SCENE.setRoot(deliveredPlateFrame);
                return ;
            }
        }
        
        deliveredPlateFrame = 
        new OperationFrame( "تسليم لوحة " , m_currentMovement.getPlate() , m_currentMovement , MODE_DELIVERED_PLATE );
        Main.SCENE.setRoot(deliveredPlateFrame);
      }
    };
    
    myzButton m_transferButton          = new myzButton("إسقاط لوحة")
    {
      @Override
      public void buttonPressed()
      {
          OperationFrame transferPlateFrame ;
        if(m_currentMovement.getPlateStatus() == Plate.PLATE_STATUS_DELIVERED)
        {
            if(!myzMessage.confirmMessage(" اللوحة مسلمة لايمكن إسقاطها هل تريد الدخول للتعديل ؟"))
                return  ;
            else
            {
                transferPlateFrame = new OperationFrame( "إسقاط لوحة" , m_currentMovement.getPlate() 
                                     , m_currentMovement , MODE_DROPPED_PLATE );
                transferPlateFrame.load();
                Main.SCENE.setRoot( transferPlateFrame );
                return;
            }
        }
        else if (m_currentMovement.getPlateStatus() == Plate.PLATE_STATUS_DROPPED)
        {
            if(!myzMessage.confirmMessage(" اللوحة مسقطة لايمكن إسقاطها مرة أخرى هل تريد الدخول للتعديل ؟"))
                return  ;
            else
            {
                transferPlateFrame = new OperationFrame( "إسقاط لوحة" , m_currentMovement.getPlate() 
                                    , m_currentMovement , MODE_DROPPED_PLATE );
                transferPlateFrame.load();
                Main.SCENE.setRoot( transferPlateFrame );
                return;
            }
        }
        
        transferPlateFrame =
        new OperationFrame( "إسقاط لوحة" , m_currentMovement.getPlate() , m_currentMovement , MODE_DROPPED_PLATE );
        Main.SCENE.setRoot( transferPlateFrame );
      }
    };
    
    //Renew --> change the card and the describe of the vehicle
    myzButton m_renewVehicleCardButton  = new myzButton("تجديد بطاقة ")
    {
      @Override
      public void buttonPressed()
      {
        if (m_currentMovement.getPlateStatus() == Plate.PLATE_STATUS_DROPPED)
        {
            myzMessage.alertMessage("اللوحة مسقطة لا يمكن تجديد البطاقة ");
            return;
        }
        FramesParent renewVehicleCardFrame = 
        new OperationFrame( "تجديد بطاقة " , m_currentMovement.getPlate() , m_currentMovement , MODE_RENEW_CARD );
        Main.SCENE.setRoot( renewVehicleCardFrame );

      }
    };
    

    
    ////////////////////////////////////////////////////////////////////////////
    //                               METHODS                                  //
    ////////////////////////////////////////////////////////////////////////////
    public void removeFrameData()
    {            
        try
        {
            Field []     fields   = CarsPlatesFrame.class.getDeclaredFields();
            myzComponent component = null ;
            for( Field field : fields)
            {
                Class className = field.getType() ;
                if ( Class.forName("com.myz.component.myzComponent").isAssignableFrom(className) )
                {
                    component = (myzComponent)field.get(this);
                    component.removeData();
                    component.resetStyle();
                }
            }
        }
        catch(SecurityException | ClassNotFoundException | IllegalArgumentException | IllegalAccessException ex)
        {
            logWriter.write(ex);
        }
        m_attachPanel.removeChildren();
        initFrameBasicData();
    }

    //return true if there is mandatory is empty
    public boolean checkEmptyAndMandatory()
    {
        try
        {
            Field []     fields   = CarsPlatesFrame.class.getDeclaredFields();
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

    public void save()
    {
        //Check mandatory fields
        if( checkEmptyAndMandatory())
        {
            alertMessage("الرجاء تعبئة الحقول الإجبارية "  );
            return ;
        }
        //If edit and save check confirmation
        if(m_currentMovement.getPnr() > 0)
        {
            if(!confirmMessage("هل أنت متأكد من حفظ التعديلات ؟؟"  ))
                return;
        }
        
        //Fill plates data 
        m_currentMovement.getPlate().setPlateNo(m_plateNo.getText());
        m_currentMovement.getPlate().setPlateStartDate(m_plateStartDate.getDate());
        m_currentMovement.getPlate().setPnrPlateCity(m_plateCity.getIntValue());
        m_currentMovement.getPlate().setPnrPlateType(m_plateType.getIntValue());
        m_currentMovement.getPlate().setPnrPlateReturn(m_plateReturn.getIntValue());
        
        int pnrCurrentMovement = m_currentMovement.getPnr();
        
        //Check if the plates already exist
        if(m_currentMovement.getPlate().isExist())
        {
            alertMessage("رقم اللوحة موجود مسبقا"  );
            return ;
        }
        
        //Fill vehicle data 
        // add the if statement by yazan 2020-12-05
        if ( checkAllVehicleDataInserted() )
        {
            m_currentMovement.getVehicle().setChassisNo(m_chassisNo.getText());
            m_currentMovement.getVehicle().setEngineNo(m_engineNo.getText());
            m_currentMovement.getVehicle().setPnrCarColor(m_vehicleColor.getIntValue());
            m_currentMovement.getVehicle().setPnrCarManufacturer(m_vehicleManufacturer.getIntValue());
            m_currentMovement.getVehicle().setProductionYear(m_productionYear.getText());
        
            int vehicleType = 0 ;
            if (m_bickRadio.isSelected())
                vehicleType = BICK ;
            if(m_carRadio.isSelected())
                vehicleType = CAR ;
            m_currentMovement.getVehicle().setType(vehicleType);
            //Check if the vehicle already exist
            if(m_currentMovement.getVehicle().isUsed( pnrCurrentMovement ))
            {
                alertMessage("رقم الشاسيه مستخدم حاليا "  );
                return ;
            } 
        }
        else
            return;//End the save process here

        //Fill vehicle card data 
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
        m_currentMovement.getVehicleCard().setCardCode(m_cardCode.getText());
        m_currentMovement.getVehicleCard().setCardNo(m_cardNo.getText());
        m_currentMovement.getVehicleCard().setStartDate(startDate);
        
        m_currentMovement.getVehicleCard().setCardStatus(VehicleCard.CARD_STATUS_EXISTED);
        //If vehicle fields not empty the card is described
        m_currentMovement.getVehicleCard().setCardDescribed( !( m_chassisNo.getValue() == null || m_chassisNo.getText().length() < 0 ));
        
        
        int cardTerm = 0 ;
        if(m_tempCardRadio.isSelected())
        {
            cardTerm = TEMP ;
            m_currentMovement.getVehicleCard().setEndDate(endDate);
        }
        if(m_permanentCardRadio.isSelected())
            cardTerm = PERMANENT ;
        
        m_currentMovement.getVehicleCard().setCardTerm(cardTerm);

        if(m_currentMovement.getVehicleCard().isExist(  ) )
        {
            alertMessage("رقم البطاقة موجود مسبقا "  );
            return ;
        }
        
        
        //The default selected at frame is (status exist)
        int plateStatus = -1 ;
        if(m_statusExist.isSelected())
            plateStatus = Plate.PLATE_STATUS_EXISTED ;
        else if(m_statusDelivered.isSelected())
            plateStatus = Plate.PLATE_STATUS_DELIVERED ;
        else if(m_statusReturned.isSelected())
            plateStatus = Plate.PLATE_STATUS_DROPPED;
        m_currentMovement.setPlateStatus(plateStatus);
        
        // الملاحظات
        m_currentMovement.setNote(m_note.getText());
        
        //SAVE
        myzMessage.noteMessage( m_currentMovement.saveCurrentMovement());
        
        //Reload after save
        removeFrameData();
        load(); 
    }
    
    private boolean search()
    {
        Vector<myzComponent> searchFields = new Vector<>();
        m_vCurrentMovement                = new Vector<>();
        m_currentMovementC                = 0 ; 
        try
        {
            Field []     buttons   = CarsPlatesFrame.class.getDeclaredFields();
            myzComponent component = null ;
            for( Field field : buttons)
            {
                Class className = field.getType() ;
                if ( Class.forName("com.myz.component.myzComponent").isAssignableFrom(className) )
                {
                    component = (myzComponent)field.get(this);
                    if(component.getFieldName() != null && !component.getFieldName().equals("") )
                        searchFields.addElement(component);
                }
            }
        }
        catch(SecurityException | ClassNotFoundException | IllegalArgumentException | IllegalAccessException ex)
        {
            logWriter.write(ex);
        }
        String where = "";
        for(int i = 0 ; i < searchFields.size() ; i++)
        {
            if(searchFields.get(i).getValue() != null)
            {
                if(where.length() <= 0 )
                    where += searchFields.get(i).getSQLWhere() ;
                else
                    where += " AND " +  searchFields.get(i).getSQLWhere() ;
            }
        }
        
        if(where.length() == 0)
        {
            alertMessage("الرجاء تعبئة أحد حقول البحث أولا  ");
            return false ;
        }
        String SQL   =  " SELECT *  FROM CURRENT_MOVEMENT " ;
               SQL  +=  " INNER JOIN PLATE               ON ( CURRENT_MOVEMENT.PNR_PLATE          = PLATE.PNR)\n" ;
               SQL  +=  " LEFT OUTER JOIN PLATE_CITY     ON ( PLATE.PNR_PLATE_CITY                = PLATE_CITY.PNR)\n   ";
               SQL  +=  " LEFT OUTER JOIN VEHICLE        ON ( CURRENT_MOVEMENT.PNR_VEHICLE        = VEHICLE.PNR)\n" ;
               SQL  +=  " LEFT OUTER JOIN VEHICLE_CARD   ON ( CURRENT_MOVEMENT.PNR_VEHICLE_CARD   = VEHICLE_CARD.PNR)\n" ;
               SQL  +=  " LEFT OUTER JOIN DELIVERED_INFO ON ( CURRENT_MOVEMENT.PNR_DELIVERED_INFO = DELIVERED_INFO.PNR)\n" ;
               SQL  +=  " WHERE " + where ;
        Statement       st = null ;
        ResultSet       rs = null ;
        CurrentMovement cM = null ;
        try
        {
            st = m_connection.m_connection.createStatement() ;
            rs = st.executeQuery(SQL);
            while(rs.next())
            {
                cM = new CurrentMovement(m_connection);
                cM.get(rs);
                m_vCurrentMovement.addElement(cM);
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
        if(m_vCurrentMovement.isEmpty() )
        {
            alertMessage("لا يوجد أي نتائج لهذا البحث");
            return false;
        }
        return true ;
    }
    
    public void load()
    {
        removeFrameData();
        //Reload movement reocord after insert
        int currentMovementPnr = m_currentMovement.getPnr();
        
        m_currentMovement = new CurrentMovement(m_connection);
        m_currentMovement.get(currentMovementPnr);
        m_currentMovement.initCurrentMovement();
        
        Plate plate = m_currentMovement.getPlate();
        if(plate != null)
        {
            m_plateNo.setText(plate.getPlateNo());
            m_plateStartDate.setDate(plate.getPlateStartDate());
            m_plateCity.select(plate.getPnrPlateCity());
            m_plateType.select(plate.getPnrPlateType());
            m_plateReturn.select(plate.getPnrPlateReturn());            
        }
        
        Vehicle vehicle = m_currentMovement.getVehicle();
        if(vehicle != null)
        {
            m_vehicleColor.select(vehicle.getPnrCarColor());
            m_vehicleManufacturer.select(vehicle.getPnrCarManufacturer());
            m_chassisNo.setText(vehicle.getChassisNo());
            m_engineNo.setText(vehicle.getEngineNo());
            m_productionYear.setText(vehicle.getProductionYear());
            int vehicleType = vehicle.getType() ;
            if(vehicleType == CAR)
                m_carRadio.setSelected(true);
            else if(vehicleType == BICK)
                m_bickRadio.setSelected(true);
        }
        
        VehicleCard vehicleCard = m_currentMovement.getVehicleCard() ;
        if(vehicleCard != null)
        {
            m_cardNo.setText(vehicleCard.getCardNo());
            m_cardCode.setText(vehicleCard.getCardCode());
            m_startDate.setDate(vehicleCard.getStartDate());
            m_endDate.setDate(vehicleCard.getEndDate());
            
            int cardTerm = vehicleCard.getCardTerm();
            if(cardTerm == PERMANENT)
            {
                m_permanentCardRadio.setSelected(true);
                m_endDate.setVisible(false);
                m_endDate.setIsMandatory(false);
            }
            else if(cardTerm == TEMP)
            {
                m_tempCardRadio.setSelected(true);
                m_endDate.setDate(vehicleCard.getEndDate());
            }    
        }
             
        DeliveredInfo deliveredInfo = m_currentMovement.getDeliveredInfo() ;
        if(deliveredInfo != null)
        {
            m_attachPanel.setPnrDeliveredInfo(deliveredInfo.getPnr());
            m_attachPanel.loadFiles();
            
            m_receiverName.setText(deliveredInfo.getReceiverName());
            m_reciverCellular.setText(deliveredInfo.getReceiverCellular());
            m_receiverPhone.setText(deliveredInfo.getReceiverPhone());
            m_deliveredUnit.select(deliveredInfo.getPnrDeliveredUnit());
            m_deliveredDate.setDate(deliveredInfo.getDeliveredDate());
            //
            int receivedTerm = deliveredInfo.getDeliveredTerm();
            if(receivedTerm == PERMANENT)
                m_permanentDeliveredRadio.setSelected(true);
            else if(receivedTerm == TEMP)
            {
                m_tempDeliveredRadio.setSelected(true);
                m_deliveredEndDate.setDate(deliveredInfo.getDeliveredEndDate());
            }                
        } 
        
        //Get movement data 
        m_note.setText(m_currentMovement.getNote());
        int plateStatus = m_currentMovement.getPlateStatus() ;
        
        switch (plateStatus) 
        {
            case Plate.PLATE_STATUS_EXISTED:
                m_statusExist.setSelected(true);
                m_attachPanel.disable();
                break;
            case Plate.PLATE_STATUS_DELIVERED:
                m_statusDelivered.setSelected(true);
                break;
            case Plate.PLATE_STATUS_DROPPED:
                m_statusReturned.setSelected(true);
                m_attachPanel.disable();
                break;
            default:
                break;
        }
        
        //Enable plate operations buttons after load it 
        if(m_mode == MODE_ADD && Main.USER.canExtendingPlate())
        {
            m_receivedFromButton.setDisable(false);
            m_deliveredToButton.setDisable(false);
            m_transferButton.setDisable(false);
            m_renewVehicleCardButton.setDisable(false);          
        }
        
        //TODO******************************************************************
       myzComboBox comboBox = null ;
        for (Node node : getCenterPane().getChildren())
        {
            if(node instanceof myzComboBox)
            {
                comboBox = (myzComboBox) node ;
                comboBox.refreshData();
            }
            
        }

    }
    
    //This function used @ plates expired table on dbClick
    public void load(String plateNo , int pnrPlateCity)
    {
        m_currentMovement.getByPlateNoAndCity(plateNo, pnrPlateCity);
        load();
    }
    
    public void delete()
    {        
        removeFrameData();
    }

    @Override
    public void refreshFrame()
    {
        //Reload after edit on plate from sub frames
        if(m_currentMovement.getPnr() > 0){load();}
        
 

    }
    
    @Override
    public void initHeader() 
    {
        if(m_mode == MODE_ADD || m_mode == MODE_SEARCH)
        {
            m_nextRecord.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/next.png") ) ) );
            m_previouseRecord.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/previouse.png") ) ) );
            //Set enable when the search is give me the results
            m_nextRecord.setDisable(true);
            m_previouseRecord.setDisable(true);

            m_recordsCount.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

            getHeader().setSpacing(30);
            getHeader().setAlignment(Pos.CENTER);

            Region region1 = new Region();
            Region region2 = new Region();
            HBox.setHgrow(region1, Priority.ALWAYS);
            HBox.setHgrow(region2, Priority.ALWAYS);

            getHeader().getChildren().add( 0 , region1 );
            getHeader().getChildren().addAll( region2 ,  m_previouseRecord , m_recordsCount , m_nextRecord);
        }
    }

    @Override
    public void initCenter()
    {
        getCenterPane().setHgap(10);
        getCenterPane().setVgap(10);
        getCenterPane().setAlignment(Pos.TOP_CENTER);
        
        int column = 0 ;
        int row    = 0 ;
        // رقم اللوحة والمحافظة 
        getCenterPane().add(m_plateNoLabel      , column++ , row );
        getCenterPane().add(m_plateNo           , column++ , row );

        getCenterPane().add(m_plateCityLabel    , column++ , row );
        getCenterPane().add(m_plateCity         , column++ , row );        

        // نوع اللوحة وعائدية اللوحة
        getCenterPane().add(m_plateTypeLabel    , column++ , row );
        getCenterPane().add(m_plateType         , column++ , row );
        
        getCenterPane().add(m_plateReturnLabel  , column++ , row );
        getCenterPane().add(m_plateReturn       , column++ , row );
               
        column = 0 ;
        row++;
                
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
        
        // نوع البطاقة دائمة او مؤقتة  و تاريخ الإدخال 
        getCenterPane().add(m_plateStartDateLabel  , column++ , row );
        getCenterPane().add(m_plateStartDate       , column++ , row );
        
        
        getCenterPane().add(m_cardPeriodTypeLabel , column++ , row );
        getCenterPane().add(m_tempCardRadio       , column++ , row );
        getCenterPane().add(m_permanentCardRadio  , column++ , row );
        
        m_permanentCardRadio.setToggleGroup(m_cardPeriodGroup ) ;
        m_tempCardRadio.setToggleGroup(m_cardPeriodGroup ) ;
        m_tempCardRadio.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean wasPreviouslySelected, Boolean isNowSelected) -> 
        {
            if(isNowSelected)
            {
                m_endDateLabel.setVisible(true);
                m_endDate.setVisible(true);
                m_endDate.setIsMandatory(true);
            }
            else
            {
                m_endDateLabel.setVisible(false);
                m_endDate.setVisible(false);
                m_endDate.setIsMandatory(false);
            }
        });
                      
        //معلومات الية 
        column = 0 ;
        row   += 4 ;
        
        getCenterPane().add(m_engineNoLabel            , column++ , row);
        getCenterPane().add(m_engineNo                 , column++ , row);

        getCenterPane().add(m_vehicleColorLabel        , column++  , row);
        getCenterPane().add(m_vehicleColor             , column++ , row);

        getCenterPane().add(m_vehicleManufacturerLabel , column++ , row);
        getCenterPane().add(m_vehicleManufacturer      , column++ , row);

        column = 0 ;
        row++ ;
        
        // نوع الية النقل سيارة او دراجة نارية 
        getCenterPane().add(m_chassisNoLabel           , column++ , row);
        getCenterPane().add(m_chassisNo                , column++ , row);
        
        getCenterPane().add(m_productionDateLabel      , column++ , row);
        getCenterPane().add(m_productionYear           , column++ , row);
        
        getCenterPane().add(m_carRadio                 , column++ , row );
        getCenterPane().add(m_bickRadio                , column++ , row );
        
        m_carRadio.setToggleGroup(m_typeGroup);
        m_bickRadio.setToggleGroup(m_typeGroup);
        
        column = 0 ;
        row++ ;
        
        getCenterPane().add(m_plateStatusLabel  , column++ , row );
        getCenterPane().add(m_statusExist       , column++ , row );
        getCenterPane().add(m_statusDelivered   , column++ , row );
        getCenterPane().add(m_statusReturned    , column++ , row );
        
        m_statusExist.setToggleGroup(m_plateStatusGroup);
        m_statusDelivered.setToggleGroup(m_plateStatusGroup);
        m_statusReturned.setToggleGroup(m_plateStatusGroup);
        
        m_statusExist.setDisable(true);
        m_statusDelivered.setDisable(true);
        m_statusReturned.setDisable(true);


        column = 0 ;
        row   += 3 ;
        
        // الملاحظات
        getCenterPane().add(m_noteLabel            , column++ , row , 1 , 1);
        getCenterPane().add(m_note                 , column++ , row , 3 , 1);
        
        if(m_mode == MODE_SEARCH)
        {
            column = 0 ;
            row++ ;
            // اسم المستلم و الجهة المستلمة
            getCenterPane().add(m_receiverNameLabel   , column++ , row );
            getCenterPane().add(m_receiverName        , column++ , row );

            getCenterPane().add(m_deliveredUnitLabel   , column++ , row );
            getCenterPane().add(m_deliveredUnit        , column++ , row );

            // هاتف المستلم أرضي و محمول
            getCenterPane().add(m_reciverPhoneLabel    , column++ , row );
            getCenterPane().add(m_reciverCellular      , column++ , row );
            getCenterPane().add(m_receiverPhone        , column++ , row );

            
            column = 0 ;
            row++ ;

            // تاريخ الاستلام
            getCenterPane().add(m_deliveredDateLabel   , column++ , row );
            getCenterPane().add(m_deliveredDate        , column++ , row );

            // مدة التسليم
            getCenterPane().add(m_deliveryPeriodLabel     , column++ , row );
            getCenterPane().add(m_permanentDeliveredRadio , column++ , row );
            getCenterPane().add(m_tempDeliveredRadio      , column++ , row );
            getCenterPane().add(m_deliveredEndDate        , column++ , row );

            
            m_tempDeliveredRadio.setToggleGroup(m_deliveredPeriodGroup);
            m_permanentDeliveredRadio.setToggleGroup(m_deliveredPeriodGroup);

           //To hide the delivery Period Date if it's permanent period
           m_deliveredEndDate.setVisible(false);
           m_tempDeliveredRadio.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean wasPreviouslySelected, Boolean isNowSelected) -> 
           {
               if(isNowSelected)
                   m_deliveredEndDate.setVisible(true);
               else
                   m_deliveredEndDate.setVisible(false);
            });
        }
                
    }
    
    @Override
    public void initLeftSidebar()
    {      
        m_searchButton.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/search.png") ) ) );
        m_newButton.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/new.png") ) ) );
        m_saveButton.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/save.png") ) ) );
        m_cancelButton.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/cancel.png") ) ) );
        m_screenSnapshot.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/print.png") ) ) );
        m_advanceSettingsButton.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/advance_settings.png") ) ) );

        KeyCombination keyComb_CTL_S = new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN);
        KeyCombination keyComb_CTL_R = new KeyCodeCombination(KeyCode.R,KeyCombination.CONTROL_DOWN);
        KeyCombination keyComb_CTL_N = new KeyCodeCombination(KeyCode.N,KeyCombination.CONTROL_DOWN);
        
        switch (m_mode)
        {
            case MODE_ADD:
                if(Main.USER.canEdit())
                {
                    addToLeft(m_searchButton);
                }
                if(Main.USER.canAdd())
                {
                    addToLeft(m_saveButton);
                }
                if(Main.USER.canMakeAdvanceSettings())
                {
                    addToLeft(m_advanceSettingsButton);
                }
                addToLeft(m_newButton);
                addToLeft(m_cancelButton);
                addToLeft(m_screenSnapshot);
                
                break;
            case MODE_SEARCH:
                editHeaderText("إستعلام عن لوحة");
                addToLeft(m_searchButton);
                addToLeft(m_newButton);
                addToLeft(m_cancelButton);
                break;
            case MODE_SHOW :
                editHeaderText("معاينة ");
                break;
        }
        
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
    
    @Override
    public void initFrameBasicData()
    {
        //For new record it's default exist
        m_statusExist.setSelected(true);
        m_carRadio.setSelected(true);
        m_tempCardRadio.setSelected(true);
        
        //Set Mandatory fields 
        m_plateNo.setIsMandatory(true);
        m_plateStartDate.setIsMandatory(true);
        m_plateCity.setIsMandatory(true);
        m_plateType.setIsMandatory(true);
        m_plateReturn.setIsMandatory(true);
        m_cardNo.setIsMandatory(true);
        m_startDate.setIsMandatory(true);
        m_endDate.setIsMandatory(true);
        
        m_endDate.setVisible(true);
        m_endDateLabel.setVisible(true);
        
        //Set fields DB name help for search
        m_plateNo.setFieldName("PLATE_NO");
        m_plateCity.setFieldName("PNR_PLATE_CITY");
        m_chassisNo.setFieldName("CHASSIS_NO");
        m_cardNo.setFieldName("CARD_NO");
        m_cardCode.setFieldName("CARD_CODE");
        
         
        //Disable plate operations buttons when (new plate - cancel plate) 
        if(m_mode == MODE_ADD && Main.USER.canExtendingPlate())
        {
            m_receivedFromButton.setDisable(true);
            m_deliveredToButton.setDisable(true);
            m_transferButton.setDisable(true);
            m_renewVehicleCardButton.setDisable(true);
        }
        
    }
    
    @Override
    public void initRightSidebar() 
    {
        if(m_mode == MODE_ADD && Main.USER.canExtendingPlate())
        {
            addToRight(m_receivedFromButton);
            addToRight(m_deliveredToButton);
            addToRight(m_transferButton);
            addToRight(m_renewVehicleCardButton);
            
        }
    }
    
    
    
}
