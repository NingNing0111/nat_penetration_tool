package me.pgthinker.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.ProxyConfig;
import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;
import me.pgthinker.util.TransferDataMessageUtil;

import java.nio.charset.Charset;

/**
 * @Project: me.pgthinker.handler
 * @Author: De Ning
 * @Date: 2024/10/7 21:26
 * @Description: TCP协议的代理隧道 公网访问代理隧道时 会将数据由byteBuf转换为TransferDataMessage 并发送给指定的channel
 */
@RequiredArgsConstructor
@Slf4j
public class TCPProxyTunnelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final Channel targetChannel;
    private final ProxyConfig proxyConfig;


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        TransferDataMessage tcpTransferMessage = TransferDataMessageUtil.buildTcpMessage(byteBuf, proxyConfig);
        targetChannel.writeAndFlush(tcpTransferMessage);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 处理异常
        log.info("e:{}",cause.getMessage());

        ctx.close(); // 关闭通道
    }

}
