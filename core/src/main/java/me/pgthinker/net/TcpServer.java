package me.pgthinker.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Project: me.pgthinker.net
 * @Author: De Ning
 * @Date: 2024/10/7 18:33
 * @Description: Tcp服务实例
 */
public class TcpServer {

    private Channel channel;

    public synchronized void bind(int port, ChannelInitializer channelInitializer) throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            channel = b.bind(port).sync().channel();
            channel.closeFuture().addListener((ChannelFutureListener) future -> {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            });
        } catch (Exception e) {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            throw e;
        }
    }

    public synchronized void close() {
        if (channel != null) {
            channel.close();
        }
    }
}
