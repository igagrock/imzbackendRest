package com.wemater.util;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;

import com.wemater.dto.Article;







public class Runner {
	
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
	 
		SessionFactory sf = HibernateUtil.getSessionFactory();
		SessionUtil su = new SessionUtil(sf.openSession());
		
		
		try {
			su.beginSessionWithTransaction();
			
			List<Article> articles = su.getSession()
					 .createQuery("from Article as article order by article.date desc")
		            .list();
			
			su.CommitCurrentTransaction();
			int i =0;
			for (Article article : articles) {
				System.out.println((++i)+" :: "+article.getId()+" :: "+article.getTitle() +" :: "+article.getLikes());
			}
			
		} catch (HibernateException e) {
			
			su.rollBackCurrentTransaction();
			e.printStackTrace();
			
		}finally{sf.close();}
		
		
		
		
		
	      
	 
	    
	}
	
	
	
	
	
	
}
