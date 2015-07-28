package com.wemater.util;

import org.hibernate.SessionFactory;

import com.wemater.dao.UserDao;






public class Runner {
	
    static SessionFactory sf = HibernateUtil.getSessionFactory();
    

	public static void main(String[] args) {
	   SessionUtil su = new SessionUtil(sf.openSession());
	   
	    UserDao ud = new UserDao(su);
	    
	    System.out.println(ud.ifUsernameExists("inininin"));
	   
	
		
	}
	
}


	

