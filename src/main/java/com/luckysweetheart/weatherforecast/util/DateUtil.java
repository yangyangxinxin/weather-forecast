package com.luckysweetheart.weatherforecast.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {
    public static Date getDate(String str, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException("CommonUtil.getDate error|str:" + str + ",pattern:" + pattern);
        }
        return date;
    }

    public static String formatNow() {
        return formatDate(new Date());
    }

    public static String formatNow(String pattern) {
        return formatDate(new Date(), pattern);
    }

    public static String formatDate(Date date) {
        String strDate = null;
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strDate = sdf.format(date);
        }
        return strDate;
    }

    public static String formatDate(Date date, String pattern) {
        String strDate = null;
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            strDate = sdf.format(date);
        }
        return strDate;
    }

    /**
     * 得到当前月的最后时刻
     *
     * @param date
     * @return
     */
    public static Date getLastMonthTime() {
        return getLastMonthTime(new Date());
    }

    /**
     * 得到当前月的最后时刻
     *
     * @param date
     * @return
     */
    public static Date getLastMonthTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        return cal.getTime();
    }

    /**
     * 得到当前月的最后时刻
     *
     * @param date
     * @return
     */
    public static Date getNextMonthTime() {
        return getNextMonthTime(new Date());
    }

    /**
     * 得到当前月的最后时刻
     *
     * @param date
     * @return
     */
    public static Date getNextMonthTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }


    /**
     * 得到当前月的最后时刻
     *
     * @param date
     * @return
     */
    public static Date getDayBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

    /**
     * 设置时间，以累加减方式
     *
     * @param date
     * @param type
     * @param num
     * @return
     */
    public static Date setDateByAdd(Date date, int type, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(type, cal.get(type) + num);
        return cal.getTime();
    }

    /**
     * 获得一个对当前时间进行加减后的时间
     *
     * @param d 时间类型 如Calendar.DATE
     * @param n 加减值
     * @return Date
     */
    public static Date getChangeDate(int d, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(d, n);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获得一个对传入时间进行加减后的时间
     *
     * @param d 时间类型 如Calendar.DATE
     * @param n 加减值
     * @param t 加减起始时间
     * @return Date
     */
    public static Date getChangeDate(int d, int n, Date t) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(t);
        calendar.add(d, n);
        Date date = calendar.getTime();
        return date;
    }

    //获取最近一周最近一个月等起始日期
    public static Date[] getStartEndTime(String timeType) {
        Date[] dates = new Date[2];
        if (StringUtils.isNotBlank(timeType)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());//日期初始化为今天
            if ("typeA".equals(timeType)) {//最近一周
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 6);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                dates[0] = calendar.getTime();
                calendar.add(Calendar.DAY_OF_MONTH, 7);
                calendar.add(Calendar.SECOND, -1);
                dates[1] = calendar.getTime();
            } else if ("typeB".equals(timeType)) {//最近两周
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 13);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                dates[0] = calendar.getTime();
                calendar.add(Calendar.DAY_OF_MONTH, 14);
                calendar.add(Calendar.SECOND, -1);
                dates[1] = calendar.getTime();
            } else if ("typeC".equals(timeType)) {//最近一个月
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.add(Calendar.MONTH, -1);
                dates[0] = calendar.getTime();
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.add(Calendar.SECOND, -1);
                calendar.add(Calendar.MONTH, 1);
                dates[1] = calendar.getTime();
            } else if ("typeD".equals(timeType)) {//最近三个月
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.add(Calendar.MONTH, -3);
                dates[0] = calendar.getTime();
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.add(Calendar.SECOND, -1);
                calendar.add(Calendar.MONTH, 3);
                dates[1] = calendar.getTime();
            } else if ("typeE".equals(timeType)) {//全部
                dates[0] = null;
                dates[1] = null;
            }
        }
        return dates;
    }

    /*public static void main(String[] args) {
        Date[] dates = getStartEndTime("typeD");
        for (Date date : dates) {
            System.out.println(formatDate(date));
        }
    }*/
}
