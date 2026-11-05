/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.controleur;

import ea2i.tpe.calcul.ChangeAndCalcul;
import ea2i.tpe.dao.sqlite.EmployeeDAO;
import ea2i.tpe.dao.sqlite.HistoryDAO;
import ea2i.tpe.model.Comptable;
import ea2i.tpe.model.Contrat;
import ea2i.tpe.model.Employee;
import ea2i.tpe.model.History;
import ea2i.tpe.model.Impot;
import ea2i.tpe.model.Prime;
import ea2i.tpe.service.ServiceComptable;
import ea2i.tpe.service.ServiceEmployee;
import ea2i.tpe.service.ServiceHistory;
import ea2i.tpe.view.ViewComptable;
import ea2i.tpe.view.ViewSociety;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lenovo
 */
public class ControleurComptable {
    
    //Déclaration de proprieté
    private final ViewComptable Vcomp;
    private ViewSociety Vs;
    private final ServiceComptable serviceComp;
    private List<Comptable> listcompt;
    private final ChangeAndCalcul change;
    private final DefaultTableModel TabModel; 
    
    public ControleurComptable(ViewComptable Vcomp,ServiceComptable serviceComp){
        
        this.Vcomp = Vcomp;
        this.serviceComp = serviceComp;
        this.change = new ChangeAndCalcul();
        //convertir de JTable en DefaultTableModel
        this.TabModel = (DefaultTableModel) Vcomp.getTableComptable().getModel();
        
        //Ajout des évenements dans le bouton et le view
        Vcomp.addWindowListener(new WindowOpenListener());
        Vcomp.getBtnStatus().addActionListener(new StatusListener());
        Vcomp.getBtnDeleteComptable().addActionListener(new DeleteExercice());
        Vcomp.getBtnResultComptable().addActionListener(new AddExerciceListener());
       
        
    }
    
    //Effacer le contenu d'inscription(Dans le vue)
    public void Cancel(){
        Vcomp.getTxtBeginDateComptable().setDate(null);
        Vcomp.getTxtEndDateComptable().setDate(null);
    }
    
    /*
       Tous les évenements ont de classe interne pour faire l'action
    */
    public class DeleteExercice implements ActionListener{
        
        
        long id;

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            //Récuperation de nombre du selectionné dans le tableau et verifier si 0
            if(Vcomp.getTableComptable().getSelectedRowCount() == 0){
                
               JOptionPane.showMessageDialog(null, "Sélectionnez l'employé que vous supprimez!", "ERREUR", JOptionPane.ERROR_MESSAGE);
                
            }
            else{
              
              //Récuperation d'élement selectionné puis convertir à l'aide de class ChangeAndCalcul
              id = change.ChangeLong(TabModel.getValueAt(Vcomp.getTableComptable().getSelectedRow(), 0).toString());
              String firstName = TabModel.getValueAt(Vcomp.getTableComptable().getSelectedRow(), 1).toString();
              String post = TabModel.getValueAt(Vcomp.getTableComptable().getSelectedRow(), 2).toString();
              Date begin = change.ChangeDateString(TabModel.getValueAt(Vcomp.getTableComptable().getSelectedRow(), 3).toString());
              Date end = change.ChangeDateString(TabModel.getValueAt(Vcomp.getTableComptable().getSelectedRow(), 4).toString());
              String month = TabModel.getValueAt(Vcomp.getTableComptable().getSelectedRow(), 5).toString();
              double salaireBase = change.ChangeDouble(TabModel.getValueAt(Vcomp.getTableComptable().getSelectedRow(), 6).toString());
              double salaireBrut = change.ChangeDouble(TabModel.getValueAt(Vcomp.getTableComptable().getSelectedRow(), 7).toString()); 
              double salaireNet = change.ChangeDouble(TabModel.getValueAt(Vcomp.getTableComptable().getSelectedRow(), 8).toString());
              
              //création d'objet Comptable
              Comptable comp = new Comptable(id, firstName, post, begin, end, month, salaireBase, salaireBrut, salaireNet);
              
              //Appel du méthode qui supprimer dans la class ServiceComptable
              serviceComp.DeleteExercice(comp);
              JOptionPane.showMessageDialog(null, "supprimé!", "Suppréssion", WIDTH);
              TabModel.removeRow(Vcomp.getTableComptable().getSelectedRow());
              
              //Ajout dans l'historique cet action
               HistoryDAO dao = new HistoryDAO();
               ServiceHistory service = new ServiceHistory(dao);
                
               History h = new History("Suppréssion de "+comp.getFirstName(),change.NowHour(),change.NowMinute(),change.NowDate());
               service.addHistory(h);
              
            }
        }
        
    }
    
   
    
    //Action qui affiche la liste
    public class ListExercice implements ActionListener{
        
        ChangeAndCalcul change = new ChangeAndCalcul();

        @Override
        public void actionPerformed(ActionEvent ae) {
            listcompt = new ArrayList<>();
            /*
             Appel de la méthode dans la ServiceComptable
             Affectation de résultat dans le tableau listcompt
            */
            listcompt = serviceComp.listExercice();
            
            //Affichage sur le tableau
            for(Comptable comp: listcompt){
                
                TabModel.addRow(new Object[]{comp.getId(),comp.getFirstName(),comp.getPoste(),change.ChangeDateFormat(comp.getBegin()),change.ChangeDateFormat(comp.getEnd()),comp.getMonth(),comp.getSalaireBase(),comp.getSalaireBrut(),comp.getSalaireNet()});
                
            }
        }
        
    }
    
    //Action pour ajouter
    public class AddExerciceListener implements ActionListener{
      
        //initialisation de date
        Date begin = null,end = null;
        
        //boolan qui verifier
        boolean verify = true;
        
        //initialisation des variables double
        double salaireBase = 0,totalMontantImpot = 0,salaireBrut = 0,salaireNet = 0;
        
        @Override
        public void actionPerformed(ActionEvent ae) {
            
           //Convertir de l'ID en long dans la class ChangeAndCalcul
           long id = change.ChangeLong(Vcomp.getTxtIDComptable().getSelectedItem().toString());
           
           //Verification de date si null
           if(Vcomp.getTxtBeginDateComptable().getDate() != null || Vcomp.getTxtEndDateComptable().getDate() != null){
            
                begin = Vcomp.getTxtBeginDateComptable().getDate();
                end = Vcomp.getTxtEndDateComptable().getDate();
                verify = true;
               
           }
           else{
               
               verify = false;
               
           }
           
           //Recherche d'element d'un employé dans le Contrat à l'aide d'ID
           Contrat cont = change.SearchInContrat(id);
           
           //Recherche d'element d'un employé dans l'Employee à l'aide d'ID
           Employee e = change.SearchInEmployee(id);
           
           //Affectation de résultat
           String firstName = e.getFirstName();
           String post = e.getPoste();
           
           salaireBase = e.getSalaire();
           
           //Recherche d'element d'un employé dans l'Impot à l'aide d'ID
           Impot imp = change.SearchInImpot(id);
           String month = imp.getMonth();
           totalMontantImpot = imp.getTotalMontants();
           
           //Recherche d'element d'un employé dans le Prime à l'aide d'ID
           Prime prim = change.SearchInPrime(id);
           
           //Calcul salaireBrut et salaireNet à l'aide de class ChangeAndCalcul
           salaireBrut = change.CalculSalaireBrut(salaireBase, prim.getMontant());
           salaireNet = change.CalculSalaireNet(salaireBrut, totalMontantImpot);
          
        //Verifier si date n'est pas null
        if(verify){
           
            //Verifier si le contrat de l'employé est actif et verifier s'il fait l'impôt
          if(cont.isStatusContrat() && (month != null && totalMontantImpot != 0)){
           
            Comptable compt = new Comptable(id, firstName, post, begin, end, month, salaireBase, salaireBrut, salaireNet);
            
                //Ajout de contrat à l'aide de méthode de ServiceComptable(envoie au base de donné)
                boolean add = serviceComp.AddExercice(compt);
                
                //effacer le contenu de tableau
                TabModel.setRowCount(0);
                
                //verification s'il y a d'erreur
                if(add){
                   
                    //Affichage sur tableau
                    TabModel.addRow(new Object[]{compt.getId(),firstName,post,change.ChangeDateFormat(begin),change.ChangeDateFormat(end),month,salaireBase,salaireBrut,salaireNet});
                     JOptionPane.showMessageDialog(null, "succes!", "Résultat", WIDTH);
               
                     Cancel();
                     
                       //Ajout au historique cet action
                       HistoryDAO dao = new HistoryDAO();
                       ServiceHistory service = new ServiceHistory(dao);
                
                       History h = new History("Consultation des salaires du "+firstName+" dans le comptable",change.NowHour(),change.NowMinute(),change.NowDate());
                       service.addHistory(h);
                }
                else{  // Messages d'erreurs//////
                 
                    JOptionPane.showMessageDialog(null, "Votre ID est déja à la liste", "ERREUR", JOptionPane.ERROR_MESSAGE);
                    
                }
            }
            else{
                    String erreur ="Si votre status sur contrat est inactif et vous ne faites pas d'Impôt, vous ne pouvez\n pas regarder votre salaire net à payé!";
                    JOptionPane.showMessageDialog(null, erreur+", Réssayez!", "ERREUR", JOptionPane.ERROR_MESSAGE);
                    
                }
        }else{
             JOptionPane.showMessageDialog(null, "Completez et réssayez!", "ERREUR", JOptionPane.ERROR_MESSAGE);
        }
          
           
         
            
        }
        
    }
    
    
    //Action de bouton status
    public class StatusListener implements ActionListener{

        
        @Override
        public void actionPerformed(ActionEvent ae) {
           
           //Recherche dans le Contrat à l'aide d'ID(changement en type long) sur ChangeAndCalcul
           Contrat cont = change.SearchInContrat(change.ChangeLong(Vcomp.getTxtIDComptable().getSelectedItem().toString()));
           
           //Ajout de texte(Actif ou Inactif) sur le label Status à l'aide de ChangeAndCalcul
           Vcomp.getTxtStatusContrat().setText(change.TextStatus(cont.isStatusContrat()));
         
           //Recherche dans le Prime à l'aide d'ID(changement en type long) sur ChangeAndCalcul
           Prime prm = change.SearchInPrime(change.ChangeLong(Vcomp.getTxtIDComptable().getSelectedItem().toString()));
           
           //Ajout de texte(Actif ou Inactif) sur le label Status à l'aide de ChangeAndCalcul
           Vcomp.getTxtStatusPrime().setText(change.TextStatus(prm.isStatus()));
            
        }
        
    }
    
    
    
    public class WindowOpenListener implements WindowListener{
      
        
        @Override
        public void windowOpened(WindowEvent we) {
            
            //Insertion de l'ID des employés quand la fenetre Contrat ouvre
            EmployeeDAO eDAO= new EmployeeDAO();
            ServiceEmployee Se = new ServiceEmployee(eDAO);
            List<Employee> list = new ArrayList<>();
            list = Se.getListEmployees();
            Vcomp.getTxtIDComptable().setMaximumRowCount(list.size());
            
            for(Employee e:list){
               Vcomp.getTxtIDComptable().addItem(e.getId());
          
            }
            
            
             listcompt = new ArrayList<>();
            /*
             Appel de la méthode dans la ServiceComptable
             Affectation de résultat dans le tableau listcompt
            */
            listcompt = serviceComp.listExercice();
            
            //Affichage sur le tableau
            for(Comptable comp: listcompt){
                
                TabModel.addRow(new Object[]{comp.getId(),comp.getFirstName(),comp.getPoste(),change.ChangeDateFormat(comp.getBegin()),change.ChangeDateFormat(comp.getEnd()),comp.getMonth(),comp.getSalaireBase(),comp.getSalaireBrut(),comp.getSalaireNet()});
                
            }
        
        }

        @Override
        public void windowClosing(WindowEvent we) {
            //Action pour la fermeture de Fenetre Contrat et revenir au menu principal
             Vcomp.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            Vs = new ViewSociety();
            ControleurSociety con = new ControleurSociety(Vs);
            Vs.setVisible(true);
            Vcomp.dispose();
            
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
