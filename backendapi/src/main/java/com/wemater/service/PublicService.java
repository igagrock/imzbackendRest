package com.wemater.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

import com.wemater.dto.Article;
import com.wemater.exception.EvaluateException;
import com.wemater.modal.ArticleModel;
import com.wemater.modal.Link;
import com.wemater.util.HibernateUtil;
import com.wemater.util.SessionUtil;

public class PublicService implements Runnable {

	 private  final SessionFactory sessionfactory;
	 private final SessionUtil su;
	 
	 private static List<Article> LatestArticles = new ArrayList<Article>();
	
	public PublicService() {
		this.sessionfactory = HibernateUtil.getSessionFactory();
		this.su = new SessionUtil(sessionfactory.openSession());
	}

	
	

	public static List<Article> getLatestArticles() {
		return LatestArticles;
	}


	public static void setLatestArticles(List<Article> latestArticles) {
		LatestArticles = latestArticles;
	}
	
	
	@Override
	public void run() { setLatestArticles(fetchLatestArticles()); }


	@SuppressWarnings({ "unchecked" })
    private List<Article> fetchLatestArticles(){
    	
		List<Article> articleList = null;
    	try {
			su.beginSessionWithTransaction();
			
			articleList = su.getSession().createCriteria(Article.class)
		 			.setMaxResults(10)
		            .addOrder(Order.desc("date"))
		            .list();			
			
			
			su.CommitCurrentTransaction();
		
			
		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			throw new EvaluateException(e);
			
		}
		return articleList;
    }
	
	
	
	public List<ArticleModel> getLatestArticleModels(UriInfo uriInfo){
		
		   return transformArticlesToModels(getLatestArticles(), uriInfo);
		
		
	}
	
	
	
	
//service related methods
	
	private List<ArticleModel> transformArticlesToModels(List<Article> articles, UriInfo uriInfo) {  
   	 List<ArticleModel> models = new ArrayList<ArticleModel>();
   	   
   	 for (Iterator<Article> iterator = articles.iterator(); iterator.hasNext();) {
			Article article = (Article) iterator.next();
			models.add( transformArticleToModel(article, uriInfo));
			
		}
   	return models;
   	 
    
    }
	
	
	private ArticleModel transformArticleToModel(Article article, UriInfo uriInfo) {
		
		Link self = LinkService.createLinkForEachArticleOfUser("getAllArticles",
                article.getUser().getUsername(),
                article.getId(), uriInfo,"self");
		Link articles = LinkService.createLinkForAllArticlesOfUser("getAllArticles",
							article.getUser().getUsername(),
							uriInfo, "articles");
		 Link comments = LinkService.createLinkForArticleComments("getAllArticles",
				   "getAllComments",
				  article.getUser().getUsername(),
				  article.getId(),
				  uriInfo, "comments");  
		 Link user = LinkService.CreateLinkForEachUser(article.getUser().getUsername(),
					uriInfo, "user");
		

			
		  ArticleModel model = new ArticleModel()
				               .constructModel(article)
		                       .addCount(article.getCommentCount())
		   					   .addLikes(article.getLikes())
		   					   .addTags(article.getTags())
	              		       .addLinks(self, articles,comments,user);
		
		return model;
	}


	
}
