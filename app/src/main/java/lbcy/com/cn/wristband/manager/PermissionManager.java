package lbcy.com.cn.wristband.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.utils.DialogUtil;

/**
 * Created by chenjie on 2017/8/7.
 * 封装权限获取操作
 * 注意方法体也要写到onRequestPermissionsResult方法中
 */

public class PermissionManager {
    Context mContext;

    public PermissionManager(Context context) {
        this.mContext = context;
    }

    public void initPermission(String permission, PermissionCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (initPermission(permission)) {
                callback.run();
            }

        } else {
            callback.run();
        }
    }

        private boolean initPermission(String permission) {

        int hasPhoneCallPermission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasPhoneCallPermission = mContext.checkSelfPermission(permission);
        }

        if (hasPhoneCallPermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int REQUEST_ASK_PERMISSION = 0;
                //位置权限，用于蓝牙广播权限获取，不需要
                if (permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    REQUEST_ASK_PERMISSION = Consts.REQUEST_CODE_ASK_LOCATION_PERMISSIONS;
                }

                ((AppCompatActivity)mContext).requestPermissions(new String[]{permission}, REQUEST_ASK_PERMISSION);
            }
            return false;
        } else {
            return true;
        }
    }

    public interface PermissionCallback {
        void run();
    }
}
