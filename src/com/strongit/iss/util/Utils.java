package com.strongit.iss.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.context.SecurityContextHolder;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.DateUtil;
import com.strongit.iss.common.PropertiesUtils;
import com.strongit.iss.exception.BusinessServiceException;

/**
 * 共通算法类
 * 
 * @author zhaojian
 * @version 1.0
 */
public class Utils {
	
	// 处理子系统标识（true-处理子系统；false-过渡子系统）
	private static Boolean isNSystem = null;
	
	public static final String PROJECT_CHK_ITEM_DATA_FORMAT_ERR  = ":FieldName0格式错误，应为:FieldName1位的正整数";
	public static final String PROJECT_CHK_ITEM_TIME_FORMAT_ERR  = ":FieldName0格式错误，正确的时间格式为：:FieldName1";
	public static final String PROJECT_CHK_ITEM_TIME_FORMAT_ERR2 = ":FieldName0格式错误，仅可以为4位（即：年份）或14位（即：年月日时分秒）的时间格式";
	

	/**
	 * 检查系统是否是处理子系统
	 * 
	 * @return
	 */
	public static boolean isNSystem() {
		if (isNSystem == null) {
			// 系统部署环境（N-国家发改委；P-省级发改委）
			String deploymentEnvironment = PropertiesUtils.getProperty("iss.deploymentEnvironment");
			isNSystem = "N".equalsIgnoreCase(deploymentEnvironment) ? true : false;
		}
		
		return isNSystem;
	}

	/**
	 * 校验字符串是否符合正则表达式
	 * 
	 * @param value
	 *            String 字符串
	 * @param regEx
	 *            String 正则表达式
	 * @return
	 */
	public static boolean matchRegular(String value, String regEx) {
		if (StringUtils.isBlank(value) || StringUtils.isBlank(regEx)) {
			return false;
		}

		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(value);
		return m.matches();
	}

	/**
	 * 校验是否是合法日期格式的字符串
	 * 
	 * @param value
	 *            String 字符串
	 * @param dateFormat
	 *            String 日期格式
	 * @return
	 */
	public static boolean isValidDate(String value, String dateFormat) {
		if (StringUtils.isBlank(value) || StringUtils.isBlank(dateFormat)) {
			return false;
		}

		if (dateFormat.length() != value.length()) {
			return false;
		}

		DateFormat formatter = new SimpleDateFormat(dateFormat);
		// 严格校验日期
		formatter.setLenient(false);
		try {
			formatter.parse(value);
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

	/**
	 * 判断字符串是否是大于0的合法的半角数值
	 * 
	 * @param value
	 *            String 字符串
	 * @param chkLen
	 *            boolean 是否校验数值长度,包括整数位及精度
	 * @param intPartLen
	 *            int 整数位长度
	 * @param precision
	 *            数值精度
	 * @return
	 */
	public static boolean isHalfNumber(String value, boolean chkLen,
			int intPartLen, int precision) {
		
		if (StringUtils.isBlank(value)) {
			return false;
		}
		
		int iPos;
		boolean fType = true;
		// 整数部分
		String strIntPart = null;
		// 小数部分
		String strPrecisionPart = null;

		if (value.startsWith("-")) {
//			value = value.substring(1);
			return false;
		}

		iPos = value.indexOf(".");
		if (iPos == -1) {
			// 整数部分
			strIntPart = value;
		} else {
			if (value.startsWith(".") || value.endsWith(".")) {
				return false;
			}

			// 整数部分
			strIntPart = value.substring(0, iPos);
			// 小数部分
			strPrecisionPart = value.substring(iPos + 1);
			if (strPrecisionPart.indexOf(".") != -1) {
				return false;
			}
		}

		// 判断整数及小数部分是否是合法的半角数值
		if (StringUtils.isNotBlank(strIntPart)) {
			fType = isHalfNum(strIntPart);
		}

		if (fType && StringUtils.isNotBlank(strPrecisionPart)) {
			fType = isHalfNum(strPrecisionPart);
		}
		
		// 校验数值长度
		if (fType && chkLen) {
			fType = (intPartLen >= strIntPart.length());
			
			// TODO 暂不对小数点后精度做校验
			// 判断是否需要验证小数点后精度
			if (precision > 0) {
				fType = fType && (precision == strPrecisionPart.length());
			}else{
				if(strPrecisionPart != null)
					fType = (precision == strPrecisionPart.length());
			}
		}

		return fType;
	}

	/**
	 * 判断字符串是否是合法的半角数值
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isHalfNum(String value) {
		
		if (StringUtils.isBlank(value)) {
			return false;
		}

		char c;
		for (int i = 0; i < value.length(); i++) {
			c = value.charAt(i);
			if (c < 0x0030 || c > 0x0039) {
				return false;
			}
		}

		return true;
	}
	
	/**
	 * 校验是否是合法日期格式的字符串
	 * 
	 * @param value
	 *            String 字符串
	 * @param dateFormat
	 *            String 日期格式
	 * @return
	 */
	public static Date transStr2Date(String value, String dateFormat) {
		if (StringUtils.isBlank(value) || StringUtils.isBlank(dateFormat)) {
			return null;
		}
		
		// 日期格式
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		try {
			return format.parse(value);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 根据消息参数替换原消息
	 * 
	 * @param argMessage
	 *            String 原消息
	 * @param argMessageParam
	 *            Object... 消息参数
	 * 
	 * @return 替换后的消息
	 */
	public static String getMessage(String argMessage, Object... argMessageParam) {
		// 原消息
		String msg = argMessage;

		// 若原消息为null,返回空值
		if (msg == null)
			return "";

		// 若替换消息参数为null,返回原消息
		if (argMessageParam == null || argMessageParam.length == 0) {
			return msg;
		}

		// 替换消息参数不为空时，执行消息内容替换
		try {
			StringBuffer msgBuffer = new StringBuffer(msg);

			int paramIndex = 0;
			String key = null;
			if (argMessageParam != null && argMessageParam.length > 0) {

				for (Object messageparam : argMessageParam) {
					String replaceValue = String.valueOf(messageparam);

					// 从原消息内容中获取参数位置
					key = ":FieldName" + paramIndex;
					int index = msgBuffer.toString().indexOf(key);
					if (index != -1) {
						int startIndex = index;

						int endIndex = startIndex + key.length();

						// 替换原消息内容
						if (replaceValue != null) {
							msgBuffer.replace(startIndex, endIndex,
									replaceValue);
						}
					}
					
					paramIndex++;
				}
			}

			// 设定替换后的消息
			msg = msgBuffer.toString();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return msg;
	}
	
	/**
	 * 判断是否需要进行模糊查询
	 * 
	 * @param str
	 *            String 查询条件
	 * @return
	 */
	public static boolean isFuzzyQuery(String str) {

		if (str.indexOf("%") != -1 || str.indexOf("_") != -1) {
			return true;
		}

		return false;
	}

	/**
	 * 模糊查询字符串替换
	 * 
	 * @param strin
	 *            要替换的字符串
	 * @param replaceWildCard
	 *            是否替换通配符（true:替换；false: 不替换）
	 * @return
	 */
	public static String replaceSqlLike(String strin, boolean replaceWildCard) {
		String strEscape = Constant.ESCAPE_CHAR;

		strin = replace(strin, strEscape, strEscape + strEscape);
		strin = replace(strin, "％", strEscape + "％");
		strin = replace(strin, "＿", strEscape + "＿");

		// 替换通配符
		if (replaceWildCard) {
			strin = replace(strin, "%", strEscape + "%");
			strin = replace(strin, "_", strEscape + "_");
		}

		return strin;
	}

	/**
	 * 字符串替换
	 * 
	 * @param strin
	 *            要替换的字符串
	 * @param strRe
	 *            替换前字符串
	 * @param strBy
	 *            替换后字符串
	 * @return
	 */
	public static String replace(String strin, String strRe, String strBy) {

		int iPos;
		String strTemp = "";

		iPos = strin.indexOf(strRe);
		while (iPos != -1) {
			strTemp = strTemp + strin.substring(0, iPos) + strBy;
			strin = strin.substring(iPos + strRe.length());
			iPos = strin.indexOf(strRe);
		}

		strin = strTemp + strin;

		return strin;
	}
	
	/**
	 * 按位数从左方补足字符串
	 * 
	 * @param str
	 *            String 原字符串
	 * @param totalLen
	 *            int 补足后的总长度
	 * @param padChar
	 *            char 补充的字符
	 * @return
	 */
	public static String lpad(String str, int totalLen, char padChar) {
		return pad(1, str, totalLen, padChar);
	}
	
	/**
	 * 按位数从右方补足字符串
	 * 
	 * @param str
	 *            String 原字符串
	 * @param totalLen
	 *            int 补足后的总长度
	 * @param padChar
	 *            char 补充的字符
	 * @return
	 */
	public static String rpad(String str, int totalLen, char padChar) {
		return pad(2, str, totalLen, padChar);
	}
	
	/**
	 * 按位数补足字符串
	 * 
	 * @param direction
	 *            int 补足方向（1-左方补足；2-右方补足）
	 * @param str
	 *            String 原字符串
	 * @param totalLen
	 *            int 补足后的总长度
	 * @param padChar
	 *            char 补充的字符
	 * @return
	 */
	public static String pad(int direction, String str, int totalLen, char padChar) {
		if (StringUtils.isBlank(str)) {
			str = "";
		}
		
		int strLen = str.length();
		int padLen = totalLen - strLen;
		if (padLen > 0) {
			StringBuilder sbStr = new StringBuilder();
			
			if (direction == 1) { // 左方补足
				for (int i = 1; i <= padLen; i++) {
					sbStr.append(padChar);
				}
				sbStr.append(str);
			} else { // 右方补足
				sbStr.append(str);
				for (int i = 1; i <= padLen; i++) {
					sbStr.append(padChar);
				}
			}
			
			return sbStr.toString();
		}
		
		return str;
	}
	

	/**
	 * 默认Escape字符串
	 */
	public static String escapeClause() {
		return " ESCAPE '" + Constant.ESCAPE_CHAR + "' ";
	}

	

	

	
	
	
	/**
	 * 获取Web根目录
	 */
	public static String getWebContentPath() {
		URL url = Utils.class.getResource("");
		if (url != null) {
			String path = url.toString();
			int webInfIndex = path.indexOf("WEB-INF");
			if (webInfIndex != -1) {
				path = path.substring(path.indexOf("/"), webInfIndex);
			}
			try {
				path = java.net.URLDecoder.decode(path, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return path;
		}
		
		return "";
	}
	
	/**
	 * 从HttpServletRequest对象中获取客户端IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (null != ip && ip.indexOf(',') != -1) {
			String[] ips = ip.split(",");
			for (int i = 0; i < ips.length; i++) {
				if (null != ips[i] && !"unknown".equalsIgnoreCase(ips[i])) {
					ip = ips[i];
					break;
				}
			}
		}
		
		// TODO IPV6
		if ("0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "127.0.0.1";
		}
		
		return ip;
	}
	
	/**
	 * 返回处理后的日志字段数据
	 * 
	 * @param value
	 * @param isLast
	 * @return
	 */
	public static String getLogColValue(Object value, boolean isLast) {
		boolean isString = false;
		StringBuilder returnValue = new StringBuilder();
		if (value == null || StringUtils.isBlank(String.valueOf(value))) {
			returnValue.append("NULL");
		} else {
			if (value instanceof String) {
				isString = true;
			}

			// 数值前追加【'】
			if (isString) {
				returnValue.append("'");
			}
			
			// 数值
			returnValue.append(value);

			// 数值后追加【'】
			if (isString) {
				returnValue.append("'");
			}
		}

		// 不是最后一列，追加【, 】
		if (!isLast) {
			returnValue.append(", ");
		}

		return returnValue.toString();
	}
	
	/**
	 * 将补位后的六位行业编码转换为标准行业编码：
	 * 转换前：前面补位一级行业编码，后面补位多个X，形成完整六位编码，如A01XXX、A011XX、A0111X、A0112X等
	 * 转换后：A、01、011、0111、0112等
	 * 
	 * @param tradeCode
	 *            String 补位后的六位行业编码
	 * @return
	 */
	public static String transToStandardTradeCode(String tradeCode) {
		try {
			if (StringUtils.isNotBlank(tradeCode)) {
				// 判断第一位是不是字母
				String firstChar = tradeCode.substring(0, 1);
				if (matchRegular(firstChar, "^[A-Za-z]+$")) {
					tradeCode = tradeCode.substring(1, tradeCode.length());
				}
	
				tradeCode = tradeCode.replaceAll("(X|x)", "");
			}
		} catch (Exception e) {
			return tradeCode;
		}

		return tradeCode;
	}
	
	
	
	/**
	 * 向客户端输出数据
	 * 
	 * @param isError
	 * @param contentType
	 * @param text
	 * @return
	 */
	public static String render(boolean isError, String contentType, String text) {
		HttpServletResponse response = null;
		PrintWriter printWriter = null;
		
		try {
			response = ServletActionContext.getResponse();
			response.setContentType(contentType);
			if (isError) {
				response.setStatus(500);
			}

			printWriter = response.getWriter();
			printWriter.write(text);
			printWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (printWriter != null) {
					printWriter.close();
					printWriter = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
		}
		
		return null;
	}
	
	/**
	 * 对文本数据进行Html编码
	 * 
	 * @param text
	 * @return
	 */
	public static String HTMLEncode(String text) {
		if (StringUtils.isBlank(text)) {
			return "";
		}
		
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(text);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '<') {
				result.append("&lt;");
			} else if (character == '>') {
				result.append("&gt;");
			} else if (character == '&') {
				result.append("&amp;");
			} 
			else {				
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}
	
	public enum Operator {
		EQ, LK, GT, LT, GTE, LTE,OREQ
	}

	public String fieldName;
	public Object value;
	public Operator operator;
	
	public Utils(String fieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}
	
	/**
	 * searchParams中key的格式为OPERATOR_FIELDNAME
	 */
	public static Map<String, Utils> parse(Map<String, Object> searchParams) {
		Map<String, Utils> filters = Maps.newHashMap();

		for (Entry<String, Object> entry : searchParams.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			Object value = entry.getValue();
			if (StringUtils.isBlank(String.valueOf(value))) {
				continue;
			}
			if(DateUtil.isDate(value.toString())){
				value=DateUtil.parseDateTime(value.toString());
			}

			// 拆分operator与filedAttribute
			String[] names = StringUtils.split(key, "_");
			if (names.length != 2) {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			String filedName = names[1];
			Operator operator = Operator.valueOf(names[0]);

			// 创建searchFilter
			Utils filter = new Utils(filedName, operator, value);
			filters.put(key, filter);
		}

		return filters;
	}
	
	public static List<Criterion> getCriterioinList(Map<String, Object> searchParams) {
		Map<String, Utils> map = parse(searchParams);
		List<Criterion> criterionList = new ArrayList<Criterion>();
		for (Utils filter : map.values()) {
			switch (filter.operator) {
			case EQ:
				criterionList.add(Restrictions.eq(filter.fieldName,
						filter.value));
				break;
			case LK:
				criterionList.add(Restrictions.like(filter.fieldName,
						filter.value));
				break;
			case GT:
				criterionList.add(Restrictions.gt(filter.fieldName,
						filter.value));
				break;
			case LT:
				criterionList.add(Restrictions.lt(filter.fieldName,
						filter.value));
				break;
			case GTE:
				criterionList.add(Restrictions.ge(filter.fieldName,
						filter.value));
				break;
			case LTE:
				criterionList.add(Restrictions.le(filter.fieldName,
						filter.value));
				break;
			/*
			 * case OREQ: String[] vals = ((String)filter.value).split("OR");
			 * if(vals.length>0){ List<Predicate> builders =
			 * Lists.newArrayList(); for(int i=0;i<vals.length;i++){
			 * builders.add(builder.equal(expression, vals[i])); }
			 * predicates.add(builder.or(builders.toArray(new
			 * Predicate[builders.size()]))); } break;
			 */
			}
		}

		return criterionList;
	}
	
	/**
	 * @Description：将Date类转换为XMLGregorianCalendar
	 * @param 		 date
	 * @return 
	 */
	public static XMLGregorianCalendar dateToXmlDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		DatatypeFactory dtf = null;
		try {
			dtf = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
		}
		XMLGregorianCalendar dateType = dtf.newXMLGregorianCalendar();
		dateType.setYear(calendar.get(Calendar.YEAR));

		// 由于Calendar.MONTH取值范围为0~11,需要加1
		dateType.setMonth(calendar.get(Calendar.MONTH) + 1);
		dateType.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		dateType.setHour(calendar.get(Calendar.HOUR_OF_DAY));
		dateType.setMinute(calendar.get(Calendar.MINUTE));
		dateType.setSecond(calendar.get(Calendar.SECOND));
		return dateType;
	}
	
	/**
	 * @Description：将XMLGregorianCalendar转换为Date
	 * @param 		 calendar
	 * @return 
	 */
	public static Date xmlDateToDate(XMLGregorianCalendar calendar){
		return calendar.toGregorianCalendar().getTime();
	}
	
	/**
	 * 获取文件的编码格式
	 * @author			蒋诗洋
	 * @createDate		2014-12-3
	 */
	public static String getEncodedFormat(File file) throws Exception{
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		boolean checked = false;
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		bis.mark(0);
		int read = bis.read(first3Bytes, 0, 3);
		if (read == -1) {
			bis.close();
			return charset; // 文件编码为 ANSI
		} else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
			charset = "UTF-16LE"; // 文件编码为 Unicode
			checked = true;
		} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
			charset = "UTF-16BE"; // 文件编码为 Unicode big endian
			checked = true;
		} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
			charset = "UTF-8"; // 文件编码为 UTF-8
			checked = true;
		}
		bis.reset();
		if (!checked) {
			while ((read = bis.read()) != -1) {
				if (read >= 0xF0)
					break;
				if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
					break;
				if (0xC0 <= read && read <= 0xDF) {
					read = bis.read();
					if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
						// (0x80
						// - 0xBF),也可能在GB编码内
						continue;
					else
						break;
				} else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
					read = bis.read();
					if (0x80 <= read && read <= 0xBF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							charset = "UTF-8";
							break;
						} else
							break;
					} else
						break;
				}
			}
		}
		bis.close();
		return charset;
	}
	
	
	public synchronized static String getBatchMark(String userName){
		try {
			Thread.sleep(1);
			return userName+System.currentTimeMillis();
		} catch (InterruptedException e) {
			return userName+System.currentTimeMillis();
		}
	}
	
	/**
	 * 从appconfig.properties文件中获取项目编码流水号长度
	 * 
	 * @return
	 */
	public static int getSerialNumLen() {
		String serialNumLen = PropertiesUtils.getProperty("project.identifycode.serialnum.length");
		if (StringUtils.isNotBlank(serialNumLen)) {
			return Integer.parseInt(serialNumLen);
		}
		
		return 6;
	}
	
	/**
	 * 判断获取的流水号是否已经超出所配置的流水号长度，如超出，则减去相应的位数并返回
	 * 
	 * @param startSerialNum long 开始流水号
	 * @param projectListSize int 项目数
	 * @param serialNumLen int 项目编码流水号长度
	 * 
	 * @return
	 */
	public static long getSerialNum(long startSerialNum, int projectListSize, int serialNumLen) {
		if (serialNumLen <= 0) {
			serialNumLen = 6;
		}
		
		// 结束流水号
		long endSerialNum = startSerialNum + projectListSize - 1;
		
		// 根据流水号长度配置所能获取的最大流水号
		String sMaxSerialNum = Utils.lpad("", serialNumLen, '9');
		long maxSerialNum = Long.parseLong(sMaxSerialNum);
		
		if (endSerialNum > maxSerialNum) {
			return (endSerialNum - maxSerialNum);
		}
		
		return startSerialNum;
	}
	
	/**
	 * 获取指定键值的JsonNode值
	 * 
	 * @param jsonNode
	 * @param key
	 * @return
	 */
	public static String getJsonNodeValue(JsonNode jsonNode, String key) {
		JsonNode jn = jsonNode.get(key);
		if (jn != null) {
			return jn.asText();
		}
		
		return null;
	}
	
	/**
	 * 将文件压缩成zip包
	 * 
	 * @param filePath
	 *            String 待压缩文件路径
	 * @param zipPath
	 *            String 压缩包存放路径
	 * @param zipName
	 *            String 压缩包名
	 *            
	 * @throws BusinessServiceException
	 */
	public static String createZipFile(String filePath, String zipPath, String zipName) throws BusinessServiceException {
		
		if (StringUtils.isBlank(filePath)) {
			throw new BusinessServiceException("待压缩文件路径不能为空.");
		}

		if (StringUtils.isBlank(zipPath)) {
			throw new BusinessServiceException("压缩包存放路径不能为空.");
		}

		if (StringUtils.isBlank(zipName)) {
			throw new BusinessServiceException("压缩包名不能为空.");
		}
		
		// 压缩后的压缩包路径
		String zipFilePath = null;
		
		// 文件压缩流
		ZipOutputStream out = null;
		try {
			// 待压缩文件路径
			File file = new File(Utils.getWebContentPath() + filePath);
			if (!file.exists()) {
				throw new BusinessServiceException("待压缩文件路径不存在.");
			}
			
			// 根据操作系统类型获取文件分隔符
//			String fileSeparator = getFileSeparator();
			String fileSeparator = "/";
			// 文件压缩流
			zipFilePath = zipPath + fileSeparator + zipName;
			out = new ZipOutputStream(new FileOutputStream(Utils.getWebContentPath()+zipFilePath));
			// 文件压缩处理
			zip(out, file, zipPath, zipName, "");
			
		} catch (Exception e) {
			throw new BusinessServiceException(e.getMessage(), e);
		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return zipFilePath;
	}
	
	/**
	 * zip文件解压到指定文件夹
	 * @author			xhf
	 * @createDate		2015年4月14日下午4:48:36
	 * @arithMetic                                                                           
	 * @param zipFilePath zip文件路径
	 * @param targetPath  目标路径文件夹
	 * @throws IOException
	 */
	@SuppressWarnings({ "resource", "rawtypes" })
	public static void unZip(String zipfilePath, String destDir)
			throws Exception {


		destDir = destDir.endsWith("\\") ? destDir : destDir + "\\";

		byte b[] = new byte[1024];

		int length;

		org.apache.tools.zip.ZipFile zipFile = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {

			zipFile = new org.apache.tools.zip.ZipFile(new File(zipfilePath),"GBK");

			Enumeration enumeration = zipFile.getEntries();

			org.apache.tools.zip.ZipEntry zipEntry = null;
			
			while (enumeration.hasMoreElements()) {

				try {
					zipEntry = (org.apache.tools.zip.ZipEntry) enumeration.nextElement();
					File loadFile = new File(destDir + zipEntry.getName());
					if (zipEntry.isDirectory()) {

						loadFile.mkdirs();

					} else {

						if (!loadFile.getParentFile().exists()) {

							loadFile.getParentFile().mkdirs();

						}

						outputStream = new FileOutputStream(loadFile);
						inputStream = zipFile.getInputStream(zipEntry);
						ZipInputStream zipInputStream = new ZipInputStream(inputStream);
						while ((length = inputStream.read(b)) > 0) {
							outputStream.write(b, 0, length);
						}

					}
				} catch (Exception e) {
					throw e;
				} finally {
					if (inputStream != null) {
						inputStream.close();
						inputStream = null;
					}
					if (outputStream != null) {
						outputStream.close();
						outputStream = null;
					}
				}

			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (zipFile != null) {
				zipFile.close();
				zipFile = null;
			}
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
			if (outputStream != null) {
				outputStream.close();
				outputStream = null;
			}
		}

	}
	
	
	/**
	 * 文件压缩处理
	 * 
	 * @param out
	 *            ZipOutputStream 文件压缩流
	 * @param file
	 *            File 待压缩文件或文件夹
	 * @param zipPath
	 *            String 压缩包存放路径
	 * @param zipName
	 *            String 压缩包名
	 * @param zipDir
	 *            String 压缩包内文件相对路径
	 * 
	 * @throws Exception
	 */
	public static void zip(ZipOutputStream out, File file, String zipPath, String zipName, String zipDir) throws Exception {
		
		// 根据操作系统类型获取文件分隔符
		String fileSeparator = getFileSeparator();
		
		// 文件写入流
		FileInputStream in = null;
		try {
			// 文件存放目录
			String fileParent = file.getParent();
			
			if (file.isDirectory()) { // 文件夹
				File[] fl = file.listFiles();
				
				// 按文件夹遍历
				zipDir = zipDir.length() == 0 ? "" : zipDir + fileSeparator;
				for (File f : fl) {
					zip(out, f, zipPath, zipName, zipDir + f.getName());
				}
			} else { // 文件
				// 判断待压缩文件路径与压缩包存放路径是否一致
				boolean isSamePath = zipPath.equalsIgnoreCase(fileParent) ? true : false;
				
				if (!isSamePath || !file.getName().equalsIgnoreCase(zipName)) {
					out.putNextEntry(new ZipEntry(zipDir));
					
					// 将文件写入压缩包
					int b;
					in = new FileInputStream(file);
					while ((b = in.read()) != -1) {
						out.write(b);
					}
				}
			}
		} catch (Exception e) {
			throw new BusinessServiceException(e.getMessage(), e);
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据操作系统类型获取文件分隔符
	 * 
	 * @return
	 */
	public static String getFileSeparator() {
		// 文件分隔符
		String fileSeparator = "/";
		
		try {
			if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
				fileSeparator = "\\";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return fileSeparator;
		}
		
		return fileSeparator;
	}
	
	
	/**
	 * 复制文件
	 * @author			xhf
	 * @createDate		2015年4月13日上午9:53:05
	 * @arithMetic                                                                           
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean copy(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), 16 * 1024);
			out = new BufferedOutputStream(new FileOutputStream(dst), 16 * 1024);
			byte[] buffer = new byte[16 * 1024];
			while (in.read(buffer) > 0) {
				out.write(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return true;
	}

	/**
	 * 删除文件夹及文件夹下的所有文件
	 * @author			xhf
	 * @createDate		2015年4月14日下午1:07:30
	 * @arithMetic                                                                           
	 * @param folder
	 */
	public static boolean deleteDir(File folder) {
		if (folder.isDirectory()) {
			String[] children = folder.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(folder, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return folder.delete();
	}
	
	/**
	 * 下载文件
	 * 
	 * @param filePath
	 */
	public static void downloadFile(String filePath) throws Exception {
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			// 获取文件名
			String[] names = filePath.split("/");
			String name = names[names.length - 1];
	
			// 生成新的文件下载地址
			File file = new File(filePath);
			if (file != null && file.exists()) {
				response.setContentType("text/html;charset=utf-8");
				response.setContentType("application/octet-stream;");
				response.setHeader("Content-disposition","attachment; filename=" + 
						new String(name.getBytes("GBK"), "ISO8859-1"));
				response.setHeader("Content-Length",String.valueOf(file.length()));
				inputStream = new BufferedInputStream(new FileInputStream(file));
				outputStream = new BufferedOutputStream(response.getOutputStream());
				byte[] buff = new byte[2048];
				int bytesRead;
				while (-1 != (bytesRead = inputStream.read(buff, 0, buff.length))) {
					outputStream.write(buff, 0, bytesRead);
				}
			}
		} finally {
			if (inputStream != null){
				inputStream.close();
				inputStream = null;
			}
			if (outputStream != null){
				outputStream.close();
				outputStream = null;
			}
		}
	}
	
	/**
	 * 判断输入是否有特殊字符
	 * @author			肖红飞
	 * @createDate		2015年11月29日下午5:56:44
	 * @arithMetic                                                                           
	 * @param value
	 * @return true 有  false 无
	 * @throws Exception
	 */
	public static boolean isSpecialChar(String value) throws Exception{
		boolean flag = true;
		try {
			String reg = ("['\\<>$!%#*&]");
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(value);
			flag = m.find();
		} catch (Exception e) {
		}
		return flag;
	}	
	/**
	 * String数组除重
	 * @author			杨硕
	 * @createDate		2016-6-21下午2:14:48
	 * @arithMetic                                                                           
	 * @param values
	 * @return
	 */
	public static String[] noRepeat(String[] values){
		List<String> list = Arrays.asList(values);
		Set<String> set = new HashSet<String>(list);
		String [] result = (String [])set.toArray(new String[0]);
		return result;
	}

}
