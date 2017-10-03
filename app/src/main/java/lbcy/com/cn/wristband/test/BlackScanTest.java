package lbcy.com.cn.wristband.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.huichenghe.bleControl.Ble.BluetoothLeService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import lbcy.com.cn.blacklibrary.ble.DeviceConnect;
import lbcy.com.cn.blacklibrary.manager.DeviceConnectManager;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.ctl.BleScanCallback;
import lbcy.com.cn.wristband.entity.BleDevice;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.utils.BleScanHelper;
import lbcy.com.cn.wristband.utils.ToastUtil;

/**
 * Created by chenjie on 2017/8/9.
 */

public class BlackScanTest extends AppCompatActivity {
    @BindView(R.id.lv_device_list)
    ListView mLvDeviceList;
    ArrayAdapter adapter;
    BleScanHelper scanHelper;
    DeviceConnectManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1);
        mLvDeviceList.setAdapter(adapter);

        scanHelper = new BleScanHelper(this);
        scanHelper.startRxAndroidBleScan(new BleScanCallback() {
            @Override
            public void updateUI(BleDevice device) {
                adapter.add(device.getName() + "");
                adapter.notifyDataSetChanged();
            }
        });

        manager = new DeviceConnectManager(getApplicationContext());
        manager.registerReceiverForAllEvent(new DeviceConnect() {
            @Override
            public void connect() {
                ToastUtil.toast("连接成功！");
                Intent intet = new Intent(BlackScanTest.this, ToolActivity.class);
                startActivity(intet);
            }

            @Override
            public void scan(ArrayList data) {

            }
        });

        manager.startService();

        mLvDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.getItem(i).equals(Consts.BLACK_WRISTBAND_NAME)) {
                    scanHelper.stopRxAndroidBleScan();

                    HashSet<String> hashSet = scanHelper.getRxAndroidBleDevices();
                    Iterator<String> iterator = hashSet.iterator();
                    String macAddress = null;
                    while (iterator.hasNext()) {
                        String data = iterator.next();
                        String name = data.split("###")[0];
                        if (name == null) {
                            continue;
                        }
                        if (name.equals(Consts.BLACK_WRISTBAND_NAME)) {
                            macAddress = data.split("###")[1];
                            break;
                        }
                    }
                    if (macAddress != null) {
                        manager.selectRxAndroidBleDevice(Consts.BLACK_WRISTBAND_NAME, macAddress);
                    } else {
                        ToastUtil.toast("错误！");
                    }
                }else if(adapter.getItem(i).equals(Consts.PURPLE_WRISTBAND_NAME)){
                    scanHelper.stopRxAndroidBleScan();

                    HashSet<String> hashSet = scanHelper.getRxAndroidBleDevices();
                    Iterator<String> iterator = hashSet.iterator();
                    String macAddress = null;
                    while (iterator.hasNext()) {
                        String data = iterator.next();
                        String name = data.split("###")[0];
                        if (name == null) {
                            continue;
                        }
                        if (name.equals(Consts.BLACK_WRISTBAND_NAME)) {
                            macAddress = data.split("###")[1];
                            break;
                        }
                    }
                    if (macAddress != null) {



                    } else {
                        ToastUtil.toast("错误！");
                    }
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BluetoothLeService.getInstance() != null)
            BluetoothLeService.getInstance().disconnect();
        if (!scanHelper.isScanSubscriptionNull())
            scanHelper.stopRxAndroidBleScan();
        manager.stopService();
    }


}
