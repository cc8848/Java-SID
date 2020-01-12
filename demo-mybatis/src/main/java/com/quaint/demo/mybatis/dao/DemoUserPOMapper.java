package com.quaint.demo.mybatis.dao;

import com.quaint.demo.mybatis.po.DemoUserPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DemoUserPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DemoUserPO record);

    int insertSelective(DemoUserPO record);

    DemoUserPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DemoUserPO record);

    int updateByPrimaryKey(DemoUserPO record);
}