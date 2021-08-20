package io.framework.toPay;

import io.framework.common.common.dto.toPay.QueryHisPayDTO;
import io.framework.common.common.vo.QueryHisPayVO;

import java.util.List;

/**
 * 支付相关方法
 */
public interface ToPayService {

    /**
     * 查询历史订单
     */
    List<QueryHisPayVO> queryHisPay(QueryHisPayDTO queryHisPay);

    /**
     * 获取签名
     */
    String getSign();

}
