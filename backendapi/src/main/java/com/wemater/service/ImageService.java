package com.wemater.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wemater.exception.DataNotInsertedException;
import com.wemater.modal.ImageModel;
import com.wemater.modal.ImageResponseModel;

public class ImageService {
    private  int FAIL_COUNT = 0;
    private static Logger log = Logger.getLogger(ImageService.class);

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
	public String processURLUpdate(String base64Image, String title){
		log.info("URL UPDATE started");
		Response resp = ConnectToCDN(base64Image,title );
			if(resp.getStatus() != 200 && FAIL_COUNT++ < 5){
				log.info("RESPONSE STATUS== "+resp.getStatus());
				 log.info("Image update---- FAILED.--- TRYING again--- @ "+FAIL_COUNT+" times");
				  processURLUpdate(base64Image, title);
			}
			else if(resp.getStatus() != 200 && FAIL_COUNT >5)
				  throw new DataNotInsertedException("unable to insert image at CDN");
			
			 log.info("Image update---- SUCCESS.--- RETURNING URL--");
			 return resp.readEntity(ImageResponseModel.class).getUrl();
		
		}
		
	


}
