package com.wemater.service;

import java.util.Base64;

import org.hibernate.HibernateException;

import com.wemater.dto.User;
import com.wemater.exception.AuthException;
import com.wemater.exception.EvaluateException;
import com.wemater.util.SessionUtil;



public class AuthService {
	

    private final SessionUtil su;
    
	public AuthService(SessionUtil su) { 
		
		this.su = su;
		}  
	
	
	
	public boolean isUserAuthenticated(String authString, String username){
        
	    if(authString ==  null) throw new AuthException("401", "Authentication Required");
	    
		String[] decodedAuth = authString.split("\\s+");
		byte[] decodedAuthParam = Base64.getDecoder().decode(decodedAuth[1]);
		String decodedAuthParamString = new String(decodedAuthParam);
		System.out.println(decodedAuthParamString);
		
		
	
    	   if (validateUserCredentials(decodedAuthParamString,username)) {
    		   return true;
	       }
	     	   return false;
	}
	
     private  boolean validateUserCredentials(String decodedAuthString, String username){
	   
    	 
    	 
      String[] params = decodedAuthString.split(":");
      System.out.println(params[0]+" "+params[1]);
    	 
	   User AuthUser = null;
	   Boolean isValidationSuccessfull = false;
	
		   try {
			   	su.beginSessionWithTransaction();
			
			   	AuthUser = (User) su.getSession().getNamedQuery("user.IsUserAvailable")
		               .setParameter("username", params[0])
		               .setParameter("password", params[1])
		               .uniqueResult();
			
			   	su.CommitCurrentTransaction();
			
			   	if(AuthUser == null || !username.equals(params[0])){ //either username doesnt match or no user present
			   		isValidationSuccessfull = false;
			   		throw new AuthException("401", "User credentials are invalid");
			   	}
			 
				if(AuthUser != null && username.equals(params[0])){ //user present and username matches to current
			   	isValidationSuccessfull =true;
				}
				
		}catch (HibernateException e) {
		  su.rollBackCurrentTransaction();
		  throw new EvaluateException(e);
	   }
		   return isValidationSuccessfull;
    }	
	

}
