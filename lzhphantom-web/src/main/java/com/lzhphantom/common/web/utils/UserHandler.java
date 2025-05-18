package com.lzhphantom.common.web.utils;

import cn.dev33.satoken.stp.StpUtil;

import java.util.List;

public class UserHandler {

    public static String getCurrentUser() {
        return StpUtil.getSession().get("username").toString();
    }

    public static Long getCurrentUserId() {
        return StpUtil.getLoginIdAsLong();
    }

    public static List<Long> getCurrentUserRoleIds() {
        return StpUtil.getSession().getModel("roleIds", List.class);
    }

    public static List<String> getCurrentUserRoles() {
        return StpUtil.getSession().getModel("roles", List.class);
    }
}
