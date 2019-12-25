package com.quaint.demo.mybatis.plus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quaint.demo.mybatis.plus.po.DemoUserPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qi cong
 * @date 2019-12-25 16:30
 */
@Mapper
public interface DemoUserMapper extends BaseMapper<DemoUserPO> {

}
