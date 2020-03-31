package com.strongit.iss.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.entity.EarlyWarning;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.IEarlyWarningService;
@Service
@Transactional
public class EarlyWarningServiceImpl extends BaseService implements IEarlyWarningService{

	/**
	 * 获取预警信息list
	 * @orderBy 
	 * @param page
	 * @param warningName 指标名称
	 * @return
	 * @author tanghw
	 * @Date 2016年12月12日下午3:38:27
	 */
	@Override
	public Page<EarlyWarning> getEarlyWarningList(Page<EarlyWarning> page, String warningName) {
		String sql = " from EarlyWarning t where 1=1 ";
		if (StringUtils.isNotBlank(warningName)) {
			sql += " and t.warningName like '%" + warningName + "%' ";
		}
		page = this.dao.find(page, sql+" order by t.sort asc ", new Object[]{});
		return page;
	}
	/**
	 * 保存预警信息
	 * @orderBy 
	 * @param configure
	 * @author tanghw
	 * @Date 2016年12月12日下午4:02:07
	 */
	@Override
	public void saveEntity(EarlyWarning earlyWarning) {
		this.save(earlyWarning);
		this.dao.flush();
	}
	/**
	 * 修改预警信息
	 * @orderBy 
	 * @param earlyWarning
	 * @author tanghw
	 * @Date 2016年12月12日下午4:02:37
	 */
	@Override
	public void updateEntity(EarlyWarning earlyWarning) {
		this.update(earlyWarning);
		this.dao.flush();
	}
	/**
	 * 删除预警信息
	 * @orderBy 
	 * @param earlyWarning
	 * @author tanghw
	 * @Date 2016年12月12日下午4:02:34
	 */
	@Override
	public void deleteEntity(EarlyWarning earlyWarning) {
		try {
			this.dao.delete(earlyWarning);
			this.dao.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public EarlyWarning getById(String id) {
		String sql = " from EarlyWarning t where 1=1  "
				+ " and t.id = '" + id + "' ";
		List<EarlyWarning> list = this.dao.find(sql, new Object[]{});
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
