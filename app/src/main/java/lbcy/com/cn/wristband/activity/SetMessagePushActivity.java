package lbcy.com.cn.wristband.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.huichenghe.bleControl.Ble.BluetoothLeService;

import java.util.List;

import butterknife.BindView;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.settingitemlibrary.SetItemView;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.utils.DialogUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by chenjie on 2017/9/8.
 */

public class SetMessagePushActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final int NOTIFICATION_PERMISSION_REQUEST = 1001;
    private static final int RC_READ_SMS_PERM = 1002;
    private static final String[] READ_SMS = {Manifest.permission.READ_SMS};
    public static final String TAG = SetMessagePushActivity.class.getSimpleName();
    @BindView(R.id.rl_sms_push)
    SetItemView rlSmsPush;
    @BindView(R.id.rl_qq_push)
    SetItemView rlQqPush;
    @BindView(R.id.rl_wechat_push)
    SetItemView rlWechatPush;
    @BindView(R.id.rl_facebook_push)
    SetItemView rlFacebookPush;
    @BindView(R.id.rl_telephone_push)
    SetItemView rlTelephonePush;

    SPUtil spUtil;
    private String which_device;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_message_push;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
        which_device = spUtil.getString("which_device", "2");
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_device_push));

        if (!isEnabled()) {
            DialogUtil.showDialog(mActivity, getString(R.string.notification_request), new DialogUtil.DialogListener() {
                @Override
                public void submit() {
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivityForResult(intent, NOTIFICATION_PERMISSION_REQUEST);
                }

                @Override
                public void cancel() {
                    rlFacebookPush.setEnable(false);
                    rlQqPush.setEnable(false);
                    rlSmsPush.setEnable(false);
                    rlTelephonePush.setEnable(false);
                    rlWechatPush.setEnable(false);
                }
            });

        }

        getDataFromSP();
    }

    @Override
    protected void loadData() {
        rlFacebookPush.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
                if (which_device.equals("2")) {
                    if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        rlFacebookPush.setChecked(!state);
                        return;
                    }
                } else {
                    String is_connected = spUtil.getString("is_connected", "0");
                    if (is_connected.equals("0")) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        rlFacebookPush.setChecked(!state);
                        return;
                    }
                }
                spUtil.putString("facebook_switch", state?"1":"0");
            }
        });
        rlQqPush.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
                if (which_device.equals("2")) {
                    if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        rlQqPush.setChecked(!state);
                        return;
                    }
                } else {
                    String is_connected = spUtil.getString("is_connected", "0");
                    if (is_connected.equals("0")) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        rlQqPush.setChecked(!state);
                        return;
                    }
                }
                spUtil.putString("qq_switch", state?"1":"0");
            }
        });
        rlSmsPush.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
//                read_sms(state);
                if (which_device.equals("2")) {
                    if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        rlSmsPush.setChecked(!state);
                        return;
                    }
                } else {
                    String is_connected = spUtil.getString("is_connected", "0");
                    if (is_connected.equals("0")) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        rlSmsPush.setChecked(!state);
                        return;
                    }
                }
                spUtil.putString("sms_switch", state?"1":"0");
            }
        });
        rlTelephonePush.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
                if (which_device.equals("2")) {
                    if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        rlTelephonePush.setChecked(!state);
                        return;
                    }
                } else {
                    String is_connected = spUtil.getString("is_connected", "0");
                    if (is_connected.equals("0")) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        rlTelephonePush.setChecked(!state);
                        return;
                    }
                }
                spUtil.putString("telephone_switch", state?"1":"0");
            }
        });
        rlWechatPush.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
                if (which_device.equals("2")) {
                    if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        rlWechatPush.setChecked(!state);
                        return;
                    }
                } else {
                    String is_connected = spUtil.getString("is_connected", "0");
                    if (is_connected.equals("0")) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        rlWechatPush.setChecked(!state);
                        return;
                    }
                }
                spUtil.putString("wechat_switch", state?"1":"0");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveData();
    }

    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasSMSPermissions(){
        return EasyPermissions.hasPermissions(mActivity, READ_SMS);
    }

    @AfterPermissionGranted(RC_READ_SMS_PERM)
    public void read_sms(boolean state){
        if (state){
            if (hasSMSPermissions()){
                // Have permissions, do the thing!
                spUtil.putString("sms_switch", "1");
            } else {
                EasyPermissions.requestPermissions(mActivity,
                        "当前应用缺少短信权限，请打开所需权限",
                        RC_READ_SMS_PERM,
                        READ_SMS);
            }
        } else {
            spUtil.putString("sms_switch", "0");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NOTIFICATION_PERMISSION_REQUEST:
                if (!isEnabled()) {
                    Toast.makeText(mActivity, "未获取权限，通知功能将无法使用！", Toast.LENGTH_SHORT).show();
                    rlFacebookPush.setEnable(false);
                    rlQqPush.setEnable(false);
                    rlSmsPush.setEnable(false);
                    rlTelephonePush.setEnable(false);
                    rlWechatPush.setEnable(false);
                }
                break;
            case AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE:
                PackageManager pm = getPackageManager();
                Toast.makeText(BaseApplication.getBaseApplication(), getString(R.string.string_help_text), Toast.LENGTH_SHORT).show();
                if (!(PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission("android.permission.READ_SMS", mActivity.getPackageName()))){
                    rlSmsPush.setChecked(false);
                }
                break;
        }
    }

    private void getDataFromSP(){
        rlFacebookPush.setChecked(spUtil.getString("facebook_switch", "0").equals("1"));
        rlQqPush.setChecked(spUtil.getString("qq_switch", "0").equals("1"));
        rlSmsPush.setChecked(spUtil.getString("sms_switch", "0").equals("1"));
        rlTelephonePush.setChecked(spUtil.getString("telephone_switch", "0").equals("1"));
        rlWechatPush.setChecked(spUtil.getString("wechat_switch", "0").equals("1"));
    }

    private void saveData(){
        spUtil.putString("facebook_switch", rlFacebookPush.isChecked()?"1":"0");
        spUtil.putString("qq_switch", rlQqPush.isChecked()?"1":"0");
        spUtil.putString("sms_switch", rlSmsPush.isChecked()?"1":"0");
        spUtil.putString("telephone_switch", rlTelephonePush.isChecked()?"1":"0");
        spUtil.putString("wechat_switch", rlWechatPush.isChecked()?"1":"0");
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (requestCode == RC_READ_SMS_PERM){
            rlSmsPush.setChecked(false);

            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this).setTitle(R.string.settings).setRationale("当前应用缺少短信权限，请打开所需权限").build().show();
            }
        }
    }
}
