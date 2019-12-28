package com.quaint.demo.generator.dao;

import com.quaint.demo.generator.po.DemoUserPO;

public interface DemoUserPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DemoUserPO record);

    int insertSelective(DemoUserPO record);

    DemoUserPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DemoUserPO record);

    int updateByPrimaryKey(DemoUserPO record);
}