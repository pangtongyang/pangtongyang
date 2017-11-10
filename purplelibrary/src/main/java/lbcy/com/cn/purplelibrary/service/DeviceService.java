package lbcy.com.cn.purplelibrary.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;

import com.milink.air.ble.HeartRate;
import com.milink.air.ble.MilPreference;
import com.milink.air.ble.OnBleHrNotification;
import com.milink.air.ble.Raw;
import com.milink.air.ble.Sleep;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lbcy.com.cn.purplelibrary.app.MyApplication;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.entity.AlarmClockInfo;
import lbcy.com.cn.purplelibrary.entity.AlarmClockInfoDao;
import lbcy.com.cn.purplelibrary.entity.HeartRateInfo;
import lbcy.com.cn.purplelibrary.entity.HeartRateInfoDao;
import lbcy.com.cn.purplelibrary.entity.NotFazeTime;
import lbcy.com.cn.purplelibrary.entity.NotFazeTimeDao;
import lbcy.com.cn.purplelibrary.entity.SleepInfo;
import lbcy.com.cn.purplelibrary.entity.SleepInfoDao;
import lbcy.com.cn.purplelibrary.entity.SportDetails;
import lbcy.com.cn.purplelibrary.entity.SportDetailsDao;
import lbcy.com.cn.purplelibrary.entity.SportInfo;
import lbcy.com.cn.purplelibrary.entity.SportInfoDao;
import lbcy.com.cn.purplelibrary.entity.SportTarget;
import lbcy.com.cn.purplelibrary.entity.SportTargetDao;
import lbcy.com.cn.purplelibrary.entity.TargetRemind;
import lbcy.com.cn.purplelibrary.entity.TargetRemindDao;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.purplelibrary.utils.WeekTool;

/**
 * 设备服务
 */
public class DeviceService extends Service {
    private String TAG = this.getClass().getSimpleName();
    private SynchroDataThread synchroDataThread = new SynchroDataThread();
    private TargetRemindThread targetRemindThread = new TargetRemindThread();
//    private HttpSynchroDataThread httpSynchroDataThread = new HttpSynchroDataThread();

    public static String deviceName = "";
    public static String deviceAddress = "";

    private SleepInfoDao sleepInfoDao;
    private SportDetailsDao sportDetailsDao;
    private SportInfoDao sportInfoDao;
    private AlarmClockInfoDao alarmClockInfoDao;
    private TargetRemindDao targetRemindDao;
    private SportTargetDao sportTargetDao;
    private NotFazeTimeDao notFazeTimeDao;
    private HeartRateInfoDao heartRateDao;

    private AirBLEService bleService = null;

    private InternalReceiver internalReceiver;
    //连接服务
    private Intent service = null;

    private SPUtil sharedPreferencesDao;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        sleepInfoDao = MyApplication.getInstances().getDaoSession().getSleepInfoDao();
        sportDetailsDao = MyApplication.getInstances().getDaoSession().getSportDetailsDao();
        sportInfoDao = MyApplication.getInstances().getDaoSession().getSportInfoDao();
        alarmClockInfoDao = MyApplication.getInstances().getDaoSession().getAlarmClockInfoDao();
        targetRemindDao = MyApplication.getInstances().getDaoSession().getTargetRemindDao();
        sportTargetDao = MyApplication.getInstances().getDaoSession().getSportTargetDao();
        notFazeTimeDao = MyApplication.getInstances().getDaoSession().getNotFazeTimeDao();
        heartRateDao = MyApplication.getInstances().getDaoSession().getHeartRateInfoDao();
        sharedPreferencesDao = new SPUtil(getApplicationContext(), CommonConfiguration.SHAREDPREFERENCES_NAME);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!synchroDataThread.isAlive()) {
            synchroDataThread.start();
        }
//        if (!targetRemindThread.isAlive()) {
//            targetRemindThread.start();
//        }
//        if(!httpSynchroDataThread.isAlive()){
//            httpSynchroDataThread.start();
//        }

        try {
            //连接设备,获取运动量数据
            registerReceiver(new String[]{
                    CommonConfiguration.CREATE_CONNECT_DEVICE_NOTIFICATION,
                    CommonConfiguration.CONNECT_DEVICE_NOTIFICATION,
                    CommonConfiguration.DIS_CONNECT_DEVICE_NOTIFICATION,
                    CommonConfiguration.GET_BLE_SPORTSDATA_NOTIFICATION,
                    CommonConfiguration.PUSH_MESSAGE_NOTIFICATION,
                    CommonConfiguration.ALARM_CLOCK_NOTIFICATION,
                    CommonConfiguration.TARGET_REMIND_NOTIFICATION,
                    CommonConfiguration.TARGET_UPDATEDEVICE_NOTIFICATION,
                    CommonConfiguration.ISLINK_DEVICE_NOTIFICATION
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        deviceAddress = sharedPreferencesDao.getString("deviceAddress");
        deviceName = sharedPreferencesDao.getString("deviceName");
        if (deviceName != null && !"".equals(deviceName) && !"null".equals(deviceName)) {
//        if (sharedPreferencesDao.getString("deviceName") != null && !"".equals(sharedPreferencesDao.getString("deviceName")) && !"null".equals(sharedPreferencesDao.getString("deviceName"))) {
//            deviceAddress = sharedPreferencesDao.getString("deviceAddress");
//            deviceName = sharedPreferencesDao.getString("deviceName");

            intent = new Intent();
            if (isWorked()) {
                //设置Intent的action属性
                intent.setAction(CommonConfiguration.CONNECT_DEVICE_NOTIFICATION);
            } else {
                intent.setAction(CommonConfiguration.CREATE_CONNECT_DEVICE_NOTIFICATION);
            }
            intent.putExtra("deviceAddress", deviceAddress);
            intent.putExtra("deviceName", deviceName);
            //发出广播
            sendBroadcast(intent);

        }


        return START_STICKY;
    }


    public void onDestroy() {
        // TODO Auto-generated method stubo
        super.onDestroy();
        try {
            if(mServiceConnection!=null){
                unbindService(mServiceConnection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (btReceiver != null) {
                unregisterReceiver(btReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Intent getIntent() {
        return new Intent("com.wisgen.health.service.DeviceService");
    }

    //自动同步数据
    class SynchroDataThread extends Thread {

        public void run() {
            while (true) {
                try {
                    //默认五分钟同步一次:
                    long syncTime = 5 * 60 * 1000;
//                    通知获取运动数据
                    Intent intent = new Intent();
                    intent.setAction(CommonConfiguration.GET_BLE_SPORTSDATA_NOTIFICATION);
                    sendBroadcast(intent);

                    if (sharedPreferencesDao.getString("syncTime") != null && !"null".equals(sharedPreferencesDao.getString("syncTime")) && !"".equals(sharedPreferencesDao.getString("syncTime"))) {
                        try {
                            //获取到多久时间同步一次：
                            syncTime = Long.parseLong(sharedPreferencesDao.getString("syncTime")) * 60 * 1000;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Thread.sleep(syncTime);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    //目标进程通知
    class TargetRemindThread extends Thread {

        public void run() {
            while (true) {
                try {
                    //目标进程通知
                    Intent intent = new Intent();
                    intent.setAction(CommonConfiguration.TARGET_REMIND_NOTIFICATION);
                    sendBroadcast(intent);

                    Thread.sleep(1 * 60 * 1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private void createLink(String deviceAddress, String deviceName) {
        try {
            if (isWorked() && bleService != null) {
                bleService.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        if (isWorked() && bleService != null && bleService.connect(deviceAddress)) {
        if (isWorked() && bleService != null && bleService.initialize()) {
            try {
            bleService.connect(deviceAddress);
////                Intent intent = new Intent();
////                intent.setAction(CommonConfiguration.RESULT_CONNECT_DEVICE_NOTIFICATION);
////                intent.putExtra("deviceAddress", deviceAddress);
////                intent.putExtra("deviceName", deviceName);
////                sendBroadcast(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            IntentFilter inf = new IntentFilter();
            inf.addAction("mil.bt");
            inf.addAction(ACTION);
            registerReceiver(btReceiver, inf);
            Intent service = new Intent(this, AirBLEService.class);
            service.putExtra("address", deviceAddress);
            service.putExtra("deviceName", deviceName);
            bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);
        }


    }

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    BroadcastReceiver btReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // 收到使能成功之后的广播 可以通信了
            if (intent.getAction().equals("mil.bt")) {
                int cmd = intent.getIntExtra("cmd", 0);
                if (cmd == 1) {
                    if (bleService != null) {

                        bleService.setNotifyBt(ooo);

                        Log.i("TAG","开始读取设备版本信息!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                        bleService.readDeviceConfig();
//                        deviceAddress=intent.getStringExtra("deviceAddress");
//                        deviceName=intent.getStringExtra("deviceName");

                        sharedPreferencesDao.putString("deviceName", deviceName);
                        sharedPreferencesDao.putString("deviceAddress", deviceAddress);

                        //发送连接设备通知
                        //设置Intent的action属性
                        intent.setAction(CommonConfiguration.RESULT_CONNECT_DEVICE_NOTIFICATION);
                        intent.putExtra("deviceAddress", deviceAddress);
                        intent.putExtra("deviceName", deviceName);
                        //发出广播
                        sendBroadcast(intent);

//                        Log.v("sunping", "RESULT_CONNECT_DEVICE_NOTIFICATION");


                        Intent intent1 = new Intent();
                        intent1.setAction(CommonConfiguration.ALARM_CLOCK_NOTIFICATION);
                        sendBroadcast(intent1);

                        //获取数据
                        new Thread() {

                            public void run() {
                                try {
                                    /*try {
                                        Thread.sleep(3000l);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }*/
                                    Intent intent2 = new Intent();
                                    intent2.setAction(CommonConfiguration.GET_BLE_SPORTSDATA_NOTIFICATION);
                                    sendBroadcast(intent2);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }.start();


                    }
                } else if (cmd == 2) {
//                    Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent();
                    intent2.putExtra("deviceName", intent.getStringExtra("deviceName"));
                    intent2.setAction(CommonConfiguration.RESULT_FAIL_CONNECT_DEVICE_NOTIFICATION);
                    sendBroadcast(intent2);

                }
            }
            if (intent.getAction().equals(ACTION)) {

                {
                    {
                        bleService.sendMsg();
                        {

                            Bundle bundle = intent.getExtras();
                            Object messages[] = (Object[]) bundle.get("pdus");
                            String allMessage = "";
                            String title = "";
                            if (messages != null && messages.length > 0) {
                                SmsMessage smsMessage[] = new SmsMessage[messages.length];
                                for (int n = 0; n < smsMessage.length; n++) {
                                    smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
                                }

                                for (SmsMessage message : smsMessage) {

                                    if (title.equals("")) {
                                        title = message.getOriginatingAddress();// 得到发件人号码
                                        allMessage = message.getMessageBody();
                                    } else if (title.equals(message.getOriginatingAddress())) {
                                        allMessage += message.getMessageBody();// 得到短信内容
                                    }
                                }
                                ///
//                                handler.obtainMessage(0, title + ":" + allMessage).sendToTarget();
                            }
                        }

                        // airVersion
                    }
                }
            }
        }
    };

    protected final void registerReceiver(String[] actionArray) {
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
            registerReceiver(internalReceiver, intentfilter);
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

    public boolean canGetData=true;//是否可以去获取数据的标识；
    Handler hd=new Handler();
    Runnable run=new Runnable() {
        @Override
        public void run() {
            canGetData=true;
            Log.i("TAG","runnable++++++++++++++++++++++++++++++++++++++++++");
        }
    };


    private  void getData(){
        if(!canGetData){
            return;
        }
        canGetData=false;

        //十五秒后将标识置为true;
        hd.postDelayed(run,15000l);


        Log.i("TAG","开始获取数据。。。。。。。。。。。。。。。。。。。。。。。。。");
        bleService.SendVibrateSet(true);
        bleService.setTime(0, 0, 0);
//                bleService.GetSleep();
//                bleService.getSpData();
        //获取数据
        new Thread() {
            public void run() {
                try {
                    try {
                        //Thread.sleep(5000l);
                        bleService.GetSleep();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    try {
//                        Thread.sleep(5000l);
//                        bleService.GetSleep();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        Thread.sleep(5000l);
//                        bleService.getSpData();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    protected void handleReceiver(Context context, Intent intent) {

        if (intent.getAction().equals(CommonConfiguration.CREATE_CONNECT_DEVICE_NOTIFICATION)) {
            deviceAddress = intent.getStringExtra("deviceAddress");
            deviceName = intent.getStringExtra("deviceName");
            createLink(deviceAddress, deviceName);
        } else if (intent.getAction().equals(CommonConfiguration.GET_BLE_SPORTSDATA_NOTIFICATION)) {
            if (isWorked() && bleService != null && bleService.initialize()) {
                getData();
            } else {
                createLink(deviceAddress, deviceName);
//                getData();

            }
        } else if (intent.getAction().equals(CommonConfiguration.DIS_CONNECT_DEVICE_NOTIFICATION)) {
            if (isWorked() && bleService != null && bleService.initialize()) {
                bleService.disconnect();
            }
        } else if (intent.getAction().equals(CommonConfiguration.CONNECT_DEVICE_NOTIFICATION)) {
            deviceAddress = intent.getStringExtra("deviceAddress");
            deviceName = intent.getStringExtra("deviceName");
            if (isWorked() && bleService != null && bleService.initialize()) {
                bleService.connect(deviceAddress);
//                intent.setAction(CommonConfiguration.RESULT_CONNECT_DEVICE_NOTIFICATION);
//                intent.putExtra("deviceAddress", deviceAddress);
//                intent.putExtra("deviceName", deviceName);
//                //发出广播
//                sendBroadcast(intent);
            } else {

                //重新连接
                createLink(deviceAddress, deviceName);
//                try{
//                    bleService.connect(deviceAddress);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
            }

        } else if (intent.getAction().equals(CommonConfiguration.PUSH_MESSAGE_NOTIFICATION)) {

            if (isWorked() && bleService != null && bleService.initialize()) {
                if (!isNotFaze()) {
                    String appName = intent.getStringExtra("appName");
                    String title = intent.getStringExtra("title");
                    bleService.AppNotification(appName, title);
                }


            } else {
                createLink(deviceAddress, deviceName);
                try {
                    if (!isNotFaze()) {
                        String appName = intent.getStringExtra("appName");
                        String title = intent.getStringExtra("title");
                        bleService.AppNotification(appName, title);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (intent.getAction().equals(CommonConfiguration.ALARM_CLOCK_NOTIFICATION)) {
            if (isWorked() && bleService != null && bleService.initialize()) {
                List<AlarmClockInfo> list = alarmClockInfoDao.queryBuilder().orderAsc(AlarmClockInfoDao.Properties.Id).build().list();

                if (!list.isEmpty()) {
                    int state1 = 0;
                    int state2 = 0;
                    int hour1 = 0;
                    int hour2 = 0;
                    int minute1 = 0;
                    int minute2 = 0;
                    String week1 = "0-1-1-1-1-1-0";
                    String week2 = "0-1-1-1-1-1-0";
                    for (int i = 0; i < list.size(); i++) {

                        AlarmClockInfo alarmClockInfo = list.get(i);

                        if (i == 0) {
                            hour1 = alarmClockInfo.getHours() != null ? Integer.parseInt(alarmClockInfo.getHours()) : 0;
                        } else {
                            hour2 = alarmClockInfo.getHours() != null ? Integer.parseInt(alarmClockInfo.getHours()) : 0;
                        }
                        if (i == 0) {
                            minute1 = alarmClockInfo.getMinutes() != null ? Integer.parseInt(alarmClockInfo.getMinutes()) : 0;
                        } else {
                            minute2 = alarmClockInfo.getMinutes() != null ? Integer.parseInt(alarmClockInfo.getMinutes()) : 0;
                        }
                        String weeksTextStr = alarmClockInfo.getWeekDays();
                        if (i == 0) {
                            week1 = weeksTextStr;
                        } else {
                            week2 = weeksTextStr;
                        }
                        if (alarmClockInfo.getState() == 1) {
                            if (i == 0) {
                                state1 = 1;
                            } else {
                                state2 = 1;
                            }
                        } else {
                            if (i == 0) {
                                state1 = 0;
                            } else {
                                state2 = 0;
                            }
                        }

                    }
                    bleService.setAlarm(new int[]{state1, hour1, minute1, state2, hour2, minute2}, week1, week2);
                }
            } else {
                createLink(deviceAddress, deviceName);
            }
        } else if (intent.getAction().equals(CommonConfiguration.TARGET_REMIND_NOTIFICATION)) {
            if (isWorked() && bleService != null && bleService.initialize()) {

                if (!isNotFaze()) {
                    List<TargetRemind> targetRemindList = targetRemindDao.loadAll();
                    if (!targetRemindList.isEmpty()) {
                        TargetRemind targetRemind = targetRemindList.get(0);
                        long betweenTime = 0;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
                        try {
//                            Log.v("sunping","targetRemind:"+targetRemind.getRemindTime()+"");
                            betweenTime = simpleDateFormat.parse(sdf.format(new Date()) + " " + targetRemind.getRemindTime() + ":00").getTime() - simpleDateFormat.parse(simpleDateFormat.format(new Date())).getTime();
//                            Log.v("sunping","time:"+betweenTime+"");
                            if (betweenTime <= 1000 * 60 * 1 && betweenTime >= -1000 * 60 * 1) {
                                List<SportTarget> sportTargetList = sportTargetDao.loadAll();
                                int steps = 0;
                                if (!sportTargetList.isEmpty()) {
                                    SportTarget sportTarget = sportTargetList.get(0);
                                    int targetStep = sportTarget.getSteps();

                                    List<SportInfo> sportInfoList = sportInfoDao.queryBuilder().where(SportInfoDao.Properties.DateTime.eq(sdf.format(new Date()))).build().list();
                                    if (!sportInfoList.isEmpty()) {
                                        SportInfo sportInfo = sportInfoList.get(0);
                                        steps = targetStep - sportInfo.getSteps();

                                    }

                                }
                                String appName = "目标进程";
                                String title = "";
                                if (steps > 0) {
                                    title = "距离今天目标还有" + steps + "步";
                                } else {
                                    title = "您今天目标已经完成";
                                }
                                bleService.AppNotification(appName, title);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                createLink(deviceAddress, deviceName);
            }
            //处理升级请求：
        } else if (intent.getAction().equals(CommonConfiguration.TARGET_UPDATEDEVICE_NOTIFICATION)) {
            if (isWorked() && bleService != null && bleService.initialize()) {
                String uploadFilePath = intent.getStringExtra("uploadFilePath");
                bleService.startUpdate(uploadFilePath);
            } else {
                createLink(deviceAddress, deviceName);
            }
        } else if (intent.getAction().equals(CommonConfiguration.ISLINK_DEVICE_NOTIFICATION)) {
            Intent mIntent = new Intent();
            mIntent.setAction(CommonConfiguration.RESULT_ISLINK_DEVICE_NOTIFICATION);
            if (bleService != null && bleService.initialize()) {
                mIntent.putExtra("isLink", true);
            } else {
                mIntent.putExtra("isLink", false);
            }
            sendBroadcast(mIntent);
        }

    }

    private Boolean isNotFaze() {

        Boolean isRemind = false;

        List<NotFazeTime> notFazeTimeList = notFazeTimeDao.queryBuilder().where(NotFazeTimeDao.Properties.State.eq("1")).build().list();
        if (!notFazeTimeList.isEmpty()) {
            NotFazeTime notFazeTime = notFazeTimeList.get(0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String startTime = sdf.format(new Date()) + " " + notFazeTime.getStartTime();
            String endTime = sdf.format(new Date()) + " " + notFazeTime.getEndTime();
            String nowDate = simpleDateFormat.format(new Date());


            long betweenTime = 0;
            try {
                betweenTime = simpleDateFormat.parse(nowDate).getTime() - simpleDateFormat.parse(endTime).getTime();
                if (betweenTime <= 0) {
                    isRemind = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                betweenTime = simpleDateFormat.parse(nowDate).getTime() - simpleDateFormat.parse(startTime).getTime();
                if (betweenTime >= 0) {
                    isRemind = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return isRemind;
    }

    ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            if (service instanceof AirBLEService.LocalBinder) {
                bleService = ((AirBLEService.LocalBinder) service).getService();
            }
        }
    };

    OnBleHrNotification ooo = new OnBleHrNotification() {


        @Override
        public void onCrcError(byte arg0) {
            // TODO Auto-generated method stub
            Log.i("TAG","数据传输失败!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+arg0);
            canGetData=true;
            hd.removeCallbacks(run);
            /*Intent intent=new Intent();
            intent.setAction(CommonConfiguration.GET_BLE_SPORTSDATA_NOTIFICATION);
            sendBroadcast(intent);*/
        }

        @Override
        public void onGetAdvSp(long arg0, long arg1, long arg2) {
            // TODO Auto-generated method stub
            System.out.println(arg0);
        }

        //所有运动数据读取完毕时回调：在此方法中获得心率信息；
        @Override
        public void onSpDataFinished() {
            // TODO Auto-generated method stub
            Log.i("TAG","onSpDataFinished()运动数据读取结束回调 ++++++++++++++++++++++++++++++");
            canGetData=true;
            hd.removeCallbacks(run);
        }

        @Override
        public void onSettingsChanged(int arg0) {
            // TODO Auto-generated method stub
            System.out.println(arg0);
        }

        @Override
        public void onAirDisplaySet() {
            // TODO Auto-generated method stub
            System.out.println("显示");
        }

        @Override
        public void onAlarmSet() {
            // TODO Auto-generated method stub
            System.out.println("闹钟");
        }

        @Override
        public void onStepLenSet() {
            // TODO Auto-generated method stub
            System.out.println("步长");
        }

        @Override
        public void onVibrateSet() {
            // TODO Auto-generated method stub
            System.out.println("震动");
        }

        @Override
        public void onPressVibrateSet() {
            // TODO Auto-generated method stub
            System.out.println("按下时是否震动");
        }

        @Override
        public void onSetTime() {
            // TODO Auto-generated method stub

//            handler.obtainMessage(0, "设置时间成功").sendToTarget();
        }

        @Override
        public void onDeviceoff() {
            // TODO Auto-generated method stub
            System.out.println("设备关闭");
        }

        @Override
        public void onGetCallphone() {
            // TODO Auto-generated method stub

//            handler.obtainMessage(0, "手环呼叫手机了").sendToTarget();
            ;
        }

        @Override
        public void onGetSettings(HashMap arg0, MilPreference mi) {
            // TODO Auto-generated method stub
            Log.v("sunping", "获取配置");
            StringBuffer sb = new StringBuffer();
            Iterator<Map.Entry<String, Object>> iterator = arg0.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<String, Object> param = iterator.next();
                // jre 1.7
                switch (param.getKey()) {
                    case "pressVibrate":
                        sb.append("触摸振动：" + param.getValue() + (param.getValue().toString().equals("1") ? "开" : "关") + "\n");

                        break;
                    case "steplen":
                        sb.append("步长：" + (param.getValue()) + "\n");
                        break;
                    case "deviceId":
                        sb.append("设备号：" + param.getValue() + "\n");
                        break;
                    case "languge":
                        sb.append("当前语言：" + ((param.getValue().toString()).equals("1") ? "中文" : "英文") + "\n");
                        break;
                    case "AlarmsSettings":

                        break;
                    case "Vibrate":
                        sb.append(
                                "振动开关：" + param.getValue() + (param.getValue().toString().equals("1") ? "开" : "关") + "\n");
                        break;
                    case "oneAlarmWeek":
                        break;
                    case "liftwrist":
                        break;
                    case "hexVer":
                        sb.append("固件版本：" + (param.getValue()) + "\n");
                        if (param.getValue() != null && !"".equals(param.getValue())) {
                            String deviceVersion = param.getValue() + "";
                            deviceVersion = deviceVersion.substring(deviceVersion.lastIndexOf("v") + 1, deviceVersion.length());
                            sharedPreferencesDao.putString("deviceVersion", deviceVersion);

                            Intent intent=new Intent();
                            intent.setAction("ON_GET_DEVICEVERSION");
                            intent.putExtra("version",deviceVersion);
                            sendBroadcast(intent);

                            Log.i("TAG","读取到的固件版本为"+deviceVersion+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }
                        break;
                    case "deviceName":
                        sb.append("蓝牙名称：" + (param.getValue()) + "\n");
                        break;
                    case "twoAlarmWeek":
                        break;
                }
            }
        }

        /**
         * 获取心率
         * @param var1
         */
        public void onReadHeartRateDatas(ArrayList var1) {
            Log.i("TAG","获得心率信息成功！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
            Date dateTime = new Date();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String toDay = sdf.format(new Date());
//                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//                WifiInfo info = wifi.getConnectionInfo();
                if (var1.isEmpty()) {
                    return;
                }
                JSONArray jsonArrayListData = new JSONArray();
                for (int i = 0; i < var1.size(); i++) {
                    HeartRate heartRate = (HeartRate) var1.get(i);
                    String date = heartRate.date;
                    if (!date.equals(toDay)) {
                        Long count = heartRateDao.queryBuilder()
                                .where(HeartRateInfoDao.Properties.DateTime.eq(date), HeartRateInfoDao.Properties.State.eq('1'))
                                .count();

                        if (count > 0) {
                            continue;
                        }
                    }


                    byte[] hrValue = heartRate.hrValue;
                    for (int j = 0; j < hrValue.length; j++) {
                        if (hrValue[j] <= 0) {
                            continue;
                        }
                        String hour = "";
                        if (j < 10) {
                            hour = "0" + j;
                        } else {
                            hour = "" + j;
                        }
                        String startHourStr = date + " " + hour + ":00:00";
                        String endHourStr = date + " " + hour + ":59:59";
                        if (date.equals(toDay) && j == dateTime.getHours()) {
                            endHourStr = simpleDateFormat.format(dateTime);
                        }

                        Object[] data = new Object[]{"HEARTBEAT", startHourStr, endHourStr, hrValue[j]};
                        JSONArray jsonArrayData = new JSONArray();
                        jsonArrayData.put(data[0]);
                        jsonArrayData.put(data[1]);
                        jsonArrayData.put(data[2]);
                        jsonArrayData.put(data[3]);
                        jsonArrayListData.put(jsonArrayData);

                    }

                    Long count = heartRateDao.queryBuilder()
                            .where(HeartRateInfoDao.Properties.DateTime.eq(date))
                            .count();

                    String str = "";
                    StringBuilder sb = new StringBuilder(str);
                    for (byte element : hrValue) {
                        sb.append(String.valueOf(element) + ";");
                    }
                    str = sb.toString();

                    if (count > 0) {
                        HeartRateInfo heartRateInfo = heartRateDao.queryBuilder().build().unique();
                        heartRateInfo.setDateTime(date);
                        heartRateInfo.setContent(str);
                        heartRateInfo.setState("1");
                        heartRateDao.update(heartRateInfo);
                    } else {
                        HeartRateInfo heartRateInfo = new HeartRateInfo();
                        heartRateInfo.setDateTime(date);
                        heartRateInfo.setContent(str);
                        heartRateInfo.setState("1");
                        heartRateDao.insert(heartRateInfo);
                    }
                }


//                RequestParams params = new RequestParams();
//                params.put("Mac", sharedPreferencesDao.getString("deviceAddress"));
//                params.put("Data", jsonArrayListData.toString());
////              Log.v("sunping", jsonArrayListData.toString() + ",," + info.getMacAddress());
//
//                Message msg = handler.obtainMessage();
//                msg.obj = params;
//                handler.sendMessage(msg);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.setAction(CommonConfiguration.RESULT_BLE_SPORTSDATA_NOTIFICATION);
            intent.putExtra("deviceAddress", deviceAddress);
            sendBroadcast(intent);
        }

        @Override
        public void onGetSleepData(Sleep arg0) {
            bleService.getSpData();
            Log.i("TAG","获取睡眠信息成功！！！！！！！！！！！！！！！！！！！！！！！！！");
            // TODO Auto-generated method stub
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            SleepInfo del_sleepInfo = sleepInfoDao.queryBuilder()
                    .where(SleepInfoDao.Properties.DateTime.eq(sdf.format(new Date()))).build().unique();
            sleepInfoDao.deleteByKey(del_sleepInfo.getId());

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

            SleepInfo sleepInfo = new SleepInfo();
            if (isSleepValid) {
                sleepInfo.setIsSleepValid(1);
            } else {
                sleepInfo.setIsSleepValid(0);
            }
            sleepInfo.setSleepHour(Integer.parseInt(sleepHour + ""));
            sleepInfo.setSleepMin(Integer.parseInt(sleepMin + ""));
            sleepInfo.setWakeHour(Integer.parseInt(wakeHour + ""));
            sleepInfo.setWakeMin(Integer.parseInt(wakeMin + ""));
            sleepInfo.setWakeCount(Integer.parseInt(wakeCount + ""));
            sleepInfo.setDeepTime(deepTime);
            sleepInfo.setLightTime(lightTime);
            sleepInfo.setSleepScore(sleepScore);
            sleepInfo.setDateTime(sdf.format(new Date()));

            String sleepTime = String.format("%1$02d:%2$02d", new Object[]{sleepHour, sleepMin});
            String wakeTime = String.format("%1$02d:%2$02d", new Object[]{wakeHour, wakeMin});

            if (sleepHour > 12) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                sleepInfo.setStartTime(sdf.format(calendar.getTime()) + " " + sleepTime);
            } else {
                sleepInfo.setStartTime(sdf.format(new Date()) + " " + sleepTime);
            }

            if (wakeHour > 12) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                sleepInfo.setStartTime(sdf.format(calendar.getTime()) + " " + sleepTime);
            } else {
                sleepInfo.setEndTime(sdf.format(new Date()) + " " + wakeTime);
            }
            int drawCount = 0;
            String drawText = "";
            //0，1是深睡，2是醒着，3是浅睡
            if (sleepShowRawi != 0) {
                for (int i = 0; i < sleepShowRaw.length; i++) {
                    if (sleepShowRaw[i] == 0x00 || sleepShowRaw[i] == 0x01) {
                        drawCount++;
                        drawText += "1";//深睡
                    } else if (sleepShowRaw[i] == 0x02) {
                        drawCount++;
                        drawText += "2";//清醒
                    } else if (sleepShowRaw[i] == 0x03) {
                        drawCount++;
                        drawText += "3";//浅睡
                    }
                    if (i == sleepShowRawi) {
                        break;
                    }
                }
            }

            sleepInfo.setDrawCount(drawCount);
            sleepInfo.setDrawText(drawText);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sleepInfo.setCreateTime(simpleDateFormat.format(new Date()));
            sleepInfo.setDid(deviceAddress);
            sleepInfoDao.insert(sleepInfo);

            try {
                if (sleepInfo.getDeepTime() != 0 && sleepInfo.getLightTime() != 0) {
//                    WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//                    WifiInfo info = wifi.getConnectionInfo();
                    List listData = new ArrayList();
                    Object[] deepData = new Object[]{"DEEPSLEEP", sleepInfo.getStartTime() + ":00", sleepInfo.getEndTime() + ":00", sleepInfo.getDeepTime()};
                    listData.add(deepData);
                    Object[] lightData = new Object[]{"SLEEP", sleepInfo.getStartTime() + ":00", sleepInfo.getEndTime() + ":00", sleepInfo.getLightTime()};
                    listData.add(lightData);

                    JSONObject object = new JSONObject();
                    JSONArray jsonArrayListData = new JSONArray();
                    JSONArray deepJsonArrayData = new JSONArray();
                    deepJsonArrayData.put(deepData[0]);
                    deepJsonArrayData.put(deepData[1]);
                    deepJsonArrayData.put(deepData[2]);
                    deepJsonArrayData.put(deepData[3]);


                    jsonArrayListData.put(deepJsonArrayData);

                    JSONArray lightJsonArrayData = new JSONArray();
                    lightJsonArrayData.put(lightData[0]);
                    lightJsonArrayData.put(lightData[1]);
                    lightJsonArrayData.put(lightData[2]);
                    lightJsonArrayData.put(lightData[3]);

                    jsonArrayListData.put(lightJsonArrayData);

//                    final RequestParams params = new RequestParams();
//                    params.put("Mac", sharedPreferencesDao.getString("deviceAddress"));
//                    params.put("Data", jsonArrayListData.toString());
////                    Log.v("sunping", jsonArrayListData.toString() + ",sleep," + info.getMacAddress());
//
//                    Message msg = handler.obtainMessage();
//                    msg.obj = params;
//                    handler.sendMessage(msg);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent();
            intent.setAction(CommonConfiguration.RESULT_BLE_SPORTSDATA_NOTIFICATION);
            intent.putExtra("deviceAddress", deviceAddress);
            sendBroadcast(intent);
        }

        /**
         * @param arg0 步数
         * @param arg1 距离
         * @param arg2 卡路里
         * @param bat  电量
         */
        @Override
        public void onGetCurSp(long arg0, long arg1, long arg2, long bat) {
            // TODO Auto-generated method stub

            Log.i("TAG","获取当前步数成功！！！！！！！！！！！！！！！！！！！！！！！！！！！");
//            handler.obtainMessage(0, "当前步数：" + arg0 + "电量：" + bat).sendToTarget();

            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long between = 0;
            try {
//                Log.v("sunping",new Date().getTime()+"");
//                Log.v("sunping",sd.parse(simpleDateFormat.format(new Date())+" 23:58:00").getTime()+"");
                if ((new Date().getTime()) - (simpleDateFormat.parse(sd.format(new Date()) + " 23:58:00").getTime()) >= 0) {
                    Thread.sleep(1000 * 60 * 5);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //删除当前数据
            SportInfo del_sportInfo = sportInfoDao.queryBuilder()
                    .where(SportInfoDao.Properties.DateTime.eq(sd.format(new Date())))
                    .build().unique();
            sportInfoDao.deleteByKey(del_sportInfo.getId());

            SportInfo sportInfo = new SportInfo();
            String steps = arg0 + "";
            String distances = arg1 + "";
            String calories = arg2 + "";
            String battery = bat + "";
            if (steps != null && !"".equals(steps)) {
                sportInfo.setSteps(Integer.parseInt(steps));
            }
            if (distances != null && !"".equals(distances)) {
                sportInfo.setDistances(Integer.parseInt(distances));
            }
            if (calories != null && !"".equals(calories)) {
                sportInfo.setCalories(Integer.parseInt(calories));
            }
            if (battery != null && !"".equals(battery)) {
                sportInfo.setBattery(battery);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sportInfo.setCreateTime(sdf.format(new Date()));
            sportInfo.setDid(deviceAddress);
            sportInfo.setDateTime(sd.format(new Date()));
            sportInfoDao.insert(sportInfo);

            Intent intent = new Intent();
            intent.setAction(CommonConfiguration.RESULT_BLE_SPORTSDATA_NOTIFICATION);
            intent.putExtra("deviceAddress", deviceAddress);
            sendBroadcast(intent);
        }

        /**
         * @param arg0 数据产生日期
         * @param arg1
         * @param arg2
         * @param arg3
         */
        @Override
        public void onGetHisSp(String arg0, long arg1, long arg2, long arg3) {
            // TODO Auto-generated method stub
//            handler.obtainMessage(0, arg0 + "步数：" + arg1).sendToTarget();
//            sportInfoDao.execSql("delete from t_sport_info where date_time=?", new String[]{arg0});
//            Log.v("sunping", "onGetHisSp");
            SportInfo sportInfo = new SportInfo();

            String date = arg0;
            String steps = arg1 + "";
            String distances = arg2 + "";
            String calories = arg3 + "";
            if (steps != null && !"".equals(steps)) {
                sportInfo.setSteps(Integer.parseInt(steps));
            }
            if (distances != null && !"".equals(distances)) {
                sportInfo.setDistances(Integer.parseInt(distances));
            }
            if (calories != null && !"".equals(calories)) {
                sportInfo.setCalories(Integer.parseInt(calories));
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sportInfo.setCreateTime(sdf.format(new Date()));
            sportInfo.setDid(deviceAddress);
            sportInfo.setDateTime(date);
            sportInfoDao.insert(sportInfo);

            Intent intent = new Intent();
            intent.setAction(CommonConfiguration.RESULT_BLE_SPORTSDATA_NOTIFICATION);
            intent.putExtra("deviceAddress", deviceAddress);
            sendBroadcast(intent);
        }

        @Override
        public void onGetRawSp(Raw[] arg0) {
//            Log.v("sunping", "onGetRawSp");
            Log.i("TAG","获得详细步数信息成功！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
            //bleService.receiveHeartRate();

            long between = 0;
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                if ((new Date().getTime()) - (sdf.parse(sd.format(new Date()) + " 23:58:00").getTime()) >= 0) {
                    Thread.sleep(1000 * 60 * 5);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("TAG",arg0.length+"详细步数总条数+++++++++++++++++++++++++++++++++++++++++++++++++++");
            Log.i("TAG",arg0[0].getHour()+"小时"+"++++++++++++++++++++++++++++++++++++++++++++++++++++");
            for (int i = 0; i < arg0.length; i++) {
                Raw arg = arg0[i];
                String date = arg.getDate();

                int hour = arg.getHour();
                int[] steps = arg.getStep();
                int[] calories = arg.getCalories();

                //删除
                SportDetails del_sportDetails = sportDetailsDao.queryBuilder()
                        .where(SportDetailsDao.Properties.DateTime.eq(date), SportDetailsDao.Properties.Hour.eq(hour+""))
                        .build().unique();
                sportDetailsDao.deleteByKey(del_sportDetails.getId());

                int grpMinute = 0;
                for (int j = 0; j < steps.length; j++) {
//                    Log.v("sunping", "onGetRawSp"+j);
                    Log.i("TAG","步数："+steps[j]+"===============================================");
                    if (steps[j] == 0) {
                        continue;
                    }
                    SportDetails sportDetails = new SportDetails();
                    sportDetails.setSteps(steps[j]);//步数

                    float distances = 0f;
                    Integer type = 1;//默认走步
                    if (steps[j] >= CommonConfiguration.STEP_600 && steps[j] <= CommonConfiguration.STEP_900) {
                        type = 2;//慢跑
                        distances = steps[j] * CommonConfiguration.SPORT_TYPE_SLOW_RUN * CommonConfiguration.PERSON_HEIGHT;
                    } else if (steps[j] > CommonConfiguration.STEP_900) {
                        type = 3;//跑步
                        distances = steps[j] * CommonConfiguration.SPORT_TYPE_RUN * CommonConfiguration.PERSON_HEIGHT;
                    } else {
                        distances = steps[j] * CommonConfiguration.SPORT_TYPE_WALK * CommonConfiguration.PERSON_HEIGHT;
                    }

                    sportDetails.setType(type);//类型,1走步，2慢跑，3跑步
                    sportDetails.setDistances(distances);//距离
                    sportDetails.setCalories(calories[j]);//卡路里
                    sportDetails.setCreateTime(sdf.format(new Date()));//创建时间
                    sportDetails.setDateTime(date);//日期
                    sportDetails.setHour(hour);//小时

                    String dateTime = String.format("%1$02d:%2$02d", new Object[]{hour, j * 5});
                    sportDetails.setStartTime(date + " " + dateTime);//开始时间

                    int minute = (j + 1) * 5;
                    if (minute % 15 == 0) {
                        grpMinute = minute;
                    }
                    sportDetails.setGrpMinute(grpMinute);
                    if (minute == 60) {
                        minute = 59;
                    }
                    sportDetails.setMinute(minute);//分钟

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        sportDetails.setWeekDate(WeekTool.getWeekOfDate(df.parse(date)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dateTime = String.format("%1$02d:%2$02d", new Object[]{Integer.valueOf(hour), Integer.valueOf(minute)});
                    sportDetails.setEndTime(date + " " + dateTime);//结束时间

                    sportDetails.setDid(deviceAddress);//设备
                    sportDetailsDao.insert(sportDetails);

                    try {
//                        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//                        WifiInfo info = wifi.getConnectionInfo();
                        List listData = new ArrayList();
                        Object[] data = new Object[]{"STEP", sportDetails.getStartTime() + ":00", sportDetails.getEndTime() + ":00", sportDetails.getSteps()};
                        listData.add(listData);
                        JSONObject object = new JSONObject();
                        JSONArray jsonArrayData = new JSONArray();
                        jsonArrayData.put(data[0]);
                        jsonArrayData.put(data[1]);
                        jsonArrayData.put(data[2]);
                        jsonArrayData.put(data[3]);

                        JSONArray jsonArrayListData = new JSONArray();
                        jsonArrayListData.put(jsonArrayData);

//                        RequestParams params = new RequestParams();
////                        params.put("Mac", info.getMacAddress());
//                        params.put("Mac", sharedPreferencesDao.getString("deviceAddress"));
//                        params.put("Data", jsonArrayListData.toString());
////                        Log.v("sunping", "deviceAddress:"+sharedPreferencesDao.getString("deviceAddress"));
//
//                        Message msg = handler.obtainMessage();
//                        msg.obj = params;
//                        handler.sendMessage(msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            // TODO Auto-generated method stub


            Intent intent = new Intent();
            intent.setAction(CommonConfiguration.RESULT_BLE_SPORTSDATA_NOTIFICATION);
            intent.putExtra("deviceAddress", deviceAddress);
            sendBroadcast(intent);
        }

//        private Handler handler = new Handler() {
//
//            @Override
//            public void handleMessage(Message msg) {
//                // TODO Auto-generated method stub
//                RequestParams params = (RequestParams) msg.obj;
//
//                httpClient.post(CommonConfiguration.HTTP_SERVICE_ADDRESS + CommonConfiguration.HTTP_SYNCH_DATA_ADDRESS, params, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                        super.onSuccess(statusCode, headers, response);
//                        if (response != null) {
//                            if (response.toString().indexOf("SUCCESS") != -1) {
//                                Log.v("sunping", "SUCCESS");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        Log.v("sunping", "onFailure");
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        Log.v("sunping", "onFinish");
//                    }
//                });
//
//            }
//
//        };


        @Override
        public void onAntiLostSet() {
            // TODO Auto-generated method stub

        }

    };

    public boolean isWorked() {
        ActivityManager myManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
//            Log.i("sunping", runningService.get(i).service.getClassName().toString()+",,,,,com.wisgen.health.service.DeviceService");
            if (runningService.get(i).service.getClassName().toString().equals("lbcy.com.cn.purplelibrary.service.AirBLEService")) {
//                Log.i("sunping", runningService.get(i).service.getClassName().toString()+",,,,,com.wisgen.health.service.AirBLEService");
                return true;
            }
        }
        return false;
    }

}
