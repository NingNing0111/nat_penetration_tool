package me.pgthinker.service.impl;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.ProxyConfig;
import me.pgthinker.config.ClientConfig;
import me.pgthinker.enums.ProtocolEnum;
import me.pgthinker.handler.TCPConnectTunnelHandler;
import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;
import me.pgthinker.net.TcpConnection;
import me.pgthinker.service.NatConnectService;
import me.pgthinker.util.TransferDataMessageUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Project: me.pgthinker.service.impl
 * @Author: De Ning
 * @Date: 2024/10/7 23:05
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NatConnectServiceImpl implements NatConnectService {

    private final ClientConfig clientConfig;

    @Override
    public void startConnect(Channel targetChannel, ProxyConfig proxyConfig, String licenseKey, TransferDataMessage transferDataMessage) {
        ProtocolEnum protocol = proxyConfig.getProtocol();
        switch (protocol) {
            case TCP -> {
                TcpConnection tcpConnection = new TcpConnection();
                try {
                    ChannelFuture connect = tcpConnection.connect(proxyConfig.getHost(), proxyConfig.getPort(), new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new TCPConnectTunnelHandler(targetChannel, licenseKey, clientConfig.getClientId(), proxyConfig));
                        }
                    });
                    byte[] byteArray = transferDataMessage.getData().toByteArray();
                    connect.channel().writeAndFlush(Unpooled.copiedBuffer(byteArray));

                }catch (InterruptedException | IOException e){
                    targetChannel.writeAndFlush(TransferDataMessageUtil.buildDisconnectMessage(licenseKey, clientConfig.getClientId(), proxyConfig));
                }
            }
            case UDP -> {
                // TODO
            }
        }

    }
}
