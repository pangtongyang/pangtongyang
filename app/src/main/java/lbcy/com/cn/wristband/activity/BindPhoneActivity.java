package lbcy.com.cn.wristband.activity;

import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.entity.MobileBean;
import lbcy.com.cn.wristband.entity.MobileTo;
import lbcy.com.cn.wristband.manager.NetManager;
import lbcy.com.cn.wristband.utils.HandlerTip;
import retrofit2.Call;
import retrofit2.Response;

public class BindPhoneActivity extends BaseActivity {
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";

    SPUtil spUtil;
    String token;
    MobileBean mobileBean;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_get_code)
    Button btnGetCode;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
        token = spUtil.getString("token", "");
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_bind_phone));

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                if (temp.length() > 20)
                {
                    editable.delete(20, 21);
                }
            }
        });

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                if (temp.length() > 10)
                {
                    editable.delete(10, 11);
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_get_code, R.id.btn_submit})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_get_code:
                if (codeValidate()){
                    time = 60;
                    getMobileCode(etPhone.getText().toString().trim());
                    btnGetCode.setEnabled(false);
                    btnGetCode.setTextColor(ContextCompat.getColor(mActivity, R.color.black_text));
                    btnGetCode.setText("获取验证码");
                    HandlerTip.getInstance().getHandler().post(runnable);
                }
                break;
            case R.id.btn_submit:
                if (validate()){
                    bindPhone(etCode.getText().toString().trim());

                }
                break;
        }
    }

    int time = 60;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time == 0){
                btnGetCode.setEnabled(true);
                btnGetCode.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                btnGetCode.setText("获取验证码");
                return;
            }
            btnGetCode.setText(String.valueOf(time));
            time--;

            HandlerTip.getInstance().getHandler().postDelayed(runnable, 1000);
        }
    };

    private boolean codeValidate(){
        if (etPhone.getText().toString().trim().equals("")){
            Toast.makeText(mActivity, "手机号为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPhone.getText().toString().trim().length() > 20){
            Toast.makeText(mActivity, "手机号长度超限！", Toast.LENGTH_SHORT).show();
            return false;
        }
        String text = etPhone.getText().toString().trim();
        if (!Pattern.matches(REGEX_MOBILE, text)){
            Toast.makeText(mActivity, "手机号错误！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validate(){
        if (etPhone.getText().toString().trim().equals("")){
            Toast.makeText(mActivity, "手机号为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPhone.getText().toString().trim().length() > 20){
            Toast.makeText(mActivity, "手机号长度超限！", Toast.LENGTH_SHORT).show();
            return false;
        }
        String text = etPhone.getText().toString().trim();
        if (!Pattern.matches(REGEX_MOBILE, text)){
            Toast.makeText(mActivity, "手机号错误！", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etCode.getText().toString().trim().equals("")){
            Toast.makeText(mActivity, "验证码为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etCode.getText().toString().trim().length() > 10){
            Toast.makeText(mActivity, "验证码长度超限！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void getMobileCode(String phone) {
        NetManager.getMobileCodeAction(token, phone, new NetManager.NetCallBack<MobileBean>() {
            @Override
            public void onResponse(Call<MobileBean> call, Response<MobileBean> response) {
                MobileBean data = response.body();
                if ((data != null ? data.getCode() : 0) == 200)
                    mobileBean = data;
                else if ((data != null ? data.getCode() : 0) == 300){
                    Toast.makeText(mActivity, "已绑定手机号" + data.getData() + "，无需重新绑定！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "验证码获取失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MobileBean> call, Throwable t) {

            }
        });
    }

    private void bindPhone(String code) {
        MobileTo mobileTo = new MobileTo();
        mobileTo.setMobile(etPhone.getText().toString().trim());
        mobileTo.setValidate(code);

        NetManager.bindPhoneAction(token, mobileTo, new NetManager.NetCallBack<MobileBean>() {
            @Override
            public void onResponse(Call<MobileBean> call, Response<MobileBean> response) {
                MobileBean data = response.body();
                if (data != null && data.getCode() == 200) {
                    Toast.makeText(mActivity, "绑定成功！", Toast.LENGTH_SHORT).show();
                } else if ((data != null ? data.getCode() : 0) == 500){
                    Toast.makeText(mActivity, data.getMessage(), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(mActivity, "绑定失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MobileBean> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HandlerTip.getInstance().getHandler().removeCallbacks(runnable);
    }
}
