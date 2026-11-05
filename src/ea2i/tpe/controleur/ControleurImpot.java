/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.controleur;

import ea2i.tpe.dao.sqlite.EmployeeDAO;
import ea2i.tpe.model.Employee;
import ea2i.tpe.model.Impot;
import ea2i.tpe.service.ServiceEmployee;
import ea2i.tpe.service.ServiceImpot;
import ea2i.tpe.calcul.ChangeAndCalcul;
import ea2i.tpe.dao.sqlite.HistoryDAO;
import ea2i.tpe.model.History;
import ea2i.tpe.service.ServiceHistory;
import ea2i.tpe.view.ViewImpot;
import ea2i.tpe.view.ViewSociety;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lenovo
 */
public class ControleurImpot {
    
    //Déclaration de proprietés
    private final ViewImpot Vi;
    private ViewSociety Vs;
    private Impot imp;
    private final ChangeAndCalcul change;
    private final ServiceImpot ServiceImp;
    private List<Impot> listImpot;
    
    private final DefaultTableModel TabModel;
    
    public ControleurImpot(ViewImpot Vi,ServiceImpot ServiceImp){
        this.Vi = Vi;
        this.ServiceImp = ServiceImp;
        
        //Convertir de JTable en DefaultTableModel
        this.TabModel = (DefaultTableModel) Vi.getTableCotisation().getModel();
        this.change = new ChangeAndCalcul();
        
        //Ajout d'évenement dans le bouton et le vue
        Vi.addWindowListener(new ExitListener());
        Vi.getTableCotisation().addMouseListener(new TableClickListener());
        Vi.getBtnAddImpot().addActionListener(new AddImpotListener());
        Vi.getBtnListImpot().addActionListener(new ListImpotListener());
        Vi.getBtnSearchImpot().addActionListener(new SearchImpotListener());
        Vi.getBtnUpdateImpot().addActionListener(new UpdateImpotListener());
        Vi.getBtnDeleteImpot().addActionListener(new DeleteImpotListener());
        Vi.getBtnImpotCancel().addActionListener(new CancelListener());
    }
    
    
    //Suppréssion du contenu de l'inscription
    public void Cancel(){
        
        Vi.getTxtTypeImpot().setText("");
        Vi.getTxtMontantImpot().setText("");
        Vi.getTxtCotisation().setText("");
        Vi.getTxtMontantCotisation().setText("");
        
    }
    
    //Action pour rechercher 
    public class SearchImpotListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
           //Verification d'element rechercher si vide
           if(Vi.getTxtSearchImpot().getText().isEmpty()){
              
             JOptionPane.showMessageDialog(null, "Entrez l'element pour rechercher!", "ERREUR", JOptionPane.ERROR_MESSAGE);  
               
           }
           else{
              
               //Récuperation du résultat dans la base de donné à l'aide d'affectation de list et le ServiceImpot
               listImpot = new ArrayList<>();
               listImpot = ServiceImp.searchImpot(Vi.getTxtSearchImpot().getText());
               TabModel.setRowCount(0);
               
               //Affichage dans le tableau
               for(Impot imp: listImpot){
                  
                   TabModel.addRow(new Object[]{imp.getId(),imp.getMonth(),imp.getTypesImpot(),imp.getTauxImpot(),imp.getTypesCotisations(),imp.getTauxCotisations(),imp.getTotalTaux(),imp.getTotalMontants()});
                   
               }
               
                //Ajout cet action dans l'historique
                HistoryDAO dao = new HistoryDAO();
                ServiceHistory service = new ServiceHistory(dao);
                
                History h = new History("Recherche "+Vi.getTxtSearchImpot().getText()+" dans le champ d'Impôt et cotisation",change.NowHour(),change.NowMinute(),change.NowDate());
                service.addHistory(h);
               
           }
        }
        
    }
    
    //Evenement de bouton liste
    public class ListImpotListener implements ActionListener{
       
        
        @Override
        public void actionPerformed(ActionEvent ae) {
            
           //Récuperation de resultat dans le base de donné à l'aide de ServiceImpot
           listImpot = new ArrayList<>();
           listImpot = ServiceImp.listImpot();
           TabModel.setRowCount(0);
           
           //Affichage dans le tableau
           for(Impot imp: listImpot){
              TabModel.addRow(new Object[]{imp.getId(),imp.getMonth(),imp.getTypesImpot(),imp.getTauxImpot(),imp.getTypesCotisations(),imp.getTauxCotisations(),imp.getTotalTaux(),imp.getTotalMontants()}); 
           }
           
           //Ajout cet action dans l'historique
           HistoryDAO dao = new HistoryDAO();
           ServiceHistory service = new ServiceHistory(dao);
                
           History h = new History("Ouverture Liste d'impôt et cotisation",change.NowHour(),change.NowMinute(),change.NowDate());
           service.addHistory(h);
           
        }
        
    }
    
    public class CancelListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            Cancel();
        }
        
    }
    
    
   public class DeleteImpotListener implements ActionListener{
     
       ChangeAndCalcul change = new ChangeAndCalcul();
       double MontantImpot = 0, MontantCotisation = 0;
       long ID = 0;
       boolean verify = true;
       
        @Override
        public void actionPerformed(ActionEvent ae) {
            
            //Verifie si l'utilisateur selectionne l'impôt et la cotisation qu'il veut supprimer
            if(Vi.getTableCotisation().getSelectedRowCount() == 0){
               
                JOptionPane.showMessageDialog(null, "Sélectionnez l'impôt et la cotisation que vous supprimer!", "ERREUR", JOptionPane.ERROR_MESSAGE); 
                
            }
            else{
                
                //Récuperation des elements dans le champ d'inscription
                 String id = Vi.getTxtIDimpot().getSelectedItem().toString();
                 int month = Vi.getTxtMonthImpot().getMonth();
                 String NameImpot = Vi.getTxtTypeImpot().getText();
                 String NameCotisation = Vi.getTxtCotisation().getText();
                 
                 //Verifie s'il n'y pas a d'erreur dans la changement de long et double par la fonction de class ChangeAndCalcul
               if(change.ChangeLong(id)!= -1 && change.ChangeDouble(Vi.getTxtMontantImpot().getText()) != -1 && change.ChangeDouble(Vi.getTxtMontantCotisation().getText()) != -1){
                  MontantImpot = change.ChangeDouble(Vi.getTxtMontantImpot().getText());
                  MontantCotisation = change.ChangeDouble(Vi.getTxtMontantCotisation().getText()); 
                  ID = change.ChangeLong(id);
                  verify = true;
               }else{
                  verify = false; 
               }
               
               //Recherche de salairebase d'un employé qui ajout d'impôt et cotisation
               Employee e = change.SearchInEmployee(ID);
               
               //Affectation de salaire qui obtient dans la base de donné dans l'Employé
               double salaireBase = e.getSalaire();
               
               //Affectation de résultat du calcul taux (ChangeAndCalcul est utilisé pour calculer)
               double tauxImpot = change.CalculTaux(salaireBase, MontantImpot);
               double tauxCotisation = change.CalculTaux(salaireBase, MontantCotisation);
               
               //Affectation de résultat du calcul total taux et total montant (ChangeAndCalcul est utilisé pour calculer)
               double totaltauxImpot = tauxImpot + tauxCotisation;
               double totalMontant = MontantImpot + MontantCotisation;
               
               //Création d'objet imp
               Impot imp = new Impot(ID, change.convStringMois(month),NameImpot, tauxImpot, NameCotisation, tauxCotisation, totaltauxImpot, totalMontant);
               
               if(verify){
                 
                   //Suppréssion d'Impôt dans la base de donné par la ServiceImpot en fonction de DAO
                   ServiceImp.deleteImpot(imp);
                   TabModel.removeRow(Vi.getTableCotisation().getSelectedRow());
                   JOptionPane.showMessageDialog(null, "L'impôt et cotisation sont supprimés!", "Supprimer", WIDTH);
                   Cancel();
                   
                   //Ajout cet action dans l'historique
                   HistoryDAO dao = new HistoryDAO();
                   ServiceHistory service = new ServiceHistory(dao);
                
                   History h = new History("Suppréssion ID:"+imp.getId()+" dans le champ d'Impôt et cotisation",change.NowHour(),change.NowMinute(),change.NowDate());
                   service.addHistory(h);
                   
               }
                
            }
            
        }
       
   }
    
    
    public class AddImpotListener implements ActionListener{
       
       ChangeAndCalcul change = new ChangeAndCalcul();
       long ID=0;
       double MontantImpot=0,MontantCotisation=0;
       boolean verify = true;
        
        
        @Override
        public void actionPerformed(ActionEvent ae) {
             
            //Verifie s'il y a de vide dans le champ
           if(Vi.getTxtTypeImpot().getText().isEmpty() || Vi.getTxtCotisation().getText().isEmpty()||Vi.getTxtMontantImpot().getText().isEmpty()|| Vi.getTxtMontantCotisation().getText().isEmpty()){
              
                JOptionPane.showMessageDialog(null, "Completez le vide et Ressayez!", "ERREUR", JOptionPane.ERROR_MESSAGE);
           }
           else{
              
               //Affectation s'il n'y a pas de vide
               String id = Vi.getTxtIDimpot().getSelectedItem().toString();
                int month = Vi.getTxtMonthImpot().getMonth();
               String NameImpot = Vi.getTxtTypeImpot().getText();
               String NameCotisation = Vi.getTxtCotisation().getText();
               
               //Verifie s'il y a d'erreur dans le changement(double,long)
               if(change.ChangeLong(id)!= -1 && change.ChangeDouble(Vi.getTxtMontantImpot().getText()) != -1 && change.ChangeDouble(Vi.getTxtMontantCotisation().getText()) != -1){
                  MontantImpot = change.ChangeDouble(Vi.getTxtMontantImpot().getText());
                  MontantCotisation = change.ChangeDouble(Vi.getTxtMontantCotisation().getText()); 
                  ID = change.ChangeLong(id);
                  verify = true;
               }else{
                  verify = false;
               }
               
               //Recherche de salairebase dans l'Employé
               Employee e = change.SearchInEmployee(ID);
               
               //Affectation de salairebase
               double salaireBase = e.getSalaire();
               
               //calcul taux(utilisé la class ChangeAndCalcul)
               double tauxImpot = change.CalculTaux(salaireBase, MontantImpot);
               double tauxCotisation = change.CalculTaux(salaireBase, MontantCotisation);
               
               //calcul total taux et total montant(utilisé la class ChangeAndCalcul)
               double totaltauxImpot = tauxImpot + tauxCotisation;
               double totalMontant = MontantImpot + MontantCotisation;
               
              //Création d'objet imp
               Impot imp = new Impot(ID,change.convStringMois(month),NameImpot, tauxImpot, NameCotisation, tauxCotisation, totaltauxImpot, totalMontant);
              
               if(verify){ //verifie s'il y a d'erreur
                   
                   //Verifie si la salaireBase est plus petit que le total montant d'impôt et la cotisation
                   if(salaireBase > totalMontant){
                      
                       //Ajout dans la base de donné
                       boolean add = ServiceImp.addImpot(imp);
                       
                       //vider la tableau avant d'afficher
                       TabModel.setRowCount(0);
                       
                       if(add){//verifie s'il y a d'erreur dans la base de donné
                         
                           //Affichage dans la tableau
                          TabModel.addRow(new Object[]{imp.getId(),imp.getMonth(),imp.getTypesImpot(),imp.getTauxImpot(),imp.getTypesCotisations(),imp.getTauxCotisations(),imp.getTotalTaux(),imp.getTotalMontants()});
                          JOptionPane.showMessageDialog(null, "Nouveau impôt et cotisation sont ajoutés!", "Ajout", WIDTH);
                          Cancel();
                          
                          //Ajout cet action dans l'historique
                          HistoryDAO dao = new HistoryDAO();
                          ServiceHistory service = new ServiceHistory(dao);
                
                          History h = new History("Ajout de ID:"+imp.getId()+" dans le champ d'Impôt et cotisation",change.NowHour(),change.NowMinute(),change.NowDate());
                          service.addHistory(h);
                          
                           
                       }else{
                           JOptionPane.showMessageDialog(null, "Votre id est deja ajouté sur la liste!", "ERREUR", JOptionPane.ERROR_MESSAGE);  
                       }
                   }
                   else{
                     String erreur = "Votre salaire base est inferieur à votre montant d'impot \net cotisation!, Modifiez votre salaire base dans l'Employés";
                     JOptionPane.showMessageDialog(null,erreur , "ERREUR", JOptionPane.ERROR_MESSAGE);    
                   }
                       
                   }else{} 
           }
        }
        
    }
    
    //Evenements qui modifie
    public class UpdateImpotListener implements ActionListener{
            
        ChangeAndCalcul change = new ChangeAndCalcul();
        boolean verify =true;
        double MontantImpot = 0,MontantCotisation = 0;
        long ID = 0;
        
        @Override
        public void actionPerformed(ActionEvent ae) {
            
            //Verifie si l'utilisateur selectionne l'impôt et la cotisation qu'il veut modifier
            if(Vi.getTableCotisation().getSelectedRowCount() == 0){
                
               JOptionPane.showMessageDialog(null, "Sélectionnez l'impôt et la cotisation que vous modifiez!", "ERREUR", JOptionPane.ERROR_MESSAGE); 
                
            }
            else{
               
                //Affectation des elements qu'il selectionne
               String id = Vi.getTxtIDimpot().getSelectedItem().toString();
               int month = Vi.getTxtMonthImpot().getMonth();
               String NameImpot = Vi.getTxtTypeImpot().getText();
               String NameCotisation = Vi.getTxtCotisation().getText();
               if(change.ChangeLong(id)!= -1 && change.ChangeDouble(Vi.getTxtMontantImpot().getText()) != -1 && change.ChangeDouble(Vi.getTxtMontantCotisation().getText()) != -1){
                  MontantImpot = change.ChangeDouble(Vi.getTxtMontantImpot().getText());
                  MontantCotisation = change.ChangeDouble(Vi.getTxtMontantCotisation().getText()); 
                  ID = change.ChangeLong(id);
                  verify = true;
               }else{
                  verify = false;
               }
        
               //Recherche de salaireBase dans l'Employé
               Employee e = change.SearchInEmployee(ID);
               
               //Affectation de salaireBase
               double salaireBase = e.getSalaire();
               
               //Calcul taux (utilisé la class ChangeAndCalcul)
               double tauxImpot = change.CalculTaux(salaireBase, MontantImpot);
               double tauxCotisation = change.CalculTaux(salaireBase, MontantCotisation);
               
               //Calcul total taux et total montant
               double totaltauxImpot = tauxImpot + tauxCotisation;
               double totalMontant = MontantImpot + MontantCotisation;
               
               //Création d'objet impModify
               Impot impModify = new Impot(ID,change.convStringMois(month),NameImpot, tauxImpot, NameCotisation, tauxCotisation, totaltauxImpot, totalMontant);
               
               if(verify){ //Verifie s'il y a d'erreur
                    
                  //Verifie si la salaireBase est plus petit que le montant d'impôt et cotisation
                  if(salaireBase > totalMontant){
                      
                     //Modifier dans la base de donné
                     boolean update = ServiceImp.updateImpot(impModify);
                     
                     //vider la tableau avant d'afficher
                     TabModel.setRowCount(0);
                     
                       if(update){// verifie s'il y a d'erreur
                         
                          //affichage
                          TabModel.addRow(new Object[]{impModify.getId(),impModify.getMonth(),impModify.getTypesImpot(),impModify.getTauxImpot(),impModify.getTypesCotisations(),impModify.getTauxCotisations(),impModify.getTotalTaux(),impModify.getTotalMontants()});
                          JOptionPane.showMessageDialog(null, "L'impôt et cotisation sont modifiés!", "Modifier", WIDTH);
                          Cancel();
                          
                          //Ajout cet action dans l'historique
                          HistoryDAO dao = new HistoryDAO();
                          ServiceHistory service = new ServiceHistory(dao);
                
                          History h = new History("Modification de ID:"+imp.getId()+" dans le champ d'Impôt et cotisation",change.NowHour(),change.NowMinute(),change.NowDate());
                          service.addHistory(h);
                           
                       }else{
                           JOptionPane.showMessageDialog(null, "Votre id est deja ajouté sur la liste!", "ERREUR", JOptionPane.ERROR_MESSAGE);  
                       } 
                  }
                   else{
                     String erreur = "Votre salaire base est inferieur à votre montant d'impot \net cotisation!, Modifiez votre salaire base dans l'Employés";
                     JOptionPane.showMessageDialog(null,erreur , "ERREUR", JOptionPane.ERROR_MESSAGE);    
                   }
                       
                   }else{} 
               
            }
            
        }
        
    }
    
    public class ExitListener implements WindowListener{

        @Override
        public void windowOpened(WindowEvent we) {
            
            /* 
              Appel de la methode dans la Service en fonction de DAO qui récupère la liste d'employé
            */
            EmployeeDAO eDAO= new EmployeeDAO();
            ServiceEmployee Se = new ServiceEmployee(eDAO);
            List<Employee> list = new ArrayList<>();
            
            //Affectation de résultat dans le tableau list
            list = Se.getListEmployees();
            Vi.getTxtIDimpot().setMaximumRowCount(list.size());
            
            //Ajout d'ID dans le champ ID d'impôt et cotisation
            for(Employee e:list){
               Vi.getTxtIDimpot().addItem(e.getId());
            }  
        }

        @Override
        public void windowClosing(WindowEvent we) {
           
            //Fermeture de la fenêtre Impot et cotisation
            Vi.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            Vs = new ViewSociety();
            ControleurSociety con = new ControleurSociety(Vs);
            Vs.setVisible(true);
            Vi.dispose();
            
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
    
    //Evenements sur la tableau
   public class TableClickListener implements MouseListener{
       ChangeAndCalcul change = new ChangeAndCalcul();
        @Override
        public void mouseClicked(MouseEvent me) {
            
            //Récuperation des elements selectionné sur la tableau et changement de long et double
            long id = change.ChangeLong(TabModel.getValueAt(Vi.getTableCotisation().getSelectedRow(), 0).toString());
            String month = TabModel.getValueAt(Vi.getTableCotisation().getSelectedRow(), 1).toString();
            String NomImpot = TabModel.getValueAt(Vi.getTableCotisation().getSelectedRow(), 2).toString();
            double tauxImpot = change.ChangeDouble(TabModel.getValueAt(Vi.getTableCotisation().getSelectedRow(), 3).toString());
            String cotisation = TabModel.getValueAt(Vi.getTableCotisation().getSelectedRow(), 4).toString();
            double tauxCotisa = change.ChangeDouble(TabModel.getValueAt(Vi.getTableCotisation().getSelectedRow(), 5).toString());
            
            //Récuperation de salaireBase dans l'employé(dans la base de donné)
            Employee e = change.SearchInEmployee(id);
            
            double salaireBase = e.getSalaire();//Affectation de résultat
            
            //Insertion des elements selectionné dans le champ d'inscription
            Vi.getTxtIDimpot().setSelectedItem(id);
            Vi.getTxtIDimpot().setEnabled(false);
            Vi.getTxtMonthImpot().setMonth(change.convIntMois(month));
            Vi.getTxtTypeImpot().setText(NomImpot);
            Vi.getTxtMontantImpot().setText(String.valueOf(change.CalculMontant(salaireBase, tauxImpot)));
            Vi.getTxtCotisation().setText(cotisation);
            Vi.getTxtMontantCotisation().setText(String.valueOf(change.CalculMontant(salaireBase, tauxCotisa)));
            
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
