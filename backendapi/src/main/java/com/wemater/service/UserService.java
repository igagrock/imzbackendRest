package com.wemater.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.hibernate.SessionFactory;

import com.wemater.dao.UserDao;
import com.wemater.dto.User;
import com.wemater.modal.Link;
import com.wemater.modal.UserModel;
import com.wemater.util.HibernateUtil;
import com.wemater.util.SessionUtil;



public class UserService {
    private final SessionFactory sessionfactory;
    private final SessionUtil su;
    private final UserDao ud;

   
   
 
	public UserService() {
		this.sessionfactory = HibernateUtil.getSessionFactory();
		this.su = new SessionUtil(sessionfactory.openSession());
		this.ud = new UserDao(su);
	  
	}   
	
	//1: get all users
     public List<UserModel> getAllusers(UriInfo uriInfo){
    	     	 
    	 return transformUsersToModels(ud.findAll(),uriInfo);	
    	 
    	
     }

     //2: get each user
     public UserModel getUser(String profileName,UriInfo uriInfo){
    	 
    	 return transformUserToModel(ud.find(profileName.trim()), uriInfo);
     }
  
    public UserModel postUser(UserModel model, UriInfo uriInfo){ 
    	 
    	     	   
    	   String username = model.getUsername().trim(); //replace the side white spaces
    	   username = username.replaceAll("\\s+","");    //replace extra inside white spaces for one word
    	   model.setUsername(username);
    	      
    	   User user = ud.createUser(model);
    	   Long id= ud.save(user);
   	       user = ud.find(id);
   	      return transformUserToModel(user, uriInfo);
      
     }
        
     
    public UserModel updateUser(String profilename,UserModel model, UriInfo uriInfo){
    	
    	String profTrimmed = profilename.trim();
    	User user = ud.find(profTrimmed); 
    	 
		 user.setBio(model.getBio()); // update the changes here
		 user.setName(model.getName()); //
		 user.setEmail(model.getEmail());//
 
         ud.update(user);  //update the user in the database
    	 user = ud.find(user.getId()); // get the updated user from database

    	return transformUserToModel(user, uriInfo);   //return the model of the updated user
 
  }
    
   public void deleteUser(String profilename){
	      
	      User user = ud.find(profilename);
	      ud.delete(user);
   }   
 
     
     ///USerService for transforming starts here
 
    
     
     //get all users
	private List<UserModel> transformUsersToModels(List<User> users, UriInfo uriInfo) {
		
		 List<UserModel> models = new ArrayList<UserModel>();
		 
		 for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
	
			 User user = iterator.next();
			 UserModel model = transformUserToModel(user, uriInfo);
			  
			 models.add(model);
		}
		
		
		return models;
	}


    // transform 
	private UserModel transformUserToModel(User user, UriInfo uriInfo) {
		
		Link self = LinkService.CreateLinkForEachUser(user.getUsername(), uriInfo,"self");
		Link users = LinkService.createLinkForAllUsers(uriInfo,"users");
		Link articles = LinkService.createLinkForAllArticlesOfUser("getAllArticles", user.getUsername(), uriInfo,"articles");
		Link comments = LinkService.createLinkForUserComments("getComments", user.getUsername(), uriInfo,"comments");
		
			
		UserModel model = new UserModel()
		                  .constructModel(user)
		                  .addId(user.getId())
		                  .addUsername(user.getUsername())
		                  .addEmail(user.getEmail())
		                  .addArticleCount(user.getArticleCount())
		                  .addCommentCount(user.getCommentCount())
	                      .addLinks(self, users,articles,comments);
		return model;
	}

	
	
	
}