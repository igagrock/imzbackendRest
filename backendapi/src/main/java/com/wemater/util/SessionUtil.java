package com.wemater.util;

import org.hibernate.Session;

public class SessionUtil {

	 private final Session session;

	public SessionUtil(Session session) {
		this.session = session;
	}

	public Session getSession() {
		if (session == null)  
            throw new IllegalStateException("Session has not been set on Sessionutil before usage");
		return session;
	}

	
	public Session beginSessionWithTransaction() {
		session.beginTransaction();
		return session;
	}


	public void CommitCurrentTransaction() {
		session.getTransaction().commit();
		
	}


	public void rollBackCurrentTransaction() {
		session.getTransaction().rollback();
	}

	 
	 
}
