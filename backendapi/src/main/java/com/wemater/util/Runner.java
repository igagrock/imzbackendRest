package com.wemater.util;

import java.io.IOException;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;









public class Runner {
	
      
	/**
	 * @param args
	 * @throws IOException 
	 */

	public static void main(String[] args) throws IOException {

		SessionFactory sf = HibernateUtil.getSessionFactory();
		SessionUtil su = new SessionUtil(sf.getCurrentSession());
;
		try {
			
			su.beginSessionWithTransaction();
			
	          
			
			
			su.CommitCurrentTransaction();
		
		
			
		} catch (HibernateException e) {
		
			su.rollBackCurrentTransaction();
		}finally{
			sf.close();
		}
		
		
		
	}
	
}