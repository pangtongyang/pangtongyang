package lbcy.com.cn.wristband.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;

import android.app.Activity;
public class SetSedentaryActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_sedentary;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_Sedentary));
    }

    @Override
    protected void loadData() {

    }
}
