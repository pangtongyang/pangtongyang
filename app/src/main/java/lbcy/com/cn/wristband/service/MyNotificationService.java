package lbcy.com.cn.wristband.service;

import android.app.Notification;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.manager.PurpleDeviceManagerNew;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxBus;

/**
 * 监听通知消息
 */
public class MyNotificationService extends NotificationListenerService {
    private PackageManager pm;

    SPUtil spUtil;
    String device_type;
    String lastPkg = "";
    String lastTitle = "";
    String lastContent = "";
    String lastRemovedPkg = "";
    String lastRemovedTitle = "";
    String lastRemovedContent = "";

    long time = 0; // 超时记录

    @Override
    public void onCreate() {
        super.onCreate();
        spUtil = new SPUtil(getBaseContext(), CommonConfiguration.SHAREDPREFERENCES_NAME);
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
        if (device_type.equals("1")){
            return;
        }
        Notification notification = sbn.getNotification();
        String pkg = sbn.getPackageName();
        String title = notification.extras.getString(Notification.EXTRA_TITLE);
        String content = notification.extras.getString(Notification.EXTRA_TEXT);
        if (!pkg.equals(Consts.INCALL)){
            return;
        }
        if (lastRemovedPkg.equals(pkg) && lastRemovedTitle.equals(title) && lastRemovedContent.equals(content))
            return;

        lastRemovedPkg = pkg;
        lastRemovedTitle = title;
        lastRemovedContent = content;

        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("type", pkg);
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putString("remove", "1");
        message.what = Consts.SHOW_MESSAGE_FROM_APPS;
        message.setData(bundle);

        RxBus.getInstance().post(Consts.NOTIFICATION_LISTENER, message);
    }

    private void purplePostHandler(StatusBarNotification sbn){
        Notification notification = sbn.getNotification();
        String pkg = sbn.getPackageName();
        String title = notification.extras.getString(Notification.EXTRA_TITLE);
        String content = notification.extras.getString(Notification.EXTRA_TEXT);
        if (!pkg.equals(Consts.QQ) && !pkg.equals(Consts.WEIXIN) && !pkg.equals(Consts.MMS) && !pkg.equals(Consts.FACEBOOK) && !pkg.equals(Consts.INCALL) && !pkg.equals(Consts.NOTRECEIVECALL)){
            return;
        }
        if (lastPkg.equals(pkg) && lastTitle.equals(title) && lastContent.equals(content)){
            if (System.currentTimeMillis() - time < 5000){
                return;
            }
        }

        time = System.currentTimeMillis();

        lastPkg = pkg;
        lastTitle = title;
        lastContent = content;
        if (pkg.equals(Consts.MMS)){
            title = "短信";
            content = "收到一条短信";
        }

        PurpleDeviceManagerNew.getInstance().sendNotification(title, content);
    }

    private void blackPostHandler(StatusBarNotification sbn){
        Notification notification = sbn.getNotification();
        String pkg = sbn.getPackageName();
        String title = notification.extras.getString(Notification.EXTRA_TITLE);
        String content = notification.extras.getString(Notification.EXTRA_TEXT);
        if (content == null || content.equals("")){
            content = notification.extras.getString(Notification.EXTRA_SUB_TEXT);
        }
        if (!pkg.equals(Consts.QQ) && !pkg.equals(Consts.WEIXIN) && !pkg.equals(Consts.MMS) && !pkg.equals(Consts.FACEBOOK) && !pkg.equals(Consts.INCALL) && !pkg.equals(Consts.NOTRECEIVECALL)){
            return;
        }

        title = (title == null ? "" : title);
        content = (content == null ? "" : content);
        if (lastPkg.equals(pkg) && lastTitle.equals(title) && lastContent.equals(content))
            return;

        lastPkg = pkg;
        lastTitle = title;
        lastContent = content;

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