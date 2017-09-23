package lbcy.com.cn.wristband.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.just.library.AgentWeb;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cenxiaozhong on 2017/5/14.
 *  source CODE  https://github.com/Justson/AgentWeb
 */

public class AndroidInterface {

    private Handler deliver = new Handler(Looper.getMainLooper());
    private AgentWeb agent;
    private Context context;

    public AndroidInterface(AgentWeb agent, Context context) {
        this.agent = agent;
        this.context = context;
    }



    @JavascriptInterface
    public String callAndroid(final String msg) {


        deliver.post(new Runnable() {
            @Override
            public void run() {

                Log.i("Info", "main Thread:" + Thread.currentThread());
                Toast.makeText(context.getApplicationContext(), "" + msg, Toast.LENGTH_LONG).show();
            }
        });


        Log.i("Info", "Thread:" + Thread.currentThread());
        return "test!!!!!!!!!";
    }

    /**
     * 开始运动心率测量
     * @return 默认返回true
     */
    @JavascriptInterface
    public boolean startSport() {
//        agent.getWebCreator().get().clearHistory();
        return true;
    }

    /**
     * 结束心率测量
     * @return 默认返回true
     */
    @JavascriptInterface
    public boolean stopSport() {
        return true;
    }

    /**
     * 获取心率数据
     * @return 默认返回json，min_heartbeats -> 最小心率 max_heartbeats -> 最大心率，
     * distance -> 距离 speed -> 速度
     */
    @JavascriptInterface
    public String getSportInfo(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("min_heartbeats", (int)(Math.random() * 100));
            jsonObject.put("max_heartbeats", (int)(Math.random() * 100 + 100));
            jsonObject.put("distance", 60);//unit: m
            jsonObject.put("speed", 15);//unit: km/h
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 目标设置
     * @param minute 默认15分钟
     * @return 默认返回true
     */
    @JavascriptInterface
    public boolean setSportTarget(String minute){

        return true;
    }

}
