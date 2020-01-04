package com.quaint.demo.es.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quaint.demo.es.config.annotation.DataChange;
import com.quaint.demo.es.po.DemoArticlePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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

    /**
     * 分页查询数据
     * @param startNum num
     * @param pageSize size
     * @return list
     */
    List<DemoArticlePO> getDemoArticleListByPage(@Param("startNum") Integer startNum,@Param("pageSize") Integer pageSize);

    /**
     * 给对应id 增加 浏览量
     * @param id id
     * @return 影响行数
     */
    @Update("update demo_article set page_views = page_views + 1 where id = #{id}")
    @DataChange
    Integer addPageViews(Integer id);

}
