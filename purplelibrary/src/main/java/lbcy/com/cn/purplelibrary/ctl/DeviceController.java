package lbcy.com.cn.purplelibrary.ctl;

import lbcy.com.cn.purplelibrary.entity.AlarmClockInfo;

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
     * 获取数据（自行处理返回值，包含运动数据、睡眠数据、心率数据）
     */
    void getData();

    /**
     * 手环升级请求（自行处理返回值）
     * @param filePath 用于手环升级的文件路径
     */
    void updateDevice(String filePath);

    /**
     * 判断手环是否连接（自行处理返回值）
     */
    void isLinked();
}
