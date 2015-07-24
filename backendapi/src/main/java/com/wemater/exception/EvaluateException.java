package com.wemater.exception;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

public class EvaluateException extends HibernateException {


	private static final long serialVersionUID = 1497666190967016492L;
	
	public EvaluateException(Throwable e) {
		super(e);
		 
		  
		 if(e instanceof ObjectNotFoundException)
			 throw new DataNotFoundException( "Data Not Found");
			    
		
		else if(e instanceof DataNotFoundException)
			   throw new DataNotFoundException( e.getMessage());
		
		else if(e instanceof DataNotInsertedException){
			  System.out.println("here in evaluate exception");
			   throw new DataNotInsertedException( e.getMessage());
		}
		
		else e.printStackTrace();
		  
	}
	
	
	


	
	
	
	

}
