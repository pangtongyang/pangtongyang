package lbcy.com.cn.wristband.utils;

import android.os.Handler;

public class HandlerTip {

    private static HandlerTip mDelayded = new HandlerTip();
    private Handler handler;

    private HandlerTip() {
        handler = new Handler();
    }


    public static HandlerTip getInstance() {
        if (mDelayded == null)
            mDelayded = new HandlerTip();
        return mDelayded;
    }

    public Handler getHandler() {
        if (handler == null)
            handler = new Handler();
        return handler;
    }

    public void postDelayed(int time, final HandlerCallback call) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                call.postDelayed();
            }
        }, time);
    }

    public interface HandlerCallback {
        void postDelayed();
    }
}
