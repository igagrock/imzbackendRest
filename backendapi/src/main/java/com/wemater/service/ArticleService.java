package com.wemater.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.hibernate.SessionFactory;

import com.wemater.dao.ArticleDao;
import com.wemater.dao.UserDao;
import com.wemater.dto.Article;
import com.wemater.dto.User;
import com.wemater.exception.EvaluateException;
import com.wemater.modal.ArticleModel;
import com.wemater.modal.Link;
import com.wemater.util.AuthUtil;
import com.wemater.util.HibernateUtil;
import com.wemater.util.SessionUtil;

public class ArticleService {
 
	    private  final SessionFactory sessionfactory;
	    private final SessionUtil su;
	    private final ArticleDao ad;
	    private final UserDao ud;
	    private final AuthUtil au;
	   
	   
	 
		public ArticleService() {
			this.sessionfactory = HibernateUtil.getSessionFactory();
			this.su = new SessionUtil(sessionfactory.openSession());
			this.ad = new ArticleDao(su);
			this.ud = new UserDao(su);
			this.au = new AuthUtil(su);
		  
		}  
		
		//here we need sql query
		//get all articles
	     public List<ArticleModel> getAllArticlesWithNoContent(String authString,String profilename, UriInfo uriInfo){
	    	     	 
	    	 au.isUserAuthenticated(authString, profilename);
	    	 //authentic check user is same as others
	    	  return transformArticlesToModels(ad.getAllArticlesOfUserByNamedQuery(profilename), uriInfo);
	     }
  
	
		//get each article
	     public ArticleModel getArticleWithFullContent(long Id,UriInfo uriInfo) {
	    	 //if this is your article get it.
	    	 //No auth here
	    	 String profilename = HibernateUtil.getUsernameFromURLforComments(3,uriInfo);
	    	  ad.find(Id);// check if article present
	    	 
	    	  if(ad.IsUserArticleAvailable(profilename, Id))
	    	 	 return transformFullArticleToModel(ad.getArticleOfUserByNamedQuery(profilename, Id), uriInfo);
	    	 else return null;
		 }
	     
	   public ArticleModel postArticle(String authString,String profilename,
			                           ArticleModel model, UriInfo uriInfo){
		 //auth here
		   au.isUserAuthenticated(authString, profilename);
		  
		   User user = ud.find(profilename); //get the related user or throw exception
		   Article article = ad.createArticle(model, user); //create the article with user attached
		   Long id = ad.save(article); // save the article. if not saved -- throws exception
		  
		   return transformFullArticleToModel(ad.find(id), uriInfo); //return the article model
	   }
	     
	 public ArticleModel updateArticle(String authString,Long id, ArticleModel model, UriInfo uriInfo){
		 
		
		 String profilename = HibernateUtil.getUsernameFromURLforComments(3,uriInfo);
		 //auth here
		  au.isUserAuthenticated(authString, profilename); 
		
		 if(ad.IsUserArticleAvailable(profilename, id)){
			 Article article = ad.find(id);
		     article = ad.updateArticleValues(article, model);
		     ad.update(article);
		     
		     return transformFullArticleToModel(ad.find(id), uriInfo);
	     
    	 }
		 else return null;
    	 
	 }   
	 
   public void deleteArticle(String authString,Long id, UriInfo uriInfo){
		 
		 String profilename = HibernateUtil.getUsernameFromURLforComments(3,uriInfo);
		 //auth here
    	 au.isUserAuthenticated(authString, profilename);
		 
		 if(ad.IsUserArticleAvailable(profilename, id)){ //if article belongs to the user
			 Article article = ad.find(id);
		     ad.delete(article); //delete the article
		     ad.decrementArticleCount(article); //decrement the articlecount of user
	     
    	 }
		
    	 
	 }
	   
   
   
   
		
	///transformation service here
	     
	 
		private List<ArticleModel> transformArticlesToModels(List<Article> articles, UriInfo uriInfo) {  
	    	 List<ArticleModel> models = new ArrayList<ArticleModel>();
	    	   
	    	 for (Iterator<Article> iterator = articles.iterator(); iterator.hasNext();) {
				Article article = (Article) iterator.next();
				models.add( transformArticleToModel(article, uriInfo));
				
			}
	    	return models;
	    	 
	     
	     }
	     

	     
	     
		//transform content of article to its model
		
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
		
		//transform all the content of article to model for full view
		private ArticleModel transformFullArticleToModel(Article article, UriInfo uriInfo) {
			
			
			 
			 Link self = LinkService.createLinkForEachArticleOfUser("getAllArticles",
					                                      article.getUser().getUsername(),
					                                      article.getId(), uriInfo,"self");
			 Link articles = LinkService.createLinkForAllArticlesOfUser("getAllArticles",
					 												article.getUser().getUsername(),
					 												uriInfo,"articles");
			 Link comments = LinkService.createLinkForArticleComments("getAllArticles",
					 												   "getAllComments",
					 												  article.getUser().getUsername(),
					 												  article.getId(),
					 												  uriInfo, "comments");  
			 Link user = LinkService.CreateLinkForEachUser(article.getUser().getUsername(),
					 											uriInfo, "user");
		
				ArticleModel model = null;
				try {
					
				
					
					model = new ArticleModel()
					                  .constructModel(article)
					                  .addCount(article.getCommentCount())
					                  .addLikes(article.getLikes())
					                  .addContent(article.returnContentString())
					                  .addImage(article.returnImageString())
					                  .addTags(article.getTags())
					                  .addUser(article.getUser(), true, false)
					                  .addLinks(self, articles,comments,user);
					
				} catch (IOException | SQLException e) {
					throw new EvaluateException(e);
				}
		
			
			return model;
		}
		
		
		
		
}
