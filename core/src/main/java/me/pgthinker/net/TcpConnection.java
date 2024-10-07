package me.pgthinker.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;

/**
 * @Project: me.pgthinker.net
 * @Author: De Ning
 * @Date: 2024/10/7 18:33
 * @Description:
 */
public class TcpConnection {
    public ChannelFuture connect(String host, int port, ChannelInitializer channelInitializer) throws InterruptedException, IOException {

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(channelInitializer);

            Channel channel = b.connect(host, port).sync().channel();
            return channel.closeFuture().addListener(future -> workerGroup.shutdownGracefully());
        } catch (Exception e) {
            workerGroup.shutdownGracefully();
            throw e;
        }
    }
}
