package com.wemater.util;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.HibernateException;

import com.wemater.dto.User;
import com.wemater.exception.AuthException;
import com.wemater.exception.EvaluateException;



public class AuthUtil {
	
private final SessionUtil su;
    
public AuthUtil(SessionUtil su) { 
	this.su = su;
}  
	
	
	
public boolean isUserAuthenticated(String authString, String username){
	
	if(Util.IsEmptyOrNull(authString)) throw new AuthException("401", "Authentication Required");
        
if (validateUserCredentials(DecodeAuthString(authString),username)) return true;
return false;
}
	
public boolean isUserAuthenticatedGET(String authString){
    
if(Util.IsEmptyOrNull(authString)) throw new AuthException("401", "Authentication Required");
	    
		
if (validateUserCredentials(DecodeAuthString(authString))) return true;
return false;
}
	


private  boolean validateUserCredentials(String decodedAuthString, String username){

      String[] params = getParamArray(decodedAuthString);
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
			
			   	if(AuthUser == null ){ //either username doesnt match or no user present
			   		isValidationSuccessfull = false;
			   		throw new AuthException("401", "User credentials are invalid");
			   	}
			   	if(!username.equals(params[0])){ //either username doesnt match or no user present
			   		isValidationSuccessfull = false;
			   		throw new AuthException("401", "Private Resouce!!");
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
	

private  boolean validateUserCredentials(String decodedAuthString){

    String[] params = getParamArray(decodedAuthString);
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
			
			   	if(AuthUser == null ){ //either username doesnt match or no user present
			   		isValidationSuccessfull = false;
			   		throw new AuthException("401", "User credentials are invalid");
			   	}
			 
				if(AuthUser != null ){ //user present and username matches to current
			   	isValidationSuccessfull =true;
				}
				
		}catch (HibernateException e) {
		  su.rollBackCurrentTransaction();
		  throw new EvaluateException(e);
	   }
		   return isValidationSuccessfull;
  }	
	
 
 private String DecodeAuthString(String authString){
	 
	 String[] decodedAuth = authString.split("\\s+");
	 if(decodedAuth.length != 2 ) throw new AuthException("401", "Invalid User Credentials");
	  byte[] decodedAuthParam = Base64.decodeBase64(decodedAuth[1]);
	  String authparam =  new String(decodedAuthParam);
	  if(Util.IsEmptyOrNull(authparam)) throw new AuthException("401", "Invalid User Credentials");
	  return authparam;
	  
 }
 
 private String[] getParamArray(String authString){
	   String[] params = authString.split(":");
	   if(params.length != 2 ) throw new AuthException("401", "Invalid User Credentials");
	   return params;
 }
 
 public String getLoggedInUser(String authString){
	return getParamArray(DecodeAuthString(authString))[0];
	  
 }


}
