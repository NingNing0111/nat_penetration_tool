package me.pgthinker.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.crypto.digest.MD5;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.common.Constants;
import me.pgthinker.config.ServerConfig;
import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;
import me.pgthinker.enums.CmdTypeProto.CmdType;
import me.pgthinker.message.TransferMessageMetaDataProto.TransferMessageMetaData;
import me.pgthinker.service.AuthService;
import me.pgthinker.util.TimestampUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project: me.pgthinker.service.impl
 * @Author: De Ning
 * @Date: 2024/10/7 19:40
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ServerConfig serverConfig;

    @Override
    public TransferDataMessage auth(String password,String clientId) {
        String encryptedPassword = serverConfig.getEncryptedPassword();
        boolean isEq = BCrypt.checkpw(password, encryptedPassword);

        CmdType cmdType = CmdType.AUTH_ERR;
        String licenseKey = "";
        if(isEq){
            cmdType = CmdType.AUTH_OK;
            // 构建通讯的授权密钥
            licenseKey = MD5.create().digestHex16(clientId + password);
        }

        // TODO: 存储 clientId: licenseKey

        // 创建MetaData
        Map<String, String> data = new HashMap<>();
        data.put(Constants.LICENSE_KEY, licenseKey);
        TransferMessageMetaData metaData = TransferMessageMetaData.newBuilder()
                .putAllMetaData(data)
                .setTimestamp(TimestampUtil.now())
                .build();

        TransferDataMessage message = TransferDataMessage.newBuilder()
                .setCmdType(cmdType)
                .setMetaData(metaData)
                .setData(ByteString.EMPTY)
                .build();
        return message;
    }
}
