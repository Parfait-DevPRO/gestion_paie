/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.service;

import ea2i.tpe.dao.sqlite.PostDAO;
import ea2i.tpe.model.Post;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author lenovo
 */
public class ServicePost {

    //déclaration de proprieté DAO
    private final  PostDAO postDAO;
    
    //constructeur
    public ServicePost(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    //Tous les fonctions sont en fonction de DAO
    
    //fonction qui ajout dans la base de donné
    public void AddPost(Post p){
      postDAO.createPost(p);
    }
    
    //fonction qui modifier dans la base de donné
    public void UpdatePost(Post p){
       postDAO.UpdatePost(p);
    }
    
    //fonction qui supprimer dans la base de donné
    public void Delete(Post p){
        postDAO.DeletePost(p);
    }
    
    //fonction qui recherche tous les elements dans la base de donné
    public List<Post> getPost(String element){
        List<Post> ret = new ArrayList<>();
        return ret=postDAO.ReadPost(element);
    }
   
    //fonction qui récupere la liste dans la base de donné
   public List<Post> ListPost(){
       List<Post> Array = new ArrayList<>();    
       return Array = postDAO.ListPost();
   }
}
