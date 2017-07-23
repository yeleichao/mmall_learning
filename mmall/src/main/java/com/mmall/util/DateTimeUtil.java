package com.mmall.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by yeleichao on 2017-7-15.
 */
public class DateTimeUtil {

    //joda-time

    //str->date
    //date->str

    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将字符串转化为Date类型
     * @param dateTimeStr 时间字符串
     * @param formatStr 转化格式
     * @return
     */
    public static Date strToDate(String dateTimeStr, String formatStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * 将Date类型转化为字符串类型
     * @param date 时间
     * @param formatStr 转化格式
     * @return
     */
    public static String dateToStr(Date date, String formatStr){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    /**
     * 将字符串转化为Date类型(标准版时间格式)
     * @param dateTimeStr 时间字符串
     * @param formatStr 转化格式
     * @return
     */
    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * 将Date类型转化为字符串类型(标准版时间格式)
     * @param date 时间
     * @param formatStr 转化格式
     * @return
     */
    public static String dateToStr(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }


    public static void main(String args[]){
        System.out.println(strToDate("2017-07-15","yyyy-MM-dd"));
        System.out.println(DateTimeUtil.dateToStr(new Date(), "yyyy年MM月dd日 HH:mm:ss"));
        System.out.println(dateToStr(new Date()));
    }
}
