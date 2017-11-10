package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.os.Message;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huichenghe.bleControl.Ble.BluetoothLeService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lbcy.com.cn.blacklibrary.manager.BlackDeviceManager;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.manager.PurpleDeviceManagerNew;
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
import lbcy.com.cn.wristband.utils.DateUtil;
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

    SPUtil spUtil;
    String which_device = "2";
    BasePopupWindow popupWindow;
    String ClockTAG;
    ClockBean mClockBean;
    ClockBeanDao clockBeanDao;

    boolean isLateClock;

    int number = 0; //闹钟编号

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_alarm_clock;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
        which_device = spUtil.getString("which_device","2");

        Intent intent = getIntent();
        ClockTAG = intent.getStringExtra("TAG");
        isLateClock = intent.getBooleanExtra("is_late_clock", false);

        clockBeanDao = BaseApplication.getBaseApplication().getBaseDaoSession().getClockBeanDao();

        //初始化闹钟数据对象
        if (ClockTAG.equals("-1")){
            mClockBean = new ClockBean();
            List<ClockBean> clockBeanList = clockBeanDao
                    .queryBuilder()
                    .orderAsc(ClockBeanDao.Properties.Number)
                    .build().list();
            number = 0;
            for (ClockBean clockBean: clockBeanList){
                if (clockBean.getNumber() == number)
                    number++;
                else
                    break;
            }
            mClockBean.setNumber(number);
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

            number = mClockBean.getNumber();
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
                // 处理只响一次闹钟开关自动关闭问题
                if (tvRighttext.getText().toString().equals("只响一次")){
                    String bookTimeStr;
                    if ((mClockBean.getHour()+":"+mClockBean.getMinute()).compareTo(DateUtil.getCurrentTime_H_m()) > 0){
                        bookTimeStr = DateUtil.getCurrentTime_Y_M_d(false) + " " + mClockBean.getHour()+":"+mClockBean.getMinute();
                    } else{
                        bookTimeStr = DateUtil.getCurrentTime_Y_M_d(true) + " " + mClockBean.getHour()+":"+mClockBean.getMinute();
                    }
                    long bookTime = DateUtil.getBookMillis(bookTimeStr);
                    mClockBean.setBookTime(bookTime);
                }

                //发送更新数据请求
                Message message = new Message();
                message.what = Consts.UPDATE_ALL_CLOCK_DATA;
                message.obj = mClockBean;
                RxBus.getInstance().post(Consts.CLOCK_LISTENER, message);

                //调用sdk设置闹钟
                if (which_device.equals("2")){
                    if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()){
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    b_setClock();
                } else {
                    String is_connected = spUtil.getString("is_connected", "0");
                    if (is_connected.equals("0")){
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    p_setClock();
                }

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
        mClockBean.setText(tvRighttext.getText().toString());
        return new RepeatSettingPopup(this, mClockBean);
    }
    BasePopupWindow getPopupTypeSetting() {
        return new TypeSettingPopup(this);
    }

    /**************************************************************************/
    //紫色手环连接相关
    private void p_setClock(){
        String str = tvRighttext.getText().toString();
        int w1 = 0;
        int w2 = 0;
        int w3 = 0;
        int w4 = 0;
        int w5 = 0;
        int w6 = 0;
        int w7 = 0;
        String week;
        switch (str){
            case "只响一次":
                // 手环bug，不设置重复时间，闹钟不响
                w1=w2=w3=w4=w5=w6=w7=1;
                break;
            case "每天":
                w1=w2=w3=w4=w5=w6=w7=1;
                break;
            default:
                if (str.contains("周一"))
                    w1 = 1;
                if (str.contains("周二"))
                    w2 = 1;
                if (str.contains("周三"))
                    w3 = 1;
                if (str.contains("周四"))
                    w4 = 1;
                if (str.contains("周五"))
                    w5 = 1;
                if (str.contains("周六"))
                    w6 = 1;
                if (str.contains("周日"))
                    w7 = 1;
                break;
        }
        week = w6+"-"+w5+"-"+w4+"-"+w3+"-"+w2+"-"+w1+"-"+w7;

        PurpleDeviceManagerNew.getInstance().setAlarm(number,
                timepicker.getSelectTime()[0], timepicker.getSelectTime()[1], week, true);
    }

    /**************************************************************************/
    //黑色手环相关
    private void b_setClock(){
        List<String> repeat_days = new ArrayList<>();
        String str = tvRighttext.getText().toString();
        switch (str){
            case "只响一次":
                for (int i=0;i<7;i++){
                    repeat_days.add("无");
                }
                break;
            case "每天":
                for (int i=0;i<7;i++){
                    repeat_days.add("有");
                }
                break;
            default:
                repeat_days.add(str.contains("周一") ? "有" : "无");
                repeat_days.add(str.contains("周二") ? "有" : "无");
                repeat_days.add(str.contains("周三") ? "有" : "无");
                repeat_days.add(str.contains("周四") ? "有" : "无");
                repeat_days.add(str.contains("周五") ? "有" : "无");
                repeat_days.add(str.contains("周六") ? "有" : "无");
                repeat_days.add(str.contains("周日") ? "有" : "无");
                break;
        }
        BlackDeviceManager.getInstance().
                setClock(number,
                tvTypeRight.getText().toString(),
                timepicker.getSelectTime()[0],
                timepicker.getSelectTime()[1],
                repeat_days);
    }

    /**************************************************************************/
}
