package com.wemater.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.hibernate.HibernateException;

import com.wemater.dao.UserDao;
import com.wemater.dto.User;
import com.wemater.util.HibernateUtil;
import com.wemater.util.SessionUtil;

public class StartExecutorforArticles extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {

		SessionUtil su = new SessionUtil(HibernateUtil.getSessionFactory()
				.openSession());
	

		System.out.println("Inserting anonymous ");

		saveUpdateAnyonymous(su);
		
		System.out.println("Backing Up Images just checking");
		//Util.StartExecutorService(new BackupImages());

	}

	public void saveUpdateAnyonymous(SessionUtil su) {
		UserDao ud = new UserDao(su);
		User user = ud.createUser("Anonymous", "Anomyous@email.com", "Anonymous", "CKBPS0423c", 
				"This article is an Orphan. I am just taking care of it. You can still read it and support it!");
		try {
			su.beginSessionWithTransaction();
			su.getSession().save(user);
			su.CommitCurrentTransaction();
			System.out.println("Inserting anonymous--DONE ");

		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			System.out	.println("Anyonymous already inserted. NO NEED TO INSERT");
		}

	}

}