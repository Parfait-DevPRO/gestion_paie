/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.service;

import ea2i.tpe.dao.sqlite.EmployeeDAO;
import ea2i.tpe.model.Employee;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author iNjaka
 */
public class ServiceEmployee {
    
    //Déclaration de proprieté DAO
    private final EmployeeDAO employeeDAO;
    
    //constructeur
    public ServiceEmployee(EmployeeDAO employeeDAO) {
      this.employeeDAO = employeeDAO;
    }
    
   //Tous les fonctions sont en fonction de DAO
    
    //fonction qui ajout dans la base de donné
    public boolean addEmployee(Employee e){
        return employeeDAO.createEmployee(e);
    }
    
    //fonction qui modifier dans la base de donné
    public boolean UpdateEmployee(Employee e){
        return employeeDAO.updateEmployee(e);
    }
    
    //fonction qui supprimer dans la base de donné
    public void DeleteEmployee(Employee e){
        employeeDAO.deleteEmployee(e);
    }
    
    //fonction qui récupere la liste dans la base de donné
    public List<Employee> getListEmployees() {
        List<Employee> ret = new ArrayList();
        return ret = employeeDAO.ListEmployee();
        
    }
    
    //fonction qui recherche tous les elements dans la base de donné
    public List<Employee> getEmployee(String element){  
        List<Employee> ret = new ArrayList();
         ret.add(employeeDAO.readEmployee(element));
         return ret;
    }
    
    //fonction qui recherche l'employé à l'aide d'ID
    public List<Employee> getEmployee(long id){
        return employeeDAO.getEmployee(id);
    }
   
}
