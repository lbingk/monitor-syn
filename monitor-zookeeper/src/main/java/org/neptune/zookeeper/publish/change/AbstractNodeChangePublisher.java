package org.neptune.zookeeper.publish.change;

import lombok.extern.slf4j.Slf4j;
import org.neptune.zookeeper.business.AbstractBusinessNodeChangePublisher;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;


@Slf4j
public abstract class AbstractNodeChangePublisher {

    /**
     * 事件发布
     *
     * @param changePublisher
     * @param event
     */
    public abstract void publish(AbstractBusinessNodeChangePublisher changePublisher, PathChildrenCacheEvent event);

}
