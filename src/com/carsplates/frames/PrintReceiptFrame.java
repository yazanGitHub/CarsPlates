/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.frames;

import com.carsplates.main.Main;
import com.carsplates.reports.ReceiptReport;
import com.carsplates.smart.records.CurrentMovement;
import com.carsplates.smart.records.Plate;
import com.myz.component.myzButton;
import com.myz.component.myzComboBox;
import com.myz.component.myzLabel;
import com.myz.component.myzTextField;
import com.myz.connection.ConnectionContext;
import com.myz.connection.arConnectionInfo;
import com.myz.log.logWriter;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.stage.FileChooser;
import java.util.Map;
import java.util.HashMap;
import javafx.geometry.NodeOrientation;
import myzMessage.myzMessage;

/**
 *
 * @author Montazar Hamoud
 */
public class PrintReceiptFrame extends FramesParent
{
    //Constructor
    public PrintReceiptFrame() 
    {
        super("طباعة وصل استلام");
        setCenterOrientation(NodeOrientation.RIGHT_TO_LEFT);
        super.initFrame(false, true, false);
    }
    
    //Data member
    myzButton         m_printButton = new myzButton("طباعة ")
    {
        @Override
        public void buttonPressed()
        {
            print();
        }
    };
    myzLabel          m_plateLabel     = new myzLabel("رقم اللوحة ");
    myzTextField      m_plateNo        = new myzTextField();
    myzLabel          m_plateCityLabel = new myzLabel("المحافظة ");
    myzComboBox       m_plateCity      = new myzComboBox("SELECT * FROM PLATE_CITY", "T_NAME" , false);
    ConnectionContext m_connection     = arConnectionInfo.getConnectionContext() ;
    
    //////////////////////////////////////////////////////////////////
    //                          Left sidebar                        // 
    //////////////////////////////////////////////////////////////////

    
    public void print()
    {
        
        String plateNo = m_plateNo.getText();
        int    pnrCity = m_plateCity.getIntValue();
        if(plateNo != null && "".equals(plateNo))
        {
            myzMessage.alertMessage("الرجاء ادخال رقم اللوحة "  );
            return ;
        }
        if(pnrCity == -1)
        {
            myzMessage.alertMessage("الرجاء اختيار المحافظة "  );
            return ;
        }
        CurrentMovement current = new CurrentMovement(m_connection);
        
        if(!current.getByPlateNoAndCity(plateNo, pnrCity))
        {
            myzMessage.alertMessage("الرجاء التأكد من معلومات اللوحة  "  );
            return ;
        }
        if ( current.getPlateStatus() != Plate.PLATE_STATUS_DELIVERED)
        {
            myzMessage.alertMessage("إن اللوحة غير مسلمة الرجاء التاكد من ذلك"  );
            return ;
        }

        
        String receiverName    = "";
        String actDeliverdDate = "";
        String deliverName     = "";//TODO
        String vecManName      = "";
        String vecKM           = "";//TODO
        String vecColor        = "";
        String vecModel        = "";//TODO
        String plateType       = "";
        String receiptNo       = "";//todo
        String vecChassisNum   = "";
        String cardEndDate     = "";
        String cardDescribed   = "";
        
        String SQL  = " SELECT   DELIVERED_INFO.RECEIVER_NAME                , DELIVERED_INFO.ACTUAL_DELIVERED_DATE AS ACT_DATE";
               SQL += "        , VEHICLE_MANUFACTURER.T_NAME AS VEC_MAN_NAME , VEHICLE_COLOR.T_NAME AS VEC_COLOR  ";
               SQL += "        , VEHICLE_CARD.END_DATE                       , PLATE_TYPE.T_NAME PLATE_TYPE_NAME ";
               SQL += "        , VEHICLE.CHASSIS_NO AS VEC_CHASSIS_NO        , VEHICLE.PRODUCTION_YEAR AS PROD_YEAR  ";
               SQL += "        , PLATE.PNR AS PNR_PLATE                      , DELIVERED_COUNTER   ";
               SQL += "        , VEHICLE_CARD.CARD_DESCRIBED";
               SQL += " FROM CURRENT_MOVEMENT " ;
               SQL += "               LEFT OUTER JOIN DELIVERED_INFO ON (CURRENT_MOVEMENT.PNR_DELIVERED_INFO = DELIVERED_INFO.PNR ) " ;// لاحضار اسم المستلم
               SQL += "               LEFT OUTER JOIN VEHICLE              ON (CURRENT_MOVEMENT.PNR_VEHICLE       = VEHICLE.PNR) ";
               SQL += "               LEFT OUTER JOIN VEHICLE_MANUFACTURER ON ( VEHICLE.PNR_VEHICLE_MANUFACTURER  = VEHICLE_MANUFACTURER.PNR ) ";//لاحضار نوع السيارة 
               SQL += "               LEFT OUTER JOIN VEHICLE_COLOR        ON ( VEHICLE.PNR_VEHICLE_COLOR         = VEHICLE_COLOR.PNR )";//لاحضار لون السيارة
               SQL += "               LEFT OUTER JOIN PLATE                ON ( CURRENT_MOVEMENT.PNR_PLATE        = PLATE.PNR ) ";//لاحضار رقم اللوحة
               SQL += "               LEFT OUTER JOIN PLATE_TYPE           ON ( PLATE.PNR_PLATE_TYPE              = PLATE_TYPE.PNR ) ";//لاحصار نوع اللوحة
               SQL += "               LEFT OUTER JOIN VEHICLE_CARD         ON ( CURRENT_MOVEMENT.PNR_VEHICLE_CARD = VEHICLE_CARD.PNR) ";// لاحصار تاربخ نهاية البطاقة
               SQL += " WHERE CURRENT_MOVEMENT.PNR_PLATE = " + current.getPnrPlate();
        Statement st  = null ;
        ResultSet rs  = null ;
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if(rs.next())
            {
                receiverName     = rs.getString("RECEIVER_NAME");
                vecManName       = rs.getString("VEC_MAN_NAME");
                vecColor         = rs.getString("VEC_COLOR");
                plateType        = rs.getString("PLATE_TYPE_NAME");
                vecChassisNum    = rs.getString("VEC_CHASSIS_NO");
                vecModel         = rs.getString("PROD_YEAR");
                cardEndDate      = rs.getString("END_DATE");
                actDeliverdDate  = rs.getString("ACT_DATE");
                receiptNo        = rs.getString("PNR_PLATE") + "م/" + rs.getString("DELIVERED_COUNTER");
                cardDescribed    = rs.getBoolean("CARD_DESCRIBED") ? "true" : "false";
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
        FileChooser      fileChooser = new FileChooser();
        FileChooser.ExtensionFilter  extFilter   = new FileChooser.ExtensionFilter( " (*.pdf)", "*." + "pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setSelectedExtensionFilter(extFilter);
        File file = fileChooser.showSaveDialog(Main.PRIMARY_STAGE);
        
        if(file == null){ return ;}
        
        Map params = new HashMap();
        params.put(ReceiptReport.PARAM_KEY_PLATE_NO, plateNo);
        params.put(ReceiptReport.PARAM_KEY_RECEIVER_NAME, receiverName);
        params.put(ReceiptReport.PARAM_KEY_VEC_MAN_NAME, vecManName);
        params.put(ReceiptReport.PARAM_KEY_VEC_COLOR, vecColor);
        params.put(ReceiptReport.PARAM_KEY_PLATE_TYPE_NAME, plateType);
        params.put(ReceiptReport.PARAM_KEY_VEC_CHASSIS_NO, vecChassisNum);
        params.put(ReceiptReport.PARAM_KEY_CARD_END_DATE, cardEndDate);
        params.put(ReceiptReport.PARAM_KEY_VEC_MODEL, vecModel);
        params.put(ReceiptReport.PARAM_KEY_DEILEVERD_DATE, actDeliverdDate);
        params.put(ReceiptReport.PARAM_KEY_RECEIPT_NUMBER, receiptNo);
        params.put(ReceiptReport.PARAM_KEY_CARD_DESCRIBED , cardDescribed);
        
       
        
       ReceiptReport report = new ReceiptReport(params);
       report.print(file);
       try
       {
           ReportFrame.browse(file, false);
       }
       catch(IOException | InterruptedException ex)
       {
           logWriter.write(ex);
       }
    }
    
    public static void print(Plate plate)
    {

        String receiverName    = "";
        String actDeliverdDate = "";
        String deliverName     = "";//TODO
        String vecManName      = "";
        String vecKM           = "";//TODO
        String vecColor        = "";
        String vecModel        = "";//TODO
        String plateType       = "";
        String receiptNo       = "";//todo
        String vecChassisNum   = "";
        String cardEndDate     = "";
        String cardDescribed   = "";

        
        String SQL  = " SELECT   DELIVERED_INFO.RECEIVER_NAME                , DELIVERED_INFO.ACTUAL_DELIVERED_DATE AS ACT_DATE";
               SQL += "        , VEHICLE_MANUFACTURER.T_NAME AS VEC_MAN_NAME , VEHICLE_COLOR.T_NAME AS VEC_COLOR  ";
               SQL += "        , VEHICLE_CARD.END_DATE                       , PLATE_TYPE.T_NAME PLATE_TYPE_NAME ";
               SQL += "        , VEHICLE.CHASSIS_NO AS VEC_CHASSIS_NO        , VEHICLE.PRODUCTION_YEAR AS PROD_YEAR  ";
               SQL += "        , PLATE.PNR AS PNR_PLATE                      , DELIVERED_COUNTER   ";
               SQL += "        , VEHICLE_CARD.CARD_DESCRIBED";
               SQL += " FROM CURRENT_MOVEMENT " ;
               SQL += "               LEFT OUTER JOIN DELIVERED_INFO ON (CURRENT_MOVEMENT.PNR_DELIVERED_INFO = DELIVERED_INFO.PNR ) " ;// لاحضار اسم المستلم
               SQL += "               LEFT OUTER JOIN VEHICLE              ON (CURRENT_MOVEMENT.PNR_VEHICLE       = VEHICLE.PNR) ";
               SQL += "               LEFT OUTER JOIN VEHICLE_MANUFACTURER ON ( VEHICLE.PNR_VEHICLE_MANUFACTURER  = VEHICLE_MANUFACTURER.PNR ) ";//لاحضار نوع السيارة 
               SQL += "               LEFT OUTER JOIN VEHICLE_COLOR        ON ( VEHICLE.PNR_VEHICLE_COLOR         = VEHICLE_COLOR.PNR )";//لاحضار لون السيارة
               SQL += "               LEFT OUTER JOIN PLATE                ON ( CURRENT_MOVEMENT.PNR_PLATE        = PLATE.PNR ) ";//لاحضار رقم اللوحة
               SQL += "               LEFT OUTER JOIN PLATE_TYPE           ON ( PLATE.PNR_PLATE_TYPE              = PLATE_TYPE.PNR ) ";//لاحصار نوع اللوحة
               SQL += "               LEFT OUTER JOIN VEHICLE_CARD         ON ( CURRENT_MOVEMENT.PNR_VEHICLE_CARD = VEHICLE_CARD.PNR) ";// لاحصار تاربخ نهاية البطاقة
               SQL += " WHERE CURRENT_MOVEMENT.PNR_PLATE = " + plate.getPnr();
        Statement st  = null ;
        ResultSet rs  = null ;
        try
        {
            st = plate.m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if(rs.next())
            {
                receiverName    = rs.getString("RECEIVER_NAME");
                vecManName      = rs.getString("VEC_MAN_NAME");
                vecColor        = rs.getString("VEC_COLOR");
                plateType       = rs.getString("PLATE_TYPE_NAME");
                vecChassisNum   = rs.getString("VEC_CHASSIS_NO");
                vecModel        = rs.getString("PROD_YEAR");
                cardEndDate     = rs.getString("END_DATE");
                actDeliverdDate = rs.getString("ACT_DATE");
                receiptNo       = rs.getString("PNR_PLATE") + "م/" + rs.getString("DELIVERED_COUNTER");
                cardDescribed   = rs.getBoolean("CARD_DESCRIBED") ? "true" : "false";
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
        FileChooser      fileChooser = new FileChooser();
        FileChooser.ExtensionFilter  extFilter   = new FileChooser.ExtensionFilter( " (*.pdf)", "*." + "pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setSelectedExtensionFilter(extFilter);
        File file = fileChooser.showSaveDialog(Main.PRIMARY_STAGE);
        
        if(file == null){ return; }
        
        Map params = new HashMap();
        params.put(ReceiptReport.PARAM_KEY_PLATE_NO, plate.getPlateNo());
        params.put(ReceiptReport.PARAM_KEY_RECEIVER_NAME, receiverName);
        params.put(ReceiptReport.PARAM_KEY_VEC_MAN_NAME, vecManName);
        params.put(ReceiptReport.PARAM_KEY_VEC_COLOR, vecColor);
        params.put(ReceiptReport.PARAM_KEY_PLATE_TYPE_NAME, plateType);
        params.put(ReceiptReport.PARAM_KEY_VEC_CHASSIS_NO, vecChassisNum);
        params.put(ReceiptReport.PARAM_KEY_CARD_END_DATE, cardEndDate);
        params.put(ReceiptReport.PARAM_KEY_VEC_MODEL, vecModel);
        params.put(ReceiptReport.PARAM_KEY_DEILEVERD_DATE, actDeliverdDate);
        params.put(ReceiptReport.PARAM_KEY_RECEIPT_NUMBER, receiptNo);
        params.put(ReceiptReport.PARAM_KEY_CARD_DESCRIBED , cardDescribed);

        
       ReceiptReport report = new ReceiptReport(params);
       report.print(file);
       try
       {
           ReportFrame.browse(file, false);
       }
       catch(IOException | InterruptedException ex)
       {
           logWriter.write(ex);
       }
    }
    
    
    @Override
    public void initCenter()
    {
        getCenterPane().setHgap(10);
        getCenterPane().setVgap(10);
        
        int column = 3 ;
        int row    = 3 ;
        // رقم اللوحة والمحافظة 
        getCenterPane().add(m_plateLabel      , column++ , row );
        getCenterPane().add(m_plateNo         , column++ , row );
        getCenterPane().add(m_plateCityLabel  , column++ , row );
        getCenterPane().add(m_plateCity       , column++ , row );
        
        row++ ;
        column = 4 ;
        
        getCenterPane().add( m_printButton  , column++ , row );
    }
    
    @Override
    public void initLeftSidebar()
    {
        //Do nothing
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
