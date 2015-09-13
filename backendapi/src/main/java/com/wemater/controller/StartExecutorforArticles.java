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

		System.out.println("Inserting anonymous--DONE ");

	}

	public void saveUpdateAnyonymous(SessionUtil su) {
		UserDao ud = new UserDao(su);
		User user = ud.createUser("Anonymous", "nomail@dot.com", "Anonymous", "ndrters123", "The author of this article is no longer avaliable");

		try {
			su.beginSessionWithTransaction();
			su.getSession().saveOrUpdate(user);
			su.CommitCurrentTransaction();

		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			System.out
					.println("Anyonymous already inserted. NO NEED TO INSERT");
		}

	}

}