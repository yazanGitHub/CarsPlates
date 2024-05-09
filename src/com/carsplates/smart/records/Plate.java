/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.smart.records;

import com.carsplates.records.PlateRecord;
import com.myz.component.myzComboBoxItem;
import com.myz.connection.ConnectionContext;
import com.myz.log.logWriter;
import com.myz.record.DbTools;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Montazar Hamoud
 */
public class Plate extends PlateRecord
{
    //Constructor
    public Plate(ConnectionContext connection) 
    {
        super(connection);
    }
    //Class members
    public static final int PLATE_STATUS_EXISTED   = 1 ;// في حال الادخال اول مرة او أعيدت الي
    public static final int PLATE_STATUS_DELIVERED = 2 ;// في حال سلمت الى سائق او جهة معينة
    public static final int PLATE_STATUS_DROPPED   = 3 ;// في حال أعيدت الى اصحابها 
    
    public static final String S_PLATE_STATUS_EXISTED   = "موجودة";
    public static final String S_PLATE_STATUS_DELIVERED = "مسلمة";
    public static final String S_PLATE_STATUS_DROPPED   = "مسقطة";
    //Methods
    public boolean isExist()
    {
        String  SQL  = "SELECT * FROM PLATE WHERE PLATE_NO = " + DbTools.setDB(m_plateNo) ;
                SQL += " AND PNR_PLATE_CITY = " + DbTools.setDB(m_pnrPlateCity) ;
                SQL += "  AND PNR != " + DbTools.setDB(m_pnr) ;
                        
        Statement st  = null ;
        ResultSet rs  = null ;
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if(rs.next())
                return true;
            
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
            return false ; 
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
        return false ;
    }
    
    public boolean getByNoAndCity(String plateNo , int pnrCity)
    {
        String    SQL  = "SELECT * FROM PLATE WHERE PLATE_NO = " + DbTools.setDB(plateNo);
                  SQL += " AND PNR_PLATE_CITY = " + DbTools.setDB(pnrCity);
        Statement st  = null ;
        ResultSet rs  = null ;
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if(rs.next())
            {
                get(rs);
                return true ;
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
        return false ;
    }
    
    public static String getPlatStatusString(int status)
    {
        if (status == PLATE_STATUS_DROPPED)
            return S_PLATE_STATUS_DROPPED;
        else if (status == PLATE_STATUS_DELIVERED)
            return S_PLATE_STATUS_DELIVERED;
        else if (status == PLATE_STATUS_EXISTED)
            return S_PLATE_STATUS_EXISTED;
        return "";
    }
    
    public static Vector getPlateStatusVec()
    {
        Vector status = new Vector();
        myzComboBoxItem item = null;
        item = new myzComboBoxItem(S_PLATE_STATUS_EXISTED, PLATE_STATUS_EXISTED);
        status.add(item);
        item = new myzComboBoxItem(S_PLATE_STATUS_DELIVERED, PLATE_STATUS_DELIVERED);
        status.add(item);
        item = new myzComboBoxItem(S_PLATE_STATUS_DROPPED, PLATE_STATUS_DROPPED);
        status.add(item);
        return status;
    }
    
    
}
