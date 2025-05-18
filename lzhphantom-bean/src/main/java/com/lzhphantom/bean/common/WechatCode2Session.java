package com.lzhphantom.bean.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WechatCode2Session {
    private String session_key;
    private String openid;
    private String unionid;
    private String errmsg;
    /**
     * 40029 code 无效 js_code无效
     * 45011 API 调用太频繁，请稍候再试
     * 40226 code blocked 高风险等级用户，小程序登录拦截 。
     * -1 system error 系统繁忙，此时请开发者稍候再试
     */
    private String errcode;
}
