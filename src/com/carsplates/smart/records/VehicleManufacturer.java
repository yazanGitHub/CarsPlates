/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.smart.records;

import com.carsplates.records.VehicleManufacturerRecord;
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
public class VehicleManufacturer extends VehicleManufacturerRecord
{
    //Constructor
    public VehicleManufacturer(ConnectionContext connection) 
    {
        super(connection);
    }
    
    //Methods
    public String getM_tName()
    {
        return m_tName;
    }

    public Vector<VehicleManufacturer> getVehicleManufacturers()
    {
        Vector <VehicleManufacturer> vehicleManufacturers = new Vector<>();
        VehicleManufacturer          vehicleManufacturer  = null ;
        
        String    SQL  = "SELECT * FROM VEHICLE_MANUFACTURER " ;

        Statement st  = null ;
        ResultSet rs  = null ;
        try
        {
            st = arConnectionInfo.getConnectionContext().m_connection.createStatement();
            rs = st.executeQuery(SQL);
            while(rs.next())
            {
                vehicleManufacturer = new VehicleManufacturer(m_connection);
                vehicleManufacturer.get(rs);
                vehicleManufacturers.addElement(vehicleManufacturer);
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
        return vehicleManufacturers;
    }
}
