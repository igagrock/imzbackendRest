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

public class PublicService implements Runnable {

	private final SessionFactory sessionfactory;
	private final SessionUtil su;
	private final PublicDao pd;

	private static List<Article> LatestArticles = new ArrayList<Article>();
	private static List<Article> trendingArticles = new ArrayList<Article>();
	private static List<Article> quickReadArticles = new ArrayList<Article>();

	public PublicService() {
		this.sessionfactory = HibernateUtil.getSessionFactory();
		this.su = new SessionUtil(sessionfactory.openSession());
		this.pd = new PublicDao(su);
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

		setLatestArticles(pd.fetchLatestArticles());
		setTrendingArticles(pd.fetchTrendingArticles());
		setQuickReadArticles(pd.fetchQuickReadArticles());
	}

	public List<ArticleModel> getLatestArticleModels(UriInfo uriInfo) {

		return transformArticlesToModels(getLatestArticles(), uriInfo);

	}

	public List<ArticleModel> getTrendingArticleModels(UriInfo uriInfo) {
		System.out.println("articles found from tredding list");
		return transformArticlesToModels(getTrendingArticles(), uriInfo);

	}

	public List<ArticleModel> getQuickReadArticleModels(UriInfo uriInfo) {
		System.out.println("articles found from read list");
		return transformArticlesToModels(getQuickReadArticles(), uriInfo);

	}

	// doesnt get the articles using executor services. rather on user command
	public List<ArticleModel> getExploreArticleModels(int next, UriInfo uriInfo) {

		System.out.println("articles found from explore list");
		return transformArticlesToModels( pd.fetchExploreArticles(next), uriInfo);

	}

	// service related methods
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

		ArticleModel model = new ArticleModel().constructModel(article)
				.addCount(article.getCommentCount())
				.addLikes(article.getLikes()).addTags(article.getTags())
				.addUser(article.getUser(), false, false)
				.addLinks(self, articles, comments, user);

		return model;
	}

}
