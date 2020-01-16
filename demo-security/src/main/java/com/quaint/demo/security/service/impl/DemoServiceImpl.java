package com.quaint.demo.security.service.impl;

import com.quaint.demo.security.service.DemoService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

/**
 * @author quaint
 * @date 2020-01-16 11:14
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Override
    @Secured("ROLE_ADMIN")
    public String getHelloWorld() {
        return "hello world";
    }
}
