# aliyun-spring-boot-starter

一个面向 Spring Boot 3 的阿里云能力聚合 Starter，当前项目将多个常见云能力封装为自动装配组件，开箱支持以下场景：

- OSS 对象存储上传、下载、删除、分片上传、签名访问
- IMM 媒体转码
- STS 临时凭证获取
- 短信发送
- PNS 号码认证与短信验证码校验
- 支付宝 APP 支付、扫码支付、资金转账

项目以单模块 Maven Jar 形式发布，适合作为业务系统的基础集成组件直接依赖使用。

## 1. 项目定位

本项目不是独立运行的业务服务，而是一个 `Spring Boot Starter`。  
核心目标是把阿里云与支付宝相关 SDK 的初始化、配置绑定和常用能力调用统一收口，业务项目只需要：

1. 引入 Starter 依赖
2. 在 `application.yml` 中配置 `aliyun.*`
3. 按需注入对应 Service 使用

## 2. 已支持能力

### 2.1 OSS 对象存储

自动装配类：

- `com.aliyun.config.AliyunOssConfiguration`

核心服务：

- `com.aliyun.core.oss.AliyunOssService`
- `com.aliyun.core.oss.impl.DefaultAliyunOssService`

当前已封装能力：

- 普通文件上传
- 分片上传
- 文件下载
- 单文件删除
- 批量删除
- 文件存在性检查
- 私有桶临时签名 URL
- 视频截帧签名 URL
- 浏览器直传签名参数生成

### 2.2 IMM 媒体处理

自动装配类：

- `com.aliyun.config.AliyunImmConfiguration`

核心服务：

- `com.aliyun.core.imm.AliyunImmService`
- `com.aliyun.core.imm.impl.DefaultAliyunImmService`

当前已封装能力：

- 媒体转码任务发起

### 2.3 STS 临时凭证

自动装配类：

- `com.aliyun.config.AliyunStsConfiguration`

核心服务：

- `com.aliyun.core.sts.AliyunStsService`

当前已封装能力：

- 获取 STS 临时安全凭证

### 2.4 短信服务

自动装配类：

- `com.aliyun.config.AliyunSmsConfiguration`

核心服务：

- `com.aliyun.core.sms.AliyunSmsService`

当前已封装能力：

- 发送登录验证码
- 发送通知类短信
- 通用模板短信发送

### 2.5 PNS 号码认证

自动装配类：

- `com.aliyun.config.AliyunPnsConfiguration`

核心服务：

- `com.aliyun.core.pns.AliyunPnsService`
- `com.aliyun.core.pns.impl.DefaultAliyunPnsService`

当前已封装能力：

- 发送短信验证码
- 校验短信验证码
- 一键登录取号

### 2.6 支付宝支付

自动装配类：

- `com.aliyun.config.AlipayConfiguration`

核心服务：

- `com.aliyun.core.pay.AliPayAppService`
- `com.aliyun.core.pay.AliPayScanCodeService`
- `com.aliyun.core.pay.AlipayFundService`

当前已封装能力：

- APP 支付下单
- 支付宝授权换取 Token
- 用户授权信息查询
- 交易查询
- 退款申请
- 退款查询
- 扫码支付
- 资金转账与转账结果查询

## 3. 技术栈与依赖版本

当前 `pom.xml` 关键依赖版本如下：

| 组件 | 版本 |
| --- | --- |
| Spring Boot Starter | 3.2.4 |
| Hutool | 5.8.24 |
| Lombok | 1.18.42 |
| 阿里云 OSS SDK | 3.17.4 |
| 阿里云 OSS V2 SDK | 0.3.1 |
| 阿里云 STS SDK | 1.1.6 |
| 阿里云 IMM SDK | 1.0.17 / 4.7.3 |
| 阿里云 PNS SDK | 2.0.0 |
| 阿里云短信 SDK | 4.5.1 |
| 支付宝 Java SDK | 4.40.771.ALL |
| 支付宝 Java V3 SDK | 3.1.53.ALL |

## 4. 引入方式

### 4.1 本地构建

```bash
mvn clean package  # 构建当前 Starter Jar 包
```

构建成功后产物默认位于：

```text
target/aliyun-spring-boot-starter-1.0.0.jar
```

### 4.2 业务项目中引入

如果你将该 Starter 发布到私服，可在业务项目中这样依赖：

```xml
<dependency>
    <groupId>com.hzj.aliyun</groupId>
    <artifactId>aliyun-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 5. 自动装配说明

Starter 通过以下文件导出自动配置：

```text
src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

当前导出配置包括：

- `AliyunConfiguration`
- `AliyunStsConfiguration`
- `AliyunImmConfiguration`
- `AliyunOssConfiguration`
- `AlipayConfiguration`
- `AliyunPnsConfiguration`
- `AliyunSmsConfiguration`

说明：

- `AliyunConfiguration` 负责启用 `aliyun.*` 配置绑定
- 具体服务按 `enable=true` 条件进行自动装配

## 6. 配置说明

### 6.1 顶层配置结构

所有能力统一挂在 `aliyun` 前缀下：

```yaml
aliyun:
  sts:
    enable: true
    access-key-id: your-access-key-id
    access-key-secret: your-access-key-secret
    endpoint: sts.cn-hangzhou.aliyuncs.com
    expire: 3600

  oss:
    enable: true
    endpoint: oss-cn-hangzhou.aliyuncs.com
    uri: https://
    region: cn-hangzhou
    default-bucket: your-default-bucket
    expire: 900
    ram-role-arn: acs:ram::xxxx:role/your-role
    callback: https://your-domain.com/api/oss/callback
    buckets:
      image: your-image-bucket
      video: your-video-bucket
      audio: your-audio-bucket

  imm:
    enable: true
    access-key-id: your-access-key-id
    access-key-secret: your-access-key-secret
    project-name: your-imm-project
    region: cn-hangzhou
    codec: H.264
    endpoint-override: imm.cn-hangzhou.aliyuncs.com
    container: mp4
    uri: oss://
    ram-role-arn: acs:ram::xxxx:role/your-role

  sms:
    enable: true
    access-key-id: your-access-key-id
    access-key-secret: your-access-key-secret
    endpoint: dysmsapi.aliyuncs.com
    region: cn-hangzhou
    default-sign-name: 示例签名
    sign-names:
      login_register: 示例签名
      change_phone: 备用签名
    ram-role-arn: acs:ram::xxxx:role/your-role

  pns:
    enable: true
    access-key-id: your-access-key-id
    access-key-secret: your-access-key-secret
    sign-name: 示例签名
    endpoint: dypnsapi.aliyuncs.com
    region: cn-hangzhou
    ram-role-arn: acs:ram::xxxx:role/your-role

  pay:
    enable: true
    access-key-id: your-access-key-id
    access-key-secret: your-access-key-secret
    app-id: your-common-app-id
    gate-way: https://openapi.alipay.com/gateway.do
    private-key: your-common-private-key
    public-key: your-common-public-key
    certificates: false
    app-cert-path: cert/appCertPublicKey.crt
    alipay-public-cert-path: cert/alipayCertPublicKey_RSA2.crt
    root-cert-path: cert/alipayRootCert.crt
    seller-id: your-common-seller-id
    validity-time: 1800000

    app:
      enable: true
      app-id: your-app-specific-app-id

    oauth:
      enable: true
      app-id: your-oauth-specific-app-id

    fund:
      enable: false
      app-id: your-fund-specific-app-id

    scan-code:
      enable: false
      app-id: your-scan-specific-app-id
      seller-id: your-seller-id
```

### 6.2 通用基础配置字段

以下对象继承 `AliyunBaseProperties`：

- `aliyun.sts`
- `aliyun.oss`
- `aliyun.imm`
- `aliyun.sms`
- `aliyun.pns`
- `aliyun.pay.app`
- `aliyun.pay.fund`
- `aliyun.pay.scan-code`
- `aliyun.pay.oauth`

公共字段：

- `enable`：是否启用当前能力
- `accessKeyId` / `accessKeySecret`：阿里云访问密钥

说明：

- `aliyun.pay` 提供公共配置
- `aliyun.pay.app`、`aliyun.pay.fund`、`aliyun.pay.scan-code`、`aliyun.pay.oauth` 提供各自专属配置
- 子配置未填写的字段会自动回退到 `aliyun.pay` 的公共配置

### 6.3 支付宝配置优先级说明

支付宝配置支持“公共一套 + 子服务各自覆盖一套”的模式。

推荐做法：

- 把大多数共用字段配置在 `aliyun.pay`，例如 `app-id`、`gate-way`、`private-key`、`public-key`
- 仅当某个子服务需要不同配置时，再在 `aliyun.pay.app`、`aliyun.pay.fund`、`aliyun.pay.scan-code`、`aliyun.pay.oauth` 中单独覆盖

回退规则：

- 如果子服务显式配置了某个字段，则优先使用子服务自己的值
- 如果子服务没有配置该字段，则自动回退到 `aliyun.pay` 的公共值
- `enable` 与 `certificates` 也遵循同样的回退规则

示例：

```yaml
aliyun:
  pay:
    gate-way: https://openapi.alipay.com/gateway.do
    private-key: your-common-private-key
    public-key: your-common-public-key

    app:
      enable: true

    fund:
      enable: true
      app-id: your-fund-app-id
```

上面的效果是：

- `app` 会复用公共的 `gate-way`、`private-key`、`public-key`
- `fund` 也会复用公共的 `gate-way`、`private-key`、`public-key`
- `fund` 因为单独配置了 `app-id`，所以它的 `app-id` 只使用自己的值

## 7. 使用示例

### 7.1 注入 OSS 服务

```java
import com.aliyun.core.oss.AliyunOssService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class OssDemoController {

    private final AliyunOssService aliyunOssService;

    @PostMapping("/demo/upload")
    public Object upload(@RequestParam("file") MultipartFile file) throws Exception {
        return aliyunOssService.upload(file.getOriginalFilename(), file.getInputStream());
    }
}
```

### 7.2 注入短信服务

```java
import com.aliyun.core.sms.AliyunSmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SmsDemoController {

    private final AliyunSmsService aliyunSmsService;

    @PostMapping("/demo/sms")
    public boolean send(@RequestParam String phone) {
        return aliyunSmsService.sendSmsCode(
                phone,
                "SMS_123456789",
                Map.of("code", "123456")
        );
    }
}
```

### 7.3 注入 PNS 服务

```java
import com.aliyun.core.pns.AliyunPnsService;
import com.aliyun.model.AliyunPnsTemplateParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PnsDemoController {

    private final AliyunPnsService aliyunPnsService;

    @PostMapping("/demo/pns/send")
    public boolean send(@RequestParam String schemeName, @RequestParam String phone) {
        AliyunPnsTemplateParam param = new AliyunPnsTemplateParam();
        return aliyunPnsService.sendSmsCode(
                schemeName,
                phone,
                "SMS_123456789",
                param
        );
    }
}
```

### 7.4 注入 APP 支付服务

```java
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class PayDemoController {

    private final AliPayAppService aliPayAppService;

    @GetMapping("/demo/pay/app")
    public String createOrder(@RequestParam String orderNo) throws Exception {
        return aliPayAppService.generateOrderStr(
                orderNo,
                new BigDecimal("0.01"),
                "测试订单",
                false,
                "https://your-domain.com/pay/notify"
        );
    }
}
```

## 8. 模板编码说明

短信与 PNS 服务现在直接使用云上模板编码字符串，由业务方自行传入具体的 `templateCode`。

## 9. 目录结构

```text
src/main/java/com/aliyun
├─ config          # 自动装配与客户端初始化
├─ core            # 能力服务实现
│  ├─ imm
│  ├─ io
│  ├─ oss
│  ├─ pay
│  ├─ pns
│  ├─ sms
│  └─ sts
├─ enums           # 业务枚举
├─ exception       # 业务异常
├─ model           # 数据模型
├─ properties      # 配置属性绑定
└─ utils           # 工具类
```

## 10. 构建与发布

### 10.1 本地验证

```bash
mvn clean package  # 执行本地构建与打包验证
```

成功标准：

- 控制台出现 `BUILD SUCCESS`
- 生成 `target/aliyun-spring-boot-starter-1.0.0.jar`

### 10.2 推送代码

首次关联远程仓库时，建议先以远程默认分支为基线，再提交本地历史，避免出现无共同祖先的历史冲突。

## 11. 注意事项

### 11.1 当前项目特性

- 当前仓库为单模块 Starter，不是多服务微服务仓库
- 更适合按“能力域”拆分提交，而不是按 Maven 子模块拆分

### 11.2 安全建议

- 不要在仓库中提交真实 `accessKeyId`、`accessKeySecret`、支付私钥、证书文件
- 支付证书路径建议由业务项目外部配置提供
- OSS 回调地址必须使用业务系统公网可访问地址

### 11.3 运行前提

- JDK 17 及以上
- Maven 3.8+ 推荐
- 业务系统需自行准备对应阿里云资源、RAM 角色和支付宝证书
