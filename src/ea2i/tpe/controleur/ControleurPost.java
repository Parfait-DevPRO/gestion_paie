/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.controleur;

import ea2i.tpe.calcul.ChangeAndCalcul;
import ea2i.tpe.dao.sqlite.HistoryDAO;
import ea2i.tpe.model.Employee;
import ea2i.tpe.model.History;
import ea2i.tpe.model.Post;
import ea2i.tpe.service.ServiceHistory;
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
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lenovo
 */
public class ControleurPost {
    
private final ViewPost Vp;
private ViewSociety Vs;
private ViewEmployee Ve;
private Post post;
private ControleurEmployee con;
private Employee e;
private final ServicePost Servicepost;
private final DefaultTableModel TabModel;
private List<Post> list;
private final ChangeAndCalcul change;

public ControleurPost(ViewPost Vp,ServicePost Servicepost){
    
    this.Vp = Vp;
    this.Servicepost = Servicepost;
    this.TabModel = (DefaultTableModel) Vp.getTablePost().getModel();
    this.change = new ChangeAndCalcul();
    
    //Ajout des evenements au bouton, tableau et la fenetre
    Vp.getBtnPostAdd().addActionListener(new AddPostLitener());
    Vp.addWindowListener(new ExitListener());
    Vp.getBtnPostReturn().addActionListener(new ReturnPostListener());
    Vp.getBtnPostList().addActionListener(new ShowListPostListener());
    Vp.getTablePost().addMouseListener(new SelectTablePost());
    Vp.getBtnPostUpdate().addActionListener(new UpdatePostListener());
    Vp.getBtnPostCancel().addActionListener(new CancelListener());
    Vp.getBtnPostDelete().addActionListener(new DeletePostListener());
    Vp.getBtnPostSearch().addActionListener(new SearchListener());
}


public class ReturnPostListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            //Revenir à la fenetre principal
            Vs = new ViewSociety();
            ControleurSociety cons = new ControleurSociety(Vs);
            Vs.setVisible(true);
            Vp.setVisible(false);
        }
    
}

//Vider le champ Departement et Poste
public void cancel(){
    
    Vp.getTxtPostNom().setText("");
    Vp.getTxtPostDepartement().setText("");
    
}

public class CancelListener  implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
           
            cancel();
            
        }
    
}

public class DeletePostListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
             if(Vp.getTablePost().getSelectedRowCount() == 1){
              
              String departement = Vp.getTxtPostDepartement().getText();
              String Nompost = Vp.getTxtPostNom().getText();
             
                        //Création d'objet Post
                        Post post = new Post(departement, Nompost);
                        
                        //Supprimer dans le base de donné à l'aide de service
                        Servicepost.Delete(post);
                         cancel();
                   
                        JOptionPane.showMessageDialog(null,"Poste supprimer!", "Suppréssion", WIDTH); 
                        
                        //Suppression dans le tableau
                        TabModel.removeRow(Vp.getTablePost().getSelectedRow()); 
                        cancel();
                        
                        //Ajout au historique
                        HistoryDAO dao = new HistoryDAO();
                        ServiceHistory service = new ServiceHistory(dao);
                
                        History h = new History("Suppréssion de poste:"+Nompost+" dans le champ Poste",change.NowHour(),change.NowMinute(),change.NowDate());
                        service.addHistory(h);
                        
                     }
                     else{
                        JOptionPane.showMessageDialog(null,"Selectionnez le poste que vous voulez supprimez!", "Erreur", JOptionPane.ERROR_MESSAGE);  
                     }
                  
              }
           
    
}

public class UpdatePostListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
           
            if(Vp.getTablePost().getSelectedRowCount() > 0){
                
                  if(Vp.getTxtPostDepartement().getText().isEmpty()||Vp.getTxtPostNom().getText().isEmpty()){
                  
                    JOptionPane.showMessageDialog(null,"Complétez le vid et réssayez!", "Erreur", JOptionPane.ERROR_MESSAGE);
                  
                  }
                  else{
                   
                   String departement = Vp.getTxtPostDepartement().getText();
                   String postName = Vp.getTxtPostNom().getText();
                      
                   Servicepost.UpdatePost(new Post(departement,postName));
                   TabModel.addRow(new Object[]{departement,postName});
                   JOptionPane.showMessageDialog(null,"Poste Modifié", "Modification", WIDTH);
                   TabModel.removeRow(Vp.getTablePost().getSelectedRow());
                   cancel();
              
                    HistoryDAO dao = new HistoryDAO();
                    ServiceHistory service = new ServiceHistory(dao);
                
                    History h = new History("Modification de poste:"+postName+" dans le champ Poste",change.NowHour(),change.NowMinute(),change.NowDate());
                    service.addHistory(h);
                 
                 }
              
             
            }
            else{
              JOptionPane.showMessageDialog(null,"Selectionnez le poste que vous voulez modifier!", "Erreur", JOptionPane.ERROR_MESSAGE);  
            }
            
        }
    
}

public class SearchListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            String element = Vp.getTxtPostElementSearch().getText();
            if(element.isEmpty()){
               JOptionPane.showMessageDialog(null,"Insérez l'élement pour rechercher!", "Erreur", JOptionPane.ERROR_MESSAGE);   
            }
            else{
               list = Servicepost.getPost(element);
               TabModel.setRowCount(0);
               for(Post p:list){
                  TabModel.addRow(new Object[]{p.getDepartement(),p.getNamePost()}); 
               }
               
                HistoryDAO dao = new HistoryDAO();
                ServiceHistory service = new ServiceHistory(dao);
                
                History h = new History("recherche: "+element+" dans le champ Poste",change.NowHour(),change.NowMinute(),change.NowDate());
                service.addHistory(h);
               
               Vp.getTxtPostElementSearch().setText("");
            }
            
        }
    
}

public class AddPostLitener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
        
              String departement = Vp.getTxtPostDepartement().getText();
              String Nompost = Vp.getTxtPostNom().getText();
              if(Nompost.isEmpty()||departement.isEmpty()){
                  
                  JOptionPane.showMessageDialog(null,"Completez et réssayez!", "Ajout", JOptionPane.ERROR_MESSAGE);
                  
              }
              else{
                
                   post = new Post(departement, Nompost);
                   Servicepost.AddPost(post);
                   TabModel.setRowCount(0);
                   TabModel.addRow(new Object[]{post.getDepartement(),post.getNamePost()});
                  JOptionPane.showMessageDialog(null,"Enregistré", "Nouveau Poste", WIDTH);
                  cancel();
                  
                   HistoryDAO dao = new HistoryDAO();
                   ServiceHistory service = new ServiceHistory(dao);
                
                   History h = new History("Ajout de: "+Nompost+" dans le champ Poste",change.NowHour(),change.NowMinute(),change.NowDate());
                   service.addHistory(h);
                  
              }      
        
      }
}

public class ShowListPostListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            list = Servicepost.ListPost();
            TabModel.setRowCount(0);
            for(int i=0; i<list.size();i++){
             
                TabModel.addRow(new Object[]{list.get(i).getDepartement(),list.get(i).getNamePost()});
               
            }
            
             HistoryDAO dao = new HistoryDAO();
             ServiceHistory service = new ServiceHistory(dao);
                
             History h = new History("Ouverture Liste de poste",change.NowHour(),change.NowMinute(),change.NowDate());
             service.addHistory(h);
            
        }
    
}

public class SelectTablePost implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent me) {
           
           String Department = TabModel.getValueAt(Vp.getTablePost().getSelectedRow(), 0).toString();
           String NamePost = TabModel.getValueAt(Vp.getTablePost().getSelectedRow(), 1).toString();
           
            Vp.getTxtPostNom().setText(NamePost);
            Vp.getTxtPostDepartement().setText(Department);
            
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
         
        }

        @Override
        public void windowClosing(WindowEvent we) {
            Vp.setDefaultCloseOperation(WIDTH);
            Vs = new ViewSociety();
            ControleurSociety cons = new ControleurSociety(Vs);
            Vs.setVisible(true);
            Vp.setVisible(false);
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
