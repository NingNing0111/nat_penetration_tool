package me.pgthinker.service.impl;

import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.common.Constants;
import me.pgthinker.config.ClientConfig;
import me.pgthinker.config.ProxyConfig;
import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;
import me.pgthinker.message.TransferMessageMetaDataProto.TransferMessageMetaData;
import me.pgthinker.enums.CmdTypeProto.CmdType;
import me.pgthinker.service.ProxyInfoService;
import me.pgthinker.util.TimestampUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Project: me.pgthinker.service.impl
 * @Author: De Ning
 * @Date: 2024/10/7 19:46
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProxyInfoServiceImpl implements ProxyInfoService {

    private final ClientConfig clientConfig;

    @Override
    public List<TransferDataMessage> getProxiesMessage() {
        List<ProxyConfig> proxies = clientConfig.getProxies();
        List<TransferDataMessage> messages = proxies.stream().map(item -> {

            HashMap<String, String> data = new HashMap<>();
            data.put(Constants.PROXY_PROTOCOL, item.getProtocol().getValue());
            data.put(Constants.PROXY_HOST, item.getHost());
            data.put(Constants.PROXY_PORT, item.getPort().toString());
            data.put(Constants.OPEN_PORT, item.getOpenPort().toString());

            TransferMessageMetaData metaData = TransferMessageMetaData.newBuilder()
                    .putAllMetaData(data)
                    .setTimestamp(TimestampUtil.now())
                    .build();

            TransferDataMessage message = TransferDataMessage.newBuilder()
                    .setCmdType(CmdType.OPEN_SERVER)
                    .setMetaData(metaData)
                    .setData(ByteString.EMPTY)
                    .build();

            return message;
        }).toList();

        return messages;
    }
}
