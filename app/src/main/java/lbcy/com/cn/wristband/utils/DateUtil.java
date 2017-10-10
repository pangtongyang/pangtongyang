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
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(new java.util.Date());
    }

    /**
     * 获取当前年月日
     * @param isNextDay 是否是后一天
     * @return 返回 年-月-日
     */
    public static String getCurrentTime_Y_M_d(boolean isNextDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        if (isNextDay)
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        return  sdf.format(calendar.getTime());
    }

    /**
     * 获取指定时间戳
     * @param time 时间
     * @return f返回时间戳
     */
    public static long getBookMillis(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
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
}
