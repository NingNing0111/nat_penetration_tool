package me.pgthinker.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.ProxyConfig;
import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;
import me.pgthinker.util.TransferDataMessageUtil;

/**
 * @Project: me.pgthinker.handler
 * @Author: De Ning
 * @Date: 2024/10/7 22:59
 * @Description: TCP连接隧道处理 主要用于客户端向内网服务发送连接 接收到数据后进行处理
 */
@Slf4j
@RequiredArgsConstructor
public class TCPConnectTunnelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final Channel targetChannel;
    private final String licenseKey;
    private final String clientId;
    private final ProxyConfig proxyConfig;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        TransferDataMessage message = TransferDataMessageUtil.buildTcpMessage(byteBuf, proxyConfig, clientId, licenseKey);

        targetChannel.writeAndFlush(message);

        // 断开连接
        channelHandlerContext.channel().close().sync();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Nat connect handler init...");
        log.info("Nat Host:{} Port: {} ", proxyConfig.getHost(),proxyConfig.getPort());
    }
}
