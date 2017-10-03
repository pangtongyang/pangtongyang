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
    private String sport_code;

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
    public boolean startSport(String sport_code) {
        this.sport_code = sport_code;
//        agent.getWebCreator().get().clearHistory();
        return true;
    }

    /**
     * 结束心率测量
     * @return 默认返回true
     */
    @JavascriptInterface
    public String stopSport() {
        //由web上传运动数据
        /*
         * {
           sport_code: 10001,
           end_time: "2017-08-10 03:04:00",
           duration: "00:30:00",
           average_heartbeats: 125,
           history: [
             {time: "2017-08-10 03:00:00", heartbeats: 153, steps: 100},
             {time: "2017-08-10 03:01:00", heartbeats: 133, steps: 100},
             {time: "2017-08-10 03:02:00", heartbeats: 123, steps: 100},
             {time: "2017-08-10 03:03:00", heartbeats: 113, steps: 100},
             {time: "2017-08-10 03:04:00", heartbeats: 153, steps: 100}
             ...
           ]
         }
         */
        return "";
    }

    /**
     * 获取心率数据
     * @return 默认返回json，min_heartbeats -> 最小心率 max_heartbeats -> 最大心率，
     * distance -> 距离 speed -> 速度
     * duration_time -> 经过的时间
     * count_time -> 还剩的时间
     * actual_heartbeats -> 实时心率
     */
    @JavascriptInterface
    public String getSportInfo(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("duration_time", (int)(Math.random() * 100));
            jsonObject.put("count_time", (int)(Math.random() * 100));
            jsonObject.put("actual_heartbeats", (int)(Math.random() * 100));
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
