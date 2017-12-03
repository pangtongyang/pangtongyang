package lbcy.com.cn.wristband.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huichenghe.bleControl.Ble.BluetoothLeService;
import com.huichenghe.bleControl.Utils.FormatUtils;
import com.just.library.AgentWeb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import lbcy.com.cn.blacklibrary.ble.DataCallback;
import lbcy.com.cn.blacklibrary.ble.DeviceConnectListener;
import lbcy.com.cn.blacklibrary.manager.BlackDeviceManager;
import lbcy.com.cn.blacklibrary.manager.BlackDeviceConnectManager;
import lbcy.com.cn.blacklibrary.utils.Util;
import lbcy.com.cn.purplelibrary.app.MyApplication;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.ctl.DataListener;
import lbcy.com.cn.purplelibrary.entity.SportData;
import lbcy.com.cn.purplelibrary.manager.PurpleDeviceManagerNew;
import lbcy.com.cn.purplelibrary.service.PurpleBLEService;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.app.BaseWebFragment;
import lbcy.com.cn.wristband.app.BaseFragmentActivity;
import lbcy.com.cn.wristband.entity.HeartBeatsAllDayDataTo;
import lbcy.com.cn.wristband.entity.HeartBeatsAllDayHistory;
import lbcy.com.cn.wristband.entity.HeartBeatsAllDayHistoryDao;
import lbcy.com.cn.wristband.entity.HeartBeatsAllDayMaxMin;
import lbcy.com.cn.wristband.entity.HeartBeatsAllDayMaxMinDao;
import lbcy.com.cn.wristband.entity.LoginData;
import lbcy.com.cn.wristband.entity.LoginDataDao;
import lbcy.com.cn.wristband.entity.MessageBean;
import lbcy.com.cn.wristband.entity.SleepAllData;
import lbcy.com.cn.wristband.entity.SleepAllDataDao;
import lbcy.com.cn.wristband.entity.SleepTo;
import lbcy.com.cn.wristband.entity.SportAllDayData;
import lbcy.com.cn.wristband.entity.SportAllDayDataDao;
import lbcy.com.cn.wristband.entity.SportSplitData;
import lbcy.com.cn.wristband.entity.SportSplitDataDao;
import lbcy.com.cn.wristband.entity.SportTo;
import lbcy.com.cn.wristband.entity.UserInfoBean;
import lbcy.com.cn.wristband.entity.UserInfoData;
import lbcy.com.cn.wristband.entity.UserInfoDataDao;
import lbcy.com.cn.wristband.fragment.WebFragment;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.manager.NetManager;
import lbcy.com.cn.wristband.service.MyNotificationService;
import lbcy.com.cn.wristband.utils.DateUtil;
import lbcy.com.cn.wristband.utils.HandlerTip;
import lbcy.com.cn.wristband.widget.NoScrollViewPager;
import lbcy.com.cn.wristband.widget.webview.WebLayout;
import retrofit2.Call;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by chenjie on 2017/9/5.
 */

public class MainActivity extends BaseFragmentActivity {
    private static final int BLUETOOTH_OPEN_REQUEST = 14450;

    private final int TAB_HEALTH = 0;
    private final int TAB_KAOQIN = 1;
    private final int TAB_EXPERT = 2;
    private final int TAB_STAR = 3;

    BaseWebFragment healthFragment, kaoqinFragment, expertFragment, starFragment;
    BaseWebFragment[] webFragments = new WebFragment[4];
    FragmentAdapter adapter;

    // 运动数据
    SportAllDayData sportAllDayData;
    SportSplitData sportSplitData;

    int prePage = 0;
    @BindView(R.id.web)
    LinearLayout web;
    @BindView(R.id.tv_top1)
    TextView tvTop1;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.tv_top2)
    TextView tvTop2;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.tv_top3)
    TextView tvTop3;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.ll_home_top_bar)
    LinearLayout llHomeTopBar;
    @BindView(R.id.iv_user)
    ImageView ivUser;
    @BindView(R.id.iv_history)
    ImageView ivHistory;
    @BindView(R.id.rl_home_top_bar)
    RelativeLayout rlHomeTopBar;
    @BindView(R.id.vp_content)
    NoScrollViewPager vpContent;
    @BindView(R.id.tv_bottom1)
    TextView tvBottom1;
    @BindView(R.id.tv_bottom2)
    TextView tvBottom2;
    @BindView(R.id.tv_bottom3)
    TextView tvBottom3;
    @BindView(R.id.tv_bottom4)
    TextView tvBottom4;
    @BindView(R.id.ll_home_bottom_bar)
    LinearLayout llHomeBottomBar;
    @BindView(R.id.rl_top1)
    RelativeLayout rlTop1;
    @BindView(R.id.rl_top2)
    RelativeLayout rlTop2;
    @BindView(R.id.rl_top3)
    RelativeLayout rlTop3;
    @BindView(R.id.ll_baseview)
    LinearLayout llBaseView;

    TextView[] textViews = new TextView[4];


    int mainPage1 = 0, mainPage2 = 0, mainPage3 = 0;
    long mExitTime;
    SPUtil spUtil;
    Bundle mBundle;

    // 当前连接的设备
    String which_device = "2";
    String token;

    // 判断localStorage是否已写入
    // 注意退出登录时，务必修改spUtil:SHAREDPREFERENCES_NAME存储的js_is_write值为0
    private boolean jsIsWrite = false;
    //判断web页面是否已加载
    private boolean isWebInit = false;
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!jsIsWrite) {
                jsIsWrite = setJsData(view);
                spUtil.putString("js_is_write", jsIsWrite ? "1" : "0");
            }
            if (!isWebInit && jsIsWrite) {
                initWeb();

                adapter = new FragmentAdapter(getSupportFragmentManager());
                vpContent.setAdapter(adapter);
                jumpHealthFragment();
                isWebInit = true;
            }
        }
    };

    public boolean setJsData(WebView webView) {
        LoginDataDao loginDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getLoginDataDao();
        if (loginDataDao.count() == 0)
            return false;
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
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (which_device.equals("2") && isSplashed){
//            if (scanHelper == null || !scanHelper.isScanning()) blackConnectAction();
//        }

        // 若进入首页时，蓝牙处于断开状态，则调用连接逻辑
        if (which_device.equals("2")) {
            if (BluetoothLeService.getInstance() == null || BluetoothLeService.getInstance().isConnectedDevice()) {
                connectedThread.start();
            }
        } else {
            if (spUtil.getString("is_connected", "0").equals("0")) {
                connectedThread.start();
            }
        }

        llHomeBottomBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
        toggleNotificationListenerService();

        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
        token = spUtil.getString("token", "");

    }

    @Override
    protected void initView() {

        if (!isSplashed)
            isSplashed = getIntent().getBooleanExtra("isSplashed", false); //判断是否从登录页跳转

        if (!isSplashed) {
            startActivity(new Intent(mActivity, SplashActivity.class));
            //判断是否已登录
            String isLogin = spUtil.getString("is_login", "0");
            if (isLogin.equals("0")) {
                finish();
                return;
            }

            //判断是否设置身高体重
            String hasSetBodyData = spUtil.getString("body_data", "0");
            if (hasSetBodyData.equals("0")) {
                finish();
                return;
            }
        }

        jsIsWrite = spUtil.getString("js_is_write", "0").equals("1");
        which_device = spUtil.getString("which_device", "2");

        setSelected(tvBottom1);

        if (!jsIsWrite) {
            //预加载localStorage
            AgentWeb.with(this)
                    .setAgentWebParent(web, new LinearLayout.LayoutParams(-1, -1))//
                    .useDefaultIndicator()//
                    .defaultProgressBarColor()
                    .setWebViewClient(mWebViewClient)
                    .setSecutityType(AgentWeb.SecurityType.strict)
                    .setWebLayout(new WebLayout(this))
                    .createAgentWeb()//
                    .ready().go(Consts.WEB_TEST);
        } else {
            //初始化web页面
            initWeb();

            adapter = new FragmentAdapter(getSupportFragmentManager());
            vpContent.setAdapter(adapter);
            jumpHealthFragment();
        }


        textViews[0] = tvBottom1;
        textViews[1] = tvBottom2;
        textViews[2] = tvBottom3;
        textViews[3] = tvBottom4;

        vpContent.setNoScroll(true);

        mRxManager.on(Consts.ACTIVITY_MANAGE_LISTENER, new Action1<Message>() {
            @Override
            public void call(Message message) {
                Intent intent;
                switch (message.what) {
                    case Consts.CLOSE_ACTIVITY:
                        finish();
                        break;
                    case Consts.CONNECT_DEVICE:
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter == null) {
                            Toast.makeText(mActivity, "本机没有找到蓝牙硬件或驱动！", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        // 如果本地蓝牙没有开启，则开启
                        if (!bluetoothAdapter.isEnabled()) {
                            // 我们通过startActivityForResult()方法发起的Intent将会在onActivityResult()回调方法中获取用户的选择，比如用户单击了Yes开启，
                            // 那么将会收到RESULT_OK的结果，
                            // 如果RESULT_CANCELED则代表用户不愿意开启蓝牙
                            Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(mIntent, BLUETOOTH_OPEN_REQUEST);
                            // 用enable()方法来开启，无需询问用户(实惠无声息的开启蓝牙设备),这时就需要用到android.permission.BLUETOOTH_ADMIN权限。
                            // mBluetoothAdapter.enable();
                            // mBluetoothAdapter.disable();//关闭蓝牙
                            break;
                        }

                        connectedThread.start();

                        //获取用户基本信息
                        getUserInfoAction();
                        break;
                    case Consts.REQUEST_ENABLE_BT:
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, Consts.REQUEST_ENABLE_BT);
                        break;
                    case Consts.RELOAD_DATA:
                        if (System.currentTimeMillis() - runningTimeOut > 12000) {
                            Thread thread = new Thread(runnableSaveData);
                            thread.start();
                        }
                        break;
                }
            }
        });

        vpContent.setOffscreenPageLimit(4);
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                prePage = position;
                switch (position) {
                    case TAB_HEALTH:
                        jumpHealthFragment();
                        break;
                    case TAB_KAOQIN:
                        jumpKaoqinFragment();
                        break;
                    case TAB_EXPERT:
                        jumpExpertFragment();
                        break;
                    case TAB_STAR:
                        jumpStarFragment();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void loadData() {

    }

    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, MyNotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(this, MyNotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }

    public void initWeb() {
        //webview初始化
        if (webFragments[0] == null) {
            webFragments[0] = healthFragment = WebFragment.getInstance(mBundle = new Bundle());
            mBundle.putString(Consts.URL_KEY, Consts.WEB_INDEX);
        }
        if (webFragments[1] == null) {
            webFragments[1] = kaoqinFragment = WebFragment.getInstance(mBundle = new Bundle());
            mBundle.putString(Consts.URL_KEY, Consts.WEB_CLASS_TODAY);
        }
        if (webFragments[2] == null) {
            webFragments[2] = expertFragment = WebFragment.getInstance(mBundle = new Bundle());
            mBundle.putString(Consts.URL_KEY, Consts.WEB_EXPERT);
        }
        if (webFragments[3] == null) {
            webFragments[3] = starFragment = WebFragment.getInstance(mBundle = new Bundle());
            mBundle.putString(Consts.URL_KEY, Consts.WEB_STAR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_OPEN_REQUEST) {
            if (resultCode == RESULT_OK) {
                connectedThread.start();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(BaseApplication.getBaseApplication(), "蓝牙设备未开启，无法连接手环", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == Consts.REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(BaseApplication.getBaseApplication(), "蓝牙开启成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BaseApplication.getBaseApplication(), "蓝牙开启失败！", Toast.LENGTH_SHORT).show();
            }
        } else {
            //一定要保证 mAgentWebFragment 回调
            webFragments[prePage].onActivityResult(requestCode, resultCode, data);
        }
    }

    private class FragmentAdapter extends FragmentPagerAdapter {
        private final int TAB_COUNT = 4;

        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case TAB_HEALTH:
                    return healthFragment;
                case TAB_KAOQIN:
                    return kaoqinFragment;
                case TAB_EXPERT:
                    return expertFragment;
                case TAB_STAR:
                    return starFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

    private void jumpHealthFragment() {
        if (!tvBottom1.isSelected()) {
            switch (mainPage1) {
                case 0:
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                    break;
                case 1:
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    view3.setVisibility(View.GONE);
                    break;
                case 2:
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.VISIBLE);
                    break;
            }
            rlTop2.setVisibility(View.VISIBLE);
            rlTop3.setVisibility(View.VISIBLE);
            ivHistory.setVisibility(View.VISIBLE);
            tvTop1.setText(R.string.sport);
            tvTop2.setText(R.string.heart_rate);
            tvTop3.setText(R.string.sleep);
            setSelected(tvBottom1);
            vpContent.setCurrentItem(TAB_HEALTH, false);
        }
    }

    private void jumpKaoqinFragment() {
        if (!tvBottom2.isSelected()) {
            switch (mainPage2) {
                case 0:
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                    break;
                case 1:
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    view3.setVisibility(View.GONE);
                    break;
                case 2:
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.VISIBLE);
                    break;
            }
            rlTop2.setVisibility(View.VISIBLE);
            rlTop3.setVisibility(View.VISIBLE);
            ivHistory.setVisibility(View.INVISIBLE);
            tvTop1.setText(R.string.today);
            tvTop2.setText(R.string.week);
            tvTop3.setText(R.string.month);
            setSelected(tvBottom2);
            vpContent.setCurrentItem(TAB_KAOQIN, false);
        }
    }

    private void jumpExpertFragment() {
        if (!tvBottom3.isSelected()) {
            switch (mainPage3) {
                case 0:
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                    break;
                case 1:
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    view3.setVisibility(View.GONE);
                    break;
            }
            rlTop2.setVisibility(View.VISIBLE);
            rlTop3.setVisibility(View.GONE);
            ivHistory.setVisibility(View.INVISIBLE);
            tvTop1.setText(R.string.sport);
            tvTop2.setText(R.string.health);
            setSelected(tvBottom3);
            vpContent.setCurrentItem(TAB_EXPERT, false);
        }
    }

    private void jumpStarFragment() {
        if (!tvBottom4.isSelected()) {
            rlTop2.setVisibility(View.GONE);
            rlTop3.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            ivHistory.setVisibility(View.INVISIBLE);
            tvTop1.setText(R.string.activity_home_tv_star);
            setSelected(tvBottom4);
            vpContent.setCurrentItem(TAB_STAR, false);
        }
    }

    public void setSelected(TextView textView) {
        tvBottom1.setSelected(false);
        tvBottom2.setSelected(false);
        tvBottom3.setSelected(false);
        tvBottom4.setSelected(false);
        textView.setSelected(true);
    }

    @OnClick({R.id.tv_bottom1, R.id.tv_bottom2, R.id.tv_bottom3, R.id.tv_bottom4, R.id.iv_user})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_bottom1:
                jumpHealthFragment();

                break;
            case R.id.tv_bottom2:
                jumpKaoqinFragment();
                break;
            case R.id.tv_bottom3:
                jumpExpertFragment();
                break;
            case R.id.tv_bottom4:
                jumpStarFragment();
                break;
            case R.id.iv_user:
                System.gc();
                intent = new Intent(mActivity, MeActivity.class);
                startActivity(intent);
                break;
        }
    }

    // 上次点击时间戳
    long lastTopClickTime = 0;
    // 多次连续点击，时间间隔小于1s时，记录点击次数
    int clickTimes = 0;

    @OnClick({R.id.rl_top1, R.id.rl_top2, R.id.rl_top3, R.id.iv_history})
    public void topClick(View v) {
        // 用户两次点击顶部栏的时间间隔应超过1s，确保上次页面已基本加载完成
        if (System.currentTimeMillis() - lastTopClickTime < 1000) {
            lastTopClickTime = System.currentTimeMillis();
            // 防止用户间隔1s内持续点击顶部栏，而页面不进行跳转
            if (clickTimes < 1) {
                clickTimes++;
                return;
            }
        }
        lastTopClickTime = System.currentTimeMillis();
        if (clickTimes != 0) clickTimes = 0;

        // 点击事件处理
        switch (v.getId()) {
            case R.id.rl_top1:
                if (view1.getVisibility() == View.GONE) {
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                    switch (prePage) {
                        case 0:
                            mainPage1 = 0;
                            healthFragment.loadUrl(Consts.WEB_INDEX);
                            break;
                        case 1:
                            mainPage2 = 0;
                            kaoqinFragment.loadUrl(Consts.WEB_CLASS_TODAY);
                            break;
                        case 2:
                            mainPage3 = 0;
                            expertFragment.loadUrl(Consts.WEB_EXPERT);
                            break;
                        case 3:
                            view1.setVisibility(View.GONE);
                            break;
                    }
                }
                break;
            case R.id.rl_top2:
                if (view2.getVisibility() == View.GONE) {
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    view3.setVisibility(View.GONE);
                    switch (prePage) {
                        case 0:
                            mainPage1 = 1;
                            healthFragment.loadUrl(Consts.WEB_HEART_RATE_INDEX);
                            break;
                        case 1:
                            mainPage2 = 1;
                            kaoqinFragment.loadUrl(Consts.WEB_CLASS_WEEK);
                            break;
                        case 2:
                            mainPage3 = 1;
                            expertFragment.loadUrl(Consts.WEB_HEALTH);
                            break;
                    }
                }
                break;
            case R.id.rl_top3:
                if (view3.getVisibility() == View.GONE) {
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.VISIBLE);
                    switch (prePage) {
                        case 0:
                            mainPage1 = 2;
                            healthFragment.loadUrl(Consts.WEB_SLEEP_INDEX);
                            break;
                        case 1:
                            mainPage2 = 2;
                            kaoqinFragment.loadUrl(Consts.WEB_CLASS_MONTH);
                            break;
                    }
                }
                break;
            case R.id.iv_history:
                if (view1.getVisibility() == View.VISIBLE)
                    startActivity(new Intent(mActivity, WebActivity.class).putExtra("url", Consts.WEB_HISTORY));
                else if (view2.getVisibility() == View.VISIBLE)
                    startActivity(new Intent(mActivity, WebActivity.class).putExtra("url", Consts.WEB_HEART_RATE_HISTORY));
                else if (view3.getVisibility() == View.VISIBLE)
                    startActivity(new Intent(mActivity, WebActivity.class).putExtra("url", Consts.WEB_SLEEP_HISTORY));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
//                if (which_device.equals("1")){
//                    HandlerTip.getInstance().postDelayed(500, new HandlerTip.HandlerCallback() {
//                        @Override
//                        public void postDelayed() {
//                            System.exit(0);
//                        }
//                    });
//                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //紫色手环销毁
        Intent intent = new Intent(mActivity, PurpleBLEService.class);
        intent.setAction("lbcy.com.cn.purplelibrary.service.PurpleBLEService");
        stopService(intent);
        //紫色手环销毁
//        stopService();
//        try {
//            if (internalReceiver != null) {
//                unregisterReceiverandStopReconnect(internalReceiver);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //黑色手环销毁
        connectStateHandler.removeCallbacks(connectStateRunnable);
        if (BluetoothLeService.getInstance() != null) {
            BluetoothLeService.getInstance().disconnect();
            BluetoothLeService.getInstance().stopSelf();
        }
        if (manager != null) {
            manager.unregisterReceiverandStopReconnect();
            manager = null;
        }
        // 销毁手环连接线程
        if (connectedThread != null) {
            connectedThread.interrupt();
            connectedThread = null;
        }
    }

    // 获取用户基本信息
    private void getUserInfoAction() {
        NetManager.getUserInfoAction(token, new NetManager.NetCallBack<UserInfoBean>() {
            @Override
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                UserInfoBean userInfoBean = response.body();
                if ((userInfoBean != null ? userInfoBean.getCode() : 0) == 200) {
                    saveData(userInfoBean.getData());
                } else {
                    Toast.makeText(mActivity, userInfoBean != null ? userInfoBean.getMessage().toString() : "用户基本信息获取失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfoBean> call, Throwable t) {

            }
        });
    }

    private void saveData(UserInfoData data) {
        UserInfoDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getUserInfoDataDao();
        if (dataDao.count() == 0) {
            dataDao.insert(data);
        } else {
            data.setMId(dataDao.loadAll().get(0).getMId());
            dataDao.update(data);
        }
    }

    // 上传运动数据
    private void uploadSportData() {
        // 获取整天数据列表
        final SportAllDayDataDao dayDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getSportAllDayDataDao();
        // 无数据，返回
        if (dayDataDao.count() != 0) {
            List<SportAllDayData> dayDataList = dayDataDao.queryBuilder().where(SportAllDayDataDao.Properties.IsUpload.eq(false)).build().list();
            // 根据整天数据列表获取每天分时数据
            SportSplitDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getSportSplitDataDao();
            for (final SportAllDayData dayData : dayDataList) {
                // 获取到当天数据，不上传
                if (dayData.getDate().equals(DateUtil.getCurrentTime_Y_M_d()))
                    continue;

                List<SportSplitData> list = dataDao.queryBuilder().where(SportSplitDataDao.Properties.Date.like(dayData.getDate() + "%")).build().list();
                // 无分时数据，<!--跳出继续执行-->，可以继续上传，受限于紫色手环没有分时数据
//            if (list.size() == 0)
//                continue;

                // 上传数据
                SportTo sportTo = new SportTo();
                sportTo.setDate(dayData.getDate());
                sportTo.setDone_steps(dayData.getDone_steps());
                List<SportTo.HistoryBean> historyList = new ArrayList<>();
                for (SportSplitData data : list) {
                    SportTo.HistoryBean historyBean = new SportTo.HistoryBean();
                    historyBean.setTime(data.getDate());
                    historyBean.setSteps(data.getSteps());
                    historyBean.setDuration(data.getDuration());
                    historyList.add(historyBean);
                }
                if (historyList.size() != 0)
                    sportTo.setHistory(historyList);
                else
                    continue;
                NetManager.uploadSportDataAction(token, sportTo, new NetManager.NetCallBack<MessageBean>() {
                    @Override
                    public void onResponse(Call<MessageBean> call, Response<MessageBean> response) {
                        MessageBean messageBean = response.body();
                        if (messageBean != null) {
                            if (messageBean.getCode() == 200) {
                                // 更新上传状态 已上传
                                dayData.setIsUpload(true);
                                dayDataDao.update(dayData);
                            } else {
                                Toast.makeText(BaseApplication.getBaseApplication(), "运动数据上传失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageBean> call, Throwable t) {
                        Log.e("testtest", t.toString());
                    }
                });
            }
        }
    }

    // 上传睡眠数据
    private void uploadSleepData() {

        // 获取睡眠数据
        final SleepAllDataDao allDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getSleepAllDataDao();
        if (allDataDao.count() != 0) {
            List<SleepAllData> sleepAllDataList = allDataDao.queryBuilder().where(SleepAllDataDao.Properties.Is_upload.eq(false)).build().list();
            for (final SleepAllData allData : sleepAllDataList) {
                SleepTo sleepTo = new SleepTo();
                sleepTo.setDate(allData.getTod_date());
                sleepTo.setStart_time(allData.getStart_time());
                sleepTo.setEnd_time(allData.getEnd_time());
                sleepTo.setDeep_duration(allData.getDeep_duration());
                sleepTo.setLight_duration(allData.getLight_duration());
                sleepTo.setWake_duration(allData.getWake_duration());
                sleepTo.setScore(allData.getScore());
                List<SleepTo.HistoryBean> historyBeanList = new ArrayList<>();

                int sleepHour = Integer.valueOf(sleepTo.getStart_time().substring(11, 13));
                int wakeHour;
                if (Integer.valueOf(sleepTo.getEnd_time().substring(14, 16)) > 0)
                    wakeHour = Integer.valueOf(sleepTo.getEnd_time().substring(11, 13)) + 1;
                else
                    wakeHour = Integer.valueOf(sleepTo.getEnd_time().substring(11, 13));
                int length = ((24 - sleepHour) + wakeHour) >= 24 ? ((24 - sleepHour) + wakeHour) - 24 : ((24 - sleepHour) + wakeHour);

                // 获取分时睡眠数据
                for (int i = 0; i < length; i++) {
                    SleepTo.HistoryBean historyBean = new SleepTo.HistoryBean();
                    int hour = (sleepHour + i) >= 24 ? (sleepHour + i) - 24 : (sleepHour + i);
                    String strHour = hour < 10 ? "0" + hour : hour + "";
                    if (hour > 12)
                        historyBean.setTime(allData.getYes_date() + " " + strHour + ":00:00");
                    else
                        historyBean.setTime(allData.getTod_date() + " " + strHour + ":00:00");
                    historyBean.setIndex(Integer.valueOf(allData.getSleep_data().substring(6 * i, 6 + 6 * i)));
                    historyBeanList.add(historyBean);
                }
                sleepTo.setHistory(historyBeanList);

                NetManager.uploadSleepDataAction(token, sleepTo, new NetManager.NetCallBack<MessageBean>() {
                    @Override
                    public void onResponse(Call<MessageBean> call, Response<MessageBean> response) {
                        MessageBean message = response.body();
                        if (message != null && message.getCode() == 200) {
                            allData.setIs_upload(true);
                            allDataDao.update(allData);
                        } else {
                            Toast.makeText(BaseApplication.getBaseApplication(), "睡眠数据上传失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageBean> call, Throwable t) {

                    }
                });
            }
        }
    }

    // 上传全天心率数据
    private void uploadHeartData() {

        // 获取心率数据
        HeartBeatsAllDayDataTo dayDataTo = new HeartBeatsAllDayDataTo();
        final HeartBeatsAllDayMaxMinDao maxMinDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHeartBeatsAllDayMaxMinDao();
        List<HeartBeatsAllDayMaxMin> maxMinList = maxMinDao.queryBuilder().where(HeartBeatsAllDayMaxMinDao.Properties.IsUpload.eq(false)).build().list();
        for (final HeartBeatsAllDayMaxMin maxMin : maxMinList) {
            // 获取到当天数据，不上传
            if (maxMin.getDate().equals(DateUtil.getCurrentTime_Y_M_d()))
                continue;
            HeartBeatsAllDayHistoryDao historyDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHeartBeatsAllDayHistoryDao();
            List<HeartBeatsAllDayHistory> histories = historyDao.queryBuilder().
                    where(HeartBeatsAllDayHistoryDao.Properties.Time.like(maxMin.getDate() + "%")).
                    build().list();
            // 数据不存在
            if (histories.size() == 0)
                continue;
            // 插入总数据
            dayDataTo.setDate(maxMin.getDate());
            dayDataTo.setMin_heartbeats(maxMin.getMin_heartbeats());
            dayDataTo.setMax_heartbeats(maxMin.getMax_heartbeats());
            List<HeartBeatsAllDayDataTo.HistoryBean> historyBeanList = new ArrayList<>();
            // 获取分时数据
            for (HeartBeatsAllDayHistory history : histories) {
                HeartBeatsAllDayDataTo.HistoryBean historyBean = new HeartBeatsAllDayDataTo.HistoryBean();
                historyBean.setHeartbeats(history.getHeartbeats());
                historyBean.setTime(history.getTime());
                historyBeanList.add(historyBean);
            }
            // 插入分时数据
            dayDataTo.setHistory(historyBeanList);

            NetManager.uploadAllDayHeartBeats(token, dayDataTo, new NetManager.NetCallBack<MessageBean>() {
                @Override
                public void onResponse(Call<MessageBean> call, Response<MessageBean> response) {
                    MessageBean message = response.body();
                    if ((message != null ? message.getCode() : 0) == 200) {
                        maxMin.setIsUpload(true);
                        maxMinDao.update(maxMin);
                    } else {
                        Toast.makeText(BaseApplication.getBaseApplication(), "心率数据上传失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MessageBean> call, Throwable t) {

                }
            });
        }
    }

    // 下拉刷新超过12s再刷新数据，未超过则不执行
    long runningTimeOut = 0;
    // 异步执行
    Runnable runnableSaveData = new Runnable() {
        @Override
        public void run() {
            runningTimeOut = System.currentTimeMillis();
            if (which_device.equals("2")) {
                b_getDataFromDevice();
            } else {
                if (MyApplication.getInstances().getThread() == null) {
                    return;
                }
                while (!MyApplication.getInstances().getThread().canCallBack) {
                    if (MyApplication.getInstances().getThread() == null) {
                        return;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                p_getDataFromDevice();
            }
        }
    };

    // 黑色手环、紫色手环连接逻辑，单独建立线程，防止页面卡顿
    MyThread connectedThread = new MyThread();

    // 黑色手环设备连接超时监听
    Handler connectStateHandler = new Handler();
    Runnable connectStateRunnable = new Runnable() {
        @Override
        public void run() {
            if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                Toast.makeText(getApplicationContext(), "仍未连接手环，请重启App后重试", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // 手环连接线程
    private class MyThread extends Thread {

        // 是否为首次连接
        private boolean isFirstConnect = true;

        @Override
        public void run() {
            if (which_device.equals("2")) blackConnectAction();
            else purpleConnectAction();
        }

        // 紫色手环连接逻辑
        private void purpleConnectAction() {
            Intent intent = new Intent(mActivity, PurpleBLEService.class);
            intent.setAction("lbcy.com.cn.purplelibrary.service.PurpleBLEService");
            String mac_address = spUtil.getString("deviceAddress");
            intent.putExtra("mac_address", mac_address);
            startService(intent);

            while (spUtil.getString("is_connected", "0").equals("0")) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 获取运动数据，并上传非当天数据
            Thread thread = new Thread(runnableSaveData);
            thread.start();
        }

        // 黑色手环连接逻辑
        private void blackConnectAction() {
            // 连接经过的时间
            final long[] connectingDuration = {System.currentTimeMillis()};
            if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                if (isFirstConnect) {
//                    Log.e("1111110", Looper.myLooper() == Looper.getMainLooper()? "1" : "0");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "设备连接中", Toast.LENGTH_SHORT).show();

                            connectStateHandler.postDelayed(connectStateRunnable, 30000);
                        }

                    });

                    isFirstConnect = false;
                }
            }
            String mac_address = spUtil.getString("deviceAddress");

            if (manager == null) {
                manager = new BlackDeviceConnectManager(mActivity, mac_address);
            }

            manager.startConnectingCallBack(new DeviceConnectListener() {
                @Override
                public void connect() {
                    connectingDuration[0] = System.currentTimeMillis() - connectingDuration[0];
                    // 防止连接过快导致显示过快
                    HandlerTip.getInstance().postDelayed((5000 - connectingDuration[0] > 0 ? (int) (5000 - connectingDuration[0]) : 100), new HandlerTip.HandlerCallback() {
                        @Override
                        public void postDelayed() {
                            Toast.makeText(getApplicationContext(), "连接成功！", Toast.LENGTH_SHORT).show();
                            connectStateHandler.removeCallbacks(connectStateRunnable);
                        }
                    });

                    // 心率监测状态获取
                    BlackDeviceManager.getInstance().getHeartRateScanningState(new DataCallback<Bundle>() {
                        @Override
                        public void OnSuccess(Bundle data) {
                            int state = data.getInt("state");
                            int freq = data.getInt("scanFreq");
                            String freqText = (freq == 1 ? freq + " 小时/次" : freq + "分钟/次");
                            if (state == 1 && spUtil.getString("scan_heart_rate_scan_switch", "0").equals("0")) {
                                spUtil.putString("scan_heart_rate_time", freqText);
                                spUtil.putString("scan_heart_rate_scan_switch", "1");
                            }
                        }

                        @Override
                        public void OnFailed() {

                        }

                        @Override
                        public void OnFinished() {
                            //心率预警状态获取
                            BlackDeviceManager.getInstance().getHeartRateWarningState(new DataCallback<Bundle>() {
                                @Override
                                public void OnSuccess(Bundle data) {
                                    int max = data.getInt("max");
                                    int min = data.getInt("min");
                                    int state = data.getInt("state"); // 0 -> 开， 1 -> 关
                                    if (state == 0 && spUtil.getString("scan_heart_rate_predict_switch", "0").equals("0")) {
                                        spUtil.putString("scan_heart_rate_max_heart_rate", String.valueOf(max));
                                        spUtil.putString("scan_heart_rate_min_heart_rate", String.valueOf(min));
                                        spUtil.putString("scan_heart_rate_predict_switch", "1");
                                    }
                                }

                                @Override
                                public void OnFailed() {

                                }

                                @Override
                                public void OnFinished() {

                                }
                            });
                        }
                    });

                    // 防丢开关状态获取
                    BlackDeviceManager.getInstance().getLostState(new DataCallback<Boolean>() {
                        @Override
                        public void OnSuccess(Boolean data) {
                            if (data && spUtil.getString("rl_loss", "0").equals("0")) {
                                spUtil.putString("rl_loss", "1");
                            }
                        }

                        @Override
                        public void OnFailed() {

                        }

                        @Override
                        public void OnFinished() {

                        }
                    });

                    // 抬腕显示状态获取
                    BlackDeviceManager.getInstance().getAwakeState(new DataCallback<Boolean>() {
                        @Override
                        public void OnSuccess(Boolean data) {
                            if (data && spUtil.getString("rl_hand_up", "0").equals("0")) {
                                spUtil.putString("rl_hand_up", "1");
                            }
                        }

                        @Override
                        public void OnFailed() {

                        }

                        @Override
                        public void OnFinished() {

                        }
                    });

                    // 久坐提醒状态获取
                    BlackDeviceManager.getInstance().getSitRemindState(new DataCallback<Bundle>() {
                        @Override
                        public void OnSuccess(Bundle data) {
                            if (data.getInt("isOpen") == 1 && spUtil.getString("sedentary_checked", "0").equals("0")) {
                                spUtil.putString("startSedentary", data.getString("beginTime"));
                                spUtil.putString("endSedentary", data.getString("endTime"));
                                spUtil.putString("spaceSedentary", data.getInt("duration") + "分钟");
                                spUtil.putString("sedentary_checked", "1");
                            }
                        }

                        @Override
                        public void OnFailed() {

                        }

                        @Override
                        public void OnFinished() {

                        }
                    });

                    // 时间同步
                    BlackDeviceManager.getInstance().synTime(new DataCallback<byte[]>() {
                        @Override
                        public void OnSuccess(byte[] data) {

                        }

                        @Override
                        public void OnFailed() {

                        }

                        @Override
                        public void OnFinished() {

                        }
                    });

                    //心率实时获取方法，包含其他方法回调，需要提前调用
                    BlackDeviceManager.getInstance().startHeartRateListener(new DataCallback<byte[]>() {
                        @Override
                        public void OnSuccess(byte[] data) {

                        }

                        @Override
                        public void OnFailed() {

                        }

                        @Override
                        public void OnFinished() {

                        }
                    });

                    BlackDeviceManager.getInstance().getHardwareVersion(new DataCallback<String>() {
                        @Override
                        public void OnSuccess(String data) {
                            spUtil.putString("black_version", data);
                        }

                        @Override
                        public void OnFailed() {

                        }

                        @Override
                        public void OnFinished() {

                        }
                    });

                    if (Looper.getMainLooper() != Looper.myLooper()) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // 获取运动数据，并上传非当天数据，紫色手环连接成功务必也要调用！！！
                    Thread thread = new Thread(runnableSaveData);
                    thread.start();
                }
            });

            manager.connectDevice();

        }
    }

    /**************************************************************************/
    //紫色手环连接相关
    private void p_getDataFromDevice() {
        // 同步时间
        PurpleDeviceManagerNew.getInstance().syncTime();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 获取当天运动数据（步数）
        PurpleDeviceManagerNew.getInstance().getSportData(new DataListener<String>() {
            @Override
            public void getData(String data) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date datetime = new Date();
                String date = format.format(datetime);

                sportAllDayData = new SportAllDayData();
                sportAllDayData.setDate(date);
                sportAllDayData.setDone_steps(Integer.valueOf(data));
                SportAllDayDataDao dayDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().
                        getSportAllDayDataDao();
                SportAllDayData buf = dayDataDao.queryBuilder().
                        where(SportAllDayDataDao.Properties.Date.eq(date)).
                        build().unique();
                if (buf == null) {
                    dayDataDao.insert(sportAllDayData);
                } else {
                    sportAllDayData.setId(buf.getId());
                    sportAllDayData.setIsUpload(buf.getIsUpload());
                    dayDataDao.update(sportAllDayData);
                }

                // 紫色手环上传运动数据
                uploadSportData();
            }
        });

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 读取设备信息（包括闹钟信息）
        PurpleDeviceManagerNew.getInstance().readDeviceConfig();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 获取历史运动数据
        PurpleDeviceManagerNew.getInstance().getSportHistory(new DataListener<List<SportData>>() {
            @Override
            public void getData(List<SportData> data) {
                for (SportData mData : data) {
                    String date = mData.getDate();

                    sportAllDayData = new SportAllDayData();
                    sportAllDayData.setDate(date);
                    sportAllDayData.setDone_steps(mData.getStep());
                    SportAllDayDataDao dayDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().
                            getSportAllDayDataDao();
                    SportAllDayData buf = dayDataDao.queryBuilder().
                            where(SportAllDayDataDao.Properties.Date.eq(date)).
                            build().unique();
                    if (buf == null) {
                        dayDataDao.insert(sportAllDayData);
                    } else {
                        sportAllDayData.setId(buf.getId());
                        sportAllDayData.setIsUpload(buf.getIsUpload());
                        dayDataDao.update(sportAllDayData);
                    }
                }

                // 紫色手环上传运动数据
                uploadSportData();
            }
        });

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 获取睡眠数据
        PurpleDeviceManagerNew.getInstance().getSleepData(new DataListener<Bundle>() {
            @Override
            public void getData(Bundle data) {
                boolean isSleepValid = data.getBoolean("isSleepValid");
                byte sleepHour = data.getByte("sleepHour");
                byte sleepMin = data.getByte("sleepMin");
                byte wakeHour = data.getByte("wakeHour");
                byte wakeMin = data.getByte("wakeMin");
                byte wakeCount = data.getByte("wakeCount");
                int deepTime = data.getInt("deepTime");
                int lightTime = data.getInt("lightTime");
                int sleepScore = data.getInt("sleepScore");
                byte[] sleepShowRaw = data.getByteArray("sleepShowRaw");
                int sleepShowRawi = data.getInt("sleepShowRawi");

                // 睡眠是否有效
                if (!isSleepValid)
                    return;

                String yes_date = DateUtil.getYesterdayTime_Y_M_d();
                String tod_date = DateUtil.getCurrentTime_Y_M_d();
                String sleepTime = (sleepHour < 10 ? "0" + sleepHour : sleepHour) + ":" + (sleepMin < 10 ? "0" + sleepMin : sleepMin) + ":00";
                String wakeTime = (wakeHour < 10 ? "0" + wakeHour : wakeHour) + ":" + (wakeMin < 10 ? "0" + wakeMin : wakeMin) + ":00";
                String startTime = yes_date + " " + sleepTime;
                String endTime = tod_date + " " + wakeTime;

                if (sleepShowRaw == null)
                    return;

                int deep = deepTime;
                int light = lightTime;
                int wake = sleepShowRaw.length - deep - light;
                int buf_state = 0;
                // 处理后的睡眠数据
                StringBuilder buffer = new StringBuilder("");
                // 睡眠原数据字符串
                StringBuilder sleepAllData = Util.bytesToString(sleepShowRaw);
                // 处理前的睡眠数据前缀
                StringBuilder before = new StringBuilder("");
                // 处理前的睡眠数据后缀
                StringBuilder later = new StringBuilder("");
                for (int i = 0; i < sleepMin; i++) {
                    before.append(2);
                }
                if (wakeMin > 0)
                    for (int i = 0; i < 60 - wakeMin; i++) {
                        later.append(2);
                    }
                sleepAllData = before.append(sleepAllData).append(later);
                // // 睡眠原数据char数组
                char[] sleepData = sleepAllData.toString().toCharArray();

                for (int i = 0; i < sleepData.length; i += 10) {
                    for (int j = i; j < i + 10; j++) {
                        if (j >= sleepData.length)
                            break;
                        if (sleepData[j] == '3') {
                            buf_state = 3;
                            break;
                        } else if (sleepData[j] == '0' && buf_state < 2) {
                            buf_state = 2;
                        } else if (sleepData[j] == '2' && buf_state < 1) {
                            buf_state = 1;
                        }
                    }
                    buffer.append(buf_state);
                    buf_state = 0;
                }

                SleepAllDataDao allDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getSleepAllDataDao();
                // 校验今日睡眠是否已缓存，已缓存则无需再次缓存
                if (allDataDao.queryBuilder().where(SleepAllDataDao.Properties.Yes_date.eq(yes_date)).count() != 0)
                    return;

                // 缓存总睡眠数据
                SleepAllData allData = new SleepAllData(null, yes_date, tod_date, startTime, endTime, deep, light, wake, sleepScore, buffer.toString(), false);
                allDataDao.insert(allData);

                // 紫色手环上传睡眠数据
                uploadSleepData();
            }
        });

    }

    /**************************************************************************/
    //黑色手环相关
    BlackDeviceConnectManager manager;

    // 黑色手环待上传数据统一获取
    private void b_getDataFromDevice() {
        // 获取全天数据
        BlackDeviceManager.getInstance().getDayData(new DataCallback<byte[]>() {
            @Override
            public void OnSuccess(byte[] data) {

                int day = data[0];                // 日
                int month = data[1];              // 月
                int year = data[2] + 2000;        // 年
                int stepAll = FormatUtils.byte2Int(data, 4);

                Calendar calendarData = Calendar.getInstance();
                calendarData.set(year, (month - 1), day);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String date = format.format(calendarData.getTime());

                sportAllDayData = new SportAllDayData();
                sportAllDayData.setDate(date);
                sportAllDayData.setDone_steps(stepAll);
                SportAllDayDataDao dayDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().
                        getSportAllDayDataDao();
                SportAllDayData buf = dayDataDao.queryBuilder().
                        where(SportAllDayDataDao.Properties.Date.eq(date)).
                        build().unique();
                if (buf == null) {
                    dayDataDao.insert(sportAllDayData);
                } else {
                    sportAllDayData.setId(buf.getId());
                    sportAllDayData.setIsUpload(buf.getIsUpload());
                    dayDataDao.update(sportAllDayData);
                }
            }

            @Override
            public void OnFailed() {

            }

            @Override
            public void OnFinished() {

            }
        });

        if (Looper.myLooper() != Looper.getMainLooper()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 获取每小时数据
        BlackDeviceManager.getInstance().getEachHourStep(new DataCallback<byte[]>() {
            @Override
            public void OnSuccess(byte[] data) {
                int day = data[0];                // 日
                int month = data[1];              // 月
                int year = data[2] + 2000;        // 年

                Calendar calendarData = Calendar.getInstance();
                calendarData.set(year, (month - 1), day);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String date = format.format(calendarData.getTime());

                int step;
                for (int i = 0; i < 24; i++) {
                    step = FormatUtils.byte2Int(data, 4 * (i + 1));
                    if (step == -1)
                        step = 0;
                    sportSplitData = new SportSplitData();
                    sportSplitData.setDate(date + " " + (String.valueOf(i).length() == 1 ? ("0" + i) : (i)) + ":00:00");
                    sportSplitData.setSteps(step);
                    sportSplitData.setDuration("01:00:00");
                    SportSplitDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getSportSplitDataDao();
                    SportSplitData buf = dataDao.queryBuilder().where(SportSplitDataDao.Properties.Date.eq(sportSplitData.getDate())).build().unique();
                    if (buf == null) {
                        dataDao.insert(sportSplitData);
                    } else {
                        sportSplitData.setId(buf.getId());
                        dataDao.update(sportSplitData);
                    }
                }

                // 黑色手环，上传运动数据
                uploadSportData();
            }

            @Override
            public void OnFailed() {

            }

            @Override
            public void OnFinished() {

            }
        });


        if (Looper.myLooper() != Looper.getMainLooper()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 10点之后能获取一天的完整数据，因此10点后再取值
        if (DateUtil.getCurrentTime_H_m().compareTo("10:00") >= 0) {
            // 获取昨晚睡眠数据
            BlackDeviceManager.getInstance().getSleepData(new DataCallback<String>() {
                @Override
                public void OnSuccess(String data) {
                    // 获取时间
                    String yes_date = data.split("###")[1];
                    String tod_date = data.split("###")[2];
                    // 获取数据
                    data = data.split("###")[0];

                    // 3未设置，代表0清醒
                    data = data.replaceAll("3", "0");
                    // 2深睡，改为3深睡
                    data = data.replaceAll("2", "3");
                    // 1浅睡，改为2浅睡
                    data = data.replaceAll("1", "2");
                    // 0清醒，改为1清醒
                    data = data.replaceAll("0", "1");

                    char[] sleepData = data.toCharArray();
                    int deep = 0;
                    int light = 0;
                    int wake = 0;

                    // 根据头尾连续0，确定真实睡眠时间
                    // 开始位置、结束位置（真实睡眠时间）
                    int startPos = 0, endPos = -1;

                    for (int i = sleepData.length - 1; i >= 0; i--) {
                        if (sleepData[i] != '1') {
                            endPos = i;
                            break;
                        }
                    }
                    // 没有睡眠数据，直接返回
                    if (endPos == -1)
                        return;

                    for (int i = 0; i < sleepData.length; i++) {
                        if (sleepData[i] != '1') {
                            startPos = i;
                            break;
                        }
                    }

                    // 获取不同类别睡眠状态时间
                    for (int i = startPos; i <= endPos; i++) {
                        switch (sleepData[i]) {
                            case '1':
                                wake += 10;
                                break;
                            case '2':
                                light += 10;
                                break;
                            case '3':
                                deep += 10;
                                break;
                        }
                    }

                    SleepAllDataDao allDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getSleepAllDataDao();
                    // 校验今日睡眠是否已缓存，已缓存则无需再次缓存
                    if (allDataDao.queryBuilder().where(SleepAllDataDao.Properties.Yes_date.eq(yes_date)).count() == 0) {
                        // 开始记录时间
                        String record_startTime = yes_date + " 22:00:00";
                        long l_startTime = DateUtil.stringToLong(record_startTime) + startPos * 10 * 60 * 1000;
                        // 真实睡眠开始时间
                        String startTime = DateUtil.longToString(l_startTime);

                        // 结束记录时间
                        String record_endTime = tod_date + " 10:00:00";
                        long l_endTime = DateUtil.stringToLong(record_endTime) - (sleepData.length - endPos - 1) * 10 * 60 * 1000;
                        // 真实睡眠结束时间
                        String endTime = DateUtil.longToString(l_endTime);
                        int score = (int) ((deep + light) / 60.0 / 8.0 * 100);

                        // 真实数据（10分钟1个数）
                        String realData = data.substring(startPos, endPos + 1);
                        // 真实数据前缀(保证数据整点开始、整点结束，每小时都有6个数)
                        StringBuilder before = new StringBuilder();
                        // 真实数据后缀
                        StringBuilder later = new StringBuilder();

                        for (int i = 0; i < Integer.valueOf(startTime.substring(14, 15)); i++) {
                            before.append("1");
                        }
                        if (Integer.valueOf(endTime.substring(14, 15)) != 0) {
                            for (int i = 0; i < 6 - Integer.valueOf(endTime.substring(14, 15)); i++) {
                                later.append("1");
                            }
                        }
                        // 带前后缀的数据
                        String totalData = before.append(realData).append(later).toString();

                        // 缓存总睡眠数据
                        SleepAllData allData = new SleepAllData(null, yes_date, tod_date, startTime, endTime, deep, light, wake, score, totalData, false);
                        allDataDao.insert(allData);
                    }

                    // 黑色手环，上传睡眠数据
                    uploadSleepData();
                }

                @Override
                public void OnFailed() {

                }

                @Override
                public void OnFinished() {

                }
            });
        }


        if (Looper.myLooper() != Looper.getMainLooper()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 获取全天心率数据
        BlackDeviceManager.getInstance().getDayHeartRateData(new DataCallback<byte[]>() {
            @Override
            public void OnSuccess(byte[] data) {
                int maxValue = 0, minValue = Integer.MAX_VALUE;

                int day = (data[0] & 0xff);
                int month = (data[1] & 0xff);
                int year = (data[2] & 0xff) + 2000;

                byte[] dataThree = new byte[180];

                // 复制以获取所有心率数据
                System.arraycopy(data, 6, dataThree, 0, dataThree.length);

                Calendar calendarData = Calendar.getInstance();
                calendarData.set(year, (month - 1), day);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String date = format.format(calendarData.getTime());

                // 心率历史数据库
                HeartBeatsAllDayHistoryDao historyDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHeartBeatsAllDayHistoryDao();

                int base = data[5];
                //(0+(base-1)*3)点到(2+(base-1)*3)点数据
                for (int j = 0; j < dataThree.length / 60; j++) {
                    for (int i = j * 60; i < j * 60 + 60; i++) {
                        int heartRate = (dataThree[i] == 0 || dataThree[i] == -1) ? 0 : dataThree[i];
                        if (dataThree[i] != 0 && heartRate > 0) {
                            if (heartRate > maxValue)
                                maxValue = heartRate;
                            if (heartRate < minValue)
                                minValue = heartRate;
                        }

                        int hour = j + (base - 1) * 3;
                        int minute = i - j * 60;
                        int second = 0;
                        calendarData.set(year, (month - 1), day, hour, minute, second);
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String time = format1.format(calendarData.getTime());
                        HeartBeatsAllDayHistory history = new HeartBeatsAllDayHistory(null, time, heartRate);
                        // 已存在该时间数据
                        if (historyDao.queryBuilder().where(HeartBeatsAllDayHistoryDao.Properties.Time.eq(time)).count() != 0) {
                            continue;
                        }
                        // 未到的时间，数据不插入数据库
                        if (time.compareTo(DateUtil.getCurrentTime()) > 0)
                            continue;
                        // 插入数据库
                        historyDao.insert(history);

                    }
                }

                if (minValue == Integer.MAX_VALUE)
                    minValue = 0;

                HeartBeatsAllDayMaxMinDao maxMinDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHeartBeatsAllDayMaxMinDao();
                HeartBeatsAllDayMaxMin buf = maxMinDao.queryBuilder().where(HeartBeatsAllDayMaxMinDao.Properties.Date.eq(date)).build().unique();
                HeartBeatsAllDayMaxMin maxMin = new HeartBeatsAllDayMaxMin(null, date, minValue, maxValue, false);
                if (buf == null)
                    maxMinDao.insert(maxMin);
                else if (buf.getMin_heartbeats() != minValue || buf.getMax_heartbeats() != maxValue) {
                    maxMin.setId(buf.getId());
                    maxMinDao.update(maxMin);
                }

                // 黑色手环，上传心率数据
                uploadHeartData();
            }

            @Override
            public void OnFailed() {

            }

            @Override
            public void OnFinished() {

            }
        });

        if (Looper.myLooper() != Looper.getMainLooper()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**************************************************************************/
}
