package me.pgthinker.service;

import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;

import java.util.List;

/**
 * @Project: me.pgthinker.service
 * @Author: De Ning
 * @Date: 2024/10/7 19:46
 * @Description:
 */
public interface ProxyInfoService {
    List<TransferDataMessage> getProxiesMessage();
}
