package lbcy.com.cn.wristband.activity;

import lbcy.com.cn.wristband.app.BaseWebActivity;

/**
 * Created by chenjie on 2017/9/17.
 */

public class WebActivity extends BaseWebActivity {
    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setUrl(getIntent().getStringExtra("url"));
    }

    @Override
    protected void loadData() {

    }
}
