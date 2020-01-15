package com.quaint.demo.wechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quaint.demo.wechat.po.DemoWxRefundOrderPO;
import org.apache.ibatis.annotations.Select;

/**
 * @author quaint
 * @date 2020-01-15 13:57
 */
public interface DemoWxRefundOrderMapper extends BaseMapper<DemoWxRefundOrderPO> {


    /**
     * 根据微信退款单号查询
     * @param transactionId id
     * @return po
     */
    @Select("select * from demo_wx_refund_order where transaction_id = #{transactionId} and valid = 1 limit 1")
    DemoWxRefundOrderPO selectByTransactionId(String transactionId);

}
