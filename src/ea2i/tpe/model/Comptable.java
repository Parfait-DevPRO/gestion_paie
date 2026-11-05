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
public class Comptable {
    
    private long id;
    private String firstName;
    private String poste;
    private Date Begin;
    private Date End;
    private String month;
    private double SalaireBase;
    private double salaireBrut;
    private double SalaireNet;

    public Comptable(long id, String firstName, String poste, Date Begin, Date End,String month, double SalaireBase, double salaireBrut, double SalaireNet) {
        this.id = id;
        this.firstName = firstName;
        this.poste = poste;
        this.Begin = Begin;
        this.End = End;
        this.month = month;
        this.SalaireBase = SalaireBase;
        this.salaireBrut = salaireBrut;
        this.SalaireNet = SalaireNet;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public Date getBegin() {
        return Begin;
    }

    public void setBegin(Date Begin) {
        this.Begin = Begin;
    }

    public Date getEnd() {
        return End;
    }

    public void setEnd(Date End) {
        this.End = End;
    }

    public double getSalaireBase() {
        return SalaireBase;
    }

    public void setSalaireBase(double SalaireBase) {
        this.SalaireBase = SalaireBase;
    }

    public double getSalaireBrut() {
        return salaireBrut;
    }

    public void setSalaireBrut(double salaireBrut) {
        this.salaireBrut = salaireBrut;
    }

    public double getSalaireNet() {
        return SalaireNet;
    }

    public void setSalaireNet(double SalaireNet) {
        this.SalaireNet = SalaireNet;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
    
}
