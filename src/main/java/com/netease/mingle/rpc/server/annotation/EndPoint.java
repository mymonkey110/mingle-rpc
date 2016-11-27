package com.netease.mingle.rpc.server.annotation;

import java.lang.annotation.*;

/**
 * EndPoint Server Definition Annotation
 * Created by Michael Jiang on 2016/11/27.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface EndPoint {
    String value();

    Method method();

    enum Method {
        GET, POST, HEAD, PUT, PATCH, DELETE, TRACE, CONNECT
    }
}
