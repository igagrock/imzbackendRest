package com.wemater.dto;

import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.wemater.util.Util;

@NamedQueries(value = {

		@NamedQuery(name = "comment.getAllUserCommentsByUsername", query = "select c "
				+ "from Comment c " + "where c.username = :username"),

		@NamedQuery(name = "comment.getUserCommentByUsernameandCommentid", query = "select c "
				+ "from Comment c "
				+ "where c.username = :username and c.id = :commentId"),

		@NamedQuery(name = "comment.getAllArticleCommentsByArticleId", query = "select c "
				+ "from Comment c " + "where c.article.id = :articleId order by c.id desc"),

		@NamedQuery(name = "comment.getArticleCommentByArticleIdAndCommentid", query = "select c "
				+ "from Comment c "
				+ "where c.article.id = :articleId and c.id = :commentId"),

})
@Entity
@Table(name = "COMMENT")
public class Comment {

	private Long id;
	private String username;
	private Clob content;
	private Date date;
	private Article article;
	private User user;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COMMENT_ID", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	@Column(name = "COMMENT_USER")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "COMMENT_CONTENT")
	private Clob getContent() {
		return content;
	}

	private void setContent(Clob content) {
		this.content = content;
	}
	
	@Column(name = "DATE")
	@Temporal(TemporalType.DATE)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String returnContentString() {
		return Util.convertClobToString(this.getContent());

	}

	public void createContentString(String content) {

		this.setContent(Util.convertStringToClob(content));
	}
	
	@ManyToOne
	@JoinColumn(name = "ARTICLE_ID")
	public Article getArticle() {
		return article;
	}

	private void setArticle(Article article) {
		this.article = article;
	}

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void MapCommentsAndArticle(Article article) {
		article.getComments().add(this);
		article.setCommentCount(article.getComments().size());
		this.setArticle(article);
	}

	public void MapCommentsAndUser(User user) {
		user.getComments().add(this);
		user.setCommentCount(user.getComments().size());
		this.setUser(user);
	}

	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}

}
