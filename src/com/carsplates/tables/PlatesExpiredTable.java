/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.tables;

import com.carsplates.frames.CarsPlatesFrame;
import com.carsplates.main.Main;
import com.carsplates.reports.ReportDocument;
import com.carsplates.smart.records.Plate;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.myz.component.myzDateField;
import com.myz.component.myzTableView;
import com.myz.connection.arConnectionInfo;
import com.myz.log.logWriter;
import com.myz.record.DbTools;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Vector;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import myzTools.Tools;

/**
 *
 * @author Montazar Hamoud
 */
public class PlatesExpiredTable  extends myzTableView
{
    //Constructor
    public PlatesExpiredTable()
    {
        m_plateNoCol          = new TableColumn("رقم اللوحة ");
        m_plateExpiredTimeCol = new TableColumn("تاريخ الانتهاء");
        m_pnrPlateCol         = new TableColumn("");
        
        m_plateNoCol.setCellValueFactory(new PropertyValueFactory("m_plateNo"));
        m_plateExpiredTimeCol.setCellValueFactory(new PropertyValueFactory("m_plateEndDate"));
        m_pnrPlateCol.setCellValueFactory(new PropertyValueFactory("m_pnrPlate"));
        getColumns().setAll(m_plateExpiredTimeCol , m_plateNoCol);
        setPrefWidth(250);
        setPrefHeight(300);
        
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        PlatesExpired platesExpired = new PlatesExpired();
        
        setTableData(platesExpired.getVPlatesExpiredInMonth() );
    }
    
    //Data member
    TableColumn m_plateNoCol   ;
    TableColumn m_pnrPlateCol ;
    TableColumn m_plateExpiredTimeCol ;
    
    @Override
    public void dbClickOnRow()
    {
        PlatesExpired platesExpired = (PlatesExpired) getSelectionModel().getSelectedItem();
        
        String plateNo      = platesExpired.m_plateNo ;
        int    pnrPlateCity = platesExpired.m_pnrPlateCity ;
        
        CarsPlatesFrame carsPlatesFrame = new CarsPlatesFrame( CarsPlatesFrame.MODE_SHOW ) ;
        carsPlatesFrame.load(plateNo, pnrPlateCity);
        Main.SCENE.setRoot(carsPlatesFrame);
        
    }
    public class PlatesExpired 
    {
        //Constructor
        public PlatesExpired(String platesNo , String platesEndDate , int pnrPlateCity)
        {
            setPlatesNo(platesNo);
            setPlatesEndDate(platesEndDate);
            setPnrPlateCity(pnrPlateCity);
        }
        public PlatesExpired()
        {
        
        }
        //Date member
        String m_plateNo ;
        String m_plateEndDate ;
        int    m_pnrPlateCity ;
    
        //Methods
        public  Vector<PlatesExpired> getVPlatesExpiredInMonth()
        {
            Vector <PlatesExpired> vPlates = new Vector();
            PlatesExpired  temp        = null ;
            LocalDate      localDate   = java.time.LocalDate.now();
            LocalDate      localDateP  = localDate.plusMonths(1);
            String         afterMonth  = localDateP.format(myzDateField.DATE_TIME_FORMATER_SAVE);
            String         SQL         = "";
        
            SQL  = " SELECT * FROM PLATE ";
            SQL += " LEFT OUTER JOIN CURRENT_MOVEMENT ON (PLATE.PNR = CURRENT_MOVEMENT.PNR_PLATE AND CURRENT_MOVEMENT.PLATE_STATUS = " + Plate.PLATE_STATUS_DELIVERED + " )";
            SQL += " LEFT OUTER JOIN DELIVERED_INFO ON (CURRENT_MOVEMENT.PNR_DELIVERED_INFO = DELIVERED_INFO.PNR " ;
            SQL += " AND  DELIVERED_INFO.DELIVERED_END_DATE  <=  " + DbTools.setDB(afterMonth) + "   ) ";
            SQL += " WHERE DELIVERED_TERM = " + CarsPlatesFrame.TEMP ;
        
            Statement st  = null ;
            ResultSet rs  = null ;
            try
            {
                st = arConnectionInfo.getConnectionContext().m_connection.createStatement();
                rs = st.executeQuery(SQL);
                while(rs.next())
                {
                    String platesNo      = rs.getString("PLATE_NO");
                    String platesEndDate = rs.getString("DELIVERED_END_DATE");
                    int    pnrPlateCity  = rs.getInt("PLATE.PNR_PLATE_CITY");
                    temp = new PlatesExpired(platesNo , platesEndDate , pnrPlateCity);
                    vPlates.addElement(temp);
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
            return vPlates ;
        }
    
        private void setPlatesNo(String platesNo)
        {
            m_plateNo = platesNo ;
        }
        private void setPlatesEndDate(String platesEndDate)
        {
            m_plateEndDate = platesEndDate ;
        }
        private void setPnrPlateCity(int pnrPlateCity)
        {
            m_pnrPlateCity = pnrPlateCity ;
        }
        public String getM_plateNo()
        {
            return m_plateNo;
        }
        public String getM_plateEndDate()
        {
            return m_plateEndDate ;
        }
        public int getM_pnrPlateCity()
        {
            return m_pnrPlateCity ;
        }
    }

    public void printTable(File file)
    {
        try
        {
            FileOutputStream stream = new FileOutputStream(file);
            // Add Data to pdf File
            Document pdfDocument = new Document( PageSize.A4,10f,10f,20f,10f );
            PdfWriter.getInstance(pdfDocument, stream);
            pdfDocument.open();
            pdfDocument.addCreationDate();
            
            PdfPCell cell = null;

            LocalDate      localDate   = java.time.LocalDate.now();
            LocalDate      localDateP  = localDate.plusMonths(1);
            String         afterMonth  = localDateP.format(myzDateField.DATE_TIME_FORMATER_SAVE);
            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setRunDirection(ReportDocument.getRunDirection());
            titleTable.setWidthPercentage(98);
            titleTable.addCell(ReportDocument.getPdfCell("اللوحات التي تنتهي صلاحيتها قبل " + Tools.getDate(afterMonth, false), ReportDocument.ROW_TYPE_SUB_HEADER));
            pdfDocument.add(titleTable);  
            
            
            
            PdfPTable dataTable = new PdfPTable(2);
            dataTable.setWidthPercentage(98);
            float [] width = new float[2];
            width[0] = 3f;
            width[1] = 3f;
            dataTable.setWidths(width);
            
            //Column Header
            dataTable.addCell(ReportDocument.getPdfCell("تاريخ الإنتهاء", ReportDocument.ROW_TYPE_TITLE));
            dataTable.addCell(ReportDocument.getPdfCell("رقم اللوحة", ReportDocument.ROW_TYPE_TITLE));
            
            for ( int i = 0 ; i < getItems().size() ; i++ )
            {
                PlatesExpired plateExpired = ( PlatesExpired ) getItems().get(i);
                dataTable.addCell(ReportDocument.getPdfCell(plateExpired.m_plateEndDate, ReportDocument.ROW_TYPE_NORMAL));
                dataTable.addCell(ReportDocument.getPdfCell(plateExpired.m_plateNo, ReportDocument.ROW_TYPE_NORMAL));
            }

            pdfDocument.add(dataTable);            
    
            pdfDocument.close();
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
            System.out.println("Error printTable  : PlatesExpiredTable.printTable(); ");
        }        
    }
}
