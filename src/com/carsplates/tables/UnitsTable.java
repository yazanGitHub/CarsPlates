/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.tables;


import com.carsplates.smart.records.Units;
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
public class UnitsTable extends myzTableView
{
        //Constructor
    public UnitsTable()
    {
        super();
        setEditable(true);
        
        m_unitsCol = new TableColumn("الوحدات المستلمة ");
        m_unitsCol.setCellValueFactory(new PropertyValueFactory("m_tName"));
        m_unitsCol.setCellFactory(TextFieldTableCell.forTableColumn());
        m_unitsCol.setEditable(true);
        m_unitsCol.setOnEditCommit(new EventHandler<CellEditEvent>() 
        {             
            @Override
            public void handle(CellEditEvent t)
            {             
                if(!myzMessage.confirmMessage("هل تريد حفظ التغيير ؟؟"))
                {
                    reload();
                    return;   
                }
                
                Units unit = ((Units) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                unit.setTName( (String) t.getNewValue());
                unit.save();
                reload();
            }
        });

        getColumns().setAll(m_unitsCol);
        setPrefWidth(150);
        setPrefHeight(300);
        
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        Units unit = new Units(arConnectionInfo.getConnectionContext());
        setTableData(unit.getUnits());
    }
    //Data members
    TableColumn m_unitsCol   ;
    
    public void reload()
    {
        Units unit = new Units(arConnectionInfo.getConnectionContext());
        setTableData(unit.getUnits());
    }
}
