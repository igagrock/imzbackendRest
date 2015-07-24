package com.wemater.util;


import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.wemater.exception.EvaluateException;

//creation of singleton for creating the Sessionfactory object
public class HibernateUtil {

	
	private static SessionFactory sessionFactory;
	private static final Logger logger = Logger.getLogger(HibernateUtil.class);
	
	
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
			
			  System.out.println("Hibernate serviceRegistry created\n");
			   logger.info( "Hibernate serviceRegistry created");  
			
			    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			
			
		}catch(HibernateException ex)
		{
			logger.error("intitial session factory creation failed"+ex);
			if(sessionFactory != null) sessionFactory.close();
			
			throw new EvaluateException(ex);
			
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
	


}
