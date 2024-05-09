/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carsplates.tools;

import com.carsplates.smart.records.DocFiles;
import com.myz.connection.ConnectionContext;
import com.myz.connection.arConnectionInfo;
import com.myz.log.logWriter;
import com.myz.record.DbTools;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import myzMessage.myzMessage;

/**
 *
 * @author Montazar Hamoud
 */
public class myzAttachPanel extends HBox
{
    //Constructor
    public myzAttachPanel(int mode) 
    {
        super(10);

        if(mode == MODE_ENABLE)
            initAttachPanelEvents();
        
    }
    //Class Member
    public static final int MODE_DISABLE = 0 ;
    public static final int MODE_ENABLE  = 1 ;
    
    //Data member
    ConnectionContext m_connection = arConnectionInfo.getConnectionContext();
    ScrollPane        m_scrollPane = null ; 
    int               m_pnrDeliveredInfo ;
    
    
    public void setPnrDeliveredInfo(int pnrDeliveredInfo)
    {
        m_pnrDeliveredInfo = pnrDeliveredInfo;
    }
    
    public int getPnrDeliveredInfo()
    {
        return m_pnrDeliveredInfo ;
    }
    //Methods
    EventHandler onDragOver   = new EventHandler<DragEvent>() 
    {
        @Override
        public void handle(DragEvent event) 
        {
            setStyle("-fx-background-color:#fffff2;");
            mouseDragOver(event);
         }
            
    };
    
    EventHandler onDragDroped = new EventHandler<DragEvent>() 
    {
        @Override
        public void handle(final DragEvent event)
        {
            mouseDragDropped(event);
        }
    };
    
    EventHandler onDragExited = new EventHandler<DragEvent>() 
    {
        @Override
        public void handle(final DragEvent event)
        {
            setStyle("-fx-border-color: #C6C6C6;");
        }
    };
    
    public final void initAttachPanelEvents()
    {        
        setWidth(USE_PREF_SIZE);
        setHeight(125);
        Image image = new Image(this.getClass().getResourceAsStream("/images/drag&drop.png"));
        setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        
        setOnDragOver(onDragOver);
        setOnDragDropped(onDragDroped);
        setOnDragExited(onDragExited); 
    }
    
    private void mouseDragDropped(final DragEvent e) 
    {
        final Dragboard db = e.getDragboard();
        boolean success = false;
        if(m_pnrDeliveredInfo <= 0)
        {
            myzMessage.alertMessage("لا يوجد عملية تسليم بعد لايمكنك الأرشفة"  );
            return;
        }
        if (db.hasFiles()) 
        {
            List<File> files  = db.getFiles();
            for(File file :files)
            {
                int pnrDocFile = saveFile(file);
                if(pnrDocFile > 0)
                    createImageView(file , pnrDocFile);
            }

            success = true;
        }
        e.setDropCompleted(success);
        e.consume();
    }
    
    private  void mouseDragOver(final DragEvent e) 
    {
        final Dragboard db = e.getDragboard();
 
        final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".png")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpg")
                || db.getFiles().get(0).getName().endsWith(".Gif")
                || db.getFiles().get(0).getName().endsWith(".MYZ");

 
        if (db.hasFiles())
        {
            if (isAccepted)
            {
                e.acceptTransferModes(TransferMode.COPY);
            }
        }
        else 
        {
            e.consume();
        }
    }
    
    public void disable()
    {
        setOnDragOver(null);
        setOnDragDropped(null);
        setOnDragExited(null);
    }
   //TODO
    private int saveFile(File file )
    {        
        DocFiles df = new DocFiles(m_connection);
        df.setPnrDeliveredInfo(m_pnrDeliveredInfo);
        
        if(df.checkIn(file))
        {
            //If does not save then delete the previouse file 
            if(!df.save())
                df.deletePhysicalFile();
        }
        return df.getPnr();
    }
    
    private void createImageView(File file , int pnrDocFile)
    {
        try
        {
            InputStream is      = new FileInputStream(file);
            Image     image     = new Image ( is  );
            ImageView imageView = new ImageView(image);
            imageView.setId(String.valueOf(pnrDocFile) );
            imageView.setFitHeight(125);
            imageView.setFitWidth(150);
            imageView.setEffect(new DropShadow(20, Color.BLACK));
            if(is != null){is.close();}
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED , new EventHandler<MouseEvent>() 
            {
                @Override
                public void handle(MouseEvent event) 
                {
                    if(event.getClickCount() == 2)
                    {
                        Stage   window = new Stage();
                        window.initStyle(StageStyle.DECORATED);
                        window.initModality(Modality.APPLICATION_MODAL);
                        window.setResizable(true);
                        window.setMaximized(true);

                        ImageView iv = new ImageView(imageView.getImage() );
                        ScrollPane scrollPane = new ScrollPane(iv);
                        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                        scrollPane.setFitToWidth(true);
                        scrollPane.setFitToHeight(true);
                        scrollPane.setMinSize(300 , 100);

                        Scene scene = new Scene(scrollPane , 900 , 700);
                        window.setScene(scene);
                        window.showAndWait();
                        
                    }
                    
                }
            });
            
            //Setting context menu to the image view
            MenuItem delete = new MenuItem("حذف ");
            delete.setOnAction((ActionEvent e) -> 
            {
                String sPnrDocFile = imageView.getId();
                
                DocFiles df = new DocFiles(m_connection);
                df.get(Integer.parseInt(sPnrDocFile));
                if(df.deletePhysicalFile())
                {
                    getChildren().remove(imageView);
                    df.delete();
                }
                
            });
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().add(delete);
            imageView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() 
            {
                @Override
                public void handle(ContextMenuEvent event) 
                {
                    contextMenu.show(imageView, event.getScreenX(), event.getScreenY());
                }
            });
            getChildren().add(imageView);            
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }
    }
    
    public void loadFiles()
    {
        String     SQL  = "SELECT * FROM DOC_FILES WHERE PNR_DELIVERED_INFO = " + DbTools.setDB(m_pnrDeliveredInfo) ;
        Statement  st   = null ;
        ResultSet  rs   = null ;
        File       file = null ;
        DocFiles   df   = null ;
        try
        {
            st = m_connection.m_connection.createStatement();
            rs = st.executeQuery(SQL);
            while(rs.next())
            {
                df   = new DocFiles(m_connection);
                df.get(rs);
                file = df.checkOut();
                createImageView(file , rs.getInt("PNR"));
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
        
    }

    public void removeChildren()
    {
        this.getChildren().removeAll( this.getChildren());
    }
}
