package com.quaint.demo.mybatis.service.impl;

import com.quaint.demo.mybatis.dao.DemoUserPOMapper;
import com.quaint.demo.mybatis.po.DemoUserPO;
import com.quaint.demo.mybatis.service.DemoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author quaint
 * @date 2020-01-12 18:53
 */
@Service
public class DemoUserServiceImpl implements DemoUserService {

    @Autowired
    DemoUserPOMapper demoUserPoMapper;

    @Override
    public DemoUserPO selectByPrimaryKey(Integer id) {
        return demoUserPoMapper.selectByPrimaryKey(id);
    }
}
