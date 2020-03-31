package com.strongit.iss.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.common.MonthReportSqlConstants;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.IMonthReportService;

/**
 * 
 * @author li
 *
 */
@Service
@Transactional
public class MonthReportServiceImpl extends BaseService implements IMonthReportService {

	private XWPFVertAlign align;


	/**
	 * 获取分行业情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:21:12
	 */
	@Override
	public List<Map<String, Object>> getIndustryInfoByMonth(String replaymonth1,String replaymonth2) {
		// TODO Auto-generated method stub
		//获取一级行业个数和总投资情况
		String tysql = String.format(MonthReportSqlConstants.INDUSTRYP_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		//String lysql = String.format(MonthReportSqlConstants.INDUSTRY_SQL, ("'" + replaymonth1 + "'"),("'" + replaymonth2 + "'"));
		//获取二级行业个数和总投资情况
		String ptsql = String.format(MonthReportSqlConstants.INDUSTRY_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		//获取项目总数和总投资情况
		String ttsql = String.format(MonthReportSqlConstants.TOTAL_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		
		List<Map<String, Object>> list1 = this.dao.findBySql(tysql, new Object[]{});
		List<Map<String, Object>> list2 = this.dao.findBySql(ttsql, new Object[]{});
		List<Map<String, Object>> list3 = this.dao.findBySql(ptsql, new Object[]{});
		
		//新建一个集合获取全部信息
		List <Map<String, Object>> industryList = new ArrayList<Map<String,Object>>();
		//把一级行业和二级行业按顺序排列
		for(Map<String, Object> map : list3){
			industryList.add(map);
			String flag = map.get("pcode").toString();
			for(int i=0;i<list1.size();i++){
				if(flag.equals(list1.get(i).get("pcode").toString())){
					industryList.add(list1.get(i));
				}
			}
		}
		//新建的集合获取所以信息
		if(null!=list2&list2.size()>0){
			// 把合计放在第一位
			industryList.add(0, list2.get(0));
		}
		return industryList;
	}

	/**
	 * 获取分地区情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:21:27
	 */
	@Override
	public List<Map<String, Object>> getAreaInfoByMonth(String replaymonth1,String replaymonth2) {
		// TODO Auto-generated method stub
		//获取省级地区项目个数和总投资
		String sql = String.format(MonthReportSqlConstants.AREA_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		//String lysql = String.format(MonthReportSqlConstants.AREA_SQL, "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
        //获取所有项目个数和总投资信息
		String ttsql = String.format(MonthReportSqlConstants.TOTAL_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		//获取中部、东部和西部地区的个数和总投资
        String aasql = String.format(MonthReportSqlConstants.ALLAREA_TOTAL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		
		List<Map<String, Object>> list1 = this.dao.findBySql(sql, new Object[]{});
		List<Map<String, Object>> list2 = this.dao.findBySql(ttsql, new Object[]{});
		List<Map<String, Object>> list3 = this.dao.findBySql(aasql, new Object[]{});

/*			for (Map<String, Object>dqlist:list1) {
				
				for(Map<String, Object>sslist:list3){
				if(sslist.get("EXTEND_FLAG").equals(dqlist.get("EXTEND_FLAG"))){
                      list1.add(0, list3.get(0));
				}
				}
			}*/
		
//		int j = 0;
		List <Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
		//各地区按地理位置排列
		for(Map<String, Object> map : list3){
			newList.add(map);
			String flag = map.get("EXTEND_FLAG").toString();
			for(int i=0;i<list1.size();i++){
				if(flag.equals(list1.get(i).get("EXTEND_FLAG").toString())){
//					j+=1;
//					list3.add(j, list1.get(i));
					newList.add(list1.get(i));
				}
			}
//			j+=1;
		}

		//所有的信息放到一起来排列
		if(null!=list2&list2.size()>0){
			// 把合计放在第一位
			newList.add(0, list2.get(0));
		}			

		return newList;
	}
	
	/**
	 * 获取各分地区信息
	 * @orderBy 
	 * @param replaymonth1
	 * @param replaymonth2
	 * @return
	 * @author li
	 * @Date 2016年10月29日下午3:46:54
	 */
	@Override
	public List<Map<String, Object>> getAllTotalInfoByMonth(String replaymonth1,String replaymonth2) {
		
		String sql = String.format(MonthReportSqlConstants.ALLAREA_TOTAL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		
		List<Map<String, Object>> list = this.dao.findBySql(sql, new Object[]{});
		
		
		return list;
		
	}
	
	/**
	 * 获取东三省情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:21:45
	 */
	@Override
	public List<Map<String, Object>> getEastAreaInfoByMonth(String replaymonth1,String replaymonth2) {
		// TODO Auto-generated method stub
		//获取东三省各个地区项目个数和总投资
		String sql = String.format(MonthReportSqlConstants.EASTAREA_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		//获取东北地区项目个数和总投资
		String ttsql = String.format(MonthReportSqlConstants.EASTTOAL_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		
		List<Map<String, Object>> list1 = this.dao.findBySql(sql, new Object[]{});
		List<Map<String, Object>> list2 = this.dao.findBySql(ttsql, new Object[]{});
		//所有信息组合排列到一起
		if(null!=list2&list2.size()>0){
			// 把合计放在第一位
			list1.add(0, list2.get(0));
		}
		return list1;
	}
	
	/**
	 * 获取东三省总计
	 * @orderBy 
	 * @param replaymonth1
	 * @param replaymonth2
	 * @return
	 * @author li
	 * @Date 2016年10月29日下午3:46:54
	 */
	@Override
	public List<Map<String, Object>> getEastTotalInfoByMonth(String replaymonth1,String replaymonth2) {
		
		String etsql = String.format(MonthReportSqlConstants.EASTTOAL_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		
		List<Map<String, Object>> list = this.dao.findBySql(etsql, new Object[]{});
		
		return list;
		
	}
	
	/**
	 * 获取分产业情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:21:45
	 */
	@Override
	public List<Map<String, Object>> getPropertyInfoByMonth(String replaymonth1,String replaymonth2) {
		// TODO Auto-generated method stub
		//获取三大产业的项目个数和总投资
		String sql = String.format(MonthReportSqlConstants.PROPERTY_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		//获取三大产业总的项目个数和总投资
		String ttsql = String.format(MonthReportSqlConstants.PROPERTYTOTAL_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		
		List<Map<String, Object>> list1 = this.dao.findBySql(sql, new Object[]{});
		List<Map<String, Object>> list2 = this.dao.findBySql(ttsql, new Object[]{});
		//合计信息和各个产业信息排列到一起
		if(null!=list2&list2.size()>0){
			// 把合计放在第一位
			list1.add(0, list2.get(0));
		}
		return list1;
	}

	/**
	 * 获取分项目类型情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:22:21
	 */
	@Override
	public List<Map<String, Object>> getProjectTypeInfoByMonth(String replaymonth1,String replaymonth2) {
		// TODO Auto-generated method stub
		//获取分项目类型的项目个数和总投资
		String sql = String.format(MonthReportSqlConstants.PROJECTTYPE_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		//获取审批项目个数和总投资
		String plsql = String.format(MonthReportSqlConstants.PLAN_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		//获取项目总个数和总投资
		String ttsql = String.format(MonthReportSqlConstants.TOTAL_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		
		List<Map<String, Object>> list1 = this.dao.findBySql(sql, new Object[]{});
		List<Map<String, Object>> list2 = this.dao.findBySql(ttsql, new Object[]{});
		List<Map<String, Object>> list3 = this.dao.findBySql(plsql, new Object[]{});
		//排列到一起
		if(null!=list3&list3.size()>0){
			// 把审批放在第一位
			list1.add(0, list3.get(0));
		}
		
		if(null!=list2&list2.size()>0){
			// 把合计放在第一位
			list1.add(0, list2.get(0));
		}
		return list1;
	}
	
	/**
	 * 获取分投资类型情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:22:21
	 */
	@Override
	public List<Map<String, Object>> getInvestTypeInfoByMonth(String replaymonth1,String replaymonth2) {
		// TODO Auto-generated method stub
		//获取政府投资和企业投资两种不同类型的项目个数和总投资
		String sql = String.format(MonthReportSqlConstants.INVESTTYPE_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		//获取项目总个数和总投资
		String ttsql = String.format(MonthReportSqlConstants.INVESTTYPETOTAL_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		
		List<Map<String, Object>> list1 = this.dao.findBySql(sql, new Object[]{});
		List<Map<String, Object>> list2 = this.dao.findBySql(ttsql, new Object[]{});
		//所有信息排列到一起
		if(null!=list2&list2.size()>0){
			// 把合计放在第一位
			list1.add(0, list2.get(0));
		}
		return list1;
	}

	/**
	 * 获取分投资规模情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:22:50
	 */
	@Override
	public List<Map<String, Object>> getInvestScaleInfoByMonth(String replaymonth1,String replaymonth2) {
		// TODO Auto-generated method stub
		//获取不同投资规模的项目个数和总投资
		String sql = String.format(MonthReportSqlConstants.INVESTSCALE_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		//获取项目总个数和总投资
		String ttsql = String.format(MonthReportSqlConstants.INVESTSCALETOTAL_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		
		List<Map<String, Object>> list1 = this.dao.findBySql(sql, new Object[]{});
		List<Map<String, Object>> list2 = this.dao.findBySql(ttsql, new Object[]{});
		//所有信息排列到一起
		if(null!=list2&list2.size()>0){
			// 把合计放在第一位
			list1.add(0, list2.get(0));
		}
		return list1;
	}

	/**
	 * 获取分开工时间情况
	 * @orderBy 
	 * @param replaymonth
	 * @return
	 * @author li
	 * @Date 2016年10月26日上午11:23:04
	 */
	@Override
	public List<Map<String, Object>> getStartTimeInfoByMonth(String replaymonth1,String replaymonth2) {
		// TODO Auto-generated method stub
		//获取不同开工时间项目个数和总投资
		String sql = String.format(MonthReportSqlConstants.STARTTIME_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		//获取总项目个数和总投资
		String ttsql = String.format(MonthReportSqlConstants.STARTTIMETOTAL_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		
		List<Map<String, Object>> list1 = this.dao.findBySql(sql, new Object[]{});
		List<Map<String, Object>> list2 = this.dao.findBySql(ttsql, new Object[]{});
		//总计信息和分开工信息排列到一起
		if(null!=list2&list2.size()>0){
			// 把合计放在第一位
			list1.add(0, list2.get(0));
		}
		return list1;
	}
	
	/**
	 * 获取总计信息
	 * @orderBy 
	 * @param replaymonth1
	 * @param replaymonth2
	 * @return
	 * @author li
	 * @Date 2016年10月29日下午3:46:54
	 */
	@Override
	public List<Map<String, Object>> getTotalInfoByMonth(String replaymonth1,String replaymonth2) {
		
		String ttsql = String.format(MonthReportSqlConstants.TOTAL_SQL, "'" + replaymonth1 + "'","'" + replaymonth2 + "'", "'" + String.valueOf(Integer.valueOf(replaymonth1)-100) + "'","'" + String.valueOf(Integer.valueOf(replaymonth2)-100) + "'");
		
		List<Map<String, Object>> list = this.dao.findBySql(ttsql, new Object[]{});
		
		return list;
		
	}
	

    /**
     * 生成word文档
     * @orderBy 
     * @param replaymonth1
     * @param replaymonth2
     * @return
     * @author li
     * @return 
     * @Date 2016年11月29日下午3:44:18
     */
    @SuppressWarnings("resource")
    public void createWord(String replaymonth1,String replaymonth2,String filePath,String fileName) {
    	//获取分行业信息表
    	List<Map<String, Object>> industrylist = this.getIndustryInfoByMonth(replaymonth1,replaymonth2);
    	//获取分地区信息表
    	List<Map<String, Object>> arealist = this.getAreaInfoByMonth(replaymonth1,replaymonth2);
    	//获取分产业信息表
    	List<Map<String, Object>> propertylist = this.getPropertyInfoByMonth(replaymonth1,replaymonth2);
    	//获取分投资类型信息表
    	List<Map<String, Object>> Investlist = this.getInvestTypeInfoByMonth(replaymonth1,replaymonth2);
    	//获取分项目类型信息表
    	List<Map<String, Object>> projectlist = this.getProjectTypeInfoByMonth(replaymonth1,replaymonth2);
    	//获取分投资规模信息表
    	List<Map<String, Object>> scalelist = this.getInvestScaleInfoByMonth(replaymonth1,replaymonth2);
    	//获取分开工年份信息表
    	List<Map<String, Object>> timelist = this.getStartTimeInfoByMonth(replaymonth1,replaymonth2);
    	//获取东三省信息表
    	List<Map<String, Object>> eastlist = this.getEastAreaInfoByMonth(replaymonth1,replaymonth2);
    	String yeid=replaymonth2.substring(0,4);
    	String monid=replaymonth2.substring(4);
    	//新建一个word文档
        XWPFDocument doc = new XWPFDocument();
        //首页
        XWPFParagraph phomepage1 = doc.createParagraph();
        phomepage1.setSpacingAfterLines(2500);
        XWPFParagraph phomepage2 = doc.createParagraph();
        XWPFParagraph phomepage3 = doc.createParagraph();
        //目录
        XWPFParagraph pcatalog1 = doc.createParagraph();
        XWPFParagraph pcatalog2 = doc.createParagraph();
        XWPFParagraph pcatalog3 = doc.createParagraph();
        XWPFParagraph pcatalog4 = doc.createParagraph();
        XWPFParagraph pcatalog5 = doc.createParagraph();
        XWPFParagraph pcatalog6 = doc.createParagraph();
        XWPFParagraph pcatalog7 = doc.createParagraph();
        XWPFParagraph pcatalog8 = doc.createParagraph();
        //第三页
        XWPFParagraph p1 = doc.createParagraph();
        XWPFParagraph p2 = doc.createParagraph();
        XWPFParagraph ptzlx = doc.createParagraph();
        XWPFParagraph p3 = doc.createParagraph();
        XWPFParagraph pxmlx = doc.createParagraph();
        XWPFParagraph p4 = doc.createParagraph();
        XWPFParagraph pcy = doc.createParagraph();
        XWPFParagraph p5 = doc.createParagraph();
        XWPFParagraph pdss = doc.createParagraph();
        XWPFParagraph p6 = doc.createParagraph();
        XWPFParagraph p7 = doc.createParagraph();
        p7.setPageBreak(true);
        XWPFTable table1 = doc.createTable(industrylist.size()+1, 7);
        XWPFParagraph p8 = doc.createParagraph();
        p8.setPageBreak(true);
        XWPFTable table2 = doc.createTable(arealist.size()+1, 7);
        XWPFParagraph p9 = doc.createParagraph();
        p9.setPageBreak(true);
        XWPFTable table3 = doc.createTable(eastlist.size()+1, 7);
        XWPFParagraph p10 = doc.createParagraph();
        p10.setPageBreak(true);
        XWPFTable table4 = doc.createTable(propertylist.size()+1, 7);
        XWPFParagraph p11 = doc.createParagraph();
        p11.setPageBreak(true);
        XWPFTable table5 = doc.createTable(projectlist.size()+1, 7);
        XWPFParagraph p12 = doc.createParagraph();
        p12.setPageBreak(true);
        XWPFTable table6 = doc.createTable(Investlist.size()+1, 7);
        XWPFParagraph p13 = doc.createParagraph();
        p13.setPageBreak(true);
        XWPFTable table7 = doc.createTable(scalelist.size()+1, 7);
        XWPFParagraph p14 = doc.createParagraph();
        p14.setPageBreak(true);
        XWPFTable table8 = doc.createTable(timelist.size()+1, 7);
        // CTTblBorders borders=table.getCTTbl().getTblPr().addNewTblBorders();
        
        // 设置行业表格标题字体对齐方式
        p7.setAlignment(ParagraphAlignment.CENTER);
        p7.setVerticalAlignment(TextAlignment.TOP);

        // 行业表格标题要使用p7所定义的属性
        XWPFRun r7 = p7.createRun();

        // 设置字体是否加粗
        r7.setBold(true);
        r7.setFontSize(16);

        // 设置使用何种字体
        r7.setFontFamily("宋体");

        // 设置上下两行之间的间距
        r7.setTextPosition(20);        
        r7.setText(yeid+"年"+monid+"月审批、核准、备案项目分行业情况统计表");
        
        //设置行业表格属性
        CTTblPr tblPr1 = table1.getCTTbl().getTblPr();
        tblPr1.getTblW().setType(STTblWidth.DXA);
        tblPr1.getTblW().setW(new BigInteger("9000"));

        // 设置上下左右四个方向的距离，可以将表格撑大
        table1.setCellMargins(20, 20, 50, 50);

        // 行业表格
        List<XWPFTableCell> tableCells1 = table1.getRow(0).getTableCells();

        //设置表格第一列的属性
        XWPFTableCell cell1 = tableCells1.get(0);
        CTTcPr cellPr1 = cell1.getCTTc().addNewTcPr(); 
        CTTblWidth cellw1 = cellPr1.addNewTcW();
        cellw1.setType(STTblWidth.DXA);
        cellPr1.addNewTcW().setW(BigInteger.valueOf(1800));
        XWPFTableCell cnt1 = tableCells1.get(1);
        XWPFTableCell cntratio1 = tableCells1.get(2);
        XWPFTableCell investtotal1 = tableCells1.get(3);
        CTTcPr cellPrinvesttotal1 = investtotal1.getCTTc().addNewTcPr(); 
        CTTblWidth cellwinvesttotal1 = cellPrinvesttotal1.addNewTcW();
        cellwinvesttotal1.setType(STTblWidth.DXA);
        cellPrinvesttotal1.addNewTcW().setW(BigInteger.valueOf(1500));
        XWPFTableCell inveatratio11 = tableCells1.get(4);
        XWPFTableCell cntrate1 = tableCells1.get(5);
        XWPFTableCell investrate1 = tableCells1.get(6);
        XWPFParagraph newPara1 = new XWPFParagraph(cell1.getCTTc().addNewP(), cell1);
        XWPFParagraph newcnt1 = new XWPFParagraph(cnt1.getCTTc().addNewP(), cnt1);
        XWPFParagraph newcntratio1 = new XWPFParagraph(cntratio1.getCTTc().addNewP(), cntratio1);
        XWPFParagraph newinvesttotal1 = new XWPFParagraph(investtotal1.getCTTc().addNewP(), investtotal1);
        XWPFParagraph newinveatratio11 = new XWPFParagraph(inveatratio11.getCTTc().addNewP(), inveatratio11);
        XWPFParagraph newcntrate1 = new XWPFParagraph(cntrate1.getCTTc().addNewP(), cntrate1);
        XWPFParagraph newinvestrate1 = new XWPFParagraph(investrate1.getCTTc().addNewP(), investrate1);
        
        //固定表头内容属性
        XWPFRun run1 = newPara1.createRun();
        XWPFRun runcnt1 = newcnt1.createRun();
        XWPFRun runcntratio1 = newcntratio1.createRun();
        XWPFRun runinvesttotal1 = newinvesttotal1.createRun();
        XWPFRun runinveatratio11 = newinveatratio11.createRun();
        XWPFRun runcntrate1 = newcntrate1.createRun();
        XWPFRun runinvestrate1 = newinvestrate1.createRun();
        /** 内容居中显示 **/
        newPara1.setAlignment(ParagraphAlignment.CENTER);
        newcnt1.setAlignment(ParagraphAlignment.CENTER);
        newcntratio1.setAlignment(ParagraphAlignment.CENTER);
        newinvesttotal1.setAlignment(ParagraphAlignment.CENTER);
        newinveatratio11.setAlignment(ParagraphAlignment.CENTER);
        newcntrate1.setAlignment(ParagraphAlignment.CENTER);
        newinvestrate1.setAlignment(ParagraphAlignment.CENTER);
        //加粗显示
        run1.setBold(true);
        runcnt1.setBold(true);
        runcntratio1.setBold(true);
        runinvesttotal1.setBold(true);
        runinveatratio11.setBold(true);
        runcntrate1.setBold(true);
        runinvestrate1.setBold(true);
        run1.setText("行业");
        runcnt1.setText("项目个数");
        runcntratio1.setText("项目个数同比");
        runinvesttotal1.setText("总投资（亿元）");
        runinveatratio11.setText("总投资同比");
        runcntrate1.setText("项目个数占比");
        runinvestrate1.setText("总投资占比");

        //循环遍历数据插入表格
        if(industrylist!=null){
        	for(int i=0;i<industrylist.size();i++){
                tableCells1 = table1.getRow(i+1).getTableCells();
                if(industrylist.get(i).get("code")!=null&&industrylist.get(i).get("code")!=""){
                	XWPFTableCell cellindustry = tableCells1.get(0);
                    CTTcPr cellPrindustry = cellindustry.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwindustry = cellPrindustry.addNewTcW();
                    cellwindustry.setType(STTblWidth.DXA);
                    cellPrindustry.addNewTcW().setW(BigInteger.valueOf(1800));
                    tableCells1.get(0).setText(industrylist.get(i).get("ITEM_VALUE").toString());
                    tableCells1.get(1).setText(industrylist.get(i).get("cnt").toString());
                    if(industrylist.get(i).get("cntratio")!=null&&!"".equals(industrylist.get(i).get("cntratio"))){
                    	tableCells1.get(2).setText(industrylist.get(i).get("cntratio").toString()+"%");
                    }else{
                    	tableCells1.get(2).setText(null);
                    }
                    XWPFTableCell investtotalindustry = tableCells1.get(3);
                    //设置列宽
                    CTTcPr cellPrinvesttotalindustry = investtotalindustry.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwinvesttotalindustry = cellPrinvesttotalindustry.addNewTcW();
                    cellwinvesttotalindustry.setType(STTblWidth.DXA);
                    cellPrinvesttotalindustry.addNewTcW().setW(BigInteger.valueOf(1500));
                    tableCells1.get(3).setText(industrylist.get(i).get("investtotal").toString());
                    if(industrylist.get(i).get("inveatratio")!=null&&!"".equals(industrylist.get(i).get("inveatratio"))){
                    	tableCells1.get(4).setText(industrylist.get(i).get("inveatratio").toString()+"%");
                    }else{
                    	tableCells1.get(4).setText(null);
                    }
                    tableCells1.get(5).setText(industrylist.get(i).get("cntrate").toString()+"%");
                    tableCells1.get(6).setText(industrylist.get(i).get("investrate").toString()+"%");

                }
                else{ 
                    XWPFTableCell cellindustry = tableCells1.get(0);
                    //设置列宽
                    CTTcPr cellPrindustry = cellindustry.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwindustry = cellPrindustry.addNewTcW();
                    cellwindustry.setType(STTblWidth.DXA);
                    cellPrindustry.addNewTcW().setW(BigInteger.valueOf(1800));
                    XWPFTableCell cntindustry = tableCells1.get(1);
                    XWPFTableCell cntratioindustry = tableCells1.get(2);
                    XWPFTableCell investtotalindustry = tableCells1.get(3);
                    //设置列宽
                    CTTcPr cellPrinvesttotalindustry = investtotalindustry.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwinvesttotalindustry = cellPrinvesttotalindustry.addNewTcW();
                    cellwinvesttotalindustry.setType(STTblWidth.DXA);
                    cellPrinvesttotalindustry.addNewTcW().setW(BigInteger.valueOf(1500));
                    XWPFTableCell inveatratio1industry = tableCells1.get(4);
                    XWPFTableCell cntrateindustry = tableCells1.get(5);
                    XWPFTableCell investrateindustry = tableCells1.get(6);
                    XWPFParagraph newParaindustry = new XWPFParagraph(cellindustry.getCTTc().addNewP(), cellindustry);
                    XWPFParagraph newcntindustry = new XWPFParagraph(cntindustry.getCTTc().addNewP(), cntindustry);
                    XWPFParagraph newcntratioindustry = new XWPFParagraph(cntratioindustry.getCTTc().addNewP(), cntratioindustry);
                    XWPFParagraph newinvesttotalindustry = new XWPFParagraph(investtotalindustry.getCTTc().addNewP(), investtotalindustry);
                    XWPFParagraph newinveatratio1industry = new XWPFParagraph(inveatratio1industry.getCTTc().addNewP(), inveatratio1industry);
                    XWPFParagraph newcntrateindustry = new XWPFParagraph(cntrateindustry.getCTTc().addNewP(), cntrateindustry);
                    XWPFParagraph newinvestrateindustry = new XWPFParagraph(investrateindustry.getCTTc().addNewP(), investrateindustry);
                    
                    //固定表头内容属性
                    XWPFRun runindustry = newParaindustry.createRun();
                    XWPFRun runcntindustry = newcntindustry.createRun();
                    XWPFRun runcntratioindustry = newcntratioindustry.createRun();
                    XWPFRun runinvesttotalindustry = newinvesttotalindustry.createRun();
                    XWPFRun runinveatratio1industry = newinveatratio1industry.createRun();
                    XWPFRun runcntrateindustry = newcntrateindustry.createRun();
                    XWPFRun runinvestrateindustry = newinvestrateindustry.createRun();
                    /** 内容居中显示 **/
/*                  newParaindustry.setAlignment(ParagraphAlignment.CENTER);
                    newcntindustry.setAlignment(ParagraphAlignment.CENTER);
                    newcntratioindustry.setAlignment(ParagraphAlignment.CENTER);
                    newinvesttotalindustry.setAlignment(ParagraphAlignment.CENTER);
                    newinveatratio1industry.setAlignment(ParagraphAlignment.CENTER);
                    newcntrateindustry.setAlignment(ParagraphAlignment.CENTER);
                    newinvestrateindustry.setAlignment(ParagraphAlignment.CENTER);*/
                    //加粗显示
                    runindustry.setBold(true);
                    runcntindustry.setBold(true);
                    runcntratioindustry.setBold(true);
                    runinvesttotalindustry.setBold(true);
                    runinveatratio1industry.setBold(true);
                    runcntrateindustry.setBold(true);
                    runinvestrateindustry.setBold(true);
                    runindustry.setText(industrylist.get(i).get("ITEM_VALUE").toString());
                    runcntindustry.setText(industrylist.get(i).get("cnt").toString());
                    if(industrylist.get(i).get("cntratio")!=null&&!"".equals(industrylist.get(i).get("cntratio"))){
                    	runcntratioindustry.setText(industrylist.get(i).get("cntratio").toString()+"%");
                    }else{
                    	runcntratioindustry.setText(null);
                    }
                    //runcntratioindustry.setText(industrylist.get(i).get("cntratio").toString()+"%");
                    runinvesttotalindustry.setText(industrylist.get(i).get("investtotal").toString());
                    if(industrylist.get(i).get("inveatratio")!=null&&!"".equals(industrylist.get(i).get("inveatratio"))){
                    	runinveatratio1industry.setText(industrylist.get(i).get("inveatratio").toString()+"%");
                    }else{
                    	runinveatratio1industry.setText(null);
                    }
                    //runinveatratio1industry.setText(industrylist.get(i).get("inveatratio").toString()+"%");
                    runcntrateindustry.setText(industrylist.get(i).get("cntrate").toString()+"%");
                    runinvestrateindustry.setText(industrylist.get(i).get("investrate").toString()+"%");
                }
        	}
        	
        }
        
        // 设置地区表格标题字体对齐方式
        p8.setAlignment(ParagraphAlignment.CENTER);
        p8.setVerticalAlignment(TextAlignment.TOP);

        // 地区表格标题要使用p8所定义的属性
        XWPFRun r8 = p8.createRun();

        // 设置字体是否加粗
        r8.setBold(true);
        r8.setFontSize(16);

        // 设置使用何种字体
        r8.setFontFamily("宋体");

        // 设置上下两行之间的间距
        r8.setTextPosition(20);        
        r8.setText(yeid+"年"+monid+"月审批、核准、备案项目分地区情况统计表");
        
        //设置地区表格属性
        CTTblPr tblPr2 = table2.getCTTbl().getTblPr();
        tblPr2.getTblW().setType(STTblWidth.DXA);
        tblPr2.getTblW().setW(new BigInteger("9000"));

        // 设置上下左右四个方向的距离，可以将表格撑大
        table1.setCellMargins(20, 20, 30, 30);

        // 地区表格
        List<XWPFTableCell> tableCells2 = table2.getRow(0).getTableCells();

        //设置表格第一列的属性
        XWPFTableCell cell2 = tableCells2.get(0);
        CTTcPr cellPr2 = cell2.getCTTc().addNewTcPr(); 
        CTTblWidth cellw2 = cellPr2.addNewTcW();
        cellw2.setType(STTblWidth.DXA);
        cellPr2.addNewTcW().setW(BigInteger.valueOf(1800));
        XWPFTableCell cnt2 = tableCells2.get(1);
        XWPFTableCell cntratio2 = tableCells2.get(2);
        XWPFTableCell investtotal2 = tableCells2.get(3);
        CTTcPr cellPrinvesttotal2 = investtotal2.getCTTc().addNewTcPr(); 
        CTTblWidth cellwinvesttotal2 = cellPrinvesttotal2.addNewTcW();
        cellwinvesttotal2.setType(STTblWidth.DXA);
        cellPrinvesttotal2.addNewTcW().setW(BigInteger.valueOf(1500));
        XWPFTableCell inveatratio12 = tableCells2.get(4);
        XWPFTableCell cntrate2 = tableCells2.get(5);
        XWPFTableCell investrate2 = tableCells2.get(6);
        XWPFParagraph newPara2 = new XWPFParagraph(cell2.getCTTc().addNewP(), cell2);
        XWPFParagraph newcnt2 = new XWPFParagraph(cnt2.getCTTc().addNewP(), cnt2);
        XWPFParagraph newcntratio2 = new XWPFParagraph(cntratio2.getCTTc().addNewP(), cntratio2);
        XWPFParagraph newinvesttotal2 = new XWPFParagraph(investtotal2.getCTTc().addNewP(), investtotal2);
        XWPFParagraph newinveatratio12 = new XWPFParagraph(inveatratio12.getCTTc().addNewP(), inveatratio12);
        XWPFParagraph newcntrate2 = new XWPFParagraph(cntrate2.getCTTc().addNewP(), cntrate2);
        XWPFParagraph newinvestrate2 = new XWPFParagraph(investrate2.getCTTc().addNewP(), investrate2);
        
        //固定表头内容属性
        XWPFRun run2 = newPara2.createRun();
        XWPFRun runcnt2 = newcnt2.createRun();
        XWPFRun runcntratio2 = newcntratio2.createRun();
        XWPFRun runinvesttotal2 = newinvesttotal2.createRun();
        XWPFRun runinveatratio12 = newinveatratio12.createRun();
        XWPFRun runcntrate2 = newcntrate2.createRun();
        XWPFRun runinvestrate2 = newinvestrate2.createRun();
        /** 内容居中显示 **/
        newPara2.setAlignment(ParagraphAlignment.CENTER);
        newcnt2.setAlignment(ParagraphAlignment.CENTER);
        newcntratio2.setAlignment(ParagraphAlignment.CENTER);
        newinvesttotal2.setAlignment(ParagraphAlignment.CENTER);
        newinveatratio12.setAlignment(ParagraphAlignment.CENTER);
        newcntrate2.setAlignment(ParagraphAlignment.CENTER);
        newinvestrate2.setAlignment(ParagraphAlignment.CENTER);
        //加粗显示
        run2.setBold(true);
        runcnt2.setBold(true);
        runcntratio2.setBold(true);
        runinvesttotal2.setBold(true);
        runinveatratio12.setBold(true);
        runcntrate2.setBold(true);
        runinvestrate2.setBold(true);
        run2.setText("地区");
        runcnt2.setText("项目个数");
        runcntratio2.setText("项目个数同比");
        runinvesttotal2.setText("总投资（亿元）");
        runinveatratio12.setText("总投资同比");
        runcntrate2.setText("项目个数占比");
        runinvestrate2.setText("总投资占比"); 

        //循环遍历数据插入表格
        if(arealist!=null){
        	for(int i=0;i<arealist.size();i++){
                tableCells2 = table2.getRow(i+1).getTableCells();
                if(arealist.get(i).get("ITEM_KEY")!=null&&arealist.get(i).get("ITEM_KEY")!=""){
                	XWPFTableCell cellarea = tableCells2.get(0);
                    //设置列宽
                    CTTcPr cellPrarea = cellarea.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwarea = cellPrarea.addNewTcW();
                    cellwarea.setType(STTblWidth.DXA);
                    cellPrarea.addNewTcW().setW(BigInteger.valueOf(1800));
                    tableCells2.get(0).setText(arealist.get(i).get("ITEM_VALUE").toString());
                    tableCells2.get(1).setText(arealist.get(i).get("cnt").toString());
                    if(arealist.get(i).get("cntratio")!=null&&!"".equals(arealist.get(i).get("cntratio"))){
                    	tableCells2.get(2).setText(arealist.get(i).get("cntratio").toString()+"%");
                    }else{
                    	tableCells2.get(2).setText(null);
                    }
                    XWPFTableCell investtotalarea = tableCells2.get(3);
                    //设置列宽
                    CTTcPr cellPrinvesttotalarea = investtotalarea.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwinvesttotalarea = cellPrinvesttotalarea.addNewTcW();
                    cellwinvesttotalarea.setType(STTblWidth.DXA);
                    cellPrinvesttotalarea.addNewTcW().setW(BigInteger.valueOf(1500));
                    tableCells2.get(3).setText(arealist.get(i).get("investtotal").toString());
                    if(arealist.get(i).get("inveatratio")!=null&&!"".equals(arealist.get(i).get("inveatratio"))){
                    	tableCells2.get(4).setText(arealist.get(i).get("inveatratio").toString()+"%");
                    }else{
                    	tableCells2.get(4).setText(null);
                    }
                    tableCells2.get(5).setText(arealist.get(i).get("cntrate").toString()+"%");
                    tableCells2.get(6).setText(arealist.get(i).get("investrate").toString()+"%");

                }
                else{ 
                    XWPFTableCell cellarea = tableCells2.get(0);
                    //设置列宽
                    CTTcPr cellPrarea = cellarea.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwarea = cellPrarea.addNewTcW();
                    cellwarea.setType(STTblWidth.DXA);
                    cellPrarea.addNewTcW().setW(BigInteger.valueOf(1800));
                    XWPFTableCell cntarea = tableCells2.get(1);
                    XWPFTableCell cntratioarea = tableCells2.get(2);
                    XWPFTableCell investtotalarea = tableCells2.get(3);
                    //设置列宽
                    CTTcPr cellPrinvesttotalarea = investtotalarea.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwinvesttotalarea = cellPrinvesttotalarea.addNewTcW();
                    cellwinvesttotalarea.setType(STTblWidth.DXA);
                    cellPrinvesttotalarea.addNewTcW().setW(BigInteger.valueOf(1500));
                    XWPFTableCell inveatratio1area = tableCells2.get(4);
                    XWPFTableCell cntratearea = tableCells2.get(5);
                    XWPFTableCell investratearea = tableCells2.get(6);
                    XWPFParagraph newParaarea = new XWPFParagraph(cellarea.getCTTc().addNewP(), cellarea);
                    XWPFParagraph newcntarea = new XWPFParagraph(cntarea.getCTTc().addNewP(), cntarea);
                    XWPFParagraph newcntratioarea = new XWPFParagraph(cntratioarea.getCTTc().addNewP(), cntratioarea);
                    XWPFParagraph newinvesttotalarea = new XWPFParagraph(investtotalarea.getCTTc().addNewP(), investtotalarea);
                    XWPFParagraph newinveatratio1area = new XWPFParagraph(inveatratio1area.getCTTc().addNewP(), inveatratio1area);
                    XWPFParagraph newcntratearea = new XWPFParagraph(cntratearea.getCTTc().addNewP(), cntratearea);
                    XWPFParagraph newinvestratearea = new XWPFParagraph(investratearea.getCTTc().addNewP(), investratearea);
                    
                    //固定表头内容属性
                    XWPFRun runarea = newParaarea.createRun();
                    XWPFRun runcntarea = newcntarea.createRun();
                    XWPFRun runcntratioarea = newcntratioarea.createRun();
                    XWPFRun runinvesttotalarea = newinvesttotalarea.createRun();
                    XWPFRun runinveatratio1area = newinveatratio1area.createRun();
                    XWPFRun runcntratearea = newcntratearea.createRun();
                    XWPFRun runinvestratearea = newinvestratearea.createRun();
                    /** 内容居中显示 **/
/*                    newParaarea.setAlignment(ParagraphAlignment.CENTER);
                    newcntarea.setAlignment(ParagraphAlignment.CENTER);
                    newcntratioarea.setAlignment(ParagraphAlignment.CENTER);
                    newinvesttotalarea.setAlignment(ParagraphAlignment.CENTER);
                    newinveatratio1area.setAlignment(ParagraphAlignment.CENTER);
                    newcntratearea.setAlignment(ParagraphAlignment.CENTER);
                    newinvestratearea.setAlignment(ParagraphAlignment.CENTER);*/
                    //加粗显示
                    runarea.setBold(true);
                    runcntarea.setBold(true);
                    runcntratioarea.setBold(true);
                    runinvesttotalarea.setBold(true);
                    runinveatratio1area.setBold(true);
                    runcntratearea.setBold(true);
                    runinvestratearea.setBold(true);
                    runarea.setText(arealist.get(i).get("ITEM_VALUE").toString());
                    runcntarea.setText(arealist.get(i).get("cnt").toString());
                    if(arealist.get(i).get("cntratio")!=null&&!"".equals(arealist.get(i).get("cntratio"))){
                    	runcntratioarea.setText(arealist.get(i).get("cntratio").toString()+"%");
                    }else{
                    	runcntratioarea.setText(null);
                    }
                    //runcntratioarea.setText(arealist.get(i).get("cntratio").toString()+"%");
                    runinvesttotalarea.setText(arealist.get(i).get("investtotal").toString());
                    if(arealist.get(i).get("inveatratio")!=null&&!"".equals(arealist.get(i).get("inveatratio"))){
                    	runinveatratio1area.setText(arealist.get(i).get("inveatratio").toString()+"%");
                    }else{
                    	runinveatratio1area.setText(null);
                    }
                    //runinveatratio1area.setText(arealist.get(i).get("inveatratio").toString()+"%");
                    runcntratearea.setText(arealist.get(i).get("cntrate").toString()+"%");
                    runinvestratearea.setText(arealist.get(i).get("investrate").toString()+"%");
                }
        	}
        	
        }

        // 设置东三省地区表格标题字体对齐方式
        p9.setAlignment(ParagraphAlignment.CENTER);
        p9.setVerticalAlignment(TextAlignment.TOP);

        // 东三省地区表格标题要使用p9所定义的属性
        XWPFRun r9 = p9.createRun();

        // 设置字体是否加粗
        r9.setBold(true);
        r9.setFontSize(16);

        // 设置使用何种字体
        r9.setFontFamily("宋体");

        // 设置上下两行之间的间距
        r9.setTextPosition(20);        
        r9.setText(yeid+"年"+monid+"月审批、核准、备案项目东北三省情况统计表");
        
        //设置东三省地区表格属性
        CTTblPr tblPr3 = table3.getCTTbl().getTblPr();
        tblPr3.getTblW().setType(STTblWidth.DXA);
        tblPr3.getTblW().setW(new BigInteger("9000"));

        // 设置上下左右四个方向的距离，可以将表格撑大
        table3.setCellMargins(20, 20, 30, 30);

        // 东三省地区表格
        List<XWPFTableCell> tableCells3 = table3.getRow(0).getTableCells();

        //设置表格第一列的属性
        XWPFTableCell cell3 = tableCells3.get(0);
        CTTcPr cellPr3 = cell3.getCTTc().addNewTcPr(); 
        CTTblWidth cellw3 = cellPr3.addNewTcW();
        cellw3.setType(STTblWidth.DXA);
        cellPr3.addNewTcW().setW(BigInteger.valueOf(1800));
        XWPFTableCell cnt3 = tableCells3.get(1);
        XWPFTableCell cntratio3 = tableCells3.get(2);
        XWPFTableCell investtotal3 = tableCells3.get(3);
        CTTcPr cellPrinvesttotal3 = investtotal3.getCTTc().addNewTcPr(); 
        CTTblWidth cellwinvesttotal3 = cellPrinvesttotal3.addNewTcW();
        cellwinvesttotal3.setType(STTblWidth.DXA);
        cellPrinvesttotal3.addNewTcW().setW(BigInteger.valueOf(1500));
        XWPFTableCell inveatratio13 = tableCells3.get(4);
        XWPFTableCell cntrate3 = tableCells3.get(5);
        XWPFTableCell investrate3 = tableCells3.get(6);
        XWPFParagraph newPara3 = new XWPFParagraph(cell3.getCTTc().addNewP(), cell3);
        XWPFParagraph newcnt3 = new XWPFParagraph(cnt3.getCTTc().addNewP(), cnt3);
        XWPFParagraph newcntratio3 = new XWPFParagraph(cntratio3.getCTTc().addNewP(), cntratio3);
        XWPFParagraph newinvesttotal3 = new XWPFParagraph(investtotal3.getCTTc().addNewP(), investtotal3);
        XWPFParagraph newinveatratio13 = new XWPFParagraph(inveatratio13.getCTTc().addNewP(), inveatratio13);
        XWPFParagraph newcntrate3 = new XWPFParagraph(cntrate3.getCTTc().addNewP(), cntrate3);
        XWPFParagraph newinvestrate3 = new XWPFParagraph(investrate3.getCTTc().addNewP(), investrate3);
        
        //固定表头内容属性
        XWPFRun run3 = newPara3.createRun();
        XWPFRun runcnt3 = newcnt3.createRun();
        XWPFRun runcntratio3 = newcntratio3.createRun();
        XWPFRun runinvesttotal3 = newinvesttotal3.createRun();
        XWPFRun runinveatratio13 = newinveatratio13.createRun();
        XWPFRun runcntrate3 = newcntrate3.createRun();
        XWPFRun runinvestrate3 = newinvestrate3.createRun();
        /** 内容居中显示 **/
        newPara3.setAlignment(ParagraphAlignment.CENTER);
        newcnt3.setAlignment(ParagraphAlignment.CENTER);
        newcntratio3.setAlignment(ParagraphAlignment.CENTER);
        newinvesttotal3.setAlignment(ParagraphAlignment.CENTER);
        newinveatratio13.setAlignment(ParagraphAlignment.CENTER);
        newcntrate3.setAlignment(ParagraphAlignment.CENTER);
        newinvestrate3.setAlignment(ParagraphAlignment.CENTER);
        //加粗显示
        run3.setBold(true);
        runcnt3.setBold(true);
        runcntratio3.setBold(true);
        runinvesttotal3.setBold(true);
        runinveatratio13.setBold(true);
        runcntrate3.setBold(true);
        runinvestrate3.setBold(true);
        run3.setText("分类");
        runcnt3.setText("项目个数");
        runcntratio3.setText("项目个数同比");
        runinvesttotal3.setText("总投资（亿元）");
        runinveatratio13.setText("总投资同比");
        runcntrate3.setText("项目个数占比");
        runinvestrate3.setText("总投资占比");

        //循环遍历数据插入表格
        if(eastlist!=null){
        	for(int i=0;i<eastlist.size();i++){
                tableCells3 = table3.getRow(i+1).getTableCells();
                if(i==0){
                    XWPFTableCell celleast = tableCells3.get(0);
                    //设置列宽
                    CTTcPr cellPreast = celleast.getCTTc().addNewTcPr(); 
                    CTTblWidth cellweast = cellPreast.addNewTcW();
                    cellweast.setType(STTblWidth.DXA);
                    cellPreast.addNewTcW().setW(BigInteger.valueOf(1800));
                    XWPFTableCell cnteast = tableCells3.get(1);
                    XWPFTableCell cntratioeast = tableCells3.get(2);
                    XWPFTableCell investtotaleast = tableCells3.get(3);
                    //设置列宽
                    CTTcPr cellPrinvesttotaleast = investtotaleast.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwinvesttotaleast = cellPrinvesttotaleast.addNewTcW();
                    cellwinvesttotaleast.setType(STTblWidth.DXA);
                    cellPrinvesttotaleast.addNewTcW().setW(BigInteger.valueOf(1500));
                    XWPFTableCell inveatratio1east = tableCells3.get(4);
                    XWPFTableCell cntrateeast = tableCells3.get(5);
                    XWPFTableCell investrateeast = tableCells3.get(6);
                    XWPFParagraph newParaeast = new XWPFParagraph(celleast.getCTTc().addNewP(), celleast);
                    XWPFParagraph newcnteast = new XWPFParagraph(cnteast.getCTTc().addNewP(), cnteast);
                    XWPFParagraph newcntratioeast = new XWPFParagraph(cntratioeast.getCTTc().addNewP(), cntratioeast);
                    XWPFParagraph newinvesttotaleast = new XWPFParagraph(investtotaleast.getCTTc().addNewP(), investtotaleast);
                    XWPFParagraph newinveatratio1east = new XWPFParagraph(inveatratio1east.getCTTc().addNewP(), inveatratio1east);
                    XWPFParagraph newcntrateeast = new XWPFParagraph(cntrateeast.getCTTc().addNewP(), cntrateeast);
                    XWPFParagraph newinvestrateeast = new XWPFParagraph(investrateeast.getCTTc().addNewP(), investrateeast);
                    
                    //固定表头内容属性
                    XWPFRun runeast = newParaeast.createRun();
                    XWPFRun runcnteast = newcnteast.createRun();
                    XWPFRun runcntratioeast = newcntratioeast.createRun();
                    XWPFRun runinvesttotaleast = newinvesttotaleast.createRun();
                    XWPFRun runinveatratio1east = newinveatratio1east.createRun();
                    XWPFRun runcntrateeast = newcntrateeast.createRun();
                    XWPFRun runinvestrateeast = newinvestrateeast.createRun();
                    /** 内容居中显示 **/
/*                    newParaeast.setAlignment(ParagraphAlignment.CENTER);
                    newcnteast.setAlignment(ParagraphAlignment.CENTER);
                    newcntratioeast.setAlignment(ParagraphAlignment.CENTER);
                    newinvesttotaleast.setAlignment(ParagraphAlignment.CENTER);
                    newinveatratio1east.setAlignment(ParagraphAlignment.CENTER);
                    newcntrateeast.setAlignment(ParagraphAlignment.CENTER);
                    newinvestrateeast.setAlignment(ParagraphAlignment.CENTER);*/
                    //加粗显示
                    runeast.setBold(true);
                    runcnteast.setBold(true);
                    runcntratioeast.setBold(true);
                    runinvesttotaleast.setBold(true);
                    runinveatratio1east.setBold(true);
                    runcntrateeast.setBold(true);
                    runinvestrateeast.setBold(true);
	                runeast.setText(eastlist.get(i).get("ITEM_VALUE").toString());
	                runcnteast.setText(eastlist.get(i).get("cnt").toString());
                    if(eastlist.get(i).get("cntratio")!=null&&!"".equals(eastlist.get(i).get("cntratio"))){
                    	runcntratioeast.setText(eastlist.get(i).get("cntratio").toString()+"%");
                    }else{
                    	runcntratioeast.setText(null);
                    }
	                //runcntratioeast.setText(eastlist.get(i).get("cntratio").toString()+"%");
	                runinvesttotaleast.setText(eastlist.get(i).get("investtotal").toString());
                    if(eastlist.get(i).get("inveatratio")!=null&&!"".equals(eastlist.get(i).get("inveatratio"))){
                    	runinveatratio1east.setText(eastlist.get(i).get("inveatratio").toString()+"%");
                    }else{
                    	runinveatratio1east.setText(null);
                    }
	                //runinveatratio1east.setText(eastlist.get(i).get("inveatratio").toString()+"%");
	                runcntrateeast.setText(eastlist.get(i).get("cntrate").toString()+"%");
	                runinvestrateeast.setText(eastlist.get(i).get("investrate").toString()+"%");
                }
                else{
                    XWPFTableCell celleast = tableCells3.get(0);
                    //设置列宽
                    CTTcPr cellPreast = celleast.getCTTc().addNewTcPr(); 
                    CTTblWidth cellweast = cellPreast.addNewTcW();
                    cellweast.setType(STTblWidth.DXA);
                    cellPreast.addNewTcW().setW(BigInteger.valueOf(1800));
                    tableCells3.get(0).setText(eastlist.get(i).get("ITEM_VALUE").toString());
                    tableCells3.get(1).setText(eastlist.get(i).get("cnt").toString());
                    if(eastlist.get(i).get("cntratio")!=null&&!"".equals(eastlist.get(i).get("cntratio"))){
                    tableCells3.get(2).setText(eastlist.get(i).get("cntratio").toString()+"%");
                    }else{
                    	tableCells3.get(2).setText(null);
                    }
                    XWPFTableCell investtotaleast = tableCells3.get(3);
                    //设置列宽
                    CTTcPr cellPrinvesttotaleast = investtotaleast.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwinvesttotaleast = cellPrinvesttotaleast.addNewTcW();
                    cellwinvesttotaleast.setType(STTblWidth.DXA);
                    cellPrinvesttotaleast.addNewTcW().setW(BigInteger.valueOf(1500));
                    tableCells3.get(3).setText(eastlist.get(i).get("investtotal").toString());
                    if(eastlist.get(i).get("inveatratio")!=null&&!"".equals(eastlist.get(i).get("inveatratio"))){
                        tableCells3.get(4).setText(eastlist.get(i).get("inveatratio").toString()+"%");
                    }else{
                    	tableCells3.get(4).setText(null);
                    }
                    tableCells3.get(5).setText(eastlist.get(i).get("cntrate").toString()+"%");
                    tableCells3.get(6).setText(eastlist.get(i).get("investrate").toString()+"%");  	
                }
        	}
        	
        }

        // 设置分产业表格标题字体对齐方式
        p10.setAlignment(ParagraphAlignment.CENTER);
        p10.setVerticalAlignment(TextAlignment.TOP);

        // 分产业表格标题要使用p10所定义的属性
        XWPFRun r10 = p10.createRun();

        // 设置字体是否加粗
        r10.setBold(true);
        r10.setFontSize(16);

        // 设置使用何种字体
        r10.setFontFamily("宋体");

        // 设置上下两行之间的间距
        r10.setTextPosition(20);        
        r10.setText(yeid+"年"+monid+"月审批、核准、备案项目分产业情况统计表");
        
        //设置分产业表格属性
        CTTblPr tblPr4 = table4.getCTTbl().getTblPr();
        tblPr4.getTblW().setType(STTblWidth.DXA);
        tblPr4.getTblW().setW(new BigInteger("9000"));

        // 设置上下左右四个方向的距离，可以将表格撑大
        table4.setCellMargins(20, 20, 30, 30);

        // 产业表格
        List<XWPFTableCell> tableCells4 = table4.getRow(0).getTableCells();

        //设置表格第一列的属性
        XWPFTableCell cell4 = tableCells4.get(0);
        CTTcPr cellPr4 = cell4.getCTTc().addNewTcPr(); 
        CTTblWidth cellw4 = cellPr4.addNewTcW();
        cellw4.setType(STTblWidth.DXA);
        cellPr4.addNewTcW().setW(BigInteger.valueOf(1800));
        XWPFTableCell cnt4 = tableCells4.get(1);
        XWPFTableCell cntratio4 = tableCells4.get(2);
        XWPFTableCell investtotal4 = tableCells4.get(3);
        CTTcPr cellPrinvesttotal4 = investtotal4.getCTTc().addNewTcPr(); 
        CTTblWidth cellwinvesttotal4 = cellPrinvesttotal4.addNewTcW();
        cellwinvesttotal4.setType(STTblWidth.DXA);
        cellPrinvesttotal4.addNewTcW().setW(BigInteger.valueOf(1500));
        XWPFTableCell inveatratio14 = tableCells4.get(4);
        XWPFTableCell cntrate4 = tableCells4.get(5);
        XWPFTableCell investrate4 = tableCells4.get(6);
        XWPFParagraph newPara4 = new XWPFParagraph(cell4.getCTTc().addNewP(), cell4);
        XWPFParagraph newcnt4 = new XWPFParagraph(cnt4.getCTTc().addNewP(), cnt4);
        XWPFParagraph newcntratio4 = new XWPFParagraph(cntratio4.getCTTc().addNewP(), cntratio4);
        XWPFParagraph newinvesttotal4 = new XWPFParagraph(investtotal4.getCTTc().addNewP(), investtotal4);
        XWPFParagraph newinveatratio14 = new XWPFParagraph(inveatratio14.getCTTc().addNewP(), inveatratio14);
        XWPFParagraph newcntrate4 = new XWPFParagraph(cntrate4.getCTTc().addNewP(), cntrate4);
        XWPFParagraph newinvestrate4 = new XWPFParagraph(investrate4.getCTTc().addNewP(), investrate4);
        
        //固定表头内容属性
        XWPFRun run4 = newPara4.createRun();
        XWPFRun runcnt4 = newcnt4.createRun();
        XWPFRun runcntratio4 = newcntratio4.createRun();
        XWPFRun runinvesttotal4 = newinvesttotal4.createRun();
        XWPFRun runinveatratio14 = newinveatratio14.createRun();
        XWPFRun runcntrate4 = newcntrate4.createRun();
        XWPFRun runinvestrate4 = newinvestrate4.createRun();
        /** 内容居中显示 **/
        newPara4.setAlignment(ParagraphAlignment.CENTER);
        newcnt4.setAlignment(ParagraphAlignment.CENTER);
        newcntratio4.setAlignment(ParagraphAlignment.CENTER);
        newinvesttotal4.setAlignment(ParagraphAlignment.CENTER);
        newinveatratio14.setAlignment(ParagraphAlignment.CENTER);
        newcntrate4.setAlignment(ParagraphAlignment.CENTER);
        newinvestrate4.setAlignment(ParagraphAlignment.CENTER);
        //加粗显示
        run4.setBold(true);
        runcnt4.setBold(true);
        runcntratio4.setBold(true);
        runinvesttotal4.setBold(true);
        runinveatratio14.setBold(true);
        runcntrate4.setBold(true);
        runinvestrate4.setBold(true);
        run4.setText("产业");
        runcnt4.setText("项目个数");
        runcntratio4.setText("项目个数同比");
        runinvesttotal4.setText("总投资（亿元）");
        runinveatratio14.setText("总投资同比");
        runcntrate4.setText("项目个数占比");
        runinvestrate4.setText("总投资占比");

        //循环遍历数据插入表格
        if(propertylist!=null){
        	for(int i=0;i<propertylist.size();i++){
                tableCells4 = table4.getRow(i+1).getTableCells();
                if(i==0){
                    XWPFTableCell cellproperty = tableCells4.get(0);
                    //设置列宽
                    CTTcPr cellPrproperty = cellproperty.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwproperty = cellPrproperty.addNewTcW();
                    cellwproperty.setType(STTblWidth.DXA);
                    cellPrproperty.addNewTcW().setW(BigInteger.valueOf(1800));
                    XWPFTableCell cntproperty = tableCells4.get(1);
                    XWPFTableCell cntratioproperty = tableCells4.get(2);
                    XWPFTableCell investtotalproperty = tableCells4.get(3);
                    //设置列宽
                    CTTcPr cellPrinvesttotalproperty = investtotalproperty.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwinvesttotalproperty = cellPrinvesttotalproperty.addNewTcW();
                    cellwinvesttotalproperty.setType(STTblWidth.DXA);
                    cellPrinvesttotalproperty.addNewTcW().setW(BigInteger.valueOf(1500));
                    XWPFTableCell inveatratio1property = tableCells4.get(4);
                    XWPFTableCell cntrateproperty = tableCells4.get(5);
                    XWPFTableCell investrateproperty = tableCells4.get(6);
                    XWPFParagraph newParaproperty = new XWPFParagraph(cellproperty.getCTTc().addNewP(), cellproperty);
                    XWPFParagraph newcntproperty = new XWPFParagraph(cntproperty.getCTTc().addNewP(), cntproperty);
                    XWPFParagraph newcntratioproperty = new XWPFParagraph(cntratioproperty.getCTTc().addNewP(), cntratioproperty);
                    XWPFParagraph newinvesttotalproperty = new XWPFParagraph(investtotalproperty.getCTTc().addNewP(), investtotalproperty);
                    XWPFParagraph newinveatratio1property = new XWPFParagraph(inveatratio1property.getCTTc().addNewP(), inveatratio1property);
                    XWPFParagraph newcntrateproperty = new XWPFParagraph(cntrateproperty.getCTTc().addNewP(), cntrateproperty);
                    XWPFParagraph newinvestrateproperty = new XWPFParagraph(investrateproperty.getCTTc().addNewP(), investrateproperty);
                    
                    //固定表头内容属性
                    XWPFRun runproperty = newParaproperty.createRun();
                    XWPFRun runcntproperty = newcntproperty.createRun();
                    XWPFRun runcntratioproperty = newcntratioproperty.createRun();
                    XWPFRun runinvesttotalproperty = newinvesttotalproperty.createRun();
                    XWPFRun runinveatratio1property = newinveatratio1property.createRun();
                    XWPFRun runcntrateproperty = newcntrateproperty.createRun();
                    XWPFRun runinvestrateproperty = newinvestrateproperty.createRun();
                    /** 内容居中显示 **/
/*                    newParaproperty.setAlignment(ParagraphAlignment.CENTER);
                    newcntproperty.setAlignment(ParagraphAlignment.CENTER);
                    newcntratioproperty.setAlignment(ParagraphAlignment.CENTER);
                    newinvesttotalproperty.setAlignment(ParagraphAlignment.CENTER);
                    newinveatratio1property.setAlignment(ParagraphAlignment.CENTER);
                    newcntrateproperty.setAlignment(ParagraphAlignment.CENTER);
                    newinvestrateproperty.setAlignment(ParagraphAlignment.CENTER);*/
                    //加粗显示
                    runproperty.setBold(true);
                    runcntproperty.setBold(true);
                    runcntratioproperty.setBold(true);
                    runinvesttotalproperty.setBold(true);
                    runinveatratio1property.setBold(true);
                    runcntrateproperty.setBold(true);
                    runinvestrateproperty.setBold(true);
	                runproperty.setText(propertylist.get(i).get("CY").toString());
	                runcntproperty.setText(propertylist.get(i).get("cnt").toString());
	                runcntratioproperty.setText(propertylist.get(i).get("cntratio").toString()+"%");
	                runinvesttotalproperty.setText(propertylist.get(i).get("investtotal").toString());
	                runinveatratio1property.setText(propertylist.get(i).get("inveatratio").toString()+"%");
	                runcntrateproperty.setText(propertylist.get(i).get("cntrate").toString()+"%");
	                runinvestrateproperty.setText(propertylist.get(i).get("investrate").toString()+"%");
                }
                else{
                    XWPFTableCell cellproperty = tableCells4.get(0);
                    //设置列宽
                    CTTcPr cellPrproperty = cellproperty.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwproperty = cellPrproperty.addNewTcW();
                    cellwproperty.setType(STTblWidth.DXA);
                    cellPrproperty.addNewTcW().setW(BigInteger.valueOf(1800));
                    tableCells4.get(0).setText(propertylist.get(i).get("CY").toString());
                    tableCells4.get(1).setText(propertylist.get(i).get("cnt").toString());
                    tableCells4.get(2).setText(propertylist.get(i).get("cntratio").toString()+"%");
                    XWPFTableCell investtotalproperty = tableCells4.get(3);
                    //设置列宽
                    CTTcPr cellPrinvesttotalproperty = investtotalproperty.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwinvesttotalproperty = cellPrinvesttotalproperty.addNewTcW();
                    cellwinvesttotalproperty.setType(STTblWidth.DXA);
                    cellPrinvesttotalproperty.addNewTcW().setW(BigInteger.valueOf(1500));
                    tableCells4.get(3).setText(propertylist.get(i).get("investtotal").toString());
                    tableCells4.get(4).setText(propertylist.get(i).get("inveatratio").toString()+"%");
                    tableCells4.get(5).setText(propertylist.get(i).get("cntrate").toString()+"%");
                    tableCells4.get(6).setText(propertylist.get(i).get("investrate").toString()+"%");    	
                }
        	}
        	
        }

        // 设置分项目类型表格标题字体对齐方式
        p11.setAlignment(ParagraphAlignment.CENTER);
        p11.setVerticalAlignment(TextAlignment.TOP);

        // 分项目类型表格标题要使用p11所定义的属性
        XWPFRun r11 = p11.createRun();

        // 设置字体是否加粗
        r11.setBold(true);
        r11.setFontSize(16);

        // 设置使用何种字体
        r11.setFontFamily("宋体");

        // 设置上下两行之间的间距
        r11.setTextPosition(20);        
        r11.setText(yeid+"年"+monid+"月审批、核准、备案项目分项目类型情况统计表");
        
        //设置分项目类型表格属性
        CTTblPr tblPr5 = table5.getCTTbl().getTblPr();
        tblPr5.getTblW().setType(STTblWidth.DXA);
        tblPr5.getTblW().setW(new BigInteger("9000"));

        // 设置上下左右四个方向的距离，可以将表格撑大
        table5.setCellMargins(20, 20, 30, 30);

        // 项目类型表格
        List<XWPFTableCell> tableCells5 = table5.getRow(0).getTableCells();

        //设置表格第一列的属性
        XWPFTableCell cell5 = tableCells5.get(0);
        CTTcPr cellPr5 = cell5.getCTTc().addNewTcPr(); 
        CTTblWidth cellw5 = cellPr5.addNewTcW();
        cellw5.setType(STTblWidth.DXA);
        cellPr5.addNewTcW().setW(BigInteger.valueOf(1800));
        XWPFTableCell cnt5 = tableCells5.get(1);
        XWPFTableCell cntratio5 = tableCells5.get(2);
        XWPFTableCell investtotal5 = tableCells5.get(3);
        CTTcPr cellPrinvesttotal5 = investtotal5.getCTTc().addNewTcPr(); 
        CTTblWidth cellwinvesttotal5 = cellPrinvesttotal5.addNewTcW();
        cellwinvesttotal5.setType(STTblWidth.DXA);
        cellPrinvesttotal5.addNewTcW().setW(BigInteger.valueOf(1500));
        XWPFTableCell inveatratio15 = tableCells5.get(4);
        XWPFTableCell cntrate5 = tableCells5.get(5);
        XWPFTableCell investrate5 = tableCells5.get(6);
        XWPFParagraph newPara5 = new XWPFParagraph(cell5.getCTTc().addNewP(), cell5);
        XWPFParagraph newcnt5 = new XWPFParagraph(cnt5.getCTTc().addNewP(), cnt5);
        XWPFParagraph newcntratio5 = new XWPFParagraph(cntratio5.getCTTc().addNewP(), cntratio5);
        XWPFParagraph newinvesttotal5 = new XWPFParagraph(investtotal5.getCTTc().addNewP(), investtotal5);
        XWPFParagraph newinveatratio15 = new XWPFParagraph(inveatratio15.getCTTc().addNewP(), inveatratio15);
        XWPFParagraph newcntrate5 = new XWPFParagraph(cntrate5.getCTTc().addNewP(), cntrate5);
        XWPFParagraph newinvestrate5 = new XWPFParagraph(investrate5.getCTTc().addNewP(), investrate5);
        
        //固定表头内容属性
        XWPFRun run5 = newPara5.createRun();
        XWPFRun runcnt5 = newcnt5.createRun();
        XWPFRun runcntratio5 = newcntratio5.createRun();
        XWPFRun runinvesttotal5 = newinvesttotal5.createRun();
        XWPFRun runinveatratio15 = newinveatratio15.createRun();
        XWPFRun runcntrate5 = newcntrate5.createRun();
        XWPFRun runinvestrate5 = newinvestrate5.createRun();
        /** 内容居中显示 **/
        newPara5.setAlignment(ParagraphAlignment.CENTER);
        newcnt5.setAlignment(ParagraphAlignment.CENTER);
        newcntratio5.setAlignment(ParagraphAlignment.CENTER);
        newinvesttotal5.setAlignment(ParagraphAlignment.CENTER);
        newinveatratio15.setAlignment(ParagraphAlignment.CENTER);
        newcntrate5.setAlignment(ParagraphAlignment.CENTER);
        newinvestrate5.setAlignment(ParagraphAlignment.CENTER);
        //加粗显示
        run5.setBold(true);
        runcnt5.setBold(true);
        runcntratio5.setBold(true);
        runinvesttotal5.setBold(true);
        runinveatratio15.setBold(true);
        runcntrate5.setBold(true);
        runinvestrate5.setBold(true);
        run5.setText("项目类型");
        runcnt5.setText("项目个数");
        runcntratio5.setText("项目个数同比");
        runinvesttotal5.setText("总投资（亿元）");
        runinveatratio15.setText("总投资同比");
        runcntrate5.setText("项目个数占比");
        runinvestrate5.setText("总投资占比");

        //循环遍历数据插入表格
        if(projectlist!=null){
                for(int i=0;i<projectlist.size();i++){
                	tableCells5 = table5.getRow(i+1).getTableCells();
                    if(i==0||i==1||i==6||i==7){
                        XWPFTableCell cellproject = tableCells5.get(0);
                        //设置列宽
                        CTTcPr cellPrproject = cellproject.getCTTc().addNewTcPr(); 
                        CTTblWidth cellwproject = cellPrproject.addNewTcW();
                        cellwproject.setType(STTblWidth.DXA);
                        cellPrproject.addNewTcW().setW(BigInteger.valueOf(1800));
                        XWPFTableCell cntproject = tableCells5.get(1);
                        XWPFTableCell cntratioproject = tableCells5.get(2);
                        XWPFTableCell investtotalproject = tableCells5.get(3);
                        //设置列宽
                        CTTcPr cellPrinvesttotalproject = investtotalproject.getCTTc().addNewTcPr(); 
                        CTTblWidth cellwinvesttotalproject = cellPrinvesttotalproject.addNewTcW();
                        cellwinvesttotalproject.setType(STTblWidth.DXA);
                        cellPrinvesttotalproject.addNewTcW().setW(BigInteger.valueOf(1500));
                        XWPFTableCell inveatratio1project = tableCells5.get(4);
                        XWPFTableCell cntrateproject = tableCells5.get(5);
                        XWPFTableCell investrateproject = tableCells5.get(6);
                        XWPFParagraph newParaproject = new XWPFParagraph(cellproject.getCTTc().addNewP(), cellproject);
                        XWPFParagraph newcntproject = new XWPFParagraph(cntproject.getCTTc().addNewP(), cntproject);
                        XWPFParagraph newcntratioproject = new XWPFParagraph(cntratioproject.getCTTc().addNewP(), cntratioproject);
                        XWPFParagraph newinvesttotalproject = new XWPFParagraph(investtotalproject.getCTTc().addNewP(), investtotalproject);
                        XWPFParagraph newinveatratio1project = new XWPFParagraph(inveatratio1project.getCTTc().addNewP(), inveatratio1project);
                        XWPFParagraph newcntrateproject = new XWPFParagraph(cntrateproject.getCTTc().addNewP(), cntrateproject);
                        XWPFParagraph newinvestrateproject = new XWPFParagraph(investrateproject.getCTTc().addNewP(), investrateproject);
                        
                        //固定表头内容属性
                        XWPFRun runproject = newParaproject.createRun();
                        XWPFRun runcntproject = newcntproject.createRun();
                        XWPFRun runcntratioproject = newcntratioproject.createRun();
                        XWPFRun runinvesttotalproject = newinvesttotalproject.createRun();
                        XWPFRun runinveatratio1project = newinveatratio1project.createRun();
                        XWPFRun runcntrateproject = newcntrateproject.createRun();
                        XWPFRun runinvestrateproject = newinvestrateproject.createRun();
                        /** 内容居中显示 **/
/*                        newParaproject.setAlignment(ParagraphAlignment.CENTER);
                        newcntproject.setAlignment(ParagraphAlignment.CENTER);
                        newcntratioproject.setAlignment(ParagraphAlignment.CENTER);
                        newinvesttotalproject.setAlignment(ParagraphAlignment.CENTER);
                        newinveatratio1project.setAlignment(ParagraphAlignment.CENTER);
                        newcntrateproject.setAlignment(ParagraphAlignment.CENTER);
                        newinvestrateproject.setAlignment(ParagraphAlignment.CENTER);*/
                        //加粗显示
                        runproject.setBold(true);
                        runcntproject.setBold(true);
                        runcntratioproject.setBold(true);
                        runinvesttotalproject.setBold(true);
                        runinveatratio1project.setBold(true);
                        runcntrateproject.setBold(true);
                        runinvestrateproject.setBold(true);
    	                runproject.setText(projectlist.get(i).get("ITEM_VALUE").toString());
    	                runcntproject.setText(projectlist.get(i).get("cnt").toString());
    	                runcntratioproject.setText(projectlist.get(i).get("cntratio").toString()+"%");
    	                runinvesttotalproject.setText(projectlist.get(i).get("investtotal").toString());
    	                runinveatratio1project.setText(projectlist.get(i).get("inveatratio").toString()+"%");
    	                runcntrateproject.setText(projectlist.get(i).get("cntrate").toString()+"%");
    	                runinvestrateproject.setText(projectlist.get(i).get("investrate").toString()+"%");
                    }
                    else{
                        XWPFTableCell cellproject = tableCells5.get(0);
                        //设置列宽
                        CTTcPr cellPrproject = cellproject.getCTTc().addNewTcPr(); 
                        CTTblWidth cellwproject = cellPrproject.addNewTcW();
                        cellwproject.setType(STTblWidth.DXA);
                        cellPrproject.addNewTcW().setW(BigInteger.valueOf(1800));
                        tableCells5 = table5.getRow(i+1).getTableCells();
                        tableCells5.get(0).setText(projectlist.get(i).get("ITEM_VALUE").toString());
                        tableCells5.get(1).setText(projectlist.get(i).get("cnt").toString());
                        tableCells5.get(2).setText(projectlist.get(i).get("cntratio").toString()+"%");
                        XWPFTableCell investtotalproject = tableCells5.get(3);
                        //设置列宽
                        CTTcPr cellPrinvesttotalproject = investtotalproject.getCTTc().addNewTcPr(); 
                        CTTblWidth cellwinvesttotalproject = cellPrinvesttotalproject.addNewTcW();
                        cellwinvesttotalproject.setType(STTblWidth.DXA);
                        cellPrinvesttotalproject.addNewTcW().setW(BigInteger.valueOf(1500));
                        tableCells5.get(3).setText(projectlist.get(i).get("investtotal").toString());
                        tableCells5.get(4).setText(projectlist.get(i).get("inveatratio").toString()+"%");
                        tableCells5.get(5).setText(projectlist.get(i).get("cntrate").toString()+"%");
                        tableCells5.get(6).setText(projectlist.get(i).get("investrate").toString()+"%"); 	
                    }
                }
                
        }

        // 设置分投资类型表格标题字体对齐方式
        p12.setAlignment(ParagraphAlignment.CENTER);
        p12.setVerticalAlignment(TextAlignment.TOP);

        // 分投资类型表格标题要使用p12所定义的属性
        XWPFRun r12 = p12.createRun();

        // 设置字体是否加粗
        r12.setBold(true);
        r12.setFontSize(16);

        // 设置使用何种字体
        r12.setFontFamily("宋体");

        // 设置上下两行之间的间距
        r12.setTextPosition(20);        
        r12.setText(yeid+"年"+monid+"月审批、核准、备案项目分投资类型情况统计表");
        
        //设置分投资类型表格属性
        CTTblPr tblPr6 = table6.getCTTbl().getTblPr();
        tblPr6.getTblW().setType(STTblWidth.DXA);
        tblPr6.getTblW().setW(new BigInteger("9000"));

        // 设置上下左右四个方向的距离，可以将表格撑大
        table6.setCellMargins(20, 20, 30, 30);

        // 投资类型表格
        List<XWPFTableCell> tableCells6 = table6.getRow(0).getTableCells();

        //设置表格第一列的属性
        XWPFTableCell cell6 = tableCells6.get(0);
        CTTcPr cellPr6 = cell6.getCTTc().addNewTcPr(); 
        CTTblWidth cellw6 = cellPr6.addNewTcW();
        cellw6.setType(STTblWidth.DXA);
        cellPr6.addNewTcW().setW(BigInteger.valueOf(1800));
        XWPFTableCell cnt6 = tableCells6.get(1);
        XWPFTableCell cntratio6 = tableCells6.get(2);
        XWPFTableCell investtotal6 = tableCells6.get(3);
        CTTcPr cellPrinvesttotal6 = investtotal6.getCTTc().addNewTcPr(); 
        CTTblWidth cellwinvesttotal6 = cellPrinvesttotal6.addNewTcW();
        cellwinvesttotal6.setType(STTblWidth.DXA);
        cellPrinvesttotal6.addNewTcW().setW(BigInteger.valueOf(1500));
        XWPFTableCell inveatratio16 = tableCells6.get(4);
        XWPFTableCell cntrate6 = tableCells6.get(5);
        XWPFTableCell investrate6 = tableCells6.get(6);
        XWPFParagraph newPara6 = new XWPFParagraph(cell6.getCTTc().addNewP(), cell6);
        XWPFParagraph newcnt6 = new XWPFParagraph(cnt6.getCTTc().addNewP(), cnt6);
        XWPFParagraph newcntratio6 = new XWPFParagraph(cntratio6.getCTTc().addNewP(), cntratio6);
        XWPFParagraph newinvesttotal6 = new XWPFParagraph(investtotal6.getCTTc().addNewP(), investtotal6);
        XWPFParagraph newinveatratio16 = new XWPFParagraph(inveatratio16.getCTTc().addNewP(), inveatratio16);
        XWPFParagraph newcntrate6 = new XWPFParagraph(cntrate6.getCTTc().addNewP(), cntrate6);
        XWPFParagraph newinvestrate6 = new XWPFParagraph(investrate6.getCTTc().addNewP(), investrate6);
        
        //固定表头内容属性
        XWPFRun run6 = newPara6.createRun();
        XWPFRun runcnt6 = newcnt6.createRun();
        XWPFRun runcntratio6 = newcntratio6.createRun();
        XWPFRun runinvesttotal6 = newinvesttotal6.createRun();
        XWPFRun runinveatratio16 = newinveatratio16.createRun();
        XWPFRun runcntrate6 = newcntrate6.createRun();
        XWPFRun runinvestrate6 = newinvestrate6.createRun();
        /** 内容居中显示 **/
        newPara6.setAlignment(ParagraphAlignment.CENTER);
        newcnt6.setAlignment(ParagraphAlignment.CENTER);
        newcntratio6.setAlignment(ParagraphAlignment.CENTER);
        newinvesttotal6.setAlignment(ParagraphAlignment.CENTER);
        newinveatratio16.setAlignment(ParagraphAlignment.CENTER);
        newcntrate6.setAlignment(ParagraphAlignment.CENTER);
        newinvestrate6.setAlignment(ParagraphAlignment.CENTER);
        //加粗显示
        run6.setBold(true);
        runcnt6.setBold(true);
        runcntratio6.setBold(true);
        runinvesttotal6.setBold(true);
        runinveatratio16.setBold(true);
        runcntrate6.setBold(true);
        runinvestrate6.setBold(true);
        run6.setText("投资类型");
        runcnt6.setText("项目个数");
        runcntratio6.setText("项目个数同比");
        runinvesttotal6.setText("总投资（亿元）");
        runinveatratio16.setText("总投资同比");
        runcntrate6.setText("项目个数占比");
        runinvestrate6.setText("总投资占比");
        
        //循环遍历数据插入表格
        if(Investlist!=null){
                for(int i=0;i<Investlist.size();i++){
                	tableCells6 = table6.getRow(i+1).getTableCells();
                    if(i==0){
                        XWPFTableCell cellinvest = tableCells6.get(0);
                        //设置列宽
                        CTTcPr cellPrinvest = cellinvest.getCTTc().addNewTcPr(); 
                        CTTblWidth cellwinvest = cellPrinvest.addNewTcW();
                        cellwinvest.setType(STTblWidth.DXA);
                        cellPrinvest.addNewTcW().setW(BigInteger.valueOf(1800));
                        XWPFTableCell cntinvest = tableCells6.get(1);
                        XWPFTableCell cntratioinvest = tableCells6.get(2);
                        XWPFTableCell investtotalinvest = tableCells6.get(3);
                        //设置列宽
                        CTTcPr cellPrinvesttotalinvest = investtotalinvest.getCTTc().addNewTcPr(); 
                        CTTblWidth cellwinvesttotalinvest = cellPrinvesttotalinvest.addNewTcW();
                        cellwinvesttotalinvest.setType(STTblWidth.DXA);
                        cellPrinvesttotalinvest.addNewTcW().setW(BigInteger.valueOf(1500));
                        XWPFTableCell inveatratio1invest = tableCells6.get(4);
                        XWPFTableCell cntrateinvest = tableCells6.get(5);
                        XWPFTableCell investrateinvest = tableCells6.get(6);
                        XWPFParagraph newParainvest = new XWPFParagraph(cellinvest.getCTTc().addNewP(), cellinvest);
                        XWPFParagraph newcntinvest = new XWPFParagraph(cntinvest.getCTTc().addNewP(), cntinvest);
                        XWPFParagraph newcntratioinvest = new XWPFParagraph(cntratioinvest.getCTTc().addNewP(), cntratioinvest);
                        XWPFParagraph newinvesttotalinvest = new XWPFParagraph(investtotalinvest.getCTTc().addNewP(), investtotalinvest);
                        XWPFParagraph newinveatratio1invest = new XWPFParagraph(inveatratio1invest.getCTTc().addNewP(), inveatratio1invest);
                        XWPFParagraph newcntrateinvest = new XWPFParagraph(cntrateinvest.getCTTc().addNewP(), cntrateinvest);
                        XWPFParagraph newinvestrateinvest = new XWPFParagraph(investrateinvest.getCTTc().addNewP(), investrateinvest);
                        
                        //固定表头内容属性
                        XWPFRun runinvest = newParainvest.createRun();
                        XWPFRun runcntinvest = newcntinvest.createRun();
                        XWPFRun runcntratioinvest = newcntratioinvest.createRun();
                        XWPFRun runinvesttotalinvest = newinvesttotalinvest.createRun();
                        XWPFRun runinveatratio1invest = newinveatratio1invest.createRun();
                        XWPFRun runcntrateinvest = newcntrateinvest.createRun();
                        XWPFRun runinvestrateinvest = newinvestrateinvest.createRun();
                        /** 内容居中显示 **/
/*                        newParainvest.setAlignment(ParagraphAlignment.CENTER);
                        newcntinvest.setAlignment(ParagraphAlignment.CENTER);
                        newcntratioinvest.setAlignment(ParagraphAlignment.CENTER);
                        newinvesttotalinvest.setAlignment(ParagraphAlignment.CENTER);
                        newinveatratio1invest.setAlignment(ParagraphAlignment.CENTER);
                        newcntrateinvest.setAlignment(ParagraphAlignment.CENTER);
                        newinvestrateinvest.setAlignment(ParagraphAlignment.CENTER);*/
                        //加粗显示
                        runinvest.setBold(true);
                        runcntinvest.setBold(true);
                        runcntratioinvest.setBold(true);
                        runinvesttotalinvest.setBold(true);
                        runinveatratio1invest.setBold(true);
                        runcntrateinvest.setBold(true);
                        runinvestrateinvest.setBold(true);
    	                runinvest.setText(Investlist.get(i).get("TZLX").toString());
    	                runcntinvest.setText(Investlist.get(i).get("cnt").toString());
    	                runcntratioinvest.setText(Investlist.get(i).get("cntratio").toString()+"%");
    	                runinvesttotalinvest.setText(Investlist.get(i).get("investtotal").toString());
    	                runinveatratio1invest.setText(Investlist.get(i).get("inveatratio").toString()+"%");
    	                runcntrateinvest.setText(Investlist.get(i).get("cntrate").toString()+"%");
    	                runinvestrateinvest.setText(Investlist.get(i).get("investrate").toString()+"%");
                    }
                    else{              
                        XWPFTableCell cellinvest = tableCells6.get(0);
                        //设置列宽
                        CTTcPr cellPrinvest = cellinvest.getCTTc().addNewTcPr(); 
                        CTTblWidth cellwinvest = cellPrinvest.addNewTcW();
                        cellwinvest.setType(STTblWidth.DXA);
                        cellPrinvest.addNewTcW().setW(BigInteger.valueOf(1800));
                        tableCells6.get(0).setText(Investlist.get(i).get("TZLX").toString());
                        tableCells6.get(1).setText(Investlist.get(i).get("cnt").toString());
                        tableCells6.get(2).setText(Investlist.get(i).get("cntratio").toString()+"%");
                        XWPFTableCell investtotalinvest = tableCells6.get(3);
                        //设置列宽
                        CTTcPr cellPrinvesttotalinvest = investtotalinvest.getCTTc().addNewTcPr(); 
                        CTTblWidth cellwinvesttotalinvest = cellPrinvesttotalinvest.addNewTcW();
                        cellwinvesttotalinvest.setType(STTblWidth.DXA);
                        cellPrinvesttotalinvest.addNewTcW().setW(BigInteger.valueOf(1500));
                        tableCells6.get(3).setText(Investlist.get(i).get("investtotal").toString());
                        tableCells6.get(4).setText(Investlist.get(i).get("inveatratio").toString()+"%");
                        tableCells6.get(5).setText(Investlist.get(i).get("cntrate").toString()+"%");
                        tableCells6.get(6).setText(Investlist.get(i).get("investrate").toString()+"%");   	
                    }
                }
                
        }

        // 设置分投资规模表格标题字体对齐方式
        p13.setAlignment(ParagraphAlignment.CENTER);
        p13.setVerticalAlignment(TextAlignment.TOP);

        // 分投资规模表格标题要使用p13所定义的属性
        XWPFRun r13 = p13.createRun();

        // 设置字体是否加粗
        r13.setBold(true);
        r13.setFontSize(16);

        // 设置使用何种字体
        r13.setFontFamily("宋体");

        // 设置上下两行之间的间距
        r13.setTextPosition(20);        
        r13.setText(yeid+"年"+monid+"月审批、核准、备案项目分投资规模情况统计表");
        
        //设置分投资规模表格属性
        CTTblPr tblPr7 = table7.getCTTbl().getTblPr();
        tblPr7.getTblW().setType(STTblWidth.DXA);
        tblPr7.getTblW().setW(new BigInteger("9000"));

        // 设置上下左右四个方向的距离，可以将表格撑大
        table7.setCellMargins(20, 20, 30, 30);

        // 投资规模表格
        List<XWPFTableCell> tableCells7 = table7.getRow(0).getTableCells();

        //设置表格第一列的属性
        XWPFTableCell cell7 = tableCells7.get(0);
        CTTcPr cellPr7 = cell7.getCTTc().addNewTcPr(); 
        CTTblWidth cellw7 = cellPr7.addNewTcW();
        cellw7.setType(STTblWidth.DXA);
        cellPr7.addNewTcW().setW(BigInteger.valueOf(1800));
        XWPFTableCell cnt7 = tableCells7.get(1);
        XWPFTableCell cntratio7 = tableCells7.get(2);
        XWPFTableCell investtotal7 = tableCells7.get(3);
        CTTcPr cellPrinvesttotal7 = investtotal7.getCTTc().addNewTcPr(); 
        CTTblWidth cellwinvesttotal7 = cellPrinvesttotal7.addNewTcW();
        cellwinvesttotal7.setType(STTblWidth.DXA);
        cellPrinvesttotal7.addNewTcW().setW(BigInteger.valueOf(1500));
        XWPFTableCell inveatratio17 = tableCells7.get(4);
        XWPFTableCell cntrate7 = tableCells7.get(5);
        XWPFTableCell investrate7 = tableCells7.get(6);
        XWPFParagraph newPara7 = new XWPFParagraph(cell7.getCTTc().addNewP(), cell7);
        XWPFParagraph newcnt7 = new XWPFParagraph(cnt7.getCTTc().addNewP(), cnt7);
        XWPFParagraph newcntratio7 = new XWPFParagraph(cntratio7.getCTTc().addNewP(), cntratio7);
        XWPFParagraph newinvesttotal7 = new XWPFParagraph(investtotal7.getCTTc().addNewP(), investtotal7);
        XWPFParagraph newinveatratio17 = new XWPFParagraph(inveatratio17.getCTTc().addNewP(), inveatratio17);
        XWPFParagraph newcntrate7 = new XWPFParagraph(cntrate7.getCTTc().addNewP(), cntrate7);
        XWPFParagraph newinvestrate7 = new XWPFParagraph(investrate7.getCTTc().addNewP(), investrate7);
        
        //固定表头内容属性
        XWPFRun run7 = newPara7.createRun();
        XWPFRun runcnt7 = newcnt7.createRun();
        XWPFRun runcntratio7 = newcntratio7.createRun();
        XWPFRun runinvesttotal7 = newinvesttotal7.createRun();
        XWPFRun runinveatratio17 = newinveatratio17.createRun();
        XWPFRun runcntrate7 = newcntrate7.createRun();
        XWPFRun runinvestrate7 = newinvestrate7.createRun();
        /** 内容居中显示 **/
        newPara7.setAlignment(ParagraphAlignment.CENTER);
        newcnt7.setAlignment(ParagraphAlignment.CENTER);
        newcntratio7.setAlignment(ParagraphAlignment.CENTER);
        newinvesttotal7.setAlignment(ParagraphAlignment.CENTER);
        newinveatratio17.setAlignment(ParagraphAlignment.CENTER);
        newcntrate7.setAlignment(ParagraphAlignment.CENTER);
        newinvestrate7.setAlignment(ParagraphAlignment.CENTER);
        //加粗显示
        run7.setBold(true);
        runcnt7.setBold(true);
        runcntratio7.setBold(true);
        runinvesttotal7.setBold(true);
        runinveatratio17.setBold(true);
        runcntrate7.setBold(true);
        runinvestrate7.setBold(true);
        run7.setText("投资规模");
        runcnt7.setText("项目个数");
        runcntratio7.setText("项目个数同比");
        runinvesttotal7.setText("总投资（亿元）");
        runinveatratio17.setText("总投资同比");
        runcntrate7.setText("项目个数占比");
        runinvestrate7.setText("总投资占比");

        //循环遍历数据插入表格
        if(scalelist!=null){
                for(int i=0;i<scalelist.size();i++){
                tableCells7 = table7.getRow(i+1).getTableCells();
                if(i==0){
                    XWPFTableCell cellscale = tableCells7.get(0);
                    //设置列宽
                    CTTcPr cellPrscale  = cellscale .getCTTc().addNewTcPr(); 
                    CTTblWidth cellwscale  = cellPrscale .addNewTcW();
                    cellwscale .setType(STTblWidth.DXA);
                    cellPrscale .addNewTcW().setW(BigInteger.valueOf(1800));
                    XWPFTableCell cntscale = tableCells7.get(1);
                    XWPFTableCell cntratioscale = tableCells7.get(2);
                    XWPFTableCell investtotalscale = tableCells7.get(3);
                    //设置列宽
                    CTTcPr cellPrinvesttotalscale = investtotalscale.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwinvesttotalscale = cellPrinvesttotalscale.addNewTcW();
                    cellwinvesttotalscale.setType(STTblWidth.DXA);
                    cellPrinvesttotalscale.addNewTcW().setW(BigInteger.valueOf(1500));
                    XWPFTableCell inveatratio1scale = tableCells7.get(4);
                    XWPFTableCell cntratescale = tableCells7.get(5);
                    XWPFTableCell investratescale = tableCells7.get(6);
                    XWPFParagraph newParascale = new XWPFParagraph(cellscale.getCTTc().addNewP(), cellscale);
                    XWPFParagraph newcntscale = new XWPFParagraph(cntscale.getCTTc().addNewP(), cntscale);
                    XWPFParagraph newcntratioscale = new XWPFParagraph(cntratioscale.getCTTc().addNewP(), cntratioscale);
                    XWPFParagraph newinvesttotalscale = new XWPFParagraph(investtotalscale.getCTTc().addNewP(), investtotalscale);
                    XWPFParagraph newinveatratio1scale = new XWPFParagraph(inveatratio1scale.getCTTc().addNewP(), inveatratio1scale);
                    XWPFParagraph newcntratescale = new XWPFParagraph(cntratescale.getCTTc().addNewP(), cntratescale);
                    XWPFParagraph newinvestratescale = new XWPFParagraph(investratescale.getCTTc().addNewP(), investratescale);
                    
                    //固定表头内容属性
                    XWPFRun runscale = newParascale.createRun();
                    XWPFRun runcntscale = newcntscale.createRun();
                    XWPFRun runcntratioscale = newcntratioscale.createRun();
                    XWPFRun runinvesttotalscale = newinvesttotalscale.createRun();
                    XWPFRun runinveatratio1scale = newinveatratio1scale.createRun();
                    XWPFRun runcntratescale = newcntratescale.createRun();
                    XWPFRun runinvestratescale = newinvestratescale.createRun();
                    /** 内容居中显示 **/
/*                    newParascale.setAlignment(ParagraphAlignment.CENTER);
                    newcntscale.setAlignment(ParagraphAlignment.CENTER);
                    newcntratioscale.setAlignment(ParagraphAlignment.CENTER);
                    newinvesttotalscale.setAlignment(ParagraphAlignment.CENTER);
                    newinveatratio1scale.setAlignment(ParagraphAlignment.CENTER);
                    newcntratescale.setAlignment(ParagraphAlignment.CENTER);
                    newinvestratescale.setAlignment(ParagraphAlignment.CENTER);*/
                    //加粗显示
                    runscale.setBold(true);
                    runcntscale.setBold(true);
                    runcntratioscale.setBold(true);
                    runinvesttotalscale.setBold(true);
                    runinveatratio1scale.setBold(true);
                    runcntratescale.setBold(true);
                    runinvestratescale.setBold(true);
	                runscale.setText(scalelist.get(i).get("TZGM").toString());
	                runcntscale.setText(scalelist.get(i).get("cnt").toString());
	                runcntratioscale.setText(scalelist.get(i).get("cntratio").toString()+"%");
	                runinvesttotalscale.setText(scalelist.get(i).get("investtotal").toString());
	                runinveatratio1scale.setText(scalelist.get(i).get("inveatratio").toString()+"%");
	                runcntratescale.setText(scalelist.get(i).get("cntrate").toString()+"%");
	                runinvestratescale.setText(scalelist.get(i).get("investrate").toString()+"%");
                }
                else{
                    XWPFTableCell cellscale = tableCells7.get(0);
                    //设置列宽
                    CTTcPr cellPrscale  = cellscale .getCTTc().addNewTcPr(); 
                    CTTblWidth cellwscale  = cellPrscale .addNewTcW();
                    cellwscale .setType(STTblWidth.DXA);
                    cellPrscale .addNewTcW().setW(BigInteger.valueOf(1800));
                    tableCells7.get(0).setText(scalelist.get(i).get("TZGM").toString());
                    tableCells7.get(1).setText(scalelist.get(i).get("cnt").toString());
                    tableCells7.get(2).setText(scalelist.get(i).get("cntratio").toString()+"%");
                    XWPFTableCell investtotalscale = tableCells7.get(3);
                    //设置列宽
                    CTTcPr cellPrinvesttotalscale = investtotalscale.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwinvesttotalscale = cellPrinvesttotalscale.addNewTcW();
                    cellwinvesttotalscale.setType(STTblWidth.DXA);
                    cellPrinvesttotalscale.addNewTcW().setW(BigInteger.valueOf(1500));
                    tableCells7.get(3).setText(scalelist.get(i).get("investtotal").toString());
                    tableCells7.get(4).setText(scalelist.get(i).get("inveatratio").toString()+"%");
                    tableCells7.get(5).setText(scalelist.get(i).get("cntrate").toString()+"%");
                    tableCells7.get(6).setText(scalelist.get(i).get("investrate").toString()+"%");     	
                }
                }
                
        }

        // 设置分开工年份表格标题字体对齐方式
        p14.setAlignment(ParagraphAlignment.CENTER);
        p14.setVerticalAlignment(TextAlignment.TOP);

        // 分开工年份表格标题要使用p14所定义的属性
        XWPFRun r14 = p14.createRun();

        // 设置字体是否加粗
        r14.setBold(true);
        r14.setFontSize(15);

        // 设置使用何种字体
        r14.setFontFamily("宋体");

        // 设置上下两行之间的间距
        r14.setTextPosition(20);        
        r14.setText(yeid+"年"+monid+"月审批、核准、备案项目预计开工年份情况统计表");
        
        //设置分投资规模表格属性
        CTTblPr tblPr8 = table8.getCTTbl().getTblPr();
        tblPr8.getTblW().setType(STTblWidth.DXA);
        tblPr8.getTblW().setW(new BigInteger("9000"));

        // 设置上下左右四个方向的距离，可以将表格撑大
        table8.setCellMargins(20, 20, 30, 30);

        // 投资规模表格
        List<XWPFTableCell> tableCells8 = table8.getRow(0).getTableCells();

        //设置表格第一列的属性
        XWPFTableCell cell8 = tableCells8.get(0);
        CTTcPr cellPr8 = cell8.getCTTc().addNewTcPr(); 
        CTTblWidth cellw8 = cellPr8.addNewTcW();
        cellw8.setType(STTblWidth.DXA);
        cellPr8.addNewTcW().setW(BigInteger.valueOf(1800));
        XWPFTableCell cnt8 = tableCells8.get(1);
        XWPFTableCell cntratio8 = tableCells8.get(2);
        XWPFTableCell investtotal8 = tableCells8.get(3);
        CTTcPr cellPrinvesttotal8 = investtotal8.getCTTc().addNewTcPr(); 
        CTTblWidth cellwinvesttotal8 = cellPrinvesttotal8.addNewTcW();
        cellwinvesttotal8.setType(STTblWidth.DXA);
        cellPrinvesttotal8.addNewTcW().setW(BigInteger.valueOf(1500));
        XWPFTableCell inveatratio18 = tableCells8.get(4);
        XWPFTableCell cntrate8 = tableCells8.get(5);
        XWPFTableCell investrate8 = tableCells8.get(6);
        XWPFParagraph newPara8 = new XWPFParagraph(cell8.getCTTc().addNewP(), cell8);
        XWPFParagraph newcnt8 = new XWPFParagraph(cnt8.getCTTc().addNewP(), cnt8);
        XWPFParagraph newcntratio8 = new XWPFParagraph(cntratio8.getCTTc().addNewP(), cntratio8);
        XWPFParagraph newinvesttotal8 = new XWPFParagraph(investtotal8.getCTTc().addNewP(), investtotal8);
        XWPFParagraph newinveatratio18 = new XWPFParagraph(inveatratio18.getCTTc().addNewP(), inveatratio18);
        XWPFParagraph newcntrate8 = new XWPFParagraph(cntrate8.getCTTc().addNewP(), cntrate8);
        XWPFParagraph newinvestrate8 = new XWPFParagraph(investrate8.getCTTc().addNewP(), investrate8);
        
        //固定表头内容属性
        XWPFRun run8 = newPara8.createRun();
        XWPFRun runcnt8 = newcnt8.createRun();
        XWPFRun runcntratio8 = newcntratio8.createRun();
        XWPFRun runinvesttotal8 = newinvesttotal8.createRun();
        XWPFRun runinveatratio18 = newinveatratio18.createRun();
        XWPFRun runcntrate8 = newcntrate8.createRun();
        XWPFRun runinvestrate8 = newinvestrate8.createRun();
        /** 内容居中显示 **/
        newPara8.setAlignment(ParagraphAlignment.CENTER);
        newcnt8.setAlignment(ParagraphAlignment.CENTER);
        newcntratio8.setAlignment(ParagraphAlignment.CENTER);
        newinvesttotal8.setAlignment(ParagraphAlignment.CENTER);
        newinveatratio18.setAlignment(ParagraphAlignment.CENTER);
        newcntrate8.setAlignment(ParagraphAlignment.CENTER);
        newinvestrate8.setAlignment(ParagraphAlignment.CENTER);
        //加粗显示
        run8.setBold(true);
        runcnt8.setBold(true);
        runcntratio8.setBold(true);
        runinvesttotal8.setBold(true);
        runinveatratio18.setBold(true);
        runcntrate8.setBold(true);
        runinvestrate8.setBold(true);
        run8.setText("开工年份");
        runcnt8.setText("项目个数");
        runcntratio8.setText("项目个数同比");
        runinvesttotal8.setText("总投资（亿元）");
        runinveatratio18.setText("总投资同比");
        runcntrate8.setText("项目个数占比");
        runinvestrate8.setText("总投资占比");

        //循环遍历数据插入表格
        if(timelist!=null){
                for(int i=0;i<timelist.size();i++){
                tableCells8 = table8.getRow(i+1).getTableCells();
                if(i==0){
                    XWPFTableCell celltime = tableCells8.get(0);
                    //设置列宽
                    CTTcPr cellPrtime  = celltime .getCTTc().addNewTcPr(); 
                    CTTblWidth cellwtime  = cellPrtime .addNewTcW();
                    cellwtime .setType(STTblWidth.DXA);
                    cellPrtime .addNewTcW().setW(BigInteger.valueOf(1800));
                    XWPFTableCell cnttime = tableCells8.get(1);
                    XWPFTableCell cntratiotime = tableCells8.get(2);
                    XWPFTableCell investtotaltime = tableCells8.get(3);
                    //设置列宽
                    CTTcPr cellPrinvesttotaltime = investtotaltime.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwinvesttotaltime = cellPrinvesttotaltime.addNewTcW();
                    cellwinvesttotaltime.setType(STTblWidth.DXA);
                    cellPrinvesttotaltime.addNewTcW().setW(BigInteger.valueOf(1500));
                    XWPFTableCell inveatratio1time = tableCells8.get(4);
                    XWPFTableCell cntratetime = tableCells8.get(5);
                    XWPFTableCell investratetime = tableCells8.get(6);
                    XWPFParagraph newParatime = new XWPFParagraph(celltime.getCTTc().addNewP(), celltime);
                    XWPFParagraph newcnttime = new XWPFParagraph(cnttime.getCTTc().addNewP(), cnttime);
                    XWPFParagraph newcntratiotime = new XWPFParagraph(cntratiotime.getCTTc().addNewP(), cntratiotime);
                    XWPFParagraph newinvesttotaltime = new XWPFParagraph(investtotaltime.getCTTc().addNewP(), investtotaltime);
                    XWPFParagraph newinveatratio1time = new XWPFParagraph(inveatratio1time.getCTTc().addNewP(), inveatratio1time);
                    XWPFParagraph newcntratetime = new XWPFParagraph(cntratetime.getCTTc().addNewP(), cntratetime);
                    XWPFParagraph newinvestratetime = new XWPFParagraph(investratetime.getCTTc().addNewP(), investratetime);
                    
                    //固定表头内容属性
                    XWPFRun runtime = newParatime.createRun();
                    XWPFRun runcnttime = newcnttime.createRun();
                    XWPFRun runcntratiotime = newcntratiotime.createRun();
                    XWPFRun runinvesttotaltime = newinvesttotaltime.createRun();
                    XWPFRun runinveatratio1time = newinveatratio1time.createRun();
                    XWPFRun runcntratetime = newcntratetime.createRun();
                    XWPFRun runinvestratetime = newinvestratetime.createRun();
                    /** 内容居中显示 **/
/*                    newParatime.setAlignment(ParagraphAlignment.CENTER);
                    newcnttime.setAlignment(ParagraphAlignment.CENTER);
                    newcntratiotime.setAlignment(ParagraphAlignment.CENTER);
                    newinvesttotaltime.setAlignment(ParagraphAlignment.CENTER);
                    newinveatratio1time.setAlignment(ParagraphAlignment.CENTER);
                    newcntratetime.setAlignment(ParagraphAlignment.CENTER);
                    newinvestratetime.setAlignment(ParagraphAlignment.CENTER);*/
                    //加粗显示
                    runtime.setBold(true);
                    runcnttime.setBold(true);
                    runcntratiotime.setBold(true);
                    runinvesttotaltime.setBold(true);
                    runinveatratio1time.setBold(true);
                    runcntratetime.setBold(true);
                    runinvestratetime.setBold(true);
	                runtime.setText(timelist.get(i).get("X11").toString());
	                runcnttime.setText(timelist.get(i).get("cnt").toString());
	                runcntratiotime.setText(timelist.get(i).get("cntratio").toString()+"%");
	                runinvesttotaltime.setText(timelist.get(i).get("investtotal").toString());
	                runinveatratio1time.setText(timelist.get(i).get("inveatratio").toString()+"%");
	                runcntratetime.setText(timelist.get(i).get("cntrate").toString()+"%");
	                runinvestratetime.setText(timelist.get(i).get("investrate").toString()+"%");
                }
                else{
                    XWPFTableCell celltime = tableCells8.get(0);
                    //设置列宽
                    CTTcPr cellPrtime  = celltime .getCTTc().addNewTcPr(); 
                    CTTblWidth cellwtime  = cellPrtime .addNewTcW();
                    cellwtime .setType(STTblWidth.DXA);
                    cellPrtime .addNewTcW().setW(BigInteger.valueOf(1800));
                    tableCells8.get(0).setText(timelist.get(i).get("X11").toString());
                    tableCells8.get(1).setText(timelist.get(i).get("cnt").toString());
                    tableCells8.get(2).setText(timelist.get(i).get("cntratio").toString()+"%");
                    XWPFTableCell investtotaltime = tableCells8.get(3);
                    //设置列宽
                    CTTcPr cellPrinvesttotaltime = investtotaltime.getCTTc().addNewTcPr(); 
                    CTTblWidth cellwinvesttotaltime = cellPrinvesttotaltime.addNewTcW();
                    cellwinvesttotaltime.setType(STTblWidth.DXA);
                    cellPrinvesttotaltime.addNewTcW().setW(BigInteger.valueOf(1500));
                    tableCells8.get(3).setText(timelist.get(i).get("investtotal").toString());
                    tableCells8.get(4).setText(timelist.get(i).get("inveatratio").toString()+"%");
                    tableCells8.get(5).setText(timelist.get(i).get("cntrate").toString()+"%");
                    tableCells8.get(6).setText(timelist.get(i).get("investrate").toString()+"%");       	
                }
               }
                
        }
        
        // 设置首页字体对齐方式
        phomepage1.setAlignment(ParagraphAlignment.CENTER);
        phomepage2.setAlignment(ParagraphAlignment.CENTER);
        phomepage3.setAlignment(ParagraphAlignment.CENTER);
        //设置目录字体对齐方式
        pcatalog1.setAlignment(ParagraphAlignment.CENTER);
        pcatalog2.setAlignment(ParagraphAlignment.LEFT);
        pcatalog3.setAlignment(ParagraphAlignment.LEFT);
        pcatalog4.setAlignment(ParagraphAlignment.LEFT);
        pcatalog5.setAlignment(ParagraphAlignment.LEFT);
        pcatalog6.setAlignment(ParagraphAlignment.LEFT);
        pcatalog7.setAlignment(ParagraphAlignment.LEFT);
        pcatalog8.setAlignment(ParagraphAlignment.LEFT);
        //设置第三页字体对齐方式
        p1.setAlignment(ParagraphAlignment.CENTER);
        p1.setVerticalAlignment(TextAlignment.TOP);
        ptzlx.setAlignment(ParagraphAlignment.LEFT);
        pxmlx.setAlignment(ParagraphAlignment.LEFT);
        pcy.setAlignment(ParagraphAlignment.LEFT);
        pdss.setAlignment(ParagraphAlignment.LEFT);
        p2.setIndentationFirstLine(360);
        p3.setIndentationFirstLine(360);
        p4.setIndentationFirstLine(360);
        p5.setIndentationFirstLine(360);
        p6.setIndentationFirstLine(360);

        // 第一页所定义的属性
        XWPFRun rhomepage1 = phomepage1.createRun();
        XWPFRun rhomepage2 = phomepage2.createRun();
        XWPFRun rhomepage3 = phomepage3.createRun();
        //目录
        XWPFRun rcatalog1 = pcatalog1.createRun();
        XWPFRun rcatalog2 = pcatalog1.createRun();
        XWPFRun rcatalog3 = pcatalog1.createRun();
        XWPFRun rcatalog4 = pcatalog1.createRun();
        XWPFRun rcatalog5 = pcatalog1.createRun();
        XWPFRun rcatalog6 = pcatalog1.createRun();
        XWPFRun rcatalog7 = pcatalog1.createRun();
        XWPFRun rcatalog8 = pcatalog1.createRun();
        //第三页
        XWPFRun r1 = p1.createRun();
        XWPFRun r2 = p2.createRun();
        XWPFRun rtzlx = ptzlx.createRun();
        XWPFRun r3 = p3.createRun();
        XWPFRun rxmlx = pxmlx.createRun();
        XWPFRun r4 = p4.createRun();
        XWPFRun rcy = pcy.createRun();
        XWPFRun r5 = p5.createRun();
        XWPFRun rdss = pdss.createRun();
        XWPFRun r6 = p6.createRun();

        // 设置首页字体是否加粗
        rhomepage1.setBold(true);
        rhomepage1.setFontSize(33);
        rhomepage2.setFontSize(18);
        rhomepage3.setFontSize(18);
        //目录
        rcatalog1.setBold(true);
        rcatalog1.setFontSize(16);
        rcatalog2.setFontSize(16);
        rcatalog3.setFontSize(16);
        rcatalog4.setFontSize(16);
        rcatalog5.setFontSize(16);
        rcatalog6.setFontSize(16);
        rcatalog7.setFontSize(16);
        rcatalog8.setFontSize(16);
        //设置第三页字体加粗
        r1.setBold(true);
        r1.setFontSize(18);
        rtzlx.setBold(true);
        rxmlx.setBold(true);
        rcy.setBold(true);
        rdss.setBold(true);
        rtzlx.setFontSize(16);
        rxmlx.setFontSize(16);
        rcy.setFontSize(16);
        rdss.setFontSize(16);

        // 设置使用何种字体
        rhomepage1.setFontFamily("宋体");
        r1.setFontFamily("宋体");

        // 设置上下两行之间的间距
        rhomepage1.setText(yeid+"年"+monid+"月全国固定资产投资审批、核准、备案项目信息月报");
        rhomepage2.setText("发展改革委投资司");
        rhomepage3.setText(yeid+"年"+monid+"月30日");
        rhomepage3.addBreak(BreakType.PAGE);
        rcatalog1.setText("目录");
        rcatalog1.addCarriageReturn();
        rcatalog2.setText("一、简要情况 ...........................................................11");
        rcatalog2.addCarriageReturn();
        rcatalog3.setText("二、分行业情况统计表 ............................................22");
        rcatalog3.addCarriageReturn();
        rcatalog4.setText("三、分地区情况统计表 ............................................33");
        rcatalog4.addCarriageReturn();
        rcatalog5.setText("四、分产业情况统计表 ............................................44");
        rcatalog5.addCarriageReturn();
        rcatalog6.setText("五、分项目类型情况统计表  ...................................55");
        rcatalog6.addCarriageReturn();
        rcatalog7.setText("六、分投资规模情况统计表  ...................................66");
        rcatalog7.addCarriageReturn();
        rcatalog8.setText("七、分开工时间情况统计表  ...................................77");
        rcatalog8.addBreak(BreakType.PAGE);
        r1.setText(yeid+"年"+monid+"月全国审批核准备案项目简要情况分析");
        r2.setText(yeid+"年"+monid+"月,全国审批、核准和备案手续项目、计划总投资、项目数量同比增长和计划总投资同比增长分别为"
				+Investlist.get(0).get("cnt").toString()+"个，"+Investlist.get(0).get("cntratio").toString()+"%"+"，"
        		+Investlist.get(0).get("investtotal").toString()+"亿元，"+Investlist.get(0).get("inveatratio").toString()+"%"+"。");
        rtzlx.setText("一、投资类型项目数量同比情况分析");
        r3.setText(Investlist.get(1).get("TZLX").toString()+"计划投资额同比增长"+Investlist.get(1).get("inveatratio").toString()+"%，"
				+Investlist.get(2).get("TZLX").toString()+"计划投资额同比增长"+Investlist.get(2).get("inveatratio").toString()+"%。");
        rxmlx.setText("二、审批、核准、备案项目数量同比情况分析");
        r4.setText("全国各地审批项目"+projectlist.get(1).get("cnt").toString()+"个，计划投资额"+projectlist.get(1).get("investtotal").toString()+"亿元，项目个数同比增加"+projectlist.get(1).get("cntratio").toString()
        		+"%，计划投资额同比增加"+projectlist.get(1).get("inveatratio").toString()+"%；核准项目"+projectlist.get(6).get("cnt").toString()+"个，计划投资额"+projectlist.get(6).get("investtotal").toString()
        		+"亿元，同比分别增加"+projectlist.get(6).get("cntratio").toString()+"%和"+projectlist.get(6).get("inveatratio").toString()+"%；备案项目"+projectlist.get(7).get("cnt").toString()
        		+"个，计划投资额"+projectlist.get(7).get("investtotal").toString()+"亿元，项目个数同比增加"+projectlist.get(7).get("cntratio").toString()
				+"%，计划投资额同比增加"+projectlist.get(7).get("inveatratio").toString()+"%。");
        rcy.setText("三、三大产业项目数量和计划投资情况");
        r5.setText("全国"+propertylist.get(0).get("cnt").toString()+"个项目中，"+propertylist.get(1).get("CY")+propertylist.get(1).get("cnt").toString()+"个，同比增长"
				+propertylist.get(1).get("cntratio").toString()+"%，计划投资额"+propertylist.get(1).get("investtotal").toString()+"亿元，同比增加"+propertylist.get(1).get("inveatratio").toString()+"%；"
				+propertylist.get(2).get("CY")+propertylist.get(2).get("cnt").toString()+"个，同比增长"+propertylist.get(2).get("cntratio").toString()+"%"+"，计划投资额"
				+propertylist.get(2).get("investtotal").toString()+"亿元，同比增加"+propertylist.get(2).get("inveatratio").toString()+"%；"
				+propertylist.get(3).get("CY")+propertylist.get(3).get("cnt").toString()+"个，同比增长"+propertylist.get(3).get("cntratio").toString()+"%"+"，计划投资额"
				+propertylist.get(3).get("investtotal").toString()+"亿元，同比增加"+propertylist.get(3).get("inveatratio").toString()+"%。");
        rdss.setText("四、东北地区项目数量和计划投资情况");
        r6.setText("东北地区项目数量、计划投资额、项目个数同比增长、计划投资额同比增长分别为"
        		+eastlist.get(0).get("cnt").toString()+"个，"
        		+eastlist.get(0).get("investtotal").toString()+"亿元，"
        		+eastlist.get(0).get("cntratio").toString()+"%，"
        		+eastlist.get(0).get("inveatratio").toString()+"%。");
        //r6.addBreak(BreakType.PAGE);

        //FileOutputStream out;
        //在指定路径下保存文件
        FileOutputStream out=null;
        try {     
        	File file=new File(filePath);
        	if(!file.exists()){
        		file.mkdirs();    
        	}
        	out = new FileOutputStream(filePath+fileName);
            doc.write(out);           
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        finally{
        	if(null!= out){
        		try {
					out.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
        	}
        }
/*        try {
            out = new FileOutputStream("e:/Work/word2007.docx");
            // 以下代码可进行文件下载
            // response.reset();
            // response.setContentType("application/x-msdownloadoctet-stream;charset=utf-8");
            // response.setHeader("Content-Disposition",
            // "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8"));
            // OutputStream out = response.getOutputStream();
            // this.doc.write(out);
            // out.flush();

            doc.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        System.out.println("success");          
    }
   

}
