/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.controleur;

import ea2i.tpe.dao.sqlite.EmployeeDAO;
import ea2i.tpe.model.Contrat;
import ea2i.tpe.model.Employee;
import ea2i.tpe.service.ServiceContrat;
import ea2i.tpe.service.ServiceEmployee;
import ea2i.tpe.calcul.ChangeAndCalcul;
import ea2i.tpe.dao.sqlite.HistoryDAO;
import ea2i.tpe.model.History;
import ea2i.tpe.service.ServiceHistory;
import ea2i.tpe.view.ViewContrat;
import ea2i.tpe.view.ViewHistory;
import ea2i.tpe.view.ViewSociety;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lenovo
 */
public class ControleurContrat {
    
    private final ViewContrat Vc;
    private final ChangeAndCalcul change;
    private ViewSociety Vs;
    private ViewHistory Vh;
    private Contrat contrat;
    private final ServiceContrat serviceContrat;
    private final DefaultTableModel TabModel;
    private List<Contrat> listContrat;

    public ControleurContrat(ViewContrat Vc, ServiceContrat serviceContrat) {
        
        this.Vc = Vc;
        this.change = new ChangeAndCalcul();
        this.Vh = new ViewHistory();
        this.serviceContrat = serviceContrat;
        this.TabModel = (DefaultTableModel) Vc.getTableContrat().getModel();
        
        //Ajout d'évenement au bouton,tableau et la fenetre de Contrat
        Vc.addWindowListener(new WindowContratListener());
        Vc.getTableContrat().addMouseListener(new TableContratClickListener());
        Vc.getBtnAddContrat().addActionListener(new AddContratListener());
        Vc.getBtnContratCancel().addActionListener(new CancelListener());
        Vc.getBtnUpdateContrat().addActionListener(new UpdateContratListener());
        Vc.getBtnListContrat().addActionListener(new ListContratListener());
        Vc.getBtnContratSearch().addActionListener(new SearchContratListener());
        Vc.getBtnDeleteContrat().addActionListener(new DeleteContratListener());
        
        
    }
 
  //Vider le champ d'inscription sur le Contrat
 public void Cancel(){
     
     Vc.getTxtTypeContrat().setText("");
     Vc.getDateBeginContrat().setDate(null);
     Vc.getDateEndContrat().setDate(null);
     Vc.getSalaryContrat().setText("");
     
 }
   
 public class CancelListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            Cancel();
        }
     
 }
 
 public class SearchContratListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            String element = Vc.getTxtElementsSearch().getText();
            if(!element.isEmpty()){//Verifie si l'élement est vide
                
                listContrat = new ArrayList<>();
                
                //Affectation de résultat dans le base de donné avec le tableau listContrat
                listContrat = serviceContrat.SearchContrat(element);
                
                //vider le tableau avant d'afficher
                TabModel.setRowCount(0);
                
                //Affichage sur le tableau
                for(Contrat cont:listContrat){
                    TabModel.addRow(new Object[]{cont.getId(),cont.getContratType(),change.ChangeDateFormat(cont.getBeginDate()),change.ChangeDateFormat(cont.getEndDate()),cont.getSalary(),change.TextStatus(cont.isStatusContrat())});
                }
                
                //Ajout dans l'historique
                HistoryDAO dao = new HistoryDAO();
                ServiceHistory service = new ServiceHistory(dao);
              
                History h = new History("Recherche dans contrat: "+element+" dans le champ Contrat",change.NowHour(),change.NowMinute(),change.NowDate());
                service.addHistory(h);
                
                
            }
            else{
               JOptionPane.showMessageDialog(null, "Insérez l'élement pour rechercher!", "Erreur", JOptionPane.ERROR_MESSAGE); 
            }
            
        }
     
 }
 
 public class DeleteContratListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
        if(Vc.getTableContrat().getSelectedRowCount() == 1){ //Verifie si l'utilisateur selectionne le contrat qu'il veut modifier
          
          String ID = Vc.getIDEmployeeContrat().getSelectedItem().toString();
          String Type = Vc.getTxtTypeContrat().getText();
          Date StartDate = Vc.getDateBeginContrat().getDate();
          Date EndDate = Vc.getDateEndContrat().getDate();
          String Salary = Vc.getSalaryContrat().getText();
         
                //Appel du méthode ChangeLong dans la class ChangeAndCalcul pour convertir en long
                long id= change.ChangeLong(ID);
                 
                //Verifie si la date début et date fin est actif ou innactif
                 boolean status = change.isActif(StartDate, EndDate);
                 
                 //Création d'objet contrat
                 contrat = new Contrat(id,Type,StartDate,EndDate,Double.parseDouble(Salary),status);
                 
                 //Suppréssion de contrat dans la base de donné à l'aide de ServiceContrat
                 serviceContrat.DeleteContrat(contrat);
                 JOptionPane.showMessageDialog(null, "Contrat supprimé", "Suppréssion", WIDTH);
                 TabModel.removeRow(Vc.getTableContrat().getSelectedRow());
                 Cancel();
                 
                 /*Ajout dans l'historique */
                 HistoryDAO dao = new HistoryDAO();
                 ServiceHistory service = new ServiceHistory(dao);
                
                 Date date = new Date();
                 Calendar cal = Calendar.getInstance();
                 cal.setTime(date);
                 
                 int hour = cal.get(Calendar.HOUR_OF_DAY); 
                
                 History h = new History("Suppréssion de contrat ID:"+contrat.getId()+" dans le champ Contrat",change.NowHour(),change.NowMinute(),change.NowDate());
                 service.addHistory(h);
                
          
       }else{
         JOptionPane.showMessageDialog(null, "Selectionnez le contrat pour supprimer!", "Erreur", JOptionPane.ERROR_MESSAGE);  
       }
            
    }
     
 }
 
 
 public class ListContratListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
           listContrat = new ArrayList<>();
           
           //Affectation de résultat dans le base de donné au tableau listContrat
           listContrat = serviceContrat.ListContrat();
           
           //Vider le tableau avant d'afficher
           TabModel.setRowCount(0);
           
           //Affichage dans le tableau
           for(Contrat cont:listContrat){
               TabModel.addRow(new Object[]{cont.getId(),cont.getContratType(),change.ChangeDateFormat(cont.getBeginDate()),change.ChangeDateFormat(cont.getEndDate()),cont.getSalary(),change.TextStatus(cont.isStatusContrat())});
           }
           
           //Ajout au historique
            HistoryDAO dao = new HistoryDAO();
            ServiceHistory service = new ServiceHistory(dao); 
                
            History h = new History("Ouverture Liste contrat",change.NowHour(),change.NowMinute(),change.NowDate());
            service.addHistory(h);
           
        }
     
 }
 
 public class AddContratListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            //Initialisation des variable globale
             boolean verify;
             boolean status = false;
             long id=0;
             double salary=0;
             Date StartDate = null,EndDate = null;
             String ContratType = null;
             
             //Verifie s'il y a de vide
             if(Vc.getTxtTypeContrat().getText().isEmpty()||Vc.getSalaryContrat().getText().isEmpty()){
                 
                 JOptionPane.showMessageDialog(null, "Completez le vide et Ressayez", "Erreur", JOptionPane.ERROR_MESSAGE);
                 verify = false;
                 
             }else{
                   
                   //Affectation des élements de contrat à l'aide de Fenêtre contrat
                   ContratType = Vc.getTxtTypeContrat().getText();
                   StartDate = Vc.getDateBeginContrat().getDate();
                   EndDate = Vc.getDateEndContrat().getDate();
                   
                   if(EndDate != null){
                
                      EndDate = Vc.getDateEndContrat().getDate(); 
                    
                   
                   }
                   else{
                     //Si l'employé n'a pas de date fin, la date prend la date maximale de calendrier
                     EndDate = Vc.getDateEndContrat().getMaxSelectableDate();
                      
                   }
                   
                   //Verifie si la date debut et date fin d'un employé est actif ou innactif
                   status = change.isActif(StartDate, EndDate);
                   
                  //Verifie si la changement de long et double a d'erreur (fonction retourne -1 s'il y a d'erreur)
                  if(change.ChangeLong(Vc.getIDEmployeeContrat().getSelectedItem().toString()) == -1 ||change.ChangeDouble(Vc.getSalaryContrat().getText())==-1){
                       
                      verify = false;
                       
                  }else{
                     
                      //Affectation si la condtion est vraie
                     id = change.ChangeLong(Vc.getIDEmployeeContrat().getSelectedItem().toString());
                     salary = change.ChangeDouble(Vc.getSalaryContrat().getText());
                     verify = true;
                     
                   } 
             }
             
             /*Appel de la méthode SearchInEmployee dans le class ChangeAndCalcul
              *Recherche à l'aide d'ID d'employé qui ajout de contrat pour avoir son salaire base */
             Employee e = change.SearchInEmployee(id);
            
            if(verify){ // Verifie s'il n'y a pas d'erreur dans le changement(long,double)
                
               //Verifie si la salaire qui ajout au contrat est égal à la salaire dans le champ Employes
               if(salary == e.getSalaire()){
                   
                   //Création d'objet contrat
                   contrat = new Contrat(id, ContratType, StartDate,EndDate, salary,status);
                   
                  //Ajout de contrat dans la base de donné
                  boolean add = serviceContrat.AddContrat(contrat);
                   
                  //Convertir le boolean status en texte
                   String txtStatus = change.TextStatus(status);
                   
                   //Vider le tableau avant d'afficher
                   TabModel.setRowCount(0);
                   
                   //Verifie si le contrat est ajouté à la base de donné ou il y a d'erreur (primary key)
                  if(add){ 
                   
                      //Affichage de tableau
                      TabModel.addRow(new Object[]{id,ContratType,change.ChangeDateFormat(StartDate),change.ChangeDateFormat(EndDate),salary,txtStatus});  
                      JOptionPane.showMessageDialog(null, "Enregistré", "Nouveau Contrat", WIDTH);
                      Cancel();
                   
                      //Ajout dans l'historique
                      HistoryDAO dao = new HistoryDAO();
                      ServiceHistory service = new ServiceHistory(dao);
                
                      History h = new History("Ajout contrat de ID:"+contrat.getId()+" dans le champ Nouveau Contrat",change.NowHour(),change.NowMinute(),change.NowDate());
                      service.addHistory(h);
                   
                 }else{
                      
                    JOptionPane.showMessageDialog(null, "Votre ID est deja ajouter sur la liste, Ressayez!", "Erreur", JOptionPane.ERROR_MESSAGE);  
                 }
                   
               }
               else{
                 
                  JOptionPane.showMessageDialog(null, "Votre salaire base doit être égale à la salaire dans l'employé!", "Erreur", JOptionPane.ERROR_MESSAGE);   
                   
               }
               
               
           }else{}
            
        }
     
 }
  
 public class UpdateContratListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            boolean verify = true;  

               //Verifie si l'utilisateur selectionne le contrat qu'il veut modifier
               if(Vc.getTableContrat().getSelectedRowCount() == 1){
               
                    long id = 0;
                    double salary = 0;
                    
                    //Verifie si la changement de long et double a d'erreur (fonction retourne -1 s'il y a d'erreur)
                    if(change.ChangeLong(Vc.getIDEmployeeContrat().getSelectedItem().toString())== -1 || change.ChangeDouble(Vc.getSalaryContrat().getText()) == -1){
                        verify = false;
                    }
                    else{
                      
                        //Affectation s'il n'y a pas d'erreur
                        id= change.ChangeLong(Vc.getIDEmployeeContrat().getSelectedItem().toString());
                        salary = change.ChangeDouble(Vc.getSalaryContrat().getText()); 
                        
                    }
                    
                       //Si vrai les changements ci-dessus
                       if(verify){
                           
                           Date StartDate = Vc.getDateBeginContrat().getDate();
                           Date EndDate = Vc.getDateEndContrat().getDate();
                           
                           //Verifie si la date fin est vide
                           if(EndDate != null){
                
                                EndDate = Vc.getDateEndContrat().getDate(); 
                    
                   
                           }
                           else{
                                
                               //Si l'employé n'a pas de date fin, la date prend la date maximale de calendrier
                                EndDate = Vc.getDateEndContrat().getMaxSelectableDate();
                      
                           }
                           
                           //Verifie si la date debut et fin d'un employé est actif ou innactif
                           boolean status = change.isActif(StartDate, EndDate);
                           
                           //Création d'objet cont
                           Contrat cont = new Contrat(id,Vc.getTxtTypeContrat().getText(),StartDate,EndDate,salary,status);
                           
                           //Modifier dans la base de donné à l'aide de méthode Update dans le SrviceContrat
                           boolean up = serviceContrat.UpdateContrat(cont);
                           
                           //Vider le tableau avant d'afficher
                           TabModel.setRowCount(0);
                           
                           //Verifie s'il y a d'erreur sur la base de donné
                           if(up){
                               
                               //Récuperation de salaire dans l'employé
                               Employee e = change.SearchInEmployee(id);
                               
                               //Verifie si la salaire qui modifie est égale à la salaire dans l'employé
                               if(salary == e.getSalaire()){
                              
                                   //Affichage dans le tableau
                                  TabModel.addRow(new Object[]{cont.getId(),cont.getContratType(),change.ChangeDateFormat(StartDate),change.ChangeDateFormat(EndDate),cont.getSalary(),change.TextStatus(status)});
                                  JOptionPane.showMessageDialog(null, "Contrat modifié", "Modification", WIDTH);
                                  Cancel();
                               
                                  //Ajout dans l'historique
                                  HistoryDAO dao = new HistoryDAO();
                                  ServiceHistory service = new ServiceHistory(dao);
                
                                  History h = new History("Modification de contrat ID:"+contrat.getId()+" dans le champ Contrat",change.NowHour(),change.NowMinute(),change.NowDate());
                                  service.addHistory(h);
                               
                               }
                              else{
                 
                                JOptionPane.showMessageDialog(null, "Votre salaire base doit être égale à la salaire dans l'employé!", "Erreur", JOptionPane.ERROR_MESSAGE);   
                   
                             }
                               
                           }
                            
                           else{
                               JOptionPane.showMessageDialog(null, "Votre ID est deja sur la liste, Ressayez!", "Erreur", JOptionPane.ERROR_MESSAGE); 
                           }
                           
                       
                   }else{}
                }
                else{
                  JOptionPane.showMessageDialog(null, "Selectionnez le contrat que vous modifiez!", "Erreur", JOptionPane.ERROR_MESSAGE);   
               }
           
            
        }
     
 }
 
  public class WindowContratListener implements WindowListener{

        @Override
        public void windowOpened(WindowEvent we) {
          
            /* Récuperation de l'ID d'employé dans la base de donné et
               Affichage sur le champ ID dans le contrat quand la fenêtre apparaît(ouvre)
            */
            EmployeeDAO eDAO= new EmployeeDAO();
            ServiceEmployee Se = new ServiceEmployee(eDAO);
            List<Employee> list = new ArrayList<>();
            list = Se.getListEmployees();
            Vc.getIDEmployeeContrat().setMaximumRowCount(list.size());
            for(Employee e:list){
               Vc.getIDEmployeeContrat().addItem(e.getId());
            }      
            
        }

        @Override
        public void windowClosing(WindowEvent we) {
         
            //Fermeture de la fenêtre Contrat pour revenir au ménu principal
             Vc.setDefaultCloseOperation(WIDTH);
             Vs = new ViewSociety();
             ControleurSociety con = new ControleurSociety(Vs);
             Vs.setVisible(true);
             Vc.setVisible(false);
            
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
  
  public class TableContratClickListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent me) {
          
            //Récuperation de chaque element de colonne dans le tableau de contrat
            long id = change.ChangeLong(TabModel.getValueAt(Vc.getTableContrat().getSelectedRow(), 0).toString());
            String ContratType = TabModel.getValueAt(Vc.getTableContrat().getSelectedRow(), 1).toString();
            
                String StartDate = TabModel.getValueAt(Vc.getTableContrat().getSelectedRow(), 2).toString();
                String EndDate = TabModel.getValueAt(Vc.getTableContrat().getSelectedRow(), 3).toString();
            
            String Salary = TabModel.getValueAt(Vc.getTableContrat().getSelectedRow(), 4).toString();
            
            //Affichage sur le champ d'inscription dans le Contrat
            Vc.getIDEmployeeContrat().setSelectedItem(id);
            
            //Affiche que le champ ID ne peut pas modifier
            Vc.getIDEmployeeContrat().setEnabled(false);
            Vc.getTxtTypeContrat().setText(ContratType);
            Vc.getDateBeginContrat().setDate(change.ChangeDateString(StartDate));
            Vc.getDateEndContrat().setDate(change.ChangeDateString(EndDate));
            Vc.getSalaryContrat().setText(Salary);
            
        }

        @Override
        public void mousePressed(MouseEvent me) {
           
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            
        }

        @Override
        public void mouseExited(MouseEvent me) {
            
        }
      
  }
}
