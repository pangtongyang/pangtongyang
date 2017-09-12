package lbcy.com.cn.wristband.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;

public class BindPhoneActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_bind_phone));
    }

    @Override
    protected void loadData() {

    }
}
