package com.wemater.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.wemater.modal.ArticleModel;
import com.wemater.service.PublicService;


@Path("public")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExploreResource {
	
	private PublicService service;
	

	
	public ExploreResource() {
		super();
		this.service = new PublicService();
	}
	
	
	
	
	
	@GET
	@Path("/trending")
	public Response getTrendingArticles(@Context UriInfo uriInfo)
	{   
		GenericEntity<List<ArticleModel>> entity = 
				new GenericEntity<List<ArticleModel>>(service.getTrendingArticleModels(uriInfo)){};
		return Response.ok(entity).build();
		   
	}  
	
	
	
	
	@GET
	@Path("/latest")
	public Response getLatestArticles(@Context UriInfo uriInfo)
	{
		GenericEntity<List<ArticleModel>> entity = 
				new GenericEntity<List<ArticleModel>>(service.getLatestArticleModels(uriInfo)){};
		
		return Response.ok(entity).build();
		
	}
	@GET
	@Path("/reads")
	public Response getQuickReadArticles(@Context UriInfo uriInfo)
	{
		GenericEntity<List<ArticleModel>> entity = 
				new GenericEntity<List<ArticleModel>>(service.getQuickReadArticleModels(uriInfo)){};
		
		return Response.ok(entity).build();
		
	}
	@GET
	@Path("/explore")
	public String exploreArticles(@Context UriInfo uriInfo,@QueryParam("start") int start,
			                           @QueryParam("size") int size )
	{
		return "queryparams are :start "+start+" :: size"+size ;
		
	}
	
	
	
	
	
	

}
    