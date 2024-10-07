package me.pgthinker.service;

import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;

/**
 * @Project: me.pgthinker.service
 * @Author: De Ning
 * @Date: 2024/10/7 19:38
 * @Description:
 */
public interface AuthService {
    TransferDataMessage auth(String password, String clientId);
}
