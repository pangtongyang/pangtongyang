package lbcy.com.cn.purplelibrary.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备管理器
 */
public class ManagerDeviceService {
    private static Context context;
    private static Intent intent;
    private String TAG = this.getClass().getSimpleName();

    public ManagerDeviceService(Context context) {
        this.context = context;
    }

    public static void startService() {


        ActivityManager mActivityManager  =  (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> mServiceList  =  mActivityManager.getRunningServices(300);
        //
        final String className = "lbcy.com.cn.purplelibrary.service.DeviceService";

        boolean b = ServiceIsStart(mServiceList, className);
        if(b){
            stopService();
        }

        intent = new Intent();
        intent.setAction("lbcy.com.cn.purplelibrary.service.DeviceService");
        intent.setPackage(context.getPackageName());
        intent.setClass(context, DeviceService.class);
        context.startService(intent);
        
    }
    //通过Service的类名来判断是否启动某个服务
    private static boolean ServiceIsStart(List<ActivityManager.RunningServiceInfo> mServiceList, String className) {
        for (int i = 0; i < mServiceList.size(); i++) {
            if (className.equals(mServiceList.get(i).service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public static void stopService() {
        context.stopService(intent);
    }

    public static boolean isWorked() {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals("lbcy.com.cn.purplelibrary.service.AirBLEService")) {
                return true;
            }
        }
        return false;
    }



}
