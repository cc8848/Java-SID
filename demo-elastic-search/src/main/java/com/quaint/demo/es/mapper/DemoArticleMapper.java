package com.quaint.demo.es.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quaint.demo.es.po.DemoArticlePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author quaint
 * @since 2019-12-30
 */
@Mapper
public interface DemoArticleMapper extends BaseMapper<DemoArticlePO> {

}
