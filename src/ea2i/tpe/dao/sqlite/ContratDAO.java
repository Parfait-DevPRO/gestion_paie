/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao.sqlite;

import ea2i.tpe.dao.IContratDAO;
import ea2i.tpe.model.Contrat;
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
public class ContratDAO implements IContratDAO{
    
    Connection cn = new SQLDataSource().connectionDb();
    private final List<Contrat> contratList= new ArrayList<>();
    
    @Override
    public boolean createContrat(Contrat c) {
        
      
        
        try {
                Date StartDate = new Date(c.getBeginDate().getTime());
                Date EndDate = new Date(c.getEndDate().getTime());
                
	        PreparedStatement statCreate = cn.prepareStatement("INSERT INTO Contrat(ID,TypeContrat,DateDebut,DateFin,Status,Salaire) VALUES(?,?,?,?,?,?)");
	            statCreate.setLong(1, c.getId());
                    statCreate.setString(2, c.getContratType());    
                    statCreate.setDate(3, StartDate);
		    statCreate.setDate(4, EndDate);
                    statCreate.setBoolean(5, c.isStatusContrat());
                    statCreate.setDouble(6, c.getSalary());    
		    statCreate.executeUpdate();
                    return true;
                    
	    }catch(SQLException ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
                
               return false;
	    }
       
    }

    @Override
    public boolean updateContrat(Contrat c) {
        
        
         try {
                Date StartDate = new Date(c.getBeginDate().getTime());
                Date  EndDate = new Date(c.getEndDate().getTime());
               PreparedStatement del = cn.prepareStatement("DELETE FROM Contrat WHERE ID=?");
                    del.setLong(1, c.getId());
                    del.executeUpdate();
	        PreparedStatement ps = cn.prepareStatement("INSERT INTO Contrat(ID,TypeContrat,DateDebut,DateFin,Status,Salaire) VALUES(?,?,?,?,?,?)");
	            ps.setLong(1, c.getId());
                    ps.setString(2, c.getContratType());
		    ps.setDate(3, StartDate);
		    ps.setDate(4, EndDate);
                    ps.setBoolean(5, c.isStatusContrat());
                    ps.setDouble(6, c.getSalary());
                    
		    ps.executeUpdate();
                    return true;
                    
	    }catch(Exception ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              
              return false;
	    }
        
    }

    @Override
    public void deleteContrat(Contrat c) {
        
        try {
             
               PreparedStatement del = cn.prepareStatement("DELETE FROM Contrat WHERE ID=?");
                    del.setLong(1, c.getId());
                    del.executeUpdate();                   
	    }catch(Exception ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
            
	    }
        
    }

    @Override
    public List<Contrat> readContrat(String element) {
        
        boolean verify = false;
        
          try {
              
                  PreparedStatement search;
              
                 if (element.matches("\\d+(\\.\\d+)?")) {// Test si c'est un nombre
        
                     search = cn.prepareStatement("SELECT * FROM Contrat WHERE ID = ? OR Salaire = ?");
                     search.setLong(1, Long.parseLong(element));
                     search.setDouble(2, Double.parseDouble(element));
                     
                }else{
                
                     search = cn.prepareStatement("SELECT*FROM Contrat WHERE TypeContrat LIKE ? OR DateDebut LIKE ? OR DateFin LIKE ? OR Status LIKE?");
                
                     search.setString(1, "%"+element+"%");
                     search.setString(2, "%"+element+"%");
                     search.setString(3, "%"+element+"%");
                     search.setString(4, "%"+element+"%");
                
                 }
                
                ResultSet rs = search.executeQuery();
                
               while(rs.next()){
                    
                     verify = true;
                   
                     long ID = rs.getLong("ID");
                     String type = rs.getString("TypeContrat");
                     Date dateBegin = rs.getDate("DateDebut");
                     Date dateEnd = rs.getDate("DateFin");
                     double salary = rs.getDouble("Salaire"); 
                     boolean status = rs.getBoolean("Status");
                    
                    Contrat cont = new Contrat(ID,type,dateBegin,dateEnd,salary,status);
                     contratList.add(cont);
                    
                }
	      if(!verify){
                  JOptionPane.showMessageDialog(null, "Aucun élement trouvé!", "Recherche", JOptionPane.ERROR_MESSAGE); 
               }
	    }catch(SQLException ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
             
	    }
         
       return contratList;
        
    }

    @Override
    public List<Contrat> ListContrat() {
        boolean correct = false;
         try {
            
            PreparedStatement ps = cn.prepareStatement("SELECT*FROM Contrat");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                correct = true;
                long ID = rs.getLong("ID");
                String type = rs.getString("TypeContrat");
                Date dateBegin = rs.getDate("DateDebut");
                Date dateEnd = rs.getDate("DateFin");
                double salary = rs.getDouble("Salaire"); 
                boolean status = rs.getBoolean("Status");
                     
               Contrat c = new Contrat(ID,type,dateBegin,dateEnd,salary,status);
               contratList.add(c);
            }
            if(correct == false){
               JOptionPane.showMessageDialog(null,"Liste vide!", "info", WIDTH); 
            }else{}
	    }catch(SQLException ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              
	    }
        return contratList;
        
    }

    @Override
    public List<Contrat> readContrat(long id) {
      
        
        
          try {
                
                PreparedStatement search = cn.prepareStatement("SELECT * FROM Contrat WHERE ID =?");
                
                search.setLong(1, id);
                              
                ResultSet rs = search.executeQuery();
                
               if(rs.next()){
                    
                long ID = rs.getLong("ID");
                String type = rs.getString("TypeContrat");
                Date dateBegin = rs.getDate("DateDebut");
                Date dateEnd = rs.getDate("DateFin");
                double salary = rs.getDouble("Salaire"); 
                boolean status = rs.getBoolean("Status");
                     
               Contrat c = new Contrat(ID,type,dateBegin,dateEnd,salary,status);
               contratList.add(c);
                    
                }
               else{
                 
                   Contrat cont = new Contrat(0,"",null,null,0,false);
                   contratList.add(cont);
                   
               }
	     
	    }catch(SQLException ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              return null;
     
            }
          
       
          
        return contratList;
        
    
    
 }
    
}
