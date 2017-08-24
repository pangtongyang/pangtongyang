package lbcy.com.cn.wristband.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.support.design.widget.TextInputLayout;
import lbcy.com.cn.wristband.R;

public class BasicbodyActivity extends AppCompatActivity {
    // Content View Elements

    private TextInputLayout mEt_height_design;
    private EditText mEt_height;
    private RelativeLayout mWeight;
    private TextInputLayout mEt_weight_design;
    private EditText mEt_weight;
    private Button mBtn_submit;

    // End Of Content View Elements

    private void bindViews() {

        mEt_height_design = (TextInputLayout) findViewById(R.id.et_height_design);
        mEt_height = (EditText) findViewById(R.id.et_height);
        mWeight = (RelativeLayout) findViewById(R.id.weight);
        mEt_weight_design = (TextInputLayout) findViewById(R.id.et_weight_design);
        mEt_weight = (EditText) findViewById(R.id.et_weight);
        mBtn_submit = (Button) findViewById(R.id.btn_submit);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicbody);
    }
}
