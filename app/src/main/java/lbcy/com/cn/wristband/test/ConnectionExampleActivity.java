package lbcy.com.cn.wristband.test;

import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huichenghe.bleControl.Ble.BleGattHelperListener;
import com.huichenghe.bleControl.Ble.BluetoothLeService;
import com.huichenghe.bleControl.Ble.LocalDeviceEntity;
import com.huichenghe.bleControl.BleGattHelper;
import com.huichenghe.bleControl.Utils.FormatUtils;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lbcy.com.cn.blacklibrary.ble.DataCallback;
import lbcy.com.cn.blacklibrary.manager.DeviceManager;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.utils.ToastUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.trello.rxlifecycle.android.ActivityEvent.DESTROY;
import static com.trello.rxlifecycle.android.ActivityEvent.PAUSE;


public class ConnectionExampleActivity extends RxAppCompatActivity {

    @BindView(R.id.connection_state)
    TextView connectionStateView;
    @BindView(R.id.connect_toggle)
    Button connectButton;
    @BindView(R.id.newMtu)
    EditText textMtu;
    @BindView(R.id.set_mtu)
    Button setMtuButton;
    @BindView(R.id.autoconnect)
    SwitchCompat autoConnectToggleSwitch;
    private RxBleDevice bleDevice;
    private Subscription connectionSubscription;

    @BindView(R.id.textView)
    TextView textView;

    @OnClick(R.id.connect_toggle)
    public void onConnectToggleClick() {

//        if (isConnected()) {
//            triggerDisconnect();
//        } else {
//            connectionSubscription = bleDevice.establishConnection(autoConnectToggleSwitch.isChecked())
//                    .compose(bindUntilEvent(PAUSE))
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnUnsubscribe(this::clearSubscription)
//                    .subscribe(this::onConnectionReceived, this::onConnectionFailure);
//        }
        LocalDeviceEntity localDeviceEntity = new LocalDeviceEntity(bleDevice.getName(), bleDevice.getMacAddress(), -50, null);
        BluetoothLeService.getInstance().connect(localDeviceEntity);
    }

    boolean isFind = true;

    @OnClick(R.id.button)
    public void findDevice() {
        if (isFind) {

            DeviceManager.getInstance().findDevice(isFind, new DataCallback() {
                @Override
                public void OnSuccess(byte[] data) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(FormatUtils.bytesToHexString(data));
                        }
                    });
                }

                @Override
                public void OnFailed() {

                }

                @Override
                public void OnFinished() {

                }
            });
        } else {
            DeviceManager.getInstance().findDevice(isFind, null);
        }
        isFind = !isFind;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.set_mtu)
    public void onSetMtu() {
        BluetoothLeService.getInstance().disconnect();
//        bleDevice.establishConnection(false)
//                .flatMap(rxBleConnection -> rxBleConnection.requestMtu(72))
//                .first() // Disconnect automatically after discovery
//                .compose(bindUntilEvent(PAUSE))
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnUnsubscribe(this::updateUI)
//                .subscribe(this::onMtuReceived, this::onConnectionFailure);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example2);
        ButterKnife.bind(this);
        String macAddress = getIntent().getStringExtra("extra_mac_address");
        setTitle(getString(R.string.mac_address, macAddress));
        bleDevice = BaseApplication.getRxBleClient(this).getBleDevice(macAddress);

        // How to listen for connection state changes
        bleDevice.observeConnectionStateChanges()
                .compose(bindUntilEvent(DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onConnectionStateChange);
    }

    private boolean isConnected() {
        return bleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED;
    }

    private void onConnectionFailure(Throwable throwable) {
        //noinspection ConstantConditions
//        Snackbar.make(findViewById(android.R.id.content), "Connection error: " + throwable, Snackbar.LENGTH_SHORT).show();
        ToastUtil.toast("Connection error: " + throwable);
    }

    private void onConnectionReceived(RxBleConnection connection) {
        //noinspection ConstantConditions
//        Snackbar.make(findViewById(android.R.id.content), "Connection received", Snackbar.LENGTH_SHORT).show();
//        ToastUtil.toast("Connection received");
//        Intent intent = new Intent(this, ToolActivity.class);
//        startActivity(intent);

        BluetoothLeService.getInstance().addCallback(
                BleGattHelper.getInstance(getApplicationContext(), new gattHelperListener()));
    }

    class gattHelperListener implements BleGattHelperListener {
        @Override
        public void onDeviceStateChangeUI(LocalDeviceEntity device,
                                          BluetoothGatt gatt,
                                          final String uuid, final byte[] value) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String showResult = "当前心率：";
                    int liveHR = value[1] & 0xff;
                    if (liveHR != 0) {
                        showResult += String.valueOf(liveHR);
                    } else {
                        showResult += "--";
                    }
//                    textheartrate.setText(showResult);
                }
            });

        }

        @Override
        public void onDeviceConnectedChangeUI(final LocalDeviceEntity device,
                                              boolean showToast,
                                              final boolean fromServer) {

        }
    }

    private void onConnectionStateChange(RxBleConnection.RxBleConnectionState newState) {
        connectionStateView.setText(newState.toString());
        updateUI();
    }

    private void onMtuReceived(Integer mtu) {
        //noinspection ConstantConditions
//        Snackbar.make(findViewById(android.R.id.content), "MTU received: " + mtu, Snackbar.LENGTH_SHORT).show();
        ToastUtil.toast("MTU received: " + mtu);
    }

    private void clearSubscription() {
        connectionSubscription = null;
        updateUI();
    }

    private void triggerDisconnect() {

        if (connectionSubscription != null) {
            connectionSubscription.unsubscribe();
        }
    }

    private void updateUI() {
        final boolean connected = isConnected();
        connectButton.setText(connected ? R.string.disconnect : R.string.connect);
        autoConnectToggleSwitch.setEnabled(!connected);
    }
}
