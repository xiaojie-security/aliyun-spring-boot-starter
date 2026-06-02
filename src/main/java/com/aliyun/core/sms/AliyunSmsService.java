package com.aliyun.core.sms;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.properties.pojo.AliyunSms;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;

/**
 * 阿里云短信服务
 */
@Slf4j
@RequiredArgsConstructor
public class AliyunSmsService implements InitializingBean {

    private final AliyunSms aliyunSms;
    private final com.aliyun.dysmsapi20170525.Client client;
    private String DEFAULT_SIGN_NAME;

    /**
     * 验证码长度
     */
    public static final int CODE_LENGTH = 6;

    /**
     * 验证码有效时间（单位：分钟）
     */
    public static final int VALID_MINUTES = 5;

    @Override
    public void afterPropertiesSet() throws Exception {
        DEFAULT_SIGN_NAME = aliyunSms.getDefaultSignName();
    }


    /**
     * 验证码短信
     * @param phoneNumber 手机号
     * @param templateCodeKey 模板编号
     * @return 是否发送成功
     */
    public String sendSmsCaptcha(String phoneNumber, String templateCodeKey)  {
        String code = String.valueOf(RandomUtil.randomInt(CODE_LENGTH));
        boolean sendSmsCode = sendSmsCode(DEFAULT_SIGN_NAME, phoneNumber, templateCodeKey,
                new LoginTemplateParams(
                        code,
                        String.valueOf(VALID_MINUTES)
                ));
        return sendSmsCode ? code : null;
    }

    /**
     * 推送短信通知
     * @param phoneNumber 手机号
     * @param templateCodeKey 模板编号key
     * @param content 推送内容
     * @return 是否发送成功
     */
    public boolean sendSmsNotifications(String phoneNumber, String templateCodeKey, String content) {
        PushNotifyTemplateParams params = new PushNotifyTemplateParams(content);
        return sendSmsCode(DEFAULT_SIGN_NAME, phoneNumber, templateCodeKey, params);
    }


    /**
     * 发送短信
     * @param phoneNumber 手机号
     * @param templateCodeKey 模板编号key
     * @param templateParam 模板参数
     * @return 是否发送成功
     */
    public boolean sendSmsCode(String phoneNumber, String templateCodeKey, TemplateParams templateParam)  {
        return sendSmsCode(DEFAULT_SIGN_NAME, phoneNumber, templateCodeKey, templateParam);
    }

    /**
     * 发送短信
     * @param signName 签名
     * @param phoneNumber 手机号
     * @param templateCodeKey 短信模板 Code key
     * @param templateParam 短信模板变量对应的实际值，JSON 字符串。
     * @return 验证码
     */
    public boolean sendSmsCode(String signName, String phoneNumber, String templateCodeKey, TemplateParams templateParam)  {
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
                    .setTemplateCode(aliyunSms.getTemplateCode(templateCodeKey))
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


    public abstract static class TemplateParams { }

    /**
     * 登录场景短信模板参数
     * 用于封装发送登录验证码时所需的参数信息
     */
    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class LoginTemplateParams extends TemplateParams {
        /**
         * 验证码
         */
        private String code;

        /**
         * 有效时间
         */
        private String min;
    }

    /**
     * 修改手机号场景短信模板参数
     * 用于封装发送修改手机号验证码时所需的参数信息
     */
    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class ChangePhoneTemplateParams extends TemplateParams {
        /**
         * 验证码
         */
        private String code;

        /**
         * 有效时间
         */
        private String min;
    }

    /**
     * 推送场景短信模板参数
     * 用于封装发送推送消息时所需的内容参数
     */
    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class PushNotifyTemplateParams extends TemplateParams {
        /**
         * 推送内容
         */
        private String content;
    }

}
