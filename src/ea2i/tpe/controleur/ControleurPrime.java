/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.controleur;

import ea2i.tpe.dao.sqlite.EmployeeDAO;
import ea2i.tpe.model.Employee;
import ea2i.tpe.model.Prime;
import ea2i.tpe.service.ServiceEmployee;
import ea2i.tpe.service.ServicePrime;
import ea2i.tpe.calcul.ChangeAndCalcul;
import ea2i.tpe.dao.sqlite.HistoryDAO;
import ea2i.tpe.model.History;
import ea2i.tpe.service.ServiceHistory;
import ea2i.tpe.view.ViewPrime;
import ea2i.tpe.view.ViewSociety;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public class ControleurPrime {
    
    private final ViewPrime Vprime;
    private ViewSociety Vs;
    private List<Prime> primelist;
    private final DefaultTableModel TabModel;
    private final ServicePrime servicePrime;
    
    public ControleurPrime(ViewPrime Vprime,ServicePrime servicePrime){
        
        //initialisation de variable d'instance
        this.Vprime = Vprime;
        this.servicePrime = servicePrime;
        
        //Convertir de JTable en DefaultTableModel
        this.TabModel = (DefaultTableModel) Vprime.getTablePrime().getModel();
        
        //Ajout des evenements à l'aide de class interne
        Vprime.addWindowListener(new ExitListener());
        Vprime.getTablePrime().addMouseListener(new TableClickListener());
        Vprime.getBtnAddPrime().addActionListener(new AddPrimeListener());
        Vprime.getBtnListPrime().addActionListener(new ListPrimeListener());
        Vprime.getBtnUpdatePrime().addActionListener(new UpdatePrimeListener());
        Vprime.getBtnDeletePrime().addActionListener(new DeletePrimeListener());
        Vprime.getBtnCancelPrime().addActionListener(new CancelListener());
        Vprime.getBtnSearchPrime().addActionListener(new SearchPrimeListener());
    }
    
    
    //Vider le champ d'inscription
    public void Cancel(){
        
        Vprime.getTxtBeginDatePrime().setDate(null);
        Vprime.getTxtEndDatePrime().setDate(null);
        Vprime.getTxtTypePrime().setText("");
        Vprime.getTxtMontantPrime().setText("");
        
    }
    
    //Bouton vider le champ
    public class CancelListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            Cancel();
            
        }
        
    }
    
    //Evenements rechercher
    public class SearchPrimeListener implements ActionListener{
        
        ChangeAndCalcul change = new ChangeAndCalcul();

        @Override
        public void actionPerformed(ActionEvent ae) {
           
            //Verifie si l'element rechercher est vide
            if(Vprime.getTxtSearchPrime().getText().isEmpty()){
               
                JOptionPane.showMessageDialog(null, "Insérez l'élement pour rechercher!!", "Erreur", JOptionPane.ERROR_MESSAGE);
                
            }
            else{
               
               primelist = new ArrayList<>();
               
               //Recherche dans la base de donné à l'aide de ServicePrime
               primelist = servicePrime.searchInPrime(Vprime.getTxtSearchPrime().getText());
               
               //Vider le tableau avant d'afficher
               TabModel.setRowCount(0);
               
               //Affichage de résultat dans le tableau
               for(Prime p: primelist){
                   
                   TabModel.addRow(new Object[]{p.getId(),p.getTypePrime(),change.ChangeDateFormat(p.getDateBegin()),change.ChangeDateFormat(p.getEndDate()),p.getMontant(),change.TextStatus(p.isStatus())});
                   
               }
               
               //ajout cet action dans l'historique
                HistoryDAO dao = new HistoryDAO();
                ServiceHistory service = new ServiceHistory(dao);
                
                History h = new History("Recherche: "+Vprime.getTxtSearchPrime().getText()+" dans le champ Prime",change.NowHour(),change.NowMinute(),change.NowDate());
                service.addHistory(h);
              
            }
            
        }
        
    }
    
    //Evenement supprimer
    public class DeletePrimeListener implements ActionListener{
        
        ChangeAndCalcul change = new ChangeAndCalcul(); //class réutilisable

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            //verifie si l'utilisateur selectionne l'element qu'il veut supprimer
            if(Vprime.getTablePrime().getSelectedRowCount() == 0){
               
                JOptionPane.showMessageDialog(null, "Selectionnez la ligne que vous supprimez!", "Erreur", JOptionPane.ERROR_MESSAGE);
                
            }
            else{
               
               //Affectation des élements dans le champ d'inscription
               long id = change.ChangeLong(Vprime.getTxtIDPrime().getSelectedItem().toString());
               String NomPrime = Vprime.getTxtTypePrime().getText();
               Date startDate = Vprime.getTxtBeginDatePrime().getDate();
               Date endDate = Vprime.getTxtEndDatePrime().getDate();
               double montant = change.ChangeDouble(Vprime.getTxtMontantPrime().getText());
               //Verifie si la date début et fin est actif ou innactif
               boolean status = change.isActif(startDate, endDate);
               
               //Création d'objet Prime p
               Prime p = new Prime(id, NomPrime, endDate, endDate, montant, status);
               
               //Suppréssion dans la base de donné
               servicePrime.DeletePrime(p);
               //Suppréssion dans la tableau
               TabModel.removeRow(Vprime.getTablePrime().getSelectedRow());
               Cancel();
               TabModel.setRowCount(0);
               
               //Ajout cet action dans l'historique
                HistoryDAO dao = new HistoryDAO();
                ServiceHistory service = new ServiceHistory(dao);
                
                History h = new History("Suppréssion ID: "+id+" dans le champ Prime",change.NowHour(),change.NowMinute(),change.NowDate());
                service.addHistory(h);
               
               JOptionPane.showMessageDialog(null, "Prime supprimé!", "Suppression", WIDTH);
                
            }
        }
        
    }
    
    
   //Evenements ajouter
    public class AddPrimeListener implements ActionListener{
        
        //initialisation 
        ChangeAndCalcul change = new ChangeAndCalcul(); // class réutilisable
        boolean correct = true;
        double montant = 0;
        long id = 0;
        
        @Override
        public void actionPerformed(ActionEvent ae) {
            
            //verifie s'il y a de vide chaque inscription
            if(Vprime.getTxtTypePrime().getText().isEmpty() || Vprime.getTxtMontantPrime().getText().isEmpty() || Vprime.getTxtBeginDatePrime().getDate() == null ||Vprime.getTxtEndDatePrime().getDate() == null || Vprime.getTxtIDPrime().getSelectedItem().toString().isEmpty()){
                
                JOptionPane.showMessageDialog(null, "Completez le vide et Ressayez", "Erreur", JOptionPane.ERROR_MESSAGE);
                 correct = false;
                
            }
            else{
                
                //Convertir de long,double et verifie s'il y a d'erreur
                if(change.ChangeLong(Vprime.getTxtIDPrime().getSelectedItem().toString())!= -1 && change.ChangeLong(Vprime.getTxtIDPrime().getSelectedItem().toString()) != -1){
                  
                     id = change.ChangeLong(Vprime.getTxtIDPrime().getSelectedItem().toString());
                     montant = change.ChangeDouble(Vprime.getTxtMontantPrime().getText());
                     correct = true;
                }else{}
                
                //Affectation de date et le type de prime
                Date startDate = Vprime.getTxtBeginDatePrime().getDate();
                Date endDate = Vprime.getTxtEndDatePrime().getDate();
                String NomPrime = Vprime.getTxtTypePrime().getText();
                
                //Verifie si la date début et fin est actif ou innactif à l'aide de boolean(utilisé la class ChangeAndCalcul)
                boolean status = change.isActif(startDate, endDate);
                
                //Création d'objet prime
                Prime prime = new Prime(id, NomPrime, startDate, endDate, montant, status);
                if(correct){//verifie s'il y a d'erreur
                    
                    //Ajout dans la base de donné 
                    boolean add = servicePrime.addPrime(prime);
                    
                    //vider la tableau avant d'afficher
                    TabModel.setRowCount(0);
                    
                    if(add){//verifie si ajouté dans la base de donné
                        
                        //Affichage dans le tableau
                        TabModel.addRow(new Object[]{id,NomPrime,change.ChangeDateFormat(startDate),change.ChangeDateFormat(endDate),montant,change.TextStatus(status)});
                        JOptionPane.showMessageDialog(null, "Prime ajouté!", "Ajout", WIDTH);
                        Cancel();
                        
                        //Ajout cet action dans l'historique
                         HistoryDAO dao = new HistoryDAO();
                         ServiceHistory service = new ServiceHistory(dao);
                
                         History h = new History("Ajout d'ID: "+prime.getId()+" dans le champ Prime",change.NowHour(),change.NowMinute(),change.NowDate());
                         service.addHistory(h);
                        
                        
                    }else{
                        
                        JOptionPane.showMessageDialog(null, "Votre ID est déja ajouté sur la liste!, Réssayez", "ERREUR", JOptionPane.ERROR_MESSAGE);  
                        
                    }
                    
                }else{}
            }
            
        }
        
    }
    
    //Evenements modifier
    public class UpdatePrimeListener implements ActionListener{

        //initialisation
        ChangeAndCalcul change = new ChangeAndCalcul();//class réutilisable
        boolean verify = true;
        double montant = 0;
        long id = 0;
        
        @Override
        public void actionPerformed(ActionEvent ae) {
            
            //Verifie si l'utilisateur selectionne le prime qu'il veut modifier
            if(Vprime.getTablePrime().getSelectedRowCount() == 0){
                
                JOptionPane.showMessageDialog(null, "Selectionnez la ligne que vous modifiez!", "Erreur", JOptionPane.ERROR_MESSAGE);
                
            }
            else{
                
                //Verification de changement long et double s'il y a d'erreur
                 if(change.ChangeLong(Vprime.getTxtIDPrime().getSelectedItem().toString())!= -1 && change.ChangeLong(Vprime.getTxtIDPrime().getSelectedItem().toString()) != -1){
                  
                     //Affectation de résultat
                     id = change.ChangeLong(Vprime.getTxtIDPrime().getSelectedItem().toString());
                     montant = change.ChangeDouble(Vprime.getTxtMontantPrime().getText());
                     verify = true;
                }else{}
                
                 //Affectation de date et le type de prime
                Date startDate = Vprime.getTxtBeginDatePrime().getDate();
                Date endDate = Vprime.getTxtEndDatePrime().getDate();
                String NomPrime = Vprime.getTxtTypePrime().getText();
                
                //Verification de date début et fin si actif ou innactif(utilisé la class ChangeAndCalcul)
                boolean status = change.isActif(startDate, endDate);
                
                //Création d'objet de Prime
                Prime primeModify = new Prime(id, NomPrime, startDate, endDate, montant, status);
                
                if(verify){// verifie s'il y a d'erreur
                  
                    //vider la tableau
                    TabModel.setRowCount(0);
                    
                    //Modifié dans la base de donné
                    boolean update = servicePrime.UpdatePrime(primeModify);
                    
                      if(update){//verifie si modifié dans la base (verifie s'il y a de doublons aussi)
                        
                          //Affichage dans le tableau
                          TabModel.addRow(new Object[]{id,NomPrime,change.ChangeDateFormat(startDate),change.ChangeDateFormat(endDate),montant,change.TextStatus(status)});
                          JOptionPane.showMessageDialog(null, "Prime modifié!", "Modification", WIDTH);
                          Cancel();
                          
                          //ajout cet action dans l'historique
                          HistoryDAO dao = new HistoryDAO();
                          ServiceHistory service = new ServiceHistory(dao);
                
                          History h = new History("Modification d'ID: "+id+" dans le champ Prime",change.NowHour(),change.NowMinute(),change.NowDate());
                          service.addHistory(h);
                          
                      }
                      else{
                         JOptionPane.showMessageDialog(null, "Votre ID est déja ajouté sur la liste!, Réssayez", "ERREUR", JOptionPane.ERROR_MESSAGE);  
                      }
                    
                }else {}
            }
            
        }
        
    }
    
    //Evenements de bouton liste
    public class ListPrimeListener implements ActionListener{
        
        ChangeAndCalcul change = new ChangeAndCalcul();

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            primelist = new ArrayList<>();
            //vider la tableau
            TabModel.setRowCount(0);
            //Récuperation de liste dans la base de donné à l'aide ServicePrime
            primelist = servicePrime.ListPrime();
            
            //Affichage dans la tableau
            for(Prime p: primelist){
                String statustxt = change.TextStatus(p.isStatus());
                TabModel.addRow(new Object[]{p.getId(),p.getTypePrime(),change.ChangeDateFormat(p.getDateBegin()),change.ChangeDateFormat(p.getEndDate()),p.getMontant(),statustxt});
            }
            
            //Ajout cet action dans l'historique
             HistoryDAO dao = new HistoryDAO();
             ServiceHistory service = new ServiceHistory(dao);
                
             History h = new History("Ouverture Liste de Prime",change.NowHour(),change.NowMinute(),change.NowDate());
             service.addHistory(h);
            
        }
        
    } 
    
    //Evenements sur le tableau
    public class TableClickListener implements MouseListener{
        
        ChangeAndCalcul change = new ChangeAndCalcul();//Instanciation de class réutilisable

        @Override
        public void mouseClicked(MouseEvent me) {
           
            //Récuperation des elements selectionné sur le tableau
            long id = change.ChangeLong(TabModel.getValueAt(Vprime.getTablePrime().getSelectedRow(), 0).toString());
            String NomPrime = TabModel.getValueAt(Vprime.getTablePrime().getSelectedRow(), 1).toString();
            String OneDate = TabModel.getValueAt(Vprime.getTablePrime().getSelectedRow(), 2).toString();
            String TwoDate = TabModel.getValueAt(Vprime.getTablePrime().getSelectedRow(), 3).toString();
            String montant = TabModel.getValueAt(Vprime.getTablePrime().getSelectedRow(), 4).toString();
            
            //Changement de date type String en type Date
            Date startDate = change.ChangeDateString(OneDate);
            Date endDate = change.ChangeDateString(TwoDate);
            
            //Insertion dans le champ d'inscription
            Vprime.getTxtIDPrime().setSelectedItem(id);
            Vprime.getTxtIDPrime().setEnabled(false);//ID ne peut pas modifier
            Vprime.getTxtTypePrime().setText(NomPrime);
            Vprime.getTxtBeginDatePrime().setDate(startDate);
            Vprime.getTxtEndDatePrime().setDate(endDate);
            Vprime.getTxtMontantPrime().setText(montant);
            
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
    
    public class ExitListener implements WindowListener{

        @Override
        public void windowOpened(WindowEvent we) {
           
            //Appel de la methode ServiceEmployee qui récupère la liste d'employé
            EmployeeDAO eDAO= new EmployeeDAO();
            ServiceEmployee Se = new ServiceEmployee(eDAO);
            List<Employee> list = new ArrayList<>();
            //Affectation de résultat dans la base de donné
            list = Se.getListEmployees();
            Vprime.getTxtIDPrime().setMaximumRowCount(list.size());
            //Ajout d'ID dans le champ de ID sur le Prime quand la fenêtre ouvre
            for(Employee e:list){
               Vprime.getTxtIDPrime().addItem(e.getId());
            }  
            
        }

        @Override
        public void windowClosing(WindowEvent we) {
           
            //Fermeture de la fenêtre Prime et revenir au menu principal
            Vprime.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            Vs = new ViewSociety();
            ControleurSociety con = new ControleurSociety(Vs);
            Vs.setVisible(true);
            Vprime.dispose();
            
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
