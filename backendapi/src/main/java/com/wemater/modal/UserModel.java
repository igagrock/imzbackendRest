package com.wemater.modal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import com.wemater.dto.Article;
import com.wemater.dto.Comment;
import com.wemater.dto.User;
import com.wemater.exception.ValueNotProvidedException;
import com.wemater.util.Util;

public class UserModel {

	private long id;
	private String username;
	private String name;
	private String email;
	private String password;
	private String bio;
	private int articlecount;
	private List<ArticleModel> articleModels;
	private int commentcount;
	private List<CommentModel> comments;
	private List<Link> links;
	private Boolean isVerified;

	public UserModel() {
		super();

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public int getArticlecount() {
		return articlecount;
	}

	public void setArticlecount(int articlecount) {
		this.articlecount = articlecount;
	}

	@XmlTransient
	public List<ArticleModel> getArticleModels() {
		if (this.articleModels == null)
			this.articleModels = new ArrayList<ArticleModel>();
		return articleModels;
	}

	public void setArticleModels(List<ArticleModel> articleModels) {
		this.articleModels = articleModels;
	}

	public int getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(int commentcount) {
		this.commentcount = commentcount;
	}

	@XmlTransient
	public List<CommentModel> getComments() {
		if (this.comments == null)
			this.comments = new ArrayList<CommentModel>();
		return comments;
	}

	public void setComments(List<CommentModel> comments) {
		this.comments = comments;
	}

	public List<Link> getLinks() {
		if (this.links == null)
			this.links = new ArrayList<Link>();
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	

	public synchronized UserModel constructModel(User user) {

		this.name = user.getName();
		this.bio = user.getBio();
		this.isVerified = user.getIsVerified();

		return this;
	}

	public synchronized UserModel addUsername(String username) {
		this.username = username;
		return this;

	}

	public synchronized UserModel addId(Long Id) {
		this.id = Id;
		return this;
	}

	public synchronized UserModel addEmail(String email) {
		this.email = email;
		return this;
	}

	public synchronized UserModel addPassword(String password) {
		this.password = password;
		return this;
	}

	public synchronized UserModel addArticleCount(int articleCount) {
		this.articlecount = articleCount;
		return this;
	}

	public synchronized UserModel addCommentCount(int commentcount) {
		this.commentcount = commentcount;
		return this;
	}

	public synchronized UserModel addArticles(List<Article> articles)
			throws IOException, SQLException {
		List<ArticleModel> models = new ArrayList<ArticleModel>();

		for (Iterator<Article> iterator = articles.iterator(); iterator
				.hasNext();) {
			Article article = (Article) iterator.next();

			ArticleModel model = new ArticleModel().constructModel(article)
					.addContent(article.returnContentString())
					.addCount(article.getCommentCount())
					.addImage(article.returnImageString())
					.addLikes(article.getLikes()).addTags(article.getTags())
					.addComments(article.getComments());

			models.add(model);

		}
		this.articleModels = models;
		return this;

	}

	public synchronized UserModel addComments(List<Comment> comments) {
		List<CommentModel> models = new ArrayList<CommentModel>();

		for (Iterator<Comment> iterator = comments.iterator(); iterator
				.hasNext();) {
			Comment comment = (Comment) iterator.next();
			CommentModel model = new CommentModel().ConstructModel(comment);
			model.addArticle(comment.getArticle());
			model.addUser(comment.getUser());

			models.add(model);
		}

		this.comments = models;
		return this;

	}

	public synchronized UserModel addLinks(Link... links) {
		List<Link> linkage = new ArrayList<Link>();
		for (Link link : links) {
			linkage.add(link);
		}
		this.setLinks(linkage);
		return this;

	}



}
