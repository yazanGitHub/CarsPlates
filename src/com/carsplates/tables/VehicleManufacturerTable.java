/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.tables;

import com.carsplates.smart.records.VehicleManufacturer;
import com.myz.component.myzTableView;
import com.myz.connection.arConnectionInfo;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import static javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import myzMessage.myzMessage;

/**
 *
 * @author Montazar Hamoud
 */
public class VehicleManufacturerTable extends myzTableView
{
        //Constructor
    public VehicleManufacturerTable()
    {
        super();
        setEditable(true);
        
        m_vehicleManufacturer = new TableColumn("شركة تصنيع الاليات ");
        m_vehicleManufacturer.setCellValueFactory(new PropertyValueFactory("m_tName"));
        m_vehicleManufacturer.setCellFactory(TextFieldTableCell.forTableColumn());
        m_vehicleManufacturer.setEditable(true);
        m_vehicleManufacturer.setOnEditCommit(new EventHandler<CellEditEvent>() 
        {             
            @Override
            public void handle(CellEditEvent t)
            {             
                if(!myzMessage.confirmMessage("هل تريد حفظ التغيير ؟؟"))
                {
                    reload();
                    return;   
                }
                
                VehicleManufacturer vm = ((VehicleManufacturer) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                vm.setTName( (String) t.getNewValue());
                vm.save();
                reload();
            }
        });
        
        getColumns().setAll(m_vehicleManufacturer);
        setPrefWidth(150);
        setPrefHeight(300);
        
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        VehicleManufacturer vehicleManufacturer = new VehicleManufacturer(arConnectionInfo.getConnectionContext());
        setTableData(vehicleManufacturer.getVehicleManufacturers());
    }
    //Data members
    TableColumn m_vehicleManufacturer   ;
    
    public void reload()
    {
        VehicleManufacturer vehicleManufacturer = new VehicleManufacturer(arConnectionInfo.getConnectionContext());
        setTableData(vehicleManufacturer.getVehicleManufacturers());
    }
}
