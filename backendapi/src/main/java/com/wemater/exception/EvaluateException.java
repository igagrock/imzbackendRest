package com.wemater.exception;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;

public class EvaluateException extends HibernateException {


	private static final long serialVersionUID = 1497666190967016492L;
	
	public EvaluateException(Throwable e) {
		super(e);
		 
		  
		 if(e instanceof ObjectNotFoundException)
			 throw new DataNotFoundException("404", "Data Not Found");
			    
		
		else if(e instanceof DataNotFoundException)
			   throw new DataNotFoundException(((DataNotFoundException) e).getErrorcode(), e.getMessage());
		
		else if(e instanceof DataNotInsertedException){
			  System.out.println("here in evaluate exception");
			   throw new DataNotInsertedException(((DataNotInsertedException) e).getErrorcode(),
					   e.getMessage());
		}
		
		else e.printStackTrace();
		  
	}
	
	
	


	
	
	
	

}
