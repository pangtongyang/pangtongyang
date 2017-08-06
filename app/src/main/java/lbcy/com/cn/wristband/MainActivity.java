package lbcy.com.cn.wristband;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.huichenghe.bleControl.Ble.BleScanUtils;
import com.huichenghe.bleControl.Ble.LocalDeviceEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lbcy.com.cn.blacklibrary.ble.DataCallback;
import lbcy.com.cn.blacklibrary.ble.DeviceConnect;
import lbcy.com.cn.blacklibrary.manager.DeviceConnectManager;
import lbcy.com.cn.blacklibrary.manager.DeviceManager;
import lbcy.com.cn.wristband.utils.ToastUtils;

public class MainActivity extends AppCompatActivity {

    DeviceConnectManager manager;
    ListView mDeviceList;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDeviceList = (ListView) findViewById(R.id.lv_device_list);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1);

        mDeviceList.setAdapter(adapter);

        manager = new DeviceConnectManager(getApplicationContext());
        manager.registerReceiverForAllEvent(new DeviceConnect() {
            @Override
            public void connect() {
                ToastUtils.toast("连接成功！");
            }

            @Override
            public void scan(ArrayList data) {

            }
        });
        manager.startService();

        manager.startScan(new DeviceConnect() {
            @Override
            public void connect() {

            }

            @Override
            public void scan(ArrayList data) {
                adapter.clear();
                for (Object entity : data) {
                    LocalDeviceEntity mEntity = (LocalDeviceEntity) entity;
                    adapter.add(mEntity.getName());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        });

        mDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                manager.selectDevice(i);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.unregisterReceiver();
        BleScanUtils.getBleScanUtilsInstance(getApplicationContext()).stopScan();
    }
}
