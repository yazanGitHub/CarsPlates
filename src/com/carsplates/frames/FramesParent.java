/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.frames;

import com.carsplates.main.Main;
import com.myz.component.myzBorderPane;
import com.myz.component.myzButton;
import com.myz.component.myzComboBox;
import java.util.Vector;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Montazar Hamoud
 * This class customize the MYZ border pane for this project .....
 */
public abstract class FramesParent extends myzBorderPane 
{
    
    public FramesParent(String headerText) 
    {
        super(headerText);
        
        //Save previouse frame
        if(Main.SCENE != null)
        {
            Parent prevFrame = Main.SCENE.getRoot() ;
            V_FRAMES.addElement(prevFrame);
        }
        
        
    }
    //Class members
        //Init vector frames (this vector use to save previouse frames)
    public static Vector    V_FRAMES = new Vector<>();
    
    public void initMainButtons(boolean save , boolean back , boolean cancel)
    {
        if(back)
        {
            myzButton backButton = new myzButton()
            {
                @Override
                public void buttonPressed()
                {
                    if(!V_FRAMES.isEmpty())
                    {
                        FramesParent prevFrame = (FramesParent) V_FRAMES.remove( V_FRAMES.size() - 1 );
                        prevFrame.refreshFrame();
                        Main.SCENE.setRoot( prevFrame );
                    }
                }
            };
            backButton.setGraphic(new ImageView(new Image (getClass().getResourceAsStream("/images/back.png") ) ) );
            addToLeft(backButton);
        }
        
    }
    
    public void initFrame(boolean save , boolean back , boolean cancel)
    {
        initCenter();
        initHeader();
        initFooter();
        initLeftSidebar();
        initRightSidebar();
        initFrameBasicData();
        initMainButtons(save, back, cancel);
        
    }
    
    public void refreshFrame()
    {
        myzComboBox comboBox = null ;
        for (Node node : getCenterPane().getChildren())
        {
            if(node instanceof myzComboBox)
            {
                comboBox = (myzComboBox) node ;
                comboBox.refreshData();
            }
            
        }
    }
    
}
