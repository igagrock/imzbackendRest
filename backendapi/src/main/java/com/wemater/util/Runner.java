package com.wemater.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;









public class Runner {
	
      
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
	    
	
		SessionFactory sf = HibernateUtil.getSessionFactory();
		SessionUtil su = new SessionUtil(sf.openSession());
		
		try {
			su.beginSessionWithTransaction();
			
			Query q = su.getSession().createQuery("select article.title,article.user.name "
					+ "from Article as article order by article.likes desc");
			
			@SuppressWarnings("unchecked")
			List list = q.setMaxResults(6).list();
			
			for (Iterator  iterator = list.iterator(); iterator.hasNext();) {
			    Object[] str = (Object[]) iterator.next();
			    System.out.println(str[0].toString());
	          System.out.println(str[1].toString());
			}
		
			su.CommitCurrentTransaction();
		} catch (HibernateException e) {
			// TODO: handle exception
			su.rollBackCurrentTransaction();
		}
		
		
	}
	

	
}


	

