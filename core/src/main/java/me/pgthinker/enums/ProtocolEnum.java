package me.pgthinker.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Project: me.pgthinker.enums
 * @Author: De Ning
 * @Date: 2024/10/7 18:46
 * @Description:
 */
@RequiredArgsConstructor
@Getter
public enum ProtocolEnum {

    TCP("tcp"),
    UDP("udp");

    private final String value;
}
