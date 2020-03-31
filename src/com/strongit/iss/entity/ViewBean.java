package com.strongit.iss.entity;
import java.io.Serializable;

public class ViewBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    /**
     *  项目个数
     */
	private String cnt;
    /**
     *  申报日期
     */
	private String reportDate;
    /**
     * 查询的项
     */
    private  String itemName;

    /**
     *  查询项对应的Code
     */
    private String itemCode="";

    /**
     *  总投资
     */   
    private String investMon1;
    
    private String investMon2;
    
    private String investMon3;
    
    private String investMon4;
    
    private String investMon5;
    
    private String investMon6;
    
    private String investMon7;
    
    //开工项目数
    private String startCnt;
    
    //竣工项目数
    private String endCnt;

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

	public String getInvestMon1() {
		return investMon1;
	}

	public void setInvestMon1(String investMon1) {
		this.investMon1 = investMon1;
	}

	public String getInvestMon2() {
		return investMon2;
	}

	public void setInvestMon2(String investMon2) {
		this.investMon2 = investMon2;
	}

	public String getInvestMon3() {
		return investMon3;
	}

	public void setInvestMon3(String investMon3) {
		this.investMon3 = investMon3;
	}

	public String getInvestMon4() {
		return investMon4;
	}

	public void setInvestMon4(String investMon4) {
		this.investMon4 = investMon4;
	}

	public String getStartCnt() {
		return startCnt;
	}

	public void setStartCnt(String startCnt) {
		this.startCnt = startCnt;
	}

	public String getEndCnt() {
		return endCnt;
	}

	public void setEndCnt(String endCnt) {
		this.endCnt = endCnt;
	}

	public String getInvestMon5() {
		return investMon5;
	}

	public void setInvestMon5(String investMon5) {
		this.investMon5 = investMon5;
	}

	public String getInvestMon6() {
		return investMon6;
	}

	public void setInvestMon6(String investMon6) {
		this.investMon6 = investMon6;
	}

	public String getInvestMon7() {
		return investMon7;
	}

	public void setInvestMon7(String investMon7) {
		this.investMon7 = investMon7;
	}

	public ViewBean() {
		super();
	}
	

	



	/**
	 * 政府精细化管理
	 * @orderBy 
	 * @return
	 * @author 周朋
	 * @Date 2016年10月22日下午3:38:51
	 */
	public ViewBean(String cnt, String itemName, String itemCode,
			String investMon1, String investMon2, String investMon3,
			String investMon4, String investMon5, String investMon6,
			String startCnt, String endCnt) {
		super();
		this.cnt = cnt;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.investMon1 = investMon1;
		this.investMon2 = investMon2;
		this.investMon3 = investMon3;
		this.investMon4 = investMon4;
		this.investMon5 = investMon5;
		this.investMon6 = investMon6;
		this.startCnt = startCnt;
		this.endCnt = endCnt;
	}

	/**
	 * 中央预算内申报报表数据类型
	 * @orderBy 
	 * @return
	 * @author wuwei
	 * @Date 2016年10月21日下午3:38:51
	 */
	public ViewBean(String itemCode, String itemName,String cnt,
			String investMon1,String investMon2,String investMon3) {
		super();				
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.cnt = cnt;
		//总投资
		this.investMon1 = investMon1;
		//中央预算内总投资
		this.investMon2 = investMon2;
		//申请资金
		this.investMon3 = investMon3;
	}
	
	
	/**
	 * 五年项目储备
	 * @orderBy 
	 * @return
	 * @author zhoupeng
	 * @Date 2016年10月22日下午3:01:51
	 */
	public ViewBean(String cnt, String itemName, String itemCode,
			String investMon1) {
		super();
		this.cnt = cnt;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.investMon1 = investMon1;
	}
	
	
	/**
	 * 三年滚动计划
	 * @orderBy 
	 * @return
	 * @author zhoupeng
	 * @Date 2016年10月22日下午3:01:51
	 */
	public ViewBean(String cnt, String itemName, String itemCode,
			String investMon1, String investMon2, String investMon3,
			String investMon4, String investMon5, String investMon6,
			String investMon7) {
		super();
		this.cnt = cnt;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.investMon1 = investMon1;
		this.investMon2 = investMon2;
		this.investMon3 = investMon3;
		this.investMon4 = investMon4;
		this.investMon5 = investMon5;
		this.investMon6 = investMon6;
		this.investMon7 = investMon7;
	}
	
	/**
	 * 专项建设基金
	 * @orderBy 
	 * @return
	 * @author zhoupeng
	 * @Date 2016年10月22日下午3:01:51
	 */
	public ViewBean(String cnt, String itemName, String itemCode,
			String investMon1, String investMon2, String investMon3,
			String investMon4) {
		super();
		this.cnt = cnt;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.investMon1 = investMon1;
		this.investMon2 = investMon2;
		this.investMon3 = investMon3;
		this.investMon4 = investMon4;
	}
	
	
}
