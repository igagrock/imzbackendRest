package com.wemater.dao;

import com.wemater.dto.User;
import com.wemater.modal.UserModel;
import com.wemater.util.SessionUtil;
import com.wemater.util.Util;

public class UserDao extends GenericDaoImpl<User, Long> {

	private final SessionUtil sessionUtil;
    //inject sessionUtil object at the runtime to use the session
	public UserDao(SessionUtil sessionUtil)
	{
		super(sessionUtil, User.class);
		this.sessionUtil = sessionUtil;
	}


	public SessionUtil getSessionUtil() throws InstantiationException {
		
			if (sessionUtil == null)  
	            throw new InstantiationException("SessionUtil has not been set on DAO before usage");
			return sessionUtil;
	}


	public User createUser(UserModel model) {
	
		model= model.validateUserModel();//validate the usermodel for null values and update the model
		User user = new User();
		user.setUsername(Util.removeSpaces(model.getUsername()));
		user.setName(model.getName());
		user.setEmail(model.getEmail());
		user.setPassword(model.getPassword());
		user.setBio(model.getBio());
		return user;
	}

	  public synchronized User updateValidateUser(User user, UserModel model){
    	  
  	    
  	    if(!Util.IsEmptyOrNull(model.getName()) )
  	    	                   user.setName(model.getName());
  	    
  	    if(!Util.IsEmptyOrNull(model.getPassword()) )
  	    						user.setPassword(model.getPassword());
  	    	
  	    if(!Util.IsEmptyOrNull( model.getEmail()) )
  	    						user.setEmail(model.getEmail());	
  	    	
  	    if(!Util.IsEmptyOrNull(model.getBio() ))
  	    						user.setBio(model.getBio());
  	    
  	    return user;
  	  
    }
	
 
	

}
