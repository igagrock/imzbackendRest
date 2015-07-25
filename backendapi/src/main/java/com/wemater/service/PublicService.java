package com.wemater.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.hibernate.SessionFactory;

import com.wemater.dao.PublicDao;
import com.wemater.dto.Article;
import com.wemater.modal.ArticleModel;
import com.wemater.modal.Link;
import com.wemater.util.HibernateUtil;
import com.wemater.util.SessionUtil;
import com.wemater.util.Util;

public class PublicService implements Runnable {

	private  final SessionFactory sessionfactory;
    private final SessionUtil su;
    private final PublicDao pd;
	 
	 private static List<Article> LatestArticles  = new ArrayList<Article>();
	 private static List<Article> trendingArticles = new ArrayList<Article>();
	 private static List<Article> quickReadArticles = new ArrayList<Article>();
	 private static List<Article> exploreArticles = new ArrayList<Article>();
	 
	 
	
	public PublicService() {
		this.sessionfactory = HibernateUtil.getSessionFactory();
		this.su = new SessionUtil(sessionfactory.openSession());
		this.pd = new PublicDao(su);
	}

	
	
  
	
	
	public static List<Article> getExploreArticles() {
		return exploreArticles;
	}


	public static void setExploreArticles(List<Article> ExploreArticles) {
	    exploreArticles = ExploreArticles;
	}

	public static List<Article> getQuickReadArticles() {
		return quickReadArticles;
	}

	public static void setQuickReadArticles(List<Article> QuickReadArticles) {
	    quickReadArticles = QuickReadArticles;
	}
	public static List<Article> getLatestArticles() {
		return LatestArticles;
	}


	public static void setLatestArticles(List<Article> latestArticles) {
		LatestArticles = latestArticles;
	}
	
	
	
	
	public static List<Article> getTrendingArticles() {
		return trendingArticles;
	}




	public static void setTrendingArticles(List<Article> trendingArticles) {
		PublicService.trendingArticles = trendingArticles;
	}




	@Override
	public void run() { 
		List<Article> latestArticles = pd.fetchLatestArticles();
		if(Util.IsEmptyOrNull(LatestArticles)) System.out.println("Latest isnull############");
		else setLatestArticles(latestArticles);	
		System.out.println("============checking if getLatest works works============");
		
		for (Iterator<Article> iterator = getLatestArticles().iterator(); iterator
				.hasNext();) {
			Article article = (Article) iterator.next();
			System.out.println(article.getTitle());
		}
 		
	    setTrendingArticles(pd.fetchTrendingArticles());	
	    setQuickReadArticles(pd.fetchQuickReadArticles());
	    setExploreArticles(pd.fetchExploreArticles());
	}

	
	public List<ArticleModel> getLatestArticleModels(UriInfo uriInfo){
		      System.out.println("articles found from list");
		      System.out.println("============checking if getLatest works works inside model gets============");
				
				for (Iterator<Article> iterator = getLatestArticles().iterator(); iterator
						.hasNext();) {
					Article article = (Article) iterator.next();
					System.out.println(article.getTitle());
				}  
		      
		   return transformArticlesToModels(getLatestArticles(), uriInfo);
		
		
	}
	
	public List<ArticleModel> getTrendingArticleModels(UriInfo uriInfo){
	      System.out.println("articles found from list");
	   return transformArticlesToModels(getTrendingArticles(), uriInfo);
	
	
	}

	public List<ArticleModel> getQuickReadArticleModels(UriInfo uriInfo){
	      System.out.println("articles found from list");
	   return transformArticlesToModels(getQuickReadArticles(), uriInfo);
	
	}
	
	//do tomarrow and use the map for it to get all the articles or 
	//think again about how do you want to get articles in the list
	// DO IT AGAIN --again
	public List<ArticleModel> getExploreArticleModels(UriInfo uriInfo, int start){
	        
		    System.out.println("articles found from list");
		    	List<Article> articles = null;
		        int SIZE = 5;
	        
			 articles = getExploreArticles();
			         
		     if(start > SIZE)
			 articles = pd.fetchAgainExploreArticles(start);
	        
		     
	       return transformArticlesToModels(articles, uriInfo);
	
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
