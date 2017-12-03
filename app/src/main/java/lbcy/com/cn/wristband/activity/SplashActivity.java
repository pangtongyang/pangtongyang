package lbcy.com.cn.wristband.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.List;

import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.app.BaseSplashActivity;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxBus;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by chenjie on 2017/9/15.
 */

public class SplashActivity extends BaseSplashActivity implements EasyPermissions.PermissionCallbacks{
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
        thread = new Thread(runnable);
        locationAndStorageAndCameraTask();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
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
            // 是否已登录
            String isLogin = spUtil.getString("is_login", "0");
            // 身高体重是否已设置
            String hasSetBodyData = spUtil.getString("body_data", "0");
            // 将紫色手环置为未连接状态
            spUtil.putString("is_connected", "0");
            // 将首屏数据获取标识标记为首次获取，确保非首次进入首页不阻塞
            spUtil.putString("is_first_in_main", "1");

            if (isLogin.equals("1") && hasSetBodyData.equals("1")){
                Message message = new Message();
                message.what = Consts.CONNECT_DEVICE;
                RxBus.getInstance().post(Consts.ACTIVITY_MANAGE_LISTENER, message);
            }

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
                // 判断是否已设置身高体重
                if (hasSetBodyData.equals("0")){
//                    // 关闭MainActivity
//                    Message message = new Message();
//                    message.what = Consts.CLOSE_ACTIVITY;
//                    RxBus.getInstance().post(Consts.ACTIVITY_MANAGE_LISTENER, message);
//                    // 进入身高体重设置页
//                    Intent intent = new Intent(mActivity, BasicBodyActivity.class);
//                    intent.putExtra("login", true);
//                    startActivity(intent);
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                }
//                Message message = new Message();
//                message.what = Consts.CONNECT_DEVICE;
//                RxBus.getInstance().post(Consts.ACTIVITY_MANAGE_LISTENER, message);
//                Intent intent = new Intent(mActivity, MainActivity.class);
//                startActivity(intent);
            } else {
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
            }
            finish();
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
            EasyPermissions.requestPermissions(this,
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

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).setTitle(R.string.settings).setRationale(R.string.string_help_text).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(BaseApplication.getBaseApplication(), getString(R.string.string_help_text), Toast.LENGTH_SHORT).show();
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
