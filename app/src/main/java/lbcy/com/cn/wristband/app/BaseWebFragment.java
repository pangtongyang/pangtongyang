package lbcy.com.cn.wristband.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.just.library.AgentWeb;
import com.just.library.AgentWebSettings;
import com.just.library.ChromeClientCallbackManager;
import com.just.library.DownLoadResultListener;
import com.just.library.PermissionInterceptor;
import com.just.library.WebDefaultSettingsManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.activity.WebActivity;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxManager;
import lbcy.com.cn.wristband.utils.ScreenUtil;

import static lbcy.com.cn.wristband.global.Consts.URL_KEY;

/**
 * Created by chenjie on 2017/9/5.
 */

public abstract class BaseWebFragment extends Fragment {
    public static final String TAG = BaseWebFragment.class.getSimpleName();
    public Activity mActivity;
    protected View mRootView;
    Unbinder unbinder;
    public RxManager mRxManager;
    List<String> webHistoryUrls = new ArrayList<>();

    public boolean loadIndexUrl = false;

    protected AgentWeb mAgentWeb;
    AgentWeb.PreAgentWeb preAgentWeb;

    @BindView(R.id.ll_web_content)
    LinearLayout llWebContent;
//    @BindView(R.id.tv_top_bar_title)
//    TextView tvTopBarTitle;
//    @BindView(R.id.iv_back)
//    ImageView ivBack;
//    @BindView(R.id.iv_righticon)
//    ImageView ivRighticon;
//    @BindView(R.id.rl_top_bar)
//    RelativeLayout rlTopBar;

    //访问一级页面，先清理url历史再访问
    public void clearHistoryUrls() {
        webHistoryUrls.clear();
    }

    public void loadUrl(String url) {
        if (mAgentWeb != null) {
            loadIndexUrl = true;
            clearHistoryUrls();
            mAgentWeb.getLoader().loadUrl(url);
        }
    }

    public AgentWeb getMyAgentWeb() {
        return mAgentWeb;
    }

//    public View getTopBar() {
//        return rlTopBar;
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_web, container, false);
        mActivity = getActivity();
        mRxManager = new RxManager();
        initData();
        unbinder = ButterKnife.bind(this, mRootView);//绑定framgent
        initView();
        loadData();
//        rlTopBar.setVisibility(View.GONE);
        return mRootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent((ViewGroup) view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件
                .setIndicatorColorWithHeight(-1, 2)//设置进度条颜色与高度-1为默认值，2单位为dp
                .setAgentWebWebSettings(getSettings())//设置 AgentWebSettings
                .setWebChromeClient(mWebChromeClient) //WebChromeClient
                .setPermissionInterceptor(mPermissionInterceptor) //权限拦截
                .setReceivedTitleCallback(mCallback)//标题回调
                .setSecurityType(AgentWeb.SecurityType.strict) //严格模式
                .addDownLoadResultListener(mDownLoadResultListener) //下载回调
                .openParallelDownload()//打开并行下载 , 默认串行下载
                .setNotifyIcon(R.mipmap.download)
                .createAgentWeb()//创建AgentWeb
                .ready();//设置 WebSettings

        mAgentWeb = preAgentWeb.go(getUrl());

        mAgentWeb.getWebCreator().get().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getScrollY() == 0 && ((WebView) view).getUrl().contains(Consts.WEB_STAR)){
                    if (motionEvent.getY() <= ScreenUtil.dip2px(mActivity, 133)) {
                        ((WebView) view).requestDisallowInterceptTouchEvent(true);
                    } else {
                        ((WebView) view).requestDisallowInterceptTouchEvent(false);
                    }
                }

                return false;
            }
        });
        WebView webView = mAgentWeb.getWebCreator().get();
        webView.setWebViewClient(mWebViewClient);

        if(mAgentWeb!=null){
            mAgentWeb.getJsInterfaceHolder().addJavaObject("android",new AndroidInterface(mAgentWeb,this.getActivity()));

        }
    }

    //初始化view
    protected abstract void initData();

    protected abstract void initView();

    protected abstract void loadData();

    public AgentWebSettings getSettings() {
        return WebDefaultSettingsManager.getInstance();
    }

    protected WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            return shouldOverrideUrlLoading(view, request.getUrl() + "");
            return false;
        }


        //
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            Log.i(TAG, "mWebViewClient shouldOverrideUrlLoading:" + url);
            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?...package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            if (url.startsWith("intent://") && url.contains("com.youku.phone"))
                return true;
            /*else if (isAlipay(view, url))   //1.2.5开始不用调用该方法了 ，只要引入支付宝sdk即可 ， DefaultWebClient 默认会处理相应url调起支付宝
                return true;*/


            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i(TAG, "url:" + url + " onPageStarted  target:" + getUrl());
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            if (webHistoryUrls.size() == 2 && webHistoryUrls.get(0).contains(view.getUrl())){
                mAgentWeb.getWebCreator().get().clearHistory();
                webHistoryUrls.remove(webHistoryUrls.size() - 1);
            }
            if (!webHistoryUrls.contains(view.getUrl())) {
                if (loadIndexUrl) {
                    webHistoryUrls.add(view.getUrl());
                    mAgentWeb.getWebCreator().get().clearHistory();
                    loadIndexUrl = false;
                    return;
                }

                if (webHistoryUrls.size() != 0) {
                    //if--else前三行未作用，暂时保留
                    if (webHistoryUrls.size() == 2)
                        webHistoryUrls.set(1, view.getUrl());
                    else
                        webHistoryUrls.add(view.getUrl());
                    startActivity(new Intent(mActivity, WebActivity.class).putExtra("url", view.getUrl()));
                    view.loadUrl(webHistoryUrls.get(0));
                } else {
                    boolean isIndex = false;
                    for (int i = 0; i < Consts.WEB_INDEXES.length; i++) {
                        if (Consts.WEB_INDEXES[i].equals(view.getUrl()))
                            isIndex = true;
                    }
                    if (isIndex) {
                        webHistoryUrls.add(view.getUrl());
                    }
                }

            }
        }
    };

    protected String getUrl() {
        String target = "";

        if (TextUtils.isEmpty(target = getArguments().getString(URL_KEY))) {
            target = Consts.WEB_INDEX;
        }
        return target;
    }

    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
            Log.i("console", message + "(" +sourceID  + ":" + lineNumber+")");
            super.onConsoleMessage(message, lineNumber, sourceID);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.i("console", "["+consoleMessage.messageLevel()+"] "+ consoleMessage.message() + "(" +consoleMessage.sourceId()  + ":" + consoleMessage.lineNumber()+")");
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //  super.onProgressChanged(view, newProgress);
            Log.i(TAG, "onProgressChanged:" + newProgress + "  view:" + view);
        }

    };

    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        //AgentWeb 在触发某些敏感的 Action 时候会回调该方法， 比如定位触发 。
        //例如 https//:www.baidu.com 该 Url 需要定位权限， 返回false ，如果版本大于等于23 ， agentWeb 会动态申请权限 ，true 该Url对应页面请求定位失败。
        //该方法是每次都会优先触发的 ， 开发者可以做一些敏感权限拦截 。
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            Log.i(TAG, "url:" + url + "  permission:" + permissions + " action:" + action);
            return false;
        }
    };

    protected ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
//            if (tvTopBarTitle != null && !TextUtils.isEmpty(title))
//                if (title.length() > 10)
//                    title = title.substring(0, 10).concat("...");
//            tvTopBarTitle.setText(title);

        }
    };

    protected DownLoadResultListener mDownLoadResultListener = new DownLoadResultListener() {
        @Override
        public void success(String path) {
            //do you work
        }

        @Override
        public void error(String path, String resUrl, String cause, Throwable e) {
            //do you work
        }
    };

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        mRxManager.clear();
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroyView();
    }
}
