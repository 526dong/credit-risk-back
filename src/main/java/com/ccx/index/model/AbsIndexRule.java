package com.ccx.index.model;


import java.text.DecimalFormat;

public class AbsIndexRule {

    private DecimalFormat format =  new DecimalFormat("0.#####");

    private Integer id;

    private Double valueMin;

    private String valueMinStr;

    private Double valueMax;

    private String valueMaxStr;

    private String value;

    private Integer score;

    private String degree;

    private Integer indexId;

    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValueMin() {
        return valueMin;
    }

    public void setValueMin(Double valueMin) {
        this.valueMin = valueMin;
    }

    public Double getValueMax() {
        return valueMax;
    }

    public void setValueMax(Double valueMax) {
        this.valueMax = valueMax;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree == null ? null : degree.trim();
    }

    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getValueMinStr() {
        String str = "";
        try {
            str = format.format(this.valueMin);
        } catch (Exception e) {
        }
        return str;
    }

    public String getValueMaxStr() {
        String str = "";
        try {
            str = format.format(this.valueMax);
        } catch (Exception e) {
        }
        return str;
    }
}