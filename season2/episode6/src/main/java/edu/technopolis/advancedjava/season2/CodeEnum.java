package edu.technopolis.advancedjava.season2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeEnum {
    SOCKS_VERSION((byte) 0x05),
    AUTH_METHOD((byte) 0x00),
    AUTH_REJECT((byte) 0xF),
    CONNECTION_PROVIDED_CODE((byte) 0x00),
    ERROR_SOCKS_SERVER((byte) 0x01),
    CMD_NUMBER((byte) 0x01),
    ADDRESS_TYPE((byte) 0x01),
    RESERVED_BYTE((byte) 0x00),
    IPV4_BYTES((byte) 4);
    //    BUFFER_SIZE(300);

    private final byte code;
}