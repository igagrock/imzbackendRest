package com.wemater.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.wemater.modal.ArticleModel;
import com.wemater.service.CacheService;
import com.wemater.service.PublicService;

@Path("public")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PublicResource {

	private PublicService service;
	private CacheService<ArticleModel> cs;

	public PublicResource() {
		this.service = new PublicService();
		this.cs = new CacheService<ArticleModel>();
	}

	@GET
	@Path("/trending")
	public Response getTrendingArticles(@Context UriInfo uriInfo,
											@Context Request request
												) {
		List<ArticleModel> modelList = service.getTrendingArticleModels(uriInfo);
		
		GenericEntity<List<ArticleModel>> entity =
				new GenericEntity<List<ArticleModel>>(modelList) {};
		
		 return cs.buildResponseWithCacheEtag(request, modelList, entity).build();

	}
	@GET
	@Path("/top")
	public Response getTopArticles(@Context UriInfo uriInfo, @Context Request request) {
		
		List<ArticleModel> modelList = service.getTopArticleModels(uriInfo);
		GenericEntity<List<ArticleModel>> entity =
				new GenericEntity<List<ArticleModel>>(modelList){};
		
			return	cs.buildResponseWithCacheEtag(request, modelList, entity).build();

	}

	@GET
	@Path("/latest")
	public Response getLatestArticles(@Context UriInfo uriInfo, @Context Request request) {
		
		List<ArticleModel> modelList = service.getLatestArticleModels(uriInfo);
		GenericEntity<List<ArticleModel>> entity = new GenericEntity<List<ArticleModel>>(modelList) {};

		return cs.buildResponseWithCacheEtag(request, modelList, entity).build();

	}

	@GET
	@Path("/reads")
	public Response getQuickReadArticles(@Context UriInfo uriInfo,@Context Request request ) {
		
		List<ArticleModel> modelList = service.getQuickReadArticleModels(uriInfo);
		GenericEntity<List<ArticleModel>> entity = new GenericEntity<List<ArticleModel>>(modelList) {};

		return cs.buildResponseWithCacheEtag(request, modelList, entity).build();

	}

	@GET
	@Path("/explore")
	public Response exploreArticles(@Context UriInfo uriInfo,
										@Context  Request request ,
										@QueryParam("next") int next) {
		
		List<ArticleModel> modelList = service.getExploreArticleModels(next , uriInfo);
		GenericEntity<List<ArticleModel>> entity = 
				new GenericEntity<List<ArticleModel>>(modelList) {};
		
				return cs.buildResponseWithCacheEtag(request, modelList, entity).build();

	}

	
}
