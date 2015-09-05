package com.wemater.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.wemater.dto.Article;
import com.wemater.modal.ArticleModel;
import com.wemater.modal.Link;
import com.wemater.service.LinkService;









public class Runner {
	
      
	/**
	 * @param args
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {

		SessionFactory sf = HibernateUtil.getSessionFactory();
		SessionUtil su = new SessionUtil(sf.getCurrentSession());
		
		int firstResult =  3;
        int maxResult =  3;
		List<Article> articleList = null;
		
		try {
			
			su.beginSessionWithTransaction();
			
			articleList = su
					.getSession()
					.createQuery("from Article as article order by article.id desc")
					.setFirstResult(firstResult)
					.setMaxResults(maxResult)
					.setCacheable(true)
					.list();
			
			su.CommitCurrentTransaction();
		
			List<ArticleModel> modelList = transformArticlesToModels(articleList);
              for (Iterator<ArticleModel> iterator = modelList.iterator(); iterator.hasNext();) {
				ArticleModel articleModel = (ArticleModel) iterator.next();
				System.out.println(articleModel);
				
			}
			
		} catch (HibernateException e) {
		
			su.rollBackCurrentTransaction();
		}finally{
			sf.close();
		}
		
		
		
		
	}
	
	// service related methods
		private static List<ArticleModel> transformArticlesToModels(
				List<Article> articles) {
			List<ArticleModel> models = new ArrayList<ArticleModel>();

			for (Iterator<Article> iterator = articles.iterator(); iterator
					.hasNext();) {
				Article article = (Article) iterator.next();
				models.add(transformArticleToModel(article));

			}
			return models;

		}

		private static ArticleModel transformArticleToModel(Article article) {


			ArticleModel model = new ArticleModel().constructModel(article)
					.addCount(article.getCommentCount())
					.addLikes(article.getLikes())
				
					.addContent(article.returnContentString())
					.addImage(article.returnImageString())
					.addTags(article.getTags())
					.addUser(article.getUser(), true, false);
				

			return model;
		}

	
}


	

