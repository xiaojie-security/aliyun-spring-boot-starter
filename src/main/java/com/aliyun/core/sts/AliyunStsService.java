package com.aliyun.core.sts;

import com.aliyun.model.AliyunStsSecurityCredential;
import com.aliyun.properties.pojo.AliyunSts;
import com.aliyun.sts20150401.models.AssumeRoleResponse;
import com.aliyun.sts20150401.models.AssumeRoleResponseBody;
import com.aliyun.tea.TeaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
public class AliyunStsService {

    private final AliyunSts sts;
    private final com.aliyun.sts20150401.Client client;


    /**
     * 获取STS临时凭证
     *
     * @param ramRoleArn RAM角色的资源描述符
     * @return STS临时凭证
     */
    public AliyunStsSecurityCredential generateStsSecurityCredential(String ramRoleArn) {
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        com.aliyun.sts20150401.models.AssumeRoleRequest assumeRoleRequest = new com.aliyun.sts20150401.models.AssumeRoleRequest()
                .setRoleArn(ramRoleArn)
                .setRoleSessionName(UUID.randomUUID().toString())
                ;
        try {
            AssumeRoleResponse response = client.assumeRoleWithOptions(assumeRoleRequest, runtime);
            AssumeRoleResponseBody.AssumeRoleResponseBodyCredentials credentials = response.body.credentials;
            return new AliyunStsSecurityCredential(credentials.getSecurityToken(), credentials.getAccessKeySecret(), credentials.getAccessKeyId(), credentials.getExpiration());
        } catch (TeaException error) {
            log.error("AliyunStsService generateStsSecurityCredential STS 临时凭证获取失败: {}", error.getMessage(), error);
            log.error("AliyunStsService generateStsSecurityCredential 诊断建议: {}", error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception e) {
            log.error("AliyunStsService generateStsSecurityCredential STS 临时凭证获取失败", e);
            throw new RuntimeException(e);
        }
        return null;
    }
}
