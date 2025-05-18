package com.lzhphantom.common.base.constants;

/**
 * @author lzhphantom
 */
public interface SecurityConstants {
    /**
     * 认证请求头key
     */
    String AUTHORIZATION_KEY = "Authorization";
    /**
     * JWT 令牌前缀
     */
    String JWT_PREFIX = "Bearer ";
    /**
     * Basic 认证前缀
     */
    String BASIC_PREFIX = "Basic ";
    /**
     * JWT 载体key
     */
    String JWT_PAYLOAD_KEY = "payload";
    /**
     * JWT ID 唯一标识
     */
    String JWT_JTI = "jti";
    /**
     * JWT 过期时间
     */
    String JWT_EXP = "exp";
    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

    /**
     * JWT 存储权限属性
     */
    String JWT_AUTHORITIES_KEY = "authorities";

    String APP_API_PATTERN = "/*/app-api/**";
    /**
     * 黑名单token前缀
     */
    String TOKEN_BLACKLIST_PREFIX = "auth:token:blacklist:";

    String THIRD_LOGIN_GITEE = "gitee";
    String THIRD_LOGIN_GITHUB = "github";
}
