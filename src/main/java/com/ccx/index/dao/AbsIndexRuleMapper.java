package com.ccx.index.dao;

import com.ccx.index.model.AbsIndexRule;

import java.util.List;

public interface AbsIndexRuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsIndexRule record);

    int insertSelective(AbsIndexRule record);

    AbsIndexRule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AbsIndexRule record);

    int updateByPrimaryKey(AbsIndexRule record);

    //批量删除规则通过指标id
    void deleteByIndexIds(List<Integer> indexIdList);

    //批量插入规则
    void insertList(List<AbsIndexRule> indexRuleList);
}