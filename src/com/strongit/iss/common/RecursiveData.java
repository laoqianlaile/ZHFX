package com.strongit.iss.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;




/**
 * 
 * @author Tanghw
 *
 */
public class RecursiveData {
	
	/**
	 * 得到下拉框树结构
	 * @param dpts
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<TreeNode> RecursiveDept(List<Map<String,Object>> dpts){
		List<TreeNode> nodes=new ArrayList<TreeNode>();
		if(dpts==null||dpts.size()<1)
			return new ArrayList<TreeNode>();
		// 分组
		else{			
		Map<String,List<Map<String,Object>>> maps=new LinkedMap(20);
	   // 变量部门
		for(Map<String,Object> dpt:dpts){
			String pid="-1";
			if(null!=dpt.get("PID") && !"".equals(dpt.get("PID"))){
				pid=dpt.get("PID").toString();
			}
			List<Map<String,Object>> deps=maps.get(pid);
			
			if(deps==null){
		           deps=new ArrayList<Map<String,Object>>();
			}	
			 deps.add(dpt);	
			 maps.put(pid, deps);
		}
		String topPId="-1";
	// 顶级部门 父级ID=-1
		List<Map<String,Object>> topDeps=maps.get(topPId);
		for(Map<String,Object> tp:topDeps){
			TreeNode  node=new TreeNode();
			node.setId(tp.get("ID")+"");
			node.setText(tp.get("NAME")+"");
			// 递归构造树
			RecursiveTree(node,tp.get("ID")+"",maps);
			nodes.add(node);
		}
		}
		return nodes;
	}

	/**
	 * 
	 * @param node
	 * 				添加的树节点
	 * @param Map<String,String>Guid
	 * 				部门的主键
	 * @param maps
	 * 	           数据集
	 */
	private static void RecursiveTree(TreeNode node, String guid,
			Map<String, List<Map<String,Object>>> maps) {
		List<Map<String,Object>>trees= maps.get(guid);
		// 已经是叶子节点
		if(null==trees||trees.size()==0)
			return ;
		else{
			node.setState(TreeNode.STATE_CLOASED);
			for(Map<String,Object> tp:trees){
				TreeNode  subnode=new TreeNode();
				subnode.setId(tp.get("ID")+"");
				subnode.setText(tp.get("NAME")+"");				
				// 递归构造树s
				RecursiveTree(subnode,tp.get("ID")+"",maps);
				// 添加节点
				node.addChild(subnode);
			}
		}
		
	}
	/**
	 * 得到下拉框树结构
	 * @param dpts
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<TreeNode> RecursiveShortValue(List<Map<String,Object>> dpts){
		List<TreeNode> nodes=new ArrayList<TreeNode>();
		if(dpts==null||dpts.size()<1)
			return new ArrayList<TreeNode>();

			// 分组
		else{
			Map<String,List<Map<String,Object>>> maps=new LinkedMap(20);
			// 变量部门
			for(Map<String,Object> dpt:dpts){
				String pid="-1";
				if(null!=dpt.get("PID") && !"".equals(dpt.get("PID"))){
					pid=dpt.get("PID").toString();
				}
				List<Map<String,Object>> deps=maps.get(pid);

				if(deps==null){
					deps=new ArrayList<Map<String,Object>>();
				}
				deps.add(dpt);
				maps.put(pid, deps);
			}
			String topPId="-1";
			// 顶级部门 父级ID=-1
			List<Map<String,Object>> topDeps=maps.get(topPId);
			for(Map<String,Object> tp:topDeps){
				TreeNode  node=new TreeNode();
				node.setId(tp.get("SHORTVALUE")+"");
				node.setText(tp.get("NAME")+"");
				// 递归构造树
				RecursiveShortTree(node,tp.get("ID")+"",maps);
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 *
	 * @param node
	 * 				添加的树节点
	 * @param Map<String,String>Guid
	 * 				部门的主键
	 * @param maps
	 * 	           数据集
	 */
	private static void RecursiveShortTree(TreeNode node, String guid, Map<String, List<Map<String,Object>>> maps) {
		List<Map<String,Object>>trees= maps.get(guid);
		// 已经是叶子节点
		if(null==trees||trees.size()==0)
			return ;
		else{
			node.setState(TreeNode.STATE_CLOASED);
			for(Map<String,Object> tp:trees){
				TreeNode  subnode=new TreeNode();
				subnode.setId(tp.get("SHORTVALUE")+"");
				subnode.setText(tp.get("NAME")+"");
				// 递归构造树s
				RecursiveTree(subnode,tp.get("ID")+"",maps);
				// 添加节点
				node.addChild(subnode);
			}
		}

	}

}
