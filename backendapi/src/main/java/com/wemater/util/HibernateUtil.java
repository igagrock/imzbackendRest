package com.wemater.util;


import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

//creation of singleton for creating the Sessionfactory object
public class HibernateUtil {

	
	private static SessionFactory sessionFactory;
	private static final Logger logger= Logger.getLogger(HibernateUtil.class);
	
	private static SessionFactory buildSessionFactory()
	{
		try{
			
		     logger.debug("entering build session factory");
			Configuration configuration = new Configuration();
			configuration.configure();
			System.out.println(" Hibernate configuration loaded");
			logger.info("Hibernate configuration loaded");
			
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
											.applySettings(configuration.getProperties()).build();
			
			  //System.out.println("Hibernate serviceRegistry created\n");
			   logger.info( "Hibernate serviceRegistry created");  
			
			    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			
			
		}catch(Exception ex)
		{
			logger.error("intitial session factory creation failed"+ex);
			if(sessionFactory != null) sessionFactory.close();
			
			throw new ExceptionInInitializerError();
			
		}
		return sessionFactory;
	}
	
	//public method to generate the instances of Hibernateutil class
	
	public static SessionFactory getSessionFactory()
	{
		if(sessionFactory == null ){
			
			sessionFactory=buildSessionFactory();
		}
		return sessionFactory;
		
	}
	
	 public static String convertClobToString(Clob clob) throws IOException, SQLException {
         Reader reader = clob.getCharacterStream();
         int c = -1;
         StringBuilder sb = new StringBuilder();
         while((c = reader.read()) != -1) {
              sb.append(((char)c));
         }

         return sb.toString();
  }
	 
	 
		
	public static String getUsernameFromURLforComments(int index,UriInfo uriInfo) {
		     
			   String url = uriInfo.getAbsolutePath().toString();
		   	   String[] tokens = url.split("/");
		       
		   	   return tokens[tokens.length -index];
		       
	}	 
	public static Long getArticleIdFromURLforComments(int index,UriInfo uriInfo) {
	     
		   String url = uriInfo.getAbsolutePath().toString();
	   	   String[] tokens = url.split("/");
	       
	   	   return Long.valueOf(tokens[tokens.length -3]);
	       
}

}
