package com.wemater.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import com.wemater.modal.ArticleModel;
import com.wemater.modal.ImageModel;
import com.wemater.modal.ImageResponseModel;
import com.wemater.modal.UserModel;









public class Runner {
	
      
	/**
	 * @param args
	 * @throws IOException 
	 */

	public static void main(String[] args) throws IOException {

	
   Client client = ClientBuilder.newClient();
   WebTarget target = client.target("http://backendapi-vbr.rhcloud.com/api").path("users").path("irshsheikh").path("articles/107");
   System.out.println(target.getUri().toURL().toString());
   Invocation.Builder  builder = target.request(MediaType.APPLICATION_JSON);
   builder.header("Authorization", "Base "+new String(Base64.encodeBase64("irshsheikh:International0401".getBytes())));
   
   Response response = builder.get();
   System.out.println(response.getStatus());
   
    ArticleModel model = response.readEntity(ArticleModel.class);
  

       ImageModel imd = new ImageModel();
       imd.setBase64Image(model.getImage());
       imd.setTitle(model.getTitle());
    //tag the new api
    
    Client postClient = ClientBuilder.newClient();
    WebTarget postwebTarget =  postClient.target("http://cdnbackendapi-vbr.rhcloud.com/api/images");
    Invocation.Builder  postBuilder = postwebTarget.request(MediaType.APPLICATION_JSON);
     Response resp = postBuilder.post(Entity.entity(imd, MediaType.APPLICATION_JSON));

	 System.out.println(resp.getStatus());
	
	ImageResponseModel imddd = resp.readEntity(ImageResponseModel.class);
	System.out.println(imddd.getName());
	System.out.println(imddd.getUrl());
	
	

		
}
	
	public static BufferedImage decodeToImage(String imageString){
		
		  BufferedImage image = null;
		  byte[] imageByte = null;
		  
		  try {
			
			  imageByte = java.util.Base64.getDecoder().decode(imageString);
			  System.out.println(imageByte);
			  ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			  image = ImageIO.read(bis);

			  bis.close();
			  
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return image;
	}
	
	public static String encodeToString(BufferedImage image, String type){
		
		String imageString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
				
		try {
			ImageIO.write(image, type, bos);
			byte[] byteArray = bos.toByteArray();
			
			imageString = Base64.encodeBase64String(byteArray);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return imageString;
	}


}