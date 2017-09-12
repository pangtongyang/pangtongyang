package lbcy.com.cn.wristband.activity;

import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;

/**
 * Created by chenjie on 2017/9/11.
 */

public class BasicBodyActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_basicbody;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.simplebody));
    }

    @Override
    protected void loadData() {

    }
}
