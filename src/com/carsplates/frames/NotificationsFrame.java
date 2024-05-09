/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.frames;

import com.carsplates.main.Main;
import com.carsplates.tables.CardsExpiredTable;
import com.carsplates.tables.PlatesExpiredTable;
import com.myz.component.myzButton;
import com.myz.log.logWriter;
import java.io.File;
import javafx.geometry.NodeOrientation;
import javafx.stage.FileChooser;

/**
 *
 * @author Montazar Hamoud
 */
public class NotificationsFrame extends FramesParent
{
    PlatesExpiredTable m_platesExpiredTable;
    CardsExpiredTable  m_cardsExpiredTable;
    myzButton m_printPlateTable  = new myzButton("طباعة الجدول")
    {
        @Override
        public void buttonPressed()
        {
            FileChooser                  fileChooser = new FileChooser();
            FileChooser.ExtensionFilter  extFilter   = new FileChooser.ExtensionFilter( " (*.pdf)", "*." + "pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setSelectedExtensionFilter(extFilter);
            File file = fileChooser.showSaveDialog(Main.PRIMARY_STAGE);
        
            if(file == null){ return ;}

            m_platesExpiredTable.printTable(file);
            try
            {
                ReportFrame.browse(file, false);
            }
            catch(Exception ex)
            {    
                logWriter.write(ex);
            }
        }
        
    };  
    myzButton m_printCardTable  = new myzButton("طباعة الجدول")
    {
        @Override
        public void buttonPressed()
        {
            FileChooser                  fileChooser = new FileChooser();
            FileChooser.ExtensionFilter  extFilter   = new FileChooser.ExtensionFilter( " (*.pdf)", "*." + "pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setSelectedExtensionFilter(extFilter);
            File file = fileChooser.showSaveDialog(Main.PRIMARY_STAGE);
        
            if(file == null){ return ;}

            m_cardsExpiredTable.printTable(file);
            try
            {
                ReportFrame.browse(file, false);
            }
            catch(Exception ex)
            {    
                logWriter.write(ex);
            }
        }
        
    };
    
    //Constructor
    public NotificationsFrame() 
    {
        super("الإخطارات ");
        setCenterOrientation(NodeOrientation.RIGHT_TO_LEFT);
        super.initFrame(false, true, false);
    }
    
    //Data member
    
    //////////////////////////////////////////////////////////////////
    //                          Left sidebar                        // 
    //////////////////////////////////////////////////////////////////
    
    //Methods
    @Override
    public void initCenter()
    {
        m_platesExpiredTable = new PlatesExpiredTable();
        m_cardsExpiredTable  = new CardsExpiredTable();
        m_printPlateTable.setMinSize(120, 30);
        m_printCardTable.setMinSize(120, 30);

        getCenterPane().add(m_platesExpiredTable    , 0 , 0 , 4 , 4 );
        getCenterPane().add(m_cardsExpiredTable     , 6 , 0 , 4 , 4 );
        getCenterPane().add(m_printPlateTable    , 2 , 8 , 4 , 4 );
        getCenterPane().add(m_printCardTable     , 8 , 8 , 4 , 4 );
    }

    @Override
    public void initHeader() 
    {
        //Do nothing
    }

    @Override
    public void initFooter() 
    {
       //Do nothing
    }

    @Override
    public void initLeftSidebar() 
    {
        //Do nothing
    }

    @Override
    public void initRightSidebar() 
    {
        //Do nothing
    }

    @Override
    public void initFrameBasicData() 
    {
        //Do nothing
    
    }
}
