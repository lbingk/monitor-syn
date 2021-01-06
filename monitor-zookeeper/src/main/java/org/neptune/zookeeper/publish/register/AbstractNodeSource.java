package org.neptune.zookeeper.publish.register;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.*;
import org.neptune.zookeeper.business.AbstractBusinessNodeChangePublisher;
import org.neptune.zookeeper.publish.change.AbstractNodeChangePublisher;
import org.neptune.zookeeper.publish.change.NodeChangePublisherFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


@Slf4j
public abstract class AbstractNodeSource implements InitializingBean {

    protected static final List<AbstractNodeSource> NODE_SOURCE = new ArrayList<>();

    private static Map<String, CuratorFramework> zkClientMap = Maps.newHashMap();

    private CuratorFramework zkClient;

    /**
     * 注册的节点地址
     */
    private String path;

    /**
     * 工厂类获取对应的节点接听类
     */
    @Autowired
    private NodeChangePublisherFactory nodeChangePublisherFactory;

    @Autowired
    private ZookeeperConfigProperties zookeeperConfigProperties;

    /**
     * 业务实现类
     */
    private AbstractBusinessNodeChangePublisher abstractBusinessNodeChangePublisher;

    private static final int DEFAULT_ZK_SESSION_TIMEOUT = 30000;
    private static final int DEFAULT_ZK_CONNECTION_TIMEOUT = 10000;
    private static final int RETRY_TIMES = 3;
    private static final int SLEEP_TIME = 1000;

    public AbstractNodeSource(AbstractBusinessNodeChangePublisher abstractBusinessNodeChangePublisher) throws Exception {
        this.abstractBusinessNodeChangePublisher = abstractBusinessNodeChangePublisher;
        NODE_SOURCE.add(this);
    }

    /**
     * 初始化ZOOKEEPER
     */
    protected void initZookeeper() {
        int sessionTimeout = zookeeperConfigProperties.getSessionTimeout() == 0 ? DEFAULT_ZK_SESSION_TIMEOUT : zookeeperConfigProperties.getSessionTimeout();
        int connectionTimeout = zookeeperConfigProperties.getConnectionTimeout() == 0 ? DEFAULT_ZK_CONNECTION_TIMEOUT : zookeeperConfigProperties.getConnectionTimeout();
        this.path = abstractBusinessNodeChangePublisher.buildNodePath();
        zkClient = zkClientMap.get(buildZkClientMapKey());
        if (Objects.isNull(zkClient)) {
            zkClient = CuratorFrameworkFactory.newClient(zookeeperConfigProperties.getConnectString(), sessionTimeout, connectionTimeout, new ExponentialBackoffRetry(SLEEP_TIME, RETRY_TIMES));
            zkClientMap.put(zookeeperConfigProperties.getConnectString(), zkClient);
        }
        zkClient.start();
    }

    /**
     * 注册节点监听器
     */
    protected void initZookeeperListener() throws Exception {
        initPathChildrenCacheListener();
        initNodeCacheListener();
    }


    /**
     * 当前节点的子节点
     *
     * @throws Exception
     */
    private void initPathChildrenCacheListener() throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(this.zkClient, this.path, true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                log.info("事件：{}", event);
                AbstractNodeChangePublisher publisher = nodeChangePublisherFactory.getPublisher(event);
                publisher.publish(abstractBusinessNodeChangePublisher, event);
            }
        });
        pathChildrenCache.start();
    }

    /**
     * 当前节点
     *
     * @throws Exception
     */
    private void initNodeCacheListener() throws Exception {
        final NodeCache nodeCache = new NodeCache(this.zkClient, this.path, true);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                log.info("NodeCache:{}", nodeCache.getCurrentData().getData());
            }
        });
        nodeCache.start();
    }

    /**
     * 构建KEY
     *
     * @return
     */
    private String buildZkClientMapKey() {
        return this.zookeeperConfigProperties.getConnectString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initZookeeper();
        this.initZookeeperListener();
    }
}


