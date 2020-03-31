package com.strongit.iss.service;

import java.util.List;

import com.strongit.iss.entity.Dict;
import com.strongit.iss.entity.DictItems;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.orm.hibernate.Page;

/**
 * 字典Service
 * @author tanghw
 * @date 20160122
 */
 public interface IDictionaryService extends IBaseService {
	 
	 /**
	  * 数据字典组树
	  * @orderBy 
	  * @return
	  * @author tanghw
	  * @Date 2016年12月14日下午3:48:37
	  */
	 public List<Dict> getDictionaryTree();
	 
	 /**
	  * 根据字典组ID获取字典项列表
	  * @orderBy 
	  * @param groupNo
	  * @return
	  * @author tanghw
	  * @Date 2016年12月14日下午4:01:02
	  */
	 public  Page<DictItems> getItemListByGroupNo(Page<DictItems> dictItemsPage,String groupNo);
	 
	 /**
	  * 根据字典组Id获取字典组实体
	  * @orderBy 
	  * @param groupNo
	  * @return
	  * @author tanghw
	  * @Date 2016年12月14日下午7:17:54
	  */
	 public Dict getDictByGroupNo(String groupNo);
	 /**
	  * 添加字典组
	  * @orderBy 
	  * @param dictionary
	  * @return
	  * @throws BusinessServiceException
	  * @author tanghw
	  * @Date 2016年12月14日下午8:36:11
	  */
	 public boolean addDict(Dict dictionary) throws BusinessServiceException;
	 /**
	  * 检查字典组key是否重复
	  * @orderBy 
	  * @param key
	  * @param groupNo
	  * @throws BusinessServiceException
	  * @author tanghw
	  * @Date 2016年12月14日下午8:38:33
	  */
	 public void checkDictKey(String key, Long groupNo) throws BusinessServiceException;
	 /**
	  * 检查字典组名称是否重复
	  * @orderBy 
	  * @param name
	  * @param groupNo
	  * @throws BusinessServiceException
	  * @author tanghw
	  * @Date 2016年12月14日下午8:39:14
	  */
	 public void checkDictName(String name, Long groupNo) throws BusinessServiceException;
	 /**
	  * 根据字典项id获取字典项对象
	  * @orderBy 
	  * @param id
	  * @return
	  * @author tanghw
	  * @Date 2016年12月15日下午1:50:54
	  */
	 public DictItems getDictItemsById(String id);
	 
	 /**
	  * 添加字典项
	  * @orderBy 
	  * @param dictionaryitem
	  * @param oldKey
	  * @param oldValue
	  * @return
	  * @throws BusinessServiceException
	  * @author tanghw
	  * @Date 2016年12月15日下午1:55:36
	  */
	 public boolean addDictItem(DictItems dictionaryitem, String oldKey, String oldValue) throws BusinessServiceException;
	 
	 /**
	  * 判断字典项编号是否在同一字典组中是否重复
	  * @orderBy 
	  * @param itemKey
	  * @param groupNo
	  * @param parentItemId
	  * @throws BusinessServiceException
	  * @author tanghw
	  * @Date 2016年12月15日下午1:58:47
	  */
	 public void isDictChildren(String itemKey, Long groupNo, String parentItemId) throws BusinessServiceException;
	 /**
	  * 判断字典项编号是否在同一字典组中是否重复
	  * @orderBy 
	  * @param itemKey
	  * @param itemGuid
	  * @param groupNo
	  * @throws BusinessServiceException
	  * @author tanghw
	  * @Date 2016年12月15日下午1:59:56
	  */
	 public void checkDictItemKey(String itemKey, String itemGuid, Long groupNo) throws BusinessServiceException;
	 
	 /**
	  * 判断字典项名称是否在同一字典组中是否重复
	  * @orderBy 
	  * @param itemValue
	  * @param itemGuid
	  * @param groupNo
	  * @param parentItemId
	  * @throws BusinessServiceException
	  * @author tanghw
	  * @Date 2016年12月15日下午2:00:29
	  */
	 public void checkDictItemsName(String itemValue, String itemGuid, Long groupNo ,String parentItemId) throws BusinessServiceException;
	 
	 /**
	  * 根据字典组ID获取字典项列表
	  * @orderBy 
	  * @param groupNo
	  * @return
	  * @throws BusinessServiceException
	  * @author tanghw
	  * @Date 2016年12月15日下午4:43:41
	  */
	 public List<DictItems> getItemListByGroupNo(String groupNo)throws BusinessServiceException; 
 }
