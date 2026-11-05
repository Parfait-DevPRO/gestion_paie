/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.service;

import ea2i.tpe.dao.sqlite.PrimeDAO;
import ea2i.tpe.model.Prime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenovo
 */
public class ServicePrime {

    //déclaration de proprieté DAO
     private final PrimeDAO pDAO;
    
    //constructeur
    public ServicePrime(PrimeDAO pDAO) {
        this.pDAO = pDAO;
    }
    
    //Tous les fonctions sont en fonction de DAO
    
    //fonction qui ajout dans la base de donné
    public boolean addPrime(Prime p){
       return pDAO.createPrimeEmployee(p);
    }
    
    //fonction qui modifier dans la base de donné
    public boolean UpdatePrime(Prime p){
        return pDAO.updatePrimeEmployee(p);
    }
    
    //fonction qui supprimer dans la base de donné
    public void DeletePrime(Prime p){
        pDAO.deletePrimeEmployee(p);
    }
    
    //fonction qui recherche tous les élements dans la base de donné
    public List<Prime> searchInPrime(String element){
        List<Prime> ret = new ArrayList<>();
         ret.add(pDAO.readPrimeEmployee(element));
         return ret;
         
    }
    
    //fonction qui récupere la montant d'un employé dans le Prime à l'aide d'ID(dans la base de donné)
    public double getMontantPrime(long id){
        return pDAO.getPrimeMontant(id);
    }
    
    //fonction qui récupere la liste dans la base de donné
    public List<Prime> ListPrime(){
        List<Prime> ret = new ArrayList<>();
        return ret = pDAO.ListPrime();
    }
    
    //fonction qui recherche le prime d'employé à l'aide d'ID dans la base de donné
    public List<Prime> readPrime(long id){
        List<Prime> ret = new ArrayList<>();
         return pDAO.readPrimeEmployee(id);
         
    }
    
}
