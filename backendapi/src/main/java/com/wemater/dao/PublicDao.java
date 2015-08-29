package com.wemater.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;

import com.wemater.dto.Article;
import com.wemater.exception.DataNotFoundException;
import com.wemater.exception.EvaluateException;
import com.wemater.util.SessionUtil;
import com.wemater.util.Util;

public class PublicDao {

	private final SessionUtil su;

	// inject sessionUtil object at the runtime to use the session
	public PublicDao(SessionUtil sessionUtil) {
		this.su = sessionUtil;
	}

	public SessionUtil getSessionUtil() throws InstantiationException {

		if (su == null)
			throw new InstantiationException(
					"SessionUtil has not been set on DAO before usage");
		return su;
	}

	@SuppressWarnings({ "unchecked" })
	public List<Article> fetchLatestArticles() {

		List<Article> articleList = null;
		try {
			su.beginSessionWithTransaction();

			articleList = su
					.getSession()
					.createQuery(
							"from Article as article order by article.date desc")
					.setMaxResults(10)
					.setCacheable(true)
					.list();

			su.CommitCurrentTransaction();

			for (Iterator<Article> iterator = articleList.iterator(); iterator
					.hasNext();) {
				Article article = (Article) iterator.next();
				System.out.println(article.getId());
			}

		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			throw new EvaluateException(e);

		}
		System.out.println("CHECKING:   articleList is null?"
				+ Util.IsEmptyOrNull(articleList));
		return articleList;
	}

	@SuppressWarnings({ "unchecked" })
	public List<Article> fetchTrendingArticles() {

		List<Article> articleList = null;
		try {
			su.beginSessionWithTransaction();

			articleList = su
					.getSession()
					.createQuery(
							"from Article as article order by article.likes desc")
					.setMaxResults(6)
					.setCacheable(true)
					.list();

			su.CommitCurrentTransaction();

		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			throw new EvaluateException(e);

		}
		return articleList;
	}
	

	@SuppressWarnings({ "unchecked" })
	public List<Article> fetchQuickReadArticles() {

		List<Article> articleList = null;
		try {
			su.beginSessionWithTransaction();

			articleList = su
					.getSession()
					.createQuery(
							"from Article as article order by article.commentCount desc")
					.setMaxResults(2)
					.setCacheable(true)
					.list();

			su.CommitCurrentTransaction();

		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			throw new EvaluateException(e);

		}
		return articleList;
	}

	// do it again
	@SuppressWarnings({ "unchecked" })
	public List<Article> fetchExploreArticles(int next) {
		System.out.println("fetchexplorearticles .. ");

        int firstResult = next * 3;
        int maxResult =  3;
		List<Article> articleList = null;
	
		try {
			su.beginSessionWithTransaction();

			articleList = su
					.getSession()
					.createQuery("from Article as article order by article.date desc")
					.setFirstResult(firstResult)
					.setMaxResults(maxResult)
					.setCacheable(true)
					.list();

			if (articleList.isEmpty())
				throw new DataNotFoundException("No more articles");
			su.CommitCurrentTransaction();
 
			
   
		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			throw new EvaluateException(e);

		}
		return articleList;
	}



}
