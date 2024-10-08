package me.pgthinker.handler;

import cn.hutool.extra.spring.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.ProxyConfig;
import me.pgthinker.common.Constants;
import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;
import me.pgthinker.enums.CmdTypeProto.CmdType;
import me.pgthinker.service.AuthService;
import me.pgthinker.service.NatConnectService;
import me.pgthinker.service.ProxyInfoService;

import java.nio.charset.Charset;
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

    private String licenseKey;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TransferDataMessage transferDataMessage) throws Exception {
        Channel channel = channelHandlerContext.channel();
        CmdType cmdType = transferDataMessage.getCmdType();
        Map<String, String> metaDataMap = transferDataMessage.getMetaData().getMetaDataMap();

        switch (cmdType) {
            case AUTH_OK -> {
                log.info("Auth successful. licenseKey:{} ", metaDataMap.get(Constants.LICENSE_KEY));
                ProxyInfoService proxyInfoService = SpringUtil.getBean(ProxyInfoService.class);
                licenseKey = metaDataMap.get(Constants.LICENSE_KEY);
                List<TransferDataMessage> proxiesMessage = proxyInfoService.getProxiesMessage(licenseKey);
                for (TransferDataMessage message: proxiesMessage) {
                    channel.writeAndFlush(message);
                }
            }
            case AUTH_ERR -> {
                log.error("Auth Error");
                channel.close().sync();
            }
            case TRANSFER -> {
                NatConnectService natConnectService = SpringUtil.getBean(NatConnectService.class);
                natConnectService.startConnect(channel, ProxyConfig.fromMap(metaDataMap),licenseKey, transferDataMessage);
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 处理异常
        log.info("e:{}",cause.getMessage());

        ctx.close(); // 关闭通道
    }


}
