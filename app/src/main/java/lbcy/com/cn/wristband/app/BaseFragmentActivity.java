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
    public boolean isSplashed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashTheme);
        mActivity = this;
        mRxManager = new RxManager();
        if (savedInstanceState!=null)
            isSplashed = savedInstanceState.getBoolean("isSplashed", false);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isSplashed", true);
        super.onSaveInstanceState(outState);
    }

    protected abstract int getLayoutId();

    //初始化view
    protected abstract void initData();
    protected abstract void initView();
    protected abstract void loadData();
}
