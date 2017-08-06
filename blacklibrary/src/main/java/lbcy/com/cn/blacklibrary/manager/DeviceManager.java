package lbcy.com.cn.blacklibrary.manager;

import android.content.Context;

import com.huichenghe.bleControl.Ble.BleForFindDevice;
import com.huichenghe.bleControl.Ble.DataSendCallback;

import lbcy.com.cn.blacklibrary.ble.DataCallback;
import lbcy.com.cn.blacklibrary.ctl.DeviceController;

/**
 * Created by chenjie on 2017/8/6.
 */

public class DeviceManager implements DeviceController {

    private static DeviceController manager;
    private Context context;

    private DeviceManager() {
        if (context == null) {
            throw new NullPointerException("have not init");
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static DeviceController getInstance() {
        if (manager == null) {
            synchronized (DeviceManager.class) {
                if (manager == null) {
                    manager = new DeviceManager();
                }
            }
        }
        return manager;
    };

    @Override
    public void findDevice(boolean isFind, final DataCallback callback) {
        BleForFindDevice.getBleForFindDeviceInstance().setListener(new DataSendCallback() {
            @Override
            public void sendSuccess(byte[] bytes) {
                callback.OnSuccess(bytes);
            }

            @Override
            public void sendFailed() {

            }

            @Override
            public void sendFinished() {

            }
        });
        BleForFindDevice.getBleForFindDeviceInstance().findConnectedDevice(isFind?(byte)0x00:(byte)0x01);
    }


}
