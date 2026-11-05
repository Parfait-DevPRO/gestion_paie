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
public class Post {
    
    private String Departement;
    private String NamePost;

    public Post(String Departement, String NamePost) {
        this.Departement = Departement;
        this.NamePost = NamePost;
    }

    public String getDepartement() {
        return Departement;
    }

    public void setDepartement(String Departement) {
        this.Departement = Departement;
    }

    public String getNamePost() {
        return NamePost;
    }

    public void setNamePost(String NamePost) {
        this.NamePost = NamePost;
    }

  
}
