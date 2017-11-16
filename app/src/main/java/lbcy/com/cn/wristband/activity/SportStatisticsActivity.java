package lbcy.com.cn.wristband.activity;

import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.entity.SportStatisticsBean;
import lbcy.com.cn.wristband.entity.SportStatisticsData;
import lbcy.com.cn.wristband.entity.SportStatisticsDataDao;
import lbcy.com.cn.wristband.entity.MessageBean;
import lbcy.com.cn.wristband.entity.SportStepsTo;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.manager.NetManager;
import lbcy.com.cn.wristband.utils.HandlerTip;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chenjie on 2017/9/7.
 */

public class SportStatisticsActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.text_avg_step)
    TextView textAvgStep;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.text_avg_calories)
    TextView textAvgCalories;
    @BindView(R.id.text_max_step_week)
    TextView textMaxStepWeek;
    @BindView(R.id.text_max_step_day)
    TextView textMaxStepDay;
    @BindView(R.id.text_count_days)
    TextView textCountDays;
    @BindView(R.id.text_standards_days)
    TextView textStandardsDays;
    @BindView(R.id.text_step_1)
    TextView textStep1;
    @BindView(R.id.text_step_2)
    TextView textStep2;
    @BindView(R.id.text_step_3)
    TextView textStep3;
    @BindView(R.id.text_step_4)
    TextView textStep4;
    @BindView(R.id.text_step_5)
    TextView textStep5;
    @BindView(R.id.ll_step)
    LinearLayout llStep;
    @BindView(R.id.lable_hobby)
    TextView lableHobby;
    @BindView(R.id.lable_amateur)
    TextView lableAmateur;
    @BindView(R.id.lable_specialty)
    TextView lableSpecialty;
    @BindView(R.id.appCompatSeekBar)
    AppCompatSeekBar appCompatSeekBar;
    @BindView(R.id.tv_walk)
    TextView tvWalk;
    @BindView(R.id.tv_run)
    TextView tvRun;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    SPUtil spUtil;
    String token;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sport_statistics;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
        token = spUtil.getString("token", "");

    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.fragment_me_statics));
        tvWalk.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvRun.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        appCompatSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //步数实时显示
                setStepText(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        refreshLayout.setColorSchemeResources(R.color.blue);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNetwork();
                HandlerTip.getInstance().postDelayed(2000, new HandlerTip.HandlerCallback() {
                    @Override
                    public void postDelayed() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    protected void loadData() {
        getDataFromDisk();
        getDataFromNetwork();
    }

    @OnClick({R.id.btn_submit})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_submit:
                //注意接口回调中保存数据，可以toast保存成功或保存失败
                setStepsAction(appCompatSeekBar.getProgress());
                break;
        }
    }

    private void setStepsAction(int step){
        SportStepsTo sportStepsTo = new SportStepsTo();
        sportStepsTo.setSteps(step);
        NetManager.stepsSetAction(token, sportStepsTo, new NetManager.NetCallBack<MessageBean>() {
            @Override
            public void onResponse(Call<MessageBean> call, Response<MessageBean> response) {
                MessageBean data = response.body();
                if ((data != null ? data.getCode() : 0) == 200){
                    Toast.makeText(mActivity, "运动目标设置成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, data != null ? data.getMessage() : null, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageBean> call, Throwable t) {

            }
        });
    }

    private void getDataFromDisk(){
        SportStatisticsDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getSportStatisticsDataDao();
        if (dataDao.count() != 0){
            setText(dataDao.loadAll().get(0));
        }
    }

    private void getDataFromNetwork(){
        NetManager.getSportHistoryAction(token, new NetManager.NetCallBack<SportStatisticsBean>() {
            @Override
            public void onResponse(Call<SportStatisticsBean> call, Response<SportStatisticsBean> response) {
                SportStatisticsBean data = response.body();
                if ((data != null ? data.getCode() : 0) == 200){
//                    SportStatisticsData mData = new SportStatisticsData(null, data.getData().getAverage_steps(),
//                            data.getData().getAverage_calorie(), data.getData().getOptimal_week(), data.getData().getOptimal_date(),
//                            data.getData().getTotal_days(), data.getData().getQualified_days(), data.getData().getGoal_steps());
                    spUtil.putString("goal_steps", String.valueOf(data.getData().getGoal_steps()));
                    setText(data.getData());
                    saveData(data.getData());
                } else {
                    Toast.makeText(mActivity, data != null ? data.getMessage().toString() : "数据获取失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SportStatisticsBean> call, Throwable t) {

            }
        });
    }

    // 页面写入数据
    private void setText(SportStatisticsData data){
        textAvgStep.setText(String.valueOf(data.getAverage_steps()));
        textAvgCalories.setText(String.valueOf(data.getAverage_calorie()));
        textMaxStepWeek.setText(data.getOptimal_week());
        textMaxStepDay.setText(data.getOptimal_date());
        textCountDays.setText(String.valueOf(data.getTotal_days()));
        textStandardsDays.setText(String.valueOf(data.getQualified_days()));
        setStepText(data.getGoal_steps());
        appCompatSeekBar.setProgress(data.getGoal_steps());
    }

    private void saveData(SportStatisticsData data){
        SportStatisticsDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getSportStatisticsDataDao();
        if (dataDao.count() == 0){
            dataDao.insert(data);
        } else {
            data.setId(dataDao.loadAll().get(0).getId());
            dataDao.update(data);
        }
    }

    private void setSportColor(int type){
        if (type == 1){
            lableHobby.setTextColor(ContextCompat.getColor(mActivity, R.color.coloraccent));
            lableAmateur.setTextColor(ContextCompat.getColor(mActivity, R.color.text_dark_gray));
            lableSpecialty.setTextColor(ContextCompat.getColor(mActivity, R.color.text_dark_gray));
        } else if(type == 2){
            lableHobby.setTextColor(ContextCompat.getColor(mActivity, R.color.text_dark_gray));
            lableAmateur.setTextColor(ContextCompat.getColor(mActivity, R.color.coloraccent));
            lableSpecialty.setTextColor(ContextCompat.getColor(mActivity, R.color.text_dark_gray));
        } else {
            lableHobby.setTextColor(ContextCompat.getColor(mActivity, R.color.text_dark_gray));
            lableAmateur.setTextColor(ContextCompat.getColor(mActivity, R.color.text_dark_gray));
            lableSpecialty.setTextColor(ContextCompat.getColor(mActivity, R.color.coloraccent));
        }
    }

    private void setStepText(int step){
        int walkMinute = step / 1000 * 11;
        int runMinute = (int)(step / 1000 * 7.5);
        int walkHour = walkMinute / 60;
        int runHour = runMinute / 60;
        walkMinute = walkMinute % 60;
        runMinute = runMinute % 60;
        String walk = walkHour == 0? "步行 "+walkMinute + "分" : "步行 "+walkHour+"时"+walkMinute + "分";
        String run = runHour == 0? "跑步 "+runMinute + "分" : "跑步 "+runHour+"时"+runMinute + "分";
        tvWalk.setText(walk);
        tvRun.setText(run);

        if (step <= 30000){
            setSportColor(1);
        } else if (step <= 60000){
            setSportColor(2);
        } else {
            setSportColor(3);
        }


        char[] progress_str = String.valueOf(step).toCharArray();
        if (step >= 0 && step < 10) {
            textStep5.setText(String.valueOf(progress_str[0]));
            textStep4.setText(String.valueOf(0));
            textStep3.setText(String.valueOf(0));
            textStep2.setText(String.valueOf(0));
            textStep1.setText(String.valueOf(0));
        } else if (step >= 10 && step < 100) {
            textStep5.setText(String.valueOf(progress_str[1]));
            textStep4.setText(String.valueOf(progress_str[0]));
            textStep3.setText(String.valueOf(0));
            textStep2.setText(String.valueOf(0));
            textStep1.setText(String.valueOf(0));
        } else if (step >= 100 && step < 1000) {
            textStep5.setText(String.valueOf(progress_str[2]));
            textStep4.setText(String.valueOf(progress_str[1]));
            textStep3.setText(String.valueOf(progress_str[0]));
            textStep2.setText(String.valueOf(0));
            textStep1.setText(String.valueOf(0));
        } else if (step >= 1000 && step < 10000) {
            textStep5.setText(String.valueOf(progress_str[3]));
            textStep4.setText(String.valueOf(progress_str[2]));
            textStep3.setText(String.valueOf(progress_str[1]));
            textStep2.setText(String.valueOf(progress_str[0]));
            textStep1.setText(String.valueOf(0));
        } else if (step >= 10000 && step < 100000) {
            textStep5.setText(String.valueOf(progress_str[4]));
            textStep4.setText(String.valueOf(progress_str[3]));
            textStep3.setText(String.valueOf(progress_str[2]));
            textStep2.setText(String.valueOf(progress_str[1]));
            textStep1.setText(String.valueOf(progress_str[0]));
        } else if (step >= 100000) {
            textStep5.setText(String.valueOf(9));
            textStep4.setText(String.valueOf(9));
            textStep3.setText(String.valueOf(9));
            textStep2.setText(String.valueOf(9));
            textStep1.setText(String.valueOf(9));
        }
    }
}
