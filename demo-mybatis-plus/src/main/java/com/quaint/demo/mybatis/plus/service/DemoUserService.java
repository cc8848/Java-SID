package com.quaint.demo.mybatis.plus.service;

import com.quaint.demo.mybatis.plus.dto.DemoUserDTO;

/**
 * @author qi cong
 * @date 2019-12-25 16:31
 */
public interface DemoUserService {


    /**
     * 根据id 获取用户信息 demo代码
     * @param id id
     * @return userInfo
     */
    DemoUserDTO getUserInfoById(Integer id);

    /**
     * 增加用户信息
     * @param reqDto dto
     * @return boolean
     */
    DemoUserDTO addUserInfo(DemoUserDTO reqDto);

}
