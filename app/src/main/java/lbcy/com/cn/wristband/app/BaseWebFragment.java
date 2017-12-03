package lbcy.com.cn.wristband.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.just.library.AgentWeb;
import com.just.library.AgentWebSettings;
import com.just.library.ChromeClientCallbackManager;
import com.just.library.DownLoadResultListener;
import com.just.library.PermissionInterceptor;
import com.just.library.WebDefaultSettingsManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.activity.WebActivity;
import lbcy.com.cn.wristband.entity.LoginData;
import lbcy.com.cn.wristband.entity.LoginDataDao;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxBus;
import lbcy.com.cn.wristband.rx.RxManager;
import lbcy.com.cn.wristband.widget.SmartRefreshWebLayout;

import static lbcy.com.cn.wristband.global.Consts.URL_KEY;
import static lbcy.com.cn.wristband.global.Consts.WEB_INDEXES;

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

    // 是否加载的是loadUrl，即顶部栏点击事件（用以区分首页和其他跳转页）
    public boolean loadIndexUrl = false;

    private SmartRefreshWebLayout mSmartRefreshWebLayout=null;
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
        mSmartRefreshWebLayout = new SmartRefreshWebLayout(mActivity);

        final SmartRefreshLayout mSmartRefreshLayout= (SmartRefreshLayout) this.mSmartRefreshWebLayout.getLayout();

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Message message = new Message();
                message.what = Consts.RELOAD_DATA;
                RxBus.getInstance().post(Consts.ACTIVITY_MANAGE_LISTENER, message);
                mAgentWeb.getLoader().reload();

                mSmartRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSmartRefreshLayout.finishRefresh();
                    }
                },2000);
            }
        });
//        mSmartRefreshLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (mAgentWeb.getWebCreator().get().getScrollY() == 0 && mAgentWeb.getWebCreator().get().getUrl().contains(Consts.WEB_STAR)){
//                    mSmartRefreshLayout.requestDisallowInterceptTouchEvent(true);
//                } else {
//                    mSmartRefreshLayout.requestDisallowInterceptTouchEvent(false);
//                }
//
//                return false;
//            }
//        });

        preAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent((ViewGroup) view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件
                .setIndicatorColorWithHeight(-1, 2)//设置进度条颜色与高度-1为默认值，2单位为dp
                .setAgentWebWebSettings(getSettings())//设置 AgentWebSettings
                .setWebChromeClient(mWebChromeClient) //WebChromeClient
                .setWebViewClient(mWebViewClient)
                .setPermissionInterceptor(mPermissionInterceptor) //权限拦截
                .setReceivedTitleCallback(mCallback)//标题回调
                .setSecurityType(AgentWeb.SecurityType.strict) //严格模式
                .addDownLoadResultListener(mDownLoadResultListener) //下载回调
                .openParallelDownload()//打开并行下载 , 默认串行下载
                .setNotifyIcon(R.mipmap.download)
                .setWebLayout(mSmartRefreshWebLayout)
                .createAgentWeb()//创建AgentWeb
                .ready();//设置 WebSettings

        mAgentWeb = preAgentWeb.go(getUrl());

        if (getUrl().equals(Consts.WEB_STAR))
            mSmartRefreshLayout.setEnabled(false);

//        mAgentWeb.getWebCreator().get().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (view.getScrollY() == 0 && ((WebView) view).getUrl().contains(Consts.WEB_STAR)){
//                    if (motionEvent.getY() <= ScreenUtil.dip2px(mActivity, 133)) {
//                        ((WebView) view).requestDisallowInterceptTouchEvent(true);
//                    } else {
//                        ((WebView) view).requestDisallowInterceptTouchEvent(false);
//                    }
//                }
//
//                return false;
//            }
//        });

        //设置js调用android方法
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

    //判断localstorage是否已写入
    private boolean jsIsWrite = false;
    private String startUrl;
    protected WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i(TAG, "url:" + url + " onPageStarted  target:" + getUrl());
//            if (!jsIsWrite){
//                startUrl = view.getUrl();
//                view.loadUrl(Consts.WEB_BASE + "/test.html");
//            }
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
                    for (int i = 0; i < WEB_INDEXES.length; i++) {
                        if (WEB_INDEXES[i].equals(view.getUrl())) return;
                    }
                    //if--else前三行未作用，暂时保留
                    if (webHistoryUrls.size() == 2)
                        webHistoryUrls.set(1, view.getUrl());
                    else
                        webHistoryUrls.add(view.getUrl());
                    startActivity(new Intent(mActivity, WebActivity.class).putExtra("url", view.getUrl()));
                    view.loadUrl(webHistoryUrls.get(0));
                } else {
                    boolean isIndex = false;
                    for (int i = 0; i < WEB_INDEXES.length; i++) {
                        if (WEB_INDEXES[i].equals(view.getUrl()))
                            isIndex = true;
                    }
                    if (isIndex) {
                        webHistoryUrls.add(view.getUrl());
                    }
                }

            }

//            if (view.getUrl().equals(Consts.WEB_BASE + "/test.html") &&!jsIsWrite){
//                setJsData(view);
//                view.loadUrl(startUrl);
//                jsIsWrite = true;
//            }
        }
    };

    public void setJsData(WebView webView){
        LoginDataDao loginDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getLoginDataDao();
        LoginData loginData = loginDataDao.loadAll().get(0);
        Gson gson = new Gson();
        String loginJson = gson.toJson(loginData);
        String js = "window.localStorage.setItem('authInf','" + loginJson + "');";
        String jsUrl = "javascript:(function({var localStorage = window.localStorage;localStorage.setItem('authInf','" + loginJson + "')})()";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(js, null);
        } else {
            webView.loadUrl(jsUrl);
            webView.reload();
        }
    }

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
