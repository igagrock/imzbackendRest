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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.wemater.modal.UserModel;
import com.wemater.service.UserService;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

	private UserService service;

	public UserResource() {
		this.service = new UserService();

	}

	// remove this after testing

	@GET
	public Response getAllUsers(@Context UriInfo uriInfo,
										@QueryParam("username") String username,
										@QueryParam("email") String email      ) {
		

		if(username != null){
			System.out.println("username is "+username);
			return Response.ok(service.IfUsernameExist(username)).build();
		
		}
		if(email != null) {
			System.out.println("email is "+email);
			return Response.ok(service.ifEmailExist(email)).build();
		}
		  
		
		
		GenericEntity<List<UserModel>> entity = new GenericEntity<List<UserModel>>(
				service.getAllusers(uriInfo)) {
		};

		return Response.ok(entity).build();
	}

	@GET
	@Path("/{profileName}")
	public Response getUser(@HeaderParam("Authorization") String authString,
								@PathParam("profileName") String profilename,
								@Context UriInfo uriInfo) {
		
		return Response.ok(service.getUser(authString, profilename, uriInfo))
				.build();

	}

	@POST
	public Response postUser(@Context UriInfo uriInfo, UserModel model) {

		return Response.status(Status.CREATED)
				.entity(service.postUser(model, uriInfo)).build();

	}

	@PUT
	@Path("/{profileName}")
	public Response updateUser(@HeaderParam("Authorization") String authString,
			@PathParam("profileName") String profilename, UserModel model,
			@Context UriInfo uriInfo) {

		return Response.ok(
				service.updateUser(authString, profilename, model, uriInfo))
				.build();

	}

	@DELETE
	@Path("/{profileName}")
	public Response deleteUser(@HeaderParam("Authorization") String authString,
			@PathParam("profileName") String profilename,
			@Context UriInfo uriInfo) {

		service.deleteUser(authString, profilename);
		return Response.noContent().build();

	}

	// redirection from here

	@Path("/{profileName}/comments")
	public UserCommentResource getComments() {
		return new UserCommentResource();

	}

	@Path("/{profileName}/articles")
	public ArticleResource getAllArticles() {
		return new ArticleResource();
	}

}
