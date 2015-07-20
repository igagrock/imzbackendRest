package com.wemater.service;

import java.util.Base64;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import com.wemater.dto.User;
import com.wemater.exception.AuthException;
import com.wemater.exception.EvaluateException;
import com.wemater.util.HibernateUtil;
import com.wemater.util.SessionUtil;



public class AuthService {
	
	private final SessionFactory sf;
    private final SessionUtil su;
    
	public AuthService() { 
		this.sf = HibernateUtil.getSessionFactory();
		this.su = new SessionUtil(sf.openSession());
		}  
	
	
	
	public boolean isUserAuthenticated(String authString){
        
	
        byte[] decodedAuthBytes = Base64.getDecoder().decode(authString.getBytes());	
		String[] decodedInfo = new String(decodedAuthBytes).split("\\s+");
		String[] decodedAuthParams = decodedInfo[1].split(":");
	
    	   if (validateUserCredentials(decodedAuthParams[0], decodedAuthParams[1])) {
    		   return true;
	       }
	     	   return false;
	}
	
     private  boolean validateUserCredentials(String username, String password){
	   
	   User AuthUser = null;
	   Boolean isValidationSuccessfull = false;
	
		   try {
			   	su.beginSessionWithTransaction();
			
			   	AuthUser = (User) su.getSession().getNamedQuery("user.IsUserPresent")
		               .setParameter("username", username)
		               .setParameter("password", password)
		               .uniqueResult();
			
			   	su.CommitCurrentTransaction();
			
			   	if(AuthUser == null){
			   		isValidationSuccessfull = false;
			   		throw new AuthException("401", "User credentials are invalid");
			   	}
			 
			   	isValidationSuccessfull =true;
			   
		}catch (HibernateException e) {
		  su.rollBackCurrentTransaction();
		  throw new EvaluateException(e);
	   }
		   return isValidationSuccessfull;
    }	
	

}
