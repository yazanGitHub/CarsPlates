/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.tables;

import com.carsplates.reports.ReportDocument;
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
import static javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY;
import javafx.scene.control.cell.PropertyValueFactory;
import myzTools.Tools;

/**
 *
 * @author Montazar Hamoud
 */

public class CardsExpiredTable extends myzTableView
{
    //Constructor
    public CardsExpiredTable()
    {
        m_cardNoCol          = new TableColumn("رقم البطاقة ");
        m_cardExpiredTimeCol = new TableColumn("تاريخ الانتهاء");
        
        m_cardNoCol.setCellValueFactory(new PropertyValueFactory("m_cardNo"));
        m_cardExpiredTimeCol.setCellValueFactory(new PropertyValueFactory("m_cardEndDate"));
        
        getColumns().setAll(m_cardExpiredTimeCol , m_cardNoCol);
        setPrefWidth(250);
        setPrefHeight(300);
        
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        CardsExpired cardsExpired = new CardsExpired();
        
        setTableData(cardsExpired.getVCardsExpiredInMonth());
        

    }
    
    //Data member
    TableColumn m_cardNoCol   ;
    TableColumn m_cardExpiredTimeCol ;
    
    
public class CardsExpired 
{
    //Constructor
    public CardsExpired(String platesNo , String platesEndDate)
    {
        setCardsNo(platesNo);
        setCardsEndDate(platesEndDate);
        
    }
    public CardsExpired()
    {
        
    }
    //Date member
    String m_cardNo ;
    String m_cardEndDate ;
    
    //Methods
    public  Vector<CardsExpired> getVCardsExpiredInMonth()
    {
        Vector <CardsExpired> vPlates = new Vector();
        CardsExpired   temp        = null ;
        LocalDate      localDate   = java.time.LocalDate.now();
        LocalDate      localDateP  = localDate.plusMonths(1);
        String         afterMonth  = localDateP.format(myzDateField.DATE_TIME_FORMATER_SAVE);
        
        String    SQL  = "SELECT * FROM VEHICLE_CARD WHERE END_DATE  <= " ;
                  SQL += DbTools.setDB(afterMonth) ;
//        System.out.println(SQL);
        Statement st  = null ;
        ResultSet rs  = null ;
        try
        {
            st = arConnectionInfo.getConnectionContext().m_connection.createStatement();
            rs = st.executeQuery(SQL);
            while(rs.next())
            {
                String platesNo      = rs.getString("CARD_NO");
                String platesEndDate = rs.getString("END_DATE");
                temp = new CardsExpired(platesNo , platesEndDate);
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
    
    private void setCardsNo(String platesNo)
    {
        m_cardNo = platesNo ;
    }
    private void setCardsEndDate(String platesEndDate)
    {
        m_cardEndDate = platesEndDate ;
    }
    public String getM_cardNo()
    {
        return m_cardNo;
    }
    public String getM_cardEndDate()
    {
        return m_cardEndDate ;
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
            titleTable.addCell(ReportDocument.getPdfCell("البطاقات التي تنتهي صلاحيتها قبل " + Tools.getDate(afterMonth, false), ReportDocument.ROW_TYPE_SUB_HEADER));
            pdfDocument.add(titleTable);  
            
            
            
            PdfPTable dataTable = new PdfPTable(2);
            dataTable.setWidthPercentage(98);
            float [] width = new float[2];
            width[0] = 3f;
            width[1] = 3f;
            dataTable.setWidths(width);
            
            //Column Header
            dataTable.addCell(ReportDocument.getPdfCell("تاريخ الإنتهاء", ReportDocument.ROW_TYPE_TITLE));
            dataTable.addCell(ReportDocument.getPdfCell("رقم البطاقة", ReportDocument.ROW_TYPE_TITLE));
            
            for ( int i = 0 ; i < getItems().size() ; i++ )
            {
                CardsExpired cardExpired = ( CardsExpired ) getItems().get(i);
                dataTable.addCell(ReportDocument.getPdfCell(cardExpired.m_cardEndDate, ReportDocument.ROW_TYPE_NORMAL));
                dataTable.addCell(ReportDocument.getPdfCell(cardExpired.m_cardNo, ReportDocument.ROW_TYPE_NORMAL));
            }

            pdfDocument.add(dataTable);            
    
            pdfDocument.close();
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
            System.out.println("Error printTable  : CardsExpiredTable.printTable(); ");
        }         
    }
}

