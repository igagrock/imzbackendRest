package com.wemater.util;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.wemater.dao.ArticleDao;
import com.wemater.dto.Article;
import com.wemater.service.ImageService;

public class ImageBackupUtil implements Runnable {
	private static final Logger log = Logger.getLogger(ImageBackupUtil.class);
	private final SessionUtil su;
	private final ArticleDao ad;
	private final ImageService is;

	public ImageBackupUtil() {
		     this.su = new SessionUtil(HibernateUtil.getSessionFactory().openSession());
			this.ad = new ArticleDao(su);
			this.is = new ImageService(su);
	}


		//background job to check if any article has EMPTY URLS 
	  // save their image and update the url
	  //future update to be a scheduled thread pool executor
	  //currently only THread pool only		
	  //feedup		
		@Override
		public void run() {
			log.info("Backup image util started ");
			List<Article> articles = ad.findAll();
			for (Iterator<Article> iterator = articles.iterator(); iterator.hasNext();) {
				Article article = (Article) iterator.next();
				is.processURLUpdate(article);
				
			}
		}
		

}
 