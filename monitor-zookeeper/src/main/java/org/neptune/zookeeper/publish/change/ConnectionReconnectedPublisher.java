package org.neptune.zookeeper.publish.change;

import lombok.extern.slf4j.Slf4j;
import org.neptune.zookeeper.business.AbstractBusinessNodeChangePublisher;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component("CONNECTION_RECONNECTED_PUBLISHER")
public class ConnectionReconnectedPublisher extends AbstractNodeChangePublisher {

    @Override
    public void publish(AbstractBusinessNodeChangePublisher changePublisher, PathChildrenCacheEvent event) {
        changePublisher.connectionReconnected(event);
    }
}
