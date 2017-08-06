package lbcy.com.cn.wristband;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.huichenghe.bleControl.Ble.BleForFindDevice;

import lbcy.com.cn.blacklibrary.ble.DataCallback;
import lbcy.com.cn.blacklibrary.manager.DeviceManager;
import lbcy.com.cn.wristband.utils.HandlerTip;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DeviceManager.getInstance().findDevice(new DataCallback() {
            @Override
            public void OnSuccess(byte[] data) {

            }

            @Override
            public void OnFailed() {

            }

            @Override
            public void OnFinished() {

            }
        });
        BleForFindDevice.getBleForFindDeviceInstance().findConnectedDevice((byte) 0x00);
    }
}
