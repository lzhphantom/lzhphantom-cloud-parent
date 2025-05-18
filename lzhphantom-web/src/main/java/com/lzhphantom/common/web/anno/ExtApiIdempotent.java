package com.lzhphantom.common.web.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
/**
 * 幂等性接口 过时
 * 请用 @see Idempotent
 */
public @interface ExtApiIdempotent {
}
