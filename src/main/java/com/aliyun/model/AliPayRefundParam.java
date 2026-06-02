package com.aliyun.model;


import com.alipay.v3.model.OpenApiRoyaltyDetailInfoPojo;
import com.alipay.v3.model.RefundGoodsDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
/**
 * 阿里云支付退款参数类
 *
 * <p>用于封装支付宝退款请求的相关参数信息</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AliPayRefundParam {

    /**
     * 退款金额
     * <p>需要退款的金额，该金额不能大于订单金额，单位为元，支持两位小数</p>
     */
    private BigDecimal refundAmount;

    /**
     * 商户订单号
     * <p>订单支付时传入的商户订单号，和支付宝交易号不能同时为空</p>
     */
    private String outTradeNo;

    /**
     * 支付宝交易号
     * <p>和商户订单号不能同时为空，tradeNo和outTradeNo如果同时存在优先取tradeNo</p>
     */
    private String tradeNo;

    /**
     * 退款原因说明
     * <p>商家自定义，将在商户和用户的PC退款账单详情中展示</p>
     */
    private String refundReason;

    /**
     * 退款请求号
     * <p>标识一次退款请求，需要保证在交易号下唯一，如需部分退款，则此参数必传。如果在退款请求时未传入，则该值为创建交易时的商户订单号</p>
     */
    private String outRequestNo;

    public AliPayRefundParam(BigDecimal refundAmount, String outTradeNo, String tradeNo, String refundReason) {
        this.refundAmount = refundAmount;
        this.outTradeNo = outTradeNo;
        this.tradeNo = tradeNo;
        this.refundReason = refundReason;
    }



    public AliPayRefundParam(BigDecimal refundAmount, String outTradeNo, String tradeNo, String refundReason, String outRequestNo) {
        this.refundAmount = refundAmount;
        this.outTradeNo = outTradeNo;
        this.tradeNo = tradeNo;
        this.refundReason = refundReason;
        this.outRequestNo = outRequestNo;
    }

    /**
     * 退款包含的商品列表信息
     */
    private RefundGoodsDetail[] refundGoodsDetail;

    /**
     * 退分账明细信息
     */
    private OpenApiRoyaltyDetailInfoPojo[] refundRoyaltyParameters;

    /**
     * 查询选项
     * <p>商户通过上送该参数来定制同步需要额外返回的信息字段，数组格式</p>
     * <p>枚举值：</p>
     * <ul>
     *     <li>refund_detail_item_list - 本次退款使用的资金渠道</li>
     *     <li>deposit_back_info - 银行卡冲退信息</li>
     *     <li>refund_voucher_detail_list - 本次退款退的券信息</li>
     * </ul>
     * <p>示例值：["refund_detail_item_list"]</p>
     */
    private String[] queryOptions;

    /**
     * 关联结算确认号
     * <p>针对账期交易，在确认结算后退款的话，需要指定确认结算时的结算单号</p>
     */
    private String relatedSettleConfirmNo;
}

