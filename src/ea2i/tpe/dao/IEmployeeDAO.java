/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao;

import ea2i.tpe.model.Employee;
import java.util.List;

/**
 *
 * @author iNjaka
 */
public interface IEmployeeDAO {
    
    //CRUD
    public boolean createEmployee(Employee e);
    public boolean updateEmployee(Employee e);
    public void deleteEmployee(Employee e);
    public Employee readEmployee(String elements);
    public List<Employee> getEmployee(long id);
    public List<Employee> ListEmployee();
}
