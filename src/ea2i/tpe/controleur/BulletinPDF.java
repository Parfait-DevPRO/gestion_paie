/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.controleur;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ea2i.tpe.calcul.ChangeAndCalcul;
import ea2i.tpe.dao.sqlite.HistoryDAO;
import ea2i.tpe.model.Comptable;
import ea2i.tpe.model.Contrat;
import ea2i.tpe.model.Employee;
import ea2i.tpe.model.History;
import ea2i.tpe.model.Impot;
import ea2i.tpe.model.Prime;
import ea2i.tpe.service.ServiceHistory;
import ea2i.tpe.view.ViewBulletin;
import ea2i.tpe.view.ViewHistory;
import ea2i.tpe.view.ViewSociety;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import static java.awt.image.ImageObserver.WIDTH;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import javax.swing.JOptionPane;
import jnafilechooser.api.JnaFileChooser;


/**
 *
 * @author lenovo
 */
public class BulletinPDF {
    
    private ViewSociety Vs;
    private ViewHistory Vh;
    private final ViewBulletin Vb;
    private final ChangeAndCalcul change ;

    public BulletinPDF(ViewBulletin Vb) {
        this.Vb = Vb;
        this.change = new ChangeAndCalcul();
        
        //Ajout au bouton et view  l'evenements qui correspondre chacun
        Vb.getBtnSearchID().addActionListener(new SearchListener());
        Vb.getBtnRetour().addActionListener(new RetourAction());
        Vb.getBtnGeneratePDF().addActionListener(new PDFListener());
        Vb.addWindowListener(new ExitListener());
        
    }
    
    /*
    
     Tous les evenements, j'utilise la class interne pour faire l'action de chacun
    
    */
    public class RetourAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            Vs = new ViewSociety();
            ControleurSociety con = new ControleurSociety(Vs);
            Vs.setVisible(true);
            Vb.dispose();
            
        }
        
    }
    
    
    //recherche d'employé pour créer son bulletin de paie
    public class SearchListener implements ActionListener{
        
        @Override
        public void actionPerformed(ActionEvent ae) {
            
            
            if(Vb.getTxtIDSearch().getText().isEmpty()){
                
                JOptionPane.showMessageDialog(null, "Cherchez l'ID de l'employé que vous generez en PDF!", "ERREUR", JOptionPane.ERROR_MESSAGE);
                
            }
            else{
                
                long id;
               
                  //Verification s'il ya d'erreur pour la convertir en long
                  if(change.ChangeLong(Vb.getTxtIDSearch().getText()) != -1){   
                      id = change.ChangeLong(Vb.getTxtIDSearch().getText());
                      
                      //Recherche des elements d'un employé dans la class ChangeAndCalcul à l'aide d'ID(directe à la base de donné)
                      Employee e = change.SearchInEmployee(id);
                      Contrat c = change.SearchInContrat(id);
                      Impot imp = change.SearchInImpot(id);
                      Prime p = change.SearchInPrime(id);
                      Comptable comp = change.SearchInComptable(id);
                    
                      //Affichage des elements rechercher sur le JTextField
                      Vb.getTxtName().setText(e.getName());
                      Vb.getTxtSecondName().setText(e.getFirstName());
                      Vb.getTxtPost().setText(e.getPoste());
                      Vb.getTxtMonth().setText(imp.getMonth());
                      Vb.getTxtbirthday().setText(change.ChangeDateFormat(e.getBirthday()));
                      Vb.getTxtDateNow().setText(change.ChangeDateFormat(change.NowDate()));
                      Vb.getTauxImpot().setText(String.valueOf(imp.getTauxImpot()));
                      Vb.getTxtMontantPrime().setText(String.valueOf(p.getMontant()));
                      Vb.getTauxcotisa().setText(String.valueOf(imp.getTauxCotisations()));
                      Vb.getTxtTotalMontantImpot().setText(String.valueOf(imp.getTotalMontants()));
                      Vb.getTxtSalaireBase().setText(String.valueOf(comp.getSalaireBase()));
                      Vb.getTxtSalaireBrut().setText(String.valueOf(comp.getSalaireBrut()));
                      Vb.getTxtSalaireNet().setText(String.valueOf(comp.getSalaireNet()));
                      
                      
                  }
                  else{}
                
                
                
            }
            
        }
        
    }
    
    //Action de la création de PDF
    public class PDFListener implements ActionListener{
        
        

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            long id = change.ChangeLong(Vb.getTxtIDSearch().getText());
            
             //Récuperation des elements d'un employé pour faire de condition
             Employee e = change.SearchInEmployee(id);
             Contrat c = change.SearchInContrat(id);
             Impot imp = change.SearchInImpot(id);
             Prime p = change.SearchInPrime(id);
             Comptable comp = change.SearchInComptable(id);
              
            //Verification d'element vide pour pouvoir crée le PDF
            if(e.getName()== null || e.getFirstName()== null || e.getBirthday() == null || e.getPoste()== null || imp.getMonth()==null || imp.getTauxImpot()==0 || imp.getTauxCotisations()==0
               || comp.getSalaireBase()== 0 || comp.getSalaireBrut()==0 || comp.getSalaireNet() == 0){
                          
                JOptionPane.showMessageDialog(null,"Completez dans son champ le vide avant\nde creer le pdf, Ressayez! ", "ERREUR", JOptionPane.ERROR_MESSAGE);  
                          
             }
             else{
                //fonction qui créer le PDF
                CreatePDF();
                
                //Ajout dans l'historique cet action
                HistoryDAO dao = new HistoryDAO();
                ServiceHistory service = new ServiceHistory(dao);
            
                
                History h = new History("Creation du bulletin de "+e.getFirstName(),change.NowHour(),change.NowMinute(),change.NowDate());
                service.addHistory(h);
            }
               
        }
        
    }
    
    
    //création du PDF
    public void CreatePDF(){
        
        String path = null; //initialisation du chémin
        
        Calendar cal = Calendar.getInstance();  
        cal.setTime(change.NowDate());
        
        //Récuperation de mois,jour et anné dans le calendrier cal
        String nowMonth = change.convStringMois(cal.get(Calendar.MONTH));  
        int nowDay = cal.get(Calendar.DAY_OF_MONTH); 
        int nowYear = cal.get(Calendar.YEAR);
        
        //Verification s'il y a d'element vide
       if(!Vb.getTxtName().getText().isEmpty() && !Vb.getTxtSecondName().getText().isEmpty() && !  Vb.getTxtPost().getText().isEmpty() && !Vb.getTxtbirthday().getText().isEmpty() && !Vb.getTauxImpot().getText().isEmpty()
        && ! Vb.getTxtMontantPrime().getText().isEmpty() && !Vb.getTauxcotisa().getText().isEmpty() && !Vb.getTxtTotalMontantImpot().getText().isEmpty()
         && !Vb.getTxtSalaireBase().getText().isEmpty() && !Vb.getTxtSalaireBrut().getText().isEmpty() && !Vb.getTxtSalaireNet().getText().isEmpty() && !Vb.getTxtMonth().getText().isEmpty()){ 
         
           
         //Ouverture de fenetre(Explorateur de fichiers)
         JnaFileChooser viewChooser = new JnaFileChooser();
          
          viewChooser.setMode(JnaFileChooser.Mode.Files); //Ajout de mode File
          viewChooser.setTitle("Emplacement du fichier"); //Titre de fenetre
          viewChooser.setMultiSelectionEnabled(false);  //désactiver les plusieurs selections
          viewChooser.setDefaultFileName("Bulletin de "+Vb.getTxtSecondName().getText()+".pdf"); //Nom du fichier avec son format(ici:.pdf)
            
            /*
              Enregistrement de l'emplacement selectionné de
              type boolean
          
          */
            boolean Result = viewChooser.showSaveDialog(null); 
            
               if(Result){
                   
                   //Récuperation de chemin si Result est vrai
                   path = viewChooser.getSelectedFile().getPath();
                   
                  try {
                
                       /* Création du document PDF */
                      
                       //créer de document
                       Document document = new Document();
                       //Ouverture de document pour faire d'écriture
                       PdfWriter.getInstance(document, new FileOutputStream(path));
                       document.open();
                       
                       //Font de document
                       Font titreFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                       Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

                       //titre de pdf
                       Paragraph titre = new Paragraph("Bulletin de Paie de "+Vb.getTxtSecondName().getText(), titreFont);
                       titre.setAlignment(Element.ALIGN_CENTER);
           
                       //ajout le titre dans le document
                       document.add(titre);

                       document.add(new Paragraph("\n"));
                                                             /* à la ligne */
                       document.add(new Paragraph("\n"));

                       //Ajout les element d'un employé à l'aide de paragraph
                       document.add(new Paragraph("Nom: " + Vb.getTxtName().getText(), normalFont));
                       document.add(new Paragraph("Prénom: " + Vb.getTxtSecondName().getText(), normalFont));
                       document.add(new Paragraph("Poste: " + Vb.getTxtPost().getText(), normalFont));
                       document.add(new Paragraph("Date de naissance: " + Vb.getTxtbirthday().getText(), normalFont));

                       document.add(new Paragraph("\nDétails du Salaire", titreFont));
     
                        //créer de tableau
                       PdfPTable table = new PdfPTable(2); // 2 colonnes
                       
                       //Pourcentage de largeur
                       table.setWidthPercentage(100);
                       //Espace
                       table.setSpacingBefore(10f);

                       // Ajout des cellules au tableau
                       table.addCell(new PdfPCell(new Phrase("Taux Impôt("+Vb.getTxtMonth().getText()+")", normalFont)));
                       table.addCell(new PdfPCell(new Phrase("  "+change.TwoNumberAfterComma(change.ChangeDouble(Vb.getTauxImpot().getText())) + " %", normalFont)));

                       table.addCell(new PdfPCell(new Phrase("Taux de Cotisation("+Vb.getTxtMonth().getText()+")", normalFont)));
                       table.addCell(new PdfPCell(new Phrase("  "+change.TwoNumberAfterComma(change.ChangeDouble(Vb.getTauxcotisa().getText())) + " %", normalFont)));

                       table.addCell(new PdfPCell(new Phrase("Impôts et Cotisations("+Vb.getTxtMonth().getText()+")", normalFont)));
                       table.addCell(new PdfPCell(new Phrase("  "+change.TwoNumberAfterComma(change.ChangeDouble(Vb.getTxtTotalMontantImpot().getText()))+ " Ar", normalFont)));

                       table.addCell(new PdfPCell(new Phrase("Montant de Prime", normalFont)));
                       table.addCell(new PdfPCell(new Phrase("  "+change.TwoNumberAfterComma(change.ChangeDouble(Vb.getTxtMontantPrime().getText())) + " Ar", normalFont)));
            
            
                       table.addCell(new PdfPCell(new Phrase("Salaire Base", normalFont)));
                       table.addCell(new PdfPCell(new Phrase("  "+change.TwoNumberAfterComma(change.ChangeDouble(Vb.getTxtSalaireBase().getText())) + " Ar", normalFont)));

                       table.addCell(new PdfPCell(new Phrase("Salaire Brut", normalFont)));
                       table.addCell(new PdfPCell(new Phrase("  "+change.TwoNumberAfterComma(change.ChangeDouble(Vb.getTxtSalaireBrut().getText())) + " Ar", normalFont)));
            
            
                       table.addCell(new PdfPCell(new Phrase("Salaire Net à payé", normalFont)));
                       table.addCell(new PdfPCell(new Phrase("  "+change.TwoNumberAfterComma(change.ChangeDouble(Vb.getTxtSalaireNet().getText())) + " Ar", normalFont)));
            
                       //Ajout de tableau au document
                       document.add(table);

    
                       Paragraph signature = new Paragraph("\n\nSignature de l'Employé", normalFont);
                       signature.setAlignment(Element.ALIGN_RIGHT);
                       document.add(signature);
            
                       document.add(new Paragraph("\n")); 
            
                       Paragraph Ville = new Paragraph("\n\nFait le "+String.valueOf(nowDay)+" "+nowMonth+" "+String.valueOf(nowYear)+"  à.......................", normalFont);
                       Ville.setAlignment(Element.ALIGN_BOTTOM);
                       document.add(Ville);

                       //Fermeture de document
                       document.close();
                       JOptionPane.showMessageDialog(null, "Création du Bulletin de paie réussi!", "Création", WIDTH);
           
           
                       Vb.getTxtName().setText("");
                       Vb.getTxtSecondName().setText("");
                       Vb.getTxtPost().setText("");
                       Vb.getTxtMonth().setText("");
                       Vb.getTxtbirthday().setText("");
                       Vb.getTxtDateNow().setText("");
                       Vb.getTauxImpot().setText("");
                       Vb.getTxtMontantPrime().setText("");
                       Vb.getTauxcotisa().setText("");
                       Vb.getTxtTotalMontantImpot().setText("");
                       Vb.getTxtSalaireBase().setText("");
                       Vb.getTxtSalaireBrut().setText("");
                       Vb.getTxtSalaireNet().setText("");
                       Vb.getTxtIDSearch().setText("");
           

                   } catch (FileNotFoundException | DocumentException | HeadlessException e) {
                      JOptionPane.showMessageDialog(null, e.getMessage(), "ERREUR", JOptionPane.ERROR_MESSAGE);
                   }
                  
               }
               else{}     
               
       }
       else{
           JOptionPane.showMessageDialog(null, "Completez le vide, Réssayez!", "ERREUR", JOptionPane.ERROR_MESSAGE); 
       }
        
    }
  
    //Action de fermer la fenetre
   public class ExitListener implements WindowListener{

        @Override
        public void windowOpened(WindowEvent we) {
           
        }

        @Override
        public void windowClosing(WindowEvent we) {
           
            Vb.setDefaultCloseOperation(ViewBulletin.DO_NOTHING_ON_CLOSE);
            
            Vs = new ViewSociety();
            ControleurSociety con = new ControleurSociety(Vs);
            Vs.setVisible(true);
            Vb.dispose();
            
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
