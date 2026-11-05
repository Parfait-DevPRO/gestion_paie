/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author lenovo
 */
public class DBManager {

 //déclaration de proprrieté static
 private static Properties props;
 
   //méthode static qui récupere et lire le fichier .properties
   public static Properties getFileProperties(){
       
        //création d'objet props
         props = new Properties();
        
        //Recherche et lecture de fichier database.properties dans le dossier src
        try (InputStream input = DBManager.class.getClassLoader().getResourceAsStream("database.properties")) {
            
            //verification si le fichier est vide
            if (input == null) {
                System.err.println("database.properties introuvable !");
            } else {
                
                //lecture de fichier
                props.load(input);
               
            }
            return props;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
   
}
