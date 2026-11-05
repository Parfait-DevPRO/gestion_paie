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
public class Prime {
    
    private long id;
    private String TypePrime;
    private Date dateBegin;
    private Date endDate;
    private double montant;
    private boolean status;

    public Prime(long id, String TypePrime, Date dateBegin, Date endDate, double montant, boolean status) {
        this.id = id;
        this.TypePrime = TypePrime;
        this.dateBegin = dateBegin;
        this.endDate = endDate;
        this.montant = montant;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypePrime() {
        return TypePrime;
    }

    public void setTypePrime(String TypePrime) {
        this.TypePrime = TypePrime;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
}
