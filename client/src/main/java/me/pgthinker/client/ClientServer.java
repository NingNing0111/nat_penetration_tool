package me.pgthinker.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.pgthinker.initializer.ClientInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Project: me.pgthinker.client
 * @Author: De Ning
 * @Date: 2024/10/3 22:34
 * @Description:
 */
@Service
@RequiredArgsConstructor
public class ClientServer {

    @Value("${client.serverPort}")
    private Integer serverPort;
    @Value("${client.serverHost}")
    private String serverHost;



    @PostConstruct
    public void runClient() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture future = bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer())
                .connect(serverHost, serverPort)
                .sync();
    }
}
