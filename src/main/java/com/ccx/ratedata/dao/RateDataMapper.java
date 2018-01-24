package com.ccx.ratedata.dao;

import java.util.List;

import com.ccx.ratedata.model.RateData;
import com.ccx.util.page.Page;

/**
 * 评级数据管理
 * @author xzd
 * @date 2017/7/13
 */
public interface RateDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RateData record);
    
    int batchInsert(List<RateData> list);

    int insertSelective(RateData record);

    RateData selectByPrimaryKey(Integer id);

    //校验唯一-企业名称
    int findByName(String name);

    //校验唯一-统一信用代码
    int findByCreditCode(String creditCode);

    //校验唯一-组织机构代码
    int findByOrganizationCode(String organizationCode);

    //校验唯一-事证号
    int findByCertificateCode(String certificateCode);

    int updateByPrimaryKeySelective(RateData record);

    int updateByPrimaryKey(RateData record);
    
    List<RateData> findAll(Page<RateData> page);

    List<RateData> findAllRateData();
}