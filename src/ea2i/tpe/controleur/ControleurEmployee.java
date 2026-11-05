/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.controleur;

import ea2i.tpe.dao.sqlite.PostDAO;
import ea2i.tpe.model.Employee;
import ea2i.tpe.model.Post;
import ea2i.tpe.service.ServiceEmployee;
import ea2i.tpe.service.ServicePost;
import ea2i.tpe.view.ViewEmployee;
import ea2i.tpe.view.ViewPost;
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
import javax.swing.table.DefaultTableModel;
import ea2i.tpe.calcul.ChangeAndCalcul;
import ea2i.tpe.dao.sqlite.HistoryDAO;
import ea2i.tpe.model.History;
import ea2i.tpe.service.ServiceHistory;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;





/**
 *
 * @author lenovo
 */
public class ControleurEmployee {
   
    //Déclaration des proprietés
   private final ChangeAndCalcul change;
   private final ViewEmployee Ve;
   private ViewPost Vp;
   private ViewSociety Vs;
   private Employee worker;
   private ControleurSociety cont;
   private List<Employee> list;
   private final ServiceEmployee ServiceWorker;
   private ServicePost Servicepost;
   private final DefaultTableModel TabModel;
   
   
   
   public ControleurEmployee(ViewEmployee Ve,ServiceEmployee Se){
       this.Ve = Ve;
       this.change = new ChangeAndCalcul();
       this.ServiceWorker = Se;
       //Convertir le JTable dans le vue en DefaultTableModel
       this.TabModel = (DefaultTableModel) Ve.getTableEmployee().getModel();
   
       //Ajout des évenements au bouton et le vue
       Ve.getBtnEmployeeRetour().addActionListener(new ReturnListener());
       Ve.getBtnAddEmployee().addActionListener(new AddEmployeeListener());
       Ve.addWindowListener(new ShowWindowListener());
       Ve.getBtnUpdateEmployee().addActionListener(new UpdateActionListener());
       Ve.getTableEmployee().addMouseListener(new TableClickListener());
       Ve.getBtnListEmployee().addActionListener(new ListEmployeeListener());
       Ve.getBtnCancelEmployee().addActionListener(new CancelListener());
       Ve.getBtnSearchEmployee().addActionListener(new SearchListenr());
       Ve.getBtnDeleteEmployee().addActionListener(new DeleteEmployeeListener());
   }
   
  
  public class ReturnListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            //Revenir au ménu principal
            Vs = new ViewSociety();
            ControleurSociety con = new ControleurSociety(Vs);
            Vs.setVisible(true);
            Ve.dispose();
        }
       
   }
   
  //Effacer le contenu d'inscription
   public void Cancel(){
       
       Ve.getTxtId().setText("");
       Ve.getTxtName().setText("");
       Ve.getTxtFirstName().setText("");
       Ve.getTxtBirthday().setDate(null);
       Ve.getTxtSalary().setText("");
   }
   
   //Action effacer le contenu
   public class CancelListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            Cancel();
        }
       
   }
   
  //Action pour la recherche
  public class SearchListenr implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
          
            //Récuperation d'element qui peut rechercher
          String search = Ve.getTxtElementSearch().getText().trim();
            
              //Verification si l'élement rechercher est vide
              if(!search.isEmpty()){
                 
                //Affectation de Resultat(Appel de méthode dans le ServiceEmployee en fonction de DAO) au tableau list
                list = ServiceWorker.getEmployee(search);
                
                //Affichage
                for(Employee e: list){
                     if(e!=null){
                         
                        TabModel.setRowCount(0);
                        TabModel.addRow(new Object[]{e.getId(),e.getName(),e.getFirstName(),change.ChangeDateFormat(e.getBirthday()),e.getPoste(),e.getSalaire()});
                        
                     }
                     else{
                       TabModel.setRowCount(0);
                     }
                }
                
                 //Ajout cet action dans l'historique
                 HistoryDAO dao = new HistoryDAO();
                 ServiceHistory service = new ServiceHistory(dao);
                
                 History h = new History("Recherche '"+search+"' dans le champ Employés",change.NowHour(),change.NowMinute(),change.NowDate());
                 service.addHistory(h);
                 
              }
             else{
                JOptionPane.showMessageDialog(null, "Insérez l'élement que vous recherchez!", "Erreur", JOptionPane.ERROR_MESSAGE);  
              }
        }
      
  }
  
   //Action ajouter d'un employé
   public class AddEmployeeListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
             
             //Déclaration de variable utilisé
             boolean correct = true;
             long id=0;
             double salary=0;
             Date Birthday = null;
             String name = null,FirstName = null,NamePost = null,CIN = null;
             
             //Verification des elements d'un employé dans le vue si null
             if(Ve.getTxtId().getText().isEmpty()||Ve.getTxtName().getText().isEmpty()||Ve.getTxtSalary().getText().isEmpty()){
                 
                 JOptionPane.showMessageDialog(null, "Completez le vide et Ressayez", "Erreur", JOptionPane.ERROR_MESSAGE);
                 correct = false;// verification faux si null
                 
             }else{
                   //Récuperation des élements si n'est pas null
                   name = Ve.getTxtName().getText();
                   FirstName = Ve.getTxtFirstName().getText();
                   Birthday = Ve.getTxtBirthday().getDate();
                   NamePost = Ve.getTxtPostEmployee().getSelectedItem().toString();
                   
                   //Convertir d'ID en long et salaire en Double et verifie s'il y a d'erreur
                   if(change.ChangeLong(Ve.getTxtId().getText()) == -1 ||change.ChangeDouble(Ve.getTxtSalary().getText())==-1 ){
                       correct = false;// verification faux s'il y a d'erreur
                   }else{
                       
                     //Convertir s'il n'a pas d'erreur à l'aide de class ChangeAndCalcul
                     id = change.ChangeLong(Ve.getTxtId().getText());
                     salary = change.ChangeDouble(Ve.getTxtSalary().getText());
                     correct = true;// verification vrai si n'a pas d'erreur
                   } 
                  
             }
             
            //Verification si vrai
            if(correct){
           
               worker = new Employee(id, name, FirstName, Birthday,NamePost, salary);
               
               //Ajouter d'un employé à l'aide de ServiceEmployee(directe à la base de donné)
               boolean add = ServiceWorker.addEmployee(worker);
               TabModel.setRowCount(0);
               if(add){
                   
                    //Ajout au tableau
                    TabModel.addRow(new Object[]{id,name,FirstName,change.ChangeDateFormat(Birthday),NamePost,salary});
                    JOptionPane.showMessageDialog(null, "Enregistré", "Nouveau Employé", WIDTH);
                    Cancel();
                    
                    //Ajout cet action dans l'historique
                    HistoryDAO dao = new HistoryDAO();
                    ServiceHistory service = new ServiceHistory(dao);
                
                    History h = new History("Ajout d'employé:"+worker.getFirstName()+" dans le champ Nouveau Employés",change.NowHour(),change.NowMinute(),change.NowDate());
                    service.addHistory(h);
                    
                   
               }else{
                   JOptionPane.showMessageDialog(null, "Votre ID est déja ajouté sur la liste,Ressayez!", "ERREUR", JOptionPane.ERROR_MESSAGE);
               }
          
           }else{}
        }
       
   }
   
   //Action pour modifier un employé
   public class UpdateActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
             //Récuperation de nombre de ligne de selection pour modifier
             if(Ve.getTableEmployee().getSelectedRowCount() == 1){
                 
               boolean correct = false;
               long id = 0;
               double salary = 0;
               String CIN = null;
               
                   if(change.ChangeLong(Ve.getTxtId().getText()) == -1 ||change.ChangeDouble(Ve.getTxtSalary().getText())==-1 ){
                       
                       correct = false;// verification faux s'il y a d'erreur
                       
                   }else{
                       
                     //Convertir s'il n'a pas d'erreur à l'aide de class ChangeAndCalcul
                     id = change.ChangeLong(Ve.getTxtId().getText());
                     salary = change.ChangeDouble(Ve.getTxtSalary().getText());
                     correct = true;// verification vrai si n'a pas d'erreur
                   } 
                 
                    if(correct){
                        
                       Date birthday = Ve.getTxtBirthday().getDate();
                       Employee worker = new Employee(id,Ve.getTxtName().getText(),Ve.getTxtFirstName().getText(),birthday,Ve.getTxtPostEmployee().getSelectedItem().toString(),salary);
                       
                       //Modification envoie au ServiceEmployee(directe au base de donné(DA0))
                       boolean update = ServiceWorker.UpdateEmployee(worker);
                       TabModel.setRowCount(0);
                       if(update){
                         
                            //Affichage dans le tableau
                            TabModel.addRow(new Object[]{worker.getId(),worker.getName(),worker.getFirstName(),change.ChangeDateFormat(birthday),worker.getPoste(),worker.getSalaire()});
                            JOptionPane.showMessageDialog(null, "Employé modifié", "Modification", WIDTH);
                          
                            Cancel();
                            //Ajout cet action au historique
                            HistoryDAO dao = new HistoryDAO();
                            ServiceHistory service = new ServiceHistory(dao);
                
                            History h = new History("Modification d'employé: "+worker.getFirstName()+" dans le champ Employés",change.NowHour(),change.NowMinute(),change.NowDate());
                            service.addHistory(h);
                           
                       }
                       else{
                          JOptionPane.showMessageDialog(null, "Votre ID est deja sur la liste,Ressayez!", "Erreur", JOptionPane.ERROR_MESSAGE); 
                       }
                       
                    }else{}
                      
               
           }
         else{
           
         JOptionPane.showMessageDialog(null, "Selectionnez l'employé que vous modifiez!", "Erreur", JOptionPane.ERROR_MESSAGE);
                 
       }
       
   }
 }  
   
   //Action pour supprimer un employé
   public class DeleteEmployeeListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
       //Récuperation de nombre de ligne du selection pour supprimer
       if(Ve.getTableEmployee().getSelectedRowCount() == 1){
          
          //Affectation des élements d'un employé
          String ID = Ve.getTxtId().getText();
          String Name = Ve.getTxtName().getText();
          String FirstName = Ve.getTxtFirstName().getText();
          String post = Ve.getTxtPostEmployee().getSelectedItem().toString();
          Date Birthday  = Ve.getTxtBirthday().getDate();
          String Salary = Ve.getTxtSalary().getText();
          
                
                 worker = new Employee(Long.parseLong(ID),Name,FirstName,Birthday,post,Double.parseDouble(Salary));
                 
                 //Suppréssion d'un employé au base de donné à l'aide ServiceEmployee
                 ServiceWorker.DeleteEmployee(worker);
                 JOptionPane.showMessageDialog(null, "Employé supprimé", "Suppréssion", WIDTH);
                 TabModel.removeRow(Ve.getTableEmployee().getSelectedRow());
                 Cancel();
                 
                 //Ajout cet action dans l'historique
                 HistoryDAO dao = new HistoryDAO();
                 ServiceHistory service = new ServiceHistory(dao);
                
                 History h = new History("Suppréssion d'employé:"+worker.getFirstName()+" dans le champ Employés",change.NowHour(),change.NowMinute(),change.NowDate());
                 service.addHistory(h);
                
          
       }else{
         JOptionPane.showMessageDialog(null, "Selectionnez l'employé pour supprimer!", "Erreur", JOptionPane.ERROR_MESSAGE);  
       }
          
        }
       
   }
   
   //Action pour le bouton liste
   public class ListEmployeeListener implements ActionListener{
      
       
        @Override
        public void actionPerformed(ActionEvent ae) {
           list = new ArrayList<>();
           
           //Affectation de Resultat dans le base de donné au tableau list à l'aide ServiceEmployee
           list = ServiceWorker.getListEmployees();
           
           TabModel.setRowCount(0);
           
           
           //Affichage dans le tableau
           for(Employee e:list){
               TabModel.addRow(new Object[]{e.getId(),e.getName(),e.getFirstName(),change.ChangeDateFormat(e.getBirthday()),e.getPoste(),e.getSalaire(),"ar"});
               
           }
           
           //Ajout cet action dans l'historique
            HistoryDAO dao = new HistoryDAO();
            ServiceHistory service = new ServiceHistory(dao);
                
            History h = new History("Ouverture Liste d'employé",change.NowHour(),change.NowMinute(),change.NowDate());
            service.addHistory(h);
           
        }
       
   }
   
   //Action pour la vue Employé
   public class ShowWindowListener implements WindowListener{

        @Override
        public void windowOpened(WindowEvent we) {
           
           //Ajout du Nom de poste dans l'employé quand la fenetre ouvre
           PostDAO postDAO = new PostDAO();
           Servicepost = new ServicePost(postDAO);
           List<Post> post = new ArrayList<>();
           post = Servicepost.ListPost();
          
           Ve.getTxtPostEmployee().setMaximumRowCount(post.size());
           for(Post p:post){
              Ve.getTxtPostEmployee().addItem(p.getNamePost());
           }
  }

        @Override
        public void windowClosing(WindowEvent we) {
            
            //Fermeture de la vue Employé
            Ve.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            Vs = new ViewSociety();
            ControleurSociety con = new ControleurSociety(Vs);
            Vs.setVisible(true);
            Ve.dispose();
        
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
   
    public class TableClickListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent me) {
            
            long IdTable = change.ChangeLong(TabModel.getValueAt(Ve.getTableEmployee().getSelectedRow(), 0).toString());
            String nameTable = TabModel.getValueAt(Ve.getTableEmployee().getSelectedRow(), 1).toString();
            String firstNameTable = TabModel.getValueAt(Ve.getTableEmployee().getSelectedRow(), 2).toString();
            String dateString = TabModel.getValueAt(Ve.getTableEmployee().getSelectedRow(), 3).toString();
           
            String postTable = TabModel.getValueAt(Ve.getTableEmployee().getSelectedRow(), 4).toString();
            String salaryTable = TabModel.getValueAt(Ve.getTableEmployee().getSelectedRow(), 5).toString();
            
            Ve.getTxtId().setText(String.valueOf(IdTable));
            Ve.getTxtId().setEditable(false);
            Ve.getTxtName().setText(nameTable);
            Ve.getTxtFirstName().setText(firstNameTable);
            Ve.getTxtBirthday().setDate(change.ChangeDateString(dateString));
            Ve.getTxtPostEmployee().setSelectedItem(postTable);
            Ve.getTxtSalary().setText(salaryTable);
            
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
