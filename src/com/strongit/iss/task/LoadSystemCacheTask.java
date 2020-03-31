package com.strongit.iss.task;

import java.util.*;

import com.strongit.iss.common.Constant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.iss.common.Cache;
import com.strongit.iss.entity.Department;
import com.strongit.iss.entity.DictionaryItems;
import com.strongit.iss.service.ISystemCacheService;

public class LoadSystemCacheTask extends TimerTask {
	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ISystemCacheService systemCacheService;

	@Override
	//加载缓存
	public void run() {
		loadDicti();
		loadDep();
	}
	/**
	 * 缓存实体字典
	 * @orderBy 
	 * @author wuwei
	 * @Date 2016年8月2日下午3:08:41
	 */
	public void loadDicti(){
		List<DictionaryItems> dic = systemCacheService.getDicts();
		Map<String,DictionaryItems> maps = new HashMap<String,DictionaryItems>();
		Map<String,DictionaryItems> fullmaps = new HashMap<String,DictionaryItems>();
		Map<String,List<DictionaryItems>>  groupDicts=new HashMap<String,List<DictionaryItems>>(20);
		if (null != dic && dic.size() > 0) {
			for (DictionaryItems item : dic){
				maps.put(item.getGroupNo()+"@"+item.getItemKey(), item);
				//地区简写的路径
				if("1".equals(item.getGroupNo())){
					String fullname=item.getShortFullValue();
					if(null!=fullname) {
						// 名称全路径
						fullmaps.put(fullname.replaceAll(Constant.FULL_NAME_SPLIT, Constant.BI_SPLIT), item);					
					}
				}else{
					String fullname=item.getItemFullValue();
					if(null!=fullname) {
						// 名称全路径
						fullmaps.put(fullname.replaceAll(Constant.FULL_NAME_SPLIT, Constant.BI_SPLIT), item);					
					}
				}	
				// 加载groupNO
				List<DictionaryItems> list=groupDicts.get(item.getGroupNo());
				if(null==list){
					list= new ArrayList<DictionaryItems>();
				}
				list.add(item);
				groupDicts.put(item.getGroupNo(),list);
			}
		}
		Cache.setDicti(maps);
		// 名称全路径
		Cache.setFullDicti(fullmaps);
		// 设置分组项
		Cache.setGroupDicts(groupDicts);
	}
	
	/**
	 * 缓存部门实体
	 * @orderBy 
	 * @author wuwei
	 * @Date 2016年8月2日下午9:56:04
	 */
	public void loadDep(){
		List<Department> dep = systemCacheService.getDep();
		Map<String,Department> maps = new HashMap<String,Department>();
		if (null != dep && dep.size() > 0) {
			for (Department item : dep){
				maps.put(item.getDepartmentGuid(), item);
			}
		}
		Cache.setDep(maps);
	}
}
