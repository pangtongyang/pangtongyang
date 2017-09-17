package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.global.Consts;

/**
 * Created by chenjie on 2017/9/15.
 */

public class SplashActivity extends BaseActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();
    SPUtil spUtil;
    Thread thread;
    private static final int sleepTime = 3000; //启动页启动间隔
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, Consts.USER_DB_NAME);
    }

    @Override
    protected void initView() {
        hideTopBar();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        thread = new Thread(runnable);
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        thread.interrupt();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            //You can do something here!
            String isLogin = spUtil.getString("is_login", "0");

            long costTime = System.currentTimeMillis() - start;
            try {
                Thread.sleep(sleepTime - costTime);
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
                MainActivity.mActivity.finish();
                finish();
                return;
            }
            if (isLogin.equals("1")){
//                Intent intent = new Intent(mActivity, MainActivity.class);
//                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
}
