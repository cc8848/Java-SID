package com.quaint.demo.es.handler.impl;

import com.quaint.demo.es.enums.DataType;
import com.quaint.demo.es.handler.AbstractDataChangeHandler;
import com.quaint.demo.es.index.ItemIndex;
import com.quaint.demo.es.mapper.ItemMapper;
import com.quaint.demo.es.po.ItemPo;
import com.quaint.demo.es.repository.ItemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author quaint
 * @date 2020-01-03 15:17
 */
@Component
public class ItemHandler extends AbstractDataChangeHandler<Long> {

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    ItemRepository itemRepository;

    @Override
    public void refresh() {
        itemRepository.deleteAll();
        // 应该分批处理, 作为demo 这里暂时查询全部
        List<ItemPo> itemList = itemMapper.getItemList();
        List<ItemIndex> indexList = itemList.stream().map(itemPo -> {
            ItemIndex index = new ItemIndex();
            BeanUtils.copyProperties(itemPo, index);
            return index;
        }).collect(Collectors.toList());
        itemRepository.saveAll(indexList);
    }

    @Override
    public void handleChange(Long pkId) {

        if (pkId != null){
            // 去数据库查询 数据
            ItemPo itemById = itemMapper.getItemById(pkId);
            ItemIndex itemIndex = new ItemIndex();
            BeanUtils.copyProperties(itemById,itemIndex);
            itemRepository.save(itemIndex);
        }

    }

    @Override
    public boolean support(DataType dataType) {
        return DataType.ITEM_TYPE.equals(dataType);
    }
}
