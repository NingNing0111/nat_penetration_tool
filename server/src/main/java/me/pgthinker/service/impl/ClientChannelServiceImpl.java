package me.pgthinker.service.impl;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;
import me.pgthinker.service.ClientChannelService;
import me.pgthinker.util.TransferDataMessageUtil;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Project: me.pgthinker.service.impl
 * @Author: De Ning
 * @Date: 2024/10/7 21:58
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClientChannelServiceImpl implements ClientChannelService {

    private final Map<String, Channel> CLIENT_CHANNEL_SET = new ConcurrentHashMap<>();

    @Override
    public Channel getChannel(String clientId) {
        return CLIENT_CHANNEL_SET.getOrDefault(clientId,null);
    }

    @Override
    public synchronized void setChannel(String clientId, Channel channel) {
        // 如果存在Channel了 需要关闭
        if(CLIENT_CHANNEL_SET.containsKey(clientId)) {
            try {
                channel.writeAndFlush(TransferDataMessageUtil.buildDisconnectMessage());
                channel.close().sync();
            }catch (InterruptedException e){
                log.error("e: {}", e.getMessage());
            }
        }
        CLIENT_CHANNEL_SET.put(clientId,channel);
    }

}
