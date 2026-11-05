/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao.sqlite;

import ea2i.tpe.dao.IPostDAO;
import ea2i.tpe.model.Post;
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
public class PostDAO implements IPostDAO{
    
     Connection cn = new SQLDataSource().connectionDb();
     List<Post> postList = new ArrayList<>();

    @Override
    public void createPost(Post p) {
         try {
	        PreparedStatement ps = cn.prepareStatement("INSERT INTO poste (Departement,NomPoste) VALUES(?,?)");
                    ps.setString(1, p.getDepartement());
                    ps.setString(2, p.getNamePost());
		   
		    ps.executeUpdate();
	    }catch(Exception ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
	    }
    }

    @Override
    public void UpdatePost(Post p) {
       
         try {
                PreparedStatement del = cn.prepareStatement("DELETE FROM poste WHERE Departement=?");
                    del.setString(1, p.getDepartement());
                    del.executeUpdate();
	        PreparedStatement ps = cn.prepareStatement("INSERT INTO poste (Departement,NomPoste) VALUES(?,?)");
	            
                    ps.setString(1, p.getDepartement());
                    ps.setString(2, p.getNamePost());
                    
		    ps.executeUpdate();
	    }catch(SQLException ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
	    }
        
    }

    @Override
    public void DeletePost(Post p) {
        
        try {
               PreparedStatement del = cn.prepareStatement("DELETE FROM poste WHERE Departement=?");
                    del.setString(1, p.getDepartement());
                    del.executeUpdate();
	       
	    }catch(SQLException ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
	    }
        
    }

    @Override
    public List<Post> ReadPost(String element) {
         
       Post p;
          try {
                
                PreparedStatement search = cn.prepareStatement("SELECT*FROM poste WHERE Departement LIKE ? OR NomPoste LIKE ?");
                
                search.setString(1, "%"+element+"%");
                search.setString(2, "%"+element+"%");
                
                ResultSet rs = search.executeQuery();
                
               if(rs.next()){
                    
                    String departement = rs.getString("Departement");
                    String namePost = rs.getString("NomPoste");
                                     
                    p = new Post(departement,namePost);
                    postList.add(p);
                }
	      else{
                  JOptionPane.showMessageDialog(null, "Aucun élement trouvé!", "Recherche", JOptionPane.ERROR_MESSAGE); 
               }
	    }catch(SQLException ex) {
	      JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
             
	    }
         
       return postList;
        
    }

    @Override
    public List<Post> ListPost() {
        boolean verify = false;
         try {
	        PreparedStatement ps = cn.prepareStatement("SELECT*FROM poste ORDER by NomPoste");
                    ResultSet rs = ps.executeQuery();
                    while(rs.next()){
                        verify = true;
                        String departement = rs.getString("Departement");
                        String NamePost = rs.getString("NomPoste");
                       Post post = new Post(departement,NamePost);
                        postList.add(post);
                    }
                    if(verify == false){
                     
                       JOptionPane.showMessageDialog(null, "Liste vide!", "info", WIDTH); 
                        
                    }else{}
	    }catch(SQLException ex) {
	       JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
               return null;
	    }
        
       return postList;
    }


}
