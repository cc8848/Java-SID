package com.quaint.demo.es.spi;

import com.quaint.demo.es.dto.article.AddDemoArticleReqDto;
import com.quaint.demo.es.dto.article.DemoArticleDto;
import com.quaint.demo.es.dto.base.BaseIdDto;
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
@RequestMapping("/demo/article")
public class DemoArticleSpi {

    @Autowired
    DemoArticleService demoArticleService;

    @PostMapping("/list")
    DemoArticleDto getDemoArticleList(@RequestBody DemoArticleDto.Param param){
        return demoArticleService.getDemoArticleList(param);
    }

    @PostMapping("/add")
    Boolean addDemoArticle(@RequestBody AddDemoArticleReqDto reqDto){
        return demoArticleService.addDemoArticle(reqDto);
    }

    @PostMapping("/id")
    DemoArticleDto.Result getDemoArticleById(@RequestBody BaseIdDto idDto){
        return demoArticleService.getDemoArticleById(idDto.getId().intValue());
    }


}
