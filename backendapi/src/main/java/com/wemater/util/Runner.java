package com.wemater.util;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.wemater.dto.Article;
import com.wemater.service.PublicService;




public class Runner {
	
	
	
	public static void main(String[] args) {
	 
		      
	          
	          final PublicService service = new PublicService();
	          
	          HibernateUtil.StartExecutorService(service);
	          
	  
	        		    
	     ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
	     exec.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				
			  	
			   List<Article> list = PublicService.getLatestArticles();	
			   
			   for (Article ar : list) {
				System.out.println(ar.getTitle());
				System.out.println(ar.getDate());
			}}}, 1, 10, TimeUnit.SECONDS);
        
        
	
	}
	
	
	
	
	
	
}
