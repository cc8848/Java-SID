package com.quaint.demo.mybatis.spi;

import com.quaint.demo.mybatis.po.DemoUserPO;
import com.quaint.demo.mybatis.service.DemoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author quaint
 * @date 2020-01-12 18:55
 */
@RestController
public class DemoUserSpi {

    @Autowired
    DemoUserService demoUserService;

    @RequestMapping("/user/{id}")
    public DemoUserPO getDemoUserPoById(@PathVariable(name = "id") Integer id){
        return demoUserService.selectByPrimaryKey(id);
    }

}
