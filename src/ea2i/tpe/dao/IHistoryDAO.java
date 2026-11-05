/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao;

import ea2i.tpe.model.History;
import java.util.List;

/**
 *
 * @author lenovo
 */
public interface IHistoryDAO {
    
    public void AddHistory(History h);
    public List<History> HistoryList();
    public void deleteHistory();
}
