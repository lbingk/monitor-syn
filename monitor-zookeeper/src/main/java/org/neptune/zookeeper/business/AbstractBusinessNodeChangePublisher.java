package org.neptune.zookeeper.business;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.springframework.util.Assert;

@Slf4j
public abstract class AbstractBusinessNodeChangePublisher {

    private String nodePath;

    public final void setNodePath(String nodepath) {
        this.nodePath = nodepath;
    }

    public  String buildNodePath() {
        Assert.hasText(this.nodePath, "ZOOKEEPER 监听节点不可为空");
        return nodePath;
    }

    /**
     * 子节点添加
     *
     * @param event
     */
    public void childAdded(PathChildrenCacheEvent event) {
        log.info("AbstractNodeChangePublisher.childAdded");
    }

    /**
     * 子节点移除
     *
     * @param event
     */
    public void childRemoved(PathChildrenCacheEvent event) {
        log.info("AbstractNodeChangePublisher.childRemoved：{}", event);
    }

    /**
     * 字节点更新
     *
     * @param event
     */
    public void childUpdated(PathChildrenCacheEvent event) {
        log.info("AbstractNodeChangePublisher.childUpdated：{}", event);
    }

    /**
     * 失去连接
     *
     * @param event
     */
    public void connectionLost(PathChildrenCacheEvent event) {
        log.info("AbstractNodeChangePublisher.connectionLost：{}", event);
    }

    /**
     * 重连
     *
     * @param event
     */
    public void connectionReconnected(PathChildrenCacheEvent event) {
        log.info("AbstractNodeChangePublisher.connectionReconnected：{}", event);
    }

    /**
     * 挂起
     *
     * @param event
     */
    public void connectionSuspended(PathChildrenCacheEvent event) {
        log.info("AbstractNodeChangePublisher.connectionSuspended：{}", event);
    }

    /**
     * 初始化
     *
     * @param event
     */
    public void initialized(PathChildrenCacheEvent event) {
        log.info("AbstractNodeChangePublisher.initialized：{}", event);
    }
}
