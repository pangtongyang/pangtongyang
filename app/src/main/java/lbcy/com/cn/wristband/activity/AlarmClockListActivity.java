package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.entity.ClockBean;
import lbcy.com.cn.wristband.global.Consts;
import rx.functions.Action1;

public class AlarmClockListActivity extends BaseActivity {

    SPUtil spUtil;
    @BindView(R.id.tv_time_1)
    TextView tvTime1;
    @BindView(R.id.tv_repeat_1)
    TextView tvRepeat1;
    @BindView(R.id.switch_1)
    SwitchCompat switch1;
    @BindView(R.id.rl_clock_1)
    RelativeLayout rlClock1;
    @BindView(R.id.tv_time_2)
    TextView tvTime2;
    @BindView(R.id.tv_repeat_2)
    TextView tvRepeat2;
    @BindView(R.id.switch_2)
    SwitchCompat switch2;
    @BindView(R.id.rl_clock_2)
    RelativeLayout rlClock2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_clock_list;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(BaseApplication.getBaseApplication(), Consts.CLOCK_DK_NAME);
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_device_clock));

    }

    @Override
    protected void loadData() {
        TextView []tvRepeats = new TextView[2];
        tvRepeats[0] = tvRepeat1;
        tvRepeats[1] = tvRepeat2;
        TextView []tvTimes = new TextView[2];
        tvTimes[0] = tvTime1;
        tvTimes[1] = tvTime2;
        SwitchCompat []switchs = new SwitchCompat[2];
        switchs[0] = switch1;
        switchs[1] = switch2;
        for (int i=0;i<2;i++){
            String hour = spUtil.getString((i+1)+"hour");
            String minute = spUtil.getString((i+1)+"minute");
            if (hour != null && minute != null){
                if (hour.length() == 1)
                    hour = 0 + hour;
                if (minute.length() == 1)
                    minute = 0 + minute;
                tvTimes[i].setText(hour+":"+minute);
            }
            String text = spUtil.getString((i+1)+"weekText","只响一次");
            tvRepeats[i].setText(text);
            boolean switchState = spUtil.getString((i+1)+"switch","0").equals("1");
            switchs[i].setChecked(switchState);
        }

        mRxManager.on(Consts.CLOCK_LISTENER, new Action1<Message>() {
            @Override
            public void call(Message message) {
                switch (message.what){
                    case Consts.UPDATE_ALL_CLOCK_DATA:
                        ClockBean clockBean = (ClockBean) message.obj;
                        int pos = message.arg1 - 1;
                            tvTimes[pos].setText(clockBean.getHour()+":"+clockBean.getMinute());
                            tvRepeats[pos].setText(clockBean.getText());
                            switchs[pos].setChecked(clockBean.isSwitchState());
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        spUtil.putString(1+"switch", switch1.isChecked()? "1":"0");
        spUtil.putString(2+"switch", switch2.isChecked()? "1":"0");
    }

    @OnClick({R.id.rl_clock_1, R.id.rl_clock_2})
    public void onClick(View v) {
        Intent intent = new Intent(mActivity, SetAlarmClockActivity.class);
        switch (v.getId()) {
            case R.id.rl_clock_1:
                intent.putExtra("TAG", "1");
                startActivity(intent);
                break;
            case R.id.rl_clock_2:
                intent.putExtra("TAG", "2");
                startActivity(intent);
                break;
        }
    }

}
