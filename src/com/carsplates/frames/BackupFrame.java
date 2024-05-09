/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.frames;

import com.carsplates.backup.Backup;
import com.carsplates.main.Main;
import com.myz.component.myzButton;
import java.io.File;
import javafx.geometry.NodeOrientation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import myzMessage.myzMessage;


/**
 *
 * @author Montazar Hamoud
 */
public class BackupFrame extends FramesParent
{
    //Constructor
    public BackupFrame() 
    {
        super("النسخ الاحتياطي ");
        setCenterOrientation(NodeOrientation.RIGHT_TO_LEFT);
        super.initFrame(false, true, false);
    }
    
    //Data member
    //////////////////////////////////////////////////////////////////
    //                          Left sidebar                        // 
    //////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////
    //                          Center                              // 
    //////////////////////////////////////////////////////////////////
    myzButton  m_doBackup   = new myzButton("نسخ احتياطي اني ")
    {
        @Override
        public void buttonPressed()
        {
            FileChooser      fileChooser = new FileChooser();
            FileChooser.ExtensionFilter  extFilter   = new FileChooser.ExtensionFilter( " (*.zip)", "*.zip");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setSelectedExtensionFilter(extFilter);
            File file = fileChooser.showSaveDialog(Main.PRIMARY_STAGE);
            
            Backup backup = new Backup();
            
            if(backup.fullBackup(file))
            {
                myzMessage.noteMessage("تم إنشاء النسخ الاحتياطي بنجاح" );
            }
            else
            {
                myzMessage.alertMessage("حدث خطأ ما أثناء النسخ الاحتياطي الرجاء إعادة المحاولة"  );
            }
            
        }
    };
    
    //Methods
    
    @Override
    public void initCenter()
    {
        getCenterPane().setHgap(10);
        getCenterPane().setVgap(10);
        
        int column = 3 ;
        int row    = 3 ;
        // رقم اللوحة والمحافظة 
        getCenterPane().add(m_doBackup  , column++ , row );
    }
    

    @Override
    public void initHeader() 
    {
        //do nothing
    }

    @Override
    public void initFooter() 
    {
        //do nothing
    }

    @Override
    public void initLeftSidebar() 
    {
        //Do nothing
    }

    @Override
    public void initRightSidebar() 
    {
        //do nothing
    }

    @Override
    public void initFrameBasicData() 
    {
        //do nothing
    }
}
