package lbcy.com.cn.wristband.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chenjie on 2017/10/6.
 */

public class DateUtil {
    /**
     * 获取当前小时、分钟
     *
     * @return 返回 小时:分钟
     */
    public static String getCurrentTime_H_m() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new java.util.Date());
    }

    /**
     * 获取当前年月日时分秒
     * @return 返回 年-月-日 时:分:秒
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        return  sdf.format(calendar.getTime());
    }

    /**
     * 获取当前年月日
     * @return 返回 年-月-日
     */
    public static String getCurrentTime_Y_M_d() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        return  sdf.format(calendar.getTime());
    }

    /**
     * 获取今日或明日年月日
     * @param isNextDay 是否是后一天
     * @return 返回 年-月-日
     */
    public static String getCurrentTime_Y_M_d(boolean isNextDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        if (isNextDay)
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        return  sdf.format(calendar.getTime());
    }

    /**
     * 获取昨天年月日
     * @return 返回 年-月-日
     */
    public static String getYesterdayTime_Y_M_d() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
        return  sdf.format(calendar.getTime());
    }

    /**
     * 获取指定时间
     * @param hour 当前时间+hour的时间，正数为后边的小时，负数为前边的小时
     * @return 返回 年-月-日 时:分:秒
     */
    public static String getTime(int hour){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);
        return  sdf.format(calendar.getTime());
    }

    /**
     * 获取指定时间戳
     * @param time 时间
     * @return f返回时间戳
     */
    public static long getBookMillis(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date;
        long millis = 0;
        try {
            date = sdf.parse(time);
            millis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis;
    }

    /**
     * 字符串时间转毫秒
     * @param strTime 字符串时间
     * @return 返回毫秒
     */
    public static long stringToLong(String strTime){
        Date date; // String类型转成date类型
        date = stringToDate(strTime);
        if (date == null) {
            return 0;
        } else {
            return dateToLong(date);
        }
    }

    /**
     * Date转毫秒
     * @param date 时间
     * @return 返回毫秒
     */
    private static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 字符串时间转Date
     * @param strTime 字符串时间
     * @return 返回Date
     */
    private static Date stringToDate(String strTime){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间戳转字符串
     * @param currentTime 当前时间戳毫秒
     * @return 返回时间字符串
     */
    public static String longToString(long currentTime){
        Date date = longToDate(currentTime); // long类型转成Date类型
        return dateToString(date);
    }

    /**
     * date转时间字符串
     * @param data Date时间
     * @return 返回时间字符串
     */
    private static String dateToString(Date data) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(data);
    }

    /**
     * 时间戳毫秒转Date时间
     * @param currentTime 当前时间戳毫秒
     * @return 返回Date时间
     */
    private static Date longToDate(long currentTime){
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld); // 把date类型的时间转换为string
        return stringToDate(sDateTime);
    }
}
