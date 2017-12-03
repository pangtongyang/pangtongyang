package lbcy.com.cn.wristband.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.multidex.MultiDex;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import lbcy.com.cn.blacklibrary.manager.BlackDeviceManager;
import lbcy.com.cn.purplelibrary.app.MyApplication;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.entity.DaoMaster;
import lbcy.com.cn.wristband.entity.DaoSession;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxManager;
import lbcy.com.cn.wristband.utils.database.UpgradeHelper;
import rx.functions.Action1;

/**
 * Created by chenjie on 2017/8/6.
 */

public class BaseApplication extends MyApplication {
    private static BaseApplication baseApplication;

    private UpgradeHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private RxManager rxManager;

    private SPUtil spUtil;

    public final List<String> WEB_TITLE_LIST = new ArrayList<>();
    public final List<String> WEB_URL_LIST = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        spUtil = new SPUtil(this, CommonConfiguration.SHAREDPREFERENCES_NAME);
        setDatabase();

        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                        .proxy(Proxy.NO_PROXY) // set proxy
                ))
                .commit();

        BlackDeviceManager.getInstance().setContext(baseApplication);

        initWebTitle();

        rxManager = new RxManager();
        rxManager.on(Consts.NOTIFICATION_LISTENER, new Action1<Message>() {
            @Override
            public void call(Message message) {
                Bundle bundle = message.getData();
                String pkg = bundle.getString("type", "");
                String title = bundle.getString("title");
                String content  =bundle.getString("content");
                String isEnable = "0";
                switch (pkg){
                    case Consts.FACEBOOK:
                        isEnable = spUtil.getString("facebook_switch", "0");
                        break;
                    case Consts.MMS:
                        isEnable = spUtil.getString("sms_switch", "0");
                        if (isEnable.equals("1")){
                            BlackDeviceManager.getInstance().smsRemind(true);
                            BlackDeviceManager.getInstance().smsNotification(title);
                        }
                        return;
                    case Consts.WEIXIN:
                        isEnable = spUtil.getString("wechat_switch", "0");
                        break;
                    case Consts.QQ:
                        isEnable = spUtil.getString("qq_switch", "0");
                        break;
                    case Consts.INCALL:
                        String remove = bundle.getString("remove", "");
                        if (remove.equals("")){
                            isEnable = spUtil.getString("telephone_switch", "0");
                            if (isEnable.equals("1")){
                                BlackDeviceManager.getInstance().phoneRemind(true);
                                BlackDeviceManager.getInstance().isPhoneRemind(true, content, title);
                            }
                        } else {
                            BlackDeviceManager.getInstance().isPhoneRemind(false, content, title);
                        }

                        return;
                    case  Consts.NOTRECEIVECALL:
                        BlackDeviceManager.getInstance().isPhoneRemind(false, content, title);
                        return;
                }

                if (isEnable.equals("1"))
                    BlackDeviceManager.getInstance().setAppNotification(pkg, title, content);
            }
        });
    }

    public static BaseApplication getBaseApplication() {

        return baseApplication;
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 通过UpgradeHelper实现数据库平滑升级
        mHelper = new UpgradeHelper(this, "bases-db");
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getBaseDaoSession() {
        return mDaoSession;
    }

    public void cleanDB() {
        if (mDaoMaster != null){
            DaoMaster.dropAllTables(mDaoMaster.getDatabase(), true);
            DaoMaster.createAllTables(mDaoMaster.getDatabase(), true);
        }
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void initWebTitle(){
        WEB_URL_LIST.add(Consts.WEB_SEQUENCE);
        WEB_TITLE_LIST.add(getString(R.string.title_sport_sequence));
        WEB_URL_LIST.add(Consts.WEB_HISTORY);
        WEB_TITLE_LIST.add(getString(R.string.title_sport_history));
        WEB_URL_LIST.add(Consts.WEB_HEART_RATE_SPORT);
        WEB_TITLE_LIST.add(getString(R.string.title_sport));
        WEB_URL_LIST.add(Consts.WEB_HEART_RATE_TEST);
        WEB_TITLE_LIST.add(getString(R.string.title_heart_rate_start));
        WEB_URL_LIST.add(Consts.WEB_HEART_RATE_DETAIL);
        WEB_TITLE_LIST.add(getString(R.string.title_sport_detail));
        WEB_URL_LIST.add(Consts.WEB_HEART_RATE_HISTORY);
        WEB_TITLE_LIST.add(getString(R.string.title_heart_rate_week));
        WEB_URL_LIST.add(Consts.WEB_SLEEP_HISTORY);
        WEB_TITLE_LIST.add(getString(R.string.title_sleep));
        WEB_URL_LIST.add(Consts.WEB_CLASS_ATTEND);
        WEB_TITLE_LIST.add(getString(R.string.title_attendance));
        WEB_URL_LIST.add(Consts.WEB_CLASS_RANKING);
        WEB_TITLE_LIST.add(getString(R.string.title_get_school_sequence));
        WEB_URL_LIST.add(Consts.WEB_VIDEO);
        WEB_TITLE_LIST.add(getString(R.string.title_sport_video));


    }
}
