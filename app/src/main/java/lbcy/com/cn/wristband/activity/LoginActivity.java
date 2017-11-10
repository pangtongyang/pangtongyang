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

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.entity.BasicBodyBean;
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
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

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

    SPUtil spUtilUser;

    int type = 1; // 1 -> 证件号登录， 2 -> 手机号登录

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        spUtilUser = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
        clearData();
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_login_et_btn_login));
        hideBackButton();
        etName.setText(spUtilUser.getString("userName",""));
        etPassword.setText(spUtilUser.getString("password",""));
        if (spUtilUser.getString("login_type","card").equals("card")){
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
            type = 2;
            etName.setHint(R.string.bindPhone);
            etName.setInputType(InputType.TYPE_CLASS_PHONE);
            tvOtherlogin.setText(R.string.activity_login_by_other);
        } else {
            type = 1;
            etName.setInputType(InputType.TYPE_CLASS_TEXT);
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
        if (type == 2){
            String text = etName.getText().toString().trim();
            if (!Pattern.matches(REGEX_MOBILE, text)){
                DialogUtil.showDialog(mActivity, "手机号错误！", false);
                return false;
            }
        }

        return true;
    }

    private void saveData(LoginBean loginBean){
        loginBean.getData().setAppType("2"); //表示android手机

        if (tvOtherlogin.getText().toString().equals(getString(R.string.activity_login_by_phone))){
            spUtilUser.putString("login_type", "card");
        } else {
            spUtilUser.putString("login_type", "phone");
        }
        //设置是否已登录
        spUtilUser.putString("is_login", "1");
        //设置用户名
        spUtilUser.putString("userName", etName.getText().toString().trim());
        //设置密码
        spUtilUser.putString("password", etPassword.getText().toString().trim());
        //设置设备类型
//        spUtilUser.putString("which_device", "2"); //1 -> 紫色 2 -> 黑色
        spUtilUser.putString("which_device", loginBean.getData().getDevice_type()); //1 -> 紫色 2 -> 黑色
        //设置设备名称
        spUtilUser.putString("deviceName", "wristband");
//        spUtilUser.putString("deviceAddress", "FE:54:B9:7C:CB:FA"); //紫色test
//        spUtilUser.putString("deviceAddress", "E3:85:C9:36:63:70"); //紫色test
        //设置设备mac地址
//        spUtilUser.putString("deviceAddress", "CD:96:8C:EC:EE:B3"); //黑色test
        spUtilUser.putString("deviceAddress", loginBean.getData().getMac_address());
        //设置token
        spUtilUser.putString("token", Consts.PRE_TOKEN_STR + loginBean.getData().getToken());
        //添加logo
        spUtilUser.putString("logo", loginBean.getData().getLogo());

        LoginDataDao loginDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getLoginDataDao();
        if (loginDataDao.count() == 0){
            loginDataDao.insert(loginBean.getData());
        } else {
            loginDataDao.deleteAll();
            loginDataDao.insert(loginBean.getData());
        }
    }

    private void clearData(){
        //数据销毁
        spUtilUser.clearData();
        spUtilUser.putString("is_login", "0");
        spUtilUser.putString("js_is_write", "0");

        BaseApplication.getBaseApplication().cleanDB();
    }

    private void loginAction(){
        LoginTo loginTo = new LoginTo();
        loginTo.setAccount_no(etName.getText().toString().trim());
        loginTo.setPassword(etPassword.getText().toString().trim());
        NetManager.loginAction(loginTo, new NetManager.NetCallBack<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                LoginBean loginBean = response.body();
                if (loginBean != null && loginBean.getCode() == 200){
                    saveData(loginBean);
                    getBodyData();

                } else {
                    Toast.makeText(mActivity, loginBean != null ?loginBean.getMessage().toString():"登录失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {

            }
        });
    }

    private void getBodyData(){
        NetManager.getBodyDataAction(spUtilUser.getString("token", ""), new NetManager.NetCallBack<BasicBodyBean>() {
            @Override
            public void onResponse(Call<BasicBodyBean> call, Response<BasicBodyBean> response) {
                BasicBodyBean bodyBean = response.body();
                if (bodyBean != null && bodyBean.getCode() == 200){
                    if (bodyBean.getData().getHeight() != 0 && bodyBean.getData().getWeight() != 0){
                        spUtilUser.putString("body_data", "1");
                        Intent intent = new Intent(mActivity, MainActivity.class);
                        intent.putExtra("isSplashed", true);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(mActivity, BasicBodyActivity.class);
                        intent.putExtra("login", true);
                        startActivity(intent);
                    }

                    finish();
                } else {
                    clearData();
                    Toast.makeText(mActivity, bodyBean != null ?bodyBean.getMessage().toString():"登录失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasicBodyBean> call, Throwable t) {

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
