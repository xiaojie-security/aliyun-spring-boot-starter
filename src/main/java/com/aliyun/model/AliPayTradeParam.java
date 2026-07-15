package com.aliyun.model;

import com.alipay.api.domain.ExtUserInfo;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.domain.SubMerchant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * 支付宝扫码支付参数类
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AliPayTradeParam {
    /**
     * 默认销售产品码
     */
    public static final String DEFAULT_PRODUCT_CODE = "FAST_INSTANT_TRADE_PAY";

    /**
     * 默认集成方式
     */
    public static final String DEFAULT_INTEGRATION_TYPE = "PCWEB";

    /**
     * 默认二维码模式
     */
    public static final Integer DEFAULT_QR_PAT_MODE = 1;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 订单总金额，单位为元
     */
    private BigDecimal totalAmount;

    /**
     * 订单标题
     */
    private String subject;

    public AliPayTradeParam(String outTradeNo, BigDecimal totalAmount, String subject) {
        this.outTradeNo = outTradeNo;
        this.totalAmount = totalAmount;
        this.subject = subject;
    }

    /**
     * 销售产品码，与支付宝签约的产品码名称
     */
    private String productCode = DEFAULT_PRODUCT_CODE;

    /**
     * 二维码支付模式
     * <p>0：订单码-简约前置模式，对应 iframe 宽度不能小于600px，高度不能小于300px</p>
     * <p>1：订单码-前置模式，对应iframe 宽度不能小于 300px，高度不能小于600px</p>
     * <p>3：订单码-迷你前置模式，对应 iframe 宽度不能小于 75px，高度不能小于75px</p>
     * <p>4：订单码-可定义宽度的嵌入式二维码，商户可根据需要设定二维码的大小</p>
     * <p>跳转模式下，用户的扫码界面是由支付宝生成的，不在商户的域名下</p>
     */
    private Integer qrPayMode = DEFAULT_QR_PAT_MODE;

    /**
     * 商户自定义二维码宽度
     */
    private Integer qrcodeWidth;

    /**
     * 订单包含的商品列表信息，JSON格式
     * <p>包含字段：</p>
     * <ul>
     *     <li>goodsId - 商品的编号</li>
     *     <li>goodsName - 商品名称</li>
     *     <li>quantity - 商品数量</li>
     *     <li>price - 商品单价，单位为元</li>
     *     <li>alipayGoodsId - 支付宝定义的统一商品编号（可选）</li>
     *     <li>goodsCategory - 商品类目（可选）</li>
     *     <li>categoriesTree - 商品类目树，从商品类目根节点到叶子节点的类目id组成，类目id值使用|分割（可选）</li>
     *     <li>showUrl - 商品的展示地址（可选）</li>
     * </ul>
     */
    private GoodsDetail[] goodsDetail;

    /**
     * 订单绝对超时时间
     * <p>格式为yyyy-MM-dd HH:mm:ss，超时时间范围：1m~15d</p>
     */
    private String timeExpire = defaultTimeExpire();

    /**
     * 二级商户信息
     */
    private SubMerchant subMerchant;

    /**
     * 业务扩展参数
     */
    private ExtendParams extendParams;

    /**
     * 商户传入业务信息
     * <p>具体值要和支付宝约定，应用于安全、营销等参数直传场景，格式为JSON格式</p>
     */
    private String businessParams;

    /**
     * 优惠参数
     * <p>为 JSON 格式，仅与支付宝协商后可用</p>
     */
    private String promoParams;

    /**
     * 请求后页面的集成方式
     * <p>枚举值：</p>
     * <ul>
     *     <li>ALIAPP - 支付宝钱包内</li>
     *     <li>PCWEB - PC端访问（默认）</li>
     * </ul>
     */
    private String integrationType = DEFAULT_INTEGRATION_TYPE;

    /**
     * 请求来源地址
     * <p>如果使用ALIAPP的集成方式，用户中途取消支付会返回该地址</p>
     */
    private String requestFromUrl;

    /**
     * 商户门店编号
     * <p>指商户创建门店时输入的门店编号</p>
     */
    private String storeId;

    /**
     * 商户原始订单号
     * <p>最大长度限制 32 位</p>
     */
    private String merchantOrderNo;

    /**
     * 外部指定买家
     */
    private ExtUserInfo extUserInfo;

    /**
     * 开票信息
     */
//    private InvoiceInfo invoiceInfo;

    /**
     * 获取默认订单有效时间
     *
     * @return 当前时间30分钟后的时间字符串，格式为yyyy-MM-dd HH:mm:ss
     */
    public String defaultTimeExpire(){
        // 追加30分钟
        return LocalDateTime.now().plus(30, ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 设置订单有效时间
     *
     * @param amountToAdd 有效时间时长，单位分钟
     */
    public void setTimeExpire(long amountToAdd){
        this.timeExpire = LocalDateTime.now().plus(amountToAdd, ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

