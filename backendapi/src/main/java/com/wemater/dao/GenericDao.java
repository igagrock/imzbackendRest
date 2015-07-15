package com.wemater.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, Id extends Serializable> {
	
	   public long save(T entity);
	   public void update(T entity);
	   public T find(Id id);
	   public T find(String naturalId);
	   public  void delete(T entity);
	   public List<T> findAll();
	   public void deleteAll();
	   
	   
	   
}
