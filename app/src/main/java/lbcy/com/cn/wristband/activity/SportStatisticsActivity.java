package lbcy.com.cn.wristband.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;

/**
 * Created by chenjie on 2017/9/7.
 */

public class SportStatisticsActivity extends BaseActivity {

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
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sport_statistics;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.fragment_me_statics));
        appCompatSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                char[] progress_str = String.valueOf(i).toCharArray();
                if (i >= 0 && i < 10) {
                    textStep5.setText(String.valueOf(progress_str[0]));
                    textStep4.setText(String.valueOf(0));
                    textStep3.setText(String.valueOf(0));
                    textStep2.setText(String.valueOf(0));
                    textStep1.setText(String.valueOf(0));
                } else if (i >= 10 && i < 100) {
                    textStep5.setText(String.valueOf(progress_str[1]));
                    textStep4.setText(String.valueOf(progress_str[0]));
                    textStep3.setText(String.valueOf(0));
                    textStep2.setText(String.valueOf(0));
                    textStep1.setText(String.valueOf(0));
                } else if (i >= 100 && i < 1000) {
                    textStep5.setText(String.valueOf(progress_str[2]));
                    textStep4.setText(String.valueOf(progress_str[1]));
                    textStep3.setText(String.valueOf(progress_str[0]));
                    textStep2.setText(String.valueOf(0));
                    textStep1.setText(String.valueOf(0));
                } else if (i >= 1000 && i < 10000) {
                    textStep5.setText(String.valueOf(progress_str[3]));
                    textStep4.setText(String.valueOf(progress_str[2]));
                    textStep3.setText(String.valueOf(progress_str[1]));
                    textStep2.setText(String.valueOf(progress_str[0]));
                    textStep1.setText(String.valueOf(0));
                } else if (i >= 10000 && i < 100000) {
                    textStep5.setText(String.valueOf(progress_str[4]));
                    textStep4.setText(String.valueOf(progress_str[3]));
                    textStep3.setText(String.valueOf(progress_str[2]));
                    textStep2.setText(String.valueOf(progress_str[1]));
                    textStep1.setText(String.valueOf(progress_str[0]));
                } else if (i >= 100000) {
                    textStep5.setText(String.valueOf(9));
                    textStep4.setText(String.valueOf(9));
                    textStep3.setText(String.valueOf(9));
                    textStep2.setText(String.valueOf(9));
                    textStep1.setText(String.valueOf(9));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void loadData() {

    }

}
