package com.lzhphantom.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PasswordEncoderTypeEnum {
    BCRYPT("{bcrypt}", "BCRYPT加密"),
    NOOP("{noop}", "明文");
    private final String prefix;
    private final String desc;
}
