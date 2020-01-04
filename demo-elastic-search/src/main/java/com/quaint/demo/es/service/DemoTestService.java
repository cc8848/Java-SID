package com.quaint.demo.es.service;

import com.quaint.demo.es.dto.DemoTestDto;

/**
 * @author qi cong
 * @date 2019-12-26 11:33
 */
public interface DemoTestService {


    /**
     * template 增加索引 demo
     * @return bool
     */
    Boolean addIndex();

    /**
     * repository 增加文档记录
     * @param dto dto
     * @return bool
     */
    Boolean addDocument(DemoTestDto dto);

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
    DemoTestDto searchItemList(DemoTestDto.Param param);

    /**
     * 通过文档id 删除 对应文档
     * @param id id
     * @return boolean
     */
    Boolean delDocumentById(Long id);

}
