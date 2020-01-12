package com.quaint.demo.mybatis.service;

import com.quaint.demo.mybatis.po.DemoUserPO;

/**
 * @author quaint
 * @date 2020-01-12 18:53
 */
public interface DemoUserService {

    DemoUserPO selectByPrimaryKey(Integer id);

}
