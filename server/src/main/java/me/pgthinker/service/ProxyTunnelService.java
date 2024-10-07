package me.pgthinker.service;

import io.netty.channel.Channel;
import me.pgthinker.ProxyConfig;

/**
 * @Project: me.pgthinker.service
 * @Author: De Ning
 * @Date: 2024/10/7 21:21
 * @Description: 代理隧道
 */
public interface ProxyTunnelService {

    // 开启一个代理隧道

    void startProxyServer(String clientId,ProxyConfig proxyConfig) throws InterruptedException;

    // 根据openPort获取Channel
    Channel getChannel(Integer openPort);

}
