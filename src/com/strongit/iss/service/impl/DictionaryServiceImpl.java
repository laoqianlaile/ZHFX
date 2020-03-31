package com.strongit.iss.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.common.Constant;
import com.strongit.iss.entity.Dict;
import com.strongit.iss.entity.DictItems;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.IDictionaryService;

/**
 * * 字典服务
 * 缓存使用机制：
 * 1、所有查询列表的方法使用缓存存储，第二次查询时，默认查询缓存中内容。
 * 2、字典组或字典项相关的增、删、改、查均完全清除缓存dictionary中的所有内容。
 * 3、字典组或字典项单个对象的查询不使用缓存机制，默认查询数据库中最新内容。
 * @author TANGHW
 *
 */
@Service
@Transactional
public class DictionaryServiceImpl extends BaseService implements  IDictionaryService {
	/**
	  * 数据字典组树
	  * @orderBy 
	  * @return
	  * @author tanghw
	  * @Date 2016年12月14日下午3:48:37
	  */
	@Override
	@Transactional(readOnly=true)
	public List<Dict> getDictionaryTree() {
		String sql = " from Dict t where 1=1 ";
		return this.dao.findInCache(sql);
	}
	/**
	  * 根据字典组ID获取字典项列表
	  * @orderBy 
	  * @param groupNo
	  * @return
	  * @author tanghw
	  * @Date 2016年12月14日下午4:01:02
	  */
	@Override
	@Transactional(readOnly=true)
	public  Page<DictItems> getItemListByGroupNo(Page<DictItems> dictItemsPage,String groupNo) {
		String hql = "from DictItems where 1=1 ";
		if(StringUtils.isNotBlank(groupNo)){
			hql += " and dictionary.groupNo="+ groupNo + "";
		}
		hql += " order by itemKey,PXH";
		dictItemsPage = this.dao.find(dictItemsPage, hql, new Object[]{});
		return dictItemsPage;
	}
	
	/**
	  * 根据字典组Id获取字典组实体
	  * @orderBy 
	  * @param groupNo
	  * @return
	  * @author tanghw
	  * @Date 2016年12月14日下午7:17:54
	  */
	@Override
	@Transactional(readOnly=true)
	public Dict getDictByGroupNo(String groupNo) {
//		String hql = "from Dict where groupNo= :groupNo";
//		Map<String, Object> values = Maps.newHashMap();
//		values.put("groupNo", groupNo);
		return this.dao.findUniqueByProperty(Dict.class, "groupNo",Long.valueOf(groupNo));
	}
	 /**
	  * 添加字典组
	  * @orderBy 
	  * @param dictionary
	  * @return
	  * @throws BusinessServiceException
	  * @author tanghw
	  * @Date 2016年12月14日下午8:36:11
	  */
	@Override
	public boolean addDict(Dict dictionary) throws BusinessServiceException {
		checkDictKey(dictionary.getGroupKey(), dictionary.getGroupNo());
    	checkDictName(dictionary.getGroupName(), dictionary.getGroupNo());
    	try{
    		this.save(dictionary);
    		return true;
    	}catch (Exception e){
    		e.printStackTrace();
    		return false;
    	}
	}
	 /**
	  * 检查字典组key是否重复
	  * @orderBy 
	  * @param key
	  * @param groupNo
	  * @throws BusinessServiceException
	  * @author tanghw
	  * @Date 2016年12月14日下午8:38:33
	  */
	@Override
	@Transactional(readOnly=true)
	public void checkDictKey(String key, Long groupNo) throws BusinessServiceException {
    	String sql = " from Dict d where 1=1  "
				+ " and d.groupKey = '" + key + "' ";
    	if(groupNo != null && !"".equals(groupNo)) {
    		sql += " and d.groupNo !='"+groupNo+"'";
        }
		List<Dict> list = this.dao.find(sql, new Object[]{});
        if(list.size() > 0) {
            throw new BusinessServiceException(Constant.ZDZBH_IS_EXISTED);
        }
	}
	 /**
	  * 检查字典组名称是否重复
	  * @orderBy 
	  * @param name
	  * @param groupNo
	  * @throws BusinessServiceException
	  * @author tanghw
	  * @Date 2016年12月14日下午8:39:14
	  */
	@Override
	@Transactional(readOnly=true)
	public void checkDictName(String name, Long groupNo) throws BusinessServiceException {
        String sql = " from Dict d where 1=1  "
				+ " and d.groupName = '" + name + "' ";
    	if(groupNo != null && !"".equals(groupNo)) {
    		sql += " and d.groupNo !='"+groupNo+"'";
        }
		List<Dict> list = this.dao.find(sql, new Object[]{});
        if(list.size() > 0) {
            throw new BusinessServiceException(Constant.ZDZMC_IS_EXISTED);
        }
	}
	/**
	  * 根据字典项id获取字典项对象
	  * @orderBy 
	  * @param id
	  * @return
	  * @author tanghw
	  * @Date 2016年12月15日下午1:50:54
	  */
	@Override
	@Transactional(readOnly=true)
	public DictItems getDictItemsById(String id) {
		return this.dao.findUniqueByProperty(DictItems.class, "itemGuid",id);
	}
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
	@Override
	public boolean addDictItem(DictItems dictionaryitem, String oldKey, String oldValue)
			throws BusinessServiceException {
				//判断字典项编号是否在同一字典组中是否重复
				if(StringUtils.isNotBlank(dictionaryitem.getItemGuid())) {
					isDictChildren(dictionaryitem.getItemKey(), dictionaryitem.getDictionary().getGroupNo(),
							dictionaryitem.getParentItemId());
				}
				if(dictionaryitem.getItemKey().equals(dictionaryitem.getParentItemId())){
					throw new BusinessServiceException("不能选择自己作为父字典项");
				}
				//判断字典项编号是否在同一字典组中是否重复
				checkDictItemKey(dictionaryitem.getItemKey(), dictionaryitem.getItemGuid(), dictionaryitem.getDictionary().getGroupNo());
				//判断字典项名称是否在同一字典组中是否重复
				checkDictItemsName(dictionaryitem.getItemValue(), dictionaryitem.getItemGuid(), dictionaryitem.getDictionary().getGroupNo(),dictionaryitem.getParentItemId());
				try{
					//取得上级字典项的key
					String parentItemKey = dictionaryitem.getParentItemId(); 
					Long groupNo = dictionaryitem.getDictionary().getGroupNo();
//					//定义DictItems的map对象
//					DictItems tempDictItem = dictionaryitem;
					//定义获取字典项全称的字典项map对象
					Map<String, Object> tempDictItem1 = new HashMap<>();
					//放入当前字典项的父级id
					tempDictItem1.put("ITEM_PARENT", dictionaryitem.getParentItemId());
					//所有父级的key
					String parentItemAll = parentItemKey;
					String parentItemKeyTmp = parentItemKey;
					//当前的字典项key
					String itemKeyTmp = dictionaryitem.getItemKey();
					//定义字典全称
					String itemValueAll = dictionaryitem.getItemValue();
					while(StringUtils.isNotBlank(parentItemKeyTmp)){
						//取得上级字典项实体
						Map<String, Object> tempDictItem = this.getDictItemsByGroupNoAndItemKey(groupNo, parentItemKeyTmp);
						if(tempDictItem!=null&&tempDictItem.get("ITEM_PARENT")!=null&&!"".equals(tempDictItem.get("ITEM_PARENT"))){
							parentItemAll = parentItemAll + "-" + tempDictItem.get("ITEM_PARENT").toString();
						}
						parentItemKeyTmp = tempDictItem.get("ITEM_PARENT").toString();
					}
					
					while(StringUtils.isNotBlank(itemKeyTmp)){
						if(tempDictItem1!=null&&tempDictItem1.get("ITEM_PARENT")!=null&&!"".equals(tempDictItem1.get("ITEM_PARENT"))){
							tempDictItem1 = this.getDictItemsByGroupNoAndItemKey(groupNo, tempDictItem1.get("ITEM_PARENT").toString());
							itemValueAll = itemValueAll + "-" + tempDictItem1.get("ITEM_VALUE");
						}
						if(tempDictItem1!=null&&tempDictItem1.get("ITEM_PARENT")!=null&&!"".equals(tempDictItem1.get("ITEM_PARENT"))){
							itemKeyTmp =tempDictItem1.get("ITEM_PARENT").toString();
						}else{
							itemKeyTmp=null;
						}
					}
					dictionaryitem.setItemParentAll(parentItemAll);
					dictionaryitem.setItemFullValue(itemValueAll);
					//1.0拿过来的子字典修改方法
					String itemFullKey = "";
					if(parentItemAll == null || "".equals(parentItemAll)){
						itemFullKey = dictionaryitem.getItemKey();
					}else{
						itemFullKey = dictionaryitem.getItemKey()+"-"+parentItemAll;
					}
					if(oldKey != null && oldValue != null && !"".equals(oldKey) && !"".equals(oldValue)){
						this.updateChildren(groupNo, itemValueAll, itemFullKey, oldKey, oldValue);
					}
					//父字典修改key子字典不显示问题
					this.updateChildren(dictionaryitem);
					
					this.dao.save(dictionaryitem);
					return true;
				}catch (Exception e){
//					logger.error("(接口名：addDictItem(DictionaryItems dictionaryitem,String oldKey, String oldValue))================================>" + e.getMessage());
					e.printStackTrace();
					return false;
				}
	}
	/**
	 * 根据字典组id和字典项key获取字典项
	 * @orderBy 
	 * @param groupNo
	 * @param itemKey
	 * @return
	 * @author tanghw
	 * @Date 2016年12月15日下午2:23:23
	 */
	@Transactional(readOnly=true)
	private Map<String, Object> getDictItemsByGroupNoAndItemKey(Long groupNo,
			String itemKey) {
		try {
			String sql = " select ITEM_VALUE,ITEM_PARENT from DICTIONARY_ITEMS where group_No='"+groupNo+"'  and ITEM_KEY='"+itemKey+"'";
			List<Map<String, Object>> list = this.dao.findBySql(sql);
			if(list.size()>0){
				return list.get(0);
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return null;
	}
	/**
	 * 修改字典项的父级时，对应子集的ITEM_PARENT_ALL、ITEM_FULL_VALUE自动更新
	 * @orderBy 
	 * @param groupNo
	 * @param ItemFullValue
	 * @param parentItemAll
	 * @param itemKey
	 * @param itemValue
	 * @throws BusinessServiceException
	 * @author tanghw
	 * @Date 2016年12月15日下午2:44:22
	 */
	
	private  void updateChildren(Long groupNo, String ItemFullValue, String parentItemAll, String itemKey, String itemValue)
			throws BusinessServiceException{
		//所有父级字典项编号不为空时
		StringBuffer hql = new StringBuffer(
			//修改编号为110，所有父级编号由110变为110-C...,它对应的子集也随之变化   A-B-110 变为  A-B-110-C... 
				"UPDATE DICTIONARY_ITEMS SET ITEM_FULL_VALUE = INSTR"
			+ "("
			+ "ITEM_FULL_VALUE,"
			+ " INSTR("
			+ "ITEM_FULL_VALUE,"
			+ " '" + itemValue + "',"
			+ " -1)," 
			+ " LENGTH('" + itemValue + "'),"
			+ " '" + ItemFullValue + "'"
			+ ")"
			+ " WHERE GROUP_NO = '" + groupNo + "'"
			+ "  AND ITEM_FULL_VALUE like '%"+itemValue+"'"
//			+ " AND SUBSTRING(ITEM_FULL_VALUE," 
//			+ " INSTR(ITEM_FULL_VALUE, '-', - 1) + 1,"
//			+ " LENGTH(ITEM_FULL_VALUE))"
//			+ " = '" + itemValue + "'"
			+ ";"

			//修改值为110 把他的父级值由110-A-B修改为110-B...||110,它对应的子集也随之变化   C-110-A-B 变为  C-110-B... || C-110
				+ " UPDATE DICTIONARY_ITEMS SET ITEM_FULL_VALUE = INSTR"
			+ "("
			+ "ITEM_FULL_VALUE,"
			+ " INSTR("
			+ "ITEM_FULL_VALUE,"
			+ " '-" + itemValue + "-')"
			+ " + 1,"
			+ " LENGTH(ITEM_FULL_VALUE) -"
			+ " INSTR("
			+ "ITEM_FULL_VALUE,"
			+ " '-" + itemValue + "-'),"
			+ " '" + ItemFullValue + "'"
			+ ")"
			+ " WHERE GROUP_NO = '" + groupNo + "'"
			+ " AND nvl("
			+ "INSTR(ITEM_FULL_VALUE, '-" + itemValue + "-'),"
			+ " 0) > 0"
			+ ";"
			//修改子字典ITEM_PARENT_ALL
			 +"UPDATE DICTIONARY_ITEMS SET ITEM_PARENT_ALL = INSTR"
			+ "("
			+ "ITEM_PARENT_ALL,"
			+ " INSTR("
			+ "ITEM_PARENT_ALL,"
			+ " '" + itemKey + "',"
			+ " -1)," 
			+ " LENGTH('" + itemKey + "'),"
			+ " '" + parentItemAll + "'"
			+ ")"
			+ " WHERE GROUP_NO = '" + groupNo + "'"
			+ "  AND ITEM_PARENT_ALL like '%"+itemKey+"'"
//			+ " AND SUBSTRING(ITEM_PARENT_ALL," 
//			+ " INSTR(ITEM_PARENT_ALL, '-', - 1) + 1,"
//			+ " LENGTH(ITEM_PARENT_ALL))"
//			+ " = '" + itemKey + "'"
			+ ";"
			
					+" UPDATE DICTIONARY_ITEMS SET ITEM_PARENT_ALL = INSTR"
			+ "("
			+ "ITEM_PARENT_ALL,"
			+ " INSTR("
			+ "ITEM_PARENT_ALL,"
			+ " '" + itemKey + "-')"
			+ ","
			+ " LENGTH(ITEM_PARENT_ALL) -"
			+ " INSTR("
			+ "ITEM_PARENT_ALL,"
			+ " '" + itemKey + "-') +1,"
			+ " '" + parentItemAll + "'"
			+ ")"
			+ " WHERE GROUP_NO = '" + groupNo + "'"
			+ " AND nvl("
			+ "INSTR(ITEM_PARENT_ALL, '" + itemKey + "-'),"
			+ " 0) > 0"
			+ ";"
			
			//修改编号由110,所有父级编号由110变为110-C...,它对应的子集也随之变化   A-B-110 变为  A-B-110-C...
					+"  UPDATE DICTIONARY_ITEMS SET ITEM_PARENT_ALL = INSTR"
			+ "("
			+ "ITEM_PARENT_ALL,"
			+ " INSTR("
			+ "ITEM_PARENT_ALL,"
			+ "'" + itemKey + "',"
			+ " -1)"
			+ " + LENGTH('" + itemKey + "'),"
			+ " 0,"
			+ " DECODE('" + parentItemAll + "'," 
			+ " '', '', '-" + parentItemAll + "')"
			+ ")"
			+ " WHERE GROUP_NO = '" + groupNo + "'"
			+ "  AND ITEM_PARENT_ALL like '%"+itemKey+"'"
//			+ " AND SUBSTRING(ITEM_PARENT_ALL," 
//			+ " INSTR("
//			+ "ITEM_PARENT_ALL,"
//			+ " '-',"
//			+ " -1)"
//			+ " + 1,"
//			+ " LENGTH(ITEM_PARENT_ALL))"
//			+ " = '" + itemKey + "'"
			+ ";"
			//--修改编号为110 把他的父级编号由A-B修改为B...||'',它对应的子集也随之变化   C-110-A-B 变为  C-110-B... || C-110
					+"  UPDATE DICTIONARY_ITEMS SET ITEM_PARENT_ALL = INSTR"
			+ "("
			+ "ITEM_PARENT_ALL,"
			+ " INSTR("
			+ "ITEM_PARENT_ALL,"
			+ " '-" + itemKey + "-')"
			+ " + LENGTH('" + itemKey + "')"
			+ " + 1,"
			+ " LENGTH(ITEM_PARENT_ALL) -"
			+ " INSTR("
			+ "ITEM_PARENT_ALL,"
			+ " '-" + itemKey + "-')"
			+ " - LENGTH('" + itemKey + "'),"
			+ " decode('" + parentItemAll + "'," 
			+ " '', '', '-" + parentItemAll + "')"
			+ ")"
			+ " WHERE GROUP_NO = '" + groupNo + "'"
			+ " AND nvl("
			+ "INSTR(ITEM_PARENT_ALL, '-" + itemKey + "-'),"
			+ " 0) > 0"
			+ ";"
			//修改编号为110 把他的父级编号由A-B修改为B...||'',它对应的子集也随之变化   110-A-B 变为  110-B... || 110
					+"  UPDATE DICTIONARY_ITEMS SET ITEM_PARENT_ALL = INSTR"
			+ "("
			+ "ITEM_PARENT_ALL,"
			+ " INSTR("
			+ "ITEM_PARENT_ALL,"
			+ " '-'),"
			+ " LENGTH(ITEM_PARENT_ALL),"
			+ " decode('" + parentItemAll + "'," 
			+ " '', '', '-" + parentItemAll + "')"
			+ ")"
			+ " WHERE GROUP_NO = '" + groupNo + "'"
			+ " AND (INSTR(ITEM_PARENT_ALL,"
			+ " INSTR("
			+ "ITEM_PARENT_ALL,"
			+ " '-'),"
			+ " LENGTH(ITEM_PARENT_ALL),"
			+ " '')"
			+ " = '" + itemKey + "'"
			+ ")"
			+ ";"
			
			);
		this.dao.createSqlQuery(hql.toString());
		this.dao.flush();
	}
	/**
	 * 修改子字典的item_parent
	 * @orderBy 
	 * @param dictionaryitem
	 * @author tanghw
	 * @Date 2016年12月15日下午2:47:07
	 */
	private void updateChildren(DictItems dictionaryitem){
		String groupNo = dictionaryitem.getDictionary().getGroupNo().toString();
		String guid = dictionaryitem.getItemGuid();
		String key = dictionaryitem.getItemKey();
		String hql = "UPDATE DICTIONARY_ITEMS SET ITEM_PARENT = '"+key+"' "
				+ "WHERE GROUP_NO = '"+groupNo+"' AND ITEM_PARENT IN "
				+ "(SELECT ITEM_KEY FROM DICTIONARY_ITEMS WHERE ITEM_GUID = '"+guid+"')";
		this.dao.createSqlQuery(hql);
		this.dao.flush();
	}
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
	@Override
	@Transactional(readOnly=true)
	public void isDictChildren(String itemKey, Long groupNo, String parentItemId) throws BusinessServiceException {
		if(parentItemId != null && !"".equals(parentItemId)) {
			String sql =" select * from DICTIONARY_ITEMS di where di.group_No ='" + groupNo + "'";
			 if(parentItemId != null && !"".equals(parentItemId)) {
		            sql += " and di.ITEM_KEY='"+parentItemId+"'";
		        }
		        if(itemKey != null && !"".equals(itemKey)) {
		            sql += " and di.ITEM_PARENT_ALL like '%"+itemKey+ "%'";
		        }
			List<Map<String, Object>> list = this.dao.findBySql(sql);
	        if(list.size() > 0) {
	            throw new BusinessServiceException(Constant.CHILDREN_IS_EXISTED);
	        }
		}
	}
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
	@Override
	@Transactional(readOnly=true)
	public void checkDictItemKey(String itemKey, String itemGuid, Long groupNo) throws BusinessServiceException {
    	String sql =" select * from DICTIONARY_ITEMS di where di.ITEM_KEY ='" + itemKey + "'";
		 if(itemGuid != null && !"".equals(itemGuid)) {
	            sql += " and di.ITEM_GUID!='"+itemGuid+"'";
	        }
	        if(groupNo != null && !"".equals(groupNo)) {
	            sql += " and di.group_No ='" + groupNo + "'";
	        }
		List<Map<String, Object>> list = this.dao.findBySql(sql);
        if(list.size() > 0) {
            throw new BusinessServiceException(Constant.ZDXBH_IS_EXISTED);
        }
	}
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
	@Override
	@Transactional(readOnly=true)
	public void checkDictItemsName(String itemValue, String itemGuid, Long groupNo, String parentItemId)
			throws BusinessServiceException {
		String sql =" select * from DICTIONARY_ITEMS di where di.ITEM_VALUE ='" + itemValue + "' and di.ITEM_PARENT ='"+parentItemId+"'";
		 if(itemGuid != null && !"".equals(itemGuid)) {
	            sql += " and di.ITEM_GUID!='"+itemGuid+"'";
	        }
	        if(groupNo != null && !"".equals(groupNo)) {
	            sql += " and di.group_No ='" + groupNo + "'";
	        }
		List<Map<String, Object>> list = this.dao.findBySql(sql);
        if(list.size() > 0) {
            throw new BusinessServiceException(Constant.ZDXMC_IS_EXISTED);
        }
	}
	 /**
	  * 根据字典组ID获取字典项列表
	  * @orderBy 
	  * @param groupNo
	  * @return
	  * @throws BusinessServiceException
	  * @author tanghw
	  * @Date 2016年12月15日下午4:43:41
	  */
	@Override
	@Transactional(readOnly=true)
	public List<DictItems> getItemListByGroupNo(String groupNo) throws BusinessServiceException {
		String hql = "from DictItems where 1=1 ";
		if(StringUtils.isNotBlank(groupNo)){
			hql += " and dictionary.groupNo="+ groupNo + "";
		}
		hql += " order by PXH, itemKey ";
		return this.dao.findInCache(hql);
	}
}
