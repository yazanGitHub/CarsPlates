/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.reports;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.myz.log.logWriter;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;




/**
 * @author yazan
 */
public class ReceiptReport
{
    // class Member
    public static final int CELL_TYPE_TITLE        = 1;
    public static final int CELL_TYPE_NORMAL       = 2;
    public static final int CELL_TYPE_FOOTER       = 3;
    
    public static final String PARAM_KEY_PLATE_NO        = "PLATE_NO";
    public static final String PARAM_KEY_RECEIVER_NAME   = "RECEIVER_NAME";
    public static final String PARAM_KEY_DEILEVERD_DATE  = "DELIVERED_DATE";
    public static final String PARAM_KEY_VEC_MAN_NAME    = "VEC_MAN_NAME";
    public static final String PARAM_KEY_VEC_COLOR       = "VEC_COLOR";
    public static final String PARAM_KEY_VEC_MODEL       = "VEC_MODEL";
    public static final String PARAM_KEY_VEC_CHASSIS_NO  = "VEC_CHASSIS_NO";
    public static final String PARAM_KEY_PLATE_TYPE_NAME = "PLATE_TYPE_NAME";
    public static final String PARAM_KEY_CARD_END_DATE   = "END_DATE";
    public static final String PARAM_KEY_RECEIPT_NUMBER  = "RECEIPT_NUMBER";
    public static final String PARAM_KEY_CARD_DESCRIBED  = "CARD_DESCRIBED";
    


    
    // Member
    Map m_params;
    
    public ReceiptReport(Map params)
    {
        m_params = params;
    }
    
    public void print(File file)
    {
        try
        {
            boolean cardDescribed = m_params.get(PARAM_KEY_CARD_DESCRIBED).equals("true");
            FileOutputStream stream = new FileOutputStream(file);
            // Add Data to pdf File
            Document pdfDocument = new Document( PageSize.A4,10f,10f,20f,10f );
            PdfWriter.getInstance(pdfDocument, stream);
            pdfDocument.open();
            pdfDocument.addCreationDate();
            
            PdfPCell cell = null;

            
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setRunDirection(getRunDirection());
            headerTable.setWidthPercentage(98);
            cell = getPdfCell("بسمه تعالى", CELL_TYPE_TITLE);
            headerTable.addCell(cell);
            headerTable.setSpacingAfter(10);
            pdfDocument.add(headerTable);            

            PdfPTable dataTable = new PdfPTable(3);
            dataTable.setWidthPercentage(98);
            float [] width = new float[3];
            width[0] = 3f;
            width[1] = 3f;
            width[2] = 3f;
            dataTable.setWidths(width);
            
            //First Row
            String str = ""; 
            dataTable.addCell(getPdfCell(null, CELL_TYPE_NORMAL));
            dataTable.addCell(getPdfCell(null, CELL_TYPE_NORMAL));  
            String deliveredDate = String.valueOf(m_params.get(PARAM_KEY_DEILEVERD_DATE));
            deliveredDate = deliveredDate.equals("null") || deliveredDate.length() < 1 ? "" : myzTools.Tools.getDate(deliveredDate, false);
            str  = "در تاريخ" + " : " + deliveredDate;
            dataTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL));
            
            
            //Second Row
            str  = "استلمت من" + " : " ;
            dataTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL));
            dataTable.addCell(getPdfCell(null, CELL_TYPE_NORMAL));  
            str  = "أنا الموقع أدناه" + " : " + m_params.get(PARAM_KEY_RECEIVER_NAME);
            dataTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL));
            
            
            //Third Row
            if ( cardDescribed )
            {
                str  = "كيلومتراج" + " KM : "  ;
                dataTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL));
                dataTable.addCell(getPdfCell(null, CELL_TYPE_NORMAL));
                String vecManName = String.valueOf(m_params.get(PARAM_KEY_VEC_MAN_NAME) );
                vecManName = vecManName.equals("null") || vecManName.length() < 1 ? "" : vecManName;
                str  = "سيارة نوع" + " : " + vecManName;
                dataTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL));
                
                //Fourth Row
                String vecModel = String.valueOf(m_params.get(PARAM_KEY_VEC_MODEL));
                vecModel        = vecModel == null || vecModel.equals("null") ? "" : vecModel;
                str  = "الموديل" + " : " + vecModel;
                dataTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL));
                dataTable.addCell(getPdfCell(null, CELL_TYPE_NORMAL)); 
                String vecColor = String.valueOf(m_params.get(PARAM_KEY_VEC_COLOR));
                vecColor = vecColor.equals("null") || vecColor.length() < 1 ? "" : vecColor ;
                str  = "اللون" + " : " + vecColor;
                dataTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL));
              
            }


            
            
            //Fifth Row
            str  = "رقم الإيصال" + " : " + m_params.get(PARAM_KEY_RECEIPT_NUMBER);
            dataTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL));
            str  = "نوع اللوحة" + " : "  + m_params.get(PARAM_KEY_PLATE_TYPE_NAME);
            dataTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL)); 
            str  = "رقم اللوحة" + " : " + m_params.get(PARAM_KEY_PLATE_NO);
            dataTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL));
                       
            //Fourth Row
            String endDate = String.valueOf(m_params.get(PARAM_KEY_CARD_END_DATE));
            endDate = endDate.equals("null") || endDate.length() < 1 ? "" : myzTools.Tools.getDate(endDate, false);
            str  = "تاريخ انتهاء البطاقة" + " : " + endDate;
            dataTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL));
            dataTable.addCell(getPdfCell(null, CELL_TYPE_NORMAL)); 
            if ( cardDescribed )
            {
                str  = "رقم الشاسي";
                dataTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL)); 
            }
            else
            {
               dataTable.addCell(getPdfCell(null, CELL_TYPE_NORMAL)); 
            }

            
            pdfDocument.add(dataTable);            
            if ( cardDescribed )
            {
                PdfPTable chassisTable = new PdfPTable(1);
                chassisTable.setRunDirection(getRunDirection());
                chassisTable.setWidthPercentage(98);
                String chassisNo = String.valueOf(m_params.get(PARAM_KEY_VEC_CHASSIS_NO));
                chassisNo = chassisNo.equals("null") || chassisNo.length() < 1 ? "" : chassisNo ;
                chassisTable.addCell(getPdfCell(chassisNo, CELL_TYPE_TITLE));
                chassisTable.setSpacingAfter(20);
                pdfDocument.add(chassisTable); 
            }
           
            
            PdfPTable noteTable = new PdfPTable(1);
            noteTable.setRunDirection(getRunDirection());
            noteTable.setWidthPercentage(98);
            noteTable.addCell(getPdfCell("ملاحظات", CELL_TYPE_NORMAL));
            pdfDocument.add(noteTable);   
            
            PdfPTable noteBoxTable = new PdfPTable(1);
            noteBoxTable.setRunDirection(getRunDirection());
            noteBoxTable.setWidthPercentage(98);
            cell = getPdfCell(" ", CELL_TYPE_TITLE);
            cell.setFixedHeight(40);
            noteBoxTable.addCell(cell);
            pdfDocument.add(noteBoxTable);
            
            //Footer Row
            PdfPTable footerTable = new PdfPTable(3);
            footerTable.setSpacingBefore(10);
            footerTable.setWidthPercentage(98);
            width = new float[3];
            width[0] = 3f;
            width[1] = 3f;
            width[2] = 3f;
            footerTable.setWidths(width);
            //First Row 
            str = "اسم و توقيع المسلم"; 
            footerTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL));
            footerTable.addCell(getPdfCell(null, CELL_TYPE_NORMAL));  
            str  = "اسم و توقيع المستلم";
            footerTable.addCell(getPdfCell(str, CELL_TYPE_NORMAL));
            // Second Row
            footerTable.addCell(getPdfCell(null, CELL_TYPE_NORMAL));
            footerTable.addCell(getPdfCell(null, CELL_TYPE_NORMAL)); 
            footerTable.addCell(getPdfCell(String.valueOf(m_params.get(PARAM_KEY_RECEIVER_NAME)), CELL_TYPE_NORMAL));
            
            pdfDocument.add(footerTable);


  
            
            pdfDocument.close();
        }
        catch(FileNotFoundException | DocumentException ex)
        {
            logWriter.write(ex);
            System.out.println("Error on saving print file : TakweemReport.printReport(); ");
        }
        
    }
    
    public File getFileFromPath(String filePath)
    {
        File   file;        
        if ( filePath == null || filePath.length() < 1)
        {
            return null;
        }
        
        file = new File(filePath);
        if (!file.exists())
        {
            try 
            {
                file.createNewFile();
            }
            catch (IOException ex) 
            {
                logWriter.write(ex);
                System.out.println("Error on get file to report : TakweemReport.getFileFromPath();");

            }  
        }
        return file;
 
    }
        
    PdfPCell getPdfCell( String str, int cellType )
    {
        if( str == null )
        {
            str = " ";
        }
        PdfPCell cell = new PdfPCell();
        Font fnt       = ReportDocument.FONT_TITLE;
        int border     = PdfPCell.NO_BORDER;
        int hAlignment = PdfPCell.ALIGN_LEFT;
        int vAlignment = PdfPCell.ALIGN_MIDDLE;
        Color bkGround = Color.white;
        if( cellType == CELL_TYPE_TITLE )
        {
            fnt        = ReportDocument.FONT_TITLE_BOLD;
            border     = PdfPCell.BOX;
            hAlignment = PdfPCell.ALIGN_CENTER;
            
        }
        if( cellType == CELL_TYPE_FOOTER)
        {
            fnt       = ReportDocument.FONT_UNDERLINE_13;
        }
        cell.setPhrase(new Phrase( str, fnt));
        
        cell.getExtraParagraphSpace();
        cell.setRunDirection(getRunDirection());
        cell.setBorder( border );
        if( border != PdfPCell.NO_BORDER )
        {
          cell.setBorderWidth( .1f );
        }
        cell.setPaddingBottom( 5f );
        cell.setHorizontalAlignment( hAlignment );
        cell.setVerticalAlignment( vAlignment );
        cell.setBackgroundColor( bkGround );
        return cell;
    }

   private int getRunDirection()
   {
     return PdfWriter.RUN_DIRECTION_RTL;
   }


   private Font getFont( int size, int style )
   {
       //ae_AlMohanad
     boolean registered = FontFactory.contains( "arialbd" );
     if( !registered )
     {
       FontFactory.register( "arialbd.ttf" );
     }
     String fontPath = "arialbd.ttf";
     return FontFactory.getFont( fontPath , BaseFont.IDENTITY_H, BaseFont.EMBEDDED, size, style,java.awt.Color.black );
   }


}
