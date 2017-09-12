package lbcy.com.cn.wristband.test;

/**
 * Created by chenjie on 2017/8/7.
 */

import android.Manifest;
import android.support.v7.app.AppCompatActivity;

import lbcy.com.cn.wristband.global.Consts;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission_group.LOCATION;

/**
 * Created by qindachang on 2017/3/30.
 */

public abstract class BaseActivity extends AppCompatActivity {

    String[] locations = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    protected boolean checkLocationPermission() {
        return EasyPermissions.hasPermissions(this, locations);
    }

    protected void requestLocationPermission() {
        EasyPermissions.requestPermissions(this, "Android 6.0以上扫描蓝牙需要该权限", Consts.REQUEST_CODE_ASK_LOCATION_PERMISSIONS, locations);
    }

}
