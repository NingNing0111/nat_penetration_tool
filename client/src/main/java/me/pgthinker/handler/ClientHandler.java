package me.pgthinker.handler;

import cn.hutool.extra.spring.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;
import me.pgthinker.enums.CmdTypeProto.CmdType;
import me.pgthinker.service.AuthService;
import me.pgthinker.service.ProxyInfoService;

import java.util.List;
import java.util.Map;

/**
 * @Project: me.pgthinker.handler
 * @Author: De Ning
 * @Date: 2024/10/7 17:05
 * @Description:
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<TransferDataMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TransferDataMessage transferDataMessage) throws Exception {
        Channel channel = channelHandlerContext.channel();
        CmdType cmdType = transferDataMessage.getCmdType();
        Map<String, String> metaDataMap = transferDataMessage.getMetaData().getMetaDataMap();
        log.info("Received message from client: {}-{}", cmdType.name(), metaDataMap);

        switch (cmdType) {
            case AUTH_OK -> {
                ProxyInfoService proxyInfoService = SpringUtil.getBean(ProxyInfoService.class);
                List<TransferDataMessage> proxiesMessage = proxyInfoService.getProxiesMessage();
                for (TransferDataMessage message: proxiesMessage) {
                    channel.writeAndFlush(message);
                }
            }
            case AUTH_ERR -> {
                log.error("Auth Error");
                channel.close().sync();
            }
        }


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        AuthService authService = SpringUtil.getBean(AuthService.class);
        TransferDataMessage authMessage = authService.getAuthMessage();

        channel.writeAndFlush(authMessage);

    }


}
