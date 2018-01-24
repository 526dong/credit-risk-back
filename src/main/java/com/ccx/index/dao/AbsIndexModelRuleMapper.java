package com.ccx.index.dao;

import com.ccx.index.model.AbsIndexModelRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AbsIndexModelRuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsIndexModelRule record);

    int insertSelective(AbsIndexModelRule record);

    AbsIndexModelRule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AbsIndexModelRule record);

    int updateByPrimaryKey(AbsIndexModelRule record);

    //批量插入
    void insertList(List<AbsIndexModelRule> modelRuleList);

    //根据模型id删除rule
    void deleteByModelId(@Param("modelId") Integer modelId);
}