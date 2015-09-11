package com.wemater.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.hibernate.SessionFactory;

import com.wemater.dao.ArticleDao;
import com.wemater.dao.UserDao;
import com.wemater.dto.Article;
import com.wemater.modal.ArticleModel;
import com.wemater.modal.Link;
import com.wemater.util.AuthUtil;
import com.wemater.util.HibernateUtil;
import com.wemater.util.SessionUtil;
import com.wemater.util.Util;

public class ArticleService {

	private final SessionFactory sessionfactory;
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

	
	public List<ArticleModel> getAllArticlesWithNoContent(String authString,
			String profilename, int next, UriInfo uriInfo){
		
		au.isUserAuthenticated(authString, profilename);
	    List<Article> articles = ad.getAllArticlesOfUserNoContent(profilename, next);
		return transformArticlesToModelsNoContent(articles, profilename, uriInfo);
	}
	
	// here we need sql query
	// get all articles
	public List<ArticleModel> getAllArticlesWithHalfContent(String authString,
			String profilename, int next, UriInfo uriInfo) {

		au.isUserAuthenticatedGET(authString);
		// authentic check user is same as others
		return transformArticlesToModels(
				ad.getAllArticlesOfUserByNamedQuery(profilename, next), uriInfo);
	}

	// get each article
	public ArticleModel getArticleWithFullContent(long Id,String authString, UriInfo uriInfo) {
		// if this is your article get it.
		// No auth here
		String profilename = Util.getUsernameFromURLforComments(3, uriInfo);
		ad.find(Id);// check if article present

		if (ad.IsUserArticleAvailable(profilename, Id))
			return transformFullArticleToModel(
					ad.getArticleOfUserByNamedQuery(profilename, Id),authString, uriInfo);
		else
			return null;
	}

	public ArticleModel postArticle(String authString, String profilename,
			ArticleModel model, UriInfo uriInfo) {
		// auth here
		au.isUserAuthenticated(authString, profilename);

		Long id = ad.save( ad.createArticle(model, ud.find(profilename))); // save
																		
		return transformFullArticleToModel(ad.find(id),authString, uriInfo); // return the
																	// article
																	// model
	}

	public ArticleModel updateArticle(String authString, Long id,
			ArticleModel model, UriInfo uriInfo) {

		String profilename = Util.getUsernameFromURLforComments(3, uriInfo);
		// auth here   
		au.isUserAuthenticated(authString, profilename);

		if (ad.IsUserArticleAvailable(profilename, id)) {
             Article article = ad.find(id);
			ad.update(ad.ValidateUpdateArticle(article, model));
			return transformFullArticleToModel(article,authString, uriInfo);
		}

		else
			return null;

	}
	
  public ArticleModel updateLikes(int likes, String authString, Long id, UriInfo uriInfo){
	  
	      au.isUserAuthenticatedGET(authString);
	      Article article = ad.find(id);
	      ad.addLikes(likes, article, authString);
	      
	      return transformFullArticleToModel(article, authString, uriInfo);
	      
	  
  }
	

	public void deleteArticle(String authString, Long id, UriInfo uriInfo) {

		String profilename = Util.getUsernameFromURLforComments(3, uriInfo);
		// auth here
		au.isUserAuthenticated(authString, profilename);

		if (ad.IsUserArticleAvailable(profilename, id)) { // if article belongs
															// to the user
			Article article = ad.find(id);
			ad.delete(article); // delete the article
			ad.decrementArticleCount(article); // decrement the articlecount of
												// user

		}

	}

	// /transformation service here

	private List<ArticleModel> transformArticlesToModels(
			List<Article> articles, UriInfo uriInfo) {
		List<ArticleModel> models = new ArrayList<ArticleModel>();

		for (Iterator<Article> iterator = articles.iterator(); iterator
				.hasNext();) {
			Article article = (Article) iterator.next();
			models.add(transformArticleToModel(article, uriInfo));

		}
		return models;

	}

	private List<ArticleModel> transformArticlesToModelsNoContent(
			List<Article> articles,String profileName, UriInfo uriInfo) {
		List<ArticleModel> models = new ArrayList<ArticleModel>();

		for (Iterator<Article> iterator = articles.iterator(); iterator
				.hasNext();) {
			Article article = (Article) iterator.next();
			models.add(transformArticleToModelNoContent(article, profileName, uriInfo));

		}
		return models;

	}
	
	// transform content of article to its model

		private ArticleModel transformArticleToModelNoContent(Article article,String profileName,
																		UriInfo uriInfo) {

			Link self = LinkService.createLinkForEachArticleOfUser(
					"getAllArticles", profileName,
					article.getId(), uriInfo, "self");
			Link articles = LinkService.createLinkForAllArticlesOfUser(
					"getAllArticles", profileName, uriInfo,
					"articles");
			Link comments = LinkService.createLinkForArticleComments(
					"getAllArticles", "getAllComments",profileName , article.getId(), uriInfo, "comments");
			Link user = LinkService.CreateLinkForEachUser(profileName, uriInfo, "user");

			return new ArticleModel().constructModel(article)
					.addLinks(self, articles, comments, user);

		}
	// transform content of article to its model

	private ArticleModel transformArticleToModel(Article article,
			UriInfo uriInfo) {

		Link self = LinkService.createLinkForEachArticleOfUser(
				"getAllArticles", article.getUser().getUsername(),
				article.getId(), uriInfo, "self");
		Link articles = LinkService.createLinkForAllArticlesOfUser(
				"getAllArticles", article.getUser().getUsername(), uriInfo,
				"articles");
		Link comments = LinkService.createLinkForArticleComments(
				"getAllArticles", "getAllComments", article.getUser()
						.getUsername(), article.getId(), uriInfo, "comments");
		Link user = LinkService.CreateLinkForEachUser(article.getUser()
				.getUsername(), uriInfo, "user");

		return new ArticleModel().constructModel(article)
				.addCount(article.getCommentCount())
				.addLikes(article.getLikes())
				.addContent(article.returnContentString())
				.addImage(article.returnImageString())
				.addUser(article.getUser(), true, false)
				.addLinks(self, articles, comments, user);

	}

	// transform all the content of article to model for full view
	private ArticleModel transformFullArticleToModel(Article article, String encodedAuth,
															UriInfo uriInfo) {

		Link self = LinkService.createLinkForEachArticleOfUser(
				"getAllArticles", article.getUser().getUsername(),
				article.getId(), uriInfo, "self");
		Link articles = LinkService.createLinkForAllArticlesOfUser(
				"getAllArticles", article.getUser().getUsername(), uriInfo,
				"articles");
		Link comments = LinkService.createLinkForArticleComments(
				"getAllArticles", "getAllComments", article.getUser()
						.getUsername(), article.getId(), uriInfo, "comments");
		Link user = LinkService.CreateLinkForEachUser(article.getUser()
				.getUsername(), uriInfo, "user");

		//check if the user has already liked the article
		
		
		
		
		return new ArticleModel().constructModel(article)
				.addCount(article.getCommentCount())
				.addLikes(article.getLikes())
				.addIsliked( ad.haveUserLiked(article, encodedAuth))
				.addContent(article.returnContentString())
				.addImage(article.returnImageString())
				.addTags(article.getTags())
				.addUser(article.getUser(), true, false)
				.addLinks(self, articles, comments, user);

	}

}
