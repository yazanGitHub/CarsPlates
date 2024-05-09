/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.tables;

import com.carsplates.smart.records.VehicleColor;
import com.myz.component.myzTableView;
import com.myz.connection.arConnectionInfo;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import myzMessage.myzMessage;

/**
 *
 * @author Montazar Hamoud
 */
public class VehicleColorTable extends myzTableView
{
    //Constructor
    public VehicleColorTable()
    {
        super();
        setEditable(true);
        
        m_vehcileColor = new TableColumn("ألوان الاليات ");
        m_vehcileColor.setCellValueFactory(new PropertyValueFactory("m_tName"));
        m_vehcileColor.setCellFactory(TextFieldTableCell.forTableColumn());
        m_vehcileColor.setEditable(true);
        m_vehcileColor.setOnEditCommit(new EventHandler<CellEditEvent>() 
        {             
            @Override
            public void handle(CellEditEvent t)
            {             
                if(!myzMessage.confirmMessage("هل تريد حفظ التغيير ؟؟"))
                {
                    reload();
                    return;   
                }
                VehicleColor vehicleColor = ((VehicleColor) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                vehicleColor.setTName( (String) t.getNewValue());
                vehicleColor.save();
                reload();
            }
        });
        
        getColumns().setAll(m_vehcileColor);
        setPrefWidth(150);
        setPrefHeight(300);
        
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        VehicleColor vehicleColor = new VehicleColor(arConnectionInfo.getConnectionContext());
        setTableData(vehicleColor.getVehicleColors());
    }
    //Data members
    TableColumn m_vehcileColor   ;
    
    public void reload()
    {
        VehicleColor vehicleColor = new VehicleColor(arConnectionInfo.getConnectionContext());
        setTableData(vehicleColor.getVehicleColors());
    }
    
    
    

}
