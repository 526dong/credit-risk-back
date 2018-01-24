package com.ccx.index.dao;

import com.ccx.index.model.AbsIndexModel;
import com.ccx.util.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AbsIndexModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsIndexModel record);

    int insertSelective(AbsIndexModel record);

    AbsIndexModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AbsIndexModel record);

    int updateByPrimaryKey(AbsIndexModel record);

    //分页
    List<AbsIndexModel> getPageList(Page<AbsIndexModel> page);

    //通过名字模糊查询
    List<AbsIndexModel> getByLikeName(String name);

    //查评分卡
    List<AbsIndexModel> selectModeListByReportType(Integer oldTypeId);

    //过期
    void expiredMode(AbsIndexModel model);

    void deleteMatch(Integer typeId);
}