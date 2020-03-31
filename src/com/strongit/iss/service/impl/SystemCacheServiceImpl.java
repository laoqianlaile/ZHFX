package com.strongit.iss.service.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.entity.Department;
import com.strongit.iss.entity.Dictionary;
import com.strongit.iss.entity.DictionaryItems;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.ISystemCacheService;

@Service
@Transactional
public class SystemCacheServiceImpl  extends BaseService implements ISystemCacheService{
	
	/**
	 * 获取字典表
	 * @orderBy 
	 * @return
	 * @author wuwei
	 * @Date 2016年10月24日下午5:34:56
	 */
	@SuppressWarnings("unchecked")
	@Override
	//获取字典缓存
	public List<DictionaryItems> getDicts() {	
		String sql = "select group_no as \"groupNo\",ITEM_PARENT as \"itemParent\",item_key as \"itemKey\",item_full_key as \"itemFullKey\",item_value as \"itemValue\",item_full_value as \"itemFullValue\",short_full_value as \"shortFullValue\",short_value as \"shortValue\"from Dictionary_Items order by group_no,item_key ";
		SQLQuery query=this.dao.createSqlQuery(sql.toString());
	    // 将结果转成Bean
		query.setResultTransformer(Transformers.aliasToBean(DictionaryItems.class));
        // 设置转化的结果
        query.addScalar("groupNo", Hibernate.STRING);
        query.addScalar("itemKey", Hibernate.STRING);
        query.addScalar("itemValue", Hibernate.STRING);
        query.addScalar("itemParent", Hibernate.STRING);
        query.addScalar("itemFullKey", Hibernate.STRING);
        query.addScalar("itemFullValue", Hibernate.STRING);
        query.addScalar("shortFullValue", Hibernate.STRING);
		query.addScalar("shortValue", Hibernate.STRING);
        List<DictionaryItems> dict = query.list();
		return dict;
	}
	
	/**
	 * 数据字典
	 * @orderBy 
	 * @return
	 * @author wuwei
	 * @Date 2016年10月24日下午5:35:07
	 */
	@SuppressWarnings("unchecked")
	@Override
	//获取数据
	public List<Dictionary> getDict() {
		String sql = "select group_no as \"groupNo\",group_key as \"groupKey\",group_name as \"groupName\" from Dictionary";
		SQLQuery query=this.dao.createSqlQuery(sql.toString());
	    // 将结果转成Bean
		query.setResultTransformer(Transformers.aliasToBean(Dictionary.class));
        // 设置转化的结果
        query.addScalar("groupNo", Hibernate.STRING);
        query.addScalar("groupKey", Hibernate.STRING);
        query.addScalar("groupName", Hibernate.STRING);
        List<Dictionary> dict = query.list();
		return dict;
	}

	/**
	 * 部门表
	 * @orderBy 
	 * @return
	 * @author wuwei
	 * @Date 2016年10月24日下午5:35:20
	 */
	@SuppressWarnings("unchecked")
	@Override
	//获取部门对象
	public List<Department> getDep() {
		String sql = "select department_guid as \"departmentGuid\",parent_guid as \"parentGuid\",department_fullname as \"departmentName\"from Department dep";
		SQLQuery query=this.dao.createSqlQuery(sql.toString());
	    // 将结果转成Bean
		query.setResultTransformer(Transformers.aliasToBean(Department.class));
        // 设置转化的结果
        query.addScalar("departmentGuid", Hibernate.STRING);
        query.addScalar("parentGuid", Hibernate.STRING);
        query.addScalar("departmentName", Hibernate.STRING);
        List<Department> dict = query.list();
		return dict;
	}	
}
