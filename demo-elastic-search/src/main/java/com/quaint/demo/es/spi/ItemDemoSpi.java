package com.quaint.demo.es.spi;

import com.quaint.demo.es.dto.ItemDTO;
import com.quaint.demo.es.service.ItemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Boolean esRepositoryAddDocument(@RequestBody ItemDTO dto) {
        return itemService.esRepositoryAddDocument(dto);
    }

    @GetMapping("/init/data")
    public Boolean esInitData(){
        return itemService.initIndexData();
    }

    @PostMapping("/search/item/list")
    public ItemDTO searchItemList(@RequestBody ItemDTO.Param param){
        return itemService.searchItemList(param);
    }


}
