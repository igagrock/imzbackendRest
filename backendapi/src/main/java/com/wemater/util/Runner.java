package com.wemater.util;



import org.apache.commons.codec.binary.Base64;
import org.hibernate.SessionFactory;


public class Runner {
	

	public static void main(String[] args) {
	 
	  SessionFactory sf = HibernateUtil.getSessionFactory();
	  SessionUtil su = new SessionUtil(sf.openSession());
	
		
	  
		String username = "sammer";
		String password = "inotapass";
		String baseString =username+":"+password;
		byte[] encoded = Base64.decodeBase64(baseString);
		String encodedAuth = "Base "+new String(encoded);
		System.out.println(encodedAuth);
		
		AuthUtil service = new AuthUtil(su);
		
		service.isUserAuthenticated(encodedAuth,"sammer");
	  
	  
	 
	    
	}
	
	
	
	
	
	
}
