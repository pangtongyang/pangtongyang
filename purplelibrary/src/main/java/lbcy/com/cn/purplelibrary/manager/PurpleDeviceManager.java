package lbcy.com.cn.purplelibrary.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lbcy.com.cn.purplelibrary.app.MyApplication;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.ctl.DeviceController;
import lbcy.com.cn.purplelibrary.entity.AlarmClockInfo;
import lbcy.com.cn.purplelibrary.entity.AlarmClockInfoDao;
import lbcy.com.cn.purplelibrary.entity.SleepInfo;
import lbcy.com.cn.purplelibrary.entity.SleepInfoDao;
import lbcy.com.cn.purplelibrary.entity.SportInfo;
import lbcy.com.cn.purplelibrary.entity.SportInfoDao;
import lbcy.com.cn.purplelibrary.utils.SPUtil;

/**
 * Created by chenjie on 2017/8/31.
 */

public class PurpleDeviceManager implements DeviceController {
    private static DeviceController manager;
    private static Context mContext;
    private InternalReceiver internalReceiver;
    private SPUtil spUtil;

    //普通数据监听
    DataListener dataListener;
    //运动数据监听
    DataListener sportsDataListener;
    //升级设备监听
    DataListener uploadDataListener;


    public PurpleDeviceManager() {
        if (mContext == null) {
            throw new NullPointerException("have not init");
        }
        spUtil = new SPUtil(mContext, CommonConfiguration.SHAREDPREFERENCES_NAME);
        registerReceiver(new String[]{
                CommonConfiguration.RESULT_ISLINK_DEVICE_NOTIFICATION,
                CommonConfiguration.RESULT_BLE_SPORTSDATA_NOTIFICATION,
                CommonConfiguration.RESULT_UPDATE_DEVICE_NOTIFICATION
        });
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static DeviceController getInstance() {
        if (manager == null) {
            synchronized (PurpleDeviceManager.class) {
                if (manager == null) {
                    manager = new PurpleDeviceManager();
                }
            }
        }
        return manager;
    }

    private void registerReceiver(String[] actionArray) {
        if (actionArray == null) {
            return;
        }
        IntentFilter intentfilter = new IntentFilter();
        for (String action : actionArray) {
            intentfilter.addAction(action);
        }
        if (internalReceiver == null) {
            internalReceiver = new InternalReceiver();
        }
        try {

            mContext.registerReceiver(internalReceiver, intentfilter);
        } catch (Exception e) {

        }

    }

    private class InternalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            handleReceiver(context, intent);
        }

    }

    private void handleReceiver(Context context, Intent intent) {

        if (intent.getAction().equals(CommonConfiguration.RESULT_ISLINK_DEVICE_NOTIFICATION)){
            boolean isLink = intent.getBooleanExtra("isLink",false);
            String deviceName = spUtil.getString("deviceName");
            String deviceAddress = spUtil.getString("deviceAddress");
            if (deviceName != null && !"".equals(deviceName) && !"null".equals(deviceName)) {
                if (dataListener != null){
                    if (isLink){
                        dataListener.getData("设备已经连接");
                    } else {
                        dataListener.getData("设备尚未连接");
                    }
                }
            } else {
                if (dataListener != null){
                    dataListener.getData("设备尚未连接");
                }
            }
        } else if (intent.getAction().equals(CommonConfiguration.RESULT_BLE_SPORTSDATA_NOTIFICATION)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SportInfoDao sportInfoDao = MyApplication.getInstances().getDaoSession().getSportInfoDao();
            List<SportInfo> list = sportInfoDao.queryBuilder()
                    .where(SportInfoDao.Properties.DateTime.eq(sdf.format(new Date())))
                    .build().list();
            if (!list.isEmpty()) {
                SportInfo sportInfo = list.get(0);
                if (sportsDataListener != null){
                    sportsDataListener.getData(sportInfo);
                    sportsDataListener = null;
                }
            }
        } else if(intent.getAction().equals(CommonConfiguration.RESULT_UPDATE_DEVICE_NOTIFICATION)){
            int progress = intent.getIntExtra("progress", 0);
            int code = intent.getIntExtra("code", 5);
            Map<String, String> map = new HashMap<>();
            map.put("progress", String.valueOf(progress));
            map.put("code", String.valueOf(code));
            if (uploadDataListener != null){
                uploadDataListener.getData(map);
                uploadDataListener = null;
            }
        }
    }

    @Override
    public void setClock(AlarmClockInfo clock) {
        AlarmClockInfoDao alarmClockInfoDao = MyApplication.getInstances().getDaoSession().getAlarmClockInfoDao();
        alarmClockInfoDao.insertOrReplace(clock);
        //闹钟提醒
        Intent intent = new Intent();
        intent.setAction(CommonConfiguration.ALARM_CLOCK_NOTIFICATION);
        mContext.sendBroadcast(intent);
    }

    @Override
    public void updateDevice(String filePath) {
        Intent intent = new Intent();
        intent.setAction(CommonConfiguration.TARGET_UPDATEDEVICE_NOTIFICATION);
        intent.putExtra("uploadFilePath", filePath);
        mContext.sendBroadcast(intent);
    }

    @Override
    public void isLinked(DataListener dataListener) {
        Intent intent = new Intent();
        intent.setAction(CommonConfiguration.ISLINK_DEVICE_NOTIFICATION);
        mContext.sendBroadcast(intent);
        this.dataListener = dataListener;
    }

    @Override
    public void getSportsData(DataListener dataListener) {
        Intent intent = new Intent();
        intent.setAction(CommonConfiguration.GET_BLE_SPORTSDATA_NOTIFICATION);
        mContext.sendBroadcast(intent);

        this.sportsDataListener = dataListener;
    }

    @Override
    public void getDaySleepData(String dateTime, DataListener dataListener) {
        SleepInfoDao sleepInfoDao = MyApplication.getInstances().getDaoSession().getSleepInfoDao();
        List<SleepInfo> sleepInfos = sleepInfoDao.queryBuilder().where(SleepInfoDao.Properties.DateTime.eq(dateTime)).build().list();
        dataListener.getData(sleepInfos);
    }

    @Override
    public void updateDevice(String uploadFilePath, DataListener dataListener) {
        Intent intent = new Intent();
        intent.setAction(CommonConfiguration.TARGET_UPDATEDEVICE_NOTIFICATION);
        intent.putExtra("uploadFilePath", uploadFilePath);
        mContext.sendBroadcast(intent);

        this.uploadDataListener = dataListener;
    }

    @Override
    public void getBattery(DataListener dataListener) {
        //通知获取运动数据
        Intent intent = new Intent();
        intent.setAction(CommonConfiguration.GET_BLE_SPORTSDATA_NOTIFICATION);
        mContext.sendBroadcast(intent);
        this.sportsDataListener = dataListener;
    }


    public interface DataListener{
        void getData(Object data);
    }
}
