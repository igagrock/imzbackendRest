package com.wemater.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.wemater.modal.ArticleModel;
import com.wemater.service.ArticleService;

@Path("/articles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArticleResource {

	private ArticleService service;

	public ArticleResource() {
		this.service = new ArticleService();
	}

	@GET
	public Response getArticles(
			@HeaderParam("Authorization") String authString,
			@PathParam("profileName") String profilename,
			@Context UriInfo uriInfo) {

		GenericEntity<List<ArticleModel>> entity = new GenericEntity<List<ArticleModel>>(
				service.getAllArticlesWithNoContent(authString, profilename,
						uriInfo)) {
		};

		return Response.ok(entity).build();
	}

	@GET
	@Path("/{articleId}")
	public Response getArticle(@PathParam("articleId") Long Id,
			@Context UriInfo uriInfo) {
		// No authentication here coz anyone should see an article
		return Response.ok(service.getArticleWithFullContent(Id, uriInfo))
				.build();

	}

	@POST
	public Response postArticle(
			@HeaderParam("Authorization") String authString,
			@PathParam("profileName") String profilename, ArticleModel model,
			@Context UriInfo uriInfo) {

		return Response
				.status(Status.CREATED)
				.entity(service.postArticle(authString, profilename, model,
						uriInfo)).build();
	}

	@PUT
	@Path("/{articleId}")
	public Response updateArticle(
			@HeaderParam("Authorization") String authString,
			@PathParam("articleId") Long Id, ArticleModel model,
			@Context UriInfo uriInfo) {
		return Response.ok(
				service.updateArticle(authString, Id, model, uriInfo)).build();

	}

	@DELETE
	@Path("/{articleId}")
	public Response deteArticle(
			@HeaderParam("Authorization") String authString,
			@PathParam("articleId") Long Id, @Context UriInfo uriInfo) {

		service.deleteArticle(authString, Id, uriInfo);
		return Response.noContent().build();

	}

	@Path("/{articleId}/comments")
	public CommentResource getAllComments() {
		return new CommentResource();
	}
}
