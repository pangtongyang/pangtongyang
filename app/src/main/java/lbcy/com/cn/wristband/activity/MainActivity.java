package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseFragmentActivity;
import lbcy.com.cn.wristband.fragment.WebFragment;

/**
 * Created by chenjie on 2017/9/5.
 */

public class MainActivity extends BaseFragmentActivity {

    private final int TAB_HEALTH = 0;
    private final int TAB_KAOQIN = 1;
    private final int TAB_EXPERT = 2;
    private final int TAB_STAR = 3;

    WebFragment healthFragment, kaoqinFragment, expertFragment, starFragment;
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
    ViewPager vpContent;
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        healthFragment = new WebFragment("10000000", R.color.colorPrimary);
        kaoqinFragment = new WebFragment("20000000", R.color.blue);
        expertFragment = new WebFragment("30000000", R.color.white);
        starFragment = new WebFragment("40000000", R.color.gray_text);
        adapter = new FragmentAdapter(getSupportFragmentManager());
        vpContent.setAdapter(adapter);
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
            rlTop2.setVisibility(View.VISIBLE);
            rlTop3.setVisibility(View.VISIBLE);
            ivHistory.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
            tvTop1.setText(R.string.sport);
            tvTop2.setText(R.string.heart_rate);
            tvTop3.setText(R.string.sleep);
            setSelected(tvBottom1);
            vpContent.setCurrentItem(TAB_HEALTH, false);
        }
    }

    private void jumpKaoqinFragment() {
        if (!tvBottom2.isSelected()) {
            rlTop2.setVisibility(View.VISIBLE);
            rlTop3.setVisibility(View.VISIBLE);
            ivHistory.setVisibility(View.INVISIBLE);
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
            tvTop1.setText(R.string.today);
            tvTop2.setText(R.string.week);
            tvTop3.setText(R.string.month);
            setSelected(tvBottom2);
            vpContent.setCurrentItem(TAB_KAOQIN, false);
        }
    }

    private void jumpExpertFragment() {
        if (!tvBottom3.isSelected()) {
            rlTop2.setVisibility(View.VISIBLE);
            rlTop3.setVisibility(View.GONE);
            ivHistory.setVisibility(View.INVISIBLE);
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
            tvTop1.setText(R.string.sport);
            tvTop2.setText(R.string.eat);
            setSelected(tvBottom3);
            vpContent.setCurrentItem(TAB_EXPERT, false);
        }
    }

    private void jumpStarFragment() {
        if (!tvBottom4.isSelected()) {
            rlTop2.setVisibility(View.GONE);
            rlTop3.setVisibility(View.GONE);
            ivHistory.setVisibility(View.INVISIBLE);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
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

}