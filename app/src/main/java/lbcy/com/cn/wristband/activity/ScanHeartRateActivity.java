package lbcy.com.cn.wristband.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;

import android.app.Activity;
public class ScanHeartRateActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_heart_rate;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_device_heartrate));
    }

    @Override
    protected void loadData() {

    }
}
