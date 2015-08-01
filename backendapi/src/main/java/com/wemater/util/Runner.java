package com.wemater.util;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.HibernateException;

import com.wemater.dto.User;
import com.wemater.exception.DataNotFoundException;
import com.wemater.service.UserService;







public class Runner {
	

    

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    
		

		String username = "irshsheik11h";
		String password = "irshsheikh@gmail.com";
		String auth = username + ":" + password;

		String encodedauth = new String(Base64.encodeBase64(auth.getBytes()));
       
		System.out.println(encodedauth);
		
		
		
	
		
		
	}
	

	
}


	

