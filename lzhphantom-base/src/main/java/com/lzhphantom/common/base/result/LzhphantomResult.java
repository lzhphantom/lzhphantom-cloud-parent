package com.lzhphantom.common.base.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * 响应信息主体
 *
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LzhphantomResult<T> implements Serializable {
    private String code;
    private String msg;
    private T data;
    private Integer total;
    private boolean success = false;

    public static <T> LzhphantomResult<T> ok() {
        return ok(null);
    }

    public static <T> LzhphantomResult<T> ok(T data) {
        ResultCode rce = ResultCode.SUCCESS;
//        if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
//            rce = ResultCode.SYSTEM_EXECUTION_ERROR;
//        }
        return result(rce, data).setSuccess(true);
    }

    public static <T> LzhphantomResult<T> ok(T data, Long total) {
        return result(ResultCode.SUCCESS, data).setTotal(total.intValue()).setSuccess(true);
    }


    public static LzhphantomResult<?> failed() {
        return result(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), ResultCode.SYSTEM_EXECUTION_ERROR.getMsg(), null);
    }

    public static LzhphantomResult<?> unauthorized() {
        return result(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMsg(), null);
    }

    public static  LzhphantomResult<?> unauthorized(String msg) {
        return result(ResultCode.UNAUTHORIZED.getCode(), msg, null);
    }

    public static LzhphantomResult<?> forbidden() {
        return result(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMsg(), null);
    }

    public static LzhphantomResult<?> forbidden(String msg) {
        return result(ResultCode.UNAUTHORIZED.getCode(), msg, null);
    }

    public static LzhphantomResult<?> failed(String msg) {
        return result(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), msg, null);
    }

    public static LzhphantomResult<?> judge(boolean status) {
        return status ? ok() : failed();
    }

    public static LzhphantomResult<?> failed(IResultCode resultCode) {
        return result(resultCode, null);
    }

    public static LzhphantomResult<?> failed(IResultCode resultCode, String msg) {
        return result(resultCode.getCode(), msg, null);
    }

    public static <T> LzhphantomResult<T> result(IResultCode resultCode, T data) {
        return result(resultCode.getCode(), resultCode.getMsg(), data);

    }

    public static <T> LzhphantomResult<T> result(String code, String msg, T data) {
        LzhphantomResult<T> apiResult = new LzhphantomResult<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public static boolean isSuccess(LzhphantomResult<?> result) {
        return Objects.nonNull(result) && ResultCode.SUCCESS.getCode().equals(result.getCode());
    }

    public static <T> T getDataWithSuccess(LzhphantomResult<T> result) {
        if (isSuccess(result)) {
            return result.getData();
        } else {
            throw new NullPointerException("data is null");
        }
    }
}
