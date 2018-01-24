package com.ccx.hierarchy.model;

import java.math.BigDecimal;

public class LayerCorrelationindexBean {
    private int id;

    private int insdustryFirst;

    private int insdustrySecond;

    private String insdustryFirstName;

    private String insdustrySecondName;

    private BigDecimal indexValue;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInsdustryFirst() {
		return insdustryFirst;
	}

	public void setInsdustryFirst(int insdustryFirst) {
		this.insdustryFirst = insdustryFirst;
	}

	public int getInsdustrySecond() {
		return insdustrySecond;
	}

	public void setInsdustrySecond(int insdustrySecond) {
		this.insdustrySecond = insdustrySecond;
	}

	public String getInsdustryFirstName() {
		return insdustryFirstName;
	}

	public void setInsdustryFirstName(String insdustryFirstName) {
		this.insdustryFirstName = insdustryFirstName;
	}

	public String getInsdustrySecondName() {
		return insdustrySecondName;
	}

	public void setInsdustrySecondName(String insdustrySecondName) {
		this.insdustrySecondName = insdustrySecondName;
	}

	public BigDecimal getIndexValue() {
		return indexValue;
	}

	public void setIndexValue(BigDecimal indexValue) {
		this.indexValue = indexValue;
	}

    
}