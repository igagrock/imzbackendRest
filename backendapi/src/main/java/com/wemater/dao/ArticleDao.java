package com.wemater.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

import com.wemater.dto.Article;
import com.wemater.dto.User;
import com.wemater.exception.DataForbiddenException;
import com.wemater.exception.DataNotFoundException;
import com.wemater.exception.EvaluateException;
import com.wemater.modal.ArticleModel;
import com.wemater.util.SessionUtil;
import com.wemater.util.Util;

public class ArticleDao extends GenericDaoImpl<Article, Long> {

	private final SessionUtil sessionUtil;

	// inject sessionUtil object at the runtime to use the session
	public ArticleDao(SessionUtil sessionUtil) {
		super(sessionUtil, Article.class);
		this.sessionUtil = sessionUtil;
	}

	public SessionUtil getSessionUtil() throws InstantiationException {

		if (sessionUtil == null)
			throw new InstantiationException(
					"SessionUtil has not been set on DAO before usage");
		return sessionUtil;
	}

	@SuppressWarnings("unchecked")
	public List<Article> findByTag(String tag) {

		String articlesByTag = "from Article as article where :tag in elements(article.tags)";
		List<Article> articles = null;
		try {

			sessionUtil.beginSessionWithTransaction();
			articles = sessionUtil.getSession().createQuery(articlesByTag)
					.setParameter("tag", tag).list();

			sessionUtil.CommitCurrentTransaction();

			if (articles.isEmpty())
				throw new DataNotFoundException(
						"No articles found with this tag");
			return articles;

		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}

	}

	// util method to send the likes. using setlikes doubles the count coz of
	// hibernate proxy
	public void addLikes(int likes, Article article) {

		try {
			sessionUtil.beginSessionWithTransaction();
			if (article != null)
				article.setLikes(likes);
			else
				throw new DataNotFoundException(
						"likes cant be set- article not found");

			sessionUtil.CommitCurrentTransaction();

		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}

	}

	public Article getArticleOfUserByNamedQuery(String username, long articleId) {

		try {

			sessionUtil.beginSessionWithTransaction();

			Article article = (Article) sessionUtil.getSession()
					.getNamedQuery("article.getArticleByUsernameAndArticleId")
					.setParameter("username", username)
					.setParameter("articleId", articleId).uniqueResult();

			sessionUtil.CommitCurrentTransaction();

			if (article == null)
				throw new DataNotFoundException("This article doesnt not exist");
			return article;

		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}

	}

	@SuppressWarnings({ "unchecked" })
	public List<Article> getAllArticlesOfUserByNamedQuery(String username) {

		List<Article> articles = null;
		try {

			sessionUtil.beginSessionWithTransaction();

			articles = sessionUtil.getSession()
					.getNamedQuery("article.getAllArticlesByUsername")
					.setParameter("username", username).list();

			sessionUtil.CommitCurrentTransaction();

			System.out.println(articles.size());

			if (articles.isEmpty())
				throw new DataNotFoundException("No articles for the "
						+ username);

		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}

		return articles;

	}

	// to check if an article exists for a particular user

	@SuppressWarnings("unchecked")
	public boolean IsUserArticleAvailable(String username, long articleid) {

		boolean IsAvailable = false;

		try {
			sessionUtil.beginSessionWithTransaction();

			List<String> usernames = sessionUtil.getSession()
					.getNamedQuery("user.IsUserArticleAvailable")
					.setParameter("id", articleid).list();

			sessionUtil.CommitCurrentTransaction();

			int value = Collections.binarySearch(usernames, username);
			if (value == 0)
				IsAvailable = true;
			else
				throw new DataForbiddenException("User '" + username
						+ "' is not author of this article");

		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}
		return IsAvailable;
	}

	public void decrementArticleCount(Article article) {

		try {
			User user = article.getUser(); // get the user
			int count = user.getArticleCount(); // get count of articles
			// throw exception if no articles written
			if (count == 0)
				throw new DataNotFoundException("No articles written by User "
						+ user.getUsername());

			sessionUtil.beginSessionWithTransaction();
			user.setArticleCount(count - 1);
			sessionUtil.CommitCurrentTransaction();

		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);

		}
	}

	public List<String> createTags(String... tags) {
		List<String> newTags = new ArrayList<String>();
		for (String tag : tags)
			newTags.add(tag);
		return newTags;

	}

	public Article createArticle(ArticleModel model, User user) {

		model = model.ValidateArticle(); // validate the article first

		Article article = new Article();
		article.setTitle(model.getTitle());
		// article.setUrl(URL); URL will be generated later when file will be
		// serialized
		article.createImageString(model.getImage());
		article.createContentString(model.getContent());
		article.setTags(model.getTags());
		article.setDate(new Date());
		article.mapUserAndArticle(user);
		// article comments will be added when new comment will be posted on
		// article so not set

		return article;
	}

	public Article ValidateUpdateArticle(Article article, ArticleModel model) {

		if (!Util.IsEmptyOrNull(model.getTitle()))
			article.setTitle(model.getTitle());
		if (!Util.IsEmptyOrNull(model.getImage()))
			article.createImageString(model.getImage());
		if (!Util.IsEmptyOrNull(model.getContent()))
			article.createContentString(model.getContent());
		if (!Util.IsEmptyOrNull(model.getTags()))
			article.setTags(model.getTags());

		return article;
	}

}
