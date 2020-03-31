package com.strongit.iss.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.common.Constant;
import com.strongit.iss.entity.Label;
import com.strongit.iss.entity.ProjectLabel;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.ILabelService;
@Service
@Transactional
public class LabelServiceImpl extends BaseService implements ILabelService {
	/**
	 * 根据用户id获取标签数据
	 * @orderBy 
	 * @param userId
	 * @return
	 * @throws BusinessServiceException
	 * @author tanghw
	 * @Date 2016年12月17日下午4:01:19
	 */
	@Override
	public List<Label> getCustomLabelsData(String userId,String moduleCode) throws BusinessServiceException {
		//判断模块是否为null
		if(StringUtils.isBlank(moduleCode)){
			moduleCode =Constant.FIVE_PLAN;
		}
		StringBuffer hql = new StringBuffer(" select * from tz_label  where enabled='0' and type='1' and create_user_id=? and MODULE_CODE=? "
				+ "union all select * from tz_label  where type='2' and enabled='0' ");
		return dao.findBySql(Label.class, hql.toString(),userId,moduleCode);
	}
	/**
	 * 根据项目ID及当前用户，获取项目对应的可用标签信息
	 * @param projectId 项目id
	 * @param userId 用户id
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Label> getProLabelSByContition(String projectId, String userId,String moduleCode) throws Exception {
		//判断模块是否为null
		if(StringUtils.isBlank(moduleCode)){
			moduleCode =Constant.FIVE_PLAN;
		}
		// 根据传入的用户Id,获取用户信息
		StringBuffer hql =new StringBuffer(" select ll.* from TZ_LABEL ll, TZ_PROJECT_LABEL pl where ll.id=pl.label_id and ll.type='1' and ll.enabled='0' "
				+ "and pl.project_id='"+projectId+"' and ll.create_user_id='"+userId+"' and ll.MODULE_CODE='"+moduleCode+"'");
		return dao.findBySql(Label.class,hql.toString());
	}
	/**
	 * 自定义标签，根据条件保存用户新增的标签信息、保存标签关联用户信息、保存项目关联标签信息
	 * @param userId 用户id
	 * @param label 标签表信息
	 * @param projectIds 项目id计划
	 * @param isAdd 标签新增标示 true：新增 false：使用旧标签
	 * @param operateType 操作类型(内部调用:INTERNAL;外部调用:EXTERNAL)
	 * @return
	 * @throws Exception
	 */	
	@Override
	public void addRelateUserLabel(String userId, Label label, List<String> projectIds, Boolean isAdd) throws Exception {
		//先判断参数是否为空
		if(userId==null||userId==""||null == label || null== projectIds || null== isAdd){
			throw new Exception("传入参数有误");
		}
		try {
			//创建时间
			Date currentTime=new Timestamp(new Date().getTime());
			if(isAdd){
				//新增标签 此时label的ParentId为页面选中的父级标签id
				String labelID = UUID.randomUUID().toString();
				label.setId(labelID);
				label.setEnabled(0);
				label.setCreateUserId(userId);
				label.setCreatTime(currentTime);
				label.setUpdateTime(currentTime);
				label.setUpdateUserId(userId);
				this.dao.save(label);
			}else{
				//选择已有标签
				//使用已有标签的时候,label的id为页面选中的标签id,label对象从数据库中查询出来
				label=this.dao.get(Label.class, label.getId());
			}
			//循环保存项目标签关联表
			for(int i=0;i<projectIds.size();i++){
				ProjectLabel projectLabel= new ProjectLabel();
				String projectLabelID=UUID.randomUUID().toString();
				projectLabel.setId(projectLabelID);
				projectLabel.setLabel(label);
				projectLabel.setProject(projectIds.get(i));
				projectLabel.setCreateUserId(userId);
				projectLabel.setCreateTime(currentTime);
				projectLabel.setUpdateTime(currentTime);
				projectLabel.setUpdateUserId(userId);
				this.dao.save(projectLabel);
			}
		}catch (Exception e) {
			throw new Exception("自定义标签失败");
		}
		
	}
	/**
	 * 根据条件更新项目标签信息
	 * @param userId 用户id
	 * @param projectId 项目id
	 * @param labelId 标签id集合
	 * @param operateType 操作类型(内部调用:INTERNAL;外部调用:EXTERNAL)
	 * @throws Exception
	 */
	@Override
	public void updateProLabelSByContition(String userId, String projectId, List<String> labelIds)
			throws Exception {
			//先判断参数是否为空
			if(userId==null||userId==""||"" == projectId || null== projectId){
				throw new Exception("传入参数有误");
			}
			try {
				//根据项目id获取项目的修改前的关联标签信息集合
				List<Label> oldLabels =this.getLabelsByProjectID(projectId);
				//循环项目原标签对象 匹配项目新标签对象 去掉新标签中重复的原标签 删除新标签中没有的原标签
				for(Label label:oldLabels){
					boolean delflag = true;
					for(int i=0;i<labelIds.size();i++){
						String labelId=labelIds.get(i);
						//判断原有标签是否存在新标签中
						if(labelId.equals(label.getId())){
							delflag = false;
							break;
						}
					}
					if(delflag){
						//删除新标签中没有的原标签
						this.deleteProLabelByContiton(label.getId(), projectId);
					}
				}
				//判断项目是否添加了新的已有标签 有的话保存添加的标签
				//labelIds可能为空存的是[]所以进行格式转换后哦判断
				String labelIdss = labelIds.toString();
				if(!labelIdss.equals("[]")){
					 for(String labelId : labelIds){
						 this.saveProjectLabelsByProjectId(projectId,labelId,userId);
					 }
				 }
			}catch (Exception e) {
				throw new Exception("更新项目标签信息失败");
			}
	
	}
	/**
	 * 根据项目id获取项目的关联标签信息集合
	 * @orderBy 
	 * @param projectId
	 * @return
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016年12月17日下午4:49:44
	 */
	private List<Label> getLabelsByProjectID(String projectId) throws Exception {
		StringBuffer hql =new StringBuffer(" select ll.* from TZ_LABEL ll, TZ_PROJECT_LABEL pl where ll.id=pl.label_id and ll.enabled='0' and pl.project_id=? ");
		return dao.findBySql(Label.class, hql.toString(),projectId);
	}
	/**
	 * //删除新标签中没有的原标签
	 * @orderBy 
	 * @param labelId
	 * @param projectId
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016年12月17日下午4:50:51
	 */
	private void deleteProLabelByContiton(String labelId, String projectId)
			throws Exception {
		String delSql="delete from TZ_PROJECT_LABEL pl where pl.label_id=? and pl.project_id=? ";
		this.dao.executeSql(delSql, labelId,projectId);
	}
	/**
	 * 根据项目id、标签id、用户信息保存项目标签关联表信息
	 * @orderBy 
	 * @param projectId
	 * @param labelId
	 * @param userId
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016年12月17日下午4:54:56
	 */
	private void saveProjectLabelsByProjectId(String projectId, String labelId,
				String userId) throws Exception {
			Date currentTime=new Timestamp(new Date().getTime());
			ProjectLabel projectLabel = new ProjectLabel();
			Label label=this.dao.get(Label.class,labelId);
			projectLabel.setId(UUID.randomUUID().toString());
			projectLabel.setLabel(label);
			projectLabel.setProject(projectId);
			projectLabel.setCreateUserId(userId);
			projectLabel.setCreateTime(currentTime);
			projectLabel.setUpdateTime(currentTime);
			projectLabel.setUpdateUserId(userId);
			this.dao.save(projectLabel);	
		}
	/**
	 * 根据标签Id，修改标签名称
	 * @param labelId
	 * @param labelName
	 * @return
	 * @throws Exception
	 */
	@Override
	public void updateLabelInfoById(String labelId, String labelName) throws Exception {
		//先判断参数是否为空
		if(labelId==null||labelId==""){
			throw new Exception("没有要修改的标签");
		}
		if(labelName==null||labelName==""){
			throw new Exception("标签名字不能为空");
		}
		try{
			String sql="UPDATE TZ_LABEL SET NAME=? WHERE ID=? ";
			this.dao.executeSql(sql,labelName,labelId);
		}catch (Exception e) {
			throw new BusinessServiceException(e.getMessage());
		}
	}
	/**
	 * 根据标签Id，删除标签并将标签绑定的项目标签表及标签角色用户表对应记录删除。
	 * @param labelId 标签id
	 * @return
	 * @throws Exception
	 */
	@Override
	public void deleteLabelById(String labelId) throws Exception {
		//先判断参数是否为空
		if(labelId==null||labelId==""){
			throw new Exception("没有要删除的标签");
		}
		try{
			String plsql="DELETE FROM  TZ_PROJECT_LABEL WHERE LABEL_ID=? ";
			String lsql ="DELETE FROM  TZ_LABEL WHERE ID=? ";
			this.dao.executeSql(plsql, labelId);
			this.dao.executeSql(lsql, labelId);
		}catch (Exception e) {
			throw new BusinessServiceException(e.getMessage());
		}
	}
}
