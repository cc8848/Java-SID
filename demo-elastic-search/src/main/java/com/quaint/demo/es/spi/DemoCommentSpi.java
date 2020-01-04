package com.quaint.demo.es.spi;

import com.quaint.demo.es.service.DemoCommentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author quaint
 * @date 2020-01-04 14:04
 */
@Api(tags = {"ES-DemoCommentApi"})
@RestController
@RequestMapping("/demo/comment")
public class DemoCommentSpi {

    @Autowired
    DemoCommentService demoCommentService;

    @GetMapping("/refresh")
    void refreshAll(){
        demoCommentService.refreshAll();
    }

}
