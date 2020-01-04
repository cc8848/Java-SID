package com.quaint.demo.es.search.handler;

import com.quaint.demo.es.enums.DataType;
import com.quaint.demo.es.search.handler.abst.AbstractDataChangeHandler;
import com.quaint.demo.es.search.index.DemoArticleIndex;
import com.quaint.demo.es.mapper.DemoArticleMapper;
import com.quaint.demo.es.po.DemoArticlePO;
import com.quaint.demo.es.search.repository.DemoArticleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author quaint
 * @date 2020-01-04 12:50
 */
@Component
public class DemoArticleHandler extends AbstractDataChangeHandler<Integer> {

    @Autowired
    DemoArticleMapper demoArticleMapper;

    @Autowired
    DemoArticleRepository demoArticleRepository;

    @Override
    public void refresh() {
        demoArticleRepository.deleteAll();
        // 分批 刷新数据开始 , 数据量较小, 这里3个一批
        int num = 0;
        int size = 3;
        List<DemoArticlePO> page = demoArticleMapper.getDemoArticleListByPage(0, size);
        while (!CollectionUtils.isEmpty(page)){
            // 分批处理数据
            batchUpdate(page, po -> handleChange(po.getId()));
            num++;
            page = demoArticleMapper.getDemoArticleListByPage(num*size, size);
        }
    }

    @Override
    public void handleChange(Integer pkId) {
        DemoArticlePO po = demoArticleMapper.selectById(pkId);
        if (po == null){
            demoArticleRepository.deleteById(pkId);
        } else {
            DemoArticleIndex index = new DemoArticleIndex();
            BeanUtils.copyProperties(po,index);
            demoArticleRepository.save(index);
        }
    }

    @Override
    public boolean support(DataType dataType) {
        return DataType.DEMO_ARTICLE_TYPE.equals(dataType);
    }
}
