package me.pgthinker.handler;

import cn.hutool.extra.spring.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.common.Constants;
import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;
import me.pgthinker.message.TransferMessageMetaDataProto.TransferMessageMetaData;
import me.pgthinker.enums.CmdTypeProto.CmdType;
import me.pgthinker.service.AuthService;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @Project: me.pgthinker.handler
 * @Author: De Ning
 * @Date: 2024/10/7 16:57
 * @Description:
 */
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<TransferDataMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TransferDataMessage transferDataMessage) throws Exception {

        Channel channel = channelHandlerContext.channel();

        CmdType cmdType = transferDataMessage.getCmdType();
        Map<String, String> metaDataMap = transferDataMessage.getMetaData().getMetaDataMap();

        log.info("Received message from client: {}-{}", cmdType.name(), metaDataMap);

        AuthService authService = SpringUtil.getBean(AuthService.class);

        if(cmdType.equals(CmdType.AUTH)) {
            TransferDataMessage authResMessage = authService.auth(metaDataMap.get(Constants.AUTH_PASSWORD), metaDataMap.get(Constants.CLIENT_ID));
            channel.writeAndFlush(authResMessage);
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Server starting...");
    }
}
