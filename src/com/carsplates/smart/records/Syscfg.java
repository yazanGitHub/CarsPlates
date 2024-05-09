/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.smart.records;

import com.carsplates.records.SyscfgRecord;
import com.myz.connection.ConnectionContext;


/**
 *
 * @author Montazar Hamoud
 */
public class Syscfg extends SyscfgRecord
{
    //Constructor
    public Syscfg(ConnectionContext connection)
    {
        super(connection);
    }
    public Syscfg(ConnectionContext connection , String lastUser)
    {
        super(connection, lastUser);
    }
    

    
}
