package com.wemater.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wemater.dao.ArticleDao;
import com.wemater.dto.Article;
import com.wemater.modal.ImageModel;
import com.wemater.modal.ImageResponseModel;
import com.wemater.util.SessionUtil;

public class ImageService {
    private static int FAIL_COUNT = 0;
    private final ArticleDao ad;
    private static Logger log = Logger.getLogger(ImageService.class);
    
    
 
	
  public ImageService(SessionUtil su) {
		this.ad = new ArticleDao(su);
	}
//create method here to use the api to save the image 
 // update the url in the articles	
	
   private Response ConnectToCDN(String base64Image, String title){
	  log.info("Connecting to CDN ---------STARTED");
	   
	   ImageModel imd = new ImageModel();
	   imd.setBase64Image(base64Image);
	   imd.setTitle(title);
	   
	    Client postClient = ClientBuilder.newClient();
	    WebTarget postwebTarget =  postClient.target("http://cdnbackendapi-vbr.rhcloud.com/api/images");
	    Invocation.Builder  postBuilder = postwebTarget.request(MediaType.APPLICATION_JSON);
	    log.info("CDN connection callback ---RECIEVED- ");
	    log.info("Return Response callback to ProcessURLUpdate");
	     return postBuilder.post(Entity.entity(imd, MediaType.APPLICATION_JSON));

		
   }
	public void processURLUpdate(Article article){
		log.info("URL UPDATE started");
		Response resp = ConnectToCDN(article.returnImageString(), article.getTitle());
		if(resp.getStatus() == 200){
			log.info("RESPONSE STATUS IS ====200==== PROCESSING UPDATE URL");
			article.setUrl(resp.readEntity(ImageResponseModel.class).getUrl());
			ad.update(article);
			log.info("URL update------ DONE");
		}
		else if(FAIL_COUNT++ < 5){
              log.info("Image update---- FAILED.--- TRYING again--- @ "+FAIL_COUNT+" times");
			  processURLUpdate(article);
			}
		}
		


}
