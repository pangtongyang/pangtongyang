package lbcy.com.cn.wristband.ctl;


import lbcy.com.cn.wristband.entity.BleDevice;

/**
 * Created by chenjie on 2017/8/9.
 */

public interface BleScanCallback {
    void updateUI(BleDevice device);
}
