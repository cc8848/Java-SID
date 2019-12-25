package com.quaint.demo.mybatis.plus.controller;

import com.quaint.demo.mybatis.plus.dto.DemoUserDTO;
import com.quaint.demo.mybatis.plus.dto.base.BaseResponse;
import com.quaint.demo.mybatis.plus.dto.base.ReqById;
import com.quaint.demo.mybatis.plus.service.DemoUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qi cong
 * @date 2019-12-25 16:50
 */
@Api(tags = {"用户信息"})
@RestController
@RequestMapping("/demo")
public class DemoUserController {

    @Autowired
    DemoUserService demoUserService;

    @GetMapping("/user")
    BaseResponse<DemoUserDTO> getUserInfoById(ReqById req){
        return new BaseResponse<>(demoUserService.getUserInfoById(req.getId()));
    }

    @PostMapping("/user")
    BaseResponse<DemoUserDTO> addUserInfo(@RequestBody DemoUserDTO req){
        return new BaseResponse<>(demoUserService.addUserInfo(req));
    }


}
