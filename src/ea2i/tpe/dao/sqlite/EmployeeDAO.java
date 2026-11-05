/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao.sqlite;
import ea2i.tpe.dao.*;
import ea2i.tpe.model.Employee;
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
 * @author iNjaka
 */
public class EmployeeDAO implements IEmployeeDAO {
    
    Connection cn = new SQLDataSource().connectionDb();
    List<Employee> employes = new ArrayList<>();
  
    
    @Override
    public boolean createEmployee(Employee e) {
       
        try {
                Date birthday = new Date(e.getBirthday().getTime());
	         PreparedStatement stmt = cn.prepareStatement("INSERT INTO employee(ID,Nom,Prenom,DateNaiss,Poste,Salaire) VALUES(?,?,?,?,?,?)");
	            stmt.setLong(1, e.getId());
                    stmt.setString(2, e.getName());
                    stmt.setString(3, e.getFirstName());
		    stmt.setDate(4, birthday);
		    stmt.setString(5, e.getPoste());
		    stmt.setDouble(6, e.getSalaire());
		    stmt.executeUpdate();
                    return true;
	     } catch (SQLException  ex) {
                 JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
                
                 return false;
             }
       
    }

    

    @Override
    public boolean updateEmployee(Employee e) {
       
         try {
                Date birthday = new Date(e.getBirthday().getTime());
               PreparedStatement del = cn.prepareStatement("DELETE FROM employee WHERE ID=?");
                    del.setLong(1, e.getId());
                    del.executeUpdate();
	        PreparedStatement upstat = cn.prepareStatement("INSERT INTO employee(ID,Nom,Prenom,DateNaiss,Poste,Salaire) VALUES(?,?,?,?,?,?)");
	            upstat.setLong(1, e.getId());
                    upstat.setString(2, e.getName());
                    upstat.setString(3, e.getFirstName());
		    upstat.setDate(4, birthday);
		    upstat.setString(5, e.getPoste());
		    upstat.setDouble(6, e.getSalaire());
                    
		    upstat.executeUpdate();
                    return true;
                    
	    }catch(Exception ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
            
              return false;
	    }
        
    }

    @Override
    public void deleteEmployee(Employee e) {
       
          try {
                PreparedStatement del = cn.prepareStatement("DELETE FROM employee WHERE ID=?");
                    del.setLong(1, e.getId());
                    del.executeUpdate();
	    }catch(Exception ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
             
	    }
        
    }

    @Override
    public Employee readEmployee(String element) {
        Employee e = null;
        boolean verify = false;
          try {
              
                 PreparedStatement search;
              
                 if (element.matches("\\d+(\\.\\d+)?")) {// Test si c'est un nombre
        
                     search = cn.prepareStatement("SELECT * FROM employee WHERE ID = ? OR Salaire = ?");
                     search.setLong(1, Long.parseLong(element));
                     search.setDouble(2, Double.parseDouble(element));
                     
                     
                }else{
              
                 search = cn.prepareStatement("SELECT*FROM employee WHERE Nom LIKE ? OR Prenom LIKE ? OR DateNaiss LIKE ? OR Poste LIKE ?");
                
                search.setString(1, "%"+element+"%");
                search.setString(2, "%"+element+"%");
                search.setString(3, "%"+element+"%");
                search.setString(4, "%"+element+"%");
                
                 }
                
                
                ResultSet rs = search.executeQuery();
                
               while(rs.next()){
                   
                    verify = true;
                    
                    long ID = rs.getLong("ID");
                    String name = rs.getString("Nom");
                    String firstName = rs.getString("Prenom");
                    Date date = rs.getDate("DateNaiss");
                    String post= rs.getString("Poste");
                    double salary = rs.getDouble("Salaire");
                    
                    e = new Employee(ID,name,firstName,date,post,salary);
                    
                }
	      if(!verify){
                  JOptionPane.showMessageDialog(null, "Aucun élement trouvé!", "Recherche", WIDTH); 
                  return null;
               }
	    }catch(SQLException ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              return null;
	    }
       
         
       return e;
    }

    @Override
    public List<Employee> ListEmployee() {
        
        Connection con = new SQLDataSource().connectionDb();
        
        boolean verify = false;
        try {
            
            PreparedStatement statList = con.prepareStatement("SELECT*FROM employee ORDER by ID");
            ResultSet rs = statList.executeQuery();
            while(rs.next()){
                
                verify = true;
                long ID = rs.getLong("ID");
                String name = rs.getString("Nom");
                String firstName = rs.getString("Prenom");
                Date date = rs.getDate("DateNaiss");
                String post= rs.getString("Poste");
                double salary = rs.getDouble("Salaire"); 
              
               Employee e = new Employee(ID,name,firstName,date,post,salary);
               employes.add(e);
            }
            if(verify == false){
              
               JOptionPane.showMessageDialog(null,"Liste vide!", "info", WIDTH); 
                
            }else{}
            
	    }catch(SQLException ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              
	    }
        
        
        return employes;
    }

    @Override
    public List<Employee> getEmployee(long id) {
       
        
          try {
                
                PreparedStatement search = cn.prepareStatement("SELECT * FROM employee WHERE ID =?");
                
                search.setLong(1, id);
                              
                ResultSet rs = search.executeQuery();
                
               if(rs.next()){
                 
                long ID = rs.getLong("ID");
                String name = rs.getString("Nom");
                String firstName = rs.getString("Prenom");
                Date date = rs.getDate("DateNaiss");
                String post= rs.getString("Poste");
                double salary = rs.getDouble("Salaire"); 
                  
               Employee e = new Employee(ID,name,firstName,date,post,salary);
               employes.add(e);
                
                }
               else{
                  
                   Employee emp = new Employee(0,"","",null,"",0);
                   employes.add(emp);
                   
               }
              
	     
	    }catch(SQLException ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
              return null;
	    }
      
        return employes;
       
    }


  }

  

