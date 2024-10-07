package me.pgthinker.service;


import io.netty.channel.Channel;

/**
 * @Project: me.pgthinker.service
 * @Author: De Ning
 * @Date: 2024/10/7 21:57
 * @Description: 客户端Channel服务
 */
public interface ClientChannelService {

    Channel getChannel(String clientId);

    void setChannel(String clientId, Channel channel);
}
