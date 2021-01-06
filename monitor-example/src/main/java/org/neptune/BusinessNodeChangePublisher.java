package org.neptune;

import org.neptune.zookeeper.business.AbstractBusinessNodeChangePublisher;
import org.springframework.stereotype.Component;

@Component("BusinessNodeChangePublisher")
public class BusinessNodeChangePublisher extends AbstractBusinessNodeChangePublisher {

    @Override
    public String buildNodePath() {
        super.setNodePath("/business/test");
        return "/business/test";
    }
}
