package com.quaint.demo.es.mapper.impl;

import com.quaint.demo.es.database.table.ItemTable;
import com.quaint.demo.es.mapper.ItemMapper;
import com.quaint.demo.es.po.ItemPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * es项目,没有整合数据库相关代码, 虚拟数据库 数据
 * @author quaint
 * @date 2020-01-03 15:25
 */
@Component
public class ItemMapperImpl implements ItemMapper {

    @Autowired
    ItemTable itemTable;

    @Override
    public ItemPo getItemById(Long id) {
        List<ItemPo> poList = itemTable.getPoList();
        Optional<ItemPo> first = poList.stream().filter(po -> po.getId().equals(id)).findFirst();
        return first.orElse(null);
    }

    @Override
    public List<ItemPo> getItemList() {
        return itemTable.getPoList();
    }
}
