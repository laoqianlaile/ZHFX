package com.strongit.iss.service;

import java.util.Map;

import com.strongit.iss.exception.BusinessServiceException;

public interface IQueryConditionService {

	public String getIQueryCondition(Map<String,String> reportParamsMap)throws BusinessServiceException;
}
