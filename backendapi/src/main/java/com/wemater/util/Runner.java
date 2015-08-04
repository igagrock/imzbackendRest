package com.wemater.util;

import org.apache.commons.codec.binary.Base64;







public class Runner {
	

    

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    
		

		String username = "nigerialimits";
		String email = "nigeria@gmail.com";
		String auth = username + ":" + email;

		String encodedauth = new String(Base64.encodeBase64(auth.getBytes()));
       
		System.out.println(encodedauth);
		
		
		
	
		
		
	}
	

	
}


	

