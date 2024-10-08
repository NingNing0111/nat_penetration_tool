package me.pgthinker.handler;

import cn.hutool.extra.spring.SpringUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.ProxyConfig;
import me.pgthinker.common.Constants;
import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;
import me.pgthinker.message.TransferMessageMetaDataProto.TransferMessageMetaData;
import me.pgthinker.enums.CmdTypeProto.CmdType;
import me.pgthinker.service.AuthService;
import me.pgthinker.service.ClientChannelService;
import me.pgthinker.service.ProxyTunnelService;

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

        switch (cmdType) {
            case AUTH -> {
                handleAuth(channel,transferDataMessage);
            }
            case OPEN_SERVER -> {
                handleOpenServer(channel, transferDataMessage);
            }
            case TRANSFER -> {
                handleTransferData(channel,transferDataMessage);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Server starting...");
    }

    private void handleAuth(Channel channel,TransferDataMessage transferDataMessage) throws InterruptedException {

        AuthService authService = SpringUtil.getBean(AuthService.class);

        Map<String, String> metaDataMap = transferDataMessage.getMetaData().getMetaDataMap();
        String clientId = metaDataMap.get(Constants.CLIENT_ID);
        TransferDataMessage authResMessage = authService.authPassword(metaDataMap.get(Constants.AUTH_PASSWORD), metaDataMap.get(Constants.CLIENT_ID));
        channel.writeAndFlush(authResMessage);
        handleAuthRes(channel,clientId,transferDataMessage.getCmdType());
    }

    private void handleAuthRes(Channel channel, String clientId, CmdType authType) throws InterruptedException {
        ClientChannelService clientChannelService = SpringUtil.getBean(ClientChannelService.class);

        // 认证失败 断开连接
        if(authType.equals(CmdType.AUTH_ERR)) {
            channel.close().sync();
        }else{
            // 否则加入连接记录中
            clientChannelService.setChannel(clientId,channel);
        }
    }

    private void handleOpenServer(Channel channel, TransferDataMessage transferDataMessage) throws InterruptedException {

        AuthService authService = SpringUtil.getBean(AuthService.class);
        ProxyTunnelService proxyTunnelService = SpringUtil.getBean(ProxyTunnelService.class);

        Map<String, String> metaDataMap = transferDataMessage.getMetaData().getMetaDataMap();

        // 先验证licenseKey
        String licenseKey = metaDataMap.get(Constants.LICENSE_KEY);
        String clientId = metaDataMap.get(Constants.CLIENT_ID);
        TransferDataMessage message = authService.authLicenseKey(clientId, licenseKey);

        // 对认证结果进行处理
        if(message.getCmdType().equals(CmdType.AUTH_ERR)){
            channel.close().sync();
            return;
        }
        // 开启代理
        proxyTunnelService.startProxyServer(clientId, ProxyConfig.fromMap(metaDataMap));

    }


    private void handleTransferData(Channel channel,TransferDataMessage transferDataMessage) throws InterruptedException {
        AuthService authService = SpringUtil.getBean(AuthService.class);
        ProxyTunnelService proxyTunnelService = SpringUtil.getBean(ProxyTunnelService.class);

        Map<String, String> metaDataMap = transferDataMessage.getMetaData().getMetaDataMap();
        String clientId = metaDataMap.get(Constants.CLIENT_ID);
        String licenseKey = metaDataMap.get(Constants.LICENSE_KEY);

        TransferDataMessage message = authService.authLicenseKey(clientId, licenseKey);
        // 对认证结果进行处理
        if(message.getCmdType().equals(CmdType.AUTH_ERR)){
            channel.close().sync();
            return;
        }
        // 将数据返回出去
        Integer openPort = Integer.parseInt(metaDataMap.get(Constants.OPEN_PORT));
        Channel openChannel = proxyTunnelService.getChannel(openPort);
        byte[] byteArray = transferDataMessage.getData().toByteArray();
        openChannel.writeAndFlush(Unpooled.copiedBuffer(byteArray));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 处理异常
        log.info("e:{}",cause.getMessage());
        ctx.close(); // 关闭通道
    }
}
