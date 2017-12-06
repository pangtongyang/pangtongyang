package lbcy.com.cn.blacklibrary.manager;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.huichenghe.bleControl.Ble.BluetoothLeService;
import com.huichenghe.bleControl.Ble.DeviceConfig;
import com.huichenghe.bleControl.Ble.LocalDeviceEntity;

import lbcy.com.cn.blacklibrary.ble.DeviceConnectListener;

/**
 * 黑色手环设备连接类
 * Created by chenjie on 2017/8/6.
 */

public class BlackDeviceConnectManager {
    private Context mContext;
    private DeviceConnectListener mDevice; //广播监听
    private ConnectThread connectThread; //连接线程
    private String macAddress; //mac地址
    private boolean destroyState = false; // 关闭app时置为true，用于关闭重连循环

    public BlackDeviceConnectManager(Context context, String macAddress) {
        mContext = context;
        this.macAddress = macAddress;
        registerReceiverForAllEvent();
        startService();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 重连监听中，12s监听一次
                reconnectListener();
            }
        }, 12000);

    }

    // 初始化蓝牙连接类
    private void startService() {
        Intent bleConnect = new Intent(mContext, BluetoothLeService.class);
        if (BluetoothLeService.getInstance() == null) {
            mContext.startService(bleConnect);
        }
    }

    // 启动设备连接线程
    public void connectDevice() {
        if (BluetoothLeService.getInstance() == null) {
            startService();
        }

        // 线程执行完会被回收，需要重新new一个线程来重新执行
        connectThread = new ConnectThread();
        connectThread.start();
    }

    /**
     * 注册广播
     */
    private void registerReceiverForAllEvent() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DeviceConfig.DEVICE_CONNECTE_AND_NOTIFY_SUCESSFUL);//连接成功
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙状态改变
//        filter.addAction(DeviceConfig.DEVICE_CONNECTING_AUTO);//设备在连接状态下断开连接时循环执行此方法
        mContext.registerReceiver(myReceiver, filter);
    }

    // 获取连接回调信息
    public void startConnectingCallBack(DeviceConnectListener device){
        mDevice = device;
    }

    // 重连线程
    private Thread reconnectThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (destroyState) return;
                if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()){
                    connectDevice();
                }
                try {
                    Thread.sleep(12000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    // 重连监听
    private void reconnectListener(){
        reconnectThread.start();
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
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int btState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                    if (btState == BluetoothAdapter.STATE_ON){
                        //连接设备
                        connectDevice();
                    } else if (btState == BluetoothAdapter.STATE_OFF){
                        Toast.makeText(context, "蓝牙服务已关闭，请打开蓝牙", Toast.LENGTH_SHORT).show();
                    }
                    break;
//                case DeviceConfig.DEVICE_CONNECTING_AUTO:
                    // 实测无效，暂不使用
//                    break;
            }
        }
    };

    // 注销广播监听器
    public void unregisterReceiverandStopReconnect() {
        mContext.unregisterReceiver(myReceiver);
        // 停止循环
        destroyState = true;
        // 停止重连thread
        reconnectThread.interrupt();
    }

    // 设备连接线程
    private class ConnectThread extends Thread{
        @Override
        public void run() {
            super.run();
            // 判断连接服务是否初始化
            while(BluetoothLeService.getInstance() == null){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!BluetoothLeService.getInstance().isConnectedDevice()){
                // 连接设备
                connectDevice();
            }
        }

        private void connectDevice() {
            if (BluetoothAdapter.getDefaultAdapter() != null
                    && BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                if (BluetoothLeService.getInstance() != null) {
                    BluetoothLeService.getInstance().connect(new LocalDeviceEntity("Wristband",
                            macAddress, -78, new byte[0]));
                }
            }
        }
    }

}