package com.oowanghan.atlantis.util.spring;

import com.oowanghan.atlantis.util.base.AtlantisException;
import com.oowanghan.atlantis.util.collection.ArrayUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * SpringUtils
 *
 * @Author WangHan
 */
@Component
public class SpringUtil implements BeanFactoryPostProcessor, ApplicationContextAware {
    /**
     * spring上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * "@PostConstruct"注解标记的类中，由于ApplicationContext还未加载，导致空指针<br>
     * 因此实现BeanFactoryPostProcessor注入ConfigurableListableBeanFactory实现bean的操作
     */
    private static ConfigurableListableBeanFactory beanFactory;


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        setBeanFactory(configurableListableBeanFactory);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setApplicationContextStatic(applicationContext);
    }

    public static void setApplicationContextStatic(final ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }

    public static void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        SpringUtil.beanFactory = beanFactory;
    }

    /**
     * 获取spring上下文
     *
     * @return 上下文对象
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取bean
     *
     * @param requiredType bean类型
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> requiredType) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(requiredType);
    }

    /**
     * 获取bean
     *
     * @param name         bean名称
     * @param requiredType bean类型
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * 获取配置文件配置项的值
     *
     * @param key 配置项key
     * @return 属性值
     */
    public static String getProperty(String key) {
        if (null == applicationContext) {
            return null;
        }
        return applicationContext.getEnvironment().getProperty(key);
    }

    /**
     * 获取应用程序名称
     *
     * @return 应用程序名称
     */
    public static String getApplicationName() {
        return getProperty("spring.application.name");
    }

    /**
     * 获取当前的环境配置，无配置返回null
     *
     * @return 当前的环境配置
     */
    public static String[] getActiveProfiles() {
        if (null == applicationContext) {
            return null;
        }
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取{@link ConfigurableListableBeanFactory}
     *
     * @return {@link ConfigurableListableBeanFactory}
     * @throws UtilException 当上下文非ConfigurableListableBeanFactory抛出异常
     */
    public static ConfigurableListableBeanFactory getConfigurableBeanFactory() throws AtlantisException {
        final ConfigurableListableBeanFactory factory;
        if (null != beanFactory) {
            factory = beanFactory;
        } else if (applicationContext instanceof ConfigurableApplicationContext) {
            factory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        } else {
            throw new AtlantisException("No ConfigurableListableBeanFactory from context!");
        }
        return factory;
    }

    /**
     * 获取当前的环境配置，当有多个环境配置时，只获取第一个
     *
     * @return 当前的环境配置
     */
    public static String getActiveProfile() {
        final String[] activeProfiles = getActiveProfiles();
        return ArrayUtil.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
    }

    /**
     * 动态向Spring注册Bean
     * <p>
     * 由{@link org.springframework.beans.factory.BeanFactory} 实现，通过工具开放API
     * <p>
     * 更新: shadow 2021-07-29 17:20:44 增加自动注入，修复注册bean无法反向注入的问题
     *
     * @param <T>      Bean类型
     * @param beanName 名称
     * @param bean     bean
     */
    public static <T> void registerBean(String beanName, T bean) {
        final ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
        factory.autowireBean(bean);
        factory.registerSingleton(beanName, bean);
    }

    /**
     * 注销bean
     * <p>
     * 将Spring中的bean注销，请谨慎使用
     *
     * @param beanName bean名称
     * @return 返回操作成功
     */
    public static boolean unregisterBean(String beanName) {
        final ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
        if (factory instanceof DefaultSingletonBeanRegistry) {
            DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) factory;
            registry.destroySingleton(beanName);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 发布事件
     *
     * @param event 待发布的事件，事件必须是{@link ApplicationEvent}的子类
     */
    public static void publishEvent(ApplicationEvent event) {
        if (null != applicationContext) {
            applicationContext.publishEvent(event);
        }
    }

    /**
     * 发布事件
     * Spring 4.2+ 版本事件可以不再是{@link ApplicationEvent}的子类
     *
     * @param event 待发布的事件
     */
    public static void publishEvent(Object event) {
        if (null != applicationContext) {
            applicationContext.publishEvent(event);
        }
    }
}