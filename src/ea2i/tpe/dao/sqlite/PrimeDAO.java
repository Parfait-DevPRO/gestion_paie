/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao.sqlite;

import ea2i.tpe.dao.IPrimeDAO;
import ea2i.tpe.model.Prime;
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
public class PrimeDAO implements IPrimeDAO{
    
    Connection cn = new SQLDataSource().connectionDb();
    private final List<Prime> primeList= new ArrayList<>();

    @Override
    public boolean createPrimeEmployee(Prime p) {
        
        try {
                java.sql.Date StartDate = new java.sql.Date(p.getDateBegin().getTime());
                java.sql.Date EndDate = new java.sql.Date(p.getEndDate().getTime());
                
	        PreparedStatement statCreate = cn.prepareStatement("INSERT INTO prime(ID,TypePrime,DateDebut,DateFin,Montant,Status) VALUES(?,?,?,?,?,?)");
	            statCreate.setLong(1, p.getId());
                    statCreate.setString(2, p.getTypePrime());
		    statCreate.setDate(3, StartDate);
		    statCreate.setDate(4, EndDate);
                    statCreate.setDouble(5, p.getMontant());
                    statCreate.setBoolean(6, p.isStatus());
                    
                    
		    statCreate.executeUpdate();
                    return true;
                    
	    }catch(SQLException ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
                
               return false;
	    }
        
    }

    @Override
    public boolean updatePrimeEmployee(Prime p) {
        
        try {
                Date StartDate = new Date(p.getDateBegin().getTime());
                Date EndDate = new Date(p.getEndDate().getTime());
               PreparedStatement del = cn.prepareStatement("DELETE FROM prime WHERE ID=?");
                    del.setLong(1, p.getId());
                    del.executeUpdate();
	        PreparedStatement ps = cn.prepareStatement("INSERT INTO prime(ID,TypePrime,DateDebut,DateFin,Montant,Status) VALUES(?,?,?,?,?,?)");
	            ps.setLong(1, p.getId());
                    ps.setString(2, p.getTypePrime());
		    ps.setDate(3, StartDate);
		    ps.setDate(4, EndDate);
                    ps.setDouble(5, p.getMontant());
                    ps.setBoolean(6, p.isStatus());
                   
		    ps.executeUpdate();
                    return true;
                    
	    }catch(Exception ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              
              return false;
	    }
       
    }

    @Override
    public void deletePrimeEmployee(Prime p) {
        
        try {
             
               PreparedStatement del = cn.prepareStatement("DELETE FROM prime WHERE ID=?");
                    del.setLong(1, p.getId());
                    del.executeUpdate();                   
	    }catch(Exception ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
            
	    }
        
    }

    @Override
    public Prime readPrimeEmployee(String elements) {
        
        Prime prim;
        
         try {
                
                 PreparedStatement search;
              
                 if (elements.matches("\\d+(\\.\\d+)?")) {// Test si c'est un nombre
        
                     search = cn.prepareStatement("SELECT * FROM prime WHERE ID = ? OR Montant = ?");
                     search.setLong(1, Long.parseLong(elements));
                     search.setDouble(2, Double.parseDouble(elements));
                     
                }else{
             
                     search = cn.prepareStatement("SELECT*FROM prime WHERE TypePrime LIKE ? OR DateDebut LIKE ? OR DateFin LIKE ? OR Status LIKE?");
                
                     search.setString(1, "%"+elements+"%");
                     search.setString(2, "%"+elements+"%");
                     search.setString(3, "%"+elements+"%");
                     search.setString(4, "%"+elements+"%");
               
                }
                
                ResultSet rs = search.executeQuery();
                
               if(rs.next()){
                    
                     long ID = rs.getLong("ID");
                     String type = rs.getString("TypePrime");
                     Date dateBegin = rs.getDate("DateDebut");
                     Date dateEnd = rs.getDate("DateFin");
                     double montant = rs.getDouble("Montant"); 
                     boolean status = rs.getBoolean("Status");
                    
                     prim = new Prime(ID,type,dateBegin,dateEnd,montant,status);
                    
                }
	      else{
                  JOptionPane.showMessageDialog(null, "Aucun élement trouvé!", "Recherche", JOptionPane.ERROR_MESSAGE); 
                  return null;
               }
	    }catch(SQLException ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              return null;
             
	    }
         
       return prim;
        
    }

    @Override
    public double getPrimeMontant(long id) {
        
         double salaire;
        
          try {
                
                PreparedStatement search = cn.prepareStatement("SELECT Salaire FROM employee WHERE ID =?");
                
                search.setLong(1, id);
                              
                ResultSet rs = search.executeQuery();
                
               if(rs.next()){
                    
                    double salary = rs.getDouble("Salaire");
                    
                  salaire = salary;
                    
                }
	      else{
                  JOptionPane.showMessageDialog(null, "Aucun élement trouvé!", "Recherche", WIDTH); 
                  return 0;
               }
	    }catch(SQLException ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              return 0;
	    }
          
       
          
        return salaire;
       
    }

    @Override
    public List<Prime> ListPrime() {
        
        boolean correct = false;
         try {
            
            PreparedStatement ps = cn.prepareStatement("SELECT*FROM prime ORDER by ID");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                correct = true;
                long ID = rs.getLong("ID");
                String type = rs.getString("TypePrime");
                Date dateBegin = rs.getDate("DateDebut");
                Date dateEnd = rs.getDate("DateFin");
                double montant = rs.getDouble("Montant"); 
                boolean status = rs.getBoolean("Status");
                     
               Prime p = new Prime(ID,type,dateBegin,dateEnd,montant,status);
               primeList.add(p);
            }
            if(correct == false){
               JOptionPane.showMessageDialog(null,"Liste vide!", "info", WIDTH); 
            }else{}
	    }catch(SQLException ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              
	    }
        return primeList;
        
    }

    @Override
    public List<Prime> readPrimeEmployee(long id) {

       
         try {
                
                PreparedStatement search = cn.prepareStatement("SELECT * FROM prime WHERE ID =?");
                
                search.setLong(1, id);
                              
                ResultSet rs = search.executeQuery();
                
               if(rs.next()){
                    
               long ID = rs.getLong("ID");
                String type = rs.getString("TypePrime");
                Date dateBegin = rs.getDate("DateDebut");
                Date dateEnd = rs.getDate("DateFin");
                double montant = rs.getDouble("Montant"); 
                boolean status = rs.getBoolean("Status");
                     
               Prime p = new Prime(ID,type,dateBegin,dateEnd,montant,status);
               primeList.add(p);
                    
                }
               else{
                  Prime p = new Prime(0,"",null,null,0,false);
                  primeList.add(p);  
               }
	     
	    }catch(SQLException ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              return null;
     
            }
        return primeList;
        
    }
        
    }

 
