package com.strongit.iss.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.strongit.iss.service.BaseService;
import net.sf.ehcache.Element;


@Service//初始类
@Transactional
public class ReportCacheServiceImpl  extends BaseService {
	
	@Resource
	net.sf.ehcache.CacheManager cacheManager;
	
	
	public String putReport(String reportData) {
		String uuid  = UUID.randomUUID().toString();
		Element element = new Element(uuid,reportData);
		cacheManager.getEhcache("ehcache").put(element);
		return uuid;
	}
	public String getReport(String uuid) {
		Element element = cacheManager.getEhcache("ehcache").get(uuid);
		if (element == null){
			return "";
		}
		return  (String)element.getObjectValue();		
	}	
	
	public Object getEverObject(String uuid) {
		Element element = cacheManager.getEhcache("everCache").get(uuid);
		if (element == null){
			return null;
		}
		return element.getObjectValue();		
	}
	public void putEverObject(String uuid,Object obj) {
		Element element = new Element(uuid,obj);
		cacheManager.getEhcache("everCache").put(element);
	}		
	
	public Object getTempObject(String uuid) {
		Element element = cacheManager.getEhcache("tempCache").get(uuid);
		if (element == null){
			return null;
		}
		return element.getObjectValue();		
	}
	public void putTempObject(String uuid,Object obj) {
		Element element = new Element(uuid,obj);
		cacheManager.getEhcache("tempCache").put(element);
	}		
	
	public String putReport(Map<String, String> reportParamsMap) {
		String uuid  = UUID.randomUUID().toString();
		Element element = new Element(uuid,reportParamsMap);
		cacheManager.getEhcache("ehcache").put(element);
		return uuid;
	}
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getReport1(String uuid) {
		Element element = cacheManager.getEhcache("ehcache").get(uuid);
		if (element == null){
			return new HashMap<String, String>();
		}
		return  (HashMap<String, String>)element.getObjectValue();		
	}
}
