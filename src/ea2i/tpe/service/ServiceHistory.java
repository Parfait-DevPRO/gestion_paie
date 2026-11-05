/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.service;

import ea2i.tpe.dao.sqlite.HistoryDAO;
import ea2i.tpe.model.History;
import java.util.List;

/**
 *
 * @author lenovo
 */
public class ServiceHistory {
    
    //Déclaration de proprieté DAO
    private final HistoryDAO histDAO;
    
    //constructeur
    public ServiceHistory(HistoryDAO historydao){
        this.histDAO = historydao;
    }
    
    //Tous les fonctions sont en fonction de DAO
    
    //fonction qui ajout dans la base de donné
    public void addHistory(History h){
        histDAO.AddHistory(h);
    }
    
    //fonction qui récupere la liste d'historique dans la base de donné
    public List<History> ListHistory(){
        List<History> list = histDAO.HistoryList();
        return list;
    }
    
    //fonction qui supprimer dans la base de donné
    public void deleteHistory(){
        histDAO.deleteHistory();
    }
    
}
