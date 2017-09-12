package lbcy.com.cn.purplelibrary.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import lbcy.com.cn.purplelibrary.entity.DaoMaster;
import lbcy.com.cn.purplelibrary.entity.DaoSession;
import lbcy.com.cn.purplelibrary.manager.PurpleDeviceManager;

/**
 * Created by chenjie on 2017/8/24.
 */

public class MyApplication extends Application {
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setDatabase();
        PurpleDeviceManager.setContext(this);
    }

    public static MyApplication getInstances() {
        return instance;
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
