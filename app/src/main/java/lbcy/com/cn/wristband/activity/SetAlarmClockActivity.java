package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.entity.ClockBean;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.popup.ScalePopup;
import lbcy.com.cn.wristband.popup.SlideFromBottomPopup;
import lbcy.com.cn.wristband.rx.RxBus;
import lbcy.com.cn.wristband.widget.timepicker.TimePickView;
import razerdp.basepopup.BasePopupWindow;
import rx.functions.Action1;

public class SetAlarmClockActivity extends BaseActivity {

    @BindView(R.id.underline)
    View underline;
    @BindView(R.id.tv_lefttext)
    TextView tvLefttext;
    @BindView(R.id.tv_righttext)
    TextView tvRighttext;
    @BindView(R.id.iv_righticon)
    ImageView ivRighticon;
    @BindView(R.id.switch_righticon)
    SwitchCompat switchRighticon;
    @BindView(R.id.rl_repeat)
    RelativeLayout rlRepeat;

    SPUtil spUtil;
    @BindView(R.id.timepicker)
    TimePickView timepicker;

    BasePopupWindow popupWindow;
    String ClockTAG;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_alarm_clock;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        ClockTAG = intent.getStringExtra("TAG");
        spUtil = new SPUtil(BaseApplication.getBaseApplication(), Consts.CLOCK_DK_NAME);
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_set_clock) + ClockTAG);
        setRightIcon(R.mipmap.checkmark);
        rightClick(new OnRightClickListener() {
            @Override
            public void click() {
                int [] time = timepicker.getSelectTime();
                spUtil.putString(ClockTAG+"hour", String.valueOf(time[0]));
                spUtil.putString(ClockTAG+"minute", String.valueOf(time[1]));
                spUtil.putString(ClockTAG+"switch", "1");

                String hour = String.valueOf(time[0]).length() == 1? (0 + String.valueOf(time[0])) : String.valueOf(time[0]);
                String minute = String.valueOf(time[1]).length() == 1? (0 + String.valueOf(time[1])) : String.valueOf(time[1]);
                ClockBean clockbean = new ClockBean(hour,minute,spUtil.getString(ClockTAG+"weekText","只响一次"), true);
                Message message = new Message();
                message.what = Consts.UPDATE_ALL_CLOCK_DATA;
                message.obj = clockbean;
                message.arg1 = Integer.valueOf(ClockTAG);
                RxBus.getInstance().post(Consts.CLOCK_LISTENER, message);
                finish();
            }
        });
    }

    @Override
    protected void loadData() {
        //数据初始化
        Calendar c = Calendar.getInstance();
        int hour = Integer.parseInt(spUtil.getString(ClockTAG+"hour", String.valueOf(c.get(Calendar.HOUR_OF_DAY))));
        int minute = Integer.parseInt(spUtil.getString(ClockTAG+"minute", String.valueOf(c.get(Calendar.MINUTE))));
        timepicker.setTime(hour, minute);
        String text = spUtil.getString(ClockTAG+"weekText","只响一次");
        tvRighttext.setText(text);

        mRxManager.on(Consts.CLOCK_LISTENER, new Action1<Message>() {
            @Override
            public void call(Message message) {
                switch (message.what) {
                    case Consts.UPDATE_CLOCK_DATA:
                        tvRighttext.setText(message.obj.toString());
                        break;
                }
            }
        });
    }

    @OnClick({R.id.rl_repeat})
    public void onCLick(View v){
        popupWindow = getPopup();
        popupWindow.showPopupWindow();
    }

    BasePopupWindow getPopup(){
        return new ScalePopup(this, ClockTAG);
    }
}
