package com.quaint.demo.es.event;

import com.quaint.demo.es.enums.DataType;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author quaint
 * @date 2020-01-03 22:42
 */
@Getter
public class DataChangeEvent<T> {

    private DataType dataType;

    private T data;

    public DataChangeEvent(DataType dataType, T data) {
        this.dataType = dataType;
        this.data = data;
    }

    public void publish(){
        EventPublisher.publish(this);
    }

    public static <T> DataChangeEventComposite add(DataType dataType, T data){
        DataChangeEventComposite dataChangeEventComposite = new DataChangeEventComposite();
        dataChangeEventComposite.add(dataType,data);
        return dataChangeEventComposite;
    }

    /**
     * 事件组合
     */
    public static class DataChangeEventComposite{

        private List<DataChangeEvent> dataChangeEventList;

        public <T> DataChangeEventComposite add(DataType dataType, T data){

            if (CollectionUtils.isEmpty(dataChangeEventList)){
                dataChangeEventList = new ArrayList<>();
            }
            dataChangeEventList.add(new DataChangeEvent(dataType,data));
            return this;
        }

        public void publish(){
            if (!CollectionUtils.isEmpty(dataChangeEventList)){
                dataChangeEventList.forEach(DataChangeEvent::publish);
            }
        }


    }

}
