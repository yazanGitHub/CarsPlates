/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.reports;
import com.carsplates.smart.records.ArchivedMovement;
import com.carsplates.smart.records.Plate;
import com.carsplates.smart.records.VehicleCard;
import com.myz.component.myMultiSelComboBox;
import com.myz.component.myzComboBox;
import com.myz.component.myzComboBoxItem;
import com.myz.component.myzComponent;
import com.myz.component.myzDateField;
import com.myz.component.myzTextField;
import com.myz.connection.ConnectionContext;
import com.myz.reportComponent.ReportCol;
import com.myz.reportComponent.ReportColCombo;
import com.myz.reportComponent.ReportColDate;
import com.myz.reportComponent.ReportColString;
import java.util.Vector;
/**
 * @author yazan
 */
public abstract  class EmbassyReport 
{
    public final int COMBO_PLATE_CITY           = 1;
    public final int COMBO_PLATE_RETURN         = 2;
    public final int COMBO_PLATE_TYPE           = 3;
    public final int COMBO_UNIT                 = 4;
    public final int COMBO_VEHICLE_COLOR        = 5;
    public final int COMBO_VEHICLE_MANUFACTURER = 6;
    public final int COMBO_PLATE_STATUS         = 7;
    public final int COMBO_CARD_STATUS          = 8;
    public final int COMBO_ARCHIVED_STATUS      = 9;

    
    public static String BASIC_CURRENT_QUERY = "SELECT    PLATE_NO  , PNR_PLATE_CITY , PNR_PLATE_TYPE , PNR_PLATE_RETURN ,  \"\" as EMPTY \n" +
                                               "        , NOTE , PLATE_STATUS\n" +
                                               "        , CHASSIS_NO , ENGINE_NO , PRODUCTION_YEAR , TYPE , PNR_VEHICLE_MANUFACTURER , PNR_VEHICLE_COLOR\n" +
                                               "        , CARD_NO , CARD_CODE , START_DATE , END_DATE , CARD_STATUS \n" +
                                               "        , RECEIVER_NAME , RECEIVER_CELLULAR , PNR_DELIVERED_UNIT , DELIVERED_DATE , DELIVERED_END_DATE, DELIVERED_TERM , ORDER_TASK , ORDER_TASK_DATE , FEMME_TASK , FEMME_TASK_DATE , FEMME_DESCRIPTION\n" +
                                               "<report_basic_cols>"+
                                               " FROM  plate      INNER JOIN current_movement ON (plate.PNR                           = current_movement.PNR_PLATE)\n" +
                                               "             LEFT OUTER JOIN vehicle          ON (current_movement.PNR_VEHICLE        = vehicle.PNR) \n" +
                                               "             LEFT OUTER JOIN vehicle_card     ON (current_movement.PNR_VEHICLE_CARD   = vehicle_card.PNR)\n" +
                                               "             LEFT OUTER JOIN delivered_info   ON (current_movement.PNR_DELIVERED_INFO = delivered_info.PNR)" +
                                               " <report_basic_join> ";
    
    
    public static String BASIC_ARCHIVED_QUERY = "SELECT    VEHICLE.CHASSIS_NO , VEHICLE.ENGINE_NO , VEHICLE.PRODUCTION_YEAR , VEHICLE.TYPE VEC_TYPE ,  \"\" as EMPTY \n" +
                                                "        , VEHICLE.PNR_VEHICLE_MANUFACTURER , VEHICLE.PNR_VEHICLE_COLOR \n" +
                                                "        , VEHICLE_CARD.CARD_NO VEC_CARD_NO, VEHICLE_CARD.CARD_CODE VEC_CARD_CODE, VEHICLE_CARD.START_DATE VEC_CARD_START_DATE, VEHICLE_CARD.END_DATE VEC_CARD_END_DATE, VEHICLE_CARD.CARD_STATUS VEC_CARD_STATUS\n" +
                                                "        , DELIVERED_INFO.RECEIVER_NAME DELIVER_RECEIVER_NAME , DELIVERED_INFO.DELIVERED_DATE , DELIVERED_INFO.DELIVERED_END_DATE\n" +
                                                "        , PLATE.PNR_PLATE_CITY , PLATE.PNR_PLATE_TYPE , PLATE.PNR_PLATE_RETURN \n "+
                                                "<report_basic_cols>"+
                                                " FROM ARCHIVED_MOVEMENT      INNER JOIN PLATE          ON ( ARCHIVED_MOVEMENT.PNR_PLATE          = PLATE.PNR)\n" +
                                                "                        LEFT OUTER JOIN PLATE_CITY     ON ( PLATE.PNR_PLATE_CITY                 = PLATE_CITY.PNR)\n   "+
                                                "                        LEFT OUTER JOIN VEHICLE        ON ( ARCHIVED_MOVEMENT.PNR_VEHICLE        = VEHICLE.PNR)\n" +
                                                "                        LEFT OUTER JOIN VEHICLE_CARD   ON ( ARCHIVED_MOVEMENT.PNR_VEHICLE_CARD   = VEHICLE_CARD.PNR)\n" +
                                                "                        LEFT OUTER JOIN DELIVERED_INFO ON ( ARCHIVED_MOVEMENT.PNR_DELIVERED_INFO = DELIVERED_INFO.PNR)"+
                                                " <report_basic_join> ";

        
    String              m_query;
    String              m_mainTitle;
    String              m_subTitle;
    ConnectionContext   m_connection;
    String              m_id;
    Vector              m_vCol;//Vector of report col
    Vector              m_vSearchComponent = null;
    
    public abstract String              getBasicQuery();
    public abstract Vector              getBasicCols();
    public abstract String              getBasicJoin();
    public abstract String              getOrderBy();
    public abstract Vector              getSearchComponent();
    public abstract boolean             isSupportDynamicColumn();
    public abstract myMultiSelComboBox  getDynamicColumnCombo();
    public abstract boolean             addTotalCountRow();
    public abstract boolean             addRowCountColumn();
    public abstract int                 getFooterMode();
    
    public String getBasicColsString()
    {
        Vector vCols   = getBasicCols();
        String strCols = "";
        for( int i = 0; i < vCols.size(); i++ )
        {
            ReportCol col = ( ReportCol )vCols.elementAt( i );
            if( i > 0 )
            {
                strCols += ", ";
            }
            strCols += col.m_dbName;
        }
        if( strCols.length() == 0 )
        {
            strCols = "1";
        }
        return strCols;
    }

    public Vector getM_vCol() 
    {
        return m_vCol;
    }

    public void setM_vCol(Vector m_vCol) 
    {
        this.m_vCol = m_vCol;
    }

    public EmbassyReport( String id, ConnectionContext connection )
    {
        m_connection    = connection;
        m_id            = id;
    }
       
    public  static Vector getBasicReport(ConnectionContext connection )
    {
        Vector reportsList = new Vector();
        EmbassyReport report;
        
        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        report = new EmbassyReport("embassy_general_report" ,connection )
        {
            @Override
            public String getOrderBy()
            {
               return " ORDER BY PLATE_NO "; 
            }
            @Override
            public int getFooterMode()
            {
                return ReportDocument.FOOTER_MODE_EMPTY;
            }
            
            @Override
            public boolean addTotalCountRow()
            {
                return true;
            }
            
            @Override
            public boolean addRowCountColumn()
            {
                return true;
            }
            
            @Override
            public String getBasicQuery()
            {
                return BASIC_CURRENT_QUERY;
            }
            
            @Override
            public String getBasicJoin()
            {
                return  " LEFT OUTER JOIN UNITS ON ( DELIVERED_INFO.PNR_DELIVERED_UNIT = UNITS.PNR ) " +
                        " WHERE 1 = 1 ";
            }
            
            @Override
            public Vector getBasicCols()
            {
                Vector vCols = new Vector();
                vCols.addElement( new ReportColString("PLATE_NO" , "رقم اللوحة") ); 
                return vCols;
            }
            
            @Override
            public boolean isSupportDynamicColumn()
            {
                return true;
            }
            
            @Override
            public myMultiSelComboBox getDynamicColumnCombo()
            {       
                myMultiSelComboBox columnCombo     = new myMultiSelComboBox();
                myzComboBoxItem item;
                
                item = new myzComboBoxItem("محافظة اللوحة", 1, getReportCombo("PNR_PLATE_CITY" , COMBO_PLATE_CITY ));
                columnCombo.addItems(item);
                 
                item = new myzComboBoxItem("نوع اللوحة", 2 , getReportCombo("PNR_PLATE_TYPE" , COMBO_PLATE_TYPE ));
                columnCombo.addItems(item);
                
                item = new myzComboBoxItem("عائدية اللوحة", 2 , getReportCombo("PNR_PLATE_RETURN" , COMBO_PLATE_RETURN ));
                columnCombo.addItems(item);
                
                item = new myzComboBoxItem("حالة اللوحة", 2 , getReportCombo("PLATE_STATUS" , COMBO_PLATE_STATUS ));
                columnCombo.addItems(item);
                             
                item = new myzComboBoxItem("رقم هيكل المركبة", 3, new ReportColString("CHASSIS_NO", "رقم هيكل المركبة"));
                columnCombo.addItems(item);
                
                item = new myzComboBoxItem("رقم محرك المركبة", 3, new ReportColString("ENGINE_NO", "رقم محرك المركبة"));
                columnCombo.addItems(item);

                item = new myzComboBoxItem("تاريخ صنع المركبة", 3, new ReportColDate("PRODUCTION_YEAR","تاريخ صنع المركبة"));
                columnCombo.addItems(item);
                 
                item = new myzComboBoxItem("نوع المركبة", 2 , getReportCombo("PNR_VEHICLE_MANUFACTURER" , COMBO_VEHICLE_MANUFACTURER ));
                columnCombo.addItems(item);
                                 
                item = new myzComboBoxItem("لون المركبة", 2 , getReportCombo("PNR_VEHICLE_COLOR" , COMBO_VEHICLE_COLOR ));
                columnCombo.addItems(item);
                
                item = new myzComboBoxItem("رقم البطاقة", 3, new ReportColString("CARD_NO", "رقم البطاقة"));
                columnCombo.addItems(item);
                
                item = new myzComboBoxItem("تاريخ بداية البطاقة", 3, new ReportColDate("START_DATE","تاريخ بداية البطاقة"));
                columnCombo.addItems(item);
                
                item = new myzComboBoxItem("تاريخ نهاية البطاقة", 3, new ReportColDate("END_DATE","تاريخ نهاية البطاقة"));
                columnCombo.addItems(item);

                item = new myzComboBoxItem("حالة البطاقة", 2 , getReportCombo("CARD_STATUS" , COMBO_CARD_STATUS ));
                columnCombo.addItems(item);

                item = new myzComboBoxItem("اسم المستلم", 3, new ReportColString("RECEIVER_NAME", "اسم المستلم"));
                columnCombo.addItems(item);
                
                item = new myzComboBoxItem("تاريخ التسليم", 3, new ReportColDate("DELIVERED_DATE","تاريخ التسليم"));
                columnCombo.addItems(item);
                
                item = new myzComboBoxItem("تاريخ انتهاء التسليم", 3, new ReportColDate("DELIVERED_DATE","تاريخ انتهاء التسليم"));
                columnCombo.addItems(item);
                
                item = new myzComboBoxItem("الوحدة المستلمة", 2 , getReportCombo("PNR_DELIVERED_UNIT" , COMBO_UNIT ));
                columnCombo.addItems(item);
                
                return columnCombo;
            }
            
            @Override
            public Vector getSearchComponent() 
            {
                if ( m_vSearchComponent != null)
                    return m_vSearchComponent;
                
                m_vSearchComponent = new Vector();

                myMultiSelComboBox plateCity = new myMultiSelComboBox("SELECT PNR , T_NAME  FROM PLATE_CITY", "T_NAME");
                plateCity.setFieldName("PNR_PLATE_CITY");
                plateCity.setCaption("محافظة اللوحة");
                m_vSearchComponent.add(plateCity);
                
                myMultiSelComboBox plateType = new myMultiSelComboBox("SELECT PNR , T_NAME  FROM PLATE_TYPE", "T_NAME");
                plateType.setFieldName("PNR_PLATE_TYPE");
                plateType.setCaption("نوع اللوحة");
                m_vSearchComponent.add(plateType);
                 
                myMultiSelComboBox plateReturn = new myMultiSelComboBox("SELECT PNR , T_NAME  FROM PLATE_RETURN", "T_NAME");
                plateReturn.setFieldName("PNR_PLATE_RETURN");
                plateReturn.setCaption("عائدية اللوحة");
                m_vSearchComponent.add(plateReturn);
                
                
                
                myMultiSelComboBox plateStatus = new myMultiSelComboBox();
                plateStatus.addItems(Plate.getPlateStatusVec());
                plateStatus.setFieldName("PLATE_STATUS");
                plateStatus.setCaption("حالة اللوحة");
                m_vSearchComponent.add(plateStatus);
                 
                myzTextField chassisNo = new myzTextField();
                chassisNo.setFieldName("CHASSIS_NO");
                chassisNo.setCaption("رقم هيكل المركبة");
                m_vSearchComponent.add(chassisNo);
                
                myzTextField engineNo = new myzTextField();
                engineNo.setFieldName("ENGINE_NO");
                engineNo.setCaption("رقم محرك المركبة");
                m_vSearchComponent.add(engineNo);
                                              
                myMultiSelComboBox vehicleMnaufacturer = new myMultiSelComboBox("SELECT PNR , T_NAME FROM VEHICLE_MANUFACTURER" , "T_NAME");
                vehicleMnaufacturer.setFieldName("PNR_VEHICLE_MANUFACTURER");
                vehicleMnaufacturer.setCaption("نوع المركبة");
                m_vSearchComponent.add(vehicleMnaufacturer);
                
                myzDateField productionDate = new myzDateField();
                productionDate.setFieldName("PRODUCTION_YEAR");
                productionDate.setCaption("تاريخ صنع المركبة من");
                m_vSearchComponent.add(productionDate);
                
                myzDateField productionDateTo = new myzDateField();
                productionDateTo.setFieldName("PRODUCTION_YEAR");
                productionDateTo.setCaption("تاريخ صنع المركبة إلى");
                m_vSearchComponent.add(productionDateTo);
                
                myMultiSelComboBox vehicleColor = new myMultiSelComboBox("SELECT PNR , T_NAME FROM VEHICLE_COLOR", "T_NAME");
                vehicleColor.setFieldName("PNR_VEHICLE_COLOR");
                vehicleColor.setCaption("لون المركبة");
                m_vSearchComponent.add(vehicleColor);
                
                myzTextField cardNo = new myzTextField();
                cardNo.setFieldName("CARD_NO");
                cardNo.setCaption("رقم البطاقة");
                m_vSearchComponent.add(cardNo);
                
                myzDateField cardStartDateFrom = new myzDateField();
                cardStartDateFrom.setFieldName("START_DATE");
                cardStartDateFrom.setCaption("تاريخ بداية البطاقة من");
                m_vSearchComponent.add(cardStartDateFrom);
                
                myzDateField cardStartDateTo = new myzDateField();
                cardStartDateTo.setFieldName("START_DATE");
                cardStartDateTo.setCaption("تاريخ بداية البطاقة إلى");
                m_vSearchComponent.add(cardStartDateTo);
                
                
                myzDateField cardEndDateFrom = new myzDateField();
                cardEndDateFrom.setFieldName("END_DATE");
                cardEndDateFrom.setCaption("تاريخ نهاية البطاقة من");
                m_vSearchComponent.add(cardEndDateFrom);
                
                myzDateField cardEndDateTo = new myzDateField();
                cardEndDateTo.setFieldName("END_DATE");
                cardEndDateTo.setCaption("تاريخ نهاية البطاقة إلى");
                m_vSearchComponent.add(cardEndDateTo);
                
                myMultiSelComboBox cardStatus = new myMultiSelComboBox();
                cardStatus.addItems(VehicleCard.getCardStatusVec());
                cardStatus.setFieldName("CARD_STATUS");
                cardStatus.setCaption("حالة البطاقة");
                m_vSearchComponent.add(cardStatus);   
                
                myzTextField receiverName = new myzTextField();
                receiverName.setFieldName("RECEIVER_NAME");
                receiverName.setCaption("اسم المستلم");
                m_vSearchComponent.add(receiverName);
                
                myzDateField deliverDateFrom = new myzDateField();
                deliverDateFrom.setFieldName("DELIVERED_DATE");
                deliverDateFrom.setCaption("تاريخ التسليم من");
                m_vSearchComponent.add(deliverDateFrom);
                
                myzDateField deliverDateTo = new myzDateField();
                deliverDateTo.setFieldName("DELIVERED_DATE");
                deliverDateTo.setCaption("تاريخ التسليم إلى");
                m_vSearchComponent.add(deliverDateTo);
                
                myzDateField deliverEndDateFrom = new myzDateField();
                deliverEndDateFrom.setFieldName("DELIVERED_DATE");
                deliverEndDateFrom.setCaption("تاريخ انتهاء التسليم من");
                m_vSearchComponent.add(deliverEndDateFrom);
                
                myzDateField deliverEndDateTo = new myzDateField();
                deliverEndDateTo.setFieldName("DELIVERED_DATE");
                deliverEndDateTo.setCaption("تاريخ انتهاء التسليم إلى");
                m_vSearchComponent.add(deliverEndDateTo);
                
                myzComboBox cardDescribed = new myzComboBox();
                cardDescribed.setFieldName("CARD_DESCRIBED");
                cardDescribed.setCaption("حالة تشخيص اللوحة");
                myzComboBoxItem described = new myzComboBoxItem("مشخصة", 1);
                myzComboBoxItem notDescribed = new myzComboBoxItem("غير مشخصة", 0);
                cardDescribed.addItems(described , notDescribed);
                m_vSearchComponent.add(cardDescribed);
                
                myMultiSelComboBox deliveredInfoUnit = new myMultiSelComboBox("SELECT * FROM UNITS" , "T_NAME");
                deliveredInfoUnit.setFieldName("UNITS.PNR");
                deliveredInfoUnit.setCaption("الوحدة المستلمة");
                m_vSearchComponent.add(deliveredInfoUnit);

                return m_vSearchComponent;
            }
        };
        report.m_query     = report.getBasicQuery();
        report.m_query     = report.m_query.replaceAll( "<report_basic_cols>", " , " + report.getBasicColsString() );
        report.m_query     = report.m_query.replaceAll( "<report_basic_join>", report.getBasicJoin() );
        report.m_vCol      = report.getBasicCols();
        report.m_mainTitle = "التقرير العام";
        report.m_subTitle  = "";
        reportsList.add(report);
        
        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        
        report = new EmbassyReport("embassy_archive_movement" ,connection )
        {
            @Override
            public String getOrderBy()
            {
               return " ORDER BY ARCHIVED_MOVEMENT.DATE DESC , ARCHIVED_MOVEMENT.PNR DESC  "; 
            }
            
            @Override
            public int getFooterMode()
            {
                return ReportDocument.FOOTER_MODE_EMPTY;
            }
            @Override
            public boolean addTotalCountRow()
            {
                return true;
            }
            
            @Override
            public boolean addRowCountColumn()
            {
                return false;
            }
            
            @Override
            public String getBasicQuery()
            {
                return BASIC_ARCHIVED_QUERY;
            }
            
            @Override
            public String getBasicJoin()
            {
                return " WHERE 1 = 1 ";
            }
            @Override
            public Vector getBasicCols()
            {
                Vector vCols = new Vector();
                vCols.addElement( new ReportColDate("ARCHIVED_MOVEMENT.DATE" , "تاريخ التبدل") ); 
                vCols.addElement( getReportCombo("ARCHIVED_MOVEMENT.PLATE_STATUS", COMBO_PLATE_STATUS) );
                vCols.addElement( getReportCombo("ARCHIVED_MOVEMENT.STATUS"      , COMBO_ARCHIVED_STATUS) );
                return vCols;
            }
            
            @Override
            public boolean isSupportDynamicColumn()
            {
                return true;
            }
            
            @Override
            public myMultiSelComboBox getDynamicColumnCombo()
            {
                myMultiSelComboBox columnCombo     = new myMultiSelComboBox();
                myzComboBoxItem item;
                
                item = new myzComboBoxItem("نوع السيارة", 1, getReportCombo("PNR_VEHICLE_MANUFACTURER" , COMBO_VEHICLE_MANUFACTURER ));
                columnCombo.addItems(item);
                 
                item = new myzComboBoxItem("لون السيارة", 2, getReportCombo("PNR_VEHICLE_COLOR" , COMBO_VEHICLE_COLOR ));
                columnCombo.addItems(item);

                item = new myzComboBoxItem("رقم البطاقة", 3, new ReportColString("VEC_CARD_NO", "رقم البطاقة"));
                columnCombo.addItems(item);
                   
                item = new myzComboBoxItem("اسم المستلم", 4, new ReportColString( "DELIVER_RECEIVER_NAME" , "اسم المستلم"));
                columnCombo.addItems(item);
                   
                return columnCombo;
            }
            
            @Override
            public Vector getSearchComponent() 
            {
                if ( m_vSearchComponent != null)
                    return m_vSearchComponent;
                
                m_vSearchComponent = new Vector();
                 
                myMultiSelComboBox archiveStatus = new myMultiSelComboBox();
                archiveStatus.addItems(ArchivedMovement.getArchivedStatusVec());
                archiveStatus.setFieldName("PLATE_STATUS");
                archiveStatus.setCaption("التبدل المطلوب");
                m_vSearchComponent.add(archiveStatus);
                
                myzTextField plateNo = new myzTextField();
                plateNo.setFieldName("PLATE_NO");
                plateNo.setCaption("رقم اللوحة");
                m_vSearchComponent.add(plateNo);
                
                myzComboBox plateCity  = new myzComboBox("SELECT * FROM PLATE_CITY" , "T_NAME" , false);
                plateCity.setFieldName("PLATE_CITY.PNR");
                plateCity.setCaption("المحافظة ");
                m_vSearchComponent.add(plateCity);
                                                
                myzDateField statusDateFrom = new myzDateField();
                statusDateFrom.setFieldName("DATE");
                statusDateFrom.setCaption("تاريخ التبدل من");
                m_vSearchComponent.add(statusDateFrom);
                
                myzDateField statusDateTo = new myzDateField();
                statusDateTo.setFieldName("DATE");
                statusDateTo.setCaption("تاريخ التبدل إلى");
                m_vSearchComponent.add(statusDateTo);

                return m_vSearchComponent;
            }
        };
        report.m_query     = report.getBasicQuery();
        report.m_query     = report.m_query.replaceAll( "<report_basic_cols>", " , " + report.getBasicColsString() );
        report.m_query     = report.m_query.replaceAll( "<report_basic_join>", report.getBasicJoin() );
        report.m_vCol      = report.getBasicCols();
        report.m_mainTitle = "تقرير تبدلات اللوحات";
        report.m_subTitle  = "";
        reportsList.add(report);
        
        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        
        report = new EmbassyReport("embassy_card" ,connection )
        {
            @Override
            public String getOrderBy()
            {
                return " ORDER BY PLATE_NO ";
            }
            
            @Override
            public int getFooterMode()
            {
                return ReportDocument.FOOTER_MODE_RECEIVER_DELIVER_NAME;
            }
            
            @Override
            public boolean addTotalCountRow()
            {
                return false;
            }
            
            @Override
            public boolean addRowCountColumn()
            {
                return true;
            }
            
            @Override
            public String getBasicQuery()
            {
                return  " SELECT DISTINCT( VEHICLE_CARD.PNR ) , CARD_CODE , START_DATE , END_DATE ,CARD_STATUS  ,  \"\" as NOTE ,   \"\" as EMPTY"
                        +"<report_basic_cols>"
                        +" FROM VEHICLE_CARD LEFT OUTER JOIN ARCHIVED_MOVEMENT ON ( VEHICLE_CARD.PNR = ARCHIVED_MOVEMENT.PNR_VEHICLE_CARD )"
                        +"                   LEFT OUTER JOIN PLATE             ON ( PLATE.PNR        = ARCHIVED_MOVEMENT.PNR_PLATE )"
                        +"<report_basic_join>";    
            }
            
            @Override
            public String getBasicJoin()
            {
                return  " where 1 = 1 ";
            }
            
            @Override
            public Vector getBasicCols()
            {
                Vector vCols = new Vector();
                vCols.addElement(new ReportColString("PLATE_NO","رقم اللوحة"));
                vCols.addElement(getReportCombo("PNR_PLATE_CITY","المحافظة" , COMBO_PLATE_CITY));
                vCols.addElement(new ReportColString("CARD_NO","رقم البطاقة"));
                vCols.addElement(new ReportColString("NOTE","ملاحظات"));
                return vCols;
            }
            
            @Override
            public boolean isSupportDynamicColumn()
            {
                return true;
            }
            
            @Override
            public myMultiSelComboBox getDynamicColumnCombo()
            {
                myMultiSelComboBox columnCombo     = new myMultiSelComboBox();
  
                    myzComboBoxItem item;

                    item = new myzComboBoxItem("باركود البطاقة", 1, new ReportColString("CARD_CODE", "باركود البطاقة"));
                    columnCombo.addItems(item);
                    
                    item = new myzComboBoxItem("تاريخ بداية البطاقة", 2, new ReportColDate( "START_DATE" , "تاريخ بداية البطاقة"));
                    columnCombo.addItems(item);
                   
                    item = new myzComboBoxItem("تاريخ نهاية البطاقة", 3, new ReportColDate( "END_DATE" , "تاريخ نهاية البطاقة"));
                    columnCombo.addItems(item);
                   
                return columnCombo;
            }
            
            @Override
            public Vector getSearchComponent() 
            {
                if ( m_vSearchComponent != null)
                    return m_vSearchComponent;
                
                m_vSearchComponent = new Vector();
                 
                myMultiSelComboBox cardStatus = new myMultiSelComboBox();// TODO GET Item
                cardStatus.addItems(VehicleCard.getCardStatusVec());
                cardStatus.setFieldName("CARD_STATUS");
                cardStatus.setCaption("حالة البطاقة");
                m_vSearchComponent.add(cardStatus); 
                
                myzDateField cardStartDateFrom = new myzDateField();
                cardStartDateFrom.setFieldName("START_DATE");
                cardStartDateFrom.setCaption("تاريخ بداية البطاقةمن");
                m_vSearchComponent.add(cardStartDateFrom);
                
                myzDateField cardStartDateTo = new myzDateField();
                cardStartDateTo.setFieldName("START_DATE");
                cardStartDateTo.setCaption("تاريخ بداية البطاقةالى");
                m_vSearchComponent.add(cardStartDateTo);
             
                myzDateField cardEndDateFrom = new myzDateField();
                cardEndDateFrom.setFieldName("END_DATE");
                cardEndDateFrom.setCaption("تاريخ نهاية البطاقة من");
                m_vSearchComponent.add(cardEndDateFrom);
                
                myzDateField cardEndDateTo = new myzDateField();
                cardEndDateTo.setFieldName("END_DATE");
                cardEndDateTo.setCaption("تاريخ نهاية البطاقة الى");
                m_vSearchComponent.add(cardEndDateTo);
               
                return m_vSearchComponent;
            }
        };
        report.m_query     = report.getBasicQuery();
        report.m_query     = report.m_query.replaceAll( "<report_basic_cols>", " , " + report.getBasicColsString() );
        report.m_query     = report.m_query.replaceAll( "<report_basic_join>", report.getBasicJoin() );
        report.m_vCol      = report.getBasicCols();
        report.m_mainTitle = "تقرير بالبطاقات المنتهية الصلاحية";
        report.m_subTitle  = "";
        reportsList.add(report);
        
        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        
        report = new EmbassyReport("embassy_archive_movement_drop_movement" ,connection )
        {
            @Override
            public String getOrderBy()
            {
               return " ORDER BY ARCHIVED_MOVEMENT.DATE "; 
            }
            
            @Override
            public int getFooterMode()
            {
                return ReportDocument.FOOTER_MODE_EMPTY;
            }
            @Override
            public boolean addTotalCountRow()
            {
                return true;
            }
            
            @Override
            public boolean addRowCountColumn()
            {
                return false;
            }
            
            @Override
            public String getBasicQuery()
            {
                return BASIC_ARCHIVED_QUERY;
            }
            
            @Override
            public String getBasicJoin()
            {
                return " WHERE ARCHIVED_MOVEMENT.STATUS = " + ArchivedMovement.STATUS_DROPPED_PLATE;
            }
            @Override
            public Vector getBasicCols()
            {
                Vector vCols = new Vector();
                vCols.addElement( new ReportColString("PLATE_NO" , "رقم اللوحة") ); 
                vCols.addElement( new ReportColDate("ARCHIVED_MOVEMENT.DATE" , "تاريخ الإسقاط") ); 
               
                return vCols;
            }
            
            @Override
            public boolean isSupportDynamicColumn()
            {
                return true;
            }
            
            @Override
            public myMultiSelComboBox getDynamicColumnCombo()
            {
                myMultiSelComboBox columnCombo     = new myMultiSelComboBox();
                myzComboBoxItem item;
                
                item = new myzComboBoxItem("محافظة اللوحة", 1, getReportCombo("PNR_PLATE_CITY" , COMBO_PLATE_CITY ));
                columnCombo.addItems(item);
                 
                item = new myzComboBoxItem("نوع اللوحة", 2 , getReportCombo("PNR_PLATE_TYPE" , COMBO_PLATE_TYPE ));
                columnCombo.addItems(item);
                
                item = new myzComboBoxItem("عائدية اللوحة", 2 , getReportCombo("PNR_PLATE_RETURN" , COMBO_PLATE_RETURN ));
                columnCombo.addItems(item);
                   
                return columnCombo;
            }
            
            @Override
            public Vector getSearchComponent() 
            {
                if ( m_vSearchComponent != null)
                    return m_vSearchComponent;
                
                m_vSearchComponent = new Vector();
                
                
                myzComboBox plateCity  = new myzComboBox("SELECT * FROM PLATE_CITY" , "T_NAME" , false);
                plateCity.setFieldName("PLATE_CITY.PNR");
                plateCity.setCaption("المحافظة ");
                m_vSearchComponent.add(plateCity);
                
                myzDateField statusDateFrom = new myzDateField();
                statusDateFrom.setFieldName("DATE");
                statusDateFrom.setCaption("تاريخ الإسقاط من");
                m_vSearchComponent.add(statusDateFrom);
                
                myzDateField statusDateTo = new myzDateField();
                statusDateTo.setFieldName("DATE");
                statusDateTo.setCaption("تاريخ الإسقاط إلى");
                m_vSearchComponent.add(statusDateTo);
                


                return m_vSearchComponent;
            }
        };
        report.m_query     = report.getBasicQuery();
        report.m_query     = report.m_query.replaceAll( "<report_basic_cols>", " , " + report.getBasicColsString() );
        report.m_query     = report.m_query.replaceAll( "<report_basic_join>", report.getBasicJoin() );
        report.m_vCol      = report.getBasicCols();
        report.m_mainTitle = "تقرير باللوحات المسقطة";
        report.m_subTitle  = "";
        reportsList.add(report);
        
        
        return reportsList;
    }
    
    
    public String getM_mainTitle()
    {
        return m_mainTitle;
    }
                
    public String getSearchTitle()
    {
        String str             = "";
        Vector searchComponent = getSearchComponent();
        for ( int i = 0 ; i < searchComponent.size() ; i++ )
        {
            myzComponent comp = (myzComponent) searchComponent.get(i);
            Object value = null;
            if ( comp instanceof myzComboBox )
            {
                myzComboBoxItem item = ((myzComboBox) comp).getItemValue();
                if ( item != null )
                {
                    value = item.getValue();
                }
            }
            else
                value = comp.getValue();
            if (value != null)
            {
                if (str.length() > 0)
                    str += " , ";
                str += comp.getCaption() + " : " + value;
            }
        }
        return str;
    }
      
    public String getReportWhere()
    {
        String where           = "";
        Vector searchComponent = new Vector (getSearchComponent());
        boolean addWhere  ; 
        for ( int i = 0 ; i < searchComponent.size() ; i++ )
        {
            addWhere   = false;// this used for the current component to not added again

            myzComponent currentCom = (myzComponent) searchComponent.get(i);
            if ( currentCom.getFieldName() == null )
            {
                continue;
            }
            for( int j = (i+1) ; j < searchComponent.size() ; j++)
            {
                myzComponent nextCom = (myzComponent) searchComponent.get(j);
                
                if ( nextCom.getFieldName() != null && nextCom.getFieldName().equalsIgnoreCase(currentCom.getFieldName()))
                {
                    if ( currentCom.getValue() != null)
                    {
                         if (where.length() > 0)
                            where += " AND ";
                        where += currentCom.getSQLWhereAfter();
                        addWhere = true;
                    }
                    if ( nextCom.getValue() != null)
                    {
                         if (where.length() > 0)
                            where += " AND ";
                        where += nextCom.getSQLWhereBefor();
                        addWhere = true;
                    }
                }
                if ( addWhere )
                {
                    searchComponent.remove(j);
                    break;
                }
            }
            if (!addWhere &&currentCom.getValue() != null)
            {
                if (where.length() > 0)
                    where += " AND ";
                where += currentCom.getSQLWhere();
            }
        }
        return where;
    }
     
    public ReportColCombo getReportCombo(String columnName , int  tableCode )
    {
        return getReportCombo(columnName,null,tableCode);
    }

    public ReportColCombo getReportCombo(String columnName ,String caption , int  tableCode )
    {
        if ( tableCode == COMBO_PLATE_CITY)
        {
            if (caption == null )
                return new ReportColCombo( columnName , "المدينة", "SELECT PNR , T_NAME FROM PLATE_CITY" , m_connection);
            return new ReportColCombo( columnName , caption ,"SELECT PNR , T_NAME FROM PLATE_CITY" , m_connection );
        }
        else if ( tableCode == COMBO_PLATE_RETURN)
        {
            if (caption == null )
                return new ReportColCombo( columnName , "عائدية اللوحة", "SELECT PNR , T_NAME FROM PLATE_RETURN" , m_connection);
            return new ReportColCombo( columnName , caption ,"SELECT PNR , T_NAME FROM PLATE_RETURN" , m_connection );
        }
        else if ( tableCode == COMBO_PLATE_TYPE)
        {
            if (caption == null )
                return new ReportColCombo( columnName , "نوع اللوحة", "SELECT PNR , T_NAME FROM PLATE_TYPE" , m_connection);
            return new ReportColCombo( columnName , caption ,"SELECT PNR , T_NAME FROM PLATE_TYPE" , m_connection );
        }
        else if ( tableCode == COMBO_UNIT)
        {  
            if (caption == null )
                return new ReportColCombo( columnName , "الوحدة", "SELECT PNR , T_NAME FROM UNITS" , m_connection);
            return new ReportColCombo( columnName , caption ,"SELECT PNR , T_NAME FROM UNIT" , m_connection );
        }
        else if ( tableCode == COMBO_UNIT)
        {
            if (caption == null )
                return new ReportColCombo( columnName , "الوحدة", "SELECT PNR , T_NAME FROM UNIT" , m_connection);
            return new ReportColCombo( columnName , caption ,"SELECT PNR , T_NAME FROM UNIT" , m_connection );
        }
        else if ( tableCode == COMBO_VEHICLE_COLOR)
        {
            if (caption == null )
                return new ReportColCombo( columnName , "اللون", "SELECT PNR , T_NAME FROM VEHICLE_COLOR" , m_connection);
            return new ReportColCombo( columnName , caption ,"SELECT PNR , T_NAME FROM VEHICLE_COLOR" , m_connection );
        }
         else if ( tableCode == COMBO_VEHICLE_MANUFACTURER)
        {
            if (caption == null )
                return new ReportColCombo( columnName , "نوع السيارة", "SELECT PNR , T_NAME FROM VEHICLE_MANUFACTURER" , m_connection);
            return new ReportColCombo( columnName , caption ,"SELECT PNR , T_NAME FROM VEHICLE_MANUFACTURER" , m_connection );
        }
        else if ( tableCode == COMBO_PLATE_STATUS)
        {
            if (caption == null )
                return new ReportColCombo( columnName , "حالة اللوحة", Plate.getPlateStatusVec());
            return new ReportColCombo( columnName , caption ,  Plate.getPlateStatusVec());
        }
        else if ( tableCode == COMBO_CARD_STATUS)
        {
            if (caption == null )
                return new ReportColCombo( columnName , "حالة البطاقة", VehicleCard.getCardStatusVec());
            return new ReportColCombo( columnName , caption ,  VehicleCard.getCardStatusVec());
        }
        else if ( tableCode == COMBO_ARCHIVED_STATUS)
        {
            if (caption == null )
                return new ReportColCombo( columnName , "التبدل ", ArchivedMovement.getArchivedStatusVec());
            return new ReportColCombo( columnName , caption ,  ArchivedMovement.getArchivedStatusVec());
        }
        return null;
    }
}
