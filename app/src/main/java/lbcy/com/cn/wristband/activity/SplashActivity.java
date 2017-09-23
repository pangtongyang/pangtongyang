package lbcy.com.cn.wristband.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.List;

import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxBus;
import lbcy.com.cn.wristband.utils.HandlerTip;
import lbcy.com.cn.wristband.utils.ToastUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by chenjie on 2017/9/15.
 */

public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{
    private static final int RC_LOCATION_STORAGE_CAMERA_PERM = 124;
    private static final String[] LOCATION_STORAGE_CAMERA =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public static final String TAG = SplashActivity.class.getSimpleName();
    SPUtil spUtil;
    Thread thread;
    private static final int sleepTime = 3000; //启动页启动间隔
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
    }

    @Override
    protected void initView() {
        hideTopBar();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        thread = new Thread(runnable);
        locationAndStorageAndCameraTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        thread.interrupt();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            //You can do something here!
            String isLogin = spUtil.getString("is_login", "0");
            long costTime = System.currentTimeMillis() - start;
            try {
                Thread.sleep(sleepTime - costTime);
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
                Message message = new Message();
                message.what = Consts.CLOSE_ACTIVITY;
                RxBus.getInstance().post(Consts.ACTIVITY_MANAGE_LISTENER, message);
                finish();
                return;
            }
            if (isLogin.equals("1")){
                Message message = new Message();
                message.what = Consts.CONNECT_DEVICE;
                RxBus.getInstance().post(Consts.ACTIVITY_MANAGE_LISTENER, message);
//                Intent intent = new Intent(mActivity, MainActivity.class);
//                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    private boolean hasLocationAndStorageAndCameraPermissions(){
        return EasyPermissions.hasPermissions(mActivity, LOCATION_STORAGE_CAMERA);
    }

    @AfterPermissionGranted(RC_LOCATION_STORAGE_CAMERA_PERM)
    public void locationAndStorageAndCameraTask(){
        if (hasLocationAndStorageAndCameraPermissions()){
            // Have permissions, do the thing!
            thread.start();
        } else {
            EasyPermissions.requestPermissions(mActivity,
                    getString(R.string.string_help_text),
                    RC_LOCATION_STORAGE_CAMERA_PERM,
                    LOCATION_STORAGE_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    boolean showFlag = true;
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms) && showFlag) {
            new AppSettingsDialog.Builder(this).setTitle(R.string.settings).setRationale(R.string.string_help_text).build().show();
            showFlag = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

            // Do something after user returned from app settings screen, like showing a Toast.
            if (hasLocationAndStorageAndCameraPermissions()){
                thread.start();
            } else {
                ToastUtil.toast(getString(R.string.string_help_text));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Message message = new Message();
            message.what = Consts.CLOSE_ACTIVITY;
            RxBus.getInstance().post(Consts.ACTIVITY_MANAGE_LISTENER, message);
            finish();
            return true;
        }
        return false;
    }
}
