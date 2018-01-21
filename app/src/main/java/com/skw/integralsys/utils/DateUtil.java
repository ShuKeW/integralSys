package com.skw.integralsys.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/19 13:56
 * @类描述 一句话描述 你的类
 */

public class DateUtil {
    public static String parceDateToStr(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
    }

}
