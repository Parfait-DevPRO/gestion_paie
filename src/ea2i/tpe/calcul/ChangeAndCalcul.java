/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.calcul;

import ea2i.tpe.dao.sqlite.ComptableDAO;
import ea2i.tpe.dao.sqlite.ContratDAO;
import ea2i.tpe.dao.sqlite.EmployeeDAO;
import ea2i.tpe.dao.sqlite.ImpotDAO;
import ea2i.tpe.dao.sqlite.PrimeDAO;
import ea2i.tpe.model.Comptable;
import ea2i.tpe.model.Contrat;
import ea2i.tpe.model.Employee;
import ea2i.tpe.model.Impot;
import ea2i.tpe.model.Prime;
import ea2i.tpe.service.ServiceComptable;
import ea2i.tpe.service.ServiceContrat;
import ea2i.tpe.service.ServiceEmployee;
import ea2i.tpe.service.ServiceImpot;
import ea2i.tpe.service.ServicePrime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author lenovo
 */
public class ChangeAndCalcul { //class réutilisable 
        
    private ContratDAO cDAO ;
    private ServiceContrat sc ;
    
    private EmployeeDAO eDAO ;
    private ServiceEmployee se ;
          
    private ImpotDAO iDAO;
    private ServiceImpot si;
          
    private PrimeDAO pDAO;
    private ServicePrime sp;
    
    private ComptableDAO compDAO;
    private ServiceComptable servComp;
    

    public ChangeAndCalcul() {
        
    
    }
    
    // fonction retourner l'heure actuelle
    public int NowHour(){ 
        
        Date date = new Date();
         Calendar cal = Calendar.getInstance();
         cal.setTime(date);
         
         return cal.get(Calendar.HOUR_OF_DAY); 
    }
    
    //fonction retourner le minute actuelle
    public int NowMinute(){ 
        
        Date date = new Date();
         Calendar cal = Calendar.getInstance();
         cal.setTime(date);
                 
         return cal.get(Calendar.MINUTE);
    }
    
    //fonction retourner la date actuelle
    public Date NowDate(){ 
        Date date = new Date();
        return date;
    }
    
    //Convertir en 2chiffre après virgule
    public String TwoNumberAfterComma(double number){
        return String.format("%.2f", number);
    }
    
    // convertir String en type long
    public long ChangeLong(String textID){  
        try{
            return  Long.parseLong(textID);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Erreur:"+e.getMessage()+", Réssayez!", "Erreur", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        
    }
    
    //convertir String en type double
    public double ChangeDouble(String textSalary){ 
        
        try{
            return Double.parseDouble(textSalary);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Erreur:"+e.getMessage()+", Réssayez!", "Erreur", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        
        
    }
    
    //convertir String en type Date de format dd/MM/yyyy (dateString seulement)
    public Date ChangeDateString(String Chainedate){ 
      
        Date trueDate;
        
            try {
                 trueDate = new SimpleDateFormat("dd/MM/yyyy").parse(Chainedate);
                 return trueDate;
            } catch (ParseException ex) {
             JOptionPane.showMessageDialog(null, "Erreur:"+ex.getMessage()+", Réssayez!", "Erreur", JOptionPane.ERROR_MESSAGE);    
             
             return null;
            }
     }
    
    
   //convertir Date en type String de format dd/MM/yyyy 
   public String ChangeDateFormat(Date date){ 
        
       SimpleDateFormat trueDate = new SimpleDateFormat("dd/MM/yyyy");
       if(date != null){
           
         return trueDate.format(date);  
           
       }
       else{
         
          return null;
           
       }
         
   }
   
   //Verification à l'aide de dateDebut et dateFin si vrai ou faux pour le status
   public boolean isActif(Date StartDate, Date EndDate){  
       Date today = new Date();
        return (StartDate.before(today) || StartDate.equals(today)) && (EndDate == null || EndDate.after(today));
   }
   
   
   //convertir de boolean en type String(Actif = true ; Inactif = false)
   public String TextStatus(boolean status){ 
       if(status == true){
         
           return "Actif";
           
       }
       else{
           return "Inactif";
       }
   }
   
   //Calcul salaireBrut
   public double CalculSalaireBrut(double salaireBase, double MontantPrime){
       
       return salaireBase + MontantPrime;
       
   }
   
    //Calcul salaireNet
   public double CalculSalaireNet(double salaireBrut, double TotalMontantImpot){
       
       return salaireBrut - TotalMontantImpot;
       
   }
   
   
   // calcule de taux d'impôt et cotisation
   public double CalculTaux(double SalaireBase,double montant){ 
       
       return (100*montant)/SalaireBase;
   }
   
   
   //calcule montant d'impôt et cotisation
   public double CalculMontant(double salaireBase, double taux){ 
       
       return (taux*salaireBase)/100;
       
   }
   
   // Récuperation des elements d'un employé dans Comptable à l'aide d'ID
   public Comptable SearchInComptable(long id){ 
       
       Comptable comp = null;
       List<Comptable> list = new ArrayList<>();
       
        compDAO = new ComptableDAO();
        servComp = new ServiceComptable(compDAO);
       
       list = servComp.SearchExercice(id);
       for(Comptable ct:list){
           comp = new Comptable(ct.getId(), ct.getFirstName(), ct.getPoste(), ct.getBegin(), ct.getEnd(), ct.getMonth(), ct.getSalaireBase(), ct.getSalaireBrut(), ct.getSalaireNet());
       }
       
      return comp; 
   }
   
   
   // Récuperation d'un employé dans l'Employee à l'aide d'ID
   public Employee SearchInEmployee(long id){ 
       
       Employee e = null;
       List<Employee> list = new ArrayList<>();
       
        eDAO = new EmployeeDAO();
        se = new ServiceEmployee(eDAO);
       
       list = se.getEmployee(id);
       for(Employee E:list){
           e = new Employee(E.getId(),E.getName(),E.getFirstName(),E.getBirthday(),E.getPoste(),E.getSalaire());
       }
       
      return e; 
   }
   
    // Récuperation des elements d'un employé dans Contrat à l'aide d'ID
     public Contrat SearchInContrat(long id){ 
         
          cDAO = new ContratDAO();
          sc = new ServiceContrat(cDAO);
            
            Contrat cont = null;
          
             List<Contrat> list = new ArrayList();
             list = sc.readContrat(id);
             
             for(Contrat c: list){
                  cont = new Contrat(c.getId(),c.getContratType(),c.getBeginDate(),c.getEndDate(),c.getSalary(),c.isStatusContrat());
             }
             
             
          return cont;
        }
        
     // Récuperation des elements d'un employé dans l'Impôt à l'aide d'ID
        public Impot SearchInImpot(long id){
            
         iDAO = new ImpotDAO();
         si = new ServiceImpot(iDAO);
            
            Impot imp = null;
            
             List<Impot> list = new ArrayList();
             list = si.readImpot(id);
             
             for(Impot i: list){
                 imp = new Impot(i.getId(),i.getMonth(),i.getTypesImpot(), i.getTauxImpot(), i.getTypesCotisations(), i.getTauxCotisations(), i.getTotalTaux(), i.getTotalMontants());
             }
             
          return imp;
        }
        
        // Récuperation des elements d'un employé dans Prime à l'aide d'ID
        public Prime SearchInPrime(long id){
            
         pDAO = new PrimeDAO();
         sp = new ServicePrime(pDAO);
            
            Prime p= null;
            
             List<Prime> list = new ArrayList();
             list = sp.readPrime(id);
             
             for(Prime prm: list){
                 p = new Prime(prm.getId(), prm.getTypePrime(), prm.getDateBegin(), prm.getEndDate(), prm.getMontant(), prm.isStatus());
             }
          
           return p; 
        }
        
        //convertir mois de type int en String
        public String convStringMois(int month){
    
            switch(month){
                
                case 0: 
                    
                    return "Janvier";
                
                case 1:
                    
                    return "Février";
                
                case 2:
                    
                    return "Mars";
                    
                case 3:
                    
                    return "Avril";
                    
                case 4:
                    
                    return "Mai";
                    
                case 5:
                    
                    return "Juin";
                    
                case 6:
                    
                    return "Juillet";
                    
                case 7:
                    
                    return "Août";
                    
                case 8:
                    
                    return "Septembre";
                    
                case 9:
                    
                    return "Octobre";
                    
                case 10:
                    
                    return "Novembre";
                    
                case 11:
                    
                    return "Décembre";
            
                default:  
                    
                    return "Empty";   
            }
            
        }
        
       //convertir mois de type String en entier
       public int convIntMois(String month){
    
            switch(month){
                
                case "Janvier": 
                    
                    return 0;
                
                case "Février":
                    
                    return 1;
                
                case "Mars":
                    
                    return 2;
                    
                case "Avril":
                    
                    return 3;
                    
                case "Mai":
                    
                    return 4;
                    
                case "Juin":
                    
                    return 5;
                    
                case "Juillet":
                    
                    return 6;
                    
                case "Août":
                    
                    return 7;
                    
                case "Septembre":
                    
                    return 8;
                    
                case "Octobre":
                    
                    return 9;
                    
                case "Novembre":
                    
                    return 10;
                    
                case "Décembre":
                    
                    return 12;
            
                default:  
                    
                    return -1;   
            }
            
        }
   
}
