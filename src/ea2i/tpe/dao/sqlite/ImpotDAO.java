/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao.sqlite;

import ea2i.tpe.dao.IImpotDAO;
import ea2i.tpe.model.Impot;
import static java.awt.image.ImageObserver.WIDTH;
import java.sql.Connection;
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
public class ImpotDAO implements IImpotDAO{
    
    Connection cn = new SQLDataSource().connectionDb();
    List<Impot> impotslist = new ArrayList<>();

    @Override
    public boolean createImpot(Impot imp) {
        
        try {
                
	        PreparedStatement statCreate = cn.prepareStatement("INSERT INTO impot_cotisation(ID,Mois,Impot,TauxImpot,Cotisation,TauxCotisation,TotalTaux,TotalMontants) VALUES(?,?,?,?,?,?,?,?)");
	            statCreate.setLong(1, imp.getId());
                    statCreate.setString(2, imp.getMonth());
                    statCreate.setString(3, imp.getTypesImpot());
		    statCreate.setDouble(4, imp.getTauxImpot());
		    statCreate.setString(5, imp.getTypesCotisations());
                    statCreate.setDouble(6, imp.getTauxCotisations());
                    statCreate.setDouble(7, imp.getTotalTaux());
                    statCreate.setDouble(8, imp.getTotalMontants());
                    
		   statCreate.executeUpdate();
                   return true;

                 } catch (SQLException ex) {
                   JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
                   return false;
                }
        
    }

    @Override
    public boolean UpdateImpot(Impot imp) {
        
        try {
               
               PreparedStatement del = cn.prepareStatement("DELETE FROM impot_cotisation WHERE ID=?");
                    del.setLong(1, imp.getId());
                    del.executeUpdate();
	        PreparedStatement ps = cn.prepareStatement("INSERT INTO impot_cotisation(ID,Mois,Impot,TauxImpot,Cotisation,TauxCotisation,TotalTaux,TotalMontants) VALUES(?,?,?,?,?,?,?,?)");
	            ps.setLong(1, imp.getId());
                    ps.setString(2, imp.getMonth());
                    ps.setString(3, imp.getTypesImpot());
		    ps.setDouble(4, imp.getTauxImpot());
		    ps.setString(5, imp.getTypesCotisations());
                    ps.setDouble(6, imp.getTauxCotisations());
                    ps.setDouble(7, imp.getTotalTaux());
                    ps.setDouble(8, imp.getTotalMontants());
                    
		    ps.executeUpdate();
                    return true;
                    
	    }catch(Exception ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
                //ex.printStackTrace();
              
              return false;
	    }
        
    }

    @Override
    public void deleteImpot(Impot imp) {
        
        try {
             
               PreparedStatement del = cn.prepareStatement("DELETE FROM impot_cotisation WHERE ID=?");
                    del.setLong(1, imp.getId());
                    del.executeUpdate();                   
	    }catch(Exception ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
            
	    }
        
    }

    @Override
    public List<Impot> readImpot(String element) {
        
        boolean verify = false;
        
         try {
                
                 PreparedStatement search;
              
                 if (element.matches("\\d+(\\.\\d+)?")) {// Test si c'est un nombre
        
                     search = cn.prepareStatement("SELECT * FROM impot_cotisation WHERE ID = ? OR TauxImpot = ? OR TauxCotisation =? OR TotalTaux=? OR TotalMontants=? ");
                     search.setLong(1, Long.parseLong(element));
                     search.setDouble(2, Double.parseDouble(element));
                     search.setDouble(3, Double.parseDouble(element));
                     search.setDouble(4, Double.parseDouble(element));
                     search.setDouble(5, Double.parseDouble(element));
                     
                }else{
             
                     search = cn.prepareStatement("SELECT*FROM impot_cotisation WHERE Mois LIKE? OR Impot LIKE ? OR Cotisation LIKE ?");
                
                     search.setString(1, "%"+element+"%");
                     search.setString(2, "%"+element+"%");
                     search.setString(3, "%"+element+"%");
               
                 }
                ResultSet rs = search.executeQuery();
                
               while(rs.next()){
                   
                     verify = true;
                    
                     long ID = rs.getLong("ID");
                     String month = rs.getString("Mois");
                     String impots = rs.getString("Impot");
                     double tauxImpot = rs.getDouble("TauxImpot");
                     String cotisation = rs.getString("Cotisation");
                     double tauxCotisa = rs.getDouble("TauxCotisation");
                     double totalTaux = rs.getDouble("TotalTaux"); 
                     double totalMontant = rs.getDouble("TotalMontants");
                    
                    Impot impt = new Impot(ID,month,impots,tauxImpot,cotisation,tauxCotisa,totalTaux,totalMontant);
                     impotslist.add(impt);
                    
                }
	      if(!verify){
                  JOptionPane.showMessageDialog(null, "Aucun élement trouvé!", "Recherche", JOptionPane.ERROR_MESSAGE); 
               }
	    }catch(SQLException ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
             
	    }
         
       return impotslist;
       
    }

    @Override
    public List<Impot> ListImpot() {
        
         boolean correct = false;
         try {
            
            PreparedStatement ps = cn.prepareStatement("SELECT*FROM impot_cotisation ORDER by ID");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                correct = true;
                long ID = rs.getLong("ID");
                String month = rs.getString("Mois");
                String impots = rs.getString("Impot");
                double tauxImpot = rs.getDouble("TauxImpot");
                String cotisation = rs.getString("Cotisation");
                double tauxCotisa = rs.getDouble("TauxCotisation");
                double totalTaux = rs.getDouble("TotalTaux"); 
                double totalMontant = rs.getDouble("TotalMontants");
                     
               Impot impot = new Impot(ID,month,impots,tauxImpot,cotisation,tauxCotisa,totalTaux,totalMontant);
               impotslist.add(impot);
            }
            if(correct == false){
               JOptionPane.showMessageDialog(null,"Liste vide!", "info", WIDTH); 
            }else{}
	    }catch(SQLException ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              
	    }
        return impotslist;
        
    }

    @Override
    public List<Impot> readImpot(long id) {
        
       
        try {
                
                PreparedStatement search = cn.prepareStatement("SELECT * FROM impot_cotisation WHERE ID =?");
                
                search.setLong(1, id);
                              
                ResultSet rs = search.executeQuery();
                
               if(rs.next()){
                    
                long ID = rs.getLong("ID");
                String month = rs.getString("Mois");
                String impots = rs.getString("Impot");
                double tauxImpot = rs.getDouble("TauxImpot");
                String cotisation = rs.getString("Cotisation");
                double tauxCotisa = rs.getDouble("TauxCotisation");
                double totalTaux = rs.getDouble("TotalTaux"); 
                double totalMontant = rs.getDouble("TotalMontants");
                     
               Impot impot = new Impot(ID,month,impots,tauxImpot,cotisation,tauxCotisa,totalTaux,totalMontant);
               impotslist.add(impot);
                    
                }
               else{
                   Impot impot = new Impot(0,"null","",0,"",0,0,0);
               impotslist.add(impot);
               }
	      
	    }catch(SQLException ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              return null;
     
            }
      
        return impotslist;
        
    }

   
    
}
