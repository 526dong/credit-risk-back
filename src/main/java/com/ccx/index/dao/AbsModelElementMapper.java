package com.ccx.index.dao;

import com.ccx.index.model.AbsModelElement;
import com.ccx.util.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AbsModelElementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsModelElement record);

    int insertSelective(AbsModelElement record);

    AbsModelElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AbsModelElement record);

    int updateByPrimaryKey(AbsModelElement record);

    //分页list
    List<AbsModelElement> getPageList(Page<AbsModelElement> page);

    //更新状态
    void UpdateState(@Param("id") Integer id, @Param("state") Integer state);

    //检测编号
    int checkCode(@Param("modelId") Integer backId, @Param("code") String code);
    
    List<AbsModelElement> getListByModelId(Integer modelId);
}