/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.reports;

import com.myz.connection.ConnectionContext;
import com.myz.log.logWriter;
import com.myz.reportComponent.ManualReportRow;
import java.util.Map;
import java.util.Vector;
import java.sql.ResultSet;
import java.io.OutputStream;
import com.myz.reportComponent.ReportColNumber;
/**
 *
 * @author yazan
 */
public class PrintReport 
{
    EmbassyReport      m_report;
    Map                m_params;
    ConnectionContext  m_connection;

    public PrintReport( EmbassyReport report , Map params, ConnectionContext connection)
    {
        m_report      = report;
        m_params      = params;
        m_connection  = connection;
    }

    public void print( OutputStream output, int reportType )
    {

        String         SQL      = m_report.m_query;
        if ( m_report.addRowCountColumn())// to add the header of the column count
        {
            m_report.m_vCol.add( 0 , new ReportColNumber("EMPTY", "ت"  , 1));
        }
        ReportDocument document = new ReportDocument( m_report, output, reportType );
        String         where    = m_report.getReportWhere();
        if (where != null && where.length() > 0 )
            SQL += " AND " + where;
        
        String orderBy = m_report.getOrderBy();
        if (orderBy != null && orderBy.length() > 0 )
            SQL += " " + orderBy;
              
        System.out.println("SQL = " + SQL);
        ResultSet       rs          = null;
        ManualReportRow reportRow   = new ManualReportRow() ;
        reportRow.m_vReportCols     = m_report.m_vCol;
        Vector    row ;
        int       rowCount          = 1;
        try
        {
            rs  = m_connection.getResultSet( SQL );
            while(rs.next())
            {
                row = reportRow.getRow( rs );
                // to add the row count for evry row
                //هون نحنا فعليا عم نعبي قيمةفاضية للعمود الفاضي يلي عم نجيبو بالكويري بس هون عم نبدلها برقم السطر
                if ( m_report.addRowCountColumn())
                {
                    row.removeElementAt(0);
                    row.add( 0 , String.valueOf(rowCount));
                }
                document.addRow( rowCount, row, document.ROW_TYPE_NORMAL );                
                row.removeAllElements();
                rowCount++;
            }
            rs.close();
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }
        finally
        {
            if (rs != null)
            try 
            {
                rs.close();
            } 
            catch (Exception ex) 
            {
                logWriter.write(ex);
            }
        }

        if ( reportType == ReportDocument.TYPE_PDF )
        {
            try
            {
                document.m_data.setSpacingAfter( 10f );
                document.m_pdfDocument.add( document.m_data );
                if ( m_report.addTotalCountRow())
                    document.addCountRow(rowCount);
            }
            catch(Exception ex )
            {
                logWriter.write(ex);
            }

        }
        try
        {
            document.close(m_params);
        }
        catch( Exception ex )
        {
            logWriter.write(ex);
        }            
    }

  }


