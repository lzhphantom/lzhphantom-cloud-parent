package com.lzhphantom.common.web.tool;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理工具，用于生成符合 MyBatis Plus 的 Lambda 表达式
 */
public class ProxyUtils {

    /**
     * 创建动态代理，生成 Lambda 表达式
     */
    public static <T> Object createLambdaProxy(Class<T> clazz, Method getterMethod) {
        return Proxy.newProxyInstance(
            clazz.getClassLoader(),
            new Class<?>[]{Serializable.class},
            new LambdaInvocationHandler(getterMethod)
        );
    }

    /**
     * 动态代理的处理器
     */
    private static class LambdaInvocationHandler implements InvocationHandler {
        private final Method getterMethod;

        public LambdaInvocationHandler(Method getterMethod) {
            this.getterMethod = getterMethod;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("apply")) {
                return getterMethod.invoke(args[0]);
            }
            return MethodHandles.lookup().unreflect(method);
        }
    }
}
