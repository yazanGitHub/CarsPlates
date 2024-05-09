/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.frames;

import com.carsplates.reports.EmbassyReport;
import com.carsplates.reports.PrintReport;
import com.carsplates.reports.ReportDocument;
import com.myz.component.myMultiSelComboBox;
import com.myz.component.myzButton;
import com.myz.component.myzComboBox;
import com.myz.component.myzComponent;
import com.myz.component.myzLabel;
import com.myz.component.myzTableView;
import com.myz.component.myzTextField;
import java.util.Vector;
import myzMessage.myzMessage;
import com.myz.connection.ConnectionContext;
import com.myz.connection.arConnectionInfo;
import com.myz.log.logWriter;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
/**
 *
 * @author yazan
 */
public class ReportFrame extends FramesParent
{
    //Member
    Vector             m_allReports;
    EmbassyReport      m_choosenReport                = null;
    myMultiSelComboBox m_choosenReportDynamicColCombo = null;
    myzTableView       m_reportTable                  = new myzTableView()
    {
        @Override
        public void clickedOnRow()
        {
            m_SearhcItemPane.getChildren().clear();
            m_choosenReportDynamicColCombo = null;
            m_choosenReport = (EmbassyReport) getSelectionModel().getSelectedItem();
            if (m_choosenReport != null )
            {
                setReportSearchComponent(m_choosenReport);
            }
            m_filePath.setText("");
        }
        @Override
        public boolean canAdd()
        {
            return false;
        }
        @Override 
        public boolean canDelete()
        {
            return false;
        }
    };
    TableColumn  m_reportTable_col_reprotName;

    GridPane     m_SearhcItemPane         = new GridPane();
        
    HBox         m_printProperties        = new HBox(10);
    myzButton    m_printReport            = new myzButton("طباعة")
    {
        @Override
        public void buttonPressed()
        {
          printReport();
        }
    };
    
    myzComboBox  m_reportType              = new myzComboBox()
    {
        @Override
        public void selectionChange()
        {
            m_filePath.setText("");
        }
    };
    myzTextField m_filePath                = new myzTextField();
    myzButton    m_chooseFilePathButton    = new myzButton("اختيار مسار الملف")
    {
        @Override
        public void buttonPressed()
        {
            int    reportType = m_reportType.getIntValue();
            if ( reportType < 0)
            {
                myzMessage.alertMessage("الرجاء اختيار نوع ملف التقرير");
                return ;
            }
            FileChooser  fileChooser  = new FileChooser(); 
            fileChooser.setInitialFileName(m_choosenReport.getM_mainTitle());
            FileChooser.ExtensionFilter pdfFilter   = new FileChooser.ExtensionFilter("Pdf Files (*.pdf)", "*.pdf");
            FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx");
            FileChooser.ExtensionFilter wordFilter  = new FileChooser.ExtensionFilter("Word Files (*.docx)", "*.docx");
            switch (m_reportType.getIntValue()) 
            {
                case ReportDocument.TYPE_PDF:
                    fileChooser.getExtensionFilters().addAll(pdfFilter);
                    break;
                case ReportDocument.TYPE_EXCEL:
                    fileChooser.getExtensionFilters().addAll(excelFilter);
                    break;
                case ReportDocument.TYPE_WORD:
                    fileChooser.getExtensionFilters().addAll(wordFilter);
                    break;
                default:
                    break;
            }
            File         file         = fileChooser.showSaveDialog(getScene().getWindow());
            try 
            {
                if (file != null)
                {
                    m_filePath.setText(file.getAbsolutePath());
                }
            }
            catch ( Exception ex)
            {
                System.out.println("Error on saving print file : ReportFrame.m_choosePathButton.buttonPressed(); ");                
                logWriter.write(ex);
            }
        }
    };
    
    //////////////////////////////////////////////////////////////////
    //                          Left sidebar                        // 
    //////////////////////////////////////////////////////////////////
    
    //Constructer
    public ReportFrame() 
    {
        super("التقارير");
        ConnectionContext connection   = arConnectionInfo.getConnectionContext() ;
        m_allReports = EmbassyReport.getBasicReport(connection);
        super.initFrame(false, true, false);
    }
    

    //Method
    public void initPrintPropertiesComp()
    {
        m_printReport.setMinSize(150, 30);
        
        m_reportType.addItems(ReportDocument.getReportTypeVec());
        m_reportType.setMinSize(150, 30);
        
        m_filePath.setMinSize(400, 30);
        m_filePath.setPromptText("مسار حفظ التقرير");
        
        m_chooseFilePathButton.setMinSize(50, 30);
        
        m_printProperties.getChildren().addAll( m_printReport , m_chooseFilePathButton , m_filePath , m_reportType);
        m_printProperties.setAlignment(Pos.TOP_RIGHT);

        
    }
    
    public void initTable()
    {
        m_reportTable_col_reprotName   = new TableColumn("اسم التقرير");
        m_reportTable_col_reprotName.setResizable(false);
        m_reportTable_col_reprotName.setEditable(false);
        m_reportTable_col_reprotName.setCellFactory(TextFieldTableCell.forTableColumn());
//        m_reportTable_col_reprotName.setStyle( "-fx-alignment: CENTER;");
        m_reportTable_col_reprotName.setMinWidth(200);
        m_reportTable_col_reprotName.setCellValueFactory(new PropertyValueFactory<EmbassyReport , String>("m_mainTitle"));
        m_reportTable.getColumns().addAll(m_reportTable_col_reprotName);
        
        m_reportTable.setTableData(m_allReports);
        addToRight(m_reportTable);
    }
    
    public void setReportSearchComponent(EmbassyReport report)
    {
        Vector searchComponents = report.getSearchComponent();
        Vector paneRow          = new Vector();
        int    row              = -1;
        myzLabel     label;
        for( int i = 0 ; i < searchComponents.size() ; i++ )
        {
            if ( i == 0 )// see if the report support dynamic column
            {
                if (m_choosenReport.isSupportDynamicColumn())
                {
                    label = new myzLabel("الأعمدة المراد طباعتها");
                    label.setStyle("-fx-border-color      : red;");
                    m_choosenReportDynamicColCombo = m_choosenReport.getDynamicColumnCombo();
                    paneRow.addElement(label);
                    paneRow.addElement(m_choosenReportDynamicColCombo);
                }
            }
            myzComponent comp  = (myzComponent)searchComponents.elementAt(i);
            label = new myzLabel(comp.getCaption());
            paneRow.addElement(label);
            paneRow.addElement(comp.getNode());

            if (paneRow.size() == 4)
            {
                if ( i == 0)
                    row = 0;
                else
                    row = i/2;
                for ( int j = 0 ; j < paneRow.size() ; j++)
                {
                    m_SearhcItemPane.add((Node) paneRow.elementAt(j), j, row);
                }
                paneRow.clear();
            }
        }
        if (!paneRow.isEmpty())
        {
                for ( int j = 0 ; j < paneRow.size() ; j++)
                {
                    m_SearhcItemPane.add((Node) paneRow.elementAt(j), j, row+1);
                }
        }
    }
    
    public void printReport()
    {
        String filePath   = m_filePath.getText();
        int    reportType = m_reportType.getIntValue();

        if ( m_choosenReport == null)
        {
            myzMessage.alertMessage("الرجاء اختيار تقرير أولا");
            return ;
        }
        if ( filePath == null || filePath.length() < 1)
        {
            myzMessage.alertMessage("الرجاء اختيار مسار لحفظ التقرير");
            return ;
        }

        try
        {
            ConnectionContext connection   = arConnectionInfo.getConnectionContext() ;
            setReportColumn();
            PrintReport tools = new PrintReport(m_choosenReport, null,connection );
            File file = new File(filePath);
            if (!file.exists())
            {
                file.createNewFile();
            }
            tools.print(new FileOutputStream(file), reportType);
            browse(file, false);
        }
        catch(IOException | InterruptedException ex)
        {
           logWriter.write(ex);
        }
    }
    
    public void setReportColumn()
    {
        if ( m_choosenReportDynamicColCombo != null)
        {
            ArrayList addedColumn    = m_choosenReportDynamicColCombo.getSelectedExtraDataValues();
            Vector    basicRerpotCol = m_choosenReport.getBasicCols();
            for ( int i = 0 ; i < addedColumn.size() ; i++)
            {
                basicRerpotCol.addElement(addedColumn.get(i));
            }
            m_choosenReport.setM_vCol(basicRerpotCol);
        }   
    }
    
    
    public static void browse( File file, boolean waitFor ) throws IOException, InterruptedException
    {
        String ext = getFileExtension( file.getName() );

        String[] cmd = new String[ 3 ];
        String   os  = System.getProperty( "os.name" );
        if(  os != null && os.toLowerCase().startsWith("windows" ) ) 
        {
            cmd[ 0 ] = "cmd";
            cmd[ 1 ] = "/c";
            cmd[ 2 ] = file.getPath();
        }
        Process proc = Runtime.getRuntime().exec( cmd );

        int p = 0;
        if( waitFor )
        {
            p     = proc.waitFor();
        }
        if( p != 0 && !"pdf".equalsIgnoreCase( ext ) )
        { 
             throw new IOException( "Error browsing file: '" + file.getName() + "'");
        }
    }

    public static String getFileExtension( String fileName )
    {
        int dot = fileName.lastIndexOf( "." );
        if( dot < 0 )
            dot = fileName.length() - 1;

        String ext = fileName.substring( ++dot );
        return ext;
    }
    
    
    @Override
    public void initFrameBasicData()
    {
        initTable();  
        initPrintPropertiesComp();
    }
    
    @Override
    public void initCenter()
    {
        m_SearhcItemPane.setAlignment(Pos.BASELINE_LEFT);
        m_SearhcItemPane.setHgap(5);
        m_SearhcItemPane.setVgap(8);
        m_SearhcItemPane.setId("center");
        m_SearhcItemPane.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        

        
        VBox center =  new VBox(20);
        center.setAlignment(Pos.TOP_RIGHT);
        center.getChildren().addAll(m_printProperties  ,m_SearhcItemPane );

        setCenter(center);
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
}
