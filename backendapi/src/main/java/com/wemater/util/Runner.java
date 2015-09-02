package com.wemater.util;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;









public class Runner {
	
      
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		String title ="In the world instead and the hate crimes that are responsibe to get awat from the literato";
		
		String hash = Base64.encodeBase64String(title.getBytes());
		
		System.out.println(hash);
		
	}
	

	
}


	

