package com.wemater.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.hibernate.SessionFactory;

import com.wemater.dao.UserDao;
import com.wemater.dto.User;
import com.wemater.modal.AjaxModel;
import com.wemater.modal.Link;
import com.wemater.modal.UserModel;
import com.wemater.util.AuthUtil;
import com.wemater.util.HibernateUtil;
import com.wemater.util.SessionUtil;
import com.wemater.util.Util;

public class UserService {
	private final SessionFactory sessionfactory;
	private final SessionUtil su;
	private final UserDao ud;
	private final AuthUtil au;

	public UserService() {
		this.sessionfactory = HibernateUtil.getSessionFactory();
		this.su = new SessionUtil(sessionfactory.openSession());
		this.ud = new UserDao(su);
		this.au = new AuthUtil(su);

	}

	// 1: get all users
	public List<UserModel> getAllusers(UriInfo uriInfo) {

		return transformUsersToModels(ud.findAll(), uriInfo);

	}

	// 2: get each user
	public UserModel getUser(String authString, String profileName,
			UriInfo uriInfo) {
		au.isUserAuthenticated(authString, profileName);

		return transformUserToModel(ud.find(Util.removeSpaces(profileName)),
				uriInfo);
	}

	public UserModel postUser(UserModel model, UriInfo uriInfo) {

		Long id = ud.save(ud.createUser(model)); // create the user in param and
													// save the user

		return transformUserToModel(ud.find(id), uriInfo);

	}

	public UserModel updateUser(String authString, String profilename,
			UserModel model, UriInfo uriInfo) {

		// authentication first
		au.isUserAuthenticated(authString, profilename);
		ud.update(ud.updateValidateUser(
				ud.find(Util.removeSpaces(profilename)), model)); // update the
																	// user in
																	// the
																	// database

		return transformUserToModel(ud.find(profilename), uriInfo); // return
																	// the model
																	// of the
																	// updated
																	// user

	}

	public void deleteUser(String authString, String profilename) {

		au.isUserAuthenticated(authString, profilename);
		User user = ud.find(profilename);
		ud.removeRefrences(user);
		ud.delete(user);
	}
	
	
	//for ajax requests
	
	public AjaxModel IfUsernameExist(String username){
		return new AjaxModel(ud.ifUsernameExists(username));
		
	}
	public AjaxModel ifEmailExist(String email){
		return new AjaxModel(ud.ifEmailExists(email));
	}

	// /USerService for transforming starts here

	// get all users
	private List<UserModel> transformUsersToModels(List<User> users,
			UriInfo uriInfo) {

		List<UserModel> models = new ArrayList<UserModel>();

		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {

			User user = iterator.next();
			UserModel model = transformUserToModel(user, uriInfo);

			models.add(model);
		}

		return models;
	}

	// transform
	private UserModel transformUserToModel(User user, UriInfo uriInfo) {

		Link self = LinkService.CreateLinkForEachUser(user.getUsername(),
				uriInfo, "self");
		Link users = LinkService.createLinkForAllUsers(uriInfo, "users");
		Link articles = LinkService.createLinkForAllArticlesOfUser(
				"getAllArticles", user.getUsername(), uriInfo, "articles");
		Link comments = LinkService.createLinkForUserComments("getComments",
				user.getUsername(), uriInfo, "comments");

		UserModel model = new UserModel().constructModel(user)
				.addId(user.getId()).addUsername(user.getUsername())
				.addEmail(user.getEmail())
				.addArticleCount(user.getArticleCount())
				.addCommentCount(user.getCommentCount())
				.addLinks(self, users, articles, comments);
		return model;
	}

}