package com.netease.mingle.rpc.shared.util;

import java.util.UUID;

/**
 * UUID Utility
 * Created by Michael Jiang on 15/8/8.
 */
public class UUIDUtil {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
