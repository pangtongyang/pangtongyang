package lbcy.com.cn.purplelibrary.ctl;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import lbcy.com.cn.purplelibrary.entity.AlarmClockInfo;
import lbcy.com.cn.purplelibrary.entity.SportData;
import lbcy.com.cn.purplelibrary.manager.PurpleDeviceManager;

/**
 * Created by chenjie on 2017/8/31.
 */

public interface DeviceControllerNew {
    /**
     * 设置上下文
     * @param context 上下文
     */
    void setContext(Context context);

    /**
     * 同步时间
     */
    void syncTime();

    /**
     * 获取电量
     * @param dataListener 数据回调
     */
    void getBattery(DataListener<Long> dataListener);

    /**
     * 勿扰开关
     * @param isDisturb 手环是否可震动，true -> 是， false -> 否
     */
    void setDisturb(boolean isDisturb);

    /**
     * 抬腕显示
     * @param isHandUp 是否抬腕显示，true -> 是， false -> 否
     */
    void setHandUp(boolean isHandUp);

    /**
     * 触摸震动
     * @param isVibrate 是否触摸震动
     */
    void setVibrate(boolean isVibrate);

    /**
     * 闹钟设置
     * @param id 闹钟id，0或1
     * @param hour 小时
     * @param minute 分钟
     * @param week 重复设置 0-0-1-0-0-0-0 代表周三重复
     * @param state 开关状态
     */
    void setAlarm(int id, int hour, int minute, String week, boolean state);

    /**
     * 读取设备信息
     */
    void readDeviceConfig();

    /**
     * 通知推送
     * @param title 标题
     * @param content 内容
     */
    void sendNotification(String title, String content);

    /**
     * 获取运动数据
     * @param listener 当前步数回调
     */
    void getSportData(DataListener<String> listener);

    /**
     * 获取历史运动数据
     * @param listener 数据回调，包含时间date、步数step
     */
    void getSportHistory(DataListener<List<SportData>> listener);

    /**
     * 获取睡眠数据
     * @param listener 睡眠数据回调
     */
    void getSleepData(DataListener<Bundle> listener);

}
