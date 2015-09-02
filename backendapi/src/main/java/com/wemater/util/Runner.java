package com.wemater.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.wemater.dto.User;









public class Runner {
	
      
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
	    
	
		//SessionFactory sf = HibernateUtil.getSessionFactory();
	//	SessionUtil su = new SessionUtil(sf.openSession());
	
		String hash =Util.generateMD5Hash("Daksh123");  
		System.out.println(hash);
		
	}
	

	
}


	

