package com.aliyun.model;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Contract;

import java.util.Map;

/**
 * 支付宝回调结果实体类
 * <p>
 * 用于封装支付宝异步回调通知的参数信息，提供便捷的字段访问方法。
 * 该类使用 Lombok 注解简化构造方法，并提供了原始参数的 Map 存储和访问方式。
 * </p>
 *
 * @author YourName
 * @version 1.0
 * @see Data
 * @see AllArgsConstructor
 */
public final class AliPayCallbackResult {

    /**
     * 支付宝回调的原始参数集合
     * <p>
     * 以 Map 形式存储支付宝回调的所有原始参数，key 为参数名，value 为参数值。
     * 可通过 {@link #get(String)} 方法访问所有回调参数。
     * </p>
     */
    private final Map<String, String> callbackResult;

    public AliPayCallbackResult(Map<String, String> callbackResult) {
        this.callbackResult = callbackResult;
        this.outTradeNo = callbackResult.getOrDefault("out_trade_no", "");
        this.tradeNo = callbackResult.getOrDefault("trade_no", "");
        this.totalAmount = callbackResult.getOrDefault("total_amount", "");
        this.receiptAmount = callbackResult.getOrDefault("receipt_amount", "");
        this.tradeStatus = callbackResult.getOrDefault("trade_status", "");
        this.appId = callbackResult.getOrDefault("app_id", "");
        this.sellerId = callbackResult.getOrDefault("seller_id", "");
    }

    /**
     * 商户订单号
     * <p>
     * 商户网站唯一订单号，由商户生成，对应支付宝异步通知中的 out_trade_no 参数。
     * </p>
     */
    private final String outTradeNo;

    /**
     * 支付宝交易号
     * <p>
     * 支付宝侧生成的唯一交易号，对应支付宝异步通知中的 trade_no 参数。
     * 用于查询、退款等后续操作。
     * </p>
     */
    private final String tradeNo;

    /**
     * 订单总金额
     * <p>
     * 订单的原始总金额，单位为元，保留两位小数。
     * 对应支付宝异步通知中的 total_amount 参数。
     * </p>
     */
    private final String totalAmount;

    /**
     * 实收金额
     * <p>
     * 买家实际支付的金额，单位为元，保留两位小数。
     * 可能扣除手续费等后与 totalAmount 有差异。
     * 对应支付宝异步通知中的 receipt_amount 参数。
     * </p>
     */
    private final String receiptAmount;

    /**
     * 交易状态
     * <p>
     * 支付宝交易状态，常见值包括：
     * <ul>
     *     <li>WAIT_BUYER_PAY - 交易创建，等待买家付款</li>
     *     <li>TRADE_CLOSED - 未付款交易超时关闭，或支付完成后全额退款</li>
     *     <li>TRADE_SUCCESS - 交易支付成功</li>
     *     <li>TRADE_FINISHED - 交易结束，不可退款</li>
     * </ul>
     * 对应支付宝异步通知中的 trade_status 参数。
     * </p>
     */
    private final String tradeStatus;

    /**
     * 开发者的应用 ID
     * <p>
     * 支付宝分配给开发者的应用 ID，对应支付宝异步通知中的 app_id 参数。
     * 用于验证回调通知的应用归属。
     * </p>
     */
    private final String appId;

    /**
     * 卖家支付宝用户号
     * <p>
     * 收款方的支付宝账号对应的唯一用户号（以 2088 开头的纯 16 位数字），
     * 对应支付宝异步通知中的 seller_id 参数。
     * 用于验证收款方是否正确。
     * </p>
     */
    private final String sellerId;

    /**
     * 根据参数名获取支付宝回调参数值
     * <p>
     * 该方法提供对 {@link #callbackResult} 中所有原始参数的访问能力，
     * 可用于获取不在本类中明确定义的扩展参数。
     * </p>
     *
     * @param paramsKey 参数名称，如 "buyer_id", "gmt_create", "fund_bill_list" 等
     * @return 参数值，如果参数不存在则返回 null
     */
    public String get(String paramsKey) {
        return callbackResult.get(paramsKey);
    }

    /**
     * 判断回调结果是否为空
     * <p>
     * 检查 {@link #callbackResult} 是否为 null 或不包含任何元素。
     * 通常用于验证回调参数是否正确接收。
     * </p>
     *
     * @return true - 回调结果为空或未包含任何参数；false - 回调结果包含有效参数
     */
    public boolean isEmpty() {
        return CollUtil.isEmpty(callbackResult);
    }


    @Contract(pure = true)
    public String getSellerId() {
        return sellerId;
    }

    @Contract(pure = true)
    public String getAppId() {
        return appId;
    }

    @Contract(pure = true)
    public String getTradeStatus() {
        return tradeStatus;
    }

    @Contract(pure = true)
    public String getReceiptAmount() {
        return receiptAmount;
    }

    @Contract(pure = true)
    public String getTotalAmount() {
        return totalAmount;
    }

    @Contract(pure = true)
    public String getTradeNo() {
        return tradeNo;
    }

    @Contract(pure = true)
    public String getOutTradeNo() {
        return outTradeNo;
    }
}
