package com.wemater.modal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.wemater.dto.Article;
import com.wemater.dto.Comment;
import com.wemater.dto.User;
import com.wemater.exception.ValueNotProvidedException;
import com.wemater.util.Util;

/**
 * @author sheikh
 *
 */
@XmlRootElement(name="article")
public class ArticleModel {

	private long id;
	private String title;
	private String url;
	private String image;
	private String content;
	private List<String> tags;
	private String date;
	private int likes;
	private boolean isLiked;
	private int commentCount;
	private UserModel userModel;
	private List<CommentModel> commentModels;
	private List<Link> links;

	
	@XmlElement(name="isliked")
	public boolean isLiked() {
		return isLiked;
	}

	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getTags() {
		if (this.tags == null)
			this.tags = new ArrayList<String>();
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@XmlTransient
	public List<CommentModel> getCommentModels() {
		if (this.commentModels == null)
			this.commentModels = new ArrayList<CommentModel>();
		return commentModels;
	}

	public void setCommentModels(List<CommentModel> commentModels) {
		this.commentModels = commentModels;
	}

	public List<Link> getLinks() {
		if (this.links == null)
			this.links = new ArrayList<Link>();
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public synchronized ArticleModel constructModel(Article article) {

		this.id = article.getId();
		this.title = article.getTitle();
		this.url = article.getUrl();
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		this.date = format.format(article.getDate());
		return this;
	}

	public synchronized ArticleModel addImage(String image) {

		this.image = image;
		return this;
	}

	public synchronized ArticleModel addContent(String content) {
		this.content = content;
		return this;
	}

	public synchronized ArticleModel addTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public synchronized ArticleModel addCount(int commentCount) {
		this.commentCount = commentCount;
		return this;
	}

	public synchronized ArticleModel addLikes(int likes) {
		this.likes = likes;
		return this;
	}
   public synchronized ArticleModel addIsliked(boolean isliked){
	   this.isLiked = isliked;
	   return this;
	   
   }
	
	public synchronized ArticleModel addUser(User user, boolean isArticleCount,
			boolean isCommentCount) {
		UserModel model = new UserModel().constructModel(user);
		if (isArticleCount)
			model.addArticleCount(user.getArticleCount());
		if (isCommentCount)
			model.addCommentCount(user.getCommentCount());
		this.setUserModel(model);
		return this;
	}

	public synchronized ArticleModel addComments(List<Comment> comments) {

		List<CommentModel> models = new ArrayList<CommentModel>();

		for (Iterator<Comment> iterator = comments.iterator(); iterator
				.hasNext();) {
			Comment comment = (Comment) iterator.next();
			CommentModel model = new CommentModel().ConstructModel(comment);
			model.addArticle(comment.getArticle());
			model.addUser(comment.getUser());

			models.add(model);
		}

		this.commentModels = models;

		return this;
	}

	public synchronized ArticleModel addLinks(Link... links) {
		List<Link> linkage = new ArrayList<Link>();
		for (Link link : links) {
			linkage.add(link);
		}
		this.setLinks(linkage);
		return this;

	}

	public synchronized ArticleModel ValidateArticle() {

		if (Util.IsEmptyOrNull(this.getTitle()))
			throw new ValueNotProvidedException("Title is not provided","Title");
		if (Util.IsEmptyOrNull(this.getImage()))
			throw new ValueNotProvidedException("Image is not provided","Image");

		if (Util.IsEmptyOrNull(this.getContent()))
			throw new ValueNotProvidedException("Content is not provided","Content");

		if (Util.IsEmptyOrNull(this.getTags()))
			throw new ValueNotProvidedException( " Tags Not provided","Tags");
		return this;
	}

	public ArticleModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	

	
	@Override
	public int hashCode() {
		
		return new HashCodeBuilder(57,77)
		         .append(id)
		         .append(title)
		         //.append(url)
		         .append(content)
		         .append(tags)
		         .append(likes)
		         .append(isLiked)
		         .append(commentCount)
		         .toHashCode();
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		   if (obj == this) { return true; }
		   if (obj.getClass() != getClass()) {
		     return false;
		     }
		   
		   ArticleModel am = (ArticleModel)obj;
		   
		   return new EqualsBuilder()
		   				.append(id, am.getId())
		   				.append(title, am.getTitle())
		   				.append(content, am.getContent())
		   				.append(tags, am.getTags())
		   				.append(likes, am.getLikes())
		   				.append(isLiked, am.isLiked())
		   				.append(commentCount, am.commentCount)
		               .isEquals();
	}
	
		  


}
