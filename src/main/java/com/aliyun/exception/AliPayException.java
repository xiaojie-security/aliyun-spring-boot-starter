package com.aliyun.exception;

public class AliPayException extends RuntimeException {

    public AliPayException() {
    }

    public AliPayException(String message) {
        super(message);
    }

    public final static AliPayException REQUEST_PAY_ERROR = new AliPayException("请求支付失败");

    public final static AliPayException QUERY_ERROR = new AliPayException("交易查询失败");

    public final static AliPayException REFUND_ERROR = new AliPayException("订单退款失败");

    public final static AliPayException QUERY_REFUND_ERROR = new AliPayException("查询订单退款结果失败");

    public final static AliPayException TRANSFER_ERROR = new AliPayException("转账失败");

    public final static AliPayException REQUEST_TOKEN_ERROR = new AliPayException("获取授权访问令牌异常");
    public final static AliPayException QUERY_USER_ERROR = new AliPayException("查询用户信息异常");

}
