/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.smart.records;

import com.carsplates.records.VehicleCardRecord;
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
public class VehicleCard extends VehicleCardRecord 
{
    //Constructor
    public VehicleCard(ConnectionContext connection)
    {
        super(connection);
    }
    //Class members
    public static final int CARD_STATUS_EXISTED   = 1 ;// في حال الادخال اول مرة او أعيدت الي
    public static final int CARD_STATUS_DELIVERED = 2 ;// في حال سلمت الى سائق او جهة معينة
    public static final int CARD_STATUS_DROPPED   = 3 ;// في حال أعيدت الى اصحابها 
    
    public static final String S_CARD_STATUS_EXISTED   = "موجودة";
    public static final String S_CARD_STATUS_DELIVERED = "مسلمة";
    public static final String S_CARD_STATUS_DROPPED   = "مسقطة";
    //Methods
    public boolean isUsed(int pnrCurrentMovement )
    {
        String  SQL  = "SELECT *    FROM CURRENT_MOVEMENT WHERE PNR_VEHICLE_CARD IN " ;
                SQL += "(SELECT PNR FROM VEHICLE_CARD WHERE CARD_NO = " +  DbTools.setDB(m_cardNo) +" )" ;
                SQL += " AND PNR != " + DbTools.setDB(pnrCurrentMovement) ;

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
    
    public boolean isExist()
    {
        String  SQL  = "SELECT * FROM VEHICLE_CARD WHERE CARD_NO = " + DbTools.setDB(m_cardNo);
                SQL += " AND PNR != " + DbTools.setDB(m_pnr) ;
                        
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
    
    public static String getCardStatusString(int status)
    {
        if (status == CARD_STATUS_DROPPED)
            return S_CARD_STATUS_DROPPED;
        else if (status == CARD_STATUS_DELIVERED)
            return S_CARD_STATUS_DELIVERED;
        else if (status == CARD_STATUS_EXISTED)
            return S_CARD_STATUS_EXISTED;
        return "";
    }
    
    public static Vector getCardStatusVec()
    {
        Vector status = new Vector();
        myzComboBoxItem item = null;
        item = new myzComboBoxItem(S_CARD_STATUS_EXISTED, CARD_STATUS_EXISTED);
        status.add(item);
        item = new myzComboBoxItem(S_CARD_STATUS_DELIVERED, CARD_STATUS_DELIVERED);
        status.add(item);
        item = new myzComboBoxItem(S_CARD_STATUS_DROPPED, CARD_STATUS_DROPPED);
        status.add(item);
        return status;
    }
}
