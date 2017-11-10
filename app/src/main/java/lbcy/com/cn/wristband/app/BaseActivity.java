package lbcy.com.cn.wristband.app;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxManager;
import rx.functions.Action1;

/**
 * Created by chenjie on 2017/9/5.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public AppCompatActivity mActivity;
    OnRightClickListener onRightClickListener;
    public RxManager mRxManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.NoActionBarTheme);
        setContentView(R.layout.activity_base);
        mActivity = this;
        mRxManager = new RxManager();
        initData();
        initContentView();
        initView();
        loadData();

        ImageView back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageView rightIcon = (ImageView) findViewById(R.id.iv_righticon);
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRightClickListener.click();
            }
        });

        //监听关闭所有activity事件
        mRxManager.on(Consts.CLOSE_ALL_ACTIVITY_LISTENER, new Action1<Message>() {
            @Override
            public void call(Message message) {
                switch (message.what){
                    case Consts.CLOSE_ALL_ACTIVITY:
                        finish();
                        break;
                }
            }
        });
    }

    private void initContentView() {
        FrameLayout mFrameLayout = (FrameLayout) findViewById(R.id.base_contentview);
        getLayoutInflater().inflate(getLayoutId(), mFrameLayout);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxManager.clear();
    }

    protected abstract int getLayoutId();

    //初始化view
    protected abstract void initData();
    protected abstract void initView();
    protected abstract void loadData();

    protected void setTitle(String text){
        TextView textView = (TextView) findViewById(R.id.tv_top_bar_title);
        textView.setText(text);
    }

    protected void hideBackButton(){
        ImageView back = (ImageView) findViewById(R.id.iv_back);
        back.setVisibility(View.GONE);
    }

    protected void hideTopBar(){
        View view = findViewById(R.id.rl_top_bar);
        view.setVisibility(View.GONE);
    }

    protected void setRightIcon(int id){
        ImageView rightIcon = (ImageView) findViewById(R.id.iv_righticon);
        rightIcon.setVisibility(View.VISIBLE);
        rightIcon.setImageResource(id);
    }

    protected interface OnRightClickListener{
        void click();
    }

    protected void rightClick(OnRightClickListener rightClickListener){
        this.onRightClickListener = rightClickListener;
    }
}
