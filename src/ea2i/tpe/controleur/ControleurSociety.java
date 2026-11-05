/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.controleur;

import ea2i.tpe.dao.sqlite.ComptableDAO;
import ea2i.tpe.dao.sqlite.ContratDAO;
import ea2i.tpe.dao.sqlite.EmployeeDAO;
import ea2i.tpe.dao.sqlite.HistoryDAO;
import ea2i.tpe.dao.sqlite.ImpotDAO;
import ea2i.tpe.dao.sqlite.PostDAO;
import ea2i.tpe.dao.sqlite.PrimeDAO;
import ea2i.tpe.service.ServiceComptable;
import ea2i.tpe.service.ServiceContrat;
import ea2i.tpe.service.ServiceEmployee;
import ea2i.tpe.service.ServiceHistory;
import ea2i.tpe.service.ServiceImpot;
import ea2i.tpe.service.ServicePost;
import ea2i.tpe.service.ServicePrime;
import ea2i.tpe.view.FullList;
import ea2i.tpe.view.ViewBulletin;
import ea2i.tpe.view.ViewComptable;
import ea2i.tpe.view.ViewContrat;
import ea2i.tpe.view.ViewEmployee;
import ea2i.tpe.view.ViewHistory;
import ea2i.tpe.view.ViewImpot;
import ea2i.tpe.view.ViewPost;
import ea2i.tpe.view.ViewPrime;
import ea2i.tpe.view.ViewSociety;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 *
 * @author lenovo
 */

//Class qui peut afficher le champ Employé,Contrat,Poste,Impôt,Prime,ListeComplet,Comptable et Bulletin à l'aide de bouton
public class ControleurSociety {
    
    //Declaration de proprieté
    private final ViewSociety Vs;
    private ViewPost Vp;
    private ViewComptable comp;
    private ViewContrat Vc;
    private ViewEmployee Ve;
    private ViewImpot Vi;
    private ViewPrime Vprime;
    private ViewHistory Vh;
    private FullList list;
    private PostDAO postDAO;
    private ServicePost servicepost;

    public ControleurSociety(ViewSociety Vs) {
        
        //initialisation de variable d'instance
        this.Vs = Vs;
        //Evenements au bouton à l'aide de class interne
      Vs.getBtnSocietyEmployee().addActionListener(new EmployeeListener());
      Vs.getBtnSocietyNewEmployee().addActionListener(new NewEmployeeListener());
      Vs.getBtnSocietyPost().addActionListener(new PostEmployeeListener());
      Vs.getBtnSocietyNewPost().addActionListener(new NewPostListener());
      Vs.getBtnSocietyContrat().addActionListener(new ContratListener());
      Vs.getBtnSocietyNewContrat().addActionListener(new NewContratListener());
      Vs.getBtnSocietyExit().addActionListener(new ExitListener());
      Vs.getBtnSocietyNewImpot().addActionListener(new NewImpotListener());
      Vs.getBtnSocietyImpot().addActionListener(new ImpotListener());
      Vs.getBtnSocietyPrime().addActionListener(new PrimeListener());
      Vs.getBtnSocietyNewPrime().addActionListener(new NewPrimeListener());
      Vs.getBtnListSociety().addActionListener(new FullListListener());
      Vs.getBtnSocietyComptable().addActionListener(new ExerciceListener());
      Vs.getBtnSocietyBulletin().addActionListener(new BulletinPaieListener());
      Vs.addWindowListener(new WindowExitListener());
      Vs.getBtnHistory().addActionListener(new HistoryListener());
    }

  
    //Action de bouton quitter la fenêtre
    public class ExitListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
           int choose = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment quitter?", "Quitter", JOptionPane.YES_NO_OPTION);
              if(choose == JOptionPane.YES_NO_OPTION){
                   System.exit(0);
                   Vs.dispose();
              }
           
            
        }
        
    }
    
    public class HistoryListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
           //Ouverture de la fenêtre Historique
           Vh = new ViewHistory();
           HistoryDAO dao = new HistoryDAO();
           ServiceHistory service = new ServiceHistory(dao);
           ControleurHistory con = new ControleurHistory(Vh, service);
           Vh.setVisible(true);
           Vs.setVisible(false);
        }
        
    }
    
    public class FullListListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            //Action qui ouvre la fenêtre ListeComplet
             list = new FullList();
            ControleurList contl = new ControleurList(list);
            list.setVisible(true);
            Vs.setVisible(false);
        }
        
    }
    
    //Action de champ Comptable/Exercice
    public class ExerciceListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            //Ouverture de la fenêtre Comptable
            comp = new ViewComptable();
            ComptableDAO cDAO = new ComptableDAO();
            ServiceComptable serviceC = new ServiceComptable(cDAO);
            ControleurComptable contComp = new ControleurComptable(comp,serviceC);
            comp.setVisible(true);
            Vs.setVisible(false);
            
        }
        
    }
    
    //Action de champ Poste
    public class PostEmployeeListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
       
            //Ouverture de champ Poste
            Vp = new ViewPost();
            postDAO = new PostDAO();
            servicepost = new ServicePost(postDAO);
            ControleurPost contp = new ControleurPost(Vp, servicepost);
            Vp.getBtnPostAdd().setVisible(false);
            Vp.setVisible(true);
            Vs.setVisible(false);
        }
        
    }

    //Action de nouveau Poste
public class NewPostListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
              Vp = new ViewPost();
            postDAO = new PostDAO();
            servicepost = new ServicePost(postDAO);
            ControleurPost contp = new ControleurPost(Vp, servicepost);
            Vp.getTxtTitlePost().setText("NOUVEAU POSTE");//Modification de titre
            
            //Bouton invisible quand l'utilisateur ouvrir la fenêtre nouveau Poste
            Vp.getBtnPostList().setVisible(false);
            Vp.getBtnPostUpdate().setVisible(false);
            Vp.getBtnPostSearch().setVisible(false);
            Vp.getTxtPostElementSearch().setVisible(false);
            Vp.setVisible(true);
            Vs.setVisible(false);
                  
            
        }
    
}    
  
//Action de champ contrat
public class ContratListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            //Ouverture de champ contrat
              Vc = new ViewContrat();
              ContratDAO eDAO = new ContratDAO();
              ServiceContrat serviceContrat = new ServiceContrat(eDAO);
              ControleurContrat contE = new ControleurContrat(Vc, serviceContrat);
              Vc.getBtnAddContrat().setVisible(false);
              Vc.setVisible(true);
              Vs.setVisible(false);
            
        }
    
}

//Action de nouvaeau Contrat
public class NewContratListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
              Vc = new ViewContrat();
              ContratDAO eDAO = new ContratDAO();
              ServiceContrat serviceContrat = new ServiceContrat(eDAO);
              ControleurContrat contE = new ControleurContrat(Vc, serviceContrat);
              Vc.getTxtTitleContrat().setText("NOUVEAU CONTRAT");//Modification de titre
              
              //Bouton invisible quand l'utilisateur ouvrir le nouveau contrat
              Vc.getBtnListContrat().setVisible(false);
              Vc.getBtnUpdateContrat().setVisible(false);
              Vc.getBtnContratSearch().setVisible(false);
              Vc.getTxtElementsSearch().setVisible(false);
              Vc.setVisible(true);
              Vs.setVisible(false);
            
        }
    
}

//Action de champ Employé
 public class EmployeeListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
               
             //Ouverture de champ Employé
              Ve = new ViewEmployee();
              EmployeeDAO eDAO = new EmployeeDAO();
              ServiceEmployee Se = new ServiceEmployee(eDAO);
              ControleurEmployee contE = new ControleurEmployee(Ve, Se);
              Ve.getBtnAddEmployee().setVisible(false);
              Ve.setVisible(true);
              Vs.setVisible(false);
           
              
        }
        
    }
 
 //Action de champ nouveau Employé
  public class NewEmployeeListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
              Ve = new ViewEmployee();
              EmployeeDAO eDAO = new EmployeeDAO();
              ServiceEmployee Se = new ServiceEmployee(eDAO);
              ControleurEmployee contE = new ControleurEmployee(Ve, Se);
              Ve.getTxtTitleEmployee().setText("NOUVEAU EMPLOYE");//Modification de titre
              
              //Bouton invisible quand l'utilisateur ouvrir le nouveau Employé
              Ve.getBtnListEmployee().setVisible(false);
              Ve.getBtnUpdateEmployee().setVisible(false);
              Ve.getBtnSearchEmployee().setVisible(false);
              Ve.getTxtElementSearch().setVisible(false);
              Ve.setVisible(true);
              Vs.setVisible(false);
               
              
        }
  }
  
  //Action de champ Impôt
  public class ImpotListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
              
            //Ouverture de champ Impôt
              Vi = new ViewImpot();
              ImpotDAO impDAO = new ImpotDAO();
              ServiceImpot SImp = new ServiceImpot(impDAO);
              ControleurImpot contE = new ControleurImpot(Vi, SImp);
              Vi.getBtnAddImpot().setVisible(false);
              Vi.setVisible(true);
              Vs.setVisible(false);
           
              
        }
        
    }
 
  //Action de nouveau Impôt
  public class NewImpotListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
              Vi = new ViewImpot();
              ImpotDAO impDAO = new ImpotDAO();
              ServiceImpot SImp = new ServiceImpot(impDAO);
              ControleurImpot contE = new ControleurImpot(Vi, SImp);
              Vi.getTxtTitleImpot().setText("NOUVEAU IMPOT ET COTISATION");//Modification de titre
              
              //Bouton invisible quand l'utilisateur ouvrir le nouveau Impôt
              Vi.getBtnListImpot().setVisible(false);
              Vi.getBtnUpdateImpot().setVisible(false);
              Vi.getBtnSearchImpot().setVisible(false);
              Vi.getTxtSearchImpot().setVisible(false);
              Vi.setVisible(true);
              Vs.setVisible(false);
               
              
        }
  }
  
  //Action pour ouvrir Prime
  public class PrimeListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
           
            //Ouverture de la fenêtre Prime
              Vprime = new ViewPrime();
              PrimeDAO primeDAO = new PrimeDAO();
              ServicePrime Sprime = new ServicePrime(primeDAO);
              ControleurPrime contP = new ControleurPrime(Vprime, Sprime);
              Vprime.getBtnAddPrime().setVisible(false);
              Vprime.setVisible(true);
              Vs.setVisible(false);
            
        }
      
  }
  
  //Action pour ouvrir le Nouveau Prime
  public class NewPrimeListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
              Vprime = new ViewPrime();
              PrimeDAO primeDAO = new PrimeDAO();
              ServicePrime Sprime = new ServicePrime(primeDAO);
              ControleurPrime contP = new ControleurPrime(Vprime, Sprime);
              Vprime.getTxtTitlePrime().setText("NOUVEAU PRIME"); //Modificaton de titre
              
              //Bouton invisible quand l'utilisateur ouvrir le nouveau Prime
              Vprime.getBtnListPrime().setVisible(false);
              Vprime.getBtnUpdatePrime().setVisible(false);
              Vprime.getBtnSearchPrime().setVisible(false);
              Vprime.getTxtSearchPrime().setVisible(false);
              Vprime.setVisible(true);
              Vs.setVisible(false);
            
        }
      
  }
  
  //Action pour ouvrir le bulletin
  public class BulletinPaieListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            ViewBulletin vb = new ViewBulletin();
            BulletinPDF bulletin = new BulletinPDF(vb);
            vb.setVisible(true);
            Vs.setVisible(false);
            
        }
      
  }
  
  
  
  
   
        public class WindowExitListener implements WindowListener{

            @Override
            public void windowOpened(WindowEvent we) {
                
            }

            @Override
            public void windowClosing(WindowEvent we) {
               
                //Fermeture de la fenetre principal
                 Vs.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                int choose = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment quitter?", "Quitter", JOptionPane.YES_NO_OPTION);
              if(choose == JOptionPane.YES_NO_OPTION){
                   System.exit(0);
                   Vs.dispose();
              }
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
