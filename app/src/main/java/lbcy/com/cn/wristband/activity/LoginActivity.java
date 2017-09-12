package lbcy.com.cn.wristband.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.widget.ClearEditText;
import lbcy.com.cn.wristband.utils.InputUtil;

/**
 * Created by chenjie on 2017/9/3.
 */

public class LoginActivity extends BaseActivity {


    @BindView(R.id.iv_name)
    ImageView ivName;
    @BindView(R.id.et_name)
    ClearEditText etName;
    @BindView(R.id.iv_password)
    ImageView ivPassword;
    @BindView(R.id.et_password)
    ClearEditText etPassword;
    @BindView(R.id.tv_otherlogin)
    TextView tvOtherlogin;
    @BindView(R.id.rl_loginbyother)
    RelativeLayout rlLoginbyother;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_login_et_btn_login));
        hideBackButton();
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void changePage() {
        InputUtil.hideSoftInput(etName, this);
        InputUtil.hideSoftInput(etPassword, this);
        etName.setText("");
        etPassword.setText("");

        if (tvOtherlogin.getText().toString().equals(getResources().getString(R.string.activity_login_by_phone))) {
            etName.setHint(R.string.bindPhone);
            tvOtherlogin.setText(R.string.activity_login_by_other);
        } else {
            etName.setHint(R.string.activity_login_et_name_hint);
            tvOtherlogin.setText(R.string.activity_login_by_phone);
        }

    }

    @OnClick({R.id.tv_otherlogin})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_otherlogin:
                changePage();
                break;

        }
    }
}
