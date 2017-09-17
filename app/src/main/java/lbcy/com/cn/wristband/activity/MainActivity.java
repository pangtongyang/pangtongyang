package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseWebFragment;
import lbcy.com.cn.wristband.app.BaseFragmentActivity;
import lbcy.com.cn.wristband.fragment.WebFragment;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.utils.AnimationUtil;
import lbcy.com.cn.wristband.widget.NoScrollViewPager;

/**
 * Created by chenjie on 2017/9/5.
 */

public class MainActivity extends BaseFragmentActivity {

    private final int TAB_HEALTH = 0;
    private final int TAB_KAOQIN = 1;
    private final int TAB_EXPERT = 2;
    private final int TAB_STAR = 3;

    BaseWebFragment healthFragment, kaoqinFragment, expertFragment, starFragment;
    BaseWebFragment[] webFragments = new WebFragment[4];
    FragmentAdapter adapter;

    int prePage = 0;
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


    int mainPage1 = 0, mainPage2 = 0, mainPage3 = 0;
    long mExitTime;
    SPUtil spUtil;
    Bundle mBundle;

    @Override
    protected void onResume() {
        super.onResume();
        llHomeBottomBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        if (!isSplashed){
            startActivity(new Intent(mActivity, SplashActivity.class));
            //判断是否已登录
            spUtil = new SPUtil(mActivity, Consts.USER_DB_NAME);
            String isLogin = spUtil.getString("is_login", "0");
            if (isLogin.equals("0")) {
                finish();
                return;
            }
        }

        //webview初始化
        webFragments[0] = healthFragment = WebFragment.getInstance(mBundle = new Bundle());
        mBundle.putString(Consts.URL_KEY, Consts.WEB_INDEX);
        webFragments[1] = kaoqinFragment = WebFragment.getInstance(mBundle = new Bundle());
        mBundle.putString(Consts.URL_KEY, Consts.WEB_CLASS_TODAY);
        webFragments[2] = expertFragment = WebFragment.getInstance(mBundle = new Bundle());
        mBundle.putString(Consts.URL_KEY, Consts.WEB_EXPERT);
        webFragments[3] = starFragment = WebFragment.getInstance(mBundle = new Bundle());
        mBundle.putString(Consts.URL_KEY, Consts.WEB_STAR);

        vpContent.setNoScroll(true);

        for (int i = 0; i < 4; i++) {
            int finalI = i;
            webFragments[i].setOnBackClickListener(new BaseWebFragment.OnBackClick() {
                @Override
                public void click() {
                    webFragments[finalI].isBackPressed = true;
                    if (!webFragments[finalI].getMyAgentWeb().back()) {
                        webFragments[finalI].isBackPressed = false;
                    }
                }
            });

            webFragments[i].setOnWebItemClickListener(new BaseWebFragment.OnWebItemClick() {
                @Override
                public void click(boolean isBackPressed, List<String> webHistoryUrls) {
//                    if (isBackPressed) {
//                        if (webHistoryUrls.size() == 1) {
//                            showMainTopBar();
//                            vpContent.setNoScroll(false);
//                        }
//                    } else {
//                        if (webHistoryUrls.size() == 2) {
//                            showNormalTopBar();
//                            vpContent.setNoScroll(true);
//                        }
//                    }
                }
            });
        }

        adapter = new FragmentAdapter(getSupportFragmentManager());
        vpContent.setAdapter(adapter);
        vpContent.setOffscreenPageLimit(4);
        jumpHealthFragment();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //一定要保证 mAentWebFragemnt 回调
        webFragments[prePage].onActivityResult(requestCode, resultCode, data);
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
                intent = new Intent(mActivity, MeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @OnClick({R.id.rl_top1, R.id.rl_top2, R.id.rl_top3, R.id.iv_history})
    public void topClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_top1:
                if (view1.getVisibility() == View.GONE) {
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                    switch (prePage) {
                        case 0:
                            mainPage1 = 0;
                            healthFragment.clearHistoryUrls();
                            healthFragment.loadUrl(Consts.WEB_INDEX);
                            break;
                        case 1:
                            mainPage2 = 0;
                            kaoqinFragment.clearHistoryUrls();
                            kaoqinFragment.loadUrl(Consts.WEB_CLASS_TODAY);
                            break;
                        case 2:
                            mainPage3 = 0;
                            expertFragment.clearHistoryUrls();
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
                            healthFragment.clearHistoryUrls();
                            healthFragment.loadUrl(Consts.WEB_HEART_RATE_INDEX);
                            break;
                        case 1:
                            mainPage2 = 1;
                            kaoqinFragment.clearHistoryUrls();
                            kaoqinFragment.loadUrl(Consts.WEB_CLASS_WEEK);
                            break;
                        case 2:
                            mainPage3 = 1;
                            expertFragment.clearHistoryUrls();
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
                            healthFragment.clearHistoryUrls();
                            healthFragment.loadUrl(Consts.WEB_SLEEP_INDEX);
                            break;
                        case 1:
                            mainPage2 = 2;
                            kaoqinFragment.clearHistoryUrls();
                            kaoqinFragment.loadUrl(Consts.WEB_CLASS_MONTH);
                            break;
                    }
                }
                break;
            case R.id.iv_history:
                startActivity(new Intent(mActivity, WebActivity.class).putExtra("url", Consts.WEB_HISTORY));
                break;
        }
    }

    public void showNormalTopBar() {
        AnimationUtil.showAndHiddenAnimation(webFragments[prePage].getTopBar(), AnimationUtil.AnimationState.STATE_SHOW, 300);
        AnimationUtil.showAndHiddenAnimation(rlHomeTopBar, AnimationUtil.AnimationState.STATE_HIDDEN, 300);
        AnimationUtil.showAndHiddenAnimation(llHomeBottomBar, AnimationUtil.AnimationState.STATE_HIDDEN, 300);
    }

    public void showMainTopBar() {
        AnimationUtil.showAndHiddenAnimation(rlHomeTopBar, AnimationUtil.AnimationState.STATE_SHOW, 300);
        AnimationUtil.showAndHiddenAnimation(llHomeBottomBar, AnimationUtil.AnimationState.STATE_SHOW, 300);
        AnimationUtil.showAndHiddenAnimation(webFragments[prePage].getTopBar(), AnimationUtil.AnimationState.STATE_HIDDEN, 300);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BaseWebFragment webFragment = webFragments[prePage];
            if (webFragment != null) {
                webFragment.isBackPressed = true;
                if (!webFragment.getMyAgentWeb().back()) {
                    webFragment.isBackPressed = false;
                    if ((System.currentTimeMillis() - mExitTime) > 2000) {
                        Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                        mExitTime = System.currentTimeMillis();

                    } else {
                        finish();
                    }
                }
                return true;
            }
        }
        return false;
    }
}
