package lbcy.com.cn.blacklibrary.manager;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.huichenghe.bleControl.Ble.BleScanUtils;
import com.huichenghe.bleControl.Ble.BluetoothLeService;
import com.huichenghe.bleControl.Ble.DeviceConfig;
import com.huichenghe.bleControl.Ble.LocalDeviceEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import lbcy.com.cn.blacklibrary.ble.DeviceConnect;
import lbcy.com.cn.blacklibrary.rx.RxBus;

/**
 * Created by chenjie on 2017/8/6.
 */

public class DeviceConnectManager {
    private Context mContext;
    private DeviceConnect mDevice; //广播监听
    private DeviceConnect mDevice1; //设备扫描
    private ArrayList<LocalDeviceEntity> mData = new ArrayList<>();
    private final int CLOSE_NOTE_NOT_CONNECT = 1;
    private final int RECONNECT_DEVICE = 2;
    private Intent bleConnect;


    public DeviceConnectManager(Context context) {
        mContext = context;
    }

    /**
     * 注册广播
     */
    public void registerReceiverForAllEvent(DeviceConnect device) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DeviceConfig.DEVICE_CONNECTE_AND_NOTIFY_SUCESSFUL);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(DeviceConfig.DEVICE_CONNECTING_AUTO);
        mDevice = device;
        mContext.registerReceiver(myReceiver, filter);
    }

    /**
     * 广播接收器
     */
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            switch (intent.getAction()) {   // 设备已连接的广播
                case DeviceConfig.DEVICE_CONNECTE_AND_NOTIFY_SUCESSFUL:
                    if (BluetoothLeService.getInstance() != null && BluetoothLeService.getInstance().isConnectedDevice()) {
                        mDevice.connect();
                    }
//                    closeProgressDialog();  关闭弹窗
//                    String deName = LocalDataSaveTool.getInstance(MainActivity.this).readSp(DeviceConfig.DEVICE_NAME);
//                    Intent intet = new Intent(MainActivity.this, ToolActivity.class);
//                    startActivity(intet);
//                    MainActivity.this.finish();
                    break;
            }
        }
    };

    public void startService() {
        bleConnect = new Intent(mContext, BluetoothLeService.class);
        if (BluetoothLeService.getInstance() == null) {
            mContext.startService(bleConnect);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopService() {
        if (BluetoothLeService.getInstance() != null) {
            BluetoothLeService.getInstance().stopService(bleConnect);
        }
    }

    public void unregisterReceiver() {
        mContext.unregisterReceiver(myReceiver);
    }

    public void startScan(DeviceConnect device) {
        mDevice1 = device;
        if (BluetoothAdapter.getDefaultAdapter() != null
                && BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BleScanUtils.getBleScanUtilsInstance(mContext).setmOnDeviceScanFoundListener(myDeviceListener);
            BleScanUtils.getBleScanUtilsInstance(mContext).scanDevice(null);
        } else {
            //蓝牙未开启
            Toast.makeText(mContext, "蓝牙不可用", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 实例化设备监听器，并对扫描到的设备进行监听
     */
    private BleScanUtils.OnDeviceScanFoundListener myDeviceListener = new BleScanUtils.OnDeviceScanFoundListener() {
        @Override
        public void OnDeviceFound(LocalDeviceEntity mLocalDeviceEntity) {
            String deviceName = mLocalDeviceEntity.getName();
            if (deviceName != null && deviceName.contains("_")) {
                String[] d = deviceName.split("_");
                if (!d[0].equals("B7")) {
                    if (!mData.contains(mLocalDeviceEntity)) {
                        mData.add(mLocalDeviceEntity);
                    }
                }
            } else {
                if (!mData.contains(mLocalDeviceEntity)) {
                    mData.add(mLocalDeviceEntity);
                }
            }

            Collections.sort(mData, new Comparator<LocalDeviceEntity>() {
                @Override
                public int compare(LocalDeviceEntity lhs, LocalDeviceEntity rhs) {
                    return (Integer.valueOf(rhs.getRssi())).compareTo(lhs.getRssi());
                }
            });

            mDevice1.scan(mData);
        }

        @Override
        public void onScanStateChange(boolean isChange) {
            // changeTheScanLayout(isChange);
        }
    };

    public void selectDevice(int position) {
        if (position<0||position>= mData.size()) {
            Toast.makeText(mContext, "position错误: " + position, Toast.LENGTH_LONG).show();
            return ;
        }

        BleScanUtils.getBleScanUtilsInstance(mContext).stopScan();
        LocalDeviceEntity deviceEntity = mData.get(position);// 获取设备实体类
//        showProgressDialog("" + deviceEntity.getName());  开启弹窗

        if (BluetoothLeService.getInstance() != null) {

            BluetoothLeService.getInstance().connect(deviceEntity);

            Message msg = mHandler.obtainMessage();
            msg.what = CLOSE_NOTE_NOT_CONNECT;
            msg.obj = deviceEntity;
            mHandler.sendMessageDelayed(msg, 30000);
        } else {
            Toast.makeText(mContext, "lanya", Toast.LENGTH_LONG).show();
        }
    }

    public void selectRxAndroidBleDevice(String name, String macAddress) {
        LocalDeviceEntity deviceEntity = new LocalDeviceEntity(name, macAddress, -50, null);// 获取设备实体类
        if (BluetoothLeService.getInstance() != null) {

            BluetoothLeService.getInstance().connect(deviceEntity);

        } else {
            Toast.makeText(mContext, "黑色手环服务未初始化！", Toast.LENGTH_LONG).show();
        }
    }

    public void rescan(){
        Message msg1 = mHandler.obtainMessage();
        msg1.what = RECONNECT_DEVICE;
        mHandler.sendMessageDelayed(msg1, 12000);
    }

    private boolean isFirstAlert = true;
    public void alert(){
        if (isFirstAlert){
            Message msg = mHandler.obtainMessage();
            msg.what = CLOSE_NOTE_NOT_CONNECT;
            mHandler.sendMessageDelayed(msg, 30000);
            isFirstAlert = false;
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CLOSE_NOTE_NOT_CONNECT:
                    if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
//                    closeProgressDialog();  关闭弹窗
                        if (!stopScan)
                            Toast.makeText(mContext, "仍未连接手环，请重启App后重试", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RECONNECT_DEVICE:
                    if (!stopScan){
                        // 设备重新扫描
                        mDevice.scan(null);
                    }
                    break;
            }
        }
    };

    private boolean stopScan = false;
    // 停止循环扫描，终止app时调用
    public void stopCircleScan(){
        stopScan = true;
    }
}