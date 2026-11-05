/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ea2i.tpe.dao;

import ea2i.tpe.model.Post;
import java.util.List;

/**
 *
 * @author lenovo
 */
public interface IPostDAO {
    
  public void createPost(Post p);
  public void UpdatePost(Post p);
  public void DeletePost(Post p);
  public List<Post> ReadPost(String element);
  public List<Post> ListPost();
}
