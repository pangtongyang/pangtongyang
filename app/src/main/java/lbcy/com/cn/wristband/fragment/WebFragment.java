package lbcy.com.cn.wristband.fragment;

import android.os.Bundle;

import com.just.library.AgentWebSettings;

import lbcy.com.cn.wristband.app.BaseWebFragment;
import lbcy.com.cn.wristband.widget.webview.CustomSettings;

/**
 * Created by chenjie on 2017/9/5.
 */

public class WebFragment extends BaseWebFragment {

    public static BaseWebFragment getInstance(Bundle bundle) {

        WebFragment mWebFragment = new WebFragment();
        if (bundle != null)
            mWebFragment.setArguments(bundle);

        return mWebFragment;

    }


    @Override
    public AgentWebSettings getSettings() {

        return new CustomSettings();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadData() {

    }

}
