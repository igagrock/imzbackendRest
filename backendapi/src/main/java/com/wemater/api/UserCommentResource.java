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

@Path("userComments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserCommentResource {
	
	  private CommentService service;
	    
		
	  
		public UserCommentResource() {
			this.service = new CommentService();
		}

		
		
		@GET
		public List<CommentModel> getComments(@PathParam("profileName") String username, @Context UriInfo uriInfo)
		{
			System.err.println("getcomments inside the usercomment resouce called");
			return service.getAlluserComments(username,uriInfo);
		}
				
		@GET
		@Path("/{commentId}")
		public CommentModel getCommentsOfUser(@PathParam("commentId") long id, @Context UriInfo uriInfo)
		{
			System.err.println("getcomment of one user inside the usercomment resouce called");
			return service.getOneuserComment(id, uriInfo);
		}

}
