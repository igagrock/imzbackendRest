package com.wemater.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
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
	public Response getComments(@PathParam("articleId") long id,
							@QueryParam("next") int next,
			                       @Context UriInfo uriInfo,
			                       @Context Request request) {
		// no auth required

		List<CommentModel> modelList = service.getAllArticleComments(id,next, uriInfo);
		GenericEntity<List<CommentModel>> entity =
				new GenericEntity<List<CommentModel>>(modelList) {};
				
				return Response.ok(entity).build();
	
	}

	@Path("/{commentId}")
	public Response getComment() {
		throw new WebApplicationException("Not Implemented", Status.NOT_IMPLEMENTED);
	}

	@POST
	public Response postComment(
			@HeaderParam("Authorization") String authString,
			CommentModel model, @Context UriInfo uriInfo) {
		return Response.status(Status.CREATED)
				.entity(service.postArticleComment(authString, model, uriInfo))
				.build();
	}
	
}
