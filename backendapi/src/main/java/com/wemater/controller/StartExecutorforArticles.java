package com.wemater.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.wemater.service.PublicService;
import com.wemater.util.HibernateUtil;


public class StartExecutorforArticles extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	@Override
	public void init() throws ServletException {
	
		System.out.println("servlet started here...");
		PublicService task = new PublicService();
		
		HibernateUtil.StartExecutorService(task);
		System.out.println("started the executor service");
		
		
		
		
	}

}
