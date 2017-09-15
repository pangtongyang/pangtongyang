package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.utils.DialogUtil;
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

    SPUtil spUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, Consts.USER_DB_NAME);
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_login_et_btn_login));
        hideBackButton();
        etName.setText(spUtil.getString("username",""));
        etPassword.setText(spUtil.getString("password",""));
        if (spUtil.getString("login_type","card").equals("card")){
            tvOtherlogin.setText(R.string.activity_login_by_phone);
            etName.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            tvOtherlogin.setText(R.string.activity_login_by_other);
            etName.setInputType(InputType.TYPE_CLASS_PHONE);
        }
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

    @OnClick({R.id.tv_otherlogin, R.id.btn_login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_otherlogin:
                changePage();
                break;
            case R.id.btn_login:
                if (validate()) {
                    saveData();
                    Intent intent = new Intent(mActivity, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    private boolean validate(){
        if (etName.getText().toString().trim().equals("")){
            DialogUtil.showDialog(mActivity, etName.getHint().toString()+"为空！", false);
            return false;
        }
        if (etPassword.getText().toString().trim().equals("")){
            DialogUtil.showDialog(mActivity, "密码为空！", false);
            return false;
        }

        return true;
    }

    private void saveData(){
        if (tvOtherlogin.getText().toString().equals(getString(R.string.activity_login_by_phone))){
            spUtil.putString("login_type", "card");
        } else {
            spUtil.putString("login_type", "phone");
        }
        spUtil.putString("is_login", "1");
        spUtil.putString("username", etName.getText().toString().trim());
        spUtil.putString("password", etPassword.getText().toString().trim());
    }
}
