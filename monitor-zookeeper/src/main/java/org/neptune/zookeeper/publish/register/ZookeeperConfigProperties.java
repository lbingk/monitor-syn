package org.neptune.zookeeper.publish.register;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "zookeeper.config")
public class ZookeeperConfigProperties {
    private String connectString;
    private int sessionTimeout;
    private int connectionTimeout;
}
