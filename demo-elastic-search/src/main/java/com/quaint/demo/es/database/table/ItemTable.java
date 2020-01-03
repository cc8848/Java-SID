package com.quaint.demo.es.database.table;

import com.quaint.demo.es.database.AbstractVirtualDataBase;
import com.quaint.demo.es.po.ItemPo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * es demo 项目,没有整合数据库 时 可以采用本代码, 虚拟数据库 数据
 * @author quaint
 * @date 2020-01-03 15:25
 */
@Component
public class ItemTable extends AbstractVirtualDataBase<ItemPo> {

    public ItemTable() {
        initData(20);
    }

    public List<ItemPo> getPoList(){
        return poList;
    }


    @Override
    public ItemPo getRandomPo(int id) {
        ItemPo itemPo = new ItemPo();
        itemPo.setId((long)id);

        int titleCount = 3;
        if (id % titleCount==0){
            itemPo.setTitle("广寒宫,饭思思,歌曲");
        } else if (id % titleCount==1){
            itemPo.setTitle("你的酒馆对我打了烊,陈雪凝,歌曲");
        } else {
            itemPo.setTitle("清明上河图,洛天依,歌曲");
        }
        itemPo.setCategory(id % 2>0?"类型一":"类型二");
        itemPo.setScore(9.9 * id);
        itemPo.setPageViews(666L + id);
        itemPo.setLike(id % 2 > 0);
        itemPo.setImages("image/url/"+id);
        // 控制一部分时间相同
        itemPo.setCreateTime(LocalDateTime.now().withHour(id % 3+10).withNano(0));
        return itemPo;
    }


}
