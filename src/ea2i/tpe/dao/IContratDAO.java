/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao;

import ea2i.tpe.model.Contrat;
import java.util.List;

/**
 *
 * @author lenovo
 */
public interface IContratDAO {
   //CRUD
    public boolean createContrat(Contrat c);
    public boolean updateContrat(Contrat c);
    public void deleteContrat(Contrat c);
    public List<Contrat> readContrat(String element);
    public List<Contrat> readContrat(long id);
    public List<Contrat> ListContrat();
}
