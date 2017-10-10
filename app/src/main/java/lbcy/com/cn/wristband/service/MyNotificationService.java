package lbcy.com.cn.wristband.service;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.List;

import lbcy.com.cn.purplelibrary.app.MyApplication;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.entity.AppPushInfo;
import lbcy.com.cn.purplelibrary.entity.AppPushInfoDao;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxBus;

/**
 * 监听通知消息
 */
public class MyNotificationService extends NotificationListenerService {
    private AppPushInfoDao appPushInfoDao;
    private PackageManager pm;

    SPUtil spUtil;
    String device_type;

    @Override
    public void onCreate() {
        super.onCreate();
        appPushInfoDao = MyApplication.getInstances().getDaoSession().getAppPushInfoDao();
        spUtil = new SPUtil(getBaseContext(), Consts.SETTING_DB_NAME);
        device_type = spUtil.getString("which_device", "2");
    }

    @Override
    public void onDestroy() {
        //不得关闭当前service，否则将无法收到消息回调
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (device_type.equals("2")){
            blackPostHandler(sbn);
        } else {
            purplePostHandler(sbn);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    private void purplePostHandler(StatusBarNotification sbn){
        pm = this.getPackageManager();


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
    }

    private void blackPostHandler(StatusBarNotification sbn){
        Notification notification = sbn.getNotification();
        String pkg = sbn.getPackageName();
        String title = notification.extras.getString(Notification.EXTRA_TITLE);
        String content = notification.extras.getString(Notification.EXTRA_TEXT);
        if (content == null || content.equals("")){
            content = notification.extras.getString(Notification.EXTRA_SUB_TEXT);
        }
        if (!pkg.equals(Consts.QQ) && !pkg.equals(Consts.WEIXIN) && pkg.equals(Consts.MMS) && pkg.equals(Consts.FACEBOOK)){
            return;
        }

        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("type", pkg);
        bundle.putString("title", title);
        bundle.putString("content", content);
        message.what = Consts.SHOW_MESSAGE_FROM_APPS;
        message.setData(bundle);

        RxBus.getInstance().post(Consts.NOTIFICATION_LISTENER, message);
    }

}