package lbcy.com.cn.blacklibrary.ctl;

import android.content.Context;

import com.huichenghe.bleControl.upgrande.UpdateVersionTask;

import java.util.List;

import lbcy.com.cn.blacklibrary.ble.DataCallback;

/**
 * Created by chenjie on 2017/8/6.
 */

public interface DeviceController {
    /**
     * 设置上下文
     * @param context 上下文
     */
    void setContext(Context context);
    /**
     * 查找设备
     * @param isFind true -> 查找设备， false -> 取消查找
     * @param callback 回调函数
     */
    void findDevice(boolean isFind, DataCallback<byte[]> callback);

    /**
     * 获取当天手环所有用户数据
     * @param callback 回调函数
     */
    void getDayData(DataCallback<byte[]> callback);

    /**
     * 获取手环电池电量
     * @param callback 回调函数
     */
    void getBattery(DataCallback<byte[]> callback);

    /**
     * 获取每小时步数（卡路里？）
     * @param callback 回调函数
     */
    void getEachHourStep(DataCallback<byte[]> callback);

    /**
     * 获取睡眠数据(昨天和今天)
     * @param callback 回调函数
     */
    void getSleepData(DataCallback<byte[]> callback);

    /**
     * 是否开启电话提醒
     * @param isOn true -> 开启， false -> 关闭
     */
    void phoneRemind(boolean isOn);

    /**
     * 是否有来电
     * @param isCalled true -> 来电进行中， false -> 结束来电
     * @param phoneNum 电话号码
     * @param name 姓名
     */
    void isPhoneRemind(boolean isCalled, String phoneNum, String name);

    /**
     * 设置同步时间
     * @param callback 回调函数
     */
    void synTime(DataCallback<byte[]> callback);

    /**
     * 设置时间格式
     * @param is24 true -> 24， false -> 12
     * @param callback 回调函数
     */
    void setTimeStyle(boolean is24, DataCallback callback);

    /**
     * 清除设备信息
     */
    void factoryReset();

    /**
     * 获取实时心率
     * @param callback 回调函数，返回心率16进制值
     */
    void startHeartRateListener(DataCallback<byte[]> callback);

    /**
     * 睡眠和运动目标设置
     * @param stepTarget 运动步数
     * @param sleepTimes 睡眠目标，睡多久
     * @param sleepHour 开始休息时间，小时
     * @param sleepMinute 开始休息时间，分钟
     */
    void setTarget(int stepTarget, int sleepTimes, int sleepHour, int sleepMinute);

    /**
     * 心率监测频率
     * @param time 表示时间填10表示10分钟监测一次，30就是30分钟一次
     * 关闭心率监测，设置time:1440
     */
    void setHeartRateFreq(int time);

    /**
     * 设置心率预警值（超过预警值手环就会震动报警）
     * @param maxHR 心率最大值
     * @param minHR 心率最小值
     * 关闭心率预警，设置min:0，max:200
     */
    void setHeartRateWarning(int maxHR, int minHR);

    /**
     * 设置手环参数
     * 必须设置这些参数。手环用于计算步数，疲劳度等，设置计步参数
     * @param hei 身高（175）cm
     * @param wei 体重(65)kg
     * @param gen 性别0表示男，1表示女
     * @param bir 年龄（1991-04-12）
     */
    void setBodyItem(String hei, String wei, String gen, String bir);

    /**
     * 关闭开启手环界面
     * @param isOpen true就是界面显示 false就是隐藏界面
     * @param position 要显示或关闭的第几个界面
     *（0：步数，1：心率，2：运动检测，3：里程，4，卡路里，5设置）
     */
    void setDeviceMenuState(boolean isOpen, int position);

    /**
     * 批量设置手环界面是否开启
     * @param data boolean数组
     *（0：步数，1：心率，2：运动检测，3：里程，4，卡路里，5设置）
     */
    void setAllMenuState(boolean []data);

    /**
     * 来电提醒设置
     * @param time 延迟时间，1s
     */
    void ringDelay(int time);

    /**
     * 久坐提醒
     * @param number 久坐提醒条目数，从0开始
     * @param isOpen 1表示开启 0表示关闭
     * @param beginTime 开启时间”10:30”
     * @param endTime 结束时间”16:30”
     * @param duration 间隔时间 30/60/90/120(可设置4个值)
     */
    void setSitRemind(int number, int isOpen, String beginTime, String endTime, int duration);

    /**
     * 删除久坐提醒
     * @param number 删除第几条久坐提醒
     */
    void deleteSitRemind(int number);

    /**
     * 设置app通知
     * @param type 通知app名称
     * @param title 通知标题
     * @param content 通知内容
     */
    void setAppNotification(String type, String title, String content);

    /**
     * 设置闹钟
     * @param num 闹钟编号
     * @param type 闹钟类型
     * @param hour 小时
     * @param minute 分钟
     * @param repeat_days 重复天
     */
    void setClock(int num, String type, int hour, int minute, List<String> repeat_days);

    /**
     * 删除闹钟
     * @param num 闹钟编号
     */
    void deleteClock(int num);

    /**
     * 获取硬件版本号
     * @param callback 版本号回调
     */
    void getHardwareVersion(DataCallback<String> callback);

    /**
     * 硬件版本升级
     * @param filepath 文件路径
     * @param listener 升级过程监听
     */
    void updateHardwareVersion(String filepath, UpdateVersionTask.UpdateListener listener);

    /**
     * 停止升级
     * @param updateTask 升级过程的updateTask
     */
    void stopUpdate(UpdateVersionTask updateTask);
}
