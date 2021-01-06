package org.neptune.zookeeper.publish.register;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.Objects;


@Slf4j
@Configuration
@EnableConfigurationProperties(ZookeeperConfigProperties.class)
public class NodeRegisterList<E> extends ArrayList<E> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @SneakyThrows
    @Override
    public boolean add(E e) {
        Assert.isInstanceOf(AbstractNodeSource.class, e.getClass());
        boolean add = super.add(e);
        if (!add) {
            log.error("节点监听注册失败：{}", e);
            return false;
        }
        doRegisterBeanDefinition(e);
        return true;
    }

    private void doRegisterBeanDefinition(E e) {
        if (isSpringManage(e)) {
            return;
        }
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) this.applicationContext;
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        //通过BeanDefinitionBuilder创建bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(e.getClass());
        // 注册bean
        defaultListableBeanFactory.registerBeanDefinition(e.getClass().getSimpleName(), beanDefinitionBuilder.getRawBeanDefinition());
        // 并将用户自定义好的OBJECT存储在对应的单例缓存池
        defaultListableBeanFactory.registerSingleton(e.getClass().getSimpleName(), e);
    }

    /**
     * 判断是否已经交于SPRING 管理
     *
     * @param e
     * @return
     */
    private boolean isSpringManage(E e) {
        return Objects.nonNull(applicationContext.getBean(e.getClass().getName()));
    }
}
