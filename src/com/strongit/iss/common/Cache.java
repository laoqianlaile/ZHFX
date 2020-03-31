package com.strongit.iss.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.strongit.iss.entity.Department;
import com.strongit.iss.entity.DictionaryItems;

/**
 *   缓存类
 * @author wuwei
 * @createDate 2016年7月30日
 */
public class Cache {
    /**
     *  数据字典缓存来 
     *  code==name
     */
	//定义系统年份
	public static String systemYear = "2016";
	
	private static List<DictionaryItems>  list= new ArrayList<DictionaryItems>();

	private static Map<String,List<DictionaryItems>>  groupDicts=new HashMap<String,List<DictionaryItems>>(20);
	
	private static Map<String,DictionaryItems>  dicti= new HashMap<String,DictionaryItems>(50);
	private static Map<String,DictionaryItems>  fullItems= new HashMap<String,DictionaryItems>(50);
	private static Map<String,Department>  dep= new HashMap<String,Department>(20);
	
	public static void setGroupDicts(Map<String,List<DictionaryItems>>  groupDicts) {
		Cache.groupDicts = groupDicts;
	}
	
	public static void setDicti(Map<String,DictionaryItems> dicti) {
		Cache.dicti = dicti;
	}
	// 全路径的映射
	public static void setFullDicti(Map<String,DictionaryItems> dicti) {
		Cache.fullItems = dicti;
	}
	public static void setDep(Map<String,Department> dep) {
		Cache.dep = dep;
	}
	
	
	/**
	 * 根据groupNo和code得到对应的实体
	 * @orderBy 
	 * @param groupNo
	 * @param code
	 * @return
	 * @author wuwei
	 * @Date 2016年8月1日下午7:27:54
	 */
	public static DictionaryItems getdicti(String groupNo,String code) {
		if(StringUtils.isBlank(groupNo)){
			return new DictionaryItems();
		}
		else{
			//返回对应的实体
		  if(null == dicti.get(groupNo+"@"+code)){
			  return new DictionaryItems();
		  }else{
			  return  dicti.get(groupNo+"@"+code);
		  }
		}		
	}
	
	/**
	 * 
	 * @orderBy 根据groupNo得到对应的List集合
	 * @param groupNo
	 * @return 
	 * @author wuwei
	 * @Date 2016年8月1日上午11:20:49
	 */
	public static List<DictionaryItems> getGroupDicts(String groupNo) {
		if(StringUtils.isBlank(groupNo)){
			return new ArrayList<DictionaryItems>();
		}
		else{
			//返回groupNo对应的实体
		   return groupDicts.get(groupNo);
		}		
	}
	
	/**
	 * 
	 * @orderBy 根据groupNO和code得到对应的name
	 * @param groupNo
	 * @param code
	 * @return String
	 * @author wuwei
	 * @Date 2016年8月1日上午11:22:15
	 */
	public static  String  getNameByCode(String groupNo,String code){
		if(StringUtils.isBlank(code)){
			return "";
		}
		else{
			if(null == dicti.get(groupNo+"@"+code)){
				return "";
			}else{
				return dicti.get(groupNo+"@"+code).getItemValue();
			}			
		}		
	}

	/**
	 *
	 * @orderBy 根据全路径名称 得到CODE
	 * @param fullName  全路径名称
	 * @param
	 * @return String
	 * @author wuwei
	 * @Date 2016年8月1日上午11:22:15
	 */
	public static  String  getCodeByFullName(String fullName){
		if(StringUtils.isBlank(fullName)){
			return "";
		}
		else{
			if(null == fullItems.get(fullName)){
				return "";
			}else{
				return fullItems.get(fullName).getItemKey();
			}
		}
	}



	
	/**
	 * 根据code拿到全部的路径key
	 * @orderBy 
	 * @param groupNo
	 * @param code
	 * @return
	 * @author wuwei
	 * @Date 2016年10月24日下午5:39:26
	 */
	public static  String  getFullKeyByCode(String groupNo,String code){
		if(StringUtils.isBlank(code)){
			return "";
		}
		else{
			if(null == dicti.get(groupNo+"@"+code)){
				return "";
			}else{
				return dicti.get(groupNo+"@"+code).getItemFullKey();
			}			
		}		
	}
	
	/**
	 * 
	 * @orderBy 
	 * @param groupNo dictionary里面的groupNo字段
	 * @return
	 * @author lbw
	 * @Date 2016年8月5日下午2:16:30
	 */
	public static List<Map<String, Object>> getByGroupNo(String groupNo) {
		if (StringUtils.isBlank(groupNo)) {
			return new ArrayList<Map<String, Object>>();
		} else{
			//返回groupNo对应的实体集合
			List<DictionaryItems> list = Cache.groupDicts.get(groupNo);
			List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
			if (null != list && list.size() > 0) {
				//遍历集合
				for (DictionaryItems item : list) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("NAME", item.getItemValue());
					map.put("ID", item.getItemKey());
					map.put("PID", item.getItemParent());
					// 简称
					map.put("SHORTVALUE",item.getShortValue());
					resultList.add(map);					
				}
			}
			return resultList;
		}
	}

	/**
	 * 缓存部门实体
	 * @orderBy 
	 * @param DepartmentGuid
	 * @return
	 * @author wuwei
	 * @Date 2016年8月2日下午10:10:26
	 */
	public static Department getDep(String DepartmentGuid) {
		if(StringUtils.isBlank(DepartmentGuid)){
			return new Department();
		}
		else{
			if(null == dep.get(DepartmentGuid)){
				return new Department(); 
			}else{
				//返回groupNo对应的实体
				   return dep.get(DepartmentGuid);
			}
		}		
	}
	/**
	 * 缓存部门名称
	 * @orderBy 
	 * @param DepartmentGuid
	 * @return
	 * @author wuwei
	 * @Date 2016年8月3日上午10:01:43
	 */
	public static String getDepNameById(String DepartmentGuid) {
		if(StringUtils.isBlank(DepartmentGuid)){
			return "";
		}else{
			if(null == dep.get(DepartmentGuid)){
				return "";
			}else{
				return dep.get(DepartmentGuid).getDepartmentName();
			}
		}
	}
	
	public static List<DictionaryItems> getList() {
		return list;
	}
	public static void setList(List<DictionaryItems> list) {
		Cache.list = list;
	}	
}
