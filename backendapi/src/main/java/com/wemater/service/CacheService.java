package com.wemater.service;

import java.util.List;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

public class CacheService<T> {
	
	/* @method addCacheControlWithEtag
	 * @params Request, List, genericEntity
	 * 
	 * 
	 */
	
	public ResponseBuilder buildResponseWithCacheEtag(Request request,
			List<T> modelList,
			GenericEntity<List<T>> entity){

		CacheControl cc =  new CacheControl();
		cc.setMaxAge(86400);

		EntityTag eTag = new EntityTag(Integer.toString(modelList.hashCode()));
		ResponseBuilder builder = request.evaluatePreconditions(eTag);

		if(builder == null) builder = Response.ok(entity).tag(eTag);

		builder.cacheControl(cc);
		return builder;
	}
	 /*
	 * @method addCacheControlWithEtag
	 * @params Request, articleModel
	 * 
	 * 
	 */
	
	public ResponseBuilder buildResponseWithCacheEtag(Request request,
			T model){

		CacheControl cc =  new CacheControl();
		cc.setMaxAge(86400);

		EntityTag eTag = new EntityTag(Integer.toString(model.hashCode()));
		ResponseBuilder builder = request.evaluatePreconditions(eTag);

		if(builder == null) 	builder = Response.ok(model).tag(eTag);

		builder.cacheControl(cc);
		return builder;
	}
	 


}
