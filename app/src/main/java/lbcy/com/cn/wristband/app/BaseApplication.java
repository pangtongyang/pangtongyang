package lbcy.com.cn.wristband.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.internal.RxBleLog;

import lbcy.com.cn.blacklibrary.manager.BlackDeviceManager;
import lbcy.com.cn.purplelibrary.app.MyApplication;

/**
 * Created by chenjie on 2017/8/6.
 */

public class BaseApplication extends MyApplication {
    private static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication baseApplication;
    private RxBleClient rxBleClient;

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
}
