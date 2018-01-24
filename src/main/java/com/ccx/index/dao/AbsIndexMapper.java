package com.ccx.index.dao;

import com.ccx.index.model.AbsIndex;
import com.ccx.util.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AbsIndexMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsIndex record);

    int insertSelective(AbsIndex record);

    AbsIndex selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AbsIndex record);

    int updateByPrimaryKey(AbsIndex record);

    //通过因素id查指标list
    List<AbsIndex> getListByElementId(Integer id);

    //通过因素id删除指标
    void deleteByElementId(Integer id);

    //获取带分页list
    List<AbsIndex> getPageList(Page<AbsIndex> page);

    //查行业内的指标编号是否重复
    Integer checkIndexCode(@Param("modelId") Integer modelId, @Param("indexCode") String indexCode);

    //通过公式id查指标
    AbsIndex getByFormulaId(Integer id);

    //查找公式在指标的引用
    List<AbsIndex> findIndexUsage(Integer formulId);

    //删除公式同时删除指标中对公式的引用
    void updateFormulaUsage(Integer id);
    
    //查定性指标
  	List<AbsIndex> findNatureIndexByElementIds(List<Integer> elementIds);
}