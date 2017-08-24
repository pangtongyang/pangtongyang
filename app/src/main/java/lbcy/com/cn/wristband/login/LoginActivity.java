package lbcy.com.cn.wristband.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.design.widget.TextInputLayout;
import lbcy.com.cn.wristband.R;

public class LoginActivity extends AppCompatActivity {
    /*
     * Exceptions found during parsing
     *
     * References a layout (@layout/progressbar_custom_rl)
     */

    // Content View Elements

    private TextInputLayout mEt_name_design;
    private EditText mEt_name;
    private RelativeLayout mPassword;
    private TextInputLayout mEt_password_design;
    private EditText mEt_password;
    private RelativeLayout mLoginbyother;
    private TextView mOtherlogin;
    private Button mBtn_login;
    private RelativeLayout mRelLoading;

    // End Of Content View Elements

    private void bindViews() {

        mEt_name_design = (TextInputLayout) findViewById(R.id.et_name_design);
        mEt_name = (EditText) findViewById(R.id.et_name);
        mPassword = (RelativeLayout) findViewById(R.id.password);
        mEt_password_design = (TextInputLayout) findViewById(R.id.et_password_design);
        mEt_password = (EditText) findViewById(R.id.et_password);
        mLoginbyother = (RelativeLayout) findViewById(R.id.loginbyother);
        mOtherlogin = (TextView) findViewById(R.id.otherlogin);
        mBtn_login = (Button) findViewById(R.id.btn_login);
        mRelLoading = (RelativeLayout) findViewById(R.id.loading);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
