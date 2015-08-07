package com.wemater.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.wemater.exception.DataNotFoundException;
import com.wemater.exception.DataNotInsertedException;
import com.wemater.exception.EvaluateException;
import com.wemater.util.SessionUtil;

public class GenericDaoImpl<T, Id extends Serializable> implements
		GenericDao<T, Id> {

	private final SessionUtil sessionUtil;
	private Class<T> type;

	// inject sessionUtil object at the runtime to use the session and type of
	// class at runtime
	public GenericDaoImpl(SessionUtil sessionUtil, Class<T> type) {
		this.sessionUtil = sessionUtil;
		this.type = type;
	}

	public SessionUtil getSessionUtil() throws InstantiationException {

		if (sessionUtil == null)
			throw new InstantiationException(
					"SessionUtil has not been set on DAO before usage");
		return sessionUtil;
	}

	@Override
	public long save(T entity) {
		Long id = null;

		try {
			sessionUtil.beginSessionWithTransaction();

			id = (Long) sessionUtil.getSession().save(entity);

			sessionUtil.CommitCurrentTransaction();

		} catch (RuntimeException e) {
			sessionUtil.rollBackCurrentTransaction();
			if(entity.getClass().getSimpleName().equals("User"))
			throw new DataNotInsertedException("Username already present");
			if(entity.getClass().getSimpleName().equals("Article"))
				throw new DataNotInsertedException("Title already present");
			else throw new EvaluateException(e);
			
		}

		return id;
	}

	@Override
	public void update(T entity) {

		try {
			sessionUtil.beginSessionWithTransaction();

			sessionUtil.getSession().update(entity);

			sessionUtil.CommitCurrentTransaction();

		} catch (RuntimeException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		List<T> entityList = new ArrayList<T>();

		try {

			sessionUtil.beginSessionWithTransaction();

			entityList = sessionUtil.getSession()
					.createQuery("from " + type.getSimpleName()).list();

			if (entityList.isEmpty())
				throw new DataNotFoundException("No " + type.getSimpleName()
						+ "'s present ");

			sessionUtil.CommitCurrentTransaction();

		} catch (RuntimeException e) {

			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}

		return entityList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T find(Id id) {

		T entity = null;
		try {
			sessionUtil.beginSessionWithTransaction();

			entity = (T) sessionUtil.getSession().get(type, id);

			if (entity == null)
				throw new DataNotFoundException(type.getSimpleName()
						+ " not found");

			sessionUtil.CommitCurrentTransaction();

		} catch (RuntimeException e) {
			sessionUtil.rollBackCurrentTransaction();
			System.out.println("inside the catch find by id");
			throw new EvaluateException(e);
		}

		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T find(String naturalId) {

		T entity = null;
		try {
			sessionUtil.beginSessionWithTransaction();

			entity = (T) sessionUtil.getSession().bySimpleNaturalId(type)
					.load(naturalId);

			if (entity == null)
				throw new DataNotFoundException(type.getSimpleName()
						+ " not found");

			sessionUtil.CommitCurrentTransaction();

		} catch (RuntimeException e) {
			sessionUtil.rollBackCurrentTransaction();
			System.out.println("inside the catch find natural id");
			throw new EvaluateException(e);
		}

		return entity;

	}

	@Override
	public void delete(T entity) {

		try {
			sessionUtil.beginSessionWithTransaction();
			sessionUtil.getSession().delete(entity);
			sessionUtil.CommitCurrentTransaction();

		} catch (HibernateException e) {
			sessionUtil.rollBackCurrentTransaction();
			throw new EvaluateException(e);
		}

	}

	// / write new here. use the HQl query and insert the parameter and
	// type.getsimplename
	@Override
	public void deleteAll() {

		System.out.println("delete all wont work");
	}

}
