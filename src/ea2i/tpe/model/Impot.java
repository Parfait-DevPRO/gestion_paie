/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.model;

/**
 *
 * @author lenovo
 */
public class Impot {
    
    private long id;
    private String month;
    private String typesImpot;
    private double tauxImpot;
    private String typesCotisations;
    private double tauxCotisations;
    private double TotalTaux;
    private double TotalMontants;

    public Impot(long id,String month, String typesImpot, double tauxImpot,String typesCotisations,double tauxCotisations,double totTaux,double totMontant) {
        this.id = id;
        this.month = month;
        this.typesImpot = typesImpot;
        this.tauxImpot = tauxImpot;
        this.typesCotisations = typesCotisations;
        this.tauxCotisations = tauxCotisations;
        this.TotalTaux = totTaux;
        this.TotalMontants = totMontant;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypesImpot() {
        return typesImpot;
    }

    public void setTypesImpot(String typeImpot) {
        this.typesImpot = typeImpot;
    }

    public double getTauxImpot() {
        return tauxImpot;
    }

    public void setTauxImpot(double taux) {
        this.tauxImpot = taux;
    }

    public String getTypesCotisations() {
        return typesCotisations;
    }

    public void setTypesCotisations(String typesCotisations) {
        this.typesCotisations = typesCotisations;
    }

    public double getTauxCotisations() {
        return tauxCotisations;
    }

    public void setTauxCotisations(double tauxCotisations) {
        this.tauxCotisations = tauxCotisations;
    }

    public double getTotalTaux() {
        return TotalTaux;
    }

    public void setTotalTaux(double TotalTaux) {
        this.TotalTaux = TotalTaux;
    }

    public double getTotalMontants() {
        return TotalMontants;
    }

    public void setTotalMontants(double TotalMontants) {
        this.TotalMontants = TotalMontants;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
    
}
