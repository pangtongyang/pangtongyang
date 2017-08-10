package lbcy.com.cn.wristband.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.ctl.BleScanCallback;
import lbcy.com.cn.wristband.entity.BleDevice;
import lbcy.com.cn.wristband.test.RxAndroidBleText;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by chenjie on 2017/8/9.
 */

public class BleScanHelper {
    Context mContext;
    private RxBleClient rxBleClient;
    private Subscription scanSubscription;
    HashSet<String> hashSet;
    BleDevice bleDevice;
    BleScanCallback callback;

    public BleScanHelper(Context context) {
        this.mContext = context;
        hashSet = new HashSet<>();
    }

    public void startRxAndroidBleScan(BleScanCallback callback) {
        rxBleClient = BaseApplication.getRxBleClient(mContext);
        if (scanSubscription == null)
            scanSubscription = rxBleClient.scanBleDevices(
                    new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                            .build(),
                    new ScanFilter.Builder()
                            // add custom filters if needed
                            .build()
            )
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnUnsubscribe(this::clearSubscription)
                    .subscribe(this::addScanResult, this::onScanFailure);
        this.callback = callback;
    }

    public void stopRxAndroidBleScan() {
        if (scanSubscription != null)
            scanSubscription.unsubscribe();

    }

    public boolean isScanSubscriptionNull() {
        return scanSubscription == null;
    }

    public HashSet<String> getRxAndroidBleDevices() {
        return hashSet;
    }

    private void clearSubscription() {
        scanSubscription = null;
    }

    private void addScanResult(ScanResult scanResult) {
        String data = scanResult.getBleDevice().getName() + "###" + scanResult.getBleDevice().getMacAddress();
        String name = data.split("###")[0];
        String macAddress = data.split("###")[1];
        bleDevice = new BleDevice(scanResult.getBleDevice().getName(), scanResult.getBleDevice().getMacAddress());
        if (!hashSet.contains(data)) {
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callback.updateUI(bleDevice);
                }
            });
        }

        hashSet.add(data);
    }

    private void onScanFailure(Throwable throwable) {

        if (throwable instanceof BleScanException) {
            handleBleScanException((BleScanException) throwable);
        }
    }

    private void handleBleScanException(BleScanException bleScanException) {

        switch (bleScanException.getReason()) {
            case BleScanException.BLUETOOTH_NOT_AVAILABLE:
                Toast.makeText(mContext, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.BLUETOOTH_DISABLED:
                Toast.makeText(mContext, "Enable bluetooth and try again", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.LOCATION_PERMISSION_MISSING:
                Toast.makeText(mContext,
                        "On Android 6.0 location permission is required. Implement Runtime Permissions", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.LOCATION_SERVICES_DISABLED:
                Toast.makeText(mContext, "Location services needs to be enabled on Android 6.0", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_ALREADY_STARTED:
                Toast.makeText(mContext, "Scan with the same filters is already started", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                Toast.makeText(mContext, "Failed to register application for bluetooth scan", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED:
                Toast.makeText(mContext, "Scan with specified parameters is not supported", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_INTERNAL_ERROR:
                Toast.makeText(mContext, "Scan failed due to internal error", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES:
                Toast.makeText(mContext, "Scan cannot start due to limited hardware resources", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.UNKNOWN_ERROR_CODE:
            case BleScanException.BLUETOOTH_CANNOT_START:
            default:
                Toast.makeText(mContext, "Unable to start scanning", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
