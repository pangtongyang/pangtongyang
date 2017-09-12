package lbcy.com.cn.purplelibrary.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.List;

import lbcy.com.cn.purplelibrary.app.MyApplication;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.ctl.DeviceController;
import lbcy.com.cn.purplelibrary.entity.AlarmClockInfo;
import lbcy.com.cn.purplelibrary.entity.AlarmClockInfoDao;

/**
 * Created by chenjie on 2017/8/31.
 */

public class PurpleDeviceManager implements DeviceController {
    private static DeviceController manager;
    private static Context mContext;

    public PurpleDeviceManager() {
        if (mContext == null) {
            throw new NullPointerException("have not init");
        }
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
    public void getData() {

    }

    @Override
    public void updateDevice(String filePath) {
        Intent intent = new Intent();
        intent.setAction(CommonConfiguration.TARGET_UPDATEDEVICE_NOTIFICATION);
        intent.putExtra("uploadFilePath", filePath);
        mContext.sendBroadcast(intent);
    }

    @Override
    public void isLinked() {
        Intent intent = new Intent();
        intent.setAction(CommonConfiguration.ISLINK_DEVICE_NOTIFICATION);
        mContext.sendBroadcast(intent);
    }


}
