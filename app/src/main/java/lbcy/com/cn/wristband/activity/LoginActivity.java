package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.entity.LoginBean;
import lbcy.com.cn.wristband.entity.LoginDataDao;
import lbcy.com.cn.wristband.entity.LoginTo;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.manager.NetManager;
import lbcy.com.cn.wristband.rx.RxBus;
import lbcy.com.cn.wristband.utils.DialogUtil;
import lbcy.com.cn.wristband.utils.HandlerTip;
import lbcy.com.cn.wristband.widget.ClearEditText;
import lbcy.com.cn.wristband.utils.InputUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_login_et_btn_login));
        hideBackButton();
        etName.setText(spUtil.getString("userName",""));
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
                    loginAction();

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

    private void saveData(LoginBean loginBean){
        loginBean.getData().setAppType("2");

        if (tvOtherlogin.getText().toString().equals(getString(R.string.activity_login_by_phone))){
            spUtil.putString("login_type", "card");
        } else {
            spUtil.putString("login_type", "phone");
        }
        spUtil.putString("is_login", "1");
        spUtil.putString("userName", etName.getText().toString().trim());
        spUtil.putString("password", etPassword.getText().toString().trim());
        spUtil.putString("which_device", "2"); //1 -> 紫色 2 -> 黑色
//        spUtil.putString("which_device", loginBean.getData().getDevice_type()); //1 -> 紫色 2 -> 黑色
        spUtil.putString("deviceName", "wristband");
//        spUtil.putString("deviceAddress", "FE:54:B9:7C:CB:FA"); //紫色test
        spUtil.putString("deviceAddress", "CD:96:8C:EC:EE:B3"); //黑色test
//        spUtil.putString("deviceAddress", loginBean.getData().getMac_address());

        LoginDataDao loginDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getLoginDataDao();
        if (loginDataDao.loadAll().size() == 0){
            loginDataDao.insert(loginBean.getData());
        } else {
            loginDataDao.deleteAll();
            loginDataDao.insert(loginBean.getData());
        }
    }

    private void clearData(){
        spUtil.clearData();
    }

    private void loginAction(){
        LoginTo loginTo = new LoginTo();
        loginTo.setAccount_no(etName.getText().toString().trim());
        loginTo.setPassword(etPassword.getText().toString().trim());
        NetManager.loginAction(loginTo).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(@NonNull Call<LoginBean> call, @NonNull Response<LoginBean> response) {
                if (response.isSuccessful()){
                    LoginBean loginBean = response.body();
                    if (loginBean != null && loginBean.getCode() == 200){
                        saveData(loginBean);

                        Intent intent = new Intent(mActivity, MainActivity.class);
                        intent.putExtra("isSplashed", true);
                        startActivity(intent);

                        finish();
                    } else {
                        clearData();
                        Toast.makeText(mActivity, loginBean != null ?loginBean.getMessage().toString():"登录失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginBean> call, @NonNull Throwable t) {
                Toast.makeText(mActivity, getString(R.string.network_timeout), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Message message = new Message();
        message.what = Consts.CONNECT_DEVICE;
        RxBus.getInstance().post(Consts.ACTIVITY_MANAGE_LISTENER, message);
    }
}
