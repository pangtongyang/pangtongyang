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
import lbcy.com.cn.wristband.entity.ClockBeanDao;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.popup.RepeatSettingPopup;
import lbcy.com.cn.wristband.popup.TypeSettingPopup;
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
    @BindView(R.id.timepicker)
    TimePickView timepicker;
    @BindView(R.id.tv_type_left)
    TextView tvTypeLeft;
    @BindView(R.id.tv_type_right)
    TextView tvTypeRight;
    @BindView(R.id.iv_type_righticon)
    ImageView ivTypeRighticon;
    @BindView(R.id.switch_type_righticon)
    SwitchCompat switchTypeRighticon;
    @BindView(R.id.rl_type)
    RelativeLayout rlType;

    BasePopupWindow popupWindow;
    String ClockTAG;
    ClockBean mClockBean;
    ClockBeanDao clockBeanDao;

    boolean isLateClock;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_alarm_clock;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        ClockTAG = intent.getStringExtra("TAG");
        isLateClock = intent.getBooleanExtra("is_late_clock", false);

        clockBeanDao = BaseApplication.getBaseApplication().getBaseDaoSession().getClockBeanDao();

        //初始化闹钟数据对象
        if (ClockTAG.equals("-1")){
            mClockBean = new ClockBean();
        } else {
            if (isLateClock){
                mClockBean = clockBeanDao.queryBuilder()
                        .where(ClockBeanDao.Properties.Type.eq("迟到提醒"),
                                ClockBeanDao.Properties.Position.eq(ClockTAG)).build().unique();
            } else {
                mClockBean = clockBeanDao.queryBuilder()
                        .where(ClockBeanDao.Properties.Type.notEq("迟到提醒"),
                                ClockBeanDao.Properties.Position.eq(ClockTAG)).build().unique();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void initView() {
        if (ClockTAG.equals("-1")){
            setTitle(getResources().getString(R.string.activity_add_clock));
        } else {
            setTitle(getResources().getString(R.string.activity_set_clock) + (Integer.valueOf(ClockTAG) + 1));
        }
        setRightIcon(R.mipmap.checkmark);

        //页面数据初始化
        Calendar c = Calendar.getInstance();
        int hour, minute, position;
        String text, type;
        if (ClockTAG.equals("-1")) {
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            text = "只响一次";
            if (isLateClock){
                type = "迟到提醒";
                position = (int) clockBeanDao.queryBuilder().where(ClockBeanDao.Properties.Type.eq("迟到提醒")).count();
            } else {
                type = "运动";
                position = (int) clockBeanDao.queryBuilder().where(ClockBeanDao.Properties.Type.notEq("迟到提醒")).count();
            }


            //更新数据对象
            mClockBean.setText(text);
            mClockBean.setType(type);
            mClockBean.setHour(String.valueOf(hour).length() == 1 ? (0 + String.valueOf(hour)) : String.valueOf(hour));
            mClockBean.setMinute(String.valueOf(minute).length() == 1 ? (0 + String.valueOf(minute)) : String.valueOf(minute));
            mClockBean.setSwitchState(false);
            mClockBean.setPosition(position);

        } else {
            hour = Integer.valueOf(mClockBean.getHour());
            minute = Integer.valueOf(mClockBean.getMinute());
            text = mClockBean.getText();
            type = mClockBean.getType();
        }
        timepicker.setTime(hour, minute);

        tvRighttext.setText(text);
        tvTypeRight.setText(type);



    }

    @Override
    protected void loadData() {
        rightClick(new OnRightClickListener() {
            @Override
            public void click() {
                int[] time = timepicker.getSelectTime();
                mClockBean.setHour(String.valueOf(time[0]).length() == 1 ? (0 + String.valueOf(time[0])) : String.valueOf(time[0]));
                mClockBean.setMinute(String.valueOf(time[1]).length() == 1 ? (0 + String.valueOf(time[1])) : String.valueOf(time[1]));
                mClockBean.setSwitchState(true);
                mClockBean.setText(tvRighttext.getText().toString());
                mClockBean.setType(tvTypeRight.getText().toString());



                Message message = new Message();
                message.what = Consts.UPDATE_ALL_CLOCK_DATA;
                message.obj = mClockBean;
                RxBus.getInstance().post(Consts.CLOCK_LISTENER, message);
                finish();
            }
        });

        mRxManager.on(Consts.CLOCK_LISTENER, new Action1<Message>() {
            @Override
            public void call(Message message) {
                switch (message.what) {
                    case Consts.UPDATE_CLOCK_DATA:
                        tvRighttext.setText(message.obj.toString());
                        break;
                    case Consts.UPDATE_CLOCK_TYPE_DATA:
                        tvTypeRight.setText(message.obj.toString());
                        break;
                }
            }
        });
    }

    @OnClick({R.id.rl_repeat, R.id.rl_type})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_repeat:
                popupWindow = getPopupRepeatSetting();
                popupWindow.showPopupWindow();
                break;
            case R.id.rl_type:
                if (!isLateClock){
                    popupWindow = getPopupTypeSetting();
                    popupWindow.showPopupWindow();
                }
                break;
        }

    }

    BasePopupWindow getPopupRepeatSetting() {
        return new RepeatSettingPopup(this, mClockBean);
    }
    BasePopupWindow getPopupTypeSetting() {
        return new TypeSettingPopup(this);
    }

}
