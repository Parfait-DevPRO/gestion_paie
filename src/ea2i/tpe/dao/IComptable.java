/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao;

import ea2i.tpe.model.Comptable;
import java.util.List;

/**
 *
 * @author lenovo
 */
public interface IComptable {
    
    public boolean createExercice(Comptable comp);
    public List<Comptable> ListExercice();
    public List<Comptable> readExercice(long id);
    public void deleteExercice(Comptable comp);
    
}
