package com.netease.mingle.rpc.server.annotation;

import java.lang.annotation.*;

/**
 * 服务参数注解
 * Created by Michael Jiang on 2016/11/27.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {
    /**
     * 参数名
     */
    String value();

    /**
     * 可选，默认必选
     */
    boolean optional = false;

    /**
     * 可选时，如果客户端不传时的默认值
     */
    String defaultValue();
}
