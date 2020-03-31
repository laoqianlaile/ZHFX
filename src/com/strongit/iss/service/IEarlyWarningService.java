package com.strongit.iss.service;

import com.strongit.iss.entity.EarlyWarning;
import com.strongit.iss.orm.hibernate.Page;
/**
 * 
 * @author tanghw
 *
 */
public interface IEarlyWarningService extends IBaseService{
	/**
	 * 获取预警信息list
	 * @orderBy 
	 * @param page
	 * @param warningName 指标名称
	 * @return
	 * @author tanghw
	 * @Date 2016年12月12日下午3:38:27
	 */
	public Page<EarlyWarning> getEarlyWarningList(Page<EarlyWarning> page, String warningName);
	/**
	 * 保存预警信息
	 * @orderBy 
	 * @param configure
	 * @author tanghw
	 * @Date 2016年12月12日下午4:02:07
	 */
	public void saveEntity(EarlyWarning earlyWarning);
	/**
	 * 根据主键id获取预警信息
	 * @orderBy 
	 * @param id
	 * @return
	 * @author tanghw
	 * @Date 2016年12月12日下午4:12:09
	 */
	public EarlyWarning getById(String id);
	/**
	 * 修改预警信息
	 * @orderBy 
	 * @param earlyWarning
	 * @author tanghw
	 * @Date 2016年12月12日下午4:02:37
	 */
	public void updateEntity(EarlyWarning earlyWarning);

	/**
	 * 删除预警信息
	 * @orderBy 
	 * @param earlyWarning
	 * @author tanghw
	 * @Date 2016年12月12日下午4:02:34
	 */
	public void deleteEntity(EarlyWarning earlyWarning);
}
