/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao.sqlite;

import ea2i.tpe.dao.IComptable;
import ea2i.tpe.model.Comptable;
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
public class ComptableDAO implements IComptable{
    
    //Récuperation de connexion
    Connection cn = new SQLDataSource().connectionDb();
    
    //declaration de tableau
    List<Comptable> listExercice = new ArrayList<>();

    @Override
    public boolean createExercice(Comptable comp) {
        
          try {
                Date StartDate = new Date(comp.getBegin().getTime());
                Date EndDate = new Date(comp.getEnd().getTime());
                
	        PreparedStatement statCreate = cn.prepareStatement("INSERT INTO Exercice(ID,Prenom,Poste,Debut,Fin,Mois,SalaireBase,SalaireBrut,SalaireNet) VALUES(?,?,?,?,?,?,?,?,?)");
	            
                    statCreate.setLong(1, comp.getId());
                    statCreate.setString(2, comp.getFirstName()); 
                    statCreate.setString(3, comp.getPoste());
                    statCreate.setDate(4, StartDate);
		    statCreate.setDate(5, EndDate);
                    statCreate.setString(6, comp.getMonth());
                    statCreate.setDouble(7, comp.getSalaireBase());  
                    statCreate.setDouble(8, comp.getSalaireBrut());
                    statCreate.setDouble(9, comp.getSalaireNet());
                    
		    statCreate.executeUpdate();
                    return true;
                    
	    }catch(SQLException ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
                
               return false;
	    }
        
    }

    @Override
    public List<Comptable> ListExercice() {
        
        boolean correct = false;
         try {
            
            PreparedStatement ps = cn.prepareStatement("SELECT*FROM Exercice ORDER by ID");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                correct = true;
                long ID = rs.getLong("ID");
                String prenom = rs.getString("Prenom");
                String post = rs.getString("Poste");
                Date dateBegin = rs.getDate("Debut");
                Date dateEnd = rs.getDate("Fin");
                String month = rs.getString("Mois");
                double salaryBase = rs.getDouble("SalaireBase"); 
                double salaireBrut = rs.getDouble("SalaireBrut");
                double salaryNet = rs.getDouble("SalaireNet");
                     
               Comptable comp = new Comptable(ID,prenom,post,dateBegin,dateEnd,month,salaryBase,salaireBrut,salaryNet);
               listExercice.add(comp);
            }
            if(correct == false){
               JOptionPane.showMessageDialog(null,"Liste vide!", "info", WIDTH); 
            }else{}
	    }catch(SQLException ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              
	    }
        return listExercice;
        
    }

    @Override
    public List<Comptable> readExercice(long id) {
       
         Comptable comp = null;
         boolean correct = false;
         try {
            
            PreparedStatement ps = cn.prepareStatement("SELECT*FROM Exercice WHERE ID =?");
            
            ps.setLong(1, id);
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                correct = true;
                long ID = rs.getLong("ID");
                String prenom = rs.getString("Prenom");
                String post = rs.getString("Poste");
                Date dateBegin = rs.getDate("Debut");
                Date dateEnd = rs.getDate("Fin");
                String month = rs.getString("Mois");
                double salaryBase = rs.getDouble("SalaireBase"); 
                double salaireBrut = rs.getDouble("SalaireBrut");
                double salaryNet = rs.getDouble("SalaireNet");
                     
                comp = new Comptable(ID,prenom,post,dateBegin,dateEnd,month,salaryBase,salaireBrut,salaryNet);
               listExercice.add(comp);
            }
            else{
               
                comp = new Comptable(0,"","",null,null,"",0,0, 0);
                listExercice.add(comp);
                
            }
	    }catch(SQLException ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              
	    }
        return listExercice;
        
    }

    @Override
    public void deleteExercice(Comptable comp) {
        
         try {
             
               PreparedStatement del = cn.prepareStatement("DELETE FROM Exercice WHERE ID=?");
                    del.setLong(1, comp.getId());
                    del.executeUpdate();                   
	    }catch(Exception ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
            
	    }
        
    }
    
}
