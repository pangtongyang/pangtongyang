package lbcy.com.cn.wristband.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseFragment;

/**
 * Created by chenjie on 2017/9/5.
 */

public class WebFragment extends BaseFragment {
    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.tv_txt)
    TextView tvTxt;
    String txt;
    int color;

    public WebFragment(String txt, int color) {
        this.txt = txt;
        this.color = color;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_web;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tvTxt.setText(txt);
        tvTxt.setBackground(ContextCompat.getDrawable(mActivity, color));
    }

    @Override
    protected void loadData() {

    }

}
