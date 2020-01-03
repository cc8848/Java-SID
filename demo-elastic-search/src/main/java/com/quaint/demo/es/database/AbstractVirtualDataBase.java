package com.quaint.demo.es.database;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author quaint
 * @date 2020-01-03 17:19
 */
public abstract class AbstractVirtualDataBase<T> implements VirtualDataBase<T>{

    /**
     * po list
     */
    protected List<T> poList;

    /**
     * 初始化虚拟数据库数据
     * @param num num
     */
    @Override
    public void initData(int num) {
        clearPoList();
        for (int i = 0; i < num; i++) {
            poList.add(getRandomPo(i));
        }
    }

    /**
     * 初始化po list
     */
    void clearPoList(){
        if (CollectionUtils.isEmpty(poList)){
            poList = new ArrayList<>();
        } else {
            poList.clear();
        }
    }


}
