package com.strongit.iss.action.supermap;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.CommonUtils;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.Struts2Utils;
import com.strongit.iss.entity.Label;
import com.strongit.iss.entity.SuperMapGovMentBean;
import com.strongit.iss.entity.SuperMapGovMentBeanListBean;
import com.strongit.iss.entity.TCityCustomCoordinates;
import com.strongit.iss.service.ILabelService;
import com.strongit.iss.service.ISuperMapService;
import com.strongit.iss.service.impl.ReportCacheServiceImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


/**
 * <pre>
 *     
 *     空间政府精细化管理功能区相关方法
 * @author zhoupeng
 * @E-mai：zhoupeng@strongit.com.cn
 *  @Date 2016年12月10日下午3:36:18
 *  @see com.strongit.iss.service.IBaseService
 * </pre>
 */
public class SuperMapAction extends BaseActionSupport<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ReportCacheServiceImpl reportCacheService;
	
	@Autowired
	private ISuperMapService superMapService;
	
	@Autowired
	private ILabelService labelService;
	
	String realPath = this.getRequest().getRequestURL().toString();
	String projectName = this.getRequest().getContextPath();
	int endLength = realPath.indexOf(projectName) + projectName.length();
	String tmp = realPath.substring(0,endLength).toString();
	//动态拼接url		
	private String reportRequestUrl = tmp.replace("http://", "");
	
    //页面过滤条件
	private Map<String,String> params=new HashMap<String,String>();
	private String searchSql="";
	private String orderbySql = "";
	
	
	
	/**
	 *  <pre>
	 *   获取空间政府精细化管理中各地区数据
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @return
	 *     --  name==null或者name== ""  返回各个地区结果数据
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年12月12日上午10:11:00
	 * </pre>
	 */
	public String getQueryByGeometry(){
		Struts2Utils.renderJson(superMapService.getQueryByGeometry(params,searchSql,orderbySql));
		return null;
	}
	
	
	
	/**
	 *  <pre>
	 *   获取空间政府精细化管理获取报表数据
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @return
	 *     --  name==null或者name== ""  返回各个地区结果数据
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年10月22日下午2:47:44
	 * </pre>
	 */
	public String getSuperMapGovMentReportData(){
		Map<String,String> resultInfo = new HashMap<String, String>();
		List<Map<String, Object>> list = Lists.newArrayList();
		//空间政府精细化管理
		list = superMapService.getSuperMapGovReportByMap(params);
		
		List<SuperMapGovMentBean> depList = Lists.newArrayList();
		for(Map<String ,Object> map:list){
			//年度计划项目ID
			String yearPlanProjectId = String.valueOf(map.get("yearPlanProjectId"));
			//项目编码
			String proCodeCountry=String.valueOf(map.get("proCodeCountry"));
			//项目名称
			String proName=String.valueOf(map.get("proName"));
			//项目类型
			String proType=String.valueOf(map.get("proType"));
			//建设地点
			String buildPlace=String.valueOf(map.get("buildPlace"));
			//总投资（万元）
			String investmentTotal=String.valueOf(map.get("investmentTotal"));
			//委内行业
			String industry=String.valueOf(map.get("industry"));
			//开工时间
			String actualStartTime=String.valueOf(map.get("actualStartTime"));
			//竣工时间
			String actualEndTime=String.valueOf(map.get("actualEndTime"));
			
			depList.add(new SuperMapGovMentBean(yearPlanProjectId,proCodeCountry,proName,proType,buildPlace,investmentTotal,industry,actualStartTime,actualEndTime));
		}    		
		XStream xstream=new XStream(new DomDriver());
		xstream.processAnnotations(SuperMapGovMentBeanListBean.class);
		SuperMapGovMentBeanListBean bean=new SuperMapGovMentBeanListBean();
		bean.setResult(depList);
		//转成xml格式
		String convertXml = xstream.toXML(bean);
		String headAndBody = CommonUtils.XMLHEARDERS + convertXml;
		//存入缓存中
		String uuid1 = reportCacheService.putReport(headAndBody);
		resultInfo.put("uuid1",uuid1);
		resultInfo.put("reportRequestUrl", reportRequestUrl);
		Struts2Utils.renderText(resultInfo);
		return null;
	}
	
	
	
	/**
	 *  <pre>
	 *    保存自定义区域的结果
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @return
	 *     --  name==null或者name== ""  返回保存事件是否成功
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年10月22日下午2:47:44
	 * </pre>
	 */
    public String saveCoordinates() throws Exception{
    	//获取当前用户信息
    	Map<String,Object> employeeInfo = (Map<String, Object>) this.getSession().getAttribute(Constant.SYS_USER_INFO);
    			
    	String employeeGuid = employeeInfo.get("employee_guid").toString();
    	
        boolean b = superMapService.saveCusCoordinates(params,employeeGuid);
    	
        
        //以下为自定义区域在项目档案中的项目分类
        Label label= new Label();
        //自定义区域名称
    	if(StringUtils.isNotBlank(params.get("coorNames"))){
    		label.setName(params.get("coorNames").toString());
    	}
		label.setParentId("-1");
		label.setType("1");
		label.setModuleCode(Constant.FIVE_PLAN);
    	//转换项目id格式
		String ids="";
		
		String[] projectGuids = ids.split(",");
		List<String> projects = Arrays.asList(projectGuids);
		String  userId = employeeGuid;
        //this.labelService.addRelateUserLabel(userId, label, projects,true);
        if (b) {
      	  this.renderText("ok");
        } else {
      	  this.renderText("no");
        }
  	  return null;
    }
    
    
    
	/**
	 *  <pre>
	 *   保存自定义区域的结果
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @return
	 *     --  name==null或者name== ""  返回保存事件是否成功
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年10月22日下午2:47:44
	 * </pre>
	 */
    public String save() {
		//获取页面上传递过来的参数
		String entity = Struts2Utils.getRequest().getParameter("entity");
		//定义标记自定义区域对象
		TCityCustomCoordinates markInfo = null;
		//若json字符串不为空
		if (StringUtils.isNotBlank(entity)) {
			try {
				//解码，格式为UTF-8
				entity = URLDecoder.decode(entity, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//将json字符串转换为java实体对象
			markInfo = (TCityCustomCoordinates) JSONObject.toBean(
					JSONObject.fromObject(entity), TCityCustomCoordinates.class);
		}
		//若对象不为空
		if (null != markInfo) {
				//保存字典
				superMapService.saveEntity(markInfo);
		}
		return renderText("true");
	}
    
    
	/**
	 *  <pre>
	 *   删除自定义区域的值
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @return
	 *     --  name==null或者name== ""  返回删除事件是否成功
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年10月22日下午2:47:44
	 * </pre>
	 */
    public String deleteCusCoordinates() throws Exception{
    	
        boolean b = superMapService.deleteCusCoordinates(params);
        if (b) {
      	  this.renderText("ok");
        } else {
      	  this.renderText("no");
        }
  	  return null;
    }
    
	
	/**
	 *  <pre>
	 *   根据当前用户获取自定义区域的结果集数据
	 *   @see com.strongit.iss.entity.TCityCustomCoordinates
	 * @return
	 *     --  name==null或者name== ""  返回自定义区域数据
	 * @author zhoupeng
	 * @E-mai：zhoupeng@strongit.com.cn
	 * @Date 2016年10月22日下午2:47:44
	 * </pre>
	 */
    public String searchCoordinates() {
    	Map<String,String> filters = null;
    	String employeeGuid = "";
    	List<Map<String, Object>> list = superMapService.searchCoordinates(filters,employeeGuid);
    	Struts2Utils.renderJson(list);
    	return null;
    }
    
    
    

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}	
}
