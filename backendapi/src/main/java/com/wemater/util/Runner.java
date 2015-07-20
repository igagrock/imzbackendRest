package com.wemater.util;

import java.util.Base64;

import com.wemater.service.AuthService;


public class Runner {
	

	public static void main(String[] args) {
	 
	
	    /* SessionFactory sf = HibernateUtil.getSessionFactory();
		  SessionUtil su = new SessionUtil(sf.openSession());*/
	
		
	  
		String username = "sammer";
		String password = "inotapass";
		String baseString ="Base "+username+":"+password;
		byte[] encoded = Base64.getEncoder().encode(baseString.getBytes());
		
		String encodedinfo = new String(encoded);
		
		System.out.println(encodedinfo);
		AuthService service = new AuthService();
		
		service.isUserAuthenticated(encodedinfo);
		
		  
	 
	    
	}
	
	
	
	
	
	
}
