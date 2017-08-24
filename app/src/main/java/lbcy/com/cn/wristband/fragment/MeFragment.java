package lbcy.com.cn.wristband.fragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import lbcy.com.cn.settingitemlibrary.SetItemView;
import lbcy.com.cn.wristband.R;

public class MeFragment extends AppCompatActivity {
    // Content View Elements

    private RelativeLayout mRoot_layout;
    private LinearLayout mRl_head;
    private ImageView mIv_header;
    private LinearLayout mRl_me;
    private TextView mTv_name;
    private TextView mTv_id;
    private RelativeLayout mRl_mac;
    private TextView mTv_mac;
    private TextView mTv_macid;
    private TextView mTv_link;
    private SetItemView mRl_statistics;
    private SetItemView mRl_setup;
    private SetItemView mRl_disturb;
    private SetItemView mRl_footprint;
    private SetItemView mRl_body;
    private SetItemView mRl_phone;
    private SetItemView mRl_news;
    private SetItemView mRl_help;
    private Button mBtn_quit;

    // End Of Content View Elements

    private void bindViews() {

        mRoot_layout = (RelativeLayout) findViewById(R.id.root_layout);
        mRl_head = (LinearLayout) findViewById(R.id.rl_head);
        mIv_header = (ImageView) findViewById(R.id.iv_header);
        mRl_me = (LinearLayout) findViewById(R.id.rl_me);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mTv_id = (TextView) findViewById(R.id.tv_id);
        mRl_mac = (RelativeLayout) findViewById(R.id.rl_mac);
        mTv_mac = (TextView) findViewById(R.id.tv_mac);
        mTv_macid = (TextView) findViewById(R.id.tv_macid);
        mTv_link = (TextView) findViewById(R.id.tv_link);
        mRl_statistics = (SetItemView) findViewById(R.id.rl_statistics);
        mRl_setup = (SetItemView) findViewById(R.id.rl_setup);
        mRl_disturb = (SetItemView) findViewById(R.id.rl_disturb);
        mRl_footprint = (SetItemView) findViewById(R.id.rl_footprint);
        mRl_body = (SetItemView) findViewById(R.id.rl_body);
        mRl_phone = (SetItemView) findViewById(R.id.rl_phone);
        mRl_news = (SetItemView) findViewById(R.id.rl_news);
        mRl_help = (SetItemView) findViewById(R.id.rl_help);
        mBtn_quit = (Button) findViewById(R.id.btn_quit);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_fragment);
    }
}
