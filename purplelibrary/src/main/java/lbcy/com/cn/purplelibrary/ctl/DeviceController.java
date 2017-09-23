package lbcy.com.cn.purplelibrary.ctl;

import lbcy.com.cn.purplelibrary.entity.AlarmClockInfo;
import lbcy.com.cn.purplelibrary.manager.PurpleDeviceManager;

/**
 * Created by chenjie on 2017/8/31.
 */

public interface DeviceController {
    /**
     * 闹钟设置。最多设置两个闹钟
     * @param clock AlarmClockInfo对象，包含闹钟信息
     */
    void setClock(AlarmClockInfo clock);

    /**
     * 手环升级请求（自行处理返回值）
     * @param filePath 用于手环升级的文件路径
     */
    void updateDevice(String filePath);

    /**
     * 判断手环是否连接
     * @param dataListener 处理返回值，可获取字符串 "已连接"，"未连接"
     */
    void isLinked(PurpleDeviceManager.DataListener dataListener);

    /**
     * 通知获取当天运动数据
     * @param dataListener 处理返回值，获取SportInfo返回值
     */
    void getSportsData(PurpleDeviceManager.DataListener dataListener);

    /**
     * 获取某天睡眠数据
     * @param dateTime 时间
     * @param dataListener 处理返回值，获取List<SleepInfo>
     */
    void getDaySleepData(String dateTime, PurpleDeviceManager.DataListener dataListener);

    /**
     * 设备升级
     * @param uploadFilePath 升级文件路径
     * @param dataListener 升级返回值，获取Map<String, String> progress 升级进程, code 错误码
     */
    void updateDevice(String uploadFilePath, PurpleDeviceManager.DataListener dataListener);
}
