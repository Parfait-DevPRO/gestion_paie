/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.controleur;


import ea2i.tpe.dao.sqlite.EmployeeDAO;
import ea2i.tpe.model.Contrat;
import ea2i.tpe.model.Employee;
import ea2i.tpe.model.Impot;
import ea2i.tpe.model.Prime;
import ea2i.tpe.service.ServiceEmployee;
import ea2i.tpe.calcul.ChangeAndCalcul;
import ea2i.tpe.dao.sqlite.HistoryDAO;
import ea2i.tpe.model.Comptable;
import ea2i.tpe.model.History;
import ea2i.tpe.service.ServiceHistory;
import ea2i.tpe.view.FullList;
import ea2i.tpe.view.ViewSociety;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author lenovo
 */
public class ControleurList {
    
    private ViewSociety Vs;
    private final FullList listView;
    private List<Employee> listEmployee;
    private final DefaultTableModel TabModel;
    
  
    
    public ControleurList(FullList view){
        
        //initialisation de variable d'instance
        this.listView = view;
        this.TabModel = (DefaultTableModel) listView.getFullTableEmployee().getModel();
        
        //Action pour la fenetre 
        listView.addWindowListener(new OpenListListener());
        
        
    }
    
    //Fenetre liste complet ouvrir
    public class OpenListListener implements WindowListener{

        ChangeAndCalcul change = new ChangeAndCalcul();
        EmployeeDAO eDAO = new EmployeeDAO();
        ServiceEmployee se = new ServiceEmployee(eDAO);
           
        
        @Override
        public void windowOpened(WindowEvent we) {
          
            
           listEmployee = new ArrayList<>();
           
           listEmployee = se.getListEmployees();
           
          for(int i=0;i<listEmployee.size();i++){
              
              //Récuperation des élements de Contrat, impot, Prime et Comptable dans la base de donné à l'aide Id d'employé
              Contrat  cont = change.SearchInContrat(listEmployee.get(i).getId());
              Impot imp = change.SearchInImpot(listEmployee.get(i).getId());
              Prime p = change.SearchInPrime(listEmployee.get(i).getId());
              Comptable ct =  change.SearchInComptable(listEmployee.get(i).getId());
              
                  //Affichage dans le tableau
                 TabModel.addRow(new Object[]{listEmployee.get(i).getId(),listEmployee.get(i).getFirstName(),listEmployee.get(i).getPoste(),change.TextStatus(cont.isStatusContrat()),change.TextStatus(p.isStatus()),change.TwoNumberAfterComma(listEmployee.get(i).getSalaire())+"Ar",imp.getMonth(),change.TwoNumberAfterComma(imp.getTauxImpot())+"%",change.TwoNumberAfterComma(imp.getTauxCotisations())+"%",change.TwoNumberAfterComma(ct.getSalaireBrut())+"Ar",change.TwoNumberAfterComma(ct.getSalaireNet())+"Ar"});
              
              }
          
              //Ajout au historique
              HistoryDAO dao = new HistoryDAO();
              ServiceHistory service = new ServiceHistory(dao);
                
              History h = new History("Ouverture de liste complet",change.NowHour(),change.NowMinute(),change.NowDate());
              service.addHistory(h);
            
            
        }
        
       
      

        @Override
        public void windowClosing(WindowEvent we) {
           
            //Fermeture de la fenetre liste complet
            listView.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            Vs = new ViewSociety();
            ControleurSociety con = new ControleurSociety(Vs);
            Vs.setVisible(true);
            listView.dispose();
            
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
