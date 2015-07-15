package com.wemater.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.hibernate.SessionFactory;

import com.wemater.dao.CommentDao;
import com.wemater.dto.Comment;
import com.wemater.modal.CommentModel;
import com.wemater.modal.Link;
import com.wemater.util.HibernateUtil;
import com.wemater.util.SessionUtil;

public class CommentService {
	
	private  final SessionFactory sessionfactory;
    private final SessionUtil su;
    private final CommentDao cd;
   
   
 
	public CommentService() {
		this.sessionfactory = HibernateUtil.getSessionFactory();
		this.su = new SessionUtil(sessionfactory.openSession());
		this.cd = new CommentDao(su);
	  
	}

	
	//1: get all Comments of User
    public List<CommentModel> getAlluserComments(String username,UriInfo uriInfo){
   
   		return transformUsersCommentsToModelsWithUsername(
   				                  cd.getAllCommentsOfUserByNamedQuery(username), uriInfo);

    }

	
  //2: get one Comments of User
	public CommentModel getOneuserComment(long commentId, UriInfo uriInfo){

   	  String profilename = HibernateUtil.getUsernameFromURLforComments(uriInfo);
   	   
   	 if(cd.IsUserCommentAvailable(profilename, commentId))
   	  return transformCommentToModelForUser(cd.getCommentOfUserByNamedQuery(profilename, commentId),uriInfo);
   	 else return null;
    }


     
  
    
  //3: getallComments on an article
 
    public List<CommentModel> getAllArticleCommentsWithArticleId(Long articleId, UriInfo uriInfo){
    	
    	return transformUsersCommentsToModelsWithArticleId(
    			                    cd.getAllCommentsOfArticleByNamedQuery(articleId), uriInfo);
    } 
    
    //4: get one Comments of article // do editing here
    public CommentModel getOneArticleComment(long commentId, UriInfo uriInfo){
   	      	 Long articleId = HibernateUtil.getArticleIdFromURLforComments(uriInfo);
   	      	 if(cd.IsArticleCommentAvailable(commentId, articleId)){
   	      	 return transformCommentToModelForArticle(
   	      			          cd.getCommentOfArticleByNamedQuery(articleId, commentId), uriInfo);
   	      	 }
   	      	 else return null;
    }
    
    
    
    
    
    
    
    
    
    
    
	//comment service starts here
    
    
    //get all users
	private List<CommentModel> transformUsersCommentsToModelsWithArticleId(List<Comment> comments, UriInfo uriInfo) {
		
		 List<CommentModel> models = new ArrayList<CommentModel>();
		 
		 for (Iterator<Comment> iterator = comments.iterator(); iterator.hasNext();) {
	           Comment comment = iterator.next();
			   CommentModel model = transformCommentToModelForArticle(comment, uriInfo);
			   models.add(model);
			 }

		return models;
	}
    
	
	private List<CommentModel> transformUsersCommentsToModelsWithUsername(List<Comment> comments, UriInfo uriInfo) {
		
		 List<CommentModel> models = new ArrayList<CommentModel>();
		 
		 for (Iterator<Comment> iterator = comments.iterator(); iterator.hasNext();) {
	
			  Comment comment = iterator.next();
			   CommentModel model = transformCommentToModelForUser(comment, uriInfo);
			   models.add(model);
			
		 }  

		return models;
	}	
	

	//transform Comment to model for user -- for usercomments
	private CommentModel transformCommentToModelForUser(Comment comment, UriInfo uriInfo) {
		
			Link self = LinkService.createLinkForEachUserComment("getComments",
					                                       comment.getUsername(),
					                                       comment.getId(),
					                                       uriInfo,"self");		
			Link comments = LinkService.createLinkForUserComments("getComments",
					                                           comment.getUsername(),
					                                           uriInfo,
					                                           "comments");
			Link user = LinkService.CreateLinkForEachUser(comment.getUsername(),
					                                    uriInfo,
					                                    "user");
			
			Link article = LinkService.createLinkForEachArticleOfUser("getAllArticles",
			                                                       comment.getArticle().getUser().getUsername(),
			                                                       comment.getArticle().getId(), uriInfo,
			                                                       "article");

		

			
			CommentModel model = new CommentModel()
		                       .ConstructModel(comment)
		 		               .addArticle(comment.getArticle())
		 		               .addUser(comment.getUser())
		                       .addLinks(self, user,article,comments);
		
			return model;
	}


	//transform Comment to model for user -- for Article
		private CommentModel transformCommentToModelForArticle(Comment comment, UriInfo uriInfo) {
			
				Link self = LinkService.createLinkForEachUserComment("getComments",
						                                       comment.getUsername(),
						                                       comment.getId(),
						                                       uriInfo,"self");		
				Link comments = LinkService.createLinkForArticleComments("getAllArticles",
																	  "getAllComments",
																	  comment.getArticle().getUser().getUsername(),
																	  comment.getArticle().getId(),
																	  uriInfo,
																	   "comments");
				Link user = LinkService.CreateLinkForEachUser(comment.getUsername(),
						                                    uriInfo,
						                                    "user");
				
				Link article = LinkService.createLinkForEachArticleOfUser("getAllArticles",
				                                                       comment.getArticle().getUser().getUsername(),
				                                                       comment.getArticle().getId(), uriInfo,
				                                                       "article");

			

				
				CommentModel model = new CommentModel()
			                       .ConstructModel(comment)
			 		               .addArticle(comment.getArticle())
			 		               .addUser(comment.getUser())
			                       .addLinks(self, user,article,comments);
			
				return model;
		}
		
	
		
	
		  
}
