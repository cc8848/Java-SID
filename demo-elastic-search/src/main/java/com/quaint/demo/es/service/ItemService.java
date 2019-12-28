package com.quaint.demo.es.service;

import com.quaint.demo.es.dto.ItemDTO;
import com.quaint.demo.es.index.ItemIndex;

import java.util.List;

/**
 * @author qi cong
 * @date 2019-12-26 11:33
 */
public interface ItemService {


    /**
     * template 增加索引 demo
     * @return bool
     */
    Boolean esTemplateAddIndex();

    /**
     * repository 增加文档记录
     * @return bool
     */
    Boolean esRepositoryAddDocument(ItemDTO dto);

    /**
     * 初始化索引数据
     * @return bool
     */
    Boolean initIndexData();

    /**
     * 通过标题搜索内容
     * @param param param
     * @return list
     */
    ItemDTO searchItemList(ItemDTO.Param param);


}
