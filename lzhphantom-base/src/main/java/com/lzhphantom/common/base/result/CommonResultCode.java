package com.lzhphantom.common.base.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum CommonResultCode implements IResultCode, Serializable {
    IDEMPOTENT_ILLEGAL("C00001","请勿重复请求！"),
    IDEMPOTENT_KEY_NULL("C00002","idempotent key 不能为空！");
    private final String code;
    private final String msg;
}
