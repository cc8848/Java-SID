package com.quaint.demo.es.spi;

import com.quaint.demo.es.service.ItemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qi cong
 * @date 2019-12-26 12:01
 */
@Api(tags = {"ES测试demoApi"})
@RestController
@RequestMapping("/demo")
public class ItemDemoSpi {

    @Autowired
    ItemService itemService;

    @PostMapping("/add/index")
    public Boolean esTemplateAddIndex(){
        return itemService.esTemplateAddIndex();
    }

    @PostMapping("/add/document")
    public Boolean esRepositoryAddDocument(){
        return itemService.esRepositoryAddDocument();
    }


}
