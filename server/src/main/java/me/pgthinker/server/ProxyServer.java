package me.pgthinker.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.pgthinker.initializer.ServerInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project: me.pgthinker.server
 * @Author: De Ning
 * @Date: 2024/10/3 22:23
 * @Description:
 */
@Service
@RequiredArgsConstructor
public class ProxyServer {

    @Value("${server.port}")
    private Integer serverPort;

    @PostConstruct
    public void runProxyServer() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture future = new ServerBootstrap()
                .group(group)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler())
                .childHandler(new ServerInitializer())
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                .bind(serverPort)
                .sync();
    }


}
