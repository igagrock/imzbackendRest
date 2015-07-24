package com.wemater.modal;


public class Link {

	private String url;
	private String rel;
	

	public String getUrl() {
		return url;
	}
	public String getRel() {
		return rel;
	}
	private void setUrl(String url) {
		this.url = url;
	}
	private void setRel(String rel) {
		this.rel = rel;
	}



	private Link() {
		
	}


	public synchronized static Link createLink(String url, String rel){
		 
		Link link = new Link();
		
	    link.setUrl(url);
		link.setRel(rel);
		 
		return link;
	
	}
	
	
	
	
}
