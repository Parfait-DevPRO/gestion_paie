/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao.sqlite;

import ea2i.tpe.dao.IHistoryDAO;
import ea2i.tpe.model.History;
import static java.awt.image.ImageObserver.WIDTH;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author lenovo
 */
public class HistoryDAO implements IHistoryDAO{
    
    Connection cn = new SQLDataSource().connectionDb();
    private final List<History> listRecent = new ArrayList<>();

   
    @Override
    public void AddHistory(History h) {
       
         try {
                Date now =new Date(h.getDate().getTime());
	        PreparedStatement statCreate = cn.prepareStatement("INSERT INTO history(Action,heure,minute,Date) VALUES(?,?,?,?)");
	            statCreate.setString(1, h.getAction()); 
                    statCreate.setInt(2, h.getHour());
                    statCreate.setInt(3, h.getMinute());
                    statCreate.setDate(4, now);
                    
		    statCreate.executeUpdate();
                    
	    }catch(SQLException ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
                
	    }
        
    }

    @Override
    public List<History> HistoryList() {
       
         boolean correct = false;
         try {
            
            PreparedStatement ps = cn.prepareStatement("SELECT*FROM history ORDER by heure");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                correct = true;
                String action = rs.getString("Action");
                int hour = rs.getInt("heure");
                int minute = rs.getInt("minute");
                Date date = rs.getDate("Date");
               History his = new History(action,hour,minute,date);
               listRecent.add(his);
            }
            if(correct == false){
               JOptionPane.showMessageDialog(null,"Aucun action!", "info", WIDTH); 
            }else{}
	    }catch(SQLException ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              
	    }
        return listRecent;
        
    }

    @Override
    public void deleteHistory() {
        
         try {
               PreparedStatement del = cn.prepareStatement("DELETE FROM history");              
                    del.executeUpdate();                   
	    }catch(Exception ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
            
	    }
        
    }

    
  

  
    
    
    
}
