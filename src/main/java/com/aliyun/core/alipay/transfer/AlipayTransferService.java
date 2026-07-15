package com.aliyun.core.alipay.transfer;

import com.aliyun.core.alipay.transfer.domain.AliPayBillDownloadUrlQueryParam;
import com.aliyun.core.alipay.transfer.domain.AliPayBillDownloadUrlResult;
import com.aliyun.core.alipay.transfer.domain.AliPayEreceiptApplyParam;
import com.aliyun.core.alipay.transfer.domain.AliPayEreceiptApplyResult;
import com.aliyun.core.alipay.transfer.domain.AliPayEreceiptQueryParam;
import com.aliyun.core.alipay.transfer.domain.AliPayEreceiptQueryResult;
import com.aliyun.core.alipay.transfer.domain.AliPayFundAccountQueryParam;
import com.aliyun.core.alipay.transfer.domain.AliPayFundAccountQueryResult;
import com.aliyun.core.alipay.transfer.domain.AliPayFundQuotaQueryParam;
import com.aliyun.core.alipay.transfer.domain.AliPayFundQuotaQueryResult;
import com.aliyun.core.alipay.transfer.domain.AliPayTransferParam;
import com.aliyun.core.alipay.transfer.domain.AliPayTransferQueryParam;
import com.aliyun.core.alipay.transfer.domain.AliPayTransferQueryResult;
import com.aliyun.core.alipay.transfer.domain.AliPayTransferResult;

public interface AlipayTransferService {

    /**
     * 支付宝资金账户资产查询。
     *
     * @param queryParam 查询参数
     * @return 查询结果
     */
    AliPayFundAccountQueryResult accountQuery(AliPayFundAccountQueryParam queryParam);

    /**
     * 查询转账额度。
     *
     * @param queryParam 查询参数
     * @return 查询结果
     */
    AliPayFundQuotaQueryResult quotaQuery(AliPayFundQuotaQueryParam queryParam);

    /**
     * 申请电子回单。
     *
     * @param applyParam 申请参数
     * @return 申请结果
     */
    AliPayEreceiptApplyResult applyEreceipt(AliPayEreceiptApplyParam applyParam);

    /**
     * 查询电子回单状态。
     *
     * @param queryParam 查询参数
     * @return 查询结果
     */
    AliPayEreceiptQueryResult queryEreceipt(AliPayEreceiptQueryParam queryParam);

    /**
     * 发起单笔转账。
     *
     * @param transferParam 转账参数
     * @return 转账结果
     */
    AliPayTransferResult transfer(AliPayTransferParam transferParam);

    /**
     * 查询转账单据。
     *
     * @param queryParam 查询参数
     * @return 查询结果
     */
    AliPayTransferQueryResult query(AliPayTransferQueryParam queryParam);

    /**
     * 查询账单下载地址。
     *
     * @param queryParam 查询参数
     * @return 查询结果
     */
    AliPayBillDownloadUrlResult queryBillDownloadUrl(AliPayBillDownloadUrlQueryParam queryParam);
}
