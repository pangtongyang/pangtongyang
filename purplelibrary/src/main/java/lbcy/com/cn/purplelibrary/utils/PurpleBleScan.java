package lbcy.com.cn.purplelibrary.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.widget.Toast;

import lbcy.com.cn.purplelibrary.app.MyApplication;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.rx.RxBus;

/**
 * Created by chenjie on 2017/11/7.
 */

public class PurpleBleScan {
    private static PurpleBleScan instance = null;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner scanner;
    private ScanCallback scanCallBack;
    private BluetoothAdapter.LeScanCallback leScanCallback;
    private Context mContext;
    private String mac_address;

    private CurrentBeatsListener listener;

    private boolean isScanning = false; //是否处于扫描中状态

    private PurpleBleScan(){

    }

    public static PurpleBleScan getInstance(){
        if (instance == null){
            synchronized (PurpleBleScan.class){
                if (instance == null){
                    instance = new PurpleBleScan();
                }
            }
        }

        return instance;
    }

    public void setListener(Context context, CurrentBeatsListener listener){
        this.listener = listener;
        this.mContext = context;
        SPUtil spUtil = new SPUtil(context, CommonConfiguration.SHAREDPREFERENCES_NAME);
        mac_address = spUtil.getString("deviceAddress", "");
    }

    // 初始化扫描回调
    private void init(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if (scanCallBack == null){
                scanCallBack = new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            if (mac_address.equals(result.getDevice().getAddress())){
                                if (result.getScanRecord() == null)
                                    return;
                                String data = bytesToHexString(result.getScanRecord().getManufacturerSpecificData().valueAt(0));
                                if (data != null){
                                    String step = data.substring(12, 16);
                                    String rate = data.substring(16, 18);
                                    if (listener != null)
                                        listener.getCurrentBeats(toD(step, 16), toD(rate, 16));
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

                    }
                };
            }
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        if (mBluetoothAdapter == null)
            mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null){
            Toast.makeText(MyApplication.getInstances(), "本机没有找到蓝牙硬件或驱动！", Toast.LENGTH_SHORT).show();
        }

        if (!mBluetoothAdapter.isEnabled()){
            Toast.makeText(MyApplication.getInstances(), "蓝牙未启动", Toast.LENGTH_SHORT).show();
        }
    }

    // 开始扫描
    public void startScan(){
        if (isScanning){
            return;
        }
        init();

        isScanning = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scanner = mBluetoothAdapter.getBluetoothLeScanner();
            scanner.startScan(scanCallBack);
        } else {
            mBluetoothAdapter.startLeScan(leScanCallback);
        }
    }

    // 停止扫描
    public void stopScan(){
        if (!isScanning)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (scanner == null)
                return;
            scanner.stopScan(scanCallBack);
        } else {
            if (mBluetoothAdapter == null)
                return;
            mBluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if(src != null && src.length > 0) {
            for (byte aSrc : src) {
                int v = aSrc & 255;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }

                stringBuilder.append(hv);
            }

            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    // 任意进制数转为十进制数
    public int toD(String a, int b) {
        int r = 0;
        for (int i = 0; i < a.length(); i++) {
            r = (int) (r + formatting(a.substring(i, i + 1))
                    * Math.pow(b, a.length() - i - 1));
        }
        return r;
    }

    // 将十六进制中的字母转为对应的数字
    private int formatting(String a) {
        int i = 0;
        for (int u = 0; u < 10; u++) {
            if (a.equals(String.valueOf(u))) {
                i = u;
            }
        }
        if (a.equals("a")) {
            i = 10;
        }
        if (a.equals("b")) {
            i = 11;
        }
        if (a.equals("c")) {
            i = 12;
        }
        if (a.equals("d")) {
            i = 13;
        }
        if (a.equals("e")) {
            i = 14;
        }
        if (a.equals("f")) {
            i = 15;
        }
        return i;
    }

    // 数据回调
    public interface CurrentBeatsListener{
        void getCurrentBeats(int step, int rate);
    }

}
