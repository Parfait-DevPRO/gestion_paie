/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.service;

import ea2i.tpe.dao.sqlite.ImpotDAO;
import ea2i.tpe.model.Impot;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenovo
 */
public class ServiceImpot {
   
    //déclaration de proprieté
    private final ImpotDAO impDAO;
    
    //constructeur
    public ServiceImpot(ImpotDAO impDAO) {
        this.impDAO = impDAO;
    }
    
    //Tous les fonctions sont en fonction de DAO
    
    //fonction qui ajout dans la base de donné
    public boolean addImpot(Impot imp){
        return impDAO.createImpot(imp);
    }
    
    //fonction qui modifier dans la base de donné
    public boolean updateImpot(Impot imp){
        return impDAO.UpdateImpot(imp);
    }
    
    //fonction qui recherche tous les elements dans la base de donné
    public List<Impot> searchImpot(String element){
        List<Impot> list = new ArrayList<>();
        return list = impDAO.readImpot(element);
    }
    
    //fonction qui supprimer dans la base de donné
    public void deleteImpot(Impot imp){
        impDAO.deleteImpot(imp);
    }
    
    ///fonction qui récupere la liste dans la base de donné
    public List<Impot> listImpot(){
        List<Impot> list = new ArrayList<>();
        return list = impDAO.ListImpot();
    }
    
    //fonction qui recherche à l'aide de ID dans la base de donné
     public List<Impot> readImpot(long id){
        List<Impot> list = new ArrayList<>();
        return list = impDAO.readImpot(id);
    }
    
}
