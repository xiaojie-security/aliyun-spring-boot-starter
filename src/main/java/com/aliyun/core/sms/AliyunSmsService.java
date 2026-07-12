package com.aliyun.core.sms;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.properties.AliyunSmsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 阿里云短信服务
 */
@Slf4j
@RequiredArgsConstructor
public class AliyunSmsService {

    private final AliyunSmsProperties aliyunSmsProperties;
    private final com.aliyun.dysmsapi20170525.Client client;



    /**
     * 发送短信
     * @param phoneNumber 手机号
     * @param templateCode 模板编号
     * @param templateParam 模板参数
     * @return 是否发送成功
     */
    public boolean sendSmsCode(String phoneNumber, String templateCode, Object templateParam)  {
        return sendSmsCode(aliyunSmsProperties.getSignName(), phoneNumber, templateCode, templateParam);
    }

    /**
     * 发送短信
     * @param signName 签名
     * @param phoneNumber 手机号
     * @param templateCode 短信模板 Code key
     * @param templateParam 短信模板变量对应的实际值，JSON 字符串。
     * @return 验证码
     */
    public boolean sendSmsCode(String signName, String phoneNumber, String templateCode, Object templateParam)  {
        if(StrUtil.isEmpty(phoneNumber)) {
            log.warn("AliyunSmsService sendSmsCode 手机号不能为空");
            throw new IllegalArgumentException("手机号不能为空");
        }
        if (!PhoneUtil.isPhone(phoneNumber)) {
            log.warn("AliyunSmsService sendSmsCode 手机号格式不正确，手机号:{}", phoneNumber);
            throw new IllegalArgumentException("手机号格式不正确");
        }
        CharSequence logPhone = PhoneUtil.hideBetween(phoneNumber);

        try {
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phoneNumber)
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setTemplateParam(JSONUtil.toJsonStr(templateParam));

            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            SendSmsResponseBody body = sendSmsResponse.getBody();

            log.info("AliyunSmsService sendSmsCode phone:{} 发送短信结果：{}", logPhone, JSONUtil.toJsonStr(body));

            return Objects.equals(body.getCode(), "OK");
        } catch (Exception e) {
            log.error("AliyunSmsService sendSmsCode phone:{} 发送短信异常", logPhone, e);
            return false;
        }
    }

}
