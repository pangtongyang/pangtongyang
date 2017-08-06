package lbcy.com.cn.wristband.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

//import lbcy.com.cn.blacklibrary.manager.DeviceManager;

/**
 * Created by chenjie on 2017/8/6.
 */

public class BaseApplication extends Application {
    private static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication baseApplication;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
//        DeviceManager.setContext(baseApplication);
    }

    public static BaseApplication getBaseApplication() {

        return baseApplication;
    }
}
