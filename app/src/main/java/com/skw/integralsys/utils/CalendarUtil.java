package com.skw.integralsys.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 中国农历算法 - 实用于公历 1901 年至 2100 年之间的 200 年
 */
public class CalendarUtil {

    private int gregorianYear;

    private int gregorianMonth;

    private int gregorianDate;

    private boolean isGregorianLeap;

    private int dayOfYear;

    private int dayOfWeek;                                                                                                                                                            // 周日一星期的第一天

    public int chineseYear;

    private int chineseMonth;                                                                                                                                                        // 负数表示闰月

    private int chineseDate;

    private int sectionalTerm;

    private int principleTerm;

    private static char[] daysInGregorianMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private static String[] stemNames = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};

    private static String[] branchNames = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};

    public static String[] animalNames = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};

    public static final String[] daysOfMonth = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
            "25", "26", "27", "28", "29", "30", "31"};

    public String monthOfAlmanac[] = {"正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "腊月"};

    public String daysOfAlmanac[] = {"初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "廿一", "廿二",
            "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"};                                                                                                                                            // 农历的天数

    public CalendarUtil() {
        setGregorian(1901, 1, 1);
    }

    /**
     * 得到对应天的农历 要判断闰月 月初 月末 *
     *
     * @param y
     * @param m
     * @param d
     * @return String
     */
    public String getChineseDay(int y, int m, int d) {

        CalendarUtil c = new CalendarUtil();

        c.setGregorian(y, m, d);
        c.computeChineseFields();
        c.computeSolarTerms();

        int cd = c.getChineseDate();

        return daysOfAlmanac[cd - 1];
    }

    /**
     * 得到对应天的农历 要判断闰月 月初 月末
     *
     * @param y
     * @param m
     * @param d
     * @return
     */
    public String getChineseMonth(int y, int m, int d) {
        setGregorian(y, m, d);
        computeChineseFields();
        computeSolarTerms();
        int cd = getChineseMonth();
        if (cd < 1 || cd > 29) {// 负数是闰月
            cd = Integer.parseInt((cd + "").replace("-", ""));
        }
        return monthOfAlmanac[cd - 1];
    }

    public void setGregorian(int y, int m, int d) {
        gregorianYear = y;
        gregorianMonth = m;
        gregorianDate = d;
        isGregorianLeap = isGregorianLeapYear(y);
        dayOfYear = dayOfYear(y, m, d);
        dayOfWeek = dayOfWeek(y, m, d);
        chineseYear = 0;
        chineseMonth = 0;
        chineseDate = 0;
        sectionalTerm = 0;
        principleTerm = 0;
    }

    // 判断是否是闰年
    public static boolean isGregorianLeapYear(int year) {
        boolean isLeap = false;
        if (year % 4 == 0) isLeap = true;
        if (year % 100 == 0) isLeap = false;
        if (year % 400 == 0) isLeap = true;
        return isLeap;
    }

    // 返回一个月有几天
    public static int daysInGregorianMonth(int y, int m) {
        int d = daysInGregorianMonth[m - 1];
        if (m == 2 && isGregorianLeapYear(y)) d++; // 公历闰年二月多一天
        return d;
    }

    // 计算当前天在本年中是第几天
    public static int dayOfYear(int y, int m, int d) {
        int c = 0;
        for (int i = 1; i < m; i++) {
            c = c + daysInGregorianMonth(y, i);
        }
        c = c + d;
        return c;
    }

    // 当前天是本周的第几天 ， 从星期天开始算
    public static int dayOfWeek(int y, int m, int d) {
        int w = 1; // 公历一年一月一日是星期一，所以起始值为星期日
        y = (y - 1) % 400 + 1; // 公历星期值分部 400 年循环一次
        int ly = (y - 1) / 4; // 闰年次数
        ly = ly - (y - 1) / 100;
        ly = ly + (y - 1) / 400;
        int ry = y - 1 - ly; // 常年次数
        w = w + ry; // 常年星期值增一
        w = w + 2 * ly; // 闰年星期值增二
        w = w + dayOfYear(y, m, d);
        w = (w - 1) % 7 + 1;
        return w;
    }

    // 农历月份大小压缩表，两个字节表示一年。两个字节共十六个二进制位数，
    // 前四个位数表示闰月月份，后十二个位数表示十二个农历月份的大小。
    private static char[] chineseMonths = {0x00, 0x04, 0xad, 0x08, 0x5a, 0x01, 0xd5, 0x54, 0xb4, 0x09, 0x64, 0x05, 0x59, 0x45, 0x95, 0x0a, 0xa6, 0x04, 0x55, 0x24, 0xad, 0x08, 0x5a, 0x62,
            0xda, 0x04, 0xb4, 0x05, 0xb4, 0x55, 0x52, 0x0d, 0x94, 0x0a, 0x4a, 0x2a, 0x56, 0x02, 0x6d, 0x71, 0x6d, 0x01, 0xda, 0x02, 0xd2, 0x52, 0xa9, 0x05, 0x49, 0x0d, 0x2a, 0x45, 0x2b, 0x09, 0x56,
            0x01, 0xb5, 0x20, 0x6d, 0x01, 0x59, 0x69, 0xd4, 0x0a, 0xa8, 0x05, 0xa9, 0x56, 0xa5, 0x04, 0x2b, 0x09, 0x9e, 0x38, 0xb6, 0x08, 0xec, 0x74, 0x6c, 0x05, 0xd4, 0x0a, 0xe4, 0x6a, 0x52, 0x05,
            0x95, 0x0a, 0x5a, 0x42, 0x5b, 0x04, 0xb6, 0x04, 0xb4, 0x22, 0x6a, 0x05, 0x52, 0x75, 0xc9, 0x0a, 0x52, 0x05, 0x35, 0x55, 0x4d, 0x0a, 0x5a, 0x02, 0x5d, 0x31, 0xb5, 0x02, 0x6a, 0x8a, 0x68,
            0x05, 0xa9, 0x0a, 0x8a, 0x6a, 0x2a, 0x05, 0x2d, 0x09, 0xaa, 0x48, 0x5a, 0x01, 0xb5, 0x09, 0xb0, 0x39, 0x64, 0x05, 0x25, 0x75, 0x95, 0x0a, 0x96, 0x04, 0x4d, 0x54, 0xad, 0x04, 0xda, 0x04,
            0xd4, 0x44, 0xb4, 0x05, 0x54, 0x85, 0x52, 0x0d, 0x92, 0x0a, 0x56, 0x6a, 0x56, 0x02, 0x6d, 0x02, 0x6a, 0x41, 0xda, 0x02, 0xb2, 0xa1, 0xa9, 0x05, 0x49, 0x0d, 0x0a, 0x6d, 0x2a, 0x09, 0x56,
            0x01, 0xad, 0x50, 0x6d, 0x01, 0xd9, 0x02, 0xd1, 0x3a, 0xa8, 0x05, 0x29, 0x85, 0xa5, 0x0c, 0x2a, 0x09, 0x96, 0x54, 0xb6, 0x08, 0x6c, 0x09, 0x64, 0x45, 0xd4, 0x0a, 0xa4, 0x05, 0x51, 0x25,
            0x95, 0x0a, 0x2a, 0x72, 0x5b, 0x04, 0xb6, 0x04, 0xac, 0x52, 0x6a, 0x05, 0xd2, 0x0a, 0xa2, 0x4a, 0x4a, 0x05, 0x55, 0x94, 0x2d, 0x0a, 0x5a, 0x02, 0x75, 0x61, 0xb5, 0x02, 0x6a, 0x03, 0x61,
            0x45, 0xa9, 0x0a, 0x4a, 0x05, 0x25, 0x25, 0x2d, 0x09, 0x9a, 0x68, 0xda, 0x08, 0xb4, 0x09, 0xa8, 0x59, 0x54, 0x03, 0xa5, 0x0a, 0x91, 0x3a, 0x96, 0x04, 0xad, 0xb0, 0xad, 0x04, 0xda, 0x04,
            0xf4, 0x62, 0xb4, 0x05, 0x54, 0x0b, 0x44, 0x5d, 0x52, 0x0a, 0x95, 0x04, 0x55, 0x22, 0x6d, 0x02, 0x5a, 0x71, 0xda, 0x02, 0xaa, 0x05, 0xb2, 0x55, 0x49, 0x0b, 0x4a, 0x0a, 0x2d, 0x39, 0x36,
            0x01, 0x6d, 0x80, 0x6d, 0x01, 0xd9, 0x02, 0xe9, 0x6a, 0xa8, 0x05, 0x29, 0x0b, 0x9a, 0x4c, 0xaa, 0x08, 0xb6, 0x08, 0xb4, 0x38, 0x6c, 0x09, 0x54, 0x75, 0xd4, 0x0a, 0xa4, 0x05, 0x45, 0x55,
            0x95, 0x0a, 0x9a, 0x04, 0x55, 0x44, 0xb5, 0x04, 0x6a, 0x82, 0x6a, 0x05, 0xd2, 0x0a, 0x92, 0x6a, 0x4a, 0x05, 0x55, 0x0a, 0x2a, 0x4a, 0x5a, 0x02, 0xb5, 0x02, 0xb2, 0x31, 0x69, 0x03, 0x31,
            0x73, 0xa9, 0x0a, 0x4a, 0x05, 0x2d, 0x55, 0x2d, 0x09, 0x5a, 0x01, 0xd5, 0x48, 0xb4, 0x09, 0x68, 0x89, 0x54, 0x0b, 0xa4, 0x0a, 0xa5, 0x6a, 0x95, 0x04, 0xad, 0x08, 0x6a, 0x44, 0xda, 0x04,
            0x74, 0x05, 0xb0, 0x25, 0x54, 0x03};

    // 初始日，公历农历对应日期：
    // 公历 1901 年 1 月 1 日，对应农历 4598 年 11 月 11 日
    private static int baseYear = 1901;

    private static int baseMonth = 1;

    private static int baseDate = 1;

    private static int baseIndex = 0;

    private static int baseChineseYear = 1901 - 1;

    private static int baseChineseMonth = 11;

    private static int baseChineseDate = 11;

    public int computeChineseFields() {
        if (gregorianYear < 1901 || gregorianYear > 2100) return 1;
        int startYear = baseYear;
        int startMonth = baseMonth;
        int startDate = baseDate;
        chineseYear = baseChineseYear;
        chineseMonth = baseChineseMonth;
        chineseDate = baseChineseDate;
        // 第二个对应日，用以提高计算效率
        // 公历 2000 年 1 月 1 日，对应农历 1999 年 11 月 25 日
        if (gregorianYear >= 2000) {
            startYear = baseYear + 99;
            startMonth = 1;
            startDate = 1;
            chineseYear = baseChineseYear + 99;
            chineseMonth = 11;
            chineseDate = 25;
        }
        int daysDiff = 0;
        for (int i = startYear; i < gregorianYear; i++) {
            daysDiff += 365;
            if (isGregorianLeapYear(i)) daysDiff += 1; // leap year
        }
        for (int i = startMonth; i < gregorianMonth; i++) {
            daysDiff += daysInGregorianMonth(gregorianYear, i);
        }
        daysDiff += gregorianDate - startDate;

        chineseDate += daysDiff;
        int lastDate = daysInChineseMonth(chineseYear, chineseMonth);
        int nextMonth = nextChineseMonth(chineseYear, chineseMonth);
        while (chineseDate > lastDate) {
            if (Math.abs(nextMonth) < Math.abs(chineseMonth)) chineseYear++;
            chineseMonth = nextMonth;
            chineseDate -= lastDate;
            lastDate = daysInChineseMonth(chineseYear, chineseMonth);
            nextMonth = nextChineseMonth(chineseYear, chineseMonth);
        }
        return 0;
    }

    private static int[] bigLeapMonthYears = {
            // 大闰月的闰年年份
            6, 14, 19, 25, 33, 36, 38, 41, 44, 52, 55, 79, 117, 136, 147, 150, 155, 158, 185, 193};

    public static int daysInChineseMonth(int y, int m) {
        // 注意：闰月 m < 0
        int index = y - baseChineseYear + baseIndex;
        int v = 0;
        int l = 0;
        int d = 30;
        if (1 <= m && m <= 8) {
            v = chineseMonths[2 * index];
            l = m - 1;
            if (((v >> l) & 0x01) == 1) d = 29;
        } else if (9 <= m && m <= 12) {
            v = chineseMonths[2 * index + 1];
            l = m - 9;
            if (((v >> l) & 0x01) == 1) d = 29;
        } else {
            v = chineseMonths[2 * index + 1];
            v = (v >> 4) & 0x0F;
            if (v != Math.abs(m)) {
                d = 0;
            } else {
                d = 29;
                for (int i = 0; i < bigLeapMonthYears.length; i++) {
                    if (bigLeapMonthYears[i] == index) {
                        d = 30;
                        break;
                    }
                }
            }
        }
        return d;
    }

    public static int nextChineseMonth(int y, int m) {
        int n = Math.abs(m) + 1;
        if (m > 0) {
            int index = y - baseChineseYear + baseIndex;
            int v = chineseMonths[2 * index + 1];
            v = (v >> 4) & 0x0F;
            if (v == m) n = -m;
        }
        if (n == 13) n = 1;
        return n;
    }

    private static char[][] sectionalTermMap = {{7, 6, 6, 6, 6, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 5, 5, 5, 5, 5, 4, 5, 5},
            {5, 4, 5, 5, 5, 4, 4, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 3, 4, 4, 4, 3, 3, 4, 4, 3, 3, 3}, {6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 5},
            {5, 5, 6, 6, 5, 5, 5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 4, 4, 5, 5, 4, 4, 4, 5, 4, 4, 4, 4, 5}, {6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 5},
            {6, 6, 7, 7, 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 5},
            {7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6, 7, 7, 6, 6, 6, 7, 7},
            {8, 8, 8, 9, 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6, 7, 7, 7},
            {8, 8, 8, 9, 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 7}, {9, 9, 9, 9, 8, 9, 9, 9, 8, 8, 9, 9, 8, 8, 8, 9, 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 8},
            {8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6, 7, 7, 7}, {7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6, 7, 7, 6, 6, 6, 7, 7}};

    private static char[][] sectionalTermYear = {{13, 49, 85, 117, 149, 185, 201, 250, 250}, {13, 45, 81, 117, 149, 185, 201, 250, 250}, {13, 48, 84, 112, 148, 184, 200, 201, 250},
            {13, 45, 76, 108, 140, 172, 200, 201, 250}, {13, 44, 72, 104, 132, 168, 200, 201, 250}, {5, 33, 68, 96, 124, 152, 188, 200, 201}, {29, 57, 85, 120, 148, 176, 200, 201, 250},
            {13, 48, 76, 104, 132, 168, 196, 200, 201}, {25, 60, 88, 120, 148, 184, 200, 201, 250}, {16, 44, 76, 108, 144, 172, 200, 201, 250}, {28, 60, 92, 124, 160, 192, 200, 201, 250},
            {17, 53, 85, 124, 156, 188, 200, 201, 250}};

    private static char[][] principleTermMap = {{21, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 20, 20, 20, 20, 20, 19, 20, 20, 20, 19, 19, 20},
            {20, 19, 19, 20, 20, 19, 19, 19, 19, 19, 19, 19, 19, 18, 19, 19, 19, 18, 18, 19, 19, 18, 18, 18, 18, 18, 18, 18},
            {21, 21, 21, 22, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 21, 20, 20, 20, 20, 19, 20, 20, 20, 20},
            {20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 21, 20, 20, 20, 20, 19, 20, 20, 20, 19, 19, 20, 20, 19, 19, 19, 20, 20},
            {21, 22, 22, 22, 21, 21, 22, 22, 21, 21, 21, 22, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 21, 21},
            {22, 22, 22, 22, 21, 22, 22, 22, 21, 21, 22, 22, 21, 21, 21, 22, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 21},
            {23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 22, 22, 22, 22, 23},
            {23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 23},
            {23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 23},
            {24, 24, 24, 24, 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 23},
            {23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 22, 22, 22, 22, 21, 22, 22, 22, 21, 21, 22, 22, 22},
            {22, 22, 23, 23, 22, 22, 22, 23, 22, 22, 22, 22, 21, 22, 22, 22, 21, 21, 22, 22, 21, 21, 21, 22, 21, 21, 21, 21, 22}};

    private static char[][] principleTermYear = {{13, 45, 81, 113, 149, 185, 201}, {21, 57, 93, 125, 161, 193, 201}, {21, 56, 88, 120, 152, 188, 200, 201},
            {21, 49, 81, 116, 144, 176, 200, 201}, {17, 49, 77, 112, 140, 168, 200, 201}, {28, 60, 88, 116, 148, 180, 200, 201}, {25, 53, 84, 112, 144, 172, 200, 201},
            {29, 57, 89, 120, 148, 180, 200, 201}, {17, 45, 73, 108, 140, 168, 200, 201}, {28, 60, 92, 124, 160, 192, 200, 201}, {16, 44, 80, 112, 148, 180, 200, 201},
            {17, 53, 88, 120, 156, 188, 200, 201}};

    public int computeSolarTerms() {
        if (gregorianYear < 1901 || gregorianYear > 2100) return 1;
        sectionalTerm = sectionalTerm(gregorianYear, gregorianMonth);
        principleTerm = principleTerm(gregorianYear, gregorianMonth);
        return 0;
    }

    public static int sectionalTerm(int y, int m) {
        if (y < 1901 || y > 2100) return 0;
        int index = 0;
        int ry = y - baseYear + 1;
        while (ry >= sectionalTermYear[m - 1][index])
            index++;
        int term = sectionalTermMap[m - 1][4 * index + ry % 4];
        if ((ry == 121) && (m == 4)) term = 5;
        if ((ry == 132) && (m == 4)) term = 5;
        if ((ry == 194) && (m == 6)) term = 6;
        return term;
    }

    public static int principleTerm(int y, int m) {
        if (y < 1901 || y > 2100) return 0;
        int index = 0;
        int ry = y - baseYear + 1;
        while (ry >= principleTermYear[m - 1][index])
            index++;
        int term = principleTermMap[m - 1][4 * index + ry % 4];
        if ((ry == 171) && (m == 3)) term = 21;
        if ((ry == 181) && (m == 5)) term = 21;
        return term;
    }

    // 当前时间是星期几
    public static String getWeekOfDate() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        Date curDate = new Date(System.currentTimeMillis());
        cal.setTime(curDate);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        return weekDays[w];
    }

    // 当前时间是周几
    public static String getWeekOfToday() {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        Date curDate = new Date(System.currentTimeMillis());
        cal.setTime(curDate);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        return weekDays[w];
    }

    // 明天时间是周几
    public static String getWeekOfComDay() {
        String[] weekDays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        Calendar cal = Calendar.getInstance();
        Date curDate = new Date(System.currentTimeMillis());
        cal.setTime(curDate);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        return weekDays[w];
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("阳历年: " + gregorianYear + "\n");
        buf.append("阳历月: " + gregorianMonth + "\n");
        buf.append("阳历日期: " + gregorianDate + "\n");
        buf.append("是否闰年: " + isGregorianLeap + "\n");
        buf.append("一年多少天: " + dayOfYear + "\n");
        buf.append("多少星期: " + dayOfWeek + "\n");
        buf.append("中国年: " + chineseYear + "\n");
        buf.append("天干: " + ((chineseYear - 1) % 10) + "\n");
        buf.append("地支: " + ((chineseYear - 1) % 12) + "\n");
        buf.append("阴历月: " + chineseMonth + "\n");
        buf.append("阴历日期: " + chineseDate + "\n");
        buf.append("Sectional Term: " + sectionalTerm + "\n");
        buf.append("Principle Term: " + principleTerm + "\n");
        return buf.toString();
    }

    public String[] getYearTable() {
        setGregorian(gregorianYear, 1, 1);
        computeChineseFields();
        computeSolarTerms();
        String[] table = new String[58]; // 6*9 + 4
        table[0] = getTextLine(27, "公历年历：" + gregorianYear);
        table[1] = getTextLine(27,
                "农历年历：" + (chineseYear + 1) + " (" + stemNames[(chineseYear + 1 - 1) % 10] + branchNames[(chineseYear + 1 - 1) % 12] + " - " + animalNames[(chineseYear + 1 - 1) % 12] + "年)");
        int ln = 2;
        String blank = "                                         " + "  " + "                                         ";
        String[] mLeft = null;
        String[] mRight = null;
        for (int i = 1; i <= 6; i++) {
            table[ln] = blank;
            ln++;
            mLeft = getMonthTable();
            mRight = getMonthTable();
            for (int j = 0; j < mLeft.length; j++) {
                String line = mLeft[j] + "  " + mRight[j];
                table[ln] = line;
                ln++;
            }
        }
        table[ln] = blank;
        ln++;
        table[ln] = getTextLine(0, "##/## - 公历日期/农历日期，(*)#月 - (闰)农历月第一天");
        ln++;
        return table;
    }

    public static String getTextLine(int s, String t) {
        String str = "                                         " + "  " + "                                         ";
        if (t != null && s < str.length() && s + t.length() < str.length())
            str = str.substring(0, s) + t + str.substring(s + t.length());
        return str;
    }

    private static String[] monthNames = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

    public String[] getMonthTable() {
        setGregorian(gregorianYear, gregorianMonth, 1);
        computeChineseFields();
        computeSolarTerms();
        String[] table = new String[8];
        String title = null;
        if (gregorianMonth < 11) title = "                   ";
        else title = "                 ";
        title = title + monthNames[gregorianMonth - 1] + "月" + "                   ";
        String header = "   日    一    二    三    四    五    六 ";
        String blank = "                                          ";
        table[0] = title;
        table[1] = header;
        int wk = 2;
        String line = "";
        for (int i = 1; i < dayOfWeek; i++) {
            line += "     " + ' ';
        }
        int days = daysInGregorianMonth(gregorianYear, gregorianMonth);
        for (int i = gregorianDate; i <= days; i++) {
            line += getDateString() + ' ';
            rollUpOneDay();
            if (dayOfWeek == 1) {
                table[wk] = line;
                line = "";
                wk++;
            }
        }
        for (int i = dayOfWeek; i <= 7; i++) {
            line += "     " + ' ';
        }
        table[wk] = line;
        for (int i = wk + 1; i < table.length; i++) {
            table[i] = blank;
        }
        for (int i = 0; i < table.length; i++) {
            table[i] = table[i].substring(0, table[i].length() - 1);
        }

        return table;
    }

    private static String[] chineseMonthNames = {"正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬", "腊"};

    private static String[] principleTermNames = {"雨水", "春分", "谷雨", "夏满", "夏至", "大暑", "处暑", "秋分", "霜降", "小雪", "冬至", "大寒"};

    private static String[] sectionalTermNames = {"立春", "惊蛰", "清明", "立夏", "芒种", "小暑", "立秋", "白露", "寒露", "立冬", "大雪", "小寒"};

    public String getDateString() {
        String str = "*  /  ";
        String gm = String.valueOf(gregorianMonth);
        if (gm.length() == 1) gm = ' ' + gm;
        String cm = String.valueOf(Math.abs(chineseMonth));
        if (cm.length() == 1) cm = ' ' + cm;
        String gd = String.valueOf(gregorianDate);
        if (gd.length() == 1) gd = ' ' + gd;
        String cd = String.valueOf(chineseDate);
        if (cd.length() == 1) cd = ' ' + cd;
        if (gregorianDate == sectionalTerm) {
            str = " " + sectionalTermNames[gregorianMonth - 1];
        } else if (gregorianDate == principleTerm) {
            str = " " + principleTermNames[gregorianMonth - 1];
        } else if (chineseDate == 1 && chineseMonth > 0) {
            str = " " + chineseMonthNames[chineseMonth - 1] + "月";
        } else if (chineseDate == 1 && chineseMonth < 0) {
            str = "*" + chineseMonthNames[-chineseMonth - 1] + "月";
        } else {
            str = gd + '/' + cd;
        }
        return str;
    }

    public int rollUpOneDay() {
        dayOfWeek = dayOfWeek % 7 + 1;
        dayOfYear++;
        gregorianDate++;
        int days = daysInGregorianMonth(gregorianYear, gregorianMonth);
        if (gregorianDate > days) {
            gregorianDate = 1;
            gregorianMonth++;
            if (gregorianMonth > 12) {
                gregorianMonth = 1;
                gregorianYear++;
                dayOfYear = 1;
                isGregorianLeap = isGregorianLeapYear(gregorianYear);
            }
            sectionalTerm = sectionalTerm(gregorianYear, gregorianMonth);
            principleTerm = principleTerm(gregorianYear, gregorianMonth);
        }
        chineseDate++;
        days = daysInChineseMonth(chineseYear, chineseMonth);
        if (chineseDate > days) {
            chineseDate = 1;
            chineseMonth = nextChineseMonth(chineseYear, chineseMonth);
            if (chineseMonth == 1) chineseYear++;
        }
        return 0;
    }

    public int getGregorianYear() {
        return gregorianYear;
    }

    public void setGregorianYear(int gregorianYear) {
        this.gregorianYear = gregorianYear;
    }

    public int getGregorianMonth() {
        return gregorianMonth;
    }

    public void setGregorianMonth(int gregorianMonth) {
        this.gregorianMonth = gregorianMonth;
    }

    public int getGregorianDate() {
        return gregorianDate;
    }

    public void setGregorianDate(int gregorianDate) {
        this.gregorianDate = gregorianDate;
    }

    public boolean isGregorianLeap() {
        return isGregorianLeap;
    }

    public void setGregorianLeap(boolean isGregorianLeap) {
        this.isGregorianLeap = isGregorianLeap;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getChineseYear() {
        return chineseYear;
    }

    public void setChineseYear(int chineseYear) {
        this.chineseYear = chineseYear;
    }

    public int getChineseMonth() {
        return chineseMonth;
    }

    public void setChineseMonth(int chineseMonth) {
        this.chineseMonth = chineseMonth;
    }

    public int getChineseDate() {
        return chineseDate;
    }

    public void setChineseDate(int chineseDate) {
        this.chineseDate = chineseDate;
    }

    public int getSectionalTerm() {
        return sectionalTerm;
    }

    public void setSectionalTerm(int sectionalTerm) {
        this.sectionalTerm = sectionalTerm;
    }

    public int getPrincipleTerm() {
        return principleTerm;
    }

    public void setPrincipleTerm(int principleTerm) {
        this.principleTerm = principleTerm;
    }

    public static char[] getDaysInGregorianMonth() {
        return daysInGregorianMonth;
    }

    public static void setDaysInGregorianMonth(char[] daysInGregorianMonth) {
        CalendarUtil.daysInGregorianMonth = daysInGregorianMonth;
    }

    public static String[] getStemNames() {
        return stemNames;
    }

    public static void setStemNames(String[] stemNames) {
        CalendarUtil.stemNames = stemNames;
    }

    public static String[] getBranchNames() {
        return branchNames;
    }

    public static void setBranchNames(String[] branchNames) {
        CalendarUtil.branchNames = branchNames;
    }

    public static String[] getAnimalNames() {
        return animalNames;
    }

    public static void setAnimalNames(String[] animalNames) {
        CalendarUtil.animalNames = animalNames;
    }

    public static char[] getChineseMonths() {
        return chineseMonths;
    }

    public static void setChineseMonths(char[] chineseMonths) {
        CalendarUtil.chineseMonths = chineseMonths;
    }

    public static int getBaseYear() {
        return baseYear;
    }

    public static void setBaseYear(int baseYear) {
        CalendarUtil.baseYear = baseYear;
    }

    public static int getBaseMonth() {
        return baseMonth;
    }

    public static void setBaseMonth(int baseMonth) {
        CalendarUtil.baseMonth = baseMonth;
    }

    public static int getBaseDate() {
        return baseDate;
    }

    public static void setBaseDate(int baseDate) {
        CalendarUtil.baseDate = baseDate;
    }

    public static int getBaseIndex() {
        return baseIndex;
    }

    public static void setBaseIndex(int baseIndex) {
        CalendarUtil.baseIndex = baseIndex;
    }

    public static int getBaseChineseYear() {
        return baseChineseYear;
    }

    public static void setBaseChineseYear(int baseChineseYear) {
        CalendarUtil.baseChineseYear = baseChineseYear;
    }

    public static int getBaseChineseMonth() {
        return baseChineseMonth;
    }

    public static void setBaseChineseMonth(int baseChineseMonth) {
        CalendarUtil.baseChineseMonth = baseChineseMonth;
    }

    public static int getBaseChineseDate() {
        return baseChineseDate;
    }

    public static void setBaseChineseDate(int baseChineseDate) {
        CalendarUtil.baseChineseDate = baseChineseDate;
    }

    public static int[] getBigLeapMonthYears() {
        return bigLeapMonthYears;
    }

    public static void setBigLeapMonthYears(int[] bigLeapMonthYears) {
        CalendarUtil.bigLeapMonthYears = bigLeapMonthYears;
    }

    public static char[][] getSectionalTermMap() {
        return sectionalTermMap;
    }

    public static void setSectionalTermMap(char[][] sectionalTermMap) {
        CalendarUtil.sectionalTermMap = sectionalTermMap;
    }

    public static char[][] getSectionalTermYear() {
        return sectionalTermYear;
    }

    public static void setSectionalTermYear(char[][] sectionalTermYear) {
        CalendarUtil.sectionalTermYear = sectionalTermYear;
    }

    public static char[][] getPrincipleTermMap() {
        return principleTermMap;
    }

    public static void setPrincipleTermMap(char[][] principleTermMap) {
        CalendarUtil.principleTermMap = principleTermMap;
    }

    public static char[][] getPrincipleTermYear() {
        return principleTermYear;
    }

    public static void setPrincipleTermYear(char[][] principleTermYear) {
        CalendarUtil.principleTermYear = principleTermYear;
    }

    public static String[] getMonthNames() {
        return monthNames;
    }

    public static void setMonthNames(String[] monthNames) {
        CalendarUtil.monthNames = monthNames;
    }

    public static String[] getChineseMonthNames() {
        return chineseMonthNames;
    }

    public static void setChineseMonthNames(String[] chineseMonthNames) {
        CalendarUtil.chineseMonthNames = chineseMonthNames;
    }

    public static String[] getPrincipleTermNames() {
        return principleTermNames;
    }

    public static void setPrincipleTermNames(String[] principleTermNames) {
        CalendarUtil.principleTermNames = principleTermNames;
    }

    public static String[] getSectionalTermNames() {
        return sectionalTermNames;
    }

    public static void setSectionalTermNames(String[] sectionalTermNames) {
        CalendarUtil.sectionalTermNames = sectionalTermNames;
    }

    /**
     * --------------------------合并
     **/

    private final static int MONTH_DAYS[] = {29, 30};

    private final static int DAYS_MONTH[][] = {{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}, {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}};

    private final static int DATAS[][] = {{23, 3, 2, 17, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0}, {41, 0, 4, 23, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1},
            {30, 7, 5, 28, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1}, {49, 0, 6, 33, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1}, {38, 0, 0, 38, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1},
            {26, 6, 2, 44, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0}, {45, 0, 3, 49, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0}, {35, 0, 4, 54, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1},
            {24, 4, 5, 59, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1}, {43, 0, 0, 5, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1}, {32, 0, 1, 10, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1},
            {21, 2, 2, 15, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1}, {40, 0, 3, 20, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1}, {28, 7, 5, 26, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1},
            {47, 0, 6, 31, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1}, {36, 0, 0, 36, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0}, {26, 5, 1, 41, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
            {44, 0, 3, 47, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1}, {33, 0, 4, 52, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0}, {23, 3, 5, 57, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1},
            {42, 0, 6, 2, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1}, {30, 8, 1, 8, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0}, {48, 0, 2, 13, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0},
            {38, 0, 3, 18, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, {27, 6, 4, 23, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0}, {45, 0, 6, 29, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0},
            {35, 0, 0, 34, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1}, {24, 4, 1, 39, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0}, {43, 0, 2, 44, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0},
            {32, 0, 4, 50, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1}, {20, 3, 5, 55, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0}, {39, 0, 6, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0},
            {29, 7, 0, 5, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1}, {47, 0, 2, 11, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1}, {36, 0, 3, 16, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0},
            {26, 5, 4, 21, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1}, {45, 0, 5, 26, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1}, {33, 0, 0, 32, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1},
            {22, 4, 1, 37, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1}, {41, 0, 2, 42, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1}, {30, 8, 3, 47, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1},
            {48, 0, 5, 53, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1}, {37, 0, 6, 58, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1}, {27, 6, 0, 3, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0},
            {46, 0, 1, 8, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0}, {35, 0, 3, 14, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1}, {24, 4, 4, 19, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1},
            {43, 0, 5, 24, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1}, {32, 10, 6, 29, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1}, {50, 0, 1, 35, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0},
            {39, 0, 2, 40, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1}, {28, 6, 3, 45, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0}, {47, 0, 4, 50, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {36, 0, 6, 56, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0}, {26, 5, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1}, {45, 0, 1, 6, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0},
            {34, 0, 2, 11, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0}, {22, 3, 4, 17, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0}, {40, 0, 5, 22, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0},
            {30, 8, 6, 27, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1}, {49, 0, 0, 32, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1}, {37, 0, 2, 38, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {27, 5, 3, 43, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1}, {46, 0, 4, 48, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1}, {35, 0, 5, 53, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1},
            {23, 4, 0, 59, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1}, {42, 0, 1, 4, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1}, {31, 0, 2, 9, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0},
            {21, 2, 3, 14, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1}, {39, 0, 5, 20, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1}, {28, 7, 6, 25, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1},
            {48, 0, 0, 30, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1}, {37, 0, 1, 35, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1}, {25, 5, 3, 41, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1},
            {44, 0, 4, 46, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1}, {33, 0, 5, 51, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1}, {22, 4, 6, 56, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
            {40, 0, 1, 2, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0}, {30, 9, 2, 7, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1}, {49, 0, 3, 12, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1},
            {38, 0, 4, 17, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0}, {27, 6, 6, 23, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1}, {46, 0, 0, 28, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0},
            {35, 0, 1, 33, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0}, {24, 4, 2, 38, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1}, {42, 0, 4, 44, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {31, 0, 5, 49, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0}, {21, 2, 6, 54, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1}, {40, 0, 0, 59, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {28, 6, 2, 5, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0}, {47, 0, 3, 10, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1}, {36, 0, 4, 15, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1},
            {25, 5, 5, 20, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0}, {43, 0, 0, 26, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1}, {32, 0, 1, 31, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0},
            {22, 3, 2, 36, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0}};

    private String dateToStr(Date d, int type) {
        String s = "";
        SimpleDateFormat sdf = null;
        switch (type) {
            case 1:
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            case 2:
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            case 3:
                sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        }
        s = sdf.format(d);
        return s;
    }

    /**
     * 根据阴历取得对应的阳历日期（该日期必须在1936—2028年之间）
     *
     * @param date 阴历日期（字符串）
     * @return 返回对应的阳历日期
     * @throws Exception
     */
    public String lunarTosolar(String date) {
        Date d = getGregorianCalendar(date);
        String s = dateToStr(d, 1);
        s = s.substring(0, 4) + "-" + s.substring(4, 6) + "-" + s.substring(6, 8);
        return s;

    }

    /**
     * 根据阳历取得对应的阴历日期（该日期必须在1936—2028年之间）
     *
     * @param date 阳历日期（字符串）
     * @return 返回阴历日期
     * @throws Exception
     */
    public String solarTolunar(String date) {
        Date d = getLunarCalendar(date);
        String s = dateToStr(d, 1);
        s = s.substring(0, 4) + "-" + s.substring(4, 6) + "-" + s.substring(6, 8);
        return s;

    }

    private Date getGregorianCalendar(String date) {
        Calendar c = getCalendar(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int index = year - 1936;
        int endIndex = month;

        if ((DATAS[index][1] != 0) && (month > DATAS[index][1])) {
            endIndex++;
        }

        int dayNum = 0;

        for (int i = 0; i < (endIndex - 1); i++) {
            dayNum += MONTH_DAYS[DATAS[index][4 + i]];
        }

        dayNum += day;
        dayNum += DATAS[index][0];

        int year_days = 365;

        if (isLeapYear(year)) {
            year_days = 366;
        }

        if (dayNum > year_days) {
            year++;
            dayNum -= year_days;
        }

        month = 1;

        int dayOfMonth[] = DAYS_MONTH[0];

        if (isLeapYear(year)) {
            dayOfMonth = DAYS_MONTH[1];
        }

        int i = 0;

        for (; i < 12; i++) {
            dayNum -= dayOfMonth[i];

            if (dayNum <= 0) {
                break;
            }

            month++;
        }

        day = dayOfMonth[i] + dayNum;

        return getDate(year + "-" + month + "-" + day);
    }

    private Date getLunarCalendar(String date) {
        Calendar calendar = getCalendar(date);
        int year = calendar.get(Calendar.YEAR);
        int month = 1;
        int day;

        if ((year < 1936) || (year > 2028)) {
            return null;
        }

        int index = year - 1936;
        int l_days = DATAS[index][0];
        int day_year = calendar.get(Calendar.DAY_OF_YEAR);
        int days;

        if (day_year >= l_days) {
            days = day_year - l_days;
        } else {
            index--;
            year--;

            Calendar c = getCalendar(year + "-12-31");
            days = (c.get((Calendar.DAY_OF_YEAR)) + day_year) - DATAS[index][0];
        }

        int i = 0;
        int day_num = 0;

        for (; i < 13; i++) {
            day_num += MONTH_DAYS[DATAS[index][i + 4]];

            if (day_num >= days) {
                break;
            }

            month++;
        }

        day = MONTH_DAYS[DATAS[index][i + 4]] - (day_num - days);

        if ((DATAS[index][1] != 0) && (month > DATAS[index][1])) {
            month--;
        }

        return getDate(year + "-" + month + "-" + day);
    }

    private Calendar getCalendar(String date) {
        Date dd = getDate(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dd);

        return calendar;
    }

    private Date getDate(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dd = null;

        try {
            dd = format.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dd;
    }

    private boolean isLeapYear(int year) {
        if ((year % 400) == 0) {
            return true;
        } else if ((year % 100) == 0) {
            return false;
        } else if ((year % 4) == 0) {
            return true;
        }

        return false;
    }

    public String getYangliDay(String chineseDay) {
        String yangliDay = "";
        if (chineseDay.equals("初一")) {
            yangliDay = "1";
        } else if (chineseDay.equals("初二")) {
            yangliDay = "2";
        } else if (chineseDay.equals("初三")) {
            yangliDay = "3";
        } else if (chineseDay.equals("初四")) {
            yangliDay = "4";
        } else if (chineseDay.equals("初五")) {
            yangliDay = "5";
        } else if (chineseDay.equals("初六")) {
            yangliDay = "6";
        } else if (chineseDay.equals("初七")) {
            yangliDay = "7";
        } else if (chineseDay.equals("初八")) {
            yangliDay = "8";
        } else if (chineseDay.equals("初九")) {
            yangliDay = "9";
        } else if (chineseDay.equals("初十")) {
            yangliDay = "10";
        } else if (chineseDay.equals("十一")) {
            yangliDay = "11";
        } else if (chineseDay.equals("十二")) {
            yangliDay = "12";
        } else if (chineseDay.equals("十三")) {
            yangliDay = "13";
        } else if (chineseDay.equals("十四")) {
            yangliDay = "14";
        } else if (chineseDay.equals("十五")) {
            yangliDay = "15";
        } else if (chineseDay.equals("十六")) {
            yangliDay = "16";
        } else if (chineseDay.equals("十七")) {
            yangliDay = "17";
        } else if (chineseDay.equals("十八")) {
            yangliDay = "18";
        } else if (chineseDay.equals("十九")) {
            yangliDay = "19";
        } else if (chineseDay.equals("廿十") || chineseDay.equals("二十")) {
            yangliDay = "20";
        } else if (chineseDay.equals("廿一")) {
            yangliDay = "21";
        } else if (chineseDay.equals("廿二")) {
            yangliDay = "22";
        } else if (chineseDay.equals("廿三")) {
            yangliDay = "23";
        } else if (chineseDay.equals("廿四")) {
            yangliDay = "24";
        } else if (chineseDay.equals("廿五")) {
            yangliDay = "25";
        } else if (chineseDay.equals("廿六")) {
            yangliDay = "26";
        } else if (chineseDay.equals("廿七")) {
            yangliDay = "27";
        } else if (chineseDay.equals("廿八")) {
            yangliDay = "28";
        } else if (chineseDay.equals("廿九")) {
            yangliDay = "29";
        } else if (chineseDay.equals("三十")) {
            yangliDay = "30";
        }
        return yangliDay;
    }

    public String getYangliMonth(String chineseMonth) {
        String yangliMonth = "";
        if (chineseMonth.equals("正月")) {
            yangliMonth = "1";
        } else if (chineseMonth.equals("二月")) {
            yangliMonth = "2";
        } else if (chineseMonth.equals("三月")) {
            yangliMonth = "3";
        } else if (chineseMonth.equals("四月")) {
            yangliMonth = "4";
        } else if (chineseMonth.equals("五月")) {
            yangliMonth = "5";
        } else if (chineseMonth.equals("六月")) {
            yangliMonth = "6";
        } else if (chineseMonth.equals("七月")) {
            yangliMonth = "7";
        } else if (chineseMonth.equals("八月")) {
            yangliMonth = "8";
        } else if (chineseMonth.equals("九月")) {
            yangliMonth = "9";
        } else if (chineseMonth.equals("十月")) {
            yangliMonth = "10";
        } else if (chineseMonth.equals("十一月")) {
            yangliMonth = "11";
        } else if (chineseMonth.equals("十二月") || chineseMonth.equals("腊月")) {
            yangliMonth = "12";
        }
        return yangliMonth;
    }

    public String getYangliYear(String chineseYear) {
        String yangliYear = chineseYear.substring(0, 4);
        return yangliYear;
    }

    public static void main(String[] arg) {
        CalendarUtil c = new CalendarUtil();
        StringBuilder stringBuilder = new StringBuilder();
        c.setGregorianYear(2015);
        c.setGregorianMonth(9);
        c.setGregorianDate(11);
        c.computeChineseFields();
        stringBuilder.append(c.getChineseYear());
        stringBuilder.append("年");
        stringBuilder.append(c.getChineseMonth());
        stringBuilder.append("月");
        stringBuilder.append(c.getChineseDate() + "日");
        System.out.println("arg = [" + stringBuilder.toString() + "]");

        System.out.println("阴历转阳历:" + c.lunarTosolar("2014-5-8"));
        System.out.println("阳历转阴历" + c.solarTolunar("2015-12-25"));
        System.out.println("阳历年:" + c.getYangliYear("2015年"));
    }

    public static String monthStr(int month) {
        String monthTmp = "";
        switch (month) {
            case 1:
                monthTmp = "01";
                break;
            case 2:
                monthTmp = "02";
                break;
            case 3:
                monthTmp = "03";
                break;
            case 4:
                monthTmp = "04";
                break;
            case 5:
                monthTmp = "05";
                break;
            case 6:
                monthTmp = "06";
                break;
            case 7:
                monthTmp = "07";
                break;
            case 8:
                monthTmp = "08";
                break;
            case 9:
                monthTmp = "09";
                break;
            case 10:
            case 11:
            case 12:
                monthTmp = month + "";
                break;
        }
        return monthTmp;
    }

    public static String dayStr(int day) {
        String dayTmp = "";
        switch (day) {
            case 1:
                dayTmp = "01";
                break;
            case 2:
                dayTmp = "02";
                break;
            case 3:
                dayTmp = "03";
                break;
            case 4:
                dayTmp = "04";
                break;
            case 5:
                dayTmp = "05";
                break;
            case 6:
                dayTmp = "06";
                break;
            case 7:
                dayTmp = "07";
                break;
            case 8:
                dayTmp = "08";
                break;
            case 9:
                dayTmp = "09";
                break;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
                dayTmp = day + "";
                break;
        }
        return dayTmp;
    }
}
