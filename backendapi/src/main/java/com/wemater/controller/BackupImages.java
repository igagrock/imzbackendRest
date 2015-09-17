package com.wemater.controller;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;

import com.wemater.dto.Article;
import com.wemater.exception.DataNotFoundException;
import com.wemater.exception.EvaluateException;
import com.wemater.util.HibernateUtil;
import com.wemater.util.SessionUtil;

public class BackupImages implements Runnable {

	private SessionUtil su;

	public BackupImages() {
		super();
		this.su = new SessionUtil(HibernateUtil.getSessionFactory().openSession());
		
	}
	
	
	@SuppressWarnings("unchecked")
	private List<Article> UpdateArticleImageUrl(SessionUtil su){
		
		List<Article> articles = null;
		try {
			su.beginSessionWithTransaction();
			articles = su.getSession().createQuery("from Article").list();
			if(articles == null)
				throw new DataNotFoundException("NO articles found");
			
			su.CommitCurrentTransaction();
			
			
		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}
		return articles;
	}


	@Override
	public void run() {
		List<Article>  articles = UpdateArticleImageUrl(su);
		for (Iterator<Article> iterator = articles.iterator(); iterator.hasNext();) {
			Article article = (Article) iterator.next();
			System.out.println(article.getId()+": "+article.getTitle());
		}
	}
	
	
}
