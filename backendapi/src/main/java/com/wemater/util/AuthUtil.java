package com.wemater.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.HibernateException;

import com.wemater.dto.User;
import com.wemater.exception.AuthException;
import com.wemater.exception.DataForbiddenException;
import com.wemater.exception.EvaluateException;

public class AuthUtil {

	private final SessionUtil su;
	private final static Map<String, String> Authmapper = new HashMap<String, String>();

	public AuthUtil(SessionUtil su) {
		this.su = su;

	}

	/*
	 * //Authentication for the private resources which only a user can see.
	 * User can see his Own Information or the comments he has put Other users
	 * can't see the information about other user such as email address or
	 * userName This is meant to keep the user Info private however the
	 * protected resources such as the articles written by a user are not
	 * controlled by it
	 */

	/**
	 * @param authString
	 * @param username
	 * @return boolean. true if the authentication is success user name should
	 *         match the user who has logged in
	 */
	public boolean isUserAuthenticated(String authString, String username) {

		if (Util.IsEmptyOrNull(authString))
			throw new AuthException("Authentication Required--NULL AUTH");

		if (IsUserAvailableInAuthMap(authString,username))
			return true;
		else if (isUserAvailableInDatabase(authString, username)) {
			return true;
		} else
			return false;
	}

	/**
	 * @param authString
	 * @return boolean /Return true if the user has already registered
	 * 
	 *         This method is used to authenticate a user for protected
	 *         resources
	 */
	public boolean isUserAuthenticatedGET(String authString) {

		if (Util.IsEmptyOrNull(authString))
			throw new AuthException("Authentication Required - NULL AUTH GET");

		if (IsUserAvailableInAuthMapGET(authString))
			return true;
		else if (isUserAvaliableInDatabaseGET(authString)) {
			return true;
		} else
			return false;

	}

	private boolean isUserAvailableInDatabase(String encodedAuthString,
			String username) {

		String[] params = getParamArray(encodedAuthString);
		System.out.println(params[0] + " " + params[1]);
		

		User AuthUser = null;
		Boolean isValidationSuccessfull = false;

		try {
			AuthUser = findRegisteredUser(params);

			if (AuthUser == null) { // either username doesnt match or no user
				// present
				isValidationSuccessfull = false;
				throw new AuthException(
						"User credentials are invalid -NOT FOUND IN DATABASE");
			}
			if (AuthUser!= null && !username.equals(params[0])) { // either username doesnt match
				// or no user present
				isValidationSuccessfull = false;
				throw new DataForbiddenException("Private Resouce!!");
			}

			if (AuthUser != null && username.equals(params[0])) { // user
				// present
				// and
				// username
				// matches
				// to
				// current
				isValidationSuccessfull = true;
				System.out.println("FOUND IN DATABASE ");
				addToAuthMap(encodedAuthString); // User validated so put it in
				// map
			}

		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}
		return isValidationSuccessfull;
	}

	private boolean isUserAvaliableInDatabaseGET(String encodedAuthString) {

		String[] params = getParamArray(encodedAuthString);
		System.out.println(params[0] + " " + params[1]);

		User AuthUser = null;
		Boolean isValidationSuccessfull = false;

		try {
			AuthUser = findRegisteredUser(params);

			if (AuthUser == null) { // either username doesnt match or no user
				// present
				isValidationSuccessfull = false;
				throw new AuthException(
						"User credentials are invalid -- NOT FOUND IN DATABASE GET");
			}

			if (AuthUser != null) { // user present
				System.out.println("FOUND IN DATABASE GET");
				addToAuthMap(encodedAuthString);
				isValidationSuccessfull = true;
			}

		} catch (HibernateException e) {
			su.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}
		return isValidationSuccessfull;
	}

	private User findRegisteredUser(String[] params) {

		
		User AuthUser = null;
		try{
		su.beginSessionWithTransaction();

		AuthUser = (User) su.getSession().getNamedQuery("user.IsUserAvailable")
				.setParameter("username", Base64.encodeBase64(params[0].getBytes()))
				.setParameter("password", Util.generateMD5Hash(params[1])).uniqueResult(); //MD5 hash done here

		su.CommitCurrentTransaction();
		}
		catch (HibernateException e) {
		throw new EvaluateException(e);
		}
	
		return AuthUser;
		
	}
	
	private void addToAuthMap(String encodedAuthString) {

		System.out.println(" Adding to auth map");
		Authmapper.put(getLoggedInUser(encodedAuthString), encodedAuthString);
		System.out.println("added to authmap "
				+ Authmapper.get(getLoggedInUser(encodedAuthString)));

	}

	public static void removeFromAuthMap(String username) {
		Authmapper.remove(username);
	}

	private boolean IsUserAvailableInAuthMapGET(String authString) {
		System.out.println("checking in Map");
		String LoggedUser = getLoggedInUser(authString);
		String mappedAuth = Authmapper.get(LoggedUser);
		System.out.println(Authmapper.get(getLoggedInUser(authString)));
		if(Authmapper.get(LoggedUser) != null  && mappedAuth.equals(authString))
		{
			System.out.println("FOUND IN MAP GET");
			return true;
		}
		return false;

	}
	
	private boolean IsUserAvailableInAuthMap(String authString,String username) {
		System.out.println("checking in Map");
		String LoggedUser = getLoggedInUser(authString);
		String mappedAuth = Authmapper.get(LoggedUser);
		System.out.println(Authmapper.get(getLoggedInUser(authString)));
		if(Authmapper.get(LoggedUser) != null && LoggedUser.equals(username) && mappedAuth.equals(authString))
		{
			System.out.println("FOUND IN MAP");
         return true;
		}
     return false;
	}

	private String DecodeAuthString(String encodedAuthString) {

		String[] decodedAuth = encodedAuthString.split("\\s+");
		if (decodedAuth.length != 2)
			throw new AuthException("Invalid User Credentials _DECODE AUTH !=2");
		byte[] decodedAuthParam = Base64.decodeBase64(decodedAuth[1]);
		String authparam = new String(decodedAuthParam);
		if (Util.IsEmptyOrNull(authparam))
			throw new AuthException("Invalid User Credentials EMPTY NULL");
		return authparam;

	}

	private String[] getParamArray(String encodedauthString) {
		String[] params = DecodeAuthString(encodedauthString).split(":");
		if (params.length != 2)
			throw new AuthException(
					"Invalid User Credentials --PARAM ARRAY != 2");
		return params;
	}

	public String getLoggedInUser(String decodedauthString) {
		return getParamArray(decodedauthString)[0];

	}
	
	public String[] getdecodeParamArray(String encodedauthString) {
		String[] params =encodedauthString.split(":");
		if (params.length != 2)
			throw new AuthException(
					"temperred verification details auth != 2");
		return params;
	}


	
}
