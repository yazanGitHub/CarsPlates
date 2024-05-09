package com.carsplates.records ;

import com.carsplates.main.Main;
import com.myz.connection.ConnectionContext;
import com.myz.log.logWriter;
import com.myz.record.DbTools;
import com.myz.record.Record;
import  java.sql.ResultSet ;

 
 
 /* 
 * --------------------------------------------------------------------------------
 *                                M   Y   Z                                        
 * --------------------------------------------------------------------------------
 * this file is generated automatically using the JavaRecordsBuilder .             
 * licensed to PRIMASYS company.                                                   
 * --------------------------------------------------------------------------------
 *    represents the table  PS_RECEIVED_INFO in database.                        
 * --------------------------------------------------------------------------------
 * @version 2.0                                                                    
 * 
 */
 
 public class DeliveredInfoRecord extends Record
 {
 
    //Constructor 
    public DeliveredInfoRecord (ConnectionContext connection ) 
    {
       super( "DELIVERED_INFO" , connection ) ;
       setLastUser(Main.USER.getUserName());
    }
 
 ///////////////////////////////////////////////////////////////////////
 //                          Data Members                             //
 ///////////////////////////////////////////////////////////////////////
    protected String     m_receiverName;
    protected String     m_receiverPhone;
    protected String     m_receiverCellular;
    protected int        m_pnrDeliveredUnit;
    protected String     m_deliveredDate;
    protected String     m_deliveredEndDate;
    protected String     m_actualDeliveredDate;
    protected String     m_actualRecivedDate;
    protected int        m_deliveredTerm;
    protected int        m_orderTask;
    protected String     m_orderTaskDate;
    protected int        m_femmeTask;
    protected String     m_femmeTaskDate;
    protected int        m_femmeDescription;
    protected long       m_lastTime;
    protected String     m_lastUser;
 
 
 ///////////////////////////////////////////////////////////////////////
 //                              Methods                              //
 ///////////////////////////////////////////////////////////////////////
 @Override
    public void get( ResultSet rs , int type )
    {
 
       try 
       {
          m_pnr                  =   DbTools.getInt(  rs ,  "PNR"  );
          m_receiverName         =   DbTools.getString(  rs ,  "RECEIVER_NAME"  );
          m_receiverPhone        =   DbTools.getString(  rs ,  "RECEIVER_PHONE"  );
          m_receiverCellular     =   DbTools.getString(  rs ,  "RECEIVER_CELLULAR"  );
          m_deliveredDate        =   DbTools.getString(  rs ,  "DELIVERED_DATE"  );
          m_deliveredEndDate     =   DbTools.getString(  rs ,  "DELIVERED_END_DATE"  );
          m_actualDeliveredDate  =   DbTools.getString(  rs ,  "ACTUAL_DELIVERED_DATE"  );
          m_actualRecivedDate    =   DbTools.getString(  rs ,  "ACTUAL_RECEIVED_DATE"  );
          m_orderTaskDate        =   DbTools.getString(  rs ,  "ORDER_TASK_DATE");
          m_femmeTaskDate        =   DbTools.getString(  rs ,  "FEMME_TASK_DATE"  );
          m_lastUser             =   DbTools.getString(  rs ,  "LAST_USER"  );
          m_pnrDeliveredUnit     =   DbTools.getInt(  rs    ,  "PNR_DELIVERED_UNIT"  );
          m_deliveredTerm        =   DbTools.getInt(  rs    ,  "DELIVERED_TERM"  );
          m_orderTask            =   DbTools.getInt(  rs    ,  "ORDER_TASK"  );
          m_femmeTask            =   DbTools.getInt(  rs    ,  "FEMME_TASK"  );
          m_femmeDescription     =   DbTools.getInt(  rs    ,  "FEMME_DESCRIPTION"  );
          m_lastTime             =   DbTools.getLong( rs    ,  "LAST_TIME"  );
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "ReceivedInfoRecord.get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
    }
 
 @Override
    public boolean insert ( int type )
    {
 
       try 
       {
          if ( m_pnr < 1 ) 
          {
             String SQL =  " INSERT INTO  DELIVERED_INFO    ( RECEIVER_NAME , RECEIVER_PHONE , RECEIVER_CELLULAR , PNR_DELIVERED_UNIT ,  DELIVERED_DATE , DELIVERED_END_DATE , ACTUAL_DELIVERED_DATE , ACTUAL_RECEIVED_DATE , DELIVERED_TERM , ORDER_TASK , ORDER_TASK_DATE   , FEMME_TASK , FEMME_TASK_DATE , FEMME_DESCRIPTION , LAST_TIME , LAST_USER  )  VALUES    (  " +  DbTools.setDB ( m_receiverName  ) +   " ,  " +  DbTools.setDB ( m_receiverPhone  ) +   " ,  " +  DbTools.setDB ( m_receiverCellular  ) + " , " + DbTools.setDB(m_pnrDeliveredUnit ) +  " ,  " +  DbTools.setDB (m_deliveredDate  ) +   " ,  " +  DbTools.setDB (m_deliveredEndDate  ) + " , " + DbTools.setDB( m_actualDeliveredDate ) + " , " + DbTools.setDB(m_actualRecivedDate) +    " ,  " +  DbTools.setDB (m_deliveredTerm  ) +   " ,  " +  DbTools.setDB ( m_orderTask  ) + " , " + DbTools.setDB( m_orderTaskDate ) +   " ,  " +  DbTools.setDB ( m_femmeTask  ) +   " ,  " +  DbTools.setDB ( m_femmeTaskDate  ) +   " ,  " +  DbTools.setDB ( m_femmeDescription  ) +   " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
             m_pnr = m_connection.executeSQL( SQL , ConnectionContext.MODE_LAST_ID);
             return m_pnr >= 1 ;
       
          }
 
          else 
          {
             String SQL =  " INSERT INTO  DELIVERED_INFO    ( PNR , RECEIVER_NAME , RECEIVER_PHONE , RECEIVER_CELLULAR , PNR_DELIVERED_UNIT , DELIVERED_DATE , DELIVERED_END_DATE , ACTUAL_DELIVERED_DATE , ACTUAL_RECEIVED_DATE , DELIVERED_TERM , ORDER_TASK , ORDER_TASK_DATE , FEMME_TASK , FEMME_TASK_DATE , FEMME_DESCRIPTION , LAST_TIME , LAST_USER  )  VALUES    ( " + DbTools.setDB ( m_pnr ) +  " ,  " +  DbTools.setDB ( m_receiverName  ) +   " ,  " +  DbTools.setDB ( m_receiverPhone  ) +   " ,  " +  DbTools.setDB ( m_receiverCellular  ) + " , " + DbTools.setDB(m_pnrDeliveredUnit ) +  " ,  " +  DbTools.setDB (m_deliveredDate  ) +   " ,  " +  DbTools.setDB (m_deliveredEndDate  ) + " , " + DbTools.setDB( m_actualDeliveredDate ) + " , " + DbTools.setDB( m_actualRecivedDate ) +  " ,  " +  DbTools.setDB (m_deliveredTerm  ) +   " ,  " +  DbTools.setDB ( m_orderTask  ) + " , " + DbTools.setDB( m_orderTaskDate ) +   " ,  " +  DbTools.setDB ( m_femmeTask  ) +   " ,  " +  DbTools.setDB ( m_femmeTaskDate  ) +   " ,  " +  DbTools.setDB ( m_femmeDescription  ) +   " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
 
             return m_connection.executeSQL( SQL);
          }
       }
 
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "ReceivedInfoRecord .Insert( int type )" + ex.toString()); 
       }
       return false ;
    }
 @Override
    public boolean update( int type )
    {
 
       try 
       {
          String SQL =  " UPDATE   DELIVERED_INFO  Set  "   +  " RECEIVER_NAME =  "  + DbTools.setDB ( m_receiverName ) +  " , RECEIVER_PHONE =  "  + DbTools.setDB ( m_receiverPhone ) +  " , RECEIVER_CELLULAR =  "  + DbTools.setDB ( m_receiverCellular ) + " , PNR_DELIVERED_UNIT = " + DbTools.setDB(m_pnrDeliveredUnit ) + " , DELIVERED_DATE =  "  + DbTools.setDB (m_deliveredDate ) +  " , DELIVERED_END_DATE =  "  + DbTools.setDB (m_deliveredEndDate ) + " , ACTUAL_DELIVERED_DATE = " + DbTools.setDB( m_actualDeliveredDate ) + " , ACTUAL_RECEIVED_DATE = " + DbTools.setDB( m_actualRecivedDate ) +  " , DELIVERED_TERM =  "  + DbTools.setDB (m_deliveredTerm ) +  " , ORDER_TASK =  "  + DbTools.setDB ( m_orderTask ) + " , ORDER_TASK_DATE = " + DbTools.setDB( m_orderTaskDate ) +  " , FEMME_TASK =  "  + DbTools.setDB ( m_femmeTask ) +  " , FEMME_TASK_DATE =  "  + DbTools.setDB ( m_femmeTaskDate ) +  " , FEMME_DESCRIPTION =  "  + DbTools.setDB ( m_femmeDescription ) +  " , LAST_TIME =  "  + DbTools.setDB ( m_lastTime ) +  " , LAST_USER =  "  + DbTools.setDB ( m_lastUser ) + " WHERE PNR = " + m_pnr ;
          return m_connection.executeSQL( SQL ) ;
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "ReceivedInfoRecord .get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
       return false ;
    }
 
 
    // Setter & Getter Methods 
     public void setReceiverName(String  receiverName)
    {
       m_receiverName   =   receiverName;
    }
     public String getReceiverName()
    {
       return m_receiverName;
    }
 
     public void setReceiverPhone(String  receiverPhone)
    {
       m_receiverPhone   =   receiverPhone;
    }
     public String getReceiverPhone()
    {
       return m_receiverPhone;
    }
 
     public void setReceiverCellular(String  receiverCellular)
    {
       m_receiverCellular   =   receiverCellular;
    }
     public String getReceiverCellular()
    {
       return m_receiverCellular;
    }
    
    public void setPnrDeliveredUnit(int pnrDeliveredUnit)
    {
       m_pnrDeliveredUnit = pnrDeliveredUnit ;
    }
    public int getPnrDeliveredUnit()
    {
        return m_pnrDeliveredUnit ;
    }
     public void setDeliveredDate(String  DeliveredDate)
    {
       m_deliveredDate   =   DeliveredDate;
    }
     public String getDeliveredDate()
    {
       return m_deliveredDate;
    }
 
     public void setDeliveredEndDate(String  deliveredEndDate)
    {
       m_deliveredEndDate   =   deliveredEndDate;
    }
     public String getDeliveredEndDate()
    {
       return m_deliveredEndDate;
    }
     
    public void setActualReceivedDate(String actualReceivedDate )
    {
        m_actualRecivedDate = actualReceivedDate ;
    }
    public String getActualReceivedDate()
    {
        return m_actualRecivedDate ;
    }
 
    public void setActualDeliveredDate(String actualDeliveredDate)
    {
        m_actualDeliveredDate = actualDeliveredDate ;
    }
    public String getActualDeliveredDate()
    {
        return m_actualDeliveredDate ;
    }
    
     public void setDeliveredTerm(int  deliveredTerm)
    {
       m_deliveredTerm   =   deliveredTerm;
    }
     public int getDeliveredTerm()
    {
       return m_deliveredTerm;
    }
 
     public void setOrderTask(int  orderTask)
    {
       m_orderTask   =   orderTask;
    }
     public int getOrderTask()
    {
       return m_orderTask;
    }
     
     public void setOrderTaskDate(String orderTaskDate)
     {
         m_orderTaskDate = orderTaskDate ;
     }
     public String getOrderTaskDate()
     {
         return m_orderTaskDate ;
     }
     public void setFemmeTask(int  femmeTask)
    {
       m_femmeTask   =   femmeTask;
    }
     public int getFemmeTask()
    {
       return m_femmeTask;
    }
 
     public void setFemmeTaskDate(String  femmeTaskDate)
    {
       m_femmeTaskDate   =   femmeTaskDate;
    }
     public String getFemmeTaskDate()
    {
       return m_femmeTaskDate;
    }
 
     public void setFemmeDescription(int  femmeDescription)
    {
       m_femmeDescription   =   femmeDescription;
    }
     public int getFemmeDescription()
    {
       return m_femmeDescription;
    }
 
     public void setLastTime(long  lastTime)
    {
       m_lastTime   =   lastTime;
    }
     public long getLastTime()
    {
       return m_lastTime;
    }
 
     public void setLastUser(String  lastUser)
    {
       m_lastUser   =   lastUser;
    }
     public String getLastUser()
    {
       return m_lastUser;
    }
 
 
 }
 
 