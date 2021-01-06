package org.neptune;

import org.neptune.zookeeper.business.AbstractBusinessNodeChangePublisher;
import org.neptune.zookeeper.publish.register.AbstractNodeSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ZookeeperNodeSource extends AbstractNodeSource {
    public ZookeeperNodeSource(@Autowired @Qualifier("BusinessNodeChangePublisher") AbstractBusinessNodeChangePublisher abstractBusinessNodeChangePublisher) throws Exception {
        super(abstractBusinessNodeChangePublisher);
    }
}
