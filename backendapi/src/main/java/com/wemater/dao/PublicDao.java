package com.wemater.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;

import com.wemater.dto.Article;
import com.wemater.exception.EvaluateException;
import com.wemater.util.SessionUtil;

public class PublicDao {
	
	private final SessionUtil su;
    //inject sessionUtil object at the runtime to use the session
	public PublicDao(SessionUtil sessionUtil) {
				this.su = sessionUtil;
	}


	public SessionUtil getSessionUtil() throws InstantiationException {
		
			if (su == null)  
	            throw new InstantiationException("SessionUtil has not been set on DAO before usage");
			return su;
	}
	
	
	
	
	@SuppressWarnings({ "unchecked" })
    public List<Article> fetchLatestArticles(){
    	
		List<Article> articleList = null;
    	try {
			su.beginSessionWithTransaction();
			
			articleList = su.getSession().createCriteria(Article.class)
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
					.setMaxResults(30)
		            .addOrder(Order.desc("date"))
		            .list();			
			
			
			su.CommitCurrentTransaction();
		
			
		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			throw new EvaluateException(e);
			
		}
		return articleList;
    }
	
	@SuppressWarnings({ "unchecked" })
    public List<Article> fetchTrendingArticles(){
    	
		List<Article> articleList = null;
    	try {
			su.beginSessionWithTransaction();
			
			articleList = su.getSession().createCriteria(Article.class)
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
		 			.addOrder(Order.desc("likes"))
		            .setMaxResults(10)
		 			.list();			
			
			
			su.CommitCurrentTransaction();
		
			
		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			throw new EvaluateException(e);
			
		}
		return articleList;
    }
	
	
	@SuppressWarnings({ "unchecked" })
    public List<Article> fetchQuickReadArticles(){
    	
		List<Article> articleList = null;
    	try {
			su.beginSessionWithTransaction();
			
			articleList = su.getSession().createCriteria(Article.class)
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
		 			.addOrder(Order.desc("likes"))
		 			.addOrder(Order.desc("commentCount"))
		            .setMaxResults(5)
		 			.list();			
			
			
			su.CommitCurrentTransaction();
		
			
		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			throw new EvaluateException(e);
			
		}
		return articleList;
    }

	
	
	@SuppressWarnings({ "unchecked" })
    public List<Article> fetchExploreArticles(int start,int size){
    	
		List<Article> articleList = null;
    	try {
			su.beginSessionWithTransaction();
			
			articleList = su.getSession().createCriteria(Article.class)
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
					.setFirstResult(start)
					.setMaxResults(size*3)
		            .list();			
			
			
			su.CommitCurrentTransaction();
		
			
		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			throw new EvaluateException(e);
			
		}
		return articleList;
    }
	
	

}
