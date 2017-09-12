package lbcy.com.cn.wristband.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import lbcy.com.cn.wristband.rx.RxManager;

/**
 * Created by chenjie on 2017/9/5.
 */

public abstract class BaseFragment extends Fragment {
    public Activity mActivity;
    protected View mRootView;
    Unbinder unbinder;
    public RxManager mRxManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView =inflater.inflate(getLayoutId(),container,false);
        mActivity = getActivity();
        mRxManager = new RxManager();
        initData();
        unbinder = ButterKnife.bind(this,mRootView);//绑定framgent
        initView();
        loadData();
        return mRootView;

    }

    protected abstract int getLayoutId();

    //初始化view
    protected abstract void initData();
    protected abstract void initView();
    protected abstract void loadData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mRxManager.clear();
    }
}
