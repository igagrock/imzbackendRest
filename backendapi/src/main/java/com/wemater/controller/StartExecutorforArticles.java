package com.wemater.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.wemater.dao.UserDao;
import com.wemater.dto.User;
import com.wemater.util.HibernateUtil;
import com.wemater.util.ImageBackupUtil;
import com.wemater.util.SessionUtil;
import com.wemater.util.Util;

public class StartExecutorforArticles extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(StartExecutorforArticles.class);

	@Override
	public void init() throws ServletException {

		SessionUtil su = new SessionUtil(HibernateUtil.getSessionFactory()
				.openSession());

	
		log.info("inserting anonymous--STARTED");

		saveUpdateAnyonymous(su);

		log.info("BackUP images service started");
		Util.StartExecutorService(new ImageBackupUtil());

	}

	public void saveUpdateAnyonymous(SessionUtil su) {
		UserDao ud = new UserDao(su);
		User user = ud.createUser("Anonymous", "Anomyous@email.com", "Anonymous", "CKBPS0423c", 
				"This article is an Orphan. I am just taking care of it. You can still read it and support it!");
		try {
			su.beginSessionWithTransaction();
			su.getSession().save(user);
			su.CommitCurrentTransaction();
		    log.info("Inserting anonymous--DONE");
		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			log.info("Anyonymous already inserted. NO NEED TO INSERT");
		}

	}

}