package lbcy.com.cn.wristband.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import lbcy.com.cn.settingitemlibrary.SetItemView;
import lbcy.com.cn.wristband.R;
import android.app.Activity;

public class DeviceSettingActivity extends Activity {
    // Content View Elements

    private RelativeLayout mSetting_layout;
    private LinearLayout mRl_head;
    private ImageView mIv_header;
    private LinearLayout mRl_me;
    private TextView mTv_name;
    private TextView mTv_id;
    private RelativeLayout mRl_mac;
    private TextView mTv_mac;
    private TextView mTv_macid;
    private TextView mTv_link;
    private SetItemView mRl_push;
    private SetItemView mRl_late;
    private SetItemView mRl_clock;
    private SetItemView mRl_handsup;
    private SetItemView mRl_antilost;
    private SetItemView mRl_shock;
    private SetItemView mRl_upgrade;
    private SetItemView mRl_heartrate;
    private SetItemView mRl_sedentary;

    // End Of Content View Elements

    private void bindViews() {

        mSetting_layout = (RelativeLayout) findViewById(R.id.setting_layout);
        mRl_head = (LinearLayout) findViewById(R.id.rl_head);
        mIv_header = (ImageView) findViewById(R.id.iv_header);
        mRl_me = (LinearLayout) findViewById(R.id.rl_me);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mTv_id = (TextView) findViewById(R.id.tv_id);
        mRl_mac = (RelativeLayout) findViewById(R.id.rl_mac);
        mTv_mac = (TextView) findViewById(R.id.tv_mac);
        mTv_macid = (TextView) findViewById(R.id.tv_macid);
        mTv_link = (TextView) findViewById(R.id.tv_link);
        mRl_push = (SetItemView) findViewById(R.id.rl_push);
        mRl_late = (SetItemView) findViewById(R.id.rl_late);
        mRl_clock = (SetItemView) findViewById(R.id.rl_clock);
        mRl_handsup = (SetItemView) findViewById(R.id.rl_handsup);
        mRl_antilost = (SetItemView) findViewById(R.id.rl_antilost);
        mRl_shock = (SetItemView) findViewById(R.id.rl_shock);
        mRl_upgrade = (SetItemView) findViewById(R.id.rl_upgrade);
        mRl_heartrate = (SetItemView) findViewById(R.id.rl_heartrate);
        mRl_sedentary = (SetItemView) findViewById(R.id.rl_sedentary);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);
    }
}
