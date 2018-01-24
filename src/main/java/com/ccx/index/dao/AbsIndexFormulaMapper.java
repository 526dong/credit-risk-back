package com.ccx.index.dao;

import com.ccx.index.model.AbsIndexFormula;
import com.ccx.util.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AbsIndexFormulaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsIndexFormula record);

    int insertSelective(AbsIndexFormula record);

    AbsIndexFormula selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AbsIndexFormula record);

    int updateByPrimaryKey(AbsIndexFormula record);

    //名字模糊查询
    List<AbsIndexFormula> getByLikeName(@Param("formulaName") String formulaName, @Param("reportTypeId") Integer reportTypeId);

    //获取带分页list
    List<AbsIndexFormula> getPageList(Page<AbsIndexFormula> page);

    //获取带分页withoutlist
    List<AbsIndexFormula> getPageWithoutFormulaList(Page<AbsIndexFormula> page);

    //通过名字和年份查询
    AbsIndexFormula getbyParentIdAndYear(@Param("parentId") Integer parentId, @Param("year") int year);

    //通过名字和年份查询
    AbsIndexFormula getbyNameAndYear(@Param("formulaName") String formulaName, @Param("year") int year);

    //删除子公式
    void deleteByParentId(Integer id);

    //通过模板id查询公式
    List<AbsIndexFormula> selectFormulaListByReportType(Integer oldTypeId);

    //过期
    void expireFormula(AbsIndexFormula indexFormula);

    //批量插入
    void insertList(List<AbsIndexFormula> newChildFormulaList);

    //查询名字是否已经存在
    List<Integer> selectIdByName(String name);
}