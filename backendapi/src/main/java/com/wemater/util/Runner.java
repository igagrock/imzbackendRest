package com.wemater.util;



import org.hibernate.SessionFactory;


public class Runner {
	

	public static void main(String[] args) {
	 
	  SessionFactory sf = HibernateUtil.getSessionFactory();
	  SessionUtil su = new SessionUtil(sf.openSession());
		
	
		
	}
	
}


	

