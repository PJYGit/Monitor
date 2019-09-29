package com.common.util;

import java.util.Date;

public class NumberUtil {
    public static long getTimeStrap() {
        // Date().getTime()返回的是毫秒数
        // 这里返回秒数
        return new Date().getTime() / 1000;
    }
}
