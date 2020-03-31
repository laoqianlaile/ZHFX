package com.strongit.iss.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.service.BaseService;
import com.strongit.iss.service.IQueryConditionService;

@Service//初始类
@Transactional
public class QueryConditionServiceImpl extends BaseService implements IQueryConditionService{

	/**
	 * 组装查询条件
	 * @orderBy 
	 * @param reportParamsMap
	 * @return
	 * @author wuwei
	 * @Date 2017年3月2日下午4:23:35
	 */
	@Override
	public String getIQueryCondition(Map<String, String> reportParamsMap) throws BusinessServiceException{
		StringBuilder SQL = new StringBuilder();
		if(null != reportParamsMap){
			//入库时间开始
			if(StringUtils.isNotBlank(reportParamsMap.get("storeTimeFrom"))){
				SQL.append(" AND VINF.store_time >= '"+reportParamsMap.get("storeTimeFrom")+"'");
			}
			//入库时间结束
			if(StringUtils.isNotBlank(reportParamsMap.get("storeTimeTo"))){
				SQL.append(" AND VINF.store_time <= '"+reportParamsMap.get("storeTimeTo")+"'");
			}
			//拟开工年份开始
			if(StringUtils.isNotBlank(reportParamsMap.get("startTimeFrom"))){
				SQL.append(" AND VINF.startTime >= '"+reportParamsMap.get("startTimeFrom")+"'");
			}
			//拟开工年份结束
			if(StringUtils.isNotBlank(reportParamsMap.get("startTimeTo"))){
				SQL.append(" AND VINF.startTime <= '"+reportParamsMap.get("startTimeTo")+"'");
			}
			//拟建成年份开始
			if(StringUtils.isNotBlank(reportParamsMap.get("endTimeFrom"))){
				SQL.append(" AND VINF.end_time >= '"+reportParamsMap.get("endTimeFrom")+"'");
			}
			//拟建成年份结束
			if(StringUtils.isNotBlank(reportParamsMap.get("endTimeTo"))){
				SQL.append(" AND VINF.end_time <= '"+reportParamsMap.get("endTimeTo")+"'");
			}
			//项目总投资开始
			if(StringUtils.isNotBlank(reportParamsMap.get("invTotalFrom"))){
				SQL.append(" AND VINF.investment_total >= '"+reportParamsMap.get("invTotalFrom")+"'");
			}
			//项目总投资结束
			if(StringUtils.isNotBlank(reportParamsMap.get("invTotalTo"))){
				SQL.append(" AND VINF.investment_total <= '"+reportParamsMap.get("invTotalTo")+"'");
			}
			//建设地点
			if(StringUtils.isNotBlank(reportParamsMap.get("buildPlace"))){
				SQL.append(" AND VINF.build_place in ('"+reportParamsMap.get("buildPlace").replace(", ", "','")+"')");
			}
			//所属行业
			if(StringUtils.isNotBlank(reportParamsMap.get("industry"))){
				SQL.append(" AND VINF.industry in ('"+reportParamsMap.get("industry").replace(", ","','")+"')");
			}
			//国标行业
			if(StringUtils.isNotBlank(reportParamsMap.get("gbIndustry"))){
				SQL.append(" AND VINF.gb_industry in ('"+reportParamsMap.get("gbIndustry").replace(", ","','")+"')");
			}
			//是否专项建设基金
			if(StringUtils.isNotBlank(reportParamsMap.get("isFunds"))){
				SQL.append(" AND VINF.isFunds = '"+reportParamsMap.get("isFunds")+"'");
			}
			//政府投资方向
			if(StringUtils.isNotBlank(reportParamsMap.get("govInvest"))){
				SQL.append(" AND VINF.government_invest_direction in ('"+reportParamsMap.get("govInvest").replace(", ","','")+"')");
			}
			//是否PPP
			if(StringUtils.isNotBlank(reportParamsMap.get("isPPP"))){
				SQL.append(" AND VINF.isPPP = '"+reportParamsMap.get("isPPP")+"'");
			}
			//申报日期开始
			if(StringUtils.isNotBlank(reportParamsMap.get("createTimeFrom"))){
				SQL.append(" AND VINF.submit_time >= '"+reportParamsMap.get("createTimeFrom")+"'");
			}
			//申报日期结束
			if(StringUtils.isNotBlank(reportParamsMap.get("createTimeTo"))){
				SQL.append(" AND VINF.submit_time <= '"+reportParamsMap.get("createTimeTo")+"'");
			}
			//中央预算内(2017)申请资金开始
			if(StringUtils.isNotBlank(reportParamsMap.get("budTotalFrom"))){
				SQL.append(" AND VINF.apply_captial_2017 >= '"+reportParamsMap.get("invTotalFrom")+"'");
			}
			//中央预算内(2017)申请资金结束
			if(StringUtils.isNotBlank(reportParamsMap.get("budTotalTo"))){
				SQL.append(" AND VINF.apply_captial_2017 <= '"+reportParamsMap.get("budTotalTo")+"'");
			}
			//中央预算内下达资金开始
			if(StringUtils.isNotBlank(reportParamsMap.get("issuedTotalFrom"))){
				SQL.append(" AND VINF.cur_allocated >= '"+reportParamsMap.get("issuedTotalFrom")+"'");
			}
			//中央预算内下达资金结束
			if(StringUtils.isNotBlank(reportParamsMap.get("issuedTotalTo"))){
				SQL.append(" AND VINF.cur_allocated <= '"+reportParamsMap.get("issuedTotalTo")+"'");
			}
			//实际开工时间开始
			if(StringUtils.isNotBlank(reportParamsMap.get("actualStartTimeFrom"))){
				SQL.append(" AND VINF.actualStartTime >= '"+reportParamsMap.get("actualStartTimeFrom")+"'");
			}
			//实际开工时间结束
			if(StringUtils.isNotBlank(reportParamsMap.get("actualStartTimeTo"))){
				SQL.append(" AND VINF.actualStartTime <= '"+reportParamsMap.get("actualStartTimeTo")+"'");
			}
			//实际竣工时间开始
			if(StringUtils.isNotBlank(reportParamsMap.get("actualEndTimeFrom"))){
				SQL.append(" AND VINF.actualEndTime >= '"+reportParamsMap.get("actualEndTimeFrom")+"'");
			}
			//实际竣工时间结束
			if(StringUtils.isNotBlank(reportParamsMap.get("actualEndTimeTo"))){
				SQL.append(" AND VINF.actualEndTime <= '"+reportParamsMap.get("actualEndTimeTo")+"'");
			}
			//中央预算内到位资金开始
			if(StringUtils.isNotBlank(reportParamsMap.get("finishTotalFrom"))){
				SQL.append(" AND VINF.A00016WC >= '"+reportParamsMap.get("finishTotalFrom")+"'");
			}
			//中央预算内到位资金结束
			if(StringUtils.isNotBlank(reportParamsMap.get("finishTotalTo"))){
				SQL.append(" AND VINF.A00016WC <= '"+reportParamsMap.get("finishTotalTo")+"'");
			}
			//专项建设基金投放资金开始
			if(StringUtils.isNotBlank(reportParamsMap.get("putinCaptialFrom"))){
				SQL.append(" AND VINF.A00018D23 >= '"+reportParamsMap.get("putinCaptialFrom")+"'");
			}
			//专项建设基金投放资金结束
			if(StringUtils.isNotBlank(reportParamsMap.get("putinCaptialTo"))){
				SQL.append(" AND VINF.A00018D23 <= '"+reportParamsMap.get("putinCaptialTo")+"'");
			}
		}		
		return SQL.toString();
	}
}