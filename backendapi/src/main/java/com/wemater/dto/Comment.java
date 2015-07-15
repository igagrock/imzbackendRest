package com.wemater.dto;

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


@NamedQueries(value = { 
		
		@NamedQuery(name = "comment.getAllUserCommentsByUsername",query = "select c "
		          		+ "from Comment c "
		          		+ "where c.username = :username"),
          		
		@NamedQuery(name = "comment.getUserCommentByUsernameandCommentid",query = "select c "
		          		+ "from Comment c "
		          		+ "where c.username = :username and c.id = :commentId"),
       
       @NamedQuery(name = "comment.getAllArticleCommentsByArticleId",query = "select c "
                  		+ "from Comment c "
                  		+ "where c.article.id = :articleId"),
                  		
       @NamedQuery(name = "comment.getArticleCommentByArticleIdAndCommentid",query = "select c "
                  		+ "from Comment c "
                  		+ "where c.article.id = :articleId and c.id = :commentId"),

       

})
@Entity
@Table(name="COMMENT")

public class Comment {
	
	private Long id;
	private String username;
	private String email;
	private String content;
	private Article article;
	private User user;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="COMMENT_ID", unique=true, nullable=false)
	public Long getId() {
		return id;
	}
	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="COMMENT_USER") 
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="COMMENT_EMAIL") public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name="COMMENT_CONTENT")public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@ManyToOne
	@JoinColumn(name="ARTICLE_ID")
	public Article getArticle() {
		return article;
	}
	private void setArticle(Article article) {
		this.article = article;
	}
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	public User getUser() {
		return user;
	}
	private void setUser(User user) {
		this.user = user;
	}
	public Comment(String username, String email, String content) {
		super();
		this.username = username;
		this.email = email;
		this.content = content;
	}
	
	public void MapCommentsAndArticle(Article article){
		article.getComments().add(this);
		article.setCommentCount(article.getComments().size());
		this.setArticle(article);
	}
   
	public void MapCommentsAndUser(User user){
		user.getComments().add(this);
		user.setCommentCount(user.getComments().size());
		this.setUser(user);
	}

	
	
	
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}
