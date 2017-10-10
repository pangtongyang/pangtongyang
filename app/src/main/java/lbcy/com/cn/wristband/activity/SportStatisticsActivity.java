package lbcy.com.cn.wristband.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
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
    @BindView(R.id.walk_minutes)
    TextView walkMinutes;
    @BindView(R.id.run_minutes)
    TextView runMinutes;
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
        spUtil = new SPUtil(mActivity, Consts.SETTING_DB_NAME);
        token = spUtil.getString("token", "");
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.fragment_me_statics));

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

    private void setStepText(int step){
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
