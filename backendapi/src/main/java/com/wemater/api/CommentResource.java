package com.wemater.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.wemater.modal.CommentModel;
import com.wemater.service.CommentService;

@Path("comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentResource {
	
		private CommentService service;
		
		public CommentResource() {
			this.service = new CommentService();
		}
	
		@GET
		public List<CommentModel> getComments(@PathParam("articleId") long id, @Context UriInfo uriInfo)
		{
			return service.getAllArticleCommentsWithArticleId(id, uriInfo);		
		}
	
	  
	
		@GET
		@Path("/{commentId}")
		public CommentModel getComment(@PathParam("commentId") long id, @Context UriInfo uriInfo){

			return service.getOneArticleComment(id, uriInfo);
		}
	
	

}  
