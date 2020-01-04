package com.quaint.demo.es.virtualdb.table;

import com.quaint.demo.es.virtualdb.AbstractVirtualDataBase;
import com.quaint.demo.es.po.DemoTestPo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * es demo 项目,没有整合数据库 时 可以采用本代码, 虚拟数据库 数据
 * @author quaint
 * @date 2020-01-03 15:25
 */
@Component
public class ItemTable extends AbstractVirtualDataBase<DemoTestPo> {

    public ItemTable() {
        initData(20);
    }

    public List<DemoTestPo> getPoList(){
        return poList;
    }


    @Override
    public DemoTestPo getRandomPo(int id) {
        DemoTestPo demoTestPo = new DemoTestPo();
        demoTestPo.setId((long)id);

        int titleCount = 3;
        if (id % titleCount==0){
            demoTestPo.setTitle("广寒宫,饭思思,歌曲");
        } else if (id % titleCount==1){
            demoTestPo.setTitle("你的酒馆对我打了烊,陈雪凝,歌曲");
        } else {
            demoTestPo.setTitle("清明上河图,洛天依,歌曲");
        }
        demoTestPo.setCategory(id % 2>0?"类型一":"类型二");
        demoTestPo.setScore(9.9 * id);
        demoTestPo.setPageViews(666L + id);
        demoTestPo.setLike(id % 2 > 0);
        demoTestPo.setImages("image/url/"+id);
        // 控制一部分时间相同
        demoTestPo.setCreateTime(LocalDateTime.now().withHour(id % 3+10).withNano(0));
        return demoTestPo;
    }


}
