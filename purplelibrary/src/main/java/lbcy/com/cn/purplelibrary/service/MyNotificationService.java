package lbcy.com.cn.purplelibrary.service;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.List;

import lbcy.com.cn.purplelibrary.app.MyApplication;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.entity.AppPushInfo;
import lbcy.com.cn.purplelibrary.entity.AppPushInfoDao;

/**
 * 监听通知消息
 */
@SuppressLint("OverrideAbstract")
public class MyNotificationService extends NotificationListenerService {
    private String TAG = this.getClass().getSimpleName();
    private AppPushInfoDao appPushInfoDao;
    private PackageManager pm;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onCreate();
//        appPushInfoDao = new AppPushInfoDaoImpl(this);
//        Log.i("sunping", "Srevice is open"+"-----");
        return super.onStartCommand(intent, flags, startId);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
//        Log.i("sunping", "Get Message"+"-----"+sbn.getNotification().tickerText.toString());
        pm = this.getPackageManager();

        appPushInfoDao = MyApplication.getInstances().getDaoSession().getAppPushInfoDao();
        List<AppPushInfo> list = appPushInfoDao.queryBuilder()
                .where(AppPushInfoDao.Properties.IsEnabled.eq(1))
                .orderAsc(AppPushInfoDao.Properties.PackageName)
                .build().list();
        if (!list.isEmpty()) {
            for (int i=0;i<list.size();i++){
                AppPushInfo appPushInfo=list.get(i);
                if(sbn.getPackageName().equals(appPushInfo.getPackageName())){
                    try {
                        ApplicationInfo applicationInfo=pm.getApplicationInfo(appPushInfo.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);
                        if(applicationInfo==null){
                            continue;
                        }
                        Intent intent = new Intent();
//                        if(AirBLEService.parser!=null){
                            //设置Intent的action属性
                            intent.setAction(CommonConfiguration.PUSH_MESSAGE_NOTIFICATION);
                            intent.putExtra("appName", applicationInfo.loadLabel(getPackageManager()).toString());
                            intent.putExtra("title", sbn.getNotification().tickerText);
                            //发出广播
                            sendBroadcast(intent);
//                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                }


            }
        }
//        Intent intent = new Intent();
//        if(AirBLEService.parser!=null) {
//            //设置Intent的action属性
//            intent.setAction(CommonConfiguration.PUSH_MESSAGE_NOTIFICATION);
//            intent.putExtra("appName", "微信");
//            intent.putExtra("title", sbn.getNotification().tickerText );
//            //发出广播
//            sendBroadcast(intent);
//        }
    }

//    @Override
//    public void onNotificationRemoved(StatusBarNotification sbn) {
//    }

}