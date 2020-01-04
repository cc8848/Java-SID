package com.quaint.demo.es.spi;

import com.quaint.demo.es.dto.DemoTestDto;
import com.quaint.demo.es.dto.base.BaseIdDto;
import com.quaint.demo.es.service.DemoTestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qi cong
 * @date 2019-12-26 12:01
 */
@Api(tags = {"ES测试demoApi"})
@RestController
@RequestMapping("/demo/test")
public class DemoTestSpi {

    @Autowired
    DemoTestService demoTestService;

    @PostMapping("/add/index")
    public Boolean addIndex(){
        return demoTestService.addIndex();
    }

    @PostMapping("/add/document")
    public Boolean addDocument(@RequestBody DemoTestDto dto) {
        return demoTestService.addDocument(dto);
    }

    @GetMapping("/init/data")
    public Boolean esInitData(){
        return demoTestService.initIndexData();
    }

    @PostMapping("/search/item/list")
    public DemoTestDto searchItemList(@RequestBody DemoTestDto.Param param){
        return demoTestService.searchItemList(param);
    }


    @DeleteMapping("/del/document")
    public Boolean delDocumentById(@RequestBody BaseIdDto idDto){
        return demoTestService.delDocumentById(idDto.getId());
    }


}
