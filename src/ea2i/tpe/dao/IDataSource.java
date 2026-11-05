/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao;

import java.sql.Connection;

/**
 *
 * @author iNjaka
 */
public interface IDataSource {
    public Connection connectionDb();
   
}
