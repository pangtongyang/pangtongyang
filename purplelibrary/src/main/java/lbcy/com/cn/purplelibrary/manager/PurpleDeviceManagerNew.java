package lbcy.com.cn.purplelibrary.manager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.milink.air.ble.MilPreference;
import com.milink.air.ble.OnBleHrNotification;
import com.milink.air.ble.OnBleNotification;
import com.milink.air.ble.Raw;
import com.milink.air.ble.Sleep;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lbcy.com.cn.purplelibrary.app.MyApplication;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.ctl.DataListener;
import lbcy.com.cn.purplelibrary.ctl.DeviceControllerNew;
import lbcy.com.cn.purplelibrary.entity.SportData;
import lbcy.com.cn.purplelibrary.entity.SportDataDao;
import lbcy.com.cn.purplelibrary.utils.SPUtil;

/**
 * Created by chenjie on 2017/11/3.
 */

public class PurpleDeviceManagerNew implements DeviceControllerNew {
    public static volatile DeviceControllerNew manager = null;
    private Context mContext;
    private SPUtil spUtil;

    private PurpleDeviceManagerNew() {
        mContext = MyApplication.getInstances();
        spUtil = new SPUtil(mContext, CommonConfiguration.SHAREDPREFERENCES_NAME);
        MyApplication.getInstances().getThread().setNotifyBt(ooo);
    }

    public static DeviceControllerNew getInstance() {
        if (manager == null) {
            synchronized (PurpleDeviceManagerNew.class) {
                if (manager == null) {
                    manager = new PurpleDeviceManagerNew();
                }
            }
        }
        return manager;
    }

    private OnBleHrNotification ooo = new OnBleHrNotification() {
        byte a1;// 闹钟1开关 1 -> 开， 2 -> 关
        byte a2;// 闹钟2开关 1 -> 开， 2 -> 关
        byte a1H;// 闹钟1小时
        byte a1M;// 闹钟1分钟
        byte a2H;// 闹钟2小时
        byte a2M;// 闹钟2分钟
        String a1week;// 闹钟1重复时间
        String a2week;// 闹钟2重复时间
        /**
         * @param step 步数
         * @param distance 距离
         * @param cal 卡路里
         * @param bat  电量
         */
        @Override
        public void onGetCurSp(long step, long distance, long cal, long bat) {
            // 回调电量
            if (powerListener != null){
                powerListener.getData(bat);
            }
            // 回调运动数据
            if (sportListener != null){
                sportListener.getData(String.valueOf(step));
            }
        }

        @Override
        public void onGetHisSp(String s, long l, long l1, long l2) {
            SportDataDao dataDao = MyApplication.getInstances().getDaoSession().getSportDataDao();
            SportData sportData = new SportData(null, s, (int)l, false);
            if (dataDao.queryBuilder().where(SportDataDao.Properties.Date.eq(s)).count() > 0){
                SportData buf = dataDao.queryBuilder().where(SportDataDao.Properties.Date.eq(s)).build().unique();
                sportData.setId(buf.getId());
                dataDao.update(sportData);
            } else {
                dataDao.insert(sportData);
            }
        }

        @Override
        public void onGetRawSp(Raw[] raws) {

        }

        @Override
        public void onSetTime() {

        }

        @Override
        public void onSettingsChanged(int i) {

        }

        @Override
        public void onGetSettings(HashMap hashMap, MilPreference milPreference) {
            for (Map.Entry<String, Object> param : (Iterable<Map.Entry<String, Object>>) hashMap.entrySet()) {
                switch (param.getKey()) {
                    case "pressVibrate":

                        break;
                    case "steplen":
                        break;
                    case "deviceId":
                        break;
                    case "languge":
                        break;
                    case "AlarmsSettings":
                        byte[] alarm = (byte[]) param.getValue();
                        a1 = alarm[0];
                        a1H = alarm[1];
                        a1M = alarm[2];
                        a2 = alarm[3];
                        a2H = alarm[4];
                        a2M = alarm[5];
                        spUtil.putString("p_a1", String.valueOf(a1));
                        spUtil.putString("p_a1H", String.valueOf(a1H));
                        spUtil.putString("p_a1M", String.valueOf(a1M));
                        spUtil.putString("p_a2", String.valueOf(a2));
                        spUtil.putString("p_a2H", String.valueOf(a2H));
                        spUtil.putString("p_a2M", String.valueOf(a2M));
                        break;
                    case "Vibrate":
                        break;
                    case "oneAlarmWeek":
                        a1week = param.getValue().toString();
                        spUtil.putString("p_a1week", a1week);
                        break;
                    case "liftwrist":
                        break;
                    case "hexVer":
                        spUtil.putString("purple_version", (String) param.getValue());
                        break;
                    case "deviceName":
                        break;
                    case "twoAlarmWeek":
                        a2week = param.getValue().toString();
                        spUtil.putString("p_a2week", a2week);
                        break;
                }
            }
        }

        @Override
        public void onGetCallphone() {

        }

        @Override
        public void onDeviceoff() {

        }

        @Override
        public void onGetSleepData(Sleep arg0) {
            boolean isSleepValid = arg0.isSleepValid;//该段睡眠是否
            byte sleepHour = arg0.sleepHour;//入睡时间
            byte sleepMin = arg0.sleepMin;//入睡分钟
            byte wakeHour = arg0.wakeHour;//醒来小时
            byte wakeMin = arg0.wakeMin;//醒来分钟
            byte wakeCount = arg0.wakeCount;//醒来次娄
            int deepTime = arg0.deepTime;//深睡眠时间
            int lightTime = arg0.lightTime;//浅睡眠时间
            int sleepScore = arg0.sleepScore;//睡眠评分
            byte[] sleepShowRaw = arg0.sleepShowRaw;//睡眠绘制原数据
            int sleepShowRawi = arg0.sleepShowRawi;//绘制结束点

            if (sleepListener != null){
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSleepValid", isSleepValid);
                bundle.putByte("sleepHour", sleepHour);
                bundle.putByte("sleepMin", sleepMin);
                bundle.putByte("wakeHour", wakeHour);
                bundle.putByte("wakeMin", wakeMin);
                bundle.putByte("wakeCount", wakeCount);
                bundle.putInt("deepTime", deepTime);
                bundle.putInt("lightTime", lightTime);
                bundle.putInt("sleepScore", sleepScore);
                bundle.putByteArray("sleepShowRaw", sleepShowRaw);
                bundle.putInt("sleepShowRawi", sleepShowRawi);
                sleepListener.getData(bundle);
            }
        }

        @Override
        public void onGetAdvSp(long l, long l1, long l2) {

        }

        @Override
        public void onStepLenSet() {

        }

        @Override
        public void onAirDisplaySet() {

        }

        @Override
        public void onAlarmSet() {

        }

        @Override
        public void onVibrateSet() {

        }

        @Override
        public void onPressVibrateSet() {

        }

        @Override
        public void onCrcError(byte b) {

        }

        @Override
        public void onSpDataFinished() {

        }

        @Override
        public void onAntiLostSet() {

        }
    };

    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void syncTime() {
        MyApplication.getInstances().getThread().setTime(0,0,0);
    }

    private DataListener<Long> powerListener;

    @Override
    public void getBattery(DataListener<Long> dataListener) {
        powerListener = dataListener;
        MyApplication.getInstances().getThread().getSpData();
    }

    @Override
    public void setDisturb(boolean disturb) {
        MyApplication.getInstances().getThread().SendVibrateSet(disturb);
    }

    @Override
    public void setHandUp(boolean isHandUp) {
        // SendDisplay第一个参数0 -> 英文， 1 -> 中文
        MyApplication.getInstances().getThread().SendDisplaySet(1, isHandUp ? 1 : 0);
    }

    @Override
    public void setVibrate(boolean isVibrate) {
        MyApplication.getInstances().getThread().SendViPressSet(isVibrate);
    }

    @Override
    public void setAlarm(int id, int hour, int minute, String week, boolean state) {
        int a1 = Integer.valueOf(spUtil.getString("p_a1", "0"));
        int a1H = Integer.valueOf(spUtil.getString("p_a1H", "10"));
        int a1M = Integer.valueOf(spUtil.getString("p_a1M", "0"));
        int a2 = Integer.valueOf(spUtil.getString("p_a2", "0"));
        int a2H = Integer.valueOf(spUtil.getString("p_a2H", "10"));
        int a2M = Integer.valueOf(spUtil.getString("p_a2M", "0"));
        String a1week = spUtil.getString("p_a1week", "0-0-0-0-0-0-0");
        String a2week = spUtil.getString("p_a2week", "0-0-0-0-0-0-0");
        if (id == 0){
            MyApplication.getInstances().getThread().setAlarm(new int[]{state?1:0, hour, minute, a2, a2H, a2M}, week, a2week);
            spUtil.putString("p_a1", state?"1":"0");
            spUtil.putString("p_a1H", hour+"");
            spUtil.putString("p_a1M", minute+"");
            spUtil.putString("p_a1week", week);
        }
        else{
            MyApplication.getInstances().getThread().setAlarm(new int[]{a1, a1H, a1M, state?1:0, hour, minute}, a1week, week);
            spUtil.putString("p_a2", state?"1":"0");
            spUtil.putString("p_a2H", hour+"");
            spUtil.putString("p_a2M", minute+"");
            spUtil.putString("p_a2week", week);
        }
    }

    @Override
    public void readDeviceConfig() {
        MyApplication.getInstances().getThread().readDeviceConfig();
    }

    @Override
    public void sendNotification(String title, String content) {
        MyApplication.getInstances().getThread().AppNotification(title, content);
    }

    private DataListener<String> sportListener;
    @Override
    public void getSportData(DataListener<String> listener) {
        sportListener = listener;
        MyApplication.getInstances().getThread().getSpData();
    }

    @Override
    public void getSportHistory(DataListener<List<SportData>> listener) {
        SportDataDao dataDao = MyApplication.getInstances().getDaoSession().getSportDataDao();
        if (dataDao.queryBuilder().where(SportDataDao.Properties.IsUsed.eq(false)).count() == 0){
            return;
        }
        List<SportData> sportDataList = dataDao.queryBuilder().where(SportDataDao.Properties.IsUsed.eq(false)).build().list();
        listener.getData(sportDataList);
        // 更新数据库，将已使用的数据置为已使用状态
        for (SportData data : sportDataList){
            data.setIsUsed(true);
        }
        dataDao.updateInTx(sportDataList);
    }

    private DataListener<Bundle> sleepListener;
    @Override
    public void getSleepData(DataListener<Bundle> listener) {
        sleepListener = listener;
        MyApplication.getInstances().getThread().GetSleep();
    }

}
