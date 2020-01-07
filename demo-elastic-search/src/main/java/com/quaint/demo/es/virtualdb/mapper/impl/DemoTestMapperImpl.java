package com.quaint.demo.es.virtualdb.mapper.impl;

import com.quaint.demo.es.virtualdb.table.DemoTestTable;
import com.quaint.demo.es.virtualdb.mapper.DemoTestMapper;
import com.quaint.demo.es.po.DemoTestPo;
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
public class DemoTestMapperImpl implements DemoTestMapper {

    @Autowired
    DemoTestTable demoTestTable;

    @Override
    public DemoTestPo getItemById(Long id) {
        List<DemoTestPo> poList = demoTestTable.getPoList();
        Optional<DemoTestPo> first = poList.stream().filter(po -> po.getId().equals(id)).findFirst();
        return first.orElse(null);
    }

    @Override
    public List<DemoTestPo> getItemList() {
        return demoTestTable.getPoList();
    }
}
