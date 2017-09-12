package lbcy.com.cn.purplelibrary.service;

import android.annotation.SuppressLint;
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
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.milink.air.ble.AirDfuAndroid;
import com.milink.air.ble.Converter;
import com.milink.air.ble.DfuProgress;
import com.milink.air.ble.LEOutPutStream;
import com.milink.air.ble.OnBleHrNotification;
import com.milink.air.ble.Parser;

import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;

@SuppressLint("NewApi")
public class AirBLEService extends Service implements DfuProgress {

    private String address;

    // just for lost

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGattService mBluetoothGattService, mBluetoothGatt1530;
    private String mBluetoothDeviceAddress;
    public BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int REQUEST_ENABLE_BT = 1;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    private LEOutPutStream outStream;
    private BluetoothGattDescriptor descriptor;
    public Parser parser;
    // private ReadRssiAndData rssidata;

    public static String AIR_DATA_F2 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static String AIR_DATA_F1 = "0000fff1-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_AIR_DATA_F2 = UUID.fromString(AIR_DATA_F2);

    public final static UUID UUID_AIR_DATA_F1 = UUID.fromString(AIR_DATA_F1);

    private boolean mScanning;
    private static final long SCAN_PERIOD = 60000;

    // Implements callback methods for GATT events that the app cares about. For
    // example,
    // connection change and services discovered.

    private void EnableFFF2() {
        BluetoothGattCharacteristic characteristic;
        characteristic = mBluetoothGattService.getCharacteristic(UUID_AIR_DATA_F2);
        boolean enabled = true;
        if (characteristic != null) {
            mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

            List<BluetoothGattDescriptor> descriptorl = characteristic.getDescriptors();
            descriptor = descriptorl.get(0);
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
            }
        }
    }

    @Override
    public void onGetMessage(int code, String message, int progress) {
        System.out.println(message + "################################");
    }

    BluetoothGattCharacteristic characteristic1531, characteristic1532;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        public void receiveHeartRate() {
            if(parser!=null){
                parser.readHeartRate();
            }
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // intentAction = ACTION_GATT_CONNECTED;
                // broadcastUpdate(intentAction);

                mConnectionState = STATE_CONNECTED;
                //
                if (mBluetoothGatt.discoverServices()) {
                }

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mConnectionState = STATE_DISCONNECTED;
                if (mBluetoothGatt != null) {
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

                // scale
                mBluetoothGattService = gatt.getService(UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
                if (mBluetoothGattService != null) {
                    // 使能通知fff2
                    EnableFFF2();
                }
                mBluetoothGatt1530 = gatt.getService(UUID.fromString(AIR_FW_1530));
                characteristic1531 = mBluetoothGatt1530.getCharacteristic(UUID.fromString(AIR_FW_31));
                characteristic1532 = mBluetoothGatt1530.getCharacteristic(UUID.fromString(AIR_FW_32));
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (characteristic.getUuid().equals(UUID.fromString(AIR_FW_31))) {
                airDfu.dfuCharacteristicData(gatt, characteristic);
            }
            if (UUID_AIR_DATA_F2.equals(characteristic.getUuid())) {
                byte[] data = characteristic.getValue();
                // FFF2在得到数据之后,调用该方法解析
                // parser.readHeartRate();
                parser.getArray(data, data.length);
            }
            //Log.i("TAG","特征值改变+++++++++++++++++++++++++++++++++++++++"+Converter.byteArrayToHexString(characteristic.getValue()));
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            // TODO Auto-generated method stub
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
            // TODO Auto-generated method stub
            if (airDfu != null) {
                airDfu.startDfuPacket(gatt);
            }

            // 使能fff2之后实例化写通道
            BluetoothGattCharacteristic characteristic2 = mBluetoothGattService.getCharacteristic(UUID_AIR_DATA_F1);
            outStream = new LEOutPutStream(mBluetoothGatt, characteristic2, false);
            try {
                parser = new Parser(outStream, AirBLEService.this, address);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Intent intent = new Intent("mil.bt");
            intent.putExtra("cmd", 1);
            sendBroadcast(intent);
            super.onDescriptorWrite(gatt, descriptor, status);

        }

    };
    int index = 0;
    public static String AIR_FW_32 = "00001532-1212-EFDE-1523-785FEABCD123";
    public static String AIR_FW_31 = "00001531-1212-EFDE-1523-785FEABCD123";
    public static String AIR_FW_1530 = "00001530-1212-EFDE-1523-785FEABCD123";

    String tran(int x) {
        if (x < 16) {
            return "000" + Integer.toHexString(x);
        } else if (x < 256) {
            return "00" + Integer.toHexString(x);
        } else if (x >= 256 && x <= 4095) {
            return "0" + Integer.toHexString(x);
        } else {
            return "" + Integer.toHexString(x);
        }
    }

    // 这个地方为了省事直接回调到activity 如果是后台长连接服务需要使用其他方法传递回调信息
    public void setNotifyBt(OnBleHrNotification bt) {
        if (parser != null) {

            parser.setOnBleNotification(bt);
        }
    }

    // 设备扫描回调
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssiint, byte[] scanRecord) {
            if (address != null && device.getAddress().equals(address)) {
                connect(address);
            }
        }
    };

    public void find2connect() {
        try {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        find2connectWait();
    }

    public void find2connectWait() {

        try {

            if (mLeScanCallback != null && mScanning == false) {
                mScanning = true;
                // disconnectLast_all();
                TimerTask task = new TimerTask() {
                    public void run() {
                        // execute the task
                        mScanning = false;
                        try {
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            // e.printStackTrace();
                        }
                    }
                };
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class LocalBinder extends Binder {
        public AirBLEService getService() {
            return AirBLEService.this;
        }
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        // disconnect();
        try{
            disconnectLast_all();
        }catch (Exception e) {
            // TODO: handle exception
        }


        try {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        } catch (Exception e) {
            // TODO: handle exception
        }

        // 关闭GATT
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
            mBluetoothGattService = null;
        }
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter = null;
        }

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        // String name = intent.getStringExtra("name");
        flags = Service.START_REDELIVER_INTENT;

        if (intent != null) {

            address = intent.getStringExtra("address");

            if (address == null) {
                return super.onStartCommand(intent, flags, startId);
            }

            if (address.length() != 17) {
                return super.onStartCommand(intent, flags, startId);
            }

            if (address != null) {

                // 连接设备
                if (!initialize()) {

                }
                find2connect();

            }

        }

        return super.onStartCommand(intent, flags, startId);

        // return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null) {

            address = intent.getStringExtra("address");

            if (address != null) {

                // 连接设备
                if (!initialize()) {

                }
                find2connect();

            }

        }
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that
        // BluetoothGatt.close() is called
        // such that resources are cleaned up properly. In this particular
        // example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter
        // through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {

                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {

            return false;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The
     * connection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */

    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {

            return false;
        }

        final BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        if (device == null) {

            return false;
        }
        SPUtil sharedPreferencesDao = new SPUtil(getApplicationContext(), CommonConfiguration.SHAREDPREFERENCES_NAME);
        String deviceAddress = sharedPreferencesDao.getString("deviceAddress");
        String deviceName = sharedPreferencesDao.getString("deviceName");

        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        if(mBluetoothGatt==null || !mBluetoothGatt.connect()){
            Intent intent = new Intent();
            intent.setAction(CommonConfiguration.RESULT_FAIL_CONNECT_DEVICE_NOTIFICATION);
            intent.putExtra("deviceAddress", deviceAddress);
            intent.putExtra("deviceName", deviceName);
            sendBroadcast(intent);
            return false;
        }

        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;


        Intent intent = new Intent();
        intent.setAction(CommonConfiguration.RESULT_CONNECT_DEVICE_NOTIFICATION);
        intent.putExtra("deviceAddress", deviceAddress);
        intent.putExtra("deviceName", deviceName);
        //发出广播
        sendBroadcast(intent);

//        Intent intent1 = new Intent();
//        intent1.setAction(CommonConfiguration.ALARM_CLOCK_NOTIFICATION);
//        sendBroadcast(intent1);

//        Intent intent2 = new Intent();
//        intent2.setAction(CommonConfiguration.GET_BLE_SPORTSDATA_NOTIFICATION);
//        sendBroadcast(intent2);

        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        onDestroy();
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {

            return;
        }
        mBluetoothGatt.disconnect();
        close();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }else{
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }

    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read
     * result is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification. False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {

            return;
        }
    }

    AirDfuAndroid airDfu;


    /**
     * 启动升级
     * @param path
     */
    public void startUpdate(String path) {
        Log.i("TAG",path+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        airDfu = new AirDfuAndroid(getApplicationContext(),new DfuProgress() {
            @Override
//            int code, String message, int progress
            public void onGetMessage(int i, String s, int progress) {
                Log.i("TAG","错误码："+i+"+++++++++++++++++++++++++++++++++++++++");
                Log.i("TAG","描述事件信息："+s+"+++++++++++++++++++++++++++++++++++++++++++");
                Log.i("TAG",progress+"进度为：++++++++++++++++++++++++++++++++++++++++");
                if(i==AirDfuAndroid.DFD_CHECK_SUCCESS){
                    Intent intent = new Intent();
                    intent.setAction(CommonConfiguration.RESULT_UPDATE_DEVICE_NOTIFICATION);
                    intent.putExtra("progress",progress);
                    intent.putExtra("code", AirDfuAndroid.DFD_CHECK_SUCCESS);
                    sendBroadcast(intent);
                }else if(i==AirDfuAndroid.DFD_ERROR){
                    Intent intent = new Intent();
                    intent.setAction(CommonConfiguration.RESULT_UPDATE_DEVICE_NOTIFICATION);
                    intent.putExtra("code", AirDfuAndroid.DFD_ERROR);
                    intent.putExtra("progress",progress);
                    sendBroadcast(intent);
                }else if(i==AirDfuAndroid.DFD_HEX_NOT_FOUND){
                    Intent intent = new Intent();
                    intent.setAction(CommonConfiguration.RESULT_UPDATE_DEVICE_NOTIFICATION);
                    intent.putExtra("code", AirDfuAndroid.DFD_HEX_NOT_FOUND);
                    intent.putExtra("progress",progress);
                    sendBroadcast(intent);
                }else if(i==AirDfuAndroid.DFD_HEX_CHECK_ERROR){
                    Intent intent = new Intent();
                    intent.setAction(CommonConfiguration.RESULT_UPDATE_DEVICE_NOTIFICATION);
                    intent.putExtra("code", AirDfuAndroid.DFD_HEX_CHECK_ERROR);
                    intent.putExtra("progress",progress);
                    sendBroadcast(intent);
                }else if(i==AirDfuAndroid.DFD_IN_PROGRESS){
                    Intent intent = new Intent();
                    intent.setAction(CommonConfiguration.RESULT_UPDATE_DEVICE_NOTIFICATION);
                    intent.putExtra("code", AirDfuAndroid.DFD_IN_PROGRESS);
                    intent.putExtra("progress",progress);
                    sendBroadcast(intent);
                }
            }
        }, AIR_FW_1530, AIR_FW_32, AIR_FW_31,parser);
        try {
            airDfu.initDfu(path, mBluetoothGatt,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This
     * should be invoked only after {@code BluetoothGatt#discoverServices()}
     * completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;

        return mBluetoothGatt.getServices();
    }

    private void disconnectLast_all() {

        if (address != null) {
            final BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
            if (device != null) {
                do_disconnectall(device);
                // mDevice = null;
            }
        }
    }

    public void do_disconnectall(BluetoothDevice device) {
        try {
            do_disconnect(device);
            if (mBluetoothGatt != null) {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
                mBluetoothGatt = null;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void do_disconnect(BluetoothDevice device) {
        mConnectionState = STATE_DISCONNECTED;
        parser = null;
    }

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

    public static byte[] strToBytes(String src) {

        if (null == src || 0 == src.length() || 0 != src.length() % 2) {
            return null;
        }

        byte[] arrRes = new byte[src.length() / 2];
        StringBuffer sBuff = new StringBuffer(src);

        int i = 0;
        String sTmp = null;
        while (i < sBuff.length() - 1) {
            sTmp = src.substring(i, i + 2);
            arrRes[i / 2] = (byte) Integer.parseInt(sTmp, 16);
            i += 2;
        }

        return arrRes;
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
}
