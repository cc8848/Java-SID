package com.quaint.demo.es.spi;

import com.quaint.demo.es.dto.article.DemoArticleDto;
import com.quaint.demo.es.service.DemoArticleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author quaint
 * @date 2020-01-04 13:59
 */
@Api(tags = {"ES-DemoArticleApi"})
@RestController
@RequestMapping("/demo")
public class DemoArticleSpi {

    @Autowired
    DemoArticleService demoArticleService;

    @PostMapping("/article/list")
    DemoArticleDto getDemoArticleList(@RequestBody DemoArticleDto.Param param){
        return demoArticleService.getDemoArticleList(param);
    }

}
