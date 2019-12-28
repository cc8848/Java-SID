package com.quaint.demo.mybatis.plus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quaint.demo.mybatis.plus.po.DemoUserPO;
import org.apache.ibatis.annotations.Param;

/**
 * @author qi cong
 * @date 2019-12-25 16:30
 */
public interface DemoUserMapper extends BaseMapper<DemoUserPO> {

    /**
     * 根据name 查询
     * @param name name
     * @return po
     */
    DemoUserPO getUserByName(@Param("name") String name);


}
