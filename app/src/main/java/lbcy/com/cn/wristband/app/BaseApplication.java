package lbcy.com.cn.wristband.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.internal.RxBleLog;

import lbcy.com.cn.blacklibrary.manager.BlackDeviceManager;
import lbcy.com.cn.purplelibrary.app.MyApplication;
import lbcy.com.cn.wristband.entity.DaoMaster;
import lbcy.com.cn.wristband.entity.DaoSession;

/**
 * Created by chenjie on 2017/8/6.
 */

public class BaseApplication extends MyApplication {
    private static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication baseApplication;
    private RxBleClient rxBleClient;

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    /**
     * In practise you will use some kind of dependency injection pattern.
     */
    public static RxBleClient getRxBleClient(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.rxBleClient;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        setDatabase();
        BlackDeviceManager.setContext(baseApplication);

        rxBleClient = RxBleClient.create(this);
        RxBleClient.setLogLevel(RxBleLog.DEBUG);

//        BluetoothConfig config = new BluetoothConfig.Builder()
//                .enableQueueInterval(true)
//                .setQueueIntervalTime(BluetoothConfig.AUTO)//设置队列间隔时间为自动
//                .build();
//        BluetoothLe.getDefault().init(this, config);

    }

    public static BaseApplication getBaseApplication() {

        return baseApplication;
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        mHelper = new DaoMaster.DevOpenHelper(this, "bases-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getBaseDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
