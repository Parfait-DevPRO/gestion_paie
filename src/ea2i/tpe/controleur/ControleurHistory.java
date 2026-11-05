/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.controleur;

import ea2i.tpe.calcul.ChangeAndCalcul;
import ea2i.tpe.model.History;
import ea2i.tpe.service.ServiceHistory;
import ea2i.tpe.view.ViewHistory;
import ea2i.tpe.view.ViewSociety;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lenovo
 */
public class ControleurHistory {
    
    //Déclaration de proprietés
    private final ViewHistory Vh;
    private final ChangeAndCalcul change;
    private ViewSociety Vs;
    private final ServiceHistory serviceHistory;
    private final DefaultTableModel histoModel;
    private List<History> histoList;
    
    public ControleurHistory(ViewHistory Vh,ServiceHistory service){
        this.Vh = Vh;
        this.serviceHistory = service;
        //Convertir de JTable dans le vue en DefaultTableModel
        this.histoModel = (DefaultTableModel) Vh.getTableHistory().getModel();
        this.change = new ChangeAndCalcul();
        
        //Ajout l'évenement dans le bouton et le vue
        Vh.getBtnDelete().addActionListener(new DeleteListener());
        Vh.addWindowListener(new CloseWindow());
    }
    
    //Action de supprimer l'historique
    public class DeleteListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            //Suppréssion d'historique dans le base de donné à l'aide de ServiceHistory
            serviceHistory.deleteHistory();
            histoModel.setRowCount(0);
            JOptionPane.showMessageDialog(null, "Historiques supprimés", "Supprimer", WIDTH);
            
        }
        
    }
    
    //Action pour la vue historique
    public class CloseWindow implements WindowListener{
        
        

        @Override
        public void windowOpened(WindowEvent we) {
            //Ajout de l'action dans le tableau historique avec ServiceHistory à l'aide de base de donné 
            histoList = new ArrayList<>();
            histoList = serviceHistory.ListHistory();
            for(History H: histoList){
                histoModel.addRow(new Object[]{H.getAction(),H.getHour()+"h "+H.getMinute()+"min",change.ChangeDateFormat(H.getDate())});
            }
            
        }

        @Override
        public void windowClosing(WindowEvent we) {
            
           //Fermeture de la vue historique
           Vh.setDefaultCloseOperation(ViewHistory.DO_NOTHING_ON_CLOSE);
           Vs = new ViewSociety();
           ControleurSociety con = new ControleurSociety(Vs);
           Vs.setVisible(true);
           Vh.setVisible(false);
        }

        @Override
        public void windowClosed(WindowEvent we) {
           
        }

        @Override
        public void windowIconified(WindowEvent we) {
            
        }

        @Override
        public void windowDeiconified(WindowEvent we) {
            
        }

        @Override
        public void windowActivated(WindowEvent we) {
           
        }

        @Override
        public void windowDeactivated(WindowEvent we) {
            
        }
        
    }
}
