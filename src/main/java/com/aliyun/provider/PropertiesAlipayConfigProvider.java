package com.aliyun.provider;

import cn.hutool.core.bean.BeanUtil;
import com.aliyun.properties.AlipayProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PropertiesAlipayConfigProvider implements AlipayConfigProvider {

    private final AlipayProperties properties;

    @Override
    public com.aliyun.provider.domain.AlipayConfig getConfig() {
        return BeanUtil.copyProperties(properties,
                com.aliyun.provider.domain.AlipayConfig.class);
    }
}
