/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.service;

import ea2i.tpe.dao.sqlite.ComptableDAO;
import ea2i.tpe.model.Comptable;
import java.util.List;

/**
 *
 * @author lenovo
 */
public class ServiceComptable {
    
    private final ComptableDAO compDAO; //declaration de proprieté

    public ServiceComptable(ComptableDAO compDAO) {
        
        //initialisation de variable d'instance
        this.compDAO = compDAO;
    }
    
    //Fonction qui ajout au base de donné à l'aide de DAO
    public boolean AddExercice(Comptable comp){
        
        return compDAO.createExercice(comp);
        
    }
    
    //Fonction récupère la liste dans la base de donné à l'aide de DAO
    public List<Comptable> listExercice(){
       return compDAO.ListExercice();
    }
    
    //Fonction qui recherche l'ID d'employé dans la base de donn' à l'aide de DAO
    public List<Comptable> SearchExercice(long id){
        return compDAO.readExercice(id);
    }
    
    //Fonction qui supprimer l'Exercice d'un employé
    public void DeleteExercice(Comptable comp){
        compDAO.deleteExercice(comp);
    }
    
}
