package com.wemater.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.wemater.modal.CommentModel;
import com.wemater.service.CacheService;
import com.wemater.service.CommentService;

@Path("userComments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserCommentResource {

	private CommentService service;
	private CacheService<CommentModel> cs;

	public UserCommentResource() {
		this.service = new CommentService();
		this.cs = new CacheService<CommentModel>();
		
	}

	@GET
	public Response getCommentsOfuser(
			@HeaderParam("Authorization") String authString,
			@PathParam("profileName") String username,
			@Context UriInfo uriInfo,
			@Context Request request) {
		
		List<CommentModel> modelList = service.getAlluserComments(authString, username, uriInfo);
		GenericEntity<List<CommentModel>> entity = new GenericEntity<List<CommentModel>>(modelList) {};

		return cs.buildResponseWithCacheEtag(request, modelList, entity).build();
	}

	@GET
	@Path("/{commentId}")
	public Response getCommentOfUser(
			@HeaderParam("Authorization") String authString,
			@PathParam("commentId") long id,
			@Context UriInfo uriInfo,
			@Context Request request) {
		return cs.buildResponseWithCacheEtag(request,
				service.getOneuserComment(authString, id, uriInfo)).build();
	}

	@PUT
	@Path("/{commentId}")
	public Response updateComment(
			@HeaderParam("Authorization") String authString,
			@PathParam("commentId") long id, CommentModel model,
			@Context UriInfo uriInfo) {

		return Response.ok(
				service.UpdateUserComment(authString, id, model, uriInfo))
				.build();
	}

	@DELETE
	@Path("/{commentId}")
	public Response deleteComment(
			@HeaderParam("Authorization") String authString,
			@PathParam("commentId") long id, @Context UriInfo uriInfo) {

		service.deleteUserComment(authString, id, uriInfo);
		return Response.noContent().build();
	}

}
