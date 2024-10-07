package me.pgthinker.service.impl;

import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.common.Constants;
import me.pgthinker.config.ClientConfig;
import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;
import me.pgthinker.message.TransferMessageMetaDataProto.TransferMessageMetaData;
import me.pgthinker.enums.CmdTypeProto.CmdType;
import me.pgthinker.service.AuthService;
import me.pgthinker.util.TimestampUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @Project: me.pgthinker.service.impl
 * @Author: De Ning
 * @Date: 2024/10/7 19:44
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ClientConfig clientConfig;

    @Override
    public TransferDataMessage getAuthMessage() {
        ///// 进行认证
        String password = clientConfig.getPassword();
        String clientId = clientConfig.getClientId();
        // 1. 构建MetaData
        HashMap<String, String> data = new HashMap<>();
        data.put(Constants.AUTH_PASSWORD,password);
        data.put(Constants.CLIENT_ID, clientId);

        TransferMessageMetaData metaData = TransferMessageMetaData.newBuilder()
                .setTimestamp(TimestampUtil.now())
                .putAllMetaData(data)
                .build();
        // 2. 构建认证信息
        return TransferDataMessage.newBuilder()
                .setCmdType(CmdType.AUTH) // 认证指令
                .setMetaData(metaData)
                .setData(ByteString.EMPTY)
                .build();
    }
}
