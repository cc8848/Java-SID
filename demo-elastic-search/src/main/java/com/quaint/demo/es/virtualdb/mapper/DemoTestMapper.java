package com.quaint.demo.es.virtualdb.mapper;

import com.quaint.demo.es.po.DemoTestPo;

import java.util.List;

/**
 * 虚拟mapper
 * @author quaint
 * @date 2020-01-03 15:23
 */
public interface DemoTestMapper {

    DemoTestPo getItemById(Long id);

    List<DemoTestPo> getItemList();

}
