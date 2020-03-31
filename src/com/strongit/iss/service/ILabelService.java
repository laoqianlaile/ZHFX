package com.strongit.iss.service;

import java.util.List;

import com.strongit.iss.entity.Label;
import com.strongit.iss.exception.BusinessServiceException;

public interface ILabelService extends IBaseService {
	/**
	 * 根据用户id获取标签数据
	 * @orderBy 
	 * @param userId
	 * @param moduleCode 模块
	 * @return 
	 * @throws BusinessServiceException
	 * @author tanghw
	 * @Date 2016年12月17日下午4:01:19
	 */
	public List<Label> getCustomLabelsData(String userId,String moduleCode) throws BusinessServiceException;
	/**
	 * 根据项目ID及当前用户，获取项目对应的可用标签信息
	 * @param projectId 项目id
	 * @param userId 用户id
	 * @return
	 * @throws Exception
	 */
	public List<Label> getProLabelSByContition(String projectId,String userId,String moduleCode)throws Exception;
	
	/**
	 * 自定义标签，根据条件保存用户新增的标签信息、保存标签关联用户信息、保存项目关联标签信息
	 * @param userId 用户id
	 * @param label 标签表信息
	 * @param projectIds 项目id计划
	 * @param isAdd 标签新增标示 true：新增 false：使用旧标签
	 * @return
	 * @throws Exception
	 */	
	public void addRelateUserLabel(String userId,Label label,List<String> projectIds,Boolean isAdd)throws Exception;
	
	/**
	 * 根据条件更新项目标签信息
	 * @param userId 用户id
	 * @param projectId 项目id
	 * @param labelId 标签id集合
	 * @throws Exception
	 */
	public void updateProLabelSByContition(String userId,String projectId,List<String> labelIds)throws Exception;
	/**
	 * 根据标签Id，修改标签名称
	 * @param labelId
	 * @param labelName
	 * @return
	 * @throws Exception
	 */
	public void updateLabelInfoById(String labelId,String labelName)throws Exception;
	/**
	 * 根据标签Id，删除标签并将标签绑定的项目标签表及标签角色用户表对应记录删除。
	 * @param labelId 标签id
	 * @return
	 * @throws Exception
	 */
	public void deleteLabelById(String labelId)throws Exception;
}
