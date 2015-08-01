package com.wemater.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;

import com.wemater.dto.Article;
import com.wemater.dto.Comment;
import com.wemater.dto.User;
import com.wemater.exception.DataNotFoundException;
import com.wemater.exception.DataNotInsertedException;
import com.wemater.exception.EvaluateException;
import com.wemater.exception.ValueNotProvidedException;
import com.wemater.modal.UserModel;
import com.wemater.util.AuthUtil;
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
			throw new InstantiationException(
					"SessionUtil has not been set on DAO before usage");
		return sessionUtil;
	}

	public User createUser(String username, String email, String name,
			String password, String bio) {

		User user = new User();
		user.setUsername(Util.removeSpaces(username));
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setBio(bio);
		return user;
	}

	public User createUser(UserModel model) {

		model = validateUserModel(model);// validate the usermodel for null
											// values and update the model
		User user = new User();
		user.setUsername(Util.removeSpaces(model.getUsername()));
		user.setName(model.getName());
		user.setEmail(model.getEmail());
		user.setPassword(model.getPassword());
		user.setBio(model.getBio());
		return user;
	}

	// validate the values of usermodel
	public  UserModel validateUserModel(UserModel model) {


		if (Util.IsEmptyOrNull(model.getUsername()))
			throw new ValueNotProvidedException("Username",
					"Username is required");

		if (Util.IsEmptyOrNull(model.getName()))
			model.setName("");

		if (Util.IsEmptyOrNull(model.getPassword()))
			throw new ValueNotProvidedException("Password",
					"Password is not provided");
		if (Util.IsEmptyOrNull(model.getEmail()))
			throw new ValueNotProvidedException("Email",
					"Email is not provided");
         if(isExistingEmail(model.getEmail()))
        	 throw new DataNotInsertedException("Email already exits");
		if (Util.IsEmptyOrNull(model.getBio()))
			model.setBio("");
		return model;
	}
	public synchronized User updateValidateUser(User user, UserModel model) {

		if (!Util.IsEmptyOrNull(model.getName()))
			user.setName(model.getName());


		if (!Util.IsEmptyOrNull(model.getPassword())){
			user.setPassword(model.getPassword());
			//remove the authkey from auth map once password is updated
			AuthUtil.removeFromAuthMap(user.getUsername());

		}
		if (!Util.IsEmptyOrNull(model.getPassword())){
			user.setPassword(model.getPassword());
			//remove the existing password key from auth map here so the user is prompted to login again
			AuthUtil.removeFromAuthMap(user.getUsername());
		}
        //if email is not empty or null and it doesnt exist
		if (!Util.IsEmptyOrNull(model.getEmail())){
			if(!isExistingEmail(model.getEmail())){
				user.setEmail(model.getEmail());
			}
			else throw new DataNotInsertedException("Email already exists");
		}
			
		if (!Util.IsEmptyOrNull(model.getBio()))
			user.setBio(model.getBio());

		return user;

	}

	// OPTIMIZE: Update this method to use the HQL queries
	public void removeRefrences(User user) {

		User anyonymous = find("Anonymous");

		try {
			sessionUtil.beginSessionWithTransaction();

			List<Article> articles = user.getArticles();
			List<Comment> comments = user.getComments();
			System.err.println(articles.size());
			System.err.println(comments.size());

			if (!Util.IsEmptyOrNull(articles)) {
				for (Iterator<Article> iterator = articles.iterator(); iterator
						.hasNext();) {
					Article article = (Article) iterator.next();
					article.setUser(anyonymous);
				}
			}

			if (!Util.IsEmptyOrNull(comments)) {
				for (Iterator<Comment> iterator = comments.iterator(); iterator
						.hasNext();) {
					Comment comment = (Comment) iterator.next();
					comment.setUser(anyonymous);
				}
				 }
				
			sessionUtil.CommitCurrentTransaction();
		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}

	}
	
	public boolean isExistingEmail(String email){
		String emailaddress=null;
		try {
			sessionUtil.beginSessionWithTransaction();
			emailaddress = (String) sessionUtil.getSession().getNamedQuery("user.ifUseremailExist")
					                     .setParameter("email", email).uniqueResult();
			
			sessionUtil.CommitCurrentTransaction();
			
			if(emailaddress == null) return false;
			return true;
		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}
	}
	
	public String ifEmailExists(String email){
		String emailaddress=null;
		try {
			sessionUtil.beginSessionWithTransaction();
			emailaddress = (String) sessionUtil.getSession().getNamedQuery("user.ifUseremailExist")
					                     .setParameter("email", email).uniqueResult();
			
			sessionUtil.CommitCurrentTransaction();
			
			if(emailaddress == null) throw new DataNotFoundException("Email does not exist");
			return emailaddress;
			
			
		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}
	}
	
	//check if username exisits
	public String ifUsernameExists(String username){
		String emailaddress=null;
		try {
			sessionUtil.beginSessionWithTransaction();
			emailaddress = (String) sessionUtil.getSession().getNamedQuery("user.ifUsernameExist")
					                     .setParameter("username", username).uniqueResult();
			
			sessionUtil.CommitCurrentTransaction();
			
			if(emailaddress == null) throw new DataNotFoundException("username does not exist");
			return emailaddress;
			
			
		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}
	}
	
	

}   //commit after merge here
