package com.quaint.demo.es.database;

/**
 * 不想链接数据库的, 可以实现本接口 临时模拟数据库
 * @author quaint
 * @date 2020-01-03 17:10
 */
public interface VirtualDataBase<T> {

    /**
     * 初始化数据
     * @param num num
     */
    void initData(int num);

    /**
     * 传入id  根据id 生成 随机po
     * @param id id
     * @return po
     */
    T getRandomPo(int id);

}
