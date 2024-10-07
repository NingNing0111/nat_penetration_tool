package me.pgthinker.util;

import com.google.protobuf.ByteString;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import me.pgthinker.ProxyConfig;
import me.pgthinker.common.Constants;
import me.pgthinker.message.TransferDataMessageProto.TransferDataMessage;
import me.pgthinker.enums.CmdTypeProto.CmdType;
import me.pgthinker.message.TransferMessageMetaDataProto.TransferMessageMetaData;

import java.util.Map;

/**
 * @Project: me.pgthinker.util
 * @Author: De Ning
 * @Date: 2024/10/7 21:29
 * @Description:
 */
public class TransferDataMessageUtil {

    public static TransferDataMessage buildTcpMessage(ByteBuf tcpData) {
        return TransferDataMessage.newBuilder()
                .setCmdType(CmdType.TRANSFER)
                .setData(ByteString.copyFrom(ByteBufUtil.getBytes(tcpData)))
                .build();
    }

    public static TransferDataMessage buildTcpMessage(ByteBuf tcpData, ProxyConfig proxyConfig){
        TransferMessageMetaData metaData = TransferMessageMetaData.newBuilder()
                .putAllMetaData(proxyConfig.toMap())
                .setTimestamp(TimestampUtil.now())
                .build();
        return TransferDataMessage.newBuilder()
                .setCmdType(CmdType.TRANSFER)
                .setMetaData(metaData)
                .setData(ByteString.copyFrom(ByteBufUtil.getBytes(tcpData)))
                .build();
    }

    public static TransferDataMessage buildTcpMessage(ByteBuf tcpData, ProxyConfig proxyConfig, String clientId, String licenseKey){
        Map<String, String> map = proxyConfig.toMap();
        map.put(Constants.CLIENT_ID, clientId);
        map.put(Constants.LICENSE_KEY, licenseKey);

        TransferMessageMetaData metaData = TransferMessageMetaData.newBuilder()
                .putAllMetaData(map)
                .setTimestamp(TimestampUtil.now())
                .build();
        return TransferDataMessage.newBuilder()
                .setCmdType(CmdType.TRANSFER)
                .setMetaData(metaData)
                .setData(ByteString.copyFrom(ByteBufUtil.getBytes(tcpData)))
                .build();
    }

    public static TransferDataMessage buildDisconnectMessage() {
        return TransferDataMessage.newBuilder()
                .setCmdType(CmdType.DISCONNECT)
                .build();
    }

    public static TransferDataMessage buildDisconnectMessage(String licenseKey, String clientId, ProxyConfig proxyConfig) {

        Map<String, String> map = proxyConfig.toMap();
        map.put(Constants.LICENSE_KEY,licenseKey);
        map.put(Constants.CLIENT_ID, clientId);

        TransferMessageMetaData metaData = TransferMessageMetaData.newBuilder()
                .setTimestamp(TimestampUtil.now())
                .putAllMetaData(map)
                .build();

        return TransferDataMessage.newBuilder()
                .setCmdType(CmdType.DISCONNECT)
                .setMetaData(metaData)
                .build();
    }
}
