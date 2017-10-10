package lbcy.com.cn.wristband.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.entity.LoginData;
import lbcy.com.cn.wristband.entity.LoginDataDao;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxManager;
import lbcy.com.cn.wristband.widget.webview.WebLayout;
import rx.functions.Action1;

/**
 * Created by chenjie on 2017/9/5.
 */
public abstract class BaseWebActivity extends AppCompatActivity {

    public AppCompatActivity mActivity;
    OnRightClickListener onRightClickListener;
    public RxManager mRxManager;

    @BindView(R.id.tv_top_bar_title)
    TextView tvTopBarTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_righticon)
    ImageView ivRighticon;
    @BindView(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @BindView(R.id.base_contentview)
    FrameLayout baseContentview;
    @BindView(R.id.ll_web_content)
    LinearLayout llWebContent;

    protected AgentWeb mAgentWeb;
    AgentWeb.PreAgentWeb preAgentWeb;
    String targetUrl = "";

    List<String> webHistoryUrls = new ArrayList<>();
    public boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashTheme);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        baseContentview.setVisibility(View.GONE);
        mActivity = this;
        mRxManager = new RxManager();
        initData();
        initView();
        loadData();

        preAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(llWebContent,new LinearLayout.LayoutParams(-1,-1) )//
                .useDefaultIndicator()//
                .defaultProgressBarColor()
                .setReceivedTitleCallback(mCallback)
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setSecutityType(AgentWeb.SecurityType.strict)
                .setWebLayout(new WebLayout(this))
                .createAgentWeb()//
                .ready();

        mAgentWeb = preAgentWeb.go(getUrl());

        if(mAgentWeb!=null){
            mAgentWeb.getJsInterfaceHolder().addJavaObject("android",new AndroidInterface(mAgentWeb, mActivity));

        }

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

    @OnClick({R.id.iv_back, R.id.iv_righticon})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_back:
                if (!mAgentWeb.back())
                    finish();
                break;
            case R.id.iv_righticon:
                onRightClickListener.click();
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    //初始化view
    protected abstract void initData();

    protected abstract void initView();

    protected abstract void loadData();

    protected void setTitle(String text) {
        TextView textView = (TextView) findViewById(R.id.tv_top_bar_title);
        textView.setText(text);
    }

    protected void hideBackButton() {
        ivBack.setVisibility(View.GONE);
    }

    protected void hideTopBar() {
        View view = findViewById(R.id.rl_top_bar);
        view.setVisibility(View.GONE);
    }

    protected void setRightIcon(int id) {
        ivRighticon.setVisibility(View.VISIBLE);
        ivRighticon.setImageResource(id);
    }

    protected interface OnRightClickListener {
        void click();
    }

    protected void rightClick(OnRightClickListener rightClickListener) {
        this.onRightClickListener = rightClickListener;
    }

    private WebViewClient mWebViewClient=new WebViewClient(){
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i("Info","BaseWebActivity onPageStarted");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (tvTopBarTitle.getText().toString().trim().equals("")){
                if (BaseApplication.getBaseApplication()
                        .WEB_URL_LIST.indexOf(view.getUrl()) >= 0){
                    tvTopBarTitle.setText(BaseApplication.getBaseApplication()
                            .WEB_TITLE_LIST.get(BaseApplication.getBaseApplication()
                                    .WEB_URL_LIST.indexOf(view.getUrl())));
                } else if (view.getUrl().contains("health")) {
                    tvTopBarTitle.setText("健康详情");
                }

            }

            if (view.getUrl().equals(Consts.WEB_HEART_RATE_SPORT) || view.getUrl().equals(Consts.WEB_HEART_RATE_TEST)){
                view.clearHistory();
            }

        }
    };
    private WebChromeClient mWebChromeClient=new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
//            Log.i("Info","progress:"+newProgress);
        }
    };

    public void setUrl(String url){
        targetUrl = url;
    }

    public String getUrl(){

        if (TextUtils.isEmpty(targetUrl)) {
            targetUrl = Consts.WEB_INDEX;
        }
        return targetUrl;
    }

    private ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
//            if (tvTopBarTitle != null)
//                tvTopBarTitle.setText(title);
        }
    };

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("Info", "result:" + requestCode + " result:" + resultCode);
        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAgentWeb.destroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
        mRxManager.clear();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mAgentWeb.handleKeyEvent(keyCode, event) || super.onKeyDown(keyCode, event);
    }


}
