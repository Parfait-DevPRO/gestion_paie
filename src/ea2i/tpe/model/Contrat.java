/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.model;

import java.util.Date;

/**
 *
 * @author lenovo
 */
public class Contrat {
    
     private long id;
     private String ContratType;
     private Date BeginDate;
     private Date EndDate;
     private double salary;
     private boolean statusContrat;

    public Contrat(long id, String ContratType, Date BeginDate, Date EndDate, double salary, boolean statusContrat) {
        
        this.id = id;
        this.ContratType = ContratType;
        this.BeginDate = BeginDate;
        this.EndDate = EndDate;
        this.salary = salary;
        this.statusContrat = statusContrat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContratType() {
        return ContratType;
    }

    public void setContratType(String ContratType) {
        this.ContratType = ContratType;
    }

    public Date getBeginDate() {
        return BeginDate;
    }

    public void setBeginDate(Date BeginDate) {
        this.BeginDate = BeginDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date EndDate) {
        this.EndDate = EndDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean isStatusContrat() {
        return statusContrat;
    }

    public void setStatusContrat(boolean statusContrat) {
        this.statusContrat = statusContrat;
    }
}
