package com.strongit.iss.service;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.iss.dao.DaoContext;
import com.strongit.iss.exception.BusinessServiceException;

/**
 * 业务逻辑基类
 * 
 * @author zhaojian
 * @version 1.0
 */
public class BaseService { 
	protected final Logger logger = Logger.getLogger(getClass());
	protected final Logger businessLogger = Logger.getLogger("business");

	@Autowired
	protected DaoContext dao;
	
	public <T> T get(Class<T> clazz, Serializable id) throws BusinessServiceException{
		return dao.get(clazz, id);
	}
	
	public <T> void save(T entity) throws BusinessServiceException{
		dao.save(entity);
	}
	public <T> void update(T entity) throws BusinessServiceException{
		dao.update(entity);
	}
	public <T> void saveOrUpdate(T entity) throws BusinessServiceException{
		dao.saveOrUpdate(entity);
	} 
}
