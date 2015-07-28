package com.wemater.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

@NamedQueries(value = {
		@NamedQuery(name = "user.findbyEmail", query = "from User u where u.email = :email"),

		@NamedQuery(name = "user.IsUserArticleAvailable", query = "select user.username from User as user "
				+ "inner join user.articles as article on article.id = :id "),

		@NamedQuery(name = "user.IsUserCommentAvailable", query = "select user.username from User as user"
				+ " inner join user.comments as comment on comment.id = :id  "),
		@NamedQuery(name = "user.IsUserAvailable", query = "from User as user "
				+ "where user.username = :username and user.password = :password "),
		@NamedQuery(name="user.ifUsernameExist", query = "select u.username from User as u  where u.username = :username"),
		@NamedQuery(name="user.ifUseremailExist", query = "select u.email from User as u  where u.email = :email")

})
@Table(name = "USER")
@Entity
public class User {

	private long id;
	private String username;
	private String name;
	private String email;
	private String password;
	private String bio;
	private int ArticleCount;
	private List<Article> articles;
	private int CommentCount;
	private List<Comment> comments;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID", unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NaturalId
	@Column(name = "USER_NAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "USER_EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "USER_PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "USER_BIO")
	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	@Column(name = "ARTICLE_COUNT")
	public int getArticleCount() {
		return this.ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		this.ArticleCount = articleCount;
	}

	@OneToMany(mappedBy = "user", cascade = { CascadeType.PERSIST })
	public List<Article> getArticles() {
		if (this.articles == null)
			this.articles = new ArrayList<Article>();

		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	@Column(name = "COMMENT_COUNT")
	public int getCommentCount() {
		return CommentCount;
	}

	public void setCommentCount(int commentCount) {
		CommentCount = commentCount;
	}

	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	public List<Comment> getComments() {
		if (this.comments == null)
			this.comments = new ArrayList<Comment>();
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public User() {
	}

}
