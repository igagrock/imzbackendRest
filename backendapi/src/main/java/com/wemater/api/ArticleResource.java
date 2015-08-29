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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.wemater.modal.ArticleModel;
import com.wemater.service.ArticleService;
import com.wemater.service.CacheService;

@Path("/articles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArticleResource {

	private ArticleService service;
	private CacheService<ArticleModel> cs;

	public ArticleResource() {
		this.service = new ArticleService();
		this.cs = new CacheService<ArticleModel>();
	}

	@GET
	public Response getArticles(
			@HeaderParam("Authorization") String authString,
			@PathParam("profileName") String profilename,
			@QueryParam("next") int next,
			@Context UriInfo uriInfo,
			@Context Request request) {

		List<ArticleModel> modelList = service.getAllArticlesWithNoContent(authString, profilename,next,
																						uriInfo);
		GenericEntity<List<ArticleModel>> entity = 
				new GenericEntity<List<ArticleModel>>(modelList) {};

		return cs.buildResponseWithCacheEtag(request, modelList, entity).build();
	}

	@GET
	@Path("/{articleId}")
	public Response getArticle(@PathParam("articleId") Long Id,
									@HeaderParam("Authorization") String authString,	
									@Context UriInfo uriInfo,
									@Context Request request) {
		// No authentication here coz anyone should see an article
		return cs.buildResponseWithCacheEtag(request,
					service.getArticleWithFullContent(Id,authString, uriInfo)).build();

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
			@QueryParam("likes") int likes,
			@Context UriInfo uriInfo) {
		
		if(likes != 0){ //update the likes incase queryparam is there
			return Response.ok(service.updateLikes(likes, authString, Id, uriInfo)).build();
			
		}
		
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
