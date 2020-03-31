package com.strongit.iss.action.dictionary;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.Datagrid;
import com.strongit.iss.common.IssJsonConfig;
import com.strongit.iss.common.RecursiveData;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.common.TreeGrid;
import com.strongit.iss.common.TreeNode;
import com.strongit.iss.entity.Dict;
import com.strongit.iss.entity.DictItems;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.service.IDictionaryService;

import net.sf.json.JSONArray;
/**
 * 
 * @author tanghw
 *
 */
@SuppressWarnings({ "rawtypes" })
public class DictionaryAction extends BaseActionSupport{
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	
	private Integer operateResult = Constant.RESULT_FAILURE;
	private String remark = null;
	//瀛楀吀缁刧roupno
	private String groupNo;
	//瀛楀吀椤圭埗id
	private String itemPid;
	//瀛楀吀椤筰d
	private String itemId;
	//瀛楀吀椤筰d
	private String itemGuid;
	//瀛楁缁勫璞�
	private Dict dictionary;
	//鏁版嵁瀛楀吀椤�
	private DictItems dictionaryItems;
	private Map<String, String> conditions;
	//page瀵硅薄
	private Page<DictItems> dictItemsPage = new Page<DictItems>(20, true);
	//绗竴椤�
	protected int page = 1;
	//椤靛閲�
	protected int rows = 20;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 鑾峰彇瀛楀吀缁勬暟鎹�
	 * @throws Exception
	 */
	public void getDictGroupData() throws Exception {
		List<Dict> list = new ArrayList<Dict>();
		operateResult = Constant.RESULT_FAILURE;
		try{
			list = dictionaryService.getDictionaryTree();
			List<TreeGrid> treeVoList = new ArrayList<TreeGrid>();
			for(Dict d : list){
				TreeGrid vo = new TreeGrid();
				vo.setId(d.getGroupNo().toString());
				vo.setText(d.getGroupName());
				vo.setOperations(d.getEnabled().toString());
				treeVoList.add(vo);
			}
			JSONArray data = JSONArray.fromObject(treeVoList, new IssJsonConfig("dict"));
			this.renderText(data.toString());
			operateResult = Constant.RESULT_SUCCESS;
		} catch(BusinessServiceException e) {
			remark = e.getMessage();
		} catch(Exception e) {
			remark = e.getMessage();
			logger.error(e.getMessage(), e);
		} 
	}
	/**
	 * 鏉′欢鏌ヨ鏁版嵁瀛楀吀page
	 * @orderBy 
	 * @author tanghw
	 * @Date 2016骞�2鏈�4鏃ヤ笅鍗�:14:09
	 */
	public void getDictionaryData(){
		if(groupNo == null || "".equals(groupNo)){
			return;
		}
		bulidPage();
		long start =System.currentTimeMillis();
		dictItemsPage = dictionaryService.getItemListByGroupNo(dictItemsPage, groupNo);
		long end =System.currentTimeMillis();
		logger.debug("execute dictionaryService.getItemListByGroupNo method cost time : "
				+ (end - start) + " mills.");
		List<DictItems> list = dictItemsPage.getResult();
		//formatList(list);
		// 灏佽鍒癲atagrid涓easyui鎺т欢娓叉煋
		Datagrid<DictItems> dg = new Datagrid<DictItems>(dictItemsPage.getTotalCount(), list);
		// 杩斿洖鍓嶇JSON
		Struts2Utils.renderJson(dg);
	}
	/**
	 * 鍒濆鍖朠age
	 * @orderBy 
	 * @author tanghw
	 * @Date 2016骞�2鏈�4鏃ヤ笅鍗�:24:38
	 */
	private void bulidPage() {
		dictItemsPage.setPageNo(page);
		dictItemsPage.setPageSize(rows);
	}
	/**
	 * 鎵撳紑瀛楀吀缁勭紪杈戦〉闈�
	 * @orderBy 
	 * @return
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016骞�2鏈�4鏃ヤ笅鍗�:14:00
	 */
	public String dictGropInput() throws Exception {
		if(groupNo != null && !"".equals(groupNo)) {
			dictionary = dictionaryService.getDictByGroupNo(groupNo);
			String key = dictionary.getGroupKey();
			StringBuffer sb1 = new StringBuffer();
			for(int i=0; i<key.length(); i++){
				String a = key.substring(i,i+1);
				if("\\".equals(a)){
					sb1.append("\\\\");
				}else if("\'".equals(a)){
					sb1.append("\\\'");
				}else{
					sb1.append(a);
				}
			}
			dictionary.setGroupKey(sb1.toString());
			String name = dictionary.getGroupName();
			StringBuffer sb2 = new StringBuffer();
			for(int i=0; i<name.length(); i++){
				String a = name.substring(i,i+1);
				if("\\".equals(a)){
					sb2.append("\\\\");
				}else if("\'".equals(a)){
					sb2.append("\\\'");
				}else{
					sb2.append(a);
				}
			}
			dictionary.setGroupName(sb2.toString());
		} else {
			dictionary = new Dict();
		}
		return "dictGropInput";
	}
	/**
	 *  鎵撳紑瀛楀吀椤规柊澧為〉闈�
	 * @orderBy 
	 * @return
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016骞�2鏈�4鏃ヤ笅鍗�:16:03
	 */
	public String dictItemInput() throws Exception {
		return "dictItemInput";
	}
	/**
	 * 鎵撳紑瀛楀吀椤圭紪杈戦〉闈�
	 * @orderBy 
	 * @return
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016骞�2鏈�4鏃ヤ笅鍗�:09:19
	 */
	public String dictItemEdit() throws Exception {
		return "dictItemEdit";
	}
	
	/**
	 * 淇濆瓨瀛楀吀缁勬暟鎹�
	 * @param isAdd, dictionary
	 * @return true, false
	 */
	public void saveDictGroup() throws Exception {
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		String userName = null;
		if(user!=null){
			userId = user.get("employee_guid").toString();
			userName = user.get("employee_fullname").toString();
		}
		if(dictionary.getGroupNo() != null){
			this.groupNo = this.dictionary.getGroupNo().toString();
		}else{
			groupNo = null;
		}
		String groupKey = dictionary.getGroupKey();
		String enabled = dictionary.getEnabled().toString();
		String groupName = dictionary.getGroupName();
		//浠ｈ〃缂栬緫
		if(groupNo!=null && !"".equals(groupNo)){
			dictionary = dictionaryService.getDictByGroupNo(groupNo);
		}else{
			dictionary = new Dict();
			dictionary.setCreateTime(new Date());
			dictionary.setCreateUserId(userId);
			dictionary.setCreateUserName(userName);
			dictionary.setDataType(Constant.DATATYPE_INSIDE);
		}
		org.json.JSONObject rs = new org.json.JSONObject();
		try{
			//浠ｈ〃鏂板椤甸潰
			if(groupNo!=null){
				remark = "瀛楀吀缁勭紪杈戞垚鍔燂紒";
			}else{
				dictionary.setGroupNo(null);
				remark = "瀛楀吀缁勬坊鍔犳垚鍔燂紒";
			}
			dictionary.setGroupKey(groupKey);
			dictionary.setEnabled(Integer.parseInt(enabled));
			dictionary.setGroupName(groupName);
			dictionary.setUpdateTime(new Date());
			dictionary.setUpdateUserId(userId);
			dictionary.setUpdateUserName(userName);
			dictionaryService.addDict(dictionary);
			rs.put("content", remark);
			rs.put("flag", "true");
			rs.put("messageType", "0");
			Struts2Utils.renderText(rs.toString());
//			recordOperateLog(LogResult.SUCCESS, "淇濆瓨瀛楀吀缁�, "澶囨敞锛氫繚瀛樻垚鍔�);
		}catch (Exception e){
			if(Constant.ZDZBH_IS_EXISTED.equals(e.getMessage())){
				remark = Constant.ZDZBH_SHOW;
			}else if(Constant.ZDZMC_IS_EXISTED.equals(e.getMessage())){
				remark = Constant.ZDZMC_SHOW;
			}
//			recordOperateLog(LogResult.FAIL, "淇濆瓨瀛楀吀缁�, "澶囨敞锛氫繚瀛樺紓甯革紝寮傚父娑堟伅鏄細" + e.getMessage());
			rs.put("content", remark);
			rs.put("flag", "false");
			rs.put("messageType", "1");
			Struts2Utils.renderText(rs.toString());
		}
	}
	
	/**
	 * 淇濆瓨瀛楀吀椤规暟鎹�
	 * @orderBy 
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016骞�2鏈�5鏃ヤ笂鍗�1:25:02
	 */
	public void saveDictItems() throws Exception {
		org.json.JSONObject rs = new org.json.JSONObject();
		@SuppressWarnings("unchecked")
		Map<String,Object> user = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
		String userId = null;
		String userName = null;
		if(user!=null){
			userId = user.get("employee_guid").toString();
			userName = user.get("employee_fullname").toString();
		}
		String parentItemKey = dictionaryItems.getParentItemId();
		String[] attachIds = null;
		attachIds = parentItemKey.split(",");
		parentItemKey = attachIds[0].trim().toString();
		Dict dict = new Dict();
		if(dictionary.getGroupNo()!=null && !"".equals(dictionary.getGroupNo())){
			dict = dictionaryService.getDictByGroupNo(dictionary.getGroupNo().toString());
		}
		try{
			//浠ｈ〃缂栬緫
			if(StringUtils.isNotBlank(dictionaryItems.getItemGuid())){
				DictItems dictItem = dictionaryService.getDictItemsById(dictionaryItems.getItemGuid().toString());
				String oldKey = dictItem.getItemKey();
				String oldValue = dictItem.getItemValue();
				dictItem.setItemKey(dictionaryItems.getItemKey());
				dictItem.setItemValue(dictionaryItems.getItemValue());
				dictItem.setEnabled(dictionaryItems.getEnabled());
				dictItem.setPxh(dictionaryItems.getPxh());
				dictItem.setFlag(dictionaryItems.getFlag());
				remark = "瀛楀吀椤圭紪杈戞垚鍔�";
				dictItem.setDictionary(dict);
				dictItem.setParentItemId(parentItemKey);
				dictItem.setUpdateTime(new Date());
				dictItem.setUpdateUserId(userId);
				dictItem.setUpdateUserName(userName);
				dictionaryService.addDictItem(dictItem,oldKey,oldValue);				
				
			}else{
				String id=UUID.randomUUID().toString();
				dictionaryItems.setItemGuid(id);
				dictionaryItems.setCreateTime(new Date());
				dictionaryItems.setCreateUserId(userId);
				dictionaryItems.setCreateUserName(userName);
				dictionaryItems.setDataType(Constant.DATATYPE_INSIDE);
				remark = "瀛楀吀椤规坊鍔犳垚鍔�";
				
				dictionaryItems.setDictionary(dict);
				dictionaryItems.setParentItemId(parentItemKey);
				dictionaryItems.setUpdateTime(new Date());
				dictionaryItems.setUpdateUserId(userId);
				dictionaryItems.setUpdateUserName(userName);
				dictionaryService.addDictItem(dictionaryItems,null,null);				
			}
			rs.put("content", remark);
			rs.put("flag", "true");
			Struts2Utils.renderText(rs.toString());
		}catch (Exception e){
			if(Constant.ZDXBH_IS_EXISTED.equals(e.getMessage())) {
				remark = Constant.ZDXBH_SHOW;
			} else if(Constant.ZDXMC_IS_EXISTED.equals(e.getMessage())) {
				remark = Constant.ZDXMC_SHOW;
			} else if(Constant.CHILDREN_IS_EXISTED.equals(e.getMessage())) {
				remark = Constant.CHILDREN_SHOW;
			} else {
				remark = e.getMessage();
			}
            rs.put("content", remark);
			rs.put("flag", "false");
			Struts2Utils.renderText(rs.toString());
		}
	}
	
	/**
	 * 鍔犺浇鏍戞暟鎹�
	 * @orderBy 
	 * @throws NumberFormatException
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016骞�2鏈�5鏃ヤ笅鍗�:41:58
	 */
	public void getParentDictItems() throws NumberFormatException, Exception{
		List<DictItems> list = dictionaryService.getItemListByGroupNo(groupNo);
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		if (null != list && list.size() > 0) {
			//閬嶅巻闆嗗悎
			for (DictItems item : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("NAME", item.getItemValue());
				map.put("ID", item.getItemKey());
				map.put("PID", item.getParentItemId());
				resultList.add(map);					
			}
		}
		List<TreeNode>	nodes=RecursiveData.RecursiveDept(resultList);
		Struts2Utils.renderJson(nodes);
	}
	/**
	 * 鍔犺浇鏍戞暟鎹牴鎹」鐩粍groupNo
	 * @orderBy 
	 * @throws NumberFormatException
	 * @throws Exception
	 * @author tanghw
	 * @Date 2016骞�2鏈�5鏃ヤ笅鍗�:41:58
	 */
	public void getParentDictItemsBygroupNo() throws NumberFormatException, Exception{
		List<DictItems> list = dictionaryService.getItemListByGroupNo(groupNo);
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		if (null != list && list.size() > 0) {
			//閬嶅巻闆嗗悎
			for (DictItems item : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("NAME", item.getItemValue());
				map.put("ID", item.getItemKey());
				map.put("PID", item.getParentItemId());
				resultList.add(map);					
			}
		}
		List<TreeNode>	nodes=RecursiveData.RecursiveDept(resultList);
		Struts2Utils.renderJson(nodes);
	}
	/**
	 * 鑾峰彇鍗曚釜瀛楀吀椤规暟鎹�
	 * @orderBy 
	 * @author tanghw
	 * @Date 2016骞�2鏈�5鏃ヤ笅鍗�:15:38
	 */
	public void getDictItemsByGroupGuid() {
		try {
			DictItems dict = dictionaryService.getDictItemsById(conditions.get("dictGuid"));
			JSONArray data = JSONArray.fromObject(dict, new IssJsonConfig("dictionary"));
			this.renderText(data.toString());
			operateResult = Constant.RESULT_SUCCESS;
		} catch(BusinessServiceException e) {
			remark = e.getMessage();
		} catch(Exception e) {
			remark = e.getMessage();
			logger.error(e.getMessage(), e);
		}
	}
	
	public Integer getOperateResult() {
		return operateResult;
	}
	public void setOperateResult(Integer operateResult) {
		this.operateResult = operateResult;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public Dict getDictionary() {
		return dictionary;
	}
	public void setDictionary(Dict dictionary) {
		this.dictionary = dictionary;
	}
	public DictItems getDictionaryItems() {
		return dictionaryItems;
	}
	public void setDictionaryItems(DictItems dictionaryItems) {
		this.dictionaryItems = dictionaryItems;
	}
	public String getItemPid() {
		return itemPid;
	}
	public void setItemPid(String itemPid) {
		this.itemPid = itemPid;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemGuid() {
		return itemGuid;
	}
	public void setItemGuid(String itemGuid) {
		this.itemGuid = itemGuid;
	}
	public Map<String, String> getConditions() {
		return conditions;
	}
	public void setConditions(Map<String, String> conditions) {
		this.conditions = conditions;
	}	
	
}
