package com.quaint.demo.wechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quaint.demo.wechat.po.DemoWxPayOrderPO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author quaint
 * @date 2020-01-15 13:57
 */
public interface DemoWxPayOrderMapper extends BaseMapper<DemoWxPayOrderPO> {


    /**
     * 根据orderCode 查询支付单
     * @param orderCode
     * @return
     */
    @Select("select * from demo_wx_pay_order where order_code = #{orderCode} and valid = 1")
    List<DemoWxPayOrderPO> selectPayOrderList(String orderCode);

    /**
     * 根据orderCode 删除过期的待支付订单
     * @param orderCode
     * @return
     */
    Integer deleteExpireToPayOrder(String orderCode);

}
