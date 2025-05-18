package com.lzhphantom.common.web.config;

import feign.RequestInterceptor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

@Configuration
@Log4j2
public class FeignConfig {
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 让DispatcherServlet想子线程传递RequestContext
     *
     * @param servlet
     * @return 注册bean
     */
    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration(DispatcherServlet servlet) {
        servlet.setThreadContextInheritable(true);
        return new ServletRegistrationBean<>(servlet, "/**");
    }

    /**
     * 复写拦截器，在feign发送请求前去除原来的header并转发
     *
     * @return 拦截器
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return (template) -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (Objects.nonNull(attributes)) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (Objects.nonNull(headerNames)) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        //将请求头保存到模板中
                        template.header(name, values);
                    }
                    log.info("当前服务名称：{}", applicationName);
                    template.header("serviceName", applicationName);
                }
            }

        };
    }
}
