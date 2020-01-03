package com.quaint.demo.es.mapper;

import com.quaint.demo.es.po.ItemPo;

import java.util.List;

/**
 * 虚拟mapper
 * @author quaint
 * @date 2020-01-03 15:23
 */
public interface ItemMapper {

    ItemPo getItemById(Long id);

    List<ItemPo> getItemList();

}
