package me.pgthinker.service.impl;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.ProxyConfig;
import me.pgthinker.enums.ProtocolEnum;
import me.pgthinker.handler.TCPProxyTunnelHandler;
import me.pgthinker.net.TcpServer;
import me.pgthinker.service.ClientChannelService;
import me.pgthinker.service.ProxyTunnelService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Project: me.pgthinker.service.impl
 * @Author: De Ning
 * @Date: 2024/10/7 21:47
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProxyTunnelServiceImpl implements ProxyTunnelService {

    private final static Map<Integer, Channel> PROXY_PORT_MAP = new ConcurrentHashMap<>();

    private final ClientChannelService clientChannelService;

    @Override
    public void startProxyServer(String clientId, ProxyConfig proxyConfig) throws InterruptedException {
        ProtocolEnum protocol = proxyConfig.getProtocol();
        Channel targetChannel = clientChannelService.getChannel(clientId);
        switch (protocol) {
            case UDP -> {
                // TODO
            }
            case TCP -> {
                TcpServer tcpServer = new TcpServer();
                tcpServer.bind(proxyConfig.getOpenPort(), new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new TCPProxyTunnelHandler(targetChannel,proxyConfig));

                    }

                    @Override
                    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                        Channel channel = ctx.channel();
                        PROXY_PORT_MAP.put(proxyConfig.getOpenPort(), channel);
                    }
                });

            }
        }
    }

    @Override
    public Channel getChannel(Integer openPort) {
        return PROXY_PORT_MAP.get(openPort);
    }
}
