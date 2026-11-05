/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.service;

import ea2i.tpe.dao.sqlite.ContratDAO;
import ea2i.tpe.model.Contrat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenovo
 */
public class ServiceContrat {

    //Déclaration de propriété DAO
     private final ContratDAO contratDAO;
    
     //constructeur
    public ServiceContrat(ContratDAO conttratDAO) {
        this.contratDAO = conttratDAO;
    }
    
    //fonction qui ajout dans la base de donné à l'aide de DAO
    public boolean AddContrat(Contrat c){
        return contratDAO.createContrat(c);
    }
    
    //fonction qui modifier dans la base de donné à l'aide DAO
    public boolean UpdateContrat(Contrat c){
        return contratDAO.updateContrat(c);
    }
    
    //fonction qui supprimer dans la base de donné à l'aide de DAO
    public void DeleteContrat(Contrat c){
        contratDAO.deleteContrat(c);
    }
    
    //fonction qui récupere la liste dans la base de donné à l'aide de DAO
    public List<Contrat> ListContrat(){
        List<Contrat> list = new ArrayList<>();
        return list = contratDAO.ListContrat();
    }
    
    //fonction qui recherche tous les elements dans la base de donné à l'aide de DAO
    public List<Contrat> SearchContrat(String element){
        List<Contrat> listSearch = new ArrayList<>();
        return listSearch = contratDAO.readContrat(element);
    }
   
    //fonction qui recherche à l'aide d'ID dans la base de donné
     public List<Contrat> readContrat(long id){
        List<Contrat> list = new ArrayList<>();
        return list = contratDAO.readContrat(id);   
    }
    
}
