package com.wemater.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.hibernate.SessionFactory;

import com.wemater.dao.ArticleDao;
import com.wemater.dao.CommentDao;
import com.wemater.dao.UserDao;
import com.wemater.dto.Article;
import com.wemater.dto.Comment;
import com.wemater.dto.User;
import com.wemater.modal.CommentModel;
import com.wemater.modal.Link;
import com.wemater.util.AuthUtil;
import com.wemater.util.HibernateUtil;
import com.wemater.util.SessionUtil;
import com.wemater.util.Util;

public class CommentService {

	private final SessionFactory sessionfactory;
	private final SessionUtil su;
	private final CommentDao cd;
	private final ArticleDao ad;
	private final UserDao ud;
	private final AuthUtil au;

	public CommentService() {
		this.sessionfactory = HibernateUtil.getSessionFactory();
		this.su = new SessionUtil(sessionfactory.openSession());
		this.cd = new CommentDao(su);
		this.ud = new UserDao(su);
		this.ad = new ArticleDao(su);
		this.au = new AuthUtil(su);

	}

	// 1: get all Comments of User
	public List<CommentModel> getAlluserComments(String authString,
			String username, UriInfo uriInfo) {

		// authentication here
		au.isUserAuthenticated(authString, username);

		return transformUsersCommentsToModelsWithUsername(
				cd.getAllCommentsOfUserByNamedQuery(username), uriInfo);

	}

	// 2: get one Comments of User
	public CommentModel getOneuserComment(String authString, long commentId,
			UriInfo uriInfo) {

		String profilename = Util.getUsernameFromURLforComments(3, uriInfo);
		au.isUserAuthenticated(authString, profilename);

		if (cd.IsUserCommentAvailable(profilename, commentId))

			return transformCommentToModelForUser(
					cd.getCommentOfUserByNamedQuery(profilename, commentId),
					uriInfo);

		else
			return null;
	}

	// 5: Update the comment in article for any ones articles

	public CommentModel UpdateUserComment(String authString, long commentId,
			CommentModel model, UriInfo uriInfo) {

		String profilename = Util.getUsernameFromURLforComments(3, uriInfo);
		System.out.println(profilename);
		au.isUserAuthenticated(authString, profilename);

		if (cd.IsUserCommentAvailable(profilename, commentId)) {

			Comment comment = cd.find(commentId);
			cd.save(cd.validateUpdateComment(comment, model));

			return transformCommentToModelForArticle(cd.find(commentId),
					uriInfo); // return model of comment

		}

		else
			return null;
	}

	// 6: delete the comment
	public void deleteUserComment(String authString, long commentId,
			UriInfo uriInfo) {

		String profilename = Util.getUsernameFromURLforComments(3, uriInfo);
		au.isUserAuthenticated(authString, profilename);

		if (cd.IsUserCommentAvailable(profilename, commentId)) {

			Comment comment = cd.find(commentId);
			cd.delete(comment);
			cd.decrementCommentCount(comment);

		}

	}

	// 3: getallComments on an article of my article

	public List<CommentModel> getAllArticleComments(Long articleId, int next,
			UriInfo uriInfo) {
		// No auth required
		String profilename = Util.getUsernameFromURLforComments(4, uriInfo);

		if (ad.IsUserArticleAvailable(profilename, articleId)) {

			return transformUsersCommentsToModelsWithArticleId(
					cd.getAllCommentsOfArticleByNamedQuery(articleId, next), uriInfo);
		} else
			return null;
	}

	// 5: post comments.

	public CommentModel postArticleComment(String authString,
			CommentModel model, UriInfo uriInfo) {

		Long articleId = Util.getArticleIdFromURLforComments(2, uriInfo);

		au.isUserAuthenticatedGET(authString);
		User user = ud.find(au.getLoggedInUser(authString)); // get user
		Article article = ad.find(articleId); // get article
		Comment comment = cd.createComment(model.getContent(), article, user); // get
																				// comment
		long id = cd.save(comment); // save article or exception will be thrown

		return transformCommentToModelForArticle(cd.find(id), uriInfo); // return
																		// model
																		// of
																		// comment
	}

	// comment service starts here

	// get all users
	private List<CommentModel> transformUsersCommentsToModelsWithArticleId(
			List<Comment> comments, UriInfo uriInfo) {

		List<CommentModel> models = new ArrayList<CommentModel>();

		for (Iterator<Comment> iterator = comments.iterator(); iterator
				.hasNext();) {
			Comment comment = iterator.next();
			CommentModel model = transformCommentToModelForArticle(comment,
					uriInfo);
			models.add(model);
		}

		return models;
	}

	private List<CommentModel> transformUsersCommentsToModelsWithUsername(
			List<Comment> comments, UriInfo uriInfo) {

		List<CommentModel> models = new ArrayList<CommentModel>();

		for (Iterator<Comment> iterator = comments.iterator(); iterator
				.hasNext();) {

			Comment comment = iterator.next();
			CommentModel model = transformCommentToModelForUser(comment,
					uriInfo);
			models.add(model);

		}

		return models;
	}

	// transform Comment to model for user -- for usercomments
	private CommentModel transformCommentToModelForUser(Comment comment,
			UriInfo uriInfo) {

		Link self = LinkService.createLinkForEachUserComment("getComments",
				comment.getUsername(), comment.getId(), uriInfo, "self");
		Link comments = LinkService.createLinkForUserComments("getComments",
				comment.getUsername(), uriInfo, "comments");
		Link user = LinkService.CreateLinkForEachUser(comment.getUsername(),
				uriInfo, "user");

		Link article = LinkService.createLinkForEachArticleOfUser(
				"getAllArticles", comment.getArticle().getUser().getUsername(),
				comment.getArticle().getId(), uriInfo, "article");

		CommentModel model = new CommentModel().ConstructModel(comment)
				.addArticle(comment.getArticle()).addUser(comment.getUser())
				.addLinks(self, user, article, comments);

		return model;
	}

	// transform Comment to model for user -- for Article
	private CommentModel transformCommentToModelForArticle(Comment comment,
			UriInfo uriInfo) {

		Link self = LinkService.createLinkForEachUserComment("getComments",
				comment.getUsername(), comment.getId(), uriInfo, "self");
		Link comments = LinkService.createLinkForArticleComments(
				"getAllArticles", "getAllComments", comment.getArticle()
						.getUser().getUsername(), comment.getArticle().getId(),
				uriInfo, "comments");
		Link user = LinkService.CreateLinkForEachUser(comment.getUsername(),
				uriInfo, "user");

		Link article = LinkService.createLinkForEachArticleOfUser(
				"getAllArticles", comment.getArticle().getUser().getUsername(),
				comment.getArticle().getId(), uriInfo, "article");

		CommentModel model = new CommentModel().ConstructModel(comment)
				.addArticle(comment.getArticle()).addUser(comment.getUser())
				.addLinks(self, user, article, comments);

		return model;
	}

}
