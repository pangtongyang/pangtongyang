package lbcy.com.cn.blacklibrary.ctl;

import android.content.Context;

import lbcy.com.cn.blacklibrary.ble.DataCallback;

/**
 * Created by chenjie on 2017/8/6.
 */

public interface DeviceController {
    /**
     * 查找设备
     * @param isFind true -> 查找设备， false -> 取消查找
     * @param callback 回调函数
     */
    void findDevice(boolean isFind, DataCallback callback);

    /**
     * 获取当天手环所有用户数据
     * @param callback 回调函数
     */
    void getDayData(DataCallback callback);

    /**
     * 获取手环电池电量
     * @param callback 回调函数
     */
    void getBattery(DataCallback callback);

    /**
     * 获取每小时步数（卡路里？）
     * @param callback 回调函数
     */
    void getEachHourStep(DataCallback callback);

    /**
     * 获取睡眠数据(昨天和今天)
     * @param callback 回调函数
     */
    void getSleepData(DataCallback callback);

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
}
