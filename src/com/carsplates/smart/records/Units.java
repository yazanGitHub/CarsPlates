/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.smart.records;

import com.carsplates.records.UnitsRecord;
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
public class Units extends UnitsRecord
{
    
    public Units(ConnectionContext connection) 
    {
        super(connection);
    }
    
        //Methods
    public String getM_tName()
    {
        return m_tName;
    }

    public Vector<Units> getUnits()
    {
        Vector <Units> units = new Vector<>();
        Units          unit  = null ;
        
        String    SQL  = "SELECT * FROM UNITS " ;

        Statement st  = null ;
        ResultSet rs  = null ;
        try
        {
            st = arConnectionInfo.getConnectionContext().m_connection.createStatement();
            rs = st.executeQuery(SQL);
            while(rs.next())
            {
                unit = new Units(m_connection);
                unit.get(rs);
                units.addElement(unit);
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
        return units;
    }
    
}
