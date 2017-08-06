package lbcy.com.cn.blacklibrary.ctl;

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
}
