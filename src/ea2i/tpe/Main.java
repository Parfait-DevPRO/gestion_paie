/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe;


import ea2i.tpe.controleur.ControleurSociety;
import ea2i.tpe.view.ViewSociety;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.Icon;




/**
 *
 * @author Parfait
 */
public class Main {
    
    public static void main(String[] args) {
        
        //Instanciation de Menu principal
        ViewSociety Vs = new ViewSociety();
        //Instanciation de Controleur de menu principal
        ControleurSociety con = new ControleurSociety(Vs);
        Vs.setVisible(true);
        Vs.setResizable(false);
       
        
    }

   
}
