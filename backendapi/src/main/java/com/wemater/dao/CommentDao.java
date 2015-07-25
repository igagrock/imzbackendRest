package com.wemater.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;

import com.wemater.dto.Article;
import com.wemater.dto.Comment;
import com.wemater.dto.User;
import com.wemater.exception.DataForbiddenException;
import com.wemater.exception.DataNotFoundException;
import com.wemater.exception.EvaluateException;
import com.wemater.exception.ValueNotProvidedException;
import com.wemater.modal.CommentModel;
import com.wemater.util.SessionUtil;
import com.wemater.util.Util;

public class CommentDao extends GenericDaoImpl<Comment, Long>{
	
	private final SessionUtil sessionUtil;
    //inject sessionUtil object at the runtime to use the session
	public CommentDao(SessionUtil sessionUtil) {
		super(sessionUtil, Comment.class);
		this.sessionUtil = sessionUtil;
	}



	public SessionUtil getSessionUtil() throws InstantiationException {
		
			if (sessionUtil == null)  
	            throw new InstantiationException("SessionUtil has not been set on DAO before usage");
			return sessionUtil;
	}



	public Comment createComment(String content, Article article, User user) {
		
		if(Util.IsEmptyOrNull(content))
			throw new ValueNotProvidedException("Content Not provided", "content");
		 
		Comment comment = new Comment();
		comment.setUsername(user.getUsername());
		comment.setContent(content);
		comment.MapCommentsAndArticle(article);
		comment.MapCommentsAndUser(user);
		return comment;
	}
	
  public Comment validateUpdateComment(Comment comment, CommentModel model)	{
	            
	       if( !Util.IsEmptyOrNull(model.getContent())) comment.setContent(model.getContent());
	       return comment;
	  
  }

	
	
/*
 * USER COMMENT SECTION STARTS HERE
 */
	
	public Comment getCommentOfUserByNamedQuery(String username, long commentId){
		
	
		
			try {
			
			sessionUtil.beginSessionWithTransaction();
            
			Comment comment = (Comment) sessionUtil.getSession()
						 .getNamedQuery("comment.getUserCommentByUsernameandCommentid")
						 .setParameter("username", username)
						 .setParameter("commentId", commentId)
						 .uniqueResult();
			 
			 sessionUtil.CommitCurrentTransaction();
			  
			 if(comment == null)  throw new DataNotFoundException("This comment doesnt not exist");
			    return comment;
		 
		  
			
		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}

    	
	 
	}

	@SuppressWarnings({ "unchecked"})
	public List<Comment> getAllCommentsOfUserByNamedQuery( String username){
		  
		List<Comment> comments = null;
		try {
			
			sessionUtil.beginSessionWithTransaction();
            
			 comments = sessionUtil.getSession()
						 .getNamedQuery("comment.getAllUserCommentsByUsername")
						 .setParameter("username", username)
						 .list();
			 
			 sessionUtil.CommitCurrentTransaction();
			  
		
		    System.out.println(comments.size());
		    
		  if(comments.isEmpty())
			  throw new DataNotFoundException("No Comments for the "+username);
		  
			
		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}
		
		return comments;
	
	}

/*
 * ARTICLE COMMENT SECTIONS STARTS HERE	
 */
	
	
public Comment getCommentOfArticleByNamedQuery(long articleId, long commentId){
		
	try {
		
		sessionUtil.beginSessionWithTransaction();
        
		Comment comment = (Comment) sessionUtil.getSession()
					 .getNamedQuery("comment.getArticleCommentByArticleIdAndCommentid")  
					 .setParameter("articleId", articleId)
					 .setParameter("commentId", commentId)
					 .uniqueResult();
		 
		 sessionUtil.CommitCurrentTransaction();
		  
		 if(comment == null)  throw new DataNotFoundException("This comment doesnt not exist");
		    return comment;
	 
	  
		
	} catch (HibernateException e) {
		sessionUtil.rollBackCurrentTransaction();
		throw new EvaluateException(e);
	}
	 
	}
	
	
	@SuppressWarnings({ "unchecked"})
	public List<Comment> getAllCommentsOfArticleByNamedQuery( long id){
		  
		List<Comment> comments = null;
		try {
			
			sessionUtil.beginSessionWithTransaction();
            
			 comments = sessionUtil.getSession()
						 .getNamedQuery("comment.getAllArticleCommentsByArticleId")
						 .setParameter("articleId", id)
						 .list();
			 
			 sessionUtil.CommitCurrentTransaction();
			  
		
		    System.out.println(comments.size());
		    
		  if(comments.isEmpty())
			  throw new DataNotFoundException("No Comments for this article");
		  
			
		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}
		
		return comments;
	
	}

	//check if user comments are available
	

	@SuppressWarnings("unchecked")
	public boolean IsUserCommentAvailable(String username,long commentId){
	
		boolean IsAvailable = false;
		   try {
			 sessionUtil.beginSessionWithTransaction();
			 
			 List<String> usernames = sessionUtil.getSession().getNamedQuery("user.IsUserCommentAvailable")
					               .setParameter("id", commentId)
					               .list();
			 
			 sessionUtil.CommitCurrentTransaction();
			 
			 int value = Collections.binarySearch(usernames, username);
			 if(value == 0) IsAvailable = true;
			 else throw new DataForbiddenException("User '"+username+"' didnt not post this comment");
			   
			   
		} catch (HibernateException e) {
			
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}
		return IsAvailable;
	}
	
	//check if article comment is available
	
	@SuppressWarnings("unchecked")
	public  boolean IsArticleCommentAvailable(Long commentId,long articleId){
		
		boolean IsAvailable = false;
		
		   try {
			   
			   sessionUtil.beginSessionWithTransaction();
			 
			List<Long>  ids = sessionUtil.getSession().getNamedQuery("article.IsArticleCommentAvailable")
								               .setParameter("id", commentId)
								               .list();
			 sessionUtil.CommitCurrentTransaction();
			 
			 //check if username is available
			 int value = Collections.binarySearch(ids, articleId); 
			 
			 if(value == 0) IsAvailable = true;
			//throw exception if not present
			 else throw new DataForbiddenException(" comment requested doesnt belong to this article");
			   
			   
		} catch (HibernateException e) {
			
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}
		return IsAvailable;
	}
	
	
	 public void decrementCommentCount(Comment comment){
		   
		   try {
			    User user = comment.getUser(); //get the user
			    Article article = comment.getArticle();
			    int userCommentCount = user.getCommentCount(); //get count of comments
			    int articleCommentCount = article.getCommentCount();
			    //throw exception if no articles written
			    if(articleCommentCount == 0) throw new DataNotFoundException("No comments article");
			    if(userCommentCount == 0) throw new DataNotFoundException( "No comments written by User "+user.getUsername());
			    
			    sessionUtil.beginSessionWithTransaction();
			      user.setCommentCount(userCommentCount-1);
			      article.setCommentCount(articleCommentCount-1);
			      
			    sessionUtil.CommitCurrentTransaction();
			    
		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
	         
		}
	   }


	
	
	

}
