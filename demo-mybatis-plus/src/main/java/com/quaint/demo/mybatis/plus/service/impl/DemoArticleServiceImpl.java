package com.quaint.demo.mybatis.plus.service.impl;

import com.quaint.demo.mybatis.plus.po.DemoArticlePO;
import com.quaint.demo.mybatis.plus.mapper.DemoArticleMapper;
import com.quaint.demo.mybatis.plus.service.IDemoArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author quaint
 * @since 2019-12-30
 */
@Service
public class DemoArticleServiceImpl extends ServiceImpl<DemoArticleMapper, DemoArticlePO> implements IDemoArticleService {

}
