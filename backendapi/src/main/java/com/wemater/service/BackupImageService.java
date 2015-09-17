package com.wemater.service;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wemater.dao.ArticleDao;
import com.wemater.dto.Article;
import com.wemater.modal.ImageModel;
import com.wemater.modal.ImageResponseModel;
import com.wemater.util.HibernateUtil;
import com.wemater.util.SessionUtil;

public class BackupImageService implements Runnable {
    private static int FAIL_COUNT = 0;
	private SessionUtil su;
	private ArticleDao ad;

	public BackupImageService() {
		super();
		this.su = new SessionUtil(HibernateUtil.getSessionFactory().openSession());
		this.ad = new ArticleDao(su);
		
		
	}
	
	
	
  //create method here to use the api to save the image 
 // update the url in the articles	
	
   public Response ConnectToCDN(String base64Image, String title){
	   System.out.println("CONNECTING TO CDN");
	   ImageModel imd = new ImageModel();
	   imd.setBase64Image(base64Image);
	   imd.setTitle(title);
	   
	    Client postClient = ClientBuilder.newClient();
	    WebTarget postwebTarget =  postClient.target("http://cdnbackendapi-vbr.rhcloud.com/api/images");
	    Invocation.Builder  postBuilder = postwebTarget.request(MediaType.APPLICATION_JSON);
	    System.out.println("CDN CONNECTION CALL BACK");
	     return postBuilder.post(Entity.entity(imd, MediaType.APPLICATION_JSON));

		
   }
	public void processURLUpdate(Article article){
		Response resp = ConnectToCDN(article.returnImageString(), article.getTitle());
		if(resp.getStatus() == 200){
			article.setUrl(resp.readEntity(ImageResponseModel.class).getUrl());
			ad.update(article);
		}
		else if(FAIL_COUNT++ < 5){
			  System.out.println("image update failed. doing again.."+FAIL_COUNT+" times");
			  processURLUpdate(article);
			}
		}
		


  //background job to check if any article has EMPTY URLS 
  // save their image and update the url
  //future update to be a scheduled thread pool executor
  //currently only THread pool only		
  //feedup		
	@Override
	public void run() {
		List<Article>  articles = ad.findAll();
		for (Iterator<Article> iterator = articles.iterator(); iterator.hasNext();) {
			Article article = (Article) iterator.next();
			System.out.println(article.getId()+": "+article.getTitle());
			System.out.println("updating the urls==================");

			//check if the article URL is null. if YES then update it
			if(article.getUrl() == null || article.getUrl().isEmpty()){
				System.out.println("ID ="+article.getId()+" EMPTY URL");
				processURLUpdate(article);
			}
			
		}
	}
	
	
}
