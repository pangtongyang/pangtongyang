package lbcy.com.cn.purplelibrary.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.milink.air.ble.AirDfuAndroid;
import com.milink.air.ble.Converter;
import com.milink.air.ble.DfuProgress;
import com.milink.air.ble.LEOutPutStream;
import com.milink.air.ble.OnBleHrNotification;
import com.milink.air.ble.Parser;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import lbcy.com.cn.purplelibrary.app.MyApplication;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.rx.RxBus;
import lbcy.com.cn.purplelibrary.utils.HandlerTip;
import lbcy.com.cn.purplelibrary.utils.SPUtil;

/**
 * 用以替代DeviceService，尚未完工
 * Created by chenjie on 2017/10/12.
 */

public class PurpleBLEService extends Service {

    private BluetoothAdapter mBluetoothAdapter;
    private static final String ACTIVITY_MANAGE_LISTENER = "ActivityListener";
    private static final int REQUEST_ENABLE_BT = 1032;
    private BluetoothGattService mBluetoothGattService;

    BluetoothLeScanner scanner;
    BluetoothGatt bluetoothGatt;
    BluetoothDevice device;

    String mac_address;

    ScanCallback scanCallBack;
    BluetoothAdapter.LeScanCallback leScanCallback;
    boolean isConnecting = false; //是否处于连接中状态

    private BluetoothGattDescriptor descriptor;

    BluetoothGattCharacteristic characteristic1531, characteristic1532;
    public static String AIR_FW_32 = "00001532-1212-EFDE-1523-785FEABCD123";
    public static String AIR_FW_31 = "00001531-1212-EFDE-1523-785FEABCD123";
    public static String AIR_FW_1530 = "00001530-1212-EFDE-1523-785FEABCD123";
    public static String AIR_DATA_F2 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static String AIR_DATA_F1 = "0000fff1-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_AIR_DATA_F2 = UUID.fromString(AIR_DATA_F2);
    public final static UUID UUID_AIR_DATA_F1 = UUID.fromString(AIR_DATA_F1);

    AirDfuAndroid airDfu;
    public Parser parser;
    private LEOutPutStream outStream;

    SPUtil userSpUtil;

    public boolean canCallBack = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        userSpUtil = new SPUtil(MyApplication.getInstances(), CommonConfiguration.SHAREDPREFERENCES_NAME);
        // 设置实例
        MyApplication.getInstances().setThread(this);
        init();
    }

    // 初始化
    private void init(){
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null){
            Toast.makeText(MyApplication.getInstances(), "本机没有找到蓝牙硬件或驱动！", Toast.LENGTH_SHORT).show();
        }

        if (!mBluetoothAdapter.isEnabled()){
            Message message = new Message();
            message.what = REQUEST_ENABLE_BT;
            RxBus.getInstance().post(ACTIVITY_MANAGE_LISTENER, message);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mac_address = intent.getStringExtra("mac_address");
        scan();
        return super.onStartCommand(intent, flags, startId);
    }

    // 蓝牙扫描
    private void scan(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if (scanCallBack == null){
                scanCallBack = new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (mac_address.equals(result.getDevice().getAddress()) && !isConnecting){
                                device = result.getDevice();
//                                scanner.stopScan(scanCallBack);
                                scanCallBack = null;
                                if (userSpUtil.getString("is_connected", "0").equals("0")){
                                    connect();
                                    isConnecting = true;
                                }

                            }
                        }
                    }

                    @Override
                    public void onScanFailed(int errorCode) {
                        super.onScanFailed(errorCode);
                    }
                };
            }
        } else {
            if (leScanCallback == null){
                leScanCallback = new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] bytes) {
                        if (mac_address.equals(bluetoothDevice.getAddress()) && !isConnecting){
                            device = bluetoothDevice;
//                            mBluetoothAdapter.stopLeScan(leScanCallback);
                            leScanCallback = null;
                            if (userSpUtil.getString("is_connected", "0").equals("0")){
                                connect();
                                isConnecting = true;
                            }

                        }
                    }
                };
            }
        }


        HandlerTip.getInstance().getHandler().postDelayed(handlerCallback, 30000);

        Toast.makeText(MyApplication.getInstances(), "正在连接设备，请稍后...", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scanner = mBluetoothAdapter.getBluetoothLeScanner();
            scanner.startScan(scanCallBack);

        } else {
            mBluetoothAdapter.startLeScan(leScanCallback);
        }
    }

    private Runnable handlerCallback = new Runnable() {
        @Override
        public void run() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//                    if (scanner != null){
//                        scanner.stopScan(scanCallBack);
//                    }
//                } else {
//                    if (mBluetoothAdapter != null){
//                        mBluetoothAdapter.stopLeScan(leScanCallback);
//                    }
//                }
            Toast.makeText(MyApplication.getInstances(), "仍未连接手环，请重启App后重试", Toast.LENGTH_SHORT).show();
        }
    };

    private void EnableFFF2() {
        BluetoothGattCharacteristic characteristic;
        characteristic = mBluetoothGattService.getCharacteristic(UUID_AIR_DATA_F2);
        if (characteristic != null) {
            bluetoothGatt.setCharacteristicNotification(characteristic, true);

            List<BluetoothGattDescriptor> descriptorl = characteristic.getDescriptors();
            descriptor = descriptorl.get(0);
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                bluetoothGatt.writeDescriptor(descriptor);
            }
        }
    }

    // 蓝牙连接
    private void connect(){
        // 连接回调
        final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    isConnecting = false;
                    if (bluetoothGatt.discoverServices()) {
                    }
                    HandlerTip.getInstance().getHandler().removeCallbacks(handlerCallback);
                    HandlerTip.getInstance().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getInstances(), "连接成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    userSpUtil.putString("is_connected", "1");
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    if (bluetoothGatt != null) {
                        bluetoothGatt.close();
                        bluetoothGatt = null;
                    }
                    userSpUtil.putString("is_connected", "0");
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                if (status == BluetoothGatt.GATT_SUCCESS) {

                    // scale
                    mBluetoothGattService = gatt.getService(UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
                    if (mBluetoothGattService != null) {
                        // 使能通知fff2
                        EnableFFF2();
                    }
                    BluetoothGattService mBluetoothGatt1530 = gatt.getService(UUID.fromString(AIR_FW_1530));
                    characteristic1531 = mBluetoothGatt1530.getCharacteristic(UUID.fromString(AIR_FW_31));
                    characteristic1532 = mBluetoothGatt1530.getCharacteristic(UUID.fromString(AIR_FW_32));
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                if (characteristic.getUuid().equals(UUID.fromString(AIR_FW_31))) {
                    airDfu.dfuCharacteristicData(gatt, characteristic);
                }
                if (UUID_AIR_DATA_F2.equals(characteristic.getUuid())) {
                    byte[] data = characteristic.getValue();
                    // FFF2在得到数据之后,调用该方法解析
                    // parser.readHeartRate();
                    parser.getArray(data, data.length);
                }
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if (airDfu != null) {
                    airDfu.dfuCharacteristicCtrl(gatt, characteristic);
                }

                super.onCharacteristicWrite(gatt, characteristic, status);
                if (UUID_AIR_DATA_F1.equals(characteristic.getUuid())) {
                    // *******回调中一定调用下面方法用于分段发送*******
                    outStream.ContinueSend();
                }
                System.out.println("Write####" + characteristic.getUuid() + "##" + Converter.byteArrayToHexString(characteristic.getValue()));
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                if (airDfu != null) {
                    airDfu.startDfuPacket(gatt);
                }

                // 使能fff2之后实例化写通道
                BluetoothGattCharacteristic characteristic2 = mBluetoothGattService.getCharacteristic(UUID_AIR_DATA_F1);
                outStream = new LEOutPutStream(bluetoothGatt, characteristic2, false);
                try {
                    parser = new Parser(outStream, PurpleBLEService.this, mac_address);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                canCallBack = true;
//                Message message = new Message();
//                message.what = CommonConfiguration.PURPLE_CAN_CALLBACK;
//
//                RxBus.getInstance().post(CommonConfiguration.PURPLE_OPERATION, message);
//                Intent intent = new Intent("mil.bt");
//                intent.putExtra("cmd", 1);
//                sendBroadcast(intent);
                super.onDescriptorWrite(gatt, descriptor, status);

            }
        };

        if (device != null){
            // 蓝牙连接
            bluetoothGatt = device.connectGatt(getBaseContext(), false, mGattCallback);
        }
    }

    /**
     * 启动升级
     * @param path
     */
    public void startUpdate(String path, final UpdateListener listener) {
        Log.i("TAG",path+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        airDfu = new AirDfuAndroid(getApplicationContext(),new DfuProgress() {
            @Override
//            int code, String message, int progress
            public void onGetMessage(int i, String s, int progress) {
                Log.i("TAG","错误码："+i+"+++++++++++++++++++++++++++++++++++++++");
                Log.i("TAG","描述事件信息："+s+"+++++++++++++++++++++++++++++++++++++++++++");
                Log.i("TAG",progress+"进度为：++++++++++++++++++++++++++++++++++++++++");
                if(i==AirDfuAndroid.DFD_CHECK_SUCCESS){
                    listener.success();
                    airDfu = null;
                }else if(i==AirDfuAndroid.DFD_ERROR){
                    listener.error();
                }else if(i==AirDfuAndroid.DFD_HEX_NOT_FOUND){
                    listener.file_not_found();
                }else if(i==AirDfuAndroid.DFD_HEX_CHECK_ERROR){
                    listener.file_check_error();
                }else if(i==AirDfuAndroid.DFD_IN_PROGRESS){
                    listener.in_progress(progress);
                }
            }
        }, AIR_FW_1530, AIR_FW_32, AIR_FW_31,parser);
        try {
            airDfu.initDfu(path, bluetoothGatt,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface UpdateListener{
        void success();
        void error();
        void file_not_found();
        void file_check_error();
        void in_progress(int progress);
    }

    /****以下是手环的一些调用方法****/
    public void setTime(Calendar time) {
        if (parser != null) {
            parser.setTime(time);
        }
    }

    public void setTime(int s, int d, int c) {
        if (parser != null) {
            parser.setTime(s, d, c);
        }
    }

    public void getSpData() {
        if (parser != null) {
            parser.getSpData();
        }
    }

    public void readDeviceConfig() {
        if (parser != null) {
            parser.readConfig();
        }
    }

    public void setHZAir(boolean on) {
        if (parser != null) {
            parser.setHZAir(on);
        }
    }

    public void sendCallInWithNumber(String string) {
        // TODO Auto-generated method stub
        if (parser != null) {
            // parser.SendCallWithNumber(string);
            try {
                parser.SendPhoneCall(string, "来电呼入");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void SendCallIncome(String string) {
        if (parser != null) {
            try {
                parser.SendPhoneCall(string, "来电呼入");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void sendCallOut() {
        // TODO Auto-generated method stub
        if (parser != null) {
            parser.SendCallEnd();
        }
    }

    public void sendMsg() {
        // TODO Auto-generated method stub
        if (parser != null) {
            parser.SendMsgIncome();
        }

    }
    /****以上是手环的一些调用方法****/

    /***
     * 设置屏幕显示
     *
     * @param languge 语言 0是中文 1是英文
     * @param handup  抬腕显示 1是 0否
     */
    public void SendDisplaySet(int languge, int handup) {
        if (parser != null) {
            parser.sendAirDisplay(languge, handup);
        }
    }

    /***
     * 按下震动
     *
     * @param on
     */
    public void SendViPressSet(boolean on) {
        if (parser != null) {
            parser.sendPressVibrate(on);
        }
    }

    public void GetSleep() {
        if (parser != null) {
            parser.SendGetSleep();
        }
    }

    public void receiveHeartRate() {
        if (parser != null) {
            parser.readHeartRate();
        }
    }


    public void AppNotification(String title, String msg) {
        if (parser != null) {
            try {
                parser.SendNotification(title, msg);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /***
     * 设备是否振动（闹钟除外）
     *
     * @param vibrate_en
     */
    public void SendVibrateSet(boolean vibrate_en) {
        if (parser != null) {
            parser.setVibrateEn(vibrate_en);
        }
    }

    public void setAlarm(int[] is, String string, String string2) {
        // TODO Auto-generated method stub
        if (parser != null) {
            parser.setAlarm(is, string, string2);
        }
    }

    /***
     * 设置步长
     *
     * @param len 厘米
     */
    public void SendStepLenSet(int len) {
        if (parser != null) {
            parser.setStepLen(len);
        }
    }

    // 回调方法
    public void setNotifyBt(OnBleHrNotification bt) {
        if (parser != null) {

            parser.setOnBleNotification(bt);
        }
    }

    private void disconnect(){
        if (bluetoothGatt != null){
            bluetoothGatt.disconnect();
            mBluetoothGattService = null;
        }
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if (scanner != null){
                scanner.stopScan(scanCallBack);
                scanCallBack = null;
                scanner = null;
            }
        } else {
            if (mBluetoothAdapter != null){
                mBluetoothAdapter.stopLeScan(leScanCallback);
                leScanCallback = null;
            }
        }

        mac_address = "";

        // 删除这个实例
        MyApplication.getInstances().removeThread();

        disconnect();
    }
}
