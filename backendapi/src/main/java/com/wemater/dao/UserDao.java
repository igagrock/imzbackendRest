package com.wemater.dao;

import com.wemater.dto.User;
import com.wemater.exception.ValueNotProvidedException;
import com.wemater.modal.UserModel;
import com.wemater.util.HibernateUtil;
import com.wemater.util.SessionUtil;

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

	
 


	public User createUser(String username,String name, String email, String password,
			String bio) {
		User user = new User();
		user.setUsername(username);
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setBio(bio);
		return user;
		
	}

	public User createUser(UserModel model) {
		User user = new User();
		model= model.validateUserModel();//validate the usermodel for null values and update the model
		
		user.setUsername(HibernateUtil.removeSpaces(model.getUsername()));
		user.setName(model.getName());
		user.setEmail(model.getEmail());
		user.setPassword(model.getPassword());
		user.setBio(model.getBio());
		return user;
	}

      
  
	

}
