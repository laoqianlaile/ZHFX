package com.strongit.iss.common;

/**
 * 常量定义类
 * 
 * @author zhaojian
 * @version 1.0
 */
public  final class  Constant {
	/**
	 *  数据库特殊字符过滤
	 */
  public final static  String  ESCAPE_CHAR="/";
	// 记录用户信息
	public final static String SYS_USER_INFO="SYS_USER_INFO";
	/**
	 *  记录用户信息
     */
	public final static String SYS_USER_ID="SYS_USER_ID";
	/**
	 *  登录成功标识
	 */
	public final static Integer RESULT_SUCCESS=0;
	/**
	 *  登录失败标识
	 */
	public final static Integer RESULT_FAILURE=1;
	/**
	 * 验证码错误
	 */
	public final static Integer RESULT_CHECK_FAILURE=2;
	/**
	 *  模块
     */
	// 五年储备
	public final static String FIVE_PLAN="fiveplan";
	// 三年滚动计划
	public final static String ROLL_PLAN="rollplan";
	//专项建设基金
	public final static  String FUNDS="funds";
	//中央预算内
    public final static String BUDGET="budget";
	//中央预算内上报
	public final static String BUDGETREPORT="budgetreport";
	//中央预算内下达
	public final static String BUDGETISSUE="budgetissue";
	//中央预算内调度
	public final static String BUDGETDIAPATCH="budgetdiapatch";
	// 查询项目个数
	public final  static String NUM="num";
	//z总投资
	public final static String INVEST="invest";
   // 指标重大战略
     public final   static String MAJOR_STRATEGIC="major";
    //发改委行业，所属行业，行业
    public final   static String FGW_INDUSTRY="industry";
    //符合政府投资方向 direction of government investment
    public final   static String  GOV_DIRECTION="direction";
    //建设地点
    public final static String BUILD_PLACE = "area";
    //申报单位
    public final static String REPORT_UNIT = "report";
	/**
	 *  未开工
	 */
	public final static  String  UNFINISH_PROJECTS="unfinish";
	/**
	 *  开工项目
	 */
	public final static  String  START_PROJECTS="start";
	/**
	 *  竣工项目
     */
	public final static  String  FINISH_PROJECTS="finish";

	// 专项类别
	public final static  String SPECIAL_TYPE="specialtype";
    /**
     * 是年度计划
     */
    public final static Integer IS_BUDGET_YES=0;
    /**
     *  是专项建设基金
     */
    public final static Integer IS_FUNDS_YES=1;
	/**
	 *  第一级
	 */
	public   final static  String LEVEL_1="1";
	/**
	 *  第二级
	 */
	public  final static  String LEVEL_2="2";
	/**
	 *  第三级
	 */
	/**
	 *  第三级
	 */
	public  final static String LEVEL_3="3";
	/**
	 *  级别
	 */
	public  final static  String  LEVEL="level";
    /**
	 *  过滤的CODE
	 */
     public final static String filterCode="filterCode";
    /**
	 *  未开工
	 */
	 public  final static String NOT_STARTED="0";
		/**
		 *  开工项目
		 */
	 public  final static String STARTED="1";

	/**
	 *  完成状态
	 */
	public  final static String FINISHED="2";

	/**
	 *  部门类别-发改委
     */
	public  final static String DEPT_TYPE_FGW="FGW";

	/**
	 * CENTRE-COM
	 *  部门类别-中央部门
	 */
	public  final static String DEPT_TYPE_DEPT="DEPT";
	/**
	 * CENTRE-COM
	 *  部门类别-中央部门
	 */
	public  final static String DEPT_TYPE_CENTRE_COM="CENTRE-COM";
	/**
	 *  个数排序
	 */
	public final static String ORDERBY_CNT="orderbycnt";
	/**
	 *  金额排序
	 */
	public final static String ORDERBY_MON="orderbymon";
	/**
	 *  排序
	 */
	public final static String ORDERBY="orderby";
	/**
	 *  是否ppp-A00002-否
	 */
	public final static String ISPPP_A00002="A00002";
	/**
	 *  是否专项债-A00002-否
	 */
	public final static String ISFUNDS_A00002="A00002";
	/**
	 * 直辖市区code
	 */
	public final static String AREAS= "110000,120000,310000,500000";
	
	public final static String ARRAY [] = {"110000","120000","310000","500000"};
	

	// 建设地点code-value映射字段
	public static String CODE_COLUMN_BUILD_PLACE = "BUILD_PLACE";
	// 建设地点所属分组
	public static String BUILD_PLACE_GROUPNO = "BuildPlaceCode";
	// 项目类型code-value映射字段
	public static String CODE_COLUMN_PRO_TYPE = "PRO_TYPE";
	// 项目类型所属分组
	public static String PRO_TYPE_GROUPNO = "ProjectTypeCode";
	// 所属行业code-value映射字段
	public static String CODE_COLUMN_INDUSTRY = "INDUSTRY";
	// 所属行业所属分组
	public static String INDUSTRY_GROUPNO = "IndustryCode";
	// 国标行业code-value映射字段
	public static String CODE_COLUMN_GB_INDUSTRY = "GB_INDUSTRY";
	// 国标行业所属分组
	public static String GB_INDUSTRY_GROUPNO = "GBIndustryCode";
	// 建设性质code-value映射字段
	public static String CODE_COLUMN_BUILD_NATURE = "BUILD_NATURE";
	// 建设性质所属分组
	public static String BUILD_NATURE_GROUPNO = "BuildProperty";
	// 国别code-value映射字段
	public static String CODE_COLUMN_COUNTRY = "COUNTRY";
	// 国别所属分组
	public static String COUNTRY_GROUPNO = "CountryType";
	// 符合规划code-value映射字段
	public static String CODE_COLUMN_PLAN = "PLAN";
	// 符合规划所属分组
	public static String PLAN_GROUPNO = "FitPlans";
	// 符合重大战略类别code-value映射字段
	public static String CODE_COLUMN_MAJOR_STRATEGY = "MAJOR_STRATEGY";
	// 符合重大战略类别所属分组
	public static String MAJOR_STRATEGY_GROUPNO = "MajorCode";
	// 符合政府投资方向code-value映射字段
	public static String CODE_COLUMN_GOVERNMENT_INVEST_DIRECTION = "GOVERNMENT_INVEST_DIRECTION";
	// 符合政府投资方向所属分组
	public static String GOVERNMENT_INVEST_DIRECTION_GROUPNO = "GovernmentCode";
	// 	是否PPP code-value映射字段
	public static String CODE_COLUMN_ISPPP = "ISPPP";
	// 	是否PPP所属分组
	public static String ISPPP_GROUPNO = "PppOperateWay";
	// 	政府参与方式code-value映射字段
	public static String CODE_COLUMN_GOVERNMENT_JOIN_TYPE = "GOVERNMENT_JOIN_TYPE";
	// 	政府参与方式所属分组
	public static String GOVERNMENT_JOIN_TYPE_GROUPNO = "GovernmentCode";
	// 	形象进度code-value映射字段
	public static String CODE_COLUMN_IMAGE_PROGRESS = "IMAGE_PROGRESS";
	// 	形象进度所属分组
	public static String IMAGE_PROGRESS_GROUPNO = "ProgressCode";
	// 	招投标形式code-value映射字段
	public static String CODE_COLUMN_BIDDING_MODE = "BIDDING_MODE";
	// 	招投标形式所属分组
	public static String BIDDING_MODE_GROUPNO = "BidWay";
	// 	类型(一带一路)类型code-value映射字段
	public static String CODE_COLUMN_ONE_ONE_TYPE = "ONE_ONE_TYPE";
	//  类型(一带一路)类型所属分组
	public static String ONE_ONE_TYPE_GROUPNO = "OneOneType";
	//  建议银行类型code-value映射字段
	public static String CODE_COLUMN_PROPOSED_BANK = "PROPOSED_BANK";
	//  建议银行所属分组
	public static String PROPOSED_BANK_GROUPNO = "SuggestBank";
	//  专项类别code-value映射字段
	public static String CODE_COLUMN_SPECIAL_TYPE = "PROPOSED_BANK";
	//  专项类别所属分组
	public static String SPECIAL_TYPE_GROUPNO = "SpecType";
//  专项类别code-value映射字段
	public static String CODE_COLUMN_STAGE = "STAGE";
	//  专项类别所属分组
	public static String STAGE_GROUPNO = "projectStatge";
//  专项类别所属分组
	public static String QUA_PROJECT_ITEM_TYPE_NAME = "LHJSGM";
    /**
	 *   BI路径的分隔符
	 */
	public static String BI_SPLIT=">>";
	public static String FULL_NAME_SPLIT="-";
	
	//报表的类型
	//地区
	public static String REPORT_BUILD_PLACE="BUILDPLACE";
	//委内行业
	public static String REPORT_INDUSTRY="INDUSTRY";
	//国标行业
	public static String REPORT_GBINDUSTRY="GBINDUSTRY";
	//项目类型
	public static String REPORT_PRO_TYPE="PROTYPE";
	//项目状态
	public static String REPORT_PRO_STAUTS="PROSTAUST";
	//建设性质
	public static String REPORT_BUILD_NATURE="BUILDNATURE";
	//政府投资方向
	public static String REPORT_GOVERNMENT_INVEST_DIRECTION="GOVERNMENT_INVEST_DIRECTION";
	//项目阶段
	public static String REPORT_BUILD_STAGE="BUILDSTAGE";
	//重大战略
	public static String REPORT_MAJOR_STRATEGY="MAJORSTRATEGY";
	//形象进度
	public static String REPORT_IMAGE_PROGRESS="IMAGEPROGRESS";
	//债券专项
	public static String REPORT_SPECIAL_TYPE="SPECIALTYPE";
	//委内司局
	public static String REPORT_WNSJ="WNSJ";
	//申报部门
	public static String REPORT_DEPARTMENT_NAME="DEPARTMENTNAME";
	//审核备类型
	public static String REPORT_PROJECT_TYPE="PROJECTTYPE";
	//投资规模
	public static String REPORT_PROJECT_TZGM="TZGM";
	//申报时间
	public static String REPORT_TIME="TIME";
	/**
	 *  调度模块开工项目
	 */
	public final static  String  START_DIAPATCH_PROJECTS="start";
	/**
	 *  调度模块竣工项目
     */
	public final static  String  END_DIAPATCH_PROJECTS="end";
	/**dataType数据类型（1-本系统数据，2-外部系统数据）*/
	public static final Integer DATATYPE_INSIDE = 1;
	public static final Integer DATATYPE_OUTSIDE = 2;
	//存在字典组编号
	public static final String ZDZBH_IS_EXISTED = "0";
	public static final String ZDZBH_SHOW = "字典组编号已经存在！";
	
	//存在字典组名称
	public static final String ZDZMC_IS_EXISTED = "1";
	public static final String ZDZMC_SHOW = "字典组名称已经存在！";
	
	//存在字典项编号
	public static final String ZDXBH_IS_EXISTED = "ZDXBH";
	public static final String ZDXBH_SHOW = "字典项编号已存在！";
	
	//存在字典项名称
	public static final String ZDXMC_IS_EXISTED = "ZDXMC";
	public static final String ZDXMC_SHOW = "字典项名称已存在！";
	
	//验证是否存在子集，若存在则不可将字典项的父级修改为它的子集
	public static final String CHILDREN_IS_EXISTED = "ZDXZJ";
	public static final String CHILDREN_SHOW = "不可将字典项的父级修改为它的子集！";
	/**
	 *  登录方法
	 */
	public static final String LOGIN_SERVLET_METHOD="login";
	/**
	 *  获取首页地图数据方法
	 */
	public static final String MAP_DATA_METHOD="map";

	/**
	 * 已经登录的方法
     */
	public static  final String LOGININFO_METHOD="loginInfo";
	/**
	 * 所有人员
     */
	public static  final  String ALL_PERSON_METHOD="getScreenInfo";
	/**
	 *  转发的连接
	 */
	public static final String DISPATCHER_URL="/common/common!overview";

	/**
	 * 帆软报表获取数据
     */
	public static final  String  REPORT_DATAFROM_CACHE="getReportDataFromCache";
	
	//UTF-8编码格式
	public static final String CODE = "UTF-8";
	/**
	 *  二维码
	 */
	public static final String CAPTCHA_METHOD="CAPTCHA";
	/**
	 *  验证二维码
	 */
	public static final String CHECK_CAPTCHA_METHOD="CHECK_CAPTCHA";
	/**
	 *  菜单启用
	 */
	public static final String MENU_ENABLE="1";
	/**
	 *  开工或拟开工时间
	 */
	public static final Integer BEFORE_2014=2014;
	public static final Integer AFTER_2021=2021;
	/**
	 *  项目类型
	 */
	public static final String UNKOWN="尚未确定";
}
