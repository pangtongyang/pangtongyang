package lbcy.com.cn.wristband.activity;

import android.app.Activity;

import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;

/**
 * Created by chenjie on 2017/9/8.
 */

public class SetMessagePushActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_message_push;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_device_push));
    }

    @Override
    protected void loadData() {

    }
}
