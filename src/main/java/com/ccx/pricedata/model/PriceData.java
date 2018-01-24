package com.ccx.pricedata.model;

import java.util.Date;

import com.ccx.util.DateUtils;

/**
 * 定价数据管理
 * @author xzd
 * @date 2017/7/13
 */
public class PriceData {
    private Integer id;

    /*评级结果id*/
    private Integer rateResult;
    
    /*评级结果*/
    private String rateResultName;

    /*保证金*/
    private String cashDepositMin;

    /*保证金*/
    private String cashDepositMax;

    /*手续费*/
    private String handlingChargeMin;

    /*手续费*/
    private String handlingChargeMax;

    /*利率*/
    private String interestRateMin;

    /*利率*/
    private String interestRateMax;

    /*IRR*/
    private String irrMin;

    /*IRR*/
    private String irrMax;
    
    /*创建人*/
    private String creatorName;

    /*创建时间*/
    private Date createTime;

    private Integer institutionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRateResult() {
        return rateResult;
    }

    public void setRateResult(Integer rateResult) {
        this.rateResult = rateResult;
    }

    public String getRateResultName() {
		return rateResultName;
	}

	public void setRateResultName(String rateResultName) {
		this.rateResultName = rateResultName;
	}

	public String getCashDepositMin() {
		return cashDepositMin;
	}

	public void setCashDepositMin(String cashDepositMin) {
		this.cashDepositMin = cashDepositMin;
	}

	public String getCashDepositMax() {
		return cashDepositMax;
	}

	public void setCashDepositMax(String cashDepositMax) {
		this.cashDepositMax = cashDepositMax;
	}

	public String getHandlingChargeMin() {
		return handlingChargeMin;
	}

	public void setHandlingChargeMin(String handlingChargeMin) {
		this.handlingChargeMin = handlingChargeMin;
	}

	public String getHandlingChargeMax() {
		return handlingChargeMax;
	}

	public void setHandlingChargeMax(String handlingChargeMax) {
		this.handlingChargeMax = handlingChargeMax;
	}

	public String getInterestRateMin() {
		return interestRateMin;
	}

	public void setInterestRateMin(String interestRateMin) {
		this.interestRateMin = interestRateMin;
	}

	public String getInterestRateMax() {
		return interestRateMax;
	}

	public void setInterestRateMax(String interestRateMax) {
		this.interestRateMax = interestRateMax;
	}

	public String getIrrMin() {
		return irrMin;
	}

	public void setIrrMin(String irrMin) {
		this.irrMin = irrMin;
	}

	public String getIrrMax() {
		return irrMax;
	}

	public void setIrrMax(String irrMax) {
		this.irrMax = irrMax;
	}
	
    public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreateTime() throws Exception {
		if (createTime != null) {
     		return DateUtils.formatDate(createTime);
 		}else{
 			return null;
 		}
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

}