package com.wemater.util;

import org.hibernate.SessionFactory;

import com.wemater.dao.ArticleDao;
import com.wemater.dto.Article;




public class Runner {
	
	
	
	public static void main(String[] args) {
	 
		       //BasicConfigurator.configure();
		       
	          SessionFactory sf = HibernateUtil.getSessionFactory();
	          SessionUtil su = new SessionUtil(sf.openSession());
	          ArticleDao ad = new ArticleDao(su);
	          
	       
	          
	          Article article  = ad.find(22l);
	          ad.addLikes(1000, article);
	       
	          
	          
	          sf.close();
	          
	          
	          
	          
	       /*
	        * final PublicService service = new PublicService();
	        * 
	        *    HibernateUtil.StartExecutorService(service);
	          
	  
	        		    
	     ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
	     exec.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				
			  	
			   List<Article> list =PublicService.getQuickReadArticles();	
			   
			   for (Article ar : list) {
				System.out.println(ar.getLikes() +" :: "+ar.getCommentCount());
			
			}}}, 1, 10, TimeUnit.SECONDS);
        
	        */
        
	
	}
	
	
	
	
	
	
}
