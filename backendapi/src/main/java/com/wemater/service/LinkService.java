package com.wemater.service;

import javax.ws.rs.core.UriInfo;

import com.wemater.api.ArticleResource;
import com.wemater.api.UserResource;
import com.wemater.modal.Link;

public class LinkService {
	

	
    public synchronized static Link createLinkForAllUsers(UriInfo uriInfo, String rel) {
   	 
   	 String url = uriInfo.getBaseUriBuilder()
		           .path(UserResource.class)
		           .build().toString();
   	 return Link.createLink(url, rel);
    }  
    
    public synchronized static Link CreateLinkForEachUser(String profname, UriInfo uriInfo,String rel){
   	
   	String url = uriInfo.getBaseUriBuilder()
		           .path(UserResource.class)
		           .path(profname)
		           .build().toString(); 
   	
     return Link.createLink(url, rel);
   	
   }
    
    
    public synchronized static Link createLinkForAllArticlesOfUser(String methodName,String profname,
    		                                                 UriInfo uriInfo, String rel) {
      	 
      	 String url = uriInfo.getBaseUriBuilder()
				   .path(UserResource.class)
		           .path(UserResource.class, methodName)
		           .resolveTemplate("profileName", profname)
		           .build().toString(); 
      	 return Link.createLink(url, rel);
       } 
    
    public synchronized static Link createLinkForEachArticleOfUser(String methodName,String profname,
    															  long id, UriInfo uriInfo,String rel) {
     	 
     	 String url = uriInfo.getBaseUriBuilder()
			     .path(UserResource.class)
			     .path(UserResource.class, methodName)
	             .resolveTemplate("profileName", profname)
	             .path(String.valueOf(id))
	             .build().toString(); 
     	 return Link.createLink(url, rel);
      } 
    
    public synchronized static Link createLinkForUserComments(String methodName,String profname,
    		                                                   UriInfo uriInfo,String rel) {
    
    	 String url = uriInfo.getBaseUriBuilder()
				   .path(UserResource.class)
		           .path(UserResource.class, methodName)
		           .resolveTemplate("profileName", profname)
		           .build().toString();  
    	 
    	 return Link.createLink(url, rel);
 
    
    } 
    
    public synchronized static Link createLinkForEachUserComment(String methodName,String profname,long id,
    															UriInfo uriInfo,String rel) {
        
   	 String url = uriInfo.getBaseUriBuilder()
				   .path(UserResource.class)
		           .path(UserResource.class, methodName)
		           .resolveTemplate("profileName", profname)
		           .path(String.valueOf(id))
		           .build().toString();  
   	 
   	 return Link.createLink(url, rel);

   
   } 
   //getAllComments
 //http://localhost:8080/backendapi/api/users/sammer/articles/9/comments
    public synchronized static Link createLinkForArticleComments(String UsermethodName,
    															 String ArticleMethodName,
    															 String profname,
    															 long id,
    															 UriInfo uriInfo,String rel) {

		String url = uriInfo.getBaseUriBuilder()
		.path(UserResource.class)
		.path(UserResource.class, UsermethodName)
		.resolveTemplate("profileName", profname)
		.path(ArticleResource.class, ArticleMethodName)
		.resolveTemplate("articleId", String.valueOf(id))
		.build().toString();  
		
		return Link.createLink(url, rel);
		
		
		}

}
