package com.wemater.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.wemater.modal.ArticleModel;
import com.wemater.service.PublicService;

@Path("public")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExploreResource {

	private PublicService service;

	public ExploreResource() {
		this.service = new PublicService();
	}

	@GET
	@Path("/trending")
	public Response getTrendingArticles(@HeaderParam("Authorization") String authString,
											@Context UriInfo uriInfo,
											@Context Request request
												) {
		GenericEntity<List<ArticleModel>> entity = new GenericEntity<List<ArticleModel>>(
				service.getTrendingArticleModels(authString,uriInfo)) {
		};
		
		CacheControl cc =  new CacheControl();
	    cc.setMaxAge(86400);
	    
	    EntityTag eTag = new EntityTag(Integer.toString(entity.hashCode()));
	    ResponseBuilder builder = request.evaluatePreconditions(eTag);
	    
	    if(builder == null){
	    	
	    	builder = Response.ok(entity).tag(eTag);
	    }
		
		builder.cacheControl(cc);
		return builder.build();

	}
	@GET
	@Path("/top")
	public Response getTopArticles(@Context UriInfo uriInfo) {
		GenericEntity<List<ArticleModel>> entity = new GenericEntity<List<ArticleModel>>(
				service.getTopArticleModels(uriInfo)){};
		return Response.ok(entity).build();

	}

	@GET
	@Path("/latest")
	public Response getLatestArticles(@HeaderParam("Authorization") String authString,@Context UriInfo uriInfo) {
		GenericEntity<List<ArticleModel>> entity = new GenericEntity<List<ArticleModel>>(
				service.getLatestArticleModels(authString,uriInfo)) {
		};

		return Response.ok(entity).build();

	}

	@GET
	@Path("/reads")
	public Response getQuickReadArticles(@HeaderParam("Authorization") String authString,@Context UriInfo uriInfo) {
		GenericEntity<List<ArticleModel>> entity = new GenericEntity<List<ArticleModel>>(
				service.getQuickReadArticleModels(authString,uriInfo)) {
		};

		return Response.ok(entity).build();

	}

	@GET
	@Path("/explore")
	public Response exploreArticles(@HeaderParam("Authorization") String authString,
										@Context UriInfo uriInfo,
										@Context  Request request ,
										@QueryParam("next") int next) {
		GenericEntity<List<ArticleModel>> entity = new GenericEntity<List<ArticleModel>>(
				service.getExploreArticleModels(next ,authString, uriInfo)) {};

				CacheControl cc =  new CacheControl();
			    cc.setMaxAge(86400);
			    
			    EntityTag eTag = new EntityTag(Integer.toString(entity.hashCode()));
			    ResponseBuilder builder = request.evaluatePreconditions(eTag);
			    
			    if(builder == null){
			    	
			    	builder = Response.ok(entity).tag(eTag);
			    }
				
				builder.cacheControl(cc);
				return builder.build();

	}

}
