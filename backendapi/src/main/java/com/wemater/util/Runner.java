package com.wemater.util;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

import com.wemater.dto.Article;




public class Runner {
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
	 
		       //BasicConfigurator.configure();
		       
	          SessionFactory sf = HibernateUtil.getSessionFactory();
	          SessionUtil su = new SessionUtil(sf.openSession());
	          
	        
	          
	  
	        		    
	          
	          try {
	        	  su.beginSessionWithTransaction();
		          
	        	 List<Article> list = su.getSession().createCriteria(Article.class)
	        			 			.setMaxResults(7)
	        			            .addOrder(Order.desc("date"))
	        			            .list();
	        			             
		          su.CommitCurrentTransaction();
	        	  
			} catch (HibernateException e) {
				su.rollBackCurrentTransaction();
				throw e;
				
			}finally{
				 sf.close();
			}
	          
	          
        
        
	
	}
	
	
	
	
	
	
}
