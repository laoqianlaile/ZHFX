package com.strongit.iss.service.neuinterface;

import com.strongit.iss.entity.ViewBean;
import com.strongit.iss.exception.BusinessServiceException;

import java.util.List;
import java.util.Map;

/**
 *   审核备总览视图
 * Created by song.hl on 2016/8/28 13:53.
 */
public interface IProjectViewService {
  /***
     *  查询地图的数据
     *  @orderBy  申报时间升序、建设地点Code 升序
     *  @module  所属模块
     *  @params
     *   @see  com.strongit.iss.common.Constant
     *   @return
     *        不存在返回空对象
     * @Author tannongchun
     * @Date 2016/8/4 21:46
     */
	
    public List<ViewBean>  findMapData(String module, String type, Map<String, String> params) throws BusinessServiceException;


    /***
     *  查询地图的数据
     *  @orderBy  维度的Code 升序
     *  @ param dimension  所属维度
     *   @return
     *        不存在返回空对象
     * @Author tannongchun
     * @Date 2016/8/4 21:46
     */
    public List<ViewBean>  findDimensionData(String dimension, String type, Map<String, String> params) throws BusinessServiceException;
     /***
      *   得到模块的金字塔数据
      * @Orderby 根据需求文档说明排序的
      * @param module
      *         -- module == null or module=='' 返回总览的金字塔
      *         -- 否则返回对应模块的金字塔
      * @retrun
      *        -- 没有查询结果，返回 空对象
      *        -- 有查询结果 返回对应的查询结果
      * @Author tannongchun
      * @Date 2016/8/6 20:39
      */
     public List<ViewBean>  getPyramidData(String module, Map<String, String> params)throws BusinessServiceException;
     /**
      *  得到三年滚动饼状结构数据
      * @param module
      * @return
      * 	    --没有查询结果，返回空对象，不显示
      * 		--有查询结果返回查询结果的值
      * @author kanfei
      * @Date 2016年8月16日下午3:12:11
      * @throws BusinessServiceException
      */
     public List<ViewBean>  getPieData(String module, Map<String, String> params)throws BusinessServiceException;

     /**
      * 
      * @orderBy 
      * @param module
      * @param type
      * @return
      * @author wuwei
      * @Date 2016年8月16日下午4:53:11
      */
	 public List<ViewBean> findDimensionData1(String module, String type, Map<String, String> params) throws BusinessServiceException;
	 /**
	  * 
	  * @param module
	  * @param type
	  * @return
	  * @author kanfei
      * @Date 2016年8月18日上午10:53:11
	  */
	 public List<ViewBean> fivePlanData(String module, String type, Map<String, String> params);
	 

	 /**
	  * 
	  * @orderBy 
	  * @param type
	  *        维度
	  *       -- params
	  *          过滤条件
	  * @return
	  * @throws BusinessServiceException
	  * @author wuwei
	  * @Date 2016年8月18日下午3:12:05
	  */
	 public List<ViewBean> getAllInfo(String type, Map<String, String> params) throws BusinessServiceException;
}
