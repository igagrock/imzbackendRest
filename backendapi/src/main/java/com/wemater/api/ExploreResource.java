package com.wemater.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("public")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExploreResource {
	
	@GET
	@Path("/trending")
	public String getTrendingArticles()
	{   //eqaual to top stories
		return "here in trending articles";
		   
	}
	@GET
	@Path("/latest")
	public String getLatestArticles()
	{
		return "here in latest articles";
		
	}
	@GET
	@Path("/reads")
	public String getQuickReadArticles()
	{
		return "here in quickread articles";
		
	}
	@GET
	@Path("/explore")
	public String exploreArticles()
	{
		return "here in explore articles";
		
	}
	
	
	
	
	
	

}
    