package lbcy.com.cn.wristband.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lbcy.com.cn.blacklibrary.manager.DeviceConnectManager;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseApplication;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by chenjie on 2017/8/7.
 */

public class RxAndroidBleText extends AppCompatActivity {
    @BindView(R.id.btn_scan)
    Button mBtnScan;
    @BindView(R.id.lv_device_list)
    ListView mLvDeviceList;
    ArrayAdapter adapter;

    private RxBleClient rxBleClient;
    private Subscription scanSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxble);
        ButterKnife.bind(this);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1);

        rxBleClient = BaseApplication.getRxBleClient(this);

        mLvDeviceList.setAdapter(adapter);

        mLvDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String macAddress = adapter.getItem(i).toString();
                Intent intent = new Intent(RxAndroidBleText.this, ConnectionExampleActivity.class);
                intent.putExtra("extra_mac_address", macAddress);
                startActivity(intent);
            }
        });

        DeviceConnectManager manager = new DeviceConnectManager(getApplicationContext());
        manager.startService();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (isScanning()) {
            /*
             * Stop scanning in onPause callback. You can use rxlifecycle for convenience. Examples are provided later.
             */
            scanSubscription.unsubscribe();
        }
    }

    private boolean isScanning() {
        return scanSubscription != null;
    }

    private void onScanFailure(Throwable throwable) {

        if (throwable instanceof BleScanException) {
            handleBleScanException((BleScanException) throwable);
        }
    }

    private void handleBleScanException(BleScanException bleScanException) {

        switch (bleScanException.getReason()) {
            case BleScanException.BLUETOOTH_NOT_AVAILABLE:
                Toast.makeText(RxAndroidBleText.this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.BLUETOOTH_DISABLED:
                Toast.makeText(RxAndroidBleText.this, "Enable bluetooth and try again", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.LOCATION_PERMISSION_MISSING:
                Toast.makeText(RxAndroidBleText.this,
                        "On Android 6.0 location permission is required. Implement Runtime Permissions", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.LOCATION_SERVICES_DISABLED:
                Toast.makeText(RxAndroidBleText.this, "Location services needs to be enabled on Android 6.0", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_ALREADY_STARTED:
                Toast.makeText(RxAndroidBleText.this, "Scan with the same filters is already started", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                Toast.makeText(RxAndroidBleText.this, "Failed to register application for bluetooth scan", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED:
                Toast.makeText(RxAndroidBleText.this, "Scan with specified parameters is not supported", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_INTERNAL_ERROR:
                Toast.makeText(RxAndroidBleText.this, "Scan failed due to internal error", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES:
                Toast.makeText(RxAndroidBleText.this, "Scan cannot start due to limited hardware resources", Toast.LENGTH_SHORT).show();
                break;
            case BleScanException.UNKNOWN_ERROR_CODE:
            case BleScanException.BLUETOOTH_CANNOT_START:
            default:
                Toast.makeText(RxAndroidBleText.this, "Unable to start scanning", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void updateButtonUIState() {
        mBtnScan.setText(isScanning() ? "停止扫描" : "开始扫描");
    }

    @OnClick({R.id.btn_scan})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                if (isScanning()) {
                    scanSubscription.unsubscribe();
                } else {
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
                }

                updateButtonUIState();
                break;
        }
    }

    private void addScanResult(ScanResult scanResult) {
        for (int i = 0; i < adapter.getCount(); i++) {

            if (adapter.getItem(i).equals(scanResult.getBleDevice().getName())) {
                return;
            }
        }
        if (scanResult.getBleDevice().getName() != null) {
            adapter.add(scanResult.getBleDevice().getMacAddress());
        }


        adapter.notifyDataSetChanged();
    }

    private void clearSubscription() {
        scanSubscription = null;
        updateButtonUIState();
    }

}
