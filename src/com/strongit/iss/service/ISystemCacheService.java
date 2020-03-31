package com.strongit.iss.service;

import java.util.List;

import com.strongit.iss.entity.Department;
import com.strongit.iss.entity.Dictionary;
import com.strongit.iss.entity.DictionaryItems;

public interface ISystemCacheService {
	//缓存数据字典items表
	public List<DictionaryItems> getDicts();
	//缓存数据字典
	public List<Dictionary> getDict();
	//缓存部门对象
	public List<Department>getDep();
}
