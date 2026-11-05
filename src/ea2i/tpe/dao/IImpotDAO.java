/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao;

import ea2i.tpe.model.Impot;
import java.util.List;

/**
 *
 * @author lenovo
 */
public interface IImpotDAO {
    
    public boolean createImpot(Impot imp);
    public boolean UpdateImpot(Impot imp);
    public void deleteImpot(Impot imp);
    public List<Impot> readImpot(String element);
    public List<Impot> ListImpot();
    public List<Impot> readImpot(long id);
    
}
