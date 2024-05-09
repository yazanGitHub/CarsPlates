/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.smart.records;

import com.carsplates.records.VehicleColorRecord;
import com.myz.connection.ConnectionContext;
import com.myz.connection.arConnectionInfo;
import com.myz.log.logWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Montazar Hamoud
 */
public class VehicleColor extends VehicleColorRecord
{
    //Constructor
    public VehicleColor(ConnectionContext connection) 
    {
        super(connection);
    }
    
    //Methods
    public String getM_tName()
    {
        return m_tName;
    }

    public Vector<VehicleColor> getVehicleColors()
    {
        Vector <VehicleColor> vehicleColors = new Vector<>();
        VehicleColor          vehicleColor  = null ;
        
        String    SQL  = "SELECT * FROM VEHICLE_COLOR " ;

        Statement st  = null ;
        ResultSet rs  = null ;
        try
        {
            st = arConnectionInfo.getConnectionContext().m_connection.createStatement();
            rs = st.executeQuery(SQL);
            while(rs.next())
            {
                vehicleColor = new VehicleColor(m_connection);
                vehicleColor.get(rs);
                vehicleColors.addElement(vehicleColor);
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
        return vehicleColors;
    }
    
    
}
