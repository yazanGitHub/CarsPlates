package com.carsplates.records;
import com.carsplates.main.Main;
 import  com.myz.connection.ConnectionContext ;
import com.myz.log.logWriter;
 import  com.myz.record.DbTools ;
 import  com.myz.record.Record;
 import  java.sql.ResultSet ;
 
 
 /* 
 * --------------------------------------------------------------------------------
 *                                M   Y   Z                                        
 * --------------------------------------------------------------------------------
 * this file is generated automatically using the JavaRecordsBuilder .             
 * licensed to PRIMASYS company.                                                   
 * --------------------------------------------------------------------------------
 *    represents the table  PSVEHICLE_CARD in database.                        
 * --------------------------------------------------------------------------------
 * @version 2.0                                                                    
 * 
 */
 
 public class VehicleCardRecord extends Record
 {
 
    //Constructor 
    public VehicleCardRecord (  ConnectionContext connection ) 
    {
       super( "VEHICLE_CARD"  , connection ) ;
       setLastUser(Main.USER.getUserName());
    }
 
 ///////////////////////////////////////////////////////////////////////
 //                          Data Members                             //
 ///////////////////////////////////////////////////////////////////////
    protected String     m_cardNo;
    protected String     m_cardCode;
    protected String     m_startDate;
    protected String     m_endDate;
    protected int        m_cardStatus;
    protected int        m_cardTerm;
    protected int        m_pnrPlate;
    protected boolean    m_cardDescribed ;
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
          m_pnr           =   DbTools.getInt(  rs ,  "PNR"  );
          m_cardNo        =   DbTools.getString(  rs ,  "CARD_NO"  );
          m_cardCode      =   DbTools.getString(  rs ,  "CARD_CODE"  );
          m_startDate     =   DbTools.getString(  rs ,  "START_DATE"  );
          m_endDate       =   DbTools.getString(  rs ,  "END_DATE"  );
          m_cardStatus    =   DbTools.getInt(    rs ,  "CARD_STATUS"  );
          m_cardTerm      =   DbTools.getInt(    rs ,  "CARD_TERM"  );
          m_pnrPlate      =   DbTools.getInt(    rs ,  "PNR_PLATE"  );
          m_cardDescribed = DbTools.getBoolean(rs, "CARD_DESCRIBED");
          m_lastTime      =   DbTools.getLong(    rs ,  "LAST_TIME"  );
          m_lastUser      =   DbTools.getString(  rs ,  "LAST_USER"  );
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "vehicleCardRecord.get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
    }
 
 @Override
    public boolean insert ( int type )
    {
 
       try 
       {
          if ( m_pnr < 1 ) 
          {
             String SQL =  " INSERT INTO  VEHICLE_CARD    ( CARD_NO , CARD_CODE  , PNR_PLATE , CARD_DESCRIBED , START_DATE , END_DATE , CARD_STATUS , CARD_TERM , LAST_TIME , LAST_USER  )  VALUES    (  " +  DbTools.setDB ( m_cardNo  ) +   " ,  " +  DbTools.setDB ( m_cardCode  ) + " , " + DbTools.setDB(m_pnrPlate) + " , " + DbTools.setDB(m_cardDescribed) +   " ,  " +  DbTools.setDB ( m_startDate  ) +   " ,  " +  DbTools.setDB ( m_endDate  ) +   " ,  " +  DbTools.setDB ( m_cardStatus  ) + " , " + DbTools.setDB( m_cardTerm ) +   " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
             m_pnr = m_connection.executeSQL( SQL , ConnectionContext.MODE_LAST_ID);
             return m_pnr >= 1 ;
       
          }
 
          else 
          {
             String SQL =  " INSERT INTO  VEHICLE_CARD    ( PNR , CARD_NO , CARD_CODE , PNR_PLATE , CARD_DESCRIBED ,  START_DATE , END_DATE , CARD_STATUS , CARD_TERM , LAST_TIME , LAST_USER  )  VALUES    ( " + DbTools.setDB ( m_pnr ) + " ,   " +  DbTools.setDB ( m_cardNo  ) +   " ,  " +  DbTools.setDB ( m_cardCode  ) + " , " + DbTools.setDB(m_pnrPlate) +    " ,  "  + DbTools.setDB(m_cardDescribed) + " , " +  DbTools.setDB ( m_startDate  ) +   " ,  " +  DbTools.setDB ( m_endDate  ) +   " ,  " +  DbTools.setDB ( m_cardStatus  ) + " , " + DbTools.setDB( m_cardTerm ) +  " ,  " +  DbTools.setDB ( m_lastTime  ) +   " ,  " +  DbTools.setDB ( m_lastUser  ) +   "  ) " ;
 
             return m_connection.executeSQL( SQL);
          }
       }
 
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "vehicleCardRecord .Insert( int type )" + ex.toString()); 
       }
       return false ;
    }
 @Override
    public boolean update( int type )
    {
 
       try 
       {
          String SQL =  " UPDATE   VEHICLE_CARD  Set  "  +  "  CARD_NO =  "  + DbTools.setDB ( m_cardNo ) +  " , CARD_CODE =  "  + DbTools.setDB ( m_cardCode ) + " , PNR_PLATE = " + DbTools.setDB(m_pnrPlate) + " , CARD_DESCRIBED = " + DbTools.setDB(m_cardDescribed) +  " , START_DATE =  "  + DbTools.setDB ( m_startDate ) +  " , END_DATE =  "  + DbTools.setDB ( m_endDate ) +  " , CARD_STATUS =  "  + DbTools.setDB ( m_cardStatus ) + " , CARD_TERM = " + DbTools.setDB( m_cardTerm ) +  " , LAST_TIME =  "  + DbTools.setDB ( m_lastTime ) +  " , LAST_USER =  "  + DbTools.setDB ( m_lastUser ) + " WHERE PNR = " + m_pnr ;
          return m_connection.executeSQL( SQL ) ;
       }
        catch (Exception ex) 
       {
           logWriter.write(ex);
          System.out.println( "vehicleCardRecord .get ( ResultSet rs , int type )" + ex.toString()); 
       }
  
       return false ;
    }
 
 
    // Setter & Getter Methods 
     public void setCardNo(String  cardNo)
    {
       m_cardNo   =   cardNo;
    }
     public String getCardNo()
    {
       return m_cardNo;
    }
 
     public void setCardCode(String  cardCode)
    {
       m_cardCode   =   cardCode;
    }
     public String getCardCode()
    {
       return m_cardCode;
    }
 
    public void setPnrPlate(int pnrPlate)
    {
        m_pnrPlate = pnrPlate;
    }
    public int getPnrPlate()
    {
        return m_pnrPlate ;
    }
    
    public void setCardDescribed(boolean cardDescribed)
    {
        m_cardDescribed = cardDescribed ; 
    }
    public boolean getCardDescribed()
    {
        return m_cardDescribed ;
    }
     public void setStartDate(String  startDate)
    {
       m_startDate   =   startDate;
    }
     public String getStartDate()
    {
       return m_startDate;
    }
 
     public void setEndDate(String  endDate)
    {
       m_endDate   =   endDate;
    }
     public String getEndDate()
    {
       return m_endDate;
    }
 
    public void setCardStatus(int  cardStatus)
    {
       m_cardStatus   =   cardStatus;
    }
     public int getCardStatus()
    {
       return m_cardStatus;
    }
 
     public void setCardTerm(int cardTerm)
     {
        m_cardTerm = cardTerm ;
     }
     public int getCardTerm()
     {
        return m_cardTerm ;
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
 
 