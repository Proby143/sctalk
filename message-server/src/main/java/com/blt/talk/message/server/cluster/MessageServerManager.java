/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.cluster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public class MessageServerManager {

    private Map<String, MessageServerInfo> messageServerInfoMap;
    private String memberId;

    public MessageServerManager(HazelcastInstance hazelcastInstance) {
        this.messageServerInfoMap = hazelcastInstance.getMap("message-server#router#server");
        this.memberId = hazelcastInstance.getCluster().getLocalMember().getUuid();
    }

    /**
     * 通过连接ID查询对应的MessageServer信息
     * 
     * @return MessageServer信息
     * @since 1.0
     */
    public MessageServerInfo getServer(String uuid) {
        return messageServerInfoMap.get(uuid);
    }

    /**
     * 根据【连接ID】移除MessageServer信息
     * 
     * @param clientUuid 客户端UUID
     * @since 1.0
     */
    public void unload(String clientUuid) {
        if (messageServerInfoMap.containsKey(clientUuid)) {
            messageServerInfoMap.remove(clientUuid);
        }
    }

    /**
     * 添加MessageServer信息
     * 
     * 
     * @param serverInfo MessageServer信息
     * @since 1.0
     */
    public void insert(MessageServerInfo serverInfo) {
        messageServerInfoMap.put(memberId, serverInfo);
    }

    /**
     * 消息服务器信息
     * 
     * @author 袁贵
     * @version 1.0
     * @since 1.0
     */
    public static class MessageServerInfo implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 3130423310364673999L;

        /** IP地址/主机地址 */
        private String ip;
        /** 端口号（Netty-Socket） */
        private Integer port;

        /**
         * @return the ip
         */
        public String getIp() {
            return ip;
        }

        /**
         * @param ip the ip to set
         */
        public void setIp(String ip) {
            this.ip = ip;
        }

        /**
         * @return the port
         */
        public Integer getPort() {
            return port;
        }

        /**
         * @param port the port to set
         */
        public void setPort(Integer port) {
            this.port = port;
        }

    }

    /**
     * @return
     * @since 1.0
     */
    public List<String> allServerIds() {
        if (messageServerInfoMap.size() == 0) {
            return null;
        }
        List<String> serverIds = new ArrayList<>();
        for (String uuid : messageServerInfoMap.keySet()) {
            serverIds.add(uuid);
        }
        return serverIds;
    }
    
    /**
     * @return
     * @since 1.0
     */
    public List<String> allServerNames() {
        if (messageServerInfoMap.size() == 0) {
            return null;
        }
        List<String> serverNames = new ArrayList<>();
        for (MessageServerInfo server : messageServerInfoMap.values()) {
            serverNames.add(server.getIp() + ":" + server.getPort());
        }
        return serverNames;
    }

}