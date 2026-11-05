/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.model;

import java.util.Date;



/**
 *
 * @author iNjaka
 */
public class Employee {
    
    private long id;//clé primaire dans la base
    private String name;
    private String FirstName;
    private Date birthday;
    private String poste;
    private double salaire;

    public Employee() {
    }

    public Employee(long id, String name,String FirstName, Date birthday, String poste, double salaire) {
        
        this.id = id;
        this.name = name;
        this.FirstName = FirstName;
        this.birthday = birthday;
        this.poste = poste;
        this.salaire = salaire;
        
    }

    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    
}
