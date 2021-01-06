package org.neptune.zookeeper.publish.change;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class NodeChangePublisherFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private Map<String, AbstractNodeChangePublisher> nodeChangePublisherMap;
    private String endName = "%s_PUBLISHER";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.nodeChangePublisherMap = this.applicationContext.getBeansOfType(AbstractNodeChangePublisher.class);
    }

    public AbstractNodeChangePublisher getPublisher(PathChildrenCacheEvent event) {
        return nodeChangePublisherMap.get(String.format(endName, event.getType().name()));
    }
}
