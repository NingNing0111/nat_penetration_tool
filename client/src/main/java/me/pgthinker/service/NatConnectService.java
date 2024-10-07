package me.pgthinker.service;

import io.netty.channel.Channel;
import me.pgthinker.ProxyConfig;
import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;

import java.io.IOException;

/**
 * @Project: me.pgthinker.service
 * @Author: De Ning
 * @Date: 2024/10/7 23:03
 * @Description:
 */
public interface NatConnectService {

    void startConnect(Channel targetChannel, ProxyConfig proxyConfig, String licenseKey, TransferDataMessage transferDataMessage) throws IOException, InterruptedException;
}
