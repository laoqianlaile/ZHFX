package com.strongit.iss.service;

import com.strongit.iss.exception.BusinessServiceException;

public interface IBaseService {
	/**
	 * 
	 * @author			杨硕
	 * @createDate		2015-11-29下午8:01:42
	 * @arithMetic                                                                           
	 * @param entity
	 * @throws BusinessServiceException
	 */
	public <T> void save(T entity) throws BusinessServiceException;
	
	public <T> void update(T entity) throws BusinessServiceException;
	
	public <T> void saveOrUpdate(T entity) throws BusinessServiceException;

}
