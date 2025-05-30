package com.lzhphantom.common.web.config.sentinel;//package com.lzhphantom.common.web.config.sentinel;
//
//import cn.hutool.http.HttpStatus;
//import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
//import com.alibaba.csp.sentinel.slots.block.BlockException;
//import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
//import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
//import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lzhphantom.common.base.result.LzhphantomResult;
//import com.lzhphantom.common.base.result.ResultCode;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//
//@Component
//public class DefaultBlockExceptionHandler implements BlockExceptionHandler {
//
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
//        response.setStatus(HttpStatus.HTTP_OK);
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json;charset=utf-8");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        // 流控
//        if (e instanceof FlowException) {
//            objectMapper.writeValue(response.getWriter(), LzhphantomResult.failed(ResultCode.FLOW_LIMITING));
//            // 降级
//        } else if (e instanceof DegradeException) {
//            objectMapper.writeValue(response.getWriter(), LzhphantomResult.failed(ResultCode.DEGRADATION));
//            // 未授权
//        } else if (e instanceof AuthorityException) {
//            objectMapper.writeValue(response.getWriter(), LzhphantomResult.failed(ResultCode.SERVICE_NO_AUTHORITY));
//        }
//    }
//}