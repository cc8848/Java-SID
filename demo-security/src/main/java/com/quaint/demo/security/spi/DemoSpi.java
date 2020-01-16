package com.quaint.demo.security.spi;

import com.quaint.demo.security.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author quaint
 * @date 2020-01-16 11:10
 */
@Controller
public class DemoSpi {

    @Autowired
    DemoService demoService;

    @GetMapping("/user/demo")
    public String getDemo(Model model){
        model.addAttribute("demo",demoService.getHelloWorld());
        return "user";
    }

}
