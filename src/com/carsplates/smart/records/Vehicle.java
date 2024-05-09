/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.smart.records;

import com.carsplates.records.VehicleRecord;
import com.myz.connection.ConnectionContext;
import com.myz.log.logWriter;
import com.myz.record.DbTools;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Montazar Hamoud
 */
public class Vehicle extends VehicleRecord 
{
    //Constructor
    public Vehicle(ConnectionContext connection) 
    {
        super(connection);
    }
    
    //Class member
    public static final int CAR         = 1 ;
    public static final int BICK        = 2 ;
    
    //Methods
    public boolean isUsed(int pnrCurrentMovement )
    {
        int       pnr = 0 ;
        Statement st  = null ;
        ResultSet rs  = null ;
        String    SQL = "SELECT PNR FROM VEHICLE WHERE CHASSIS_NO = " + DbTools.setDB(m_chassisNo)  ;
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if(rs.next())
            {
                pnr = rs.getInt("PNR");
            }
            else
            {
                return false;
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
        SQL  = "SELECT * FROM CURRENT_MOVEMENT WHERE PNR_VEHICLE = " + pnr;
        SQL += " AND PNR != " + DbTools.setDB( pnrCurrentMovement ) ;
        // added by yazan 2020-12-05 
        SQL += " AND PLATE_STATUS NOT IN ( " + Plate.PLATE_STATUS_DROPPED + " )";
                
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            if(rs.next())
            {
                return true;
            }
            else
            {
                setPnr(pnr);
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
}
