package lbcy.com.cn.wristband.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import butterknife.ButterKnife;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.rx.RxManager;

/**
 * Created by chenjie on 2017/9/5.
 */

public abstract class BaseFragmentActivity extends FragmentActivity {
    public FragmentActivity mActivity;
    public RxManager mRxManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.NoActionBarTheme);
        mActivity = this;
        mRxManager = new RxManager();
        initData();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView();
        loadData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxManager.clear();
    }

    protected abstract int getLayoutId();

    //初始化view
    protected abstract void initData();
    protected abstract void initView();
    protected abstract void loadData();
}
