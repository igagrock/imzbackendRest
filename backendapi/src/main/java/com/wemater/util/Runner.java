package com.wemater.util;

import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;

import com.wemater.exception.DataNotFoundException;
import com.wemater.exception.EvaluateException;




public class Runner {
	
	
	public static void main(String[] args) {
	/*
	 * 
	 * 
		       //BasicConfigurator.configure();
		       
	          SessionFactory sf = HibernateUtil.getSessionFactory();
	          SessionUtil su = new SessionUtil(sf.openSession());
	          UserDao ud = new UserDao(su);
	          ArticleDao ad = new ArticleDao(su);
	          ArticleService as = new ArticleService();
	        
	          
	          
	          
	         // System.out.println(IsArticleCommentAvailable(22l, 10l, su));
	          
	          String query = "select distinct article "+
	        		     "from User as user inner join user.articles as article "+
	        		     "where article.user.username = 'sammer' and article.id = '8l'";
	          
	          try {
	        	  su.beginSessionWithTransaction();
		          
	        	  List<Article> articles = su.getSession().createQuery(query).list();
	        	  
		          su.CommitCurrentTransaction();
		          
		          for (Article article : articles) {
					System.out.println(article.getTitle());
					System.out.println(article.getId());
				}
		          
	        	  
			} catch (HibernateException e) {
				su.rollBackCurrentTransaction();
				throw e;
				
			}
	          
	          
	          
	          
	        	 sf.close();
	     
	        
	
	          
	          
	 */
	          
     
	
	}
	
	@SuppressWarnings("unchecked")
	public static boolean IsArticleCommentAvailable(Long commentId,long articleId, SessionUtil su){
	
		String query ="select article.id from Article as article"
		   		+ " inner join article.comments as comment on comment.id = :id ";
		
		boolean IsAvailable = false;
		
		   try {
			   
			 su.beginSessionWithTransaction();
			 
			List<Long>  ids = su.getSession().createQuery(query)
								               .setParameter("id", commentId)
								               .list();
			 su.CommitCurrentTransaction();
			 
			 //check if username is available
			 int value = Collections.binarySearch(ids, articleId); 
			 
			 if(value == 0) IsAvailable = true;
			 else throw new DataNotFoundException("404", " comment requested doesnt belong to this article");//throw exception if not present
			   
			   
		} catch (HibernateException e) {
			
			su.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}
		return IsAvailable;
	}
	
}
