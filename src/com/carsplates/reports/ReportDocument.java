/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.reports;
import com.lowagie.text.Document;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.PageSize;
import java.io.OutputStream;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.Phrase;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import java.util.Map;
import com.lowagie.text.pdf.PdfPTable;
import com.myz.component.myzComboBoxItem;
import com.myz.log.logWriter;
import com.myz.reportComponent.ReportCol;
import java.util.Vector;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.awt.Color;
import org.apache.poi.xwpf.usermodel.TableRowAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 *
 * @author yazan
 */
public class ReportDocument
{
    public static final int ROW_TYPE_NORMAL       = 0;//data spreadsheet
    public static final int ROW_TYPE_TITLE        = 1;//data title (columns)
    public static final int ROW_TYPE_MAIN_HEADER  = 10;//report name
    public static final int ROW_TYPE_SUB_HEADER   = 11;//search criteria (from...to...)
    public static final int ROW_TYPE_STAMP        = 12;//al jamhourieh al3arabieh... + (department)
    public static final int ROW_TYPE_INTRODUMTION = 20;//isharatan ila kitabikom...etc

    public static final int TYPE_PDF   = 1;
    public static final int TYPE_EXCEL = 2;
    public static final int TYPE_WORD  = 3;
    
    public static final String S_TYPE_PDF   = "Pdf";
    public static final String S_TYPE_EXCEL = "Excel";
    public static final String S_TYPE_WORD  = "Word";
                                        
    public static final int FOOTER_MODE_EMPTY                 = 0;
    public static final int FOOTER_MODE_RECEIVER_DELIVER_NAME = 1;
    

    public static Font FONT_TITLE_BOLD   = getFont(15, Font.BOLD);
    public static Font FONT_TITLE        = getFont(12, Font.NORMAL);
    public static Font FONT_DETAILS      = getFont(10, Font.NORMAL);
    public static Font FONT_UNDERLINE_13 = getFont(13, Font.UNDERLINE);

   

    
    //general
    int            m_type    = TYPE_PDF;
    boolean        m_error   = false;
    public boolean m_complete;
    OutputStream   m_output;
    
    EmbassyReport  m_report;
    //for PDF
    Document       m_pdfDocument;
    PdfPTable      m_data;

    //for Excel
    XSSFWorkbook   m_excelWB;
    XSSFSheet      m_excelSheet;
    
    //for Word
    XWPFDocument m_wordDocument;
    XWPFTable    m_wordTable;

    public ReportDocument( EmbassyReport report, OutputStream output, int reportType )
    {
        m_report = report;
        m_output = output;
        if( reportType == TYPE_EXCEL )
        {
            m_type = TYPE_EXCEL;
        }
        else if (reportType == TYPE_WORD)
        {
            m_type = TYPE_WORD;
        }
        try
        {
            init( report );
        }
        catch( Exception ex )
        {
          logWriter.write(ex);
         m_error = true;
        }
    }

    void init( EmbassyReport report) throws Exception
    {
        float []width     = new float[report.m_vCol.size()];


    //set the sum in units
        float totalSum = 0;
        for( int i = 0; i < report.m_vCol.size(); i++ )
        {
            ReportCol col = ( ReportCol )report.m_vCol.elementAt( i );
            width[ report.m_vCol.size()- i -1  ]    = col.getUnits();
        }

        if( m_type == TYPE_EXCEL )
        {
            m_excelWB = new XSSFWorkbook();
           
            m_excelWB.createSheet();
            //m_workbook.setCompressTempFiles( true );
            m_excelSheet = (XSSFSheet) m_excelWB.getSheetAt(0);
            m_excelSheet.setRightToLeft(true);
        }
        else if ( m_type == TYPE_WORD)
        {
            m_wordDocument =  new XWPFDocument();
            m_wordTable    = m_wordDocument.createTable();
            m_wordTable.setTableAlignment(TableRowAlign.CENTER);
            for( int i = 0; i < report.m_vCol.size() - 1; i++ )
            {
                m_wordTable.addNewCol();
            }
            
        }
        else if ( m_type == TYPE_PDF)
        {
            m_pdfDocument  = new Document( PageSize.A4,10f,10f,20f,10f );
            PdfWriter.getInstance( m_pdfDocument , m_output );
            m_pdfDocument.open();
            addHeaders(report);
            m_complete = false;
            m_data     = new PdfPTable( report.m_vCol.size() )
            {
                public boolean isComplete()
                {
                    return m_complete;
                }
            };
             m_data.setWidthPercentage( 100 );
             m_data.setWidths( width );
        }

        Vector row = new Vector();
        for( int i = 0; i < report.m_vCol.size(); i++ )
        {
            ReportCol col = ( ReportCol )report.m_vCol.elementAt( i );
            row.addElement( col.m_caption );
        }

        addRow( 0, row, ROW_TYPE_TITLE );
    }


    public void addRow( int rowNum, Vector row, int rowType )
    {
        if( m_error )
        {
            return;
        }
        if( m_type == TYPE_EXCEL )
        {
            Row exr = m_excelSheet.createRow( rowNum );
            for (int i = row.size()-1 ; i >=0 ; i-- )
            {
                String str = (String) row.elementAt(i);
                createExcelCell( exr, i, str );
            }
        }
        else if ( m_type == TYPE_WORD)
        {
            XWPFTableRow wordRow = m_wordTable.createRow();
            for (int i = 0 ; i < row.size() ; i++ )
            {
                String str = (String) row.elementAt(i);
                wordRow.getCell( row.size() - 1 - i).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                wordRow.getCell( row.size() - 1 - i).setText(str);
            }
        }
        else
        {
            for (int i = row.size()-1 ; i >=0 ; i-- )
            {
                String col = (String) row.elementAt(i);
                m_data.addCell( getPdfCell(col, rowType ) );
                if( rowNum > 0 && ( rowNum % 500 == 0 ) )
                {
                    try
                    {
                      m_pdfDocument.add( m_data );
                    }
                    catch( Exception ex )
                    {
                      logWriter.write(ex);
                    }
                }
            }
        }
    }
    public void addCountRow(int rowCount)
    {
        rowCount = rowCount - 1;
        String    str           = "العدد الكلي" + " : " + rowCount;
        PdfPTable rowCountTable = new PdfPTable(1);
        PdfPCell cell;
        rowCountTable.setTotalWidth(1);
        rowCountTable.setWidthPercentage(35);
        rowCountTable.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        rowCountTable.setRunDirection( getRunDirection() );
        cell = getPdfCell(str, ROW_TYPE_SUB_HEADER );
        rowCountTable.addCell( cell );
        try
        {
          m_pdfDocument.add(rowCountTable);
        }
        catch( Exception ex )
        {
          logWriter.write(ex);
        }
    }

    public void addBasicData()
    {

    }

    public void close(Map params) throws Exception
    {
        if( m_type == TYPE_EXCEL )
        {
            m_excelWB.write( m_output );
            m_output.close();
        }
        else if (m_type == TYPE_WORD)
        {
            m_wordTable.removeRow(0);
            m_wordDocument.write(m_output);
            m_output.close();

        }
        else if (m_type == TYPE_PDF)
        {
            m_complete = true;
            addFooter(m_report.getFooterMode());
            m_pdfDocument.close();
        }
    }

    public void addFooter(int mode)
    {
        if( m_type == TYPE_EXCEL || m_type == TYPE_WORD)
        {
          return;
        }
        if ( (mode & FOOTER_MODE_RECEIVER_DELIVER_NAME) == FOOTER_MODE_RECEIVER_DELIVER_NAME)
        {
            try
            {
//                float []width     = new float[2];
//                width[0] = 2;
//                width[1] = 2;
                PdfPTable letterFooter = new PdfPTable(2);
//                letterFooter.setWidths(width);
                PdfPCell   cell;
                letterFooter.setTotalWidth(2);
                letterFooter.setWidthPercentage(98);
                letterFooter.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                letterFooter.setRunDirection( getRunDirection() );
                cell = getPdfCell("توقيع المسلم", ROW_TYPE_STAMP );
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                letterFooter.addCell( cell );
                cell = getPdfCell("توقيع المستلم", ROW_TYPE_STAMP );
                cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                letterFooter.addCell( cell );
                m_pdfDocument.add(letterFooter);

            }
            catch(Exception ex)
            {
                logWriter.write(ex);
            }
        }
    }



    public void addHeaders(  EmbassyReport report )
    {
        if( m_type == TYPE_EXCEL || m_type == TYPE_WORD)
        {
          return;
        }
        PdfPCell cell;
        
        PdfPTable hraderTable = new PdfPTable(1);
        hraderTable.setTotalWidth(1);
        hraderTable.setWidthPercentage(98);
        hraderTable.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        hraderTable.setRunDirection( getRunDirection() );
        cell = getPdfCell( "بسمه تعالى", ROW_TYPE_STAMP  );
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        hraderTable.addCell(cell);

        cell = getPdfCell( "التاريخ" + " : " + myzTools.Tools.getDate(java.time.LocalDate.now(), false) , ROW_TYPE_STAMP  );
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        hraderTable.addCell(cell);
       



        PdfPTable titleTable = new PdfPTable( 1 );
        titleTable.setTotalWidth(1);
        titleTable.setWidthPercentage(100);
        titleTable.setRunDirection( getRunDirection() );
        titleTable.setSpacingAfter( 20f );

 
        cell = getPdfCell( report.m_mainTitle , ROW_TYPE_MAIN_HEADER );
        titleTable.addCell( cell );
        
        String searchTitle = report.getSearchTitle();
        cell = getPdfCell( searchTitle != null ? searchTitle : "" , ROW_TYPE_SUB_HEADER );
        titleTable.addCell( cell );
        
        try
        {
          m_pdfDocument.add(hraderTable);
          m_pdfDocument.add(titleTable);
        }
        catch( Exception ex )
        {
          logWriter.write(ex);
        }
  }

    void createExcelCell( Row row, int cellnum, String str )
    {
        Cell cell = row.createCell(cellnum);
        cell.setCellValue( str );
    }
//    
//    void createWordCell(XWPFTableRow row, int cellnum, String str)
//    {
//        XWPFTableCell cell =  row.createCell();
//        cell.setText(str);
//       
//    }
    

    public static PdfPCell getPdfCell( String str, int rowType )
    {
        if( str == null )
        {
            str = " ";
        }
        PdfPCell cell = new PdfPCell();

        Font fnt       = FONT_DETAILS;
        int border     = PdfPCell.BOX;
        int hAlignment = PdfPCell.ALIGN_CENTER;
        int vAlignment = PdfPCell.ALIGN_MIDDLE;
        Color bkGround = Color.white;
        if( rowType == ROW_TYPE_MAIN_HEADER )
        {
            fnt          = FONT_TITLE_BOLD;
            border       = PdfPCell.NO_BORDER;
        }
        if( rowType == ROW_TYPE_SUB_HEADER )
        {
            fnt          = FONT_TITLE;
            border       = PdfPCell.NO_BORDER;
            bkGround     = Color.lightGray;
        }
        if( rowType == ROW_TYPE_STAMP )
        {
            fnt          = FONT_TITLE;
            border       = PdfPCell.NO_BORDER;
            hAlignment   = PdfPCell.ALIGN_RIGHT;
        }
        if( rowType == ROW_TYPE_TITLE )
        {
            fnt          = FONT_DETAILS;
        }
        if( rowType == ROW_TYPE_INTRODUMTION )
        {
            fnt          = FONT_TITLE;
            border       = PdfPCell.NO_BORDER;
            hAlignment   = PdfPCell.ALIGN_LEFT;
        }

        cell.setPhrase(new Phrase( str, fnt));

        cell.getExtraParagraphSpace();
        cell.setRunDirection(getRunDirection());
        cell.setBorder( border );
        if( border != PdfPCell.NO_BORDER )
        {
            cell.setBorderWidth( .1f );
        }
        cell.setPaddingBottom( 3f );
        cell.setHorizontalAlignment( hAlignment );
        cell.setVerticalAlignment( vAlignment );
        cell.setBackgroundColor( bkGround );

        return cell;
    }

   public static int getRunDirection()
   {
     return PdfWriter.RUN_DIRECTION_RTL;
   }
   


   public static Font getFont( int size, int style )
   {
        boolean registered = FontFactory.contains( "arialbd" );
        if( !registered )
        {
            FontFactory.register( "arialbd.ttf" );
        }
        String fontPath = "arialbd.ttf";
        return FontFactory.getFont( fontPath , BaseFont.IDENTITY_H, BaseFont.EMBEDDED, size, style,java.awt.Color.black );
   }
   
   public static Vector getReportTypeVec()
   {
        Vector reportType = new Vector();
       
        myzComboBoxItem type = new myzComboBoxItem(S_TYPE_PDF, TYPE_PDF);
        reportType.add(type);
       
        type = new myzComboBoxItem(S_TYPE_EXCEL, TYPE_EXCEL);
        reportType.add(type);
       
        type = new myzComboBoxItem(S_TYPE_WORD, TYPE_WORD);
        reportType.add(type);
       
       return reportType;
   }
}
