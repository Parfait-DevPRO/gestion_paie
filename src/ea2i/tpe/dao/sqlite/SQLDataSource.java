/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao.sqlite;

import ea2i.tpe.dao.IDataSource;
import ea2i.tpe.util.DBManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author iNjaka
 */
public class SQLDataSource implements IDataSource{
    private Connection cn;
    @Override
	public Connection connectionDb() {
	
            if (cn == null) {
            
		try {
			//1
			Class.forName("org.sqlite.JDBC");
                        
                        //Affectation de résultat qui obtient dans le fichier .properties
                        String path = DBManager.getFileProperties().getProperty("db.url");
                        String user = DBManager.getFileProperties().getProperty("db.user");
                        String password = DBManager.getFileProperties().getProperty("db.pass");

                        //2
                        //Connexion au base de donné
                        cn = DriverManager.getConnection(path,user,password);
                        
              
		}catch(ClassNotFoundException | SQLException ex) {
	               JOptionPane.showMessageDialog(null, ex.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
		}
                
             }
		
           return cn;
	}
    
}
