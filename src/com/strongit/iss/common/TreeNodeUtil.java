package com.strongit.iss.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
* @ClassName: TreeNodeUtil
* @Description 描述: 获取树节点集合(这里用一句话描述这个类的作用)
*
*/
public class TreeNodeUtil {

	/**
	* @Title: getFatherNode
	* @Description 方法描述: 父节点
	* @param 设定文件： @param treeDataList
	* @param 设定文件： @return    
	* @return 返回类型：List<JsonTreeData>    
	* @throws
	*/
	public final static List<JsonTreeData> getFatherNode(List<JsonTreeData> treeDataList) {
		List<JsonTreeData> newTreeDataList = new ArrayList<JsonTreeData>();
		for (JsonTreeData jsonTreeData : treeDataList) {
			if(jsonTreeData.getPid() == null || StringUtils.isEmpty(jsonTreeData.getPid())) {
				//获取父节点下的子节点
				List<JsonTreeData> childList = getChildrenNode(jsonTreeData.getId(),treeDataList);
				if(childList != null && childList.size() > 0){
					jsonTreeData.setChildren(childList);
					jsonTreeData.setState("closed");
				}else{
					jsonTreeData.setChildren(null);
					jsonTreeData.setState("open");
				}
				newTreeDataList.add(jsonTreeData);
			}
		}
		return newTreeDataList;
	}
	
	/**
	* @Title: getChildrenNode
	* @Description 方法描述: 子节点
	* @param 设定文件： @param pid
	* @param 设定文件： @param treeDataList
	* @param 设定文件： @return    
	* @return 返回类型：List<JsonTreeData>    
	* @throws
	*/
	private final static List<JsonTreeData> getChildrenNode(String pid , List<JsonTreeData> treeDataList) {
		List<JsonTreeData> newTreeDataList = new ArrayList<JsonTreeData>();
		for (JsonTreeData jsonTreeData : treeDataList) {
			if(jsonTreeData.getPid() == null || StringUtils.isEmpty(jsonTreeData.getPid()))  continue;
			//这是一个子节点
			if(jsonTreeData.getPid().equals(pid)){
				//递归获取子节点下的子节点
				List<JsonTreeData> childList = getChildrenNode(jsonTreeData.getId() , treeDataList);
				if(childList != null && childList.size() > 0){
					jsonTreeData.setChildren(childList);
					jsonTreeData.setState("closed");
				}else{
					jsonTreeData.setChildren(null);
					jsonTreeData.setState("open");
				}
				
				newTreeDataList.add(jsonTreeData);
			}
		}
		return newTreeDataList;
	}
}
