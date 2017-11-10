package lbcy.com.cn.purplelibrary.app;

import android.app.Application;
import android.app.Service;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

import lbcy.com.cn.purplelibrary.entity.DaoMaster;
import lbcy.com.cn.purplelibrary.entity.DaoSession;
import lbcy.com.cn.purplelibrary.manager.PurpleDeviceManager;
import lbcy.com.cn.purplelibrary.service.PurpleBLEService;

/**
 * Created by chenjie on 2017/8/24.
 */

public class MyApplication extends Application {
    private SQLiteDatabase db;
    private DaoSession mDaoSession;
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setDatabase();
        PurpleDeviceManager.getInstance().setContext(this);
    }

    public static MyApplication getInstances() {
        return instance;
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        DaoMaster mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    private Map<String, PurpleBLEService> threadLocal = new HashMap<>();

    public void setThread(PurpleBLEService service){
        threadLocal.put("thread", service);
    }

    public PurpleBLEService getThread(){
        return threadLocal.get("thread");
    }

    public void removeThread(){
        threadLocal.remove("thread");
    }
}
