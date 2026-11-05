/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao;

import ea2i.tpe.model.Prime;
import java.util.List;

/**
 *
 * @author lenovo
 */
public interface IPrimeDAO {
    //CRUD
    
    public boolean createPrimeEmployee(Prime p);
    public boolean updatePrimeEmployee(Prime p);
    public void deletePrimeEmployee(Prime p);
    public Prime readPrimeEmployee(String elements);
    public double getPrimeMontant(long id);
    public List<Prime> ListPrime();
    public List<Prime> readPrimeEmployee(long id);
    
}
