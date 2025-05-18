package com.lzhphantom.common.base.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum ResultCode implements IResultCode, Serializable {

    SUCCESS("000000", "成功"),
    SYSTEM_EXECUTION_ERROR("999999", "系统执行出错"),
    LACK_ONLY_ONE_TOKEN_ERROR("999998", "缺少Oo参数"),
    DUPLICATE_SUBMIT_ERROR("999997", "请勿重复提交"),
    USERNAME_OR_PASSWORD_ERROR("AA00100", "用户名或密码错误"),
    USER_NOT_EXIST("A00101", "用户不存在"),
    UNAUTHORIZED("A00401","未登录"),
    FORBIDDEN("A00403","禁止访问"),
    CLIENT_AUTHENTICATION_FAILED("A00212", "客户端认证失败"),
    ACCESS_UNAUTHORIZED("A00213", "未收取"),
    TOKEN_INVALID_OR_EXPIRED("A00214", "token非法或失效"),
    TOKEN_ACCESS_FORBIDDEN("A00215", "token禁止访问"),
    WECHAT_SIGNATURE_ERROR("PWX001", "签名验证失败"),
    FLOW_LIMITING("B0210", "系统限流"),
    DEGRADATION("B0220", "系统功能降级"),
    SERVICE_NO_AUTHORITY("B0221", "服务为授权"), NOT_FOUND_ERROR("000404", "该记录不存在");

    private final String code;
    private final String msg;

}
