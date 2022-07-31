package com.oowanghan.atlantis.framework.web.exception;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author WangHan
 * @Create 2022/7/31 21:37
 */
@Configuration(proxyBeanMethods = false)
@Import(ExceptionTranslator.class)
public class ExceptionAutoConfiguration {
}
