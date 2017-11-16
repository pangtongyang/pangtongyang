package lbcy.com.cn.wristband.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.huichenghe.bleControl.Ble.BluetoothLeService;
import com.just.library.AgentWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lbcy.com.cn.blacklibrary.ble.DataCallback;
import lbcy.com.cn.blacklibrary.manager.BlackDeviceManager;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.PurpleBleScan;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.entity.HeartBeatsAllDayHistory;
import lbcy.com.cn.wristband.entity.HeartBeatsAllDayHistoryDao;
import lbcy.com.cn.wristband.entity.HeartBeatsAllDayMaxMin;
import lbcy.com.cn.wristband.entity.HeartBeatsAllDayMaxMinDao;
import lbcy.com.cn.wristband.entity.HeartBeatsHistory;
import lbcy.com.cn.wristband.entity.HeartBeatsHistoryDao;
import lbcy.com.cn.wristband.entity.HeartBeatsInfoCurrentBean;
import lbcy.com.cn.wristband.entity.MessageBean;
import lbcy.com.cn.wristband.entity.SportAllDayData;
import lbcy.com.cn.wristband.entity.SportAllDayDataDao;
import lbcy.com.cn.wristband.entity.SportCodeBean;
import lbcy.com.cn.wristband.entity.SportHeartRateTo;
import lbcy.com.cn.wristband.entity.SportInfoCurrentBean;
import lbcy.com.cn.wristband.entity.SportLocationTo;
import lbcy.com.cn.wristband.entity.SportSplitData;
import lbcy.com.cn.wristband.entity.SportSplitDataDao;
import lbcy.com.cn.wristband.manager.NetManager;
import lbcy.com.cn.wristband.utils.DateUtil;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by cenxiaozhong on 2017/5/14.
 *  source CODE  https://github.com/Justson/AgentWeb
 */

public class AndroidInterface {

    private Handler deliver = new Handler(Looper.getMainLooper());
    private AgentWeb agent;
    private Context context;
    private String sport_code;
    private String target_duration = "15"; //目标运动时长 单位min
    private int current_heart_rate = 0; //当前心率
    private int max_heart_rate = 0; //最大心率
    private int min_heart_rate = Integer.MAX_VALUE; //最小心率
    private int current_steps = 0; //当前总步数
    private int before_steps = 0; //上一次总步数
    private int distance = 0; // 当前手环总距离 单位m
    private int start_distance = 0; // 开始手环总距离 单位m

    private long currentMilli = 0;

    private long start_time; //开始运动时间 单位ms
    private double speed; //当前运动速度

    private SPUtil spUtil;
    private String which_device;
    private String token;

    // 计算平均心率
    private int sum_heartbeats = 0;
    private int num_heartbeats = 0;

    // 位置上传相关
    private AMapLocationClient locationClient = null;
    private SportLocationTo locations;
    private List<SportLocationTo.HistoryBean> locationHistoryList;

    // 心率数据
    private SportHeartRateTo  heartRateTo;
    private List<SportHeartRateTo.HistoryBean> heartRateHistoryList;

    public AndroidInterface(AgentWeb agent, Context context) {
        this.agent = agent;
        this.context = context;

        // 获取设备信息
        spUtil = new SPUtil(context, CommonConfiguration.SHAREDPREFERENCES_NAME);
        which_device = spUtil.getString("which_device", "2");
        token = spUtil.getString("token", "");
    }


    // demo
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
    public int startSport() {
        Toast.makeText(BaseApplication.getBaseApplication(), "请注意打开设备心率功能", Toast.LENGTH_LONG).show();

        sum_heartbeats = 0;
        num_heartbeats = 0;

        start_time = System.currentTimeMillis();
//        agent.getWebCreator().get().clearHistory();
        // 首先判断手环是否连接
        if (which_device.equals("2")){
            if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice())
                return 0;
            else
                // 异步获取心率运动数据，可能存在延迟
                b_openHeartSportScanning(true);
        }
        else {
            String is_connected = spUtil.getString("is_connected", "0");
            if (is_connected.equals("0")){
                return 0;
            }
            p_getData();
        }

        // 获取运动code，同时初始化相关数据对象
        getSportCode();

        // 初始化定位服务
        initLocation();
        // 开始定位
        if (locationClient != null)
            locationClient.startLocation();

        return 1;
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
        if (which_device.equals("2")){
            if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice())
                return null;
            // 关闭异步获取心率运动数据
            b_openHeartSportScanning(false);
        }

        start_distance = before_steps = current_steps = 0;

        // 停止获取位置信息
        if (locationClient != null)
            locationClient.stopLocation();
//        if (locationManager != null)
//            locationManager.removeUpdates(locationListener1);

        // 上传位置信息
        if (locations == null || locationHistoryList == null)
            return null;
        locations.setHistory(locationHistoryList);
        uploadLocations();

        // 将运动数据传递给web
        if (heartRateTo == null || heartRateHistoryList == null)
            return null;

        long duration_time = System.currentTimeMillis() - start_time; //已运动时长 单位ms
        int hour = (int) (duration_time / 1000 / 3600);
        int minute = (int) (duration_time / 1000 / 60) - hour * 60;
        int second = (int) (duration_time / 1000) - hour * 3600 - minute * 60;
        String h = hour < 10 ? "0"+ hour : String.valueOf(hour);
        String m = minute < 10 ? "0"+ minute : String.valueOf(minute);
        String s = second < 10 ? "0"+ second : String.valueOf(second);
        heartRateTo.setDuration(h + ":" + m + ":" + s);
        heartRateTo.setEnd_time(DateUtil.getCurrentTime());
        heartRateTo.setAverage_heartbeats(sum_heartbeats / num_heartbeats);
        heartRateTo.setHistory(heartRateHistoryList);
        Gson gson = new Gson();
        String json = gson.toJson(heartRateTo);

        if (heartRateTo.getSport_code() == 0){
            Toast.makeText(BaseApplication.getBaseApplication(), "数据上传失败！", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (heartRateTo.getHistory() != null && heartRateTo.getHistory().size() != 0)
            return json;
        else
            return null;
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
        if (which_device.equals("2")){
            if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice())
                return null;
            else
                BlackDeviceManager.getInstance().sendHeartSportRequestForData();
        }

        JSONObject jsonObject = new JSONObject();

        long duration_time = System.currentTimeMillis() - start_time; //已运动时长 单位ms
        long left_time = Long.valueOf(target_duration) * 60 * 1000 - duration_time; //还剩时长 单位ms

        SportHeartRateTo.HistoryBean history = new SportHeartRateTo.HistoryBean();
        history.setTime(DateUtil.getCurrentTime());
        history.setHeartbeats(current_heart_rate);
        history.setSteps(before_steps == 0 ? 0 : current_steps - before_steps);
        heartRateHistoryList.add(history);

        // 记录上次步数
        before_steps = current_steps;

        try {
            jsonObject.put("duration_time", (int)(duration_time / 1000));
            jsonObject.put("count_time", (int)(left_time / 1000));
            jsonObject.put("actual_heartbeats", current_heart_rate);
            jsonObject.put("min_heartbeats", min_heart_rate == Integer.MAX_VALUE ? 0 : min_heart_rate);
            jsonObject.put("max_heartbeats", max_heart_rate);
            jsonObject.put("distance", distance - start_distance);//unit: m
            // 四舍五入保留两位小数
            BigDecimal decimal = new BigDecimal(speed);
            speed = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            jsonObject.put("speed", speed);//unit: km/h
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
    public int setSportTarget(String minute){
        start_time = System.currentTimeMillis();
        target_duration = minute;
        return 1;
    }

    /**
     * 获取当前运动数据
     * @return 返回运动数据
     */
    @JavascriptInterface
    public String getSportInfoCurrent(){
        long lastTime = System.currentTimeMillis();
        // 判断是否首次加载首屏
        if (spUtil.getString("is_first_in_main", "0").equals("1")){
            // 等待连接
//            while(true){
//                if (System.currentTimeMillis() - lastTime > 12000){
//                    return null;
//                }
//                // 首先判断手环是否连接
//                if (which_device.equals("2")){
//                    if (BluetoothLeService.getInstance() != null && BluetoothLeService.getInstance().isConnectedDevice())
//                        break;
//                }
//                else {
//                    // 紫色手环不做连接检测，因为连接速度慢
//                    String is_connected = spUtil.getString("is_connected", "0");
//                    if (MyApplication.getInstances().getThread().canCallBack)
//                        break;
//                }
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            // 将状态置为非首次加载首屏
            spUtil.putString("is_first_in_main", "0");
        }

        SportInfoCurrentBean currentBean = new SportInfoCurrentBean();
        int goal_steps = Integer.parseInt(spUtil.getString("goal_steps", "10000"));
        String date = DateUtil.getCurrentTime_Y_M_d();
        SportAllDayDataDao dayDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getSportAllDayDataDao();
        SportAllDayData data = dayDataDao.queryBuilder().where(SportAllDayDataDao.Properties.Date.eq(date)).build().unique();
        int done_steps;

        // 数据未获取，等待数据获取超时
        lastTime = System.currentTimeMillis();
        if(data == null)
            return null;
//        while(data == null){
//            if (System.currentTimeMillis() - lastTime > 5000){
//                break;
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            data = dayDataDao.queryBuilder().where(SportAllDayDataDao.Properties.Date.eq(date)).build().unique();
//        }
        done_steps = data.getDone_steps();

        currentBean.setGoal_steps(goal_steps);
        currentBean.setDone_steps(done_steps);
        SportSplitDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getSportSplitDataDao();
        List<SportSplitData> list = dataDao.queryBuilder().where(SportSplitDataDao.Properties.Date.like(date+"%")).build().list();
        if (list != null && list.size() != 0){
            List<SportInfoCurrentBean.HistoryBean> sportList = new ArrayList<>();
            for (SportSplitData mData : list){
                SportInfoCurrentBean.HistoryBean historyBean = new SportInfoCurrentBean.HistoryBean();
                historyBean.setTime(mData.getDate());
                historyBean.setSteps(mData.getSteps());
                sportList.add(historyBean);
            }
            currentBean.setHistory(sportList);
        }

        // 没有数据，<!--返回空-->，照常发送，受限于紫色手环没有分时数据
//        if (currentBean.getHistory().size() == 0)
//            return "";
        Gson gson = new Gson();
        return gson.toJson(currentBean);
    }

    // getHeartbeatsInfoNow使用
    private int min = Integer.MAX_VALUE;
    private int max = 0;
    /**
     * 首次进入页面
     * 获取历史4小时内的心率数据，每隔超过5分钟测量一次
     * @return 返回心率历史数据
     */
    @JavascriptInterface
    public String getHeartbeatsInfoCurrent(){

        current_heart_rate = max_heart_rate = 0;
        min_heart_rate = Integer.MAX_VALUE;

        // 首先判断手环是否连接
        if (which_device.equals("2")){
            if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice())
                return null;
//            BlackDeviceManager.getInstance().heartScan(true);
        }
        else {
            String is_connected = spUtil.getString("is_connected", "0");
            if (is_connected.equals("0")){
                return null;
            }
            p_getData();
        }

        HeartBeatsAllDayMaxMinDao maxMinDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHeartBeatsAllDayMaxMinDao();
        String date = DateUtil.getCurrentTime_Y_M_d();
        // 没有当天的心率数据，返回空
        if (maxMinDao.queryBuilder().where(HeartBeatsAllDayMaxMinDao.Properties.Date.eq(date)).count() == 0)
            return null;
        HeartBeatsAllDayMaxMin maxMin = maxMinDao.queryBuilder().where(HeartBeatsAllDayMaxMinDao.Properties.Date.eq(date)).build().unique();
        int max = maxMin.getMax_heartbeats();
        int min = maxMin.getMin_heartbeats();
        HeartBeatsAllDayHistoryDao historyDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHeartBeatsAllDayHistoryDao();

        // 四小时前的时间
        String time = DateUtil.getTime(-4);
        List<HeartBeatsAllDayHistory> histories = historyDao.queryBuilder().
                where(HeartBeatsAllDayHistoryDao.Properties.Time.like(date + "%"), HeartBeatsAllDayHistoryDao.Properties.Time.gt(time)).
                build().list();
        if (histories.size() == 0)
            return null;


        HeartBeatsInfoCurrentBean currentBean = new HeartBeatsInfoCurrentBean();
        List<HeartBeatsInfoCurrentBean.HistoryBean> historyBeanList = new ArrayList<>();
        for (HeartBeatsAllDayHistory history : histories){
            if (history.getHeartbeats() == 0)
                continue;

            HeartBeatsInfoCurrentBean.HistoryBean historyBean = new HeartBeatsInfoCurrentBean.HistoryBean();
            historyBean.setTime(history.getTime());
            historyBean.setHeartbeats(history.getHeartbeats());
            historyBeanList.add(historyBean);
        }
        currentBean.setHistory(historyBeanList);
        currentBean.setMin_heartbeats(min);
        currentBean.setMax_heartbeats(max);
        Gson gson = new Gson();
        return gson.toJson(currentBean);
    }

    private boolean isCallBackStart = false;
    /**
     * 获取心率数据
     * @return 返回当前心率数据
     */
    @JavascriptInterface
    public String getHeartbeatsInfoNow(){

        // 首先判断手环是否连接
        if (which_device.equals("2")){
            if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice())
                return null;
            else if (!isCallBackStart){
                b_getHeartData();
                isCallBackStart = true;
            }
        }
        else {
            String is_connected = spUtil.getString("is_connected", "0");
            if (is_connected.equals("0")){
                return null;
            }else if (!isCallBackStart){
                p_getData();
                isCallBackStart = true;
            }

        }


        JSONObject object = new JSONObject();
        try {
            if (current_heart_rate < min && current_heart_rate > 0){
                min = current_heart_rate;
            }
            if (current_heart_rate > max){
                max = current_heart_rate;
            }
            object.put("min_heartbeats", min == Integer.MAX_VALUE ? 0 : min);
            object.put("max_heartbeats", max);
            object.put("actual_heartbeats", current_heart_rate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }

    /**
     * 停止心率首页实时心率测量
     */
    @JavascriptInterface
    public void stopHeartScanning(){
        isCallBackStart = false;
        if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice())
            return;
        BlackDeviceManager.getInstance().heartScan(false);
    }

    // 初始化高德
    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(BaseApplication.getBaseApplication());
        AMapLocationClientOption locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    // 定位服务默认配置项
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setNeedAddress(false);//可选，设置是否返回逆地理地址信息。默认是true
        return mOption;
    }

    /**
     * 定位监听
     */
    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    SportLocationTo.HistoryBean historyBean = new SportLocationTo.HistoryBean();
                    historyBean.setTime(DateUtil.getCurrentTime());
                    historyBean.setLatitude(location.getLatitude());
                    historyBean.setLongitude(location.getLongitude());
                    locationHistoryList.add(historyBean);
                }
            }
        }
    };

    private void getSportCode(){
        NetManager.getSportCodeAction(token, new NetManager.NetCallBack<SportCodeBean>() {
            @Override
            public void onResponse(Call<SportCodeBean> call, Response<SportCodeBean> response) {
                SportCodeBean sportCodeBean = response.body();
                if ((sportCodeBean != null ? sportCodeBean.getCode() : 0) == 200){
                    sport_code = String.valueOf(sportCodeBean.getData().getSport_code());

                    // 初始化相关数据对象
                    locations = new SportLocationTo();
                    heartRateTo = new SportHeartRateTo();
                    locationHistoryList = new ArrayList<>();
                    heartRateHistoryList = new ArrayList<>();

                    locations.setSport_code(Integer.valueOf(sport_code));

                    heartRateTo.setSport_code(Integer.valueOf(sport_code));
                } else {
                    Toast.makeText(BaseApplication.getBaseApplication(), "新建运动模式记录失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SportCodeBean> call, Throwable t) {

            }
        });
    }

    private void uploadLocations(){
        if (locations == null)
            return;
        NetManager.uploadSportLocationsAction(token, locations, new NetManager.NetCallBack<MessageBean>() {
            @Override
            public void onResponse(Call<MessageBean> call, Response<MessageBean> response) {
                MessageBean messageBean = response.body();
                if ((messageBean != null ? messageBean.getCode() : 0) == 200){
//                    Toast.makeText(BaseApplication.getBaseApplication(), "数据上传成功！", Toast.LENGTH_SHORT ).show();
                } else {
                    Toast.makeText(BaseApplication.getBaseApplication(), "运动数据上传失败！", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onFailure(Call<MessageBean> call, Throwable t) {

            }
        });
    }

    /**************************************************************************/
    //紫色手环连接相关
    private void p_getData(){
        PurpleBleScan.getInstance().setListener(context, new PurpleBleScan.CurrentBeatsListener() {
            @Override
            public void getCurrentBeats(int step, int rate) {
                current_heart_rate = rate;
                sum_heartbeats += current_heart_rate;
                num_heartbeats ++;
                if (current_heart_rate > max_heart_rate)
                    max_heart_rate = current_heart_rate;
                if (current_heart_rate < min_heart_rate)
                    min_heart_rate = current_heart_rate;

                // 总里程，步长 0.6
                int mileage = (int) (step * 0.6);
                if (start_distance == 0) {
                    start_distance = mileage;
                    return;
                }
                distance = mileage;

                speed = (distance - start_distance) / 1000.0 / ((System.currentTimeMillis() - start_time) / 3600000.0);

                current_steps = step;

                // 本地存储心率数据
                if (currentMilli == 0){
                    currentMilli = System.currentTimeMillis();
                    HeartBeatsHistory history = new HeartBeatsHistory(null, DateUtil.getCurrentTime(), current_heart_rate);
                    HeartBeatsHistoryDao historyDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHeartBeatsHistoryDao();
                    historyDao.insert(history);
                } else if (System.currentTimeMillis() - currentMilli > 5*60*1000){
                    currentMilli = System.currentTimeMillis();
                    HeartBeatsHistory history = new HeartBeatsHistory(null, DateUtil.getCurrentTime(), current_heart_rate);
                    HeartBeatsHistoryDao historyDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHeartBeatsHistoryDao();
                    historyDao.insert(history);
                }
            }
        });
        PurpleBleScan.getInstance().startScan();
    }
    /**************************************************************************/
    //黑色手环相关
    private DataCallback<byte[]> callback = new DataCallback<byte[]>() {
        @Override
        public void OnSuccess(byte[] data) {
            int heartRate = data[1] & 0xff;
            if (heartRate == 0) {
//                BlackDeviceManager.getInstance().startHeartRateListener(callback);
                return;
            }
            current_heart_rate = heartRate;
            sum_heartbeats += current_heart_rate;
            num_heartbeats ++;
            if (current_heart_rate > max_heart_rate)
                max_heart_rate = current_heart_rate;
            if (current_heart_rate < min_heart_rate)
                min_heart_rate = current_heart_rate;

            // 本地存储心率数据
            if (currentMilli == 0){
                currentMilli = System.currentTimeMillis();
                HeartBeatsHistory history = new HeartBeatsHistory(null, DateUtil.getCurrentTime(), current_heart_rate);
                HeartBeatsHistoryDao historyDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHeartBeatsHistoryDao();
                historyDao.insert(history);
            } else if (System.currentTimeMillis() - currentMilli > 5*60*1000){
                currentMilli = System.currentTimeMillis();
                HeartBeatsHistory history = new HeartBeatsHistory(null, DateUtil.getCurrentTime(), current_heart_rate);
                HeartBeatsHistoryDao historyDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHeartBeatsHistoryDao();
                historyDao.insert(history);
            }
        }

        @Override
        public void OnFailed() {

        }

        @Override
        public void OnFinished() {

        }
    };

    private void b_getHeartData(){
        //获取实时心率
        BlackDeviceManager.getInstance().startHeartRateListener(callback);
        //开启心率实时监测
        BlackDeviceManager.getInstance().heartScan(true);
    }

    private void b_openHeartSportScanning(boolean isOpen){
        if (isOpen)
            BlackDeviceManager.getInstance().startHeartSport(true, new DataCallback<Bundle>() {
                @Override
                public void OnSuccess(Bundle data) {
                    // 总步数
                    int stepAll = data.getInt("step", 0);
                    int mileage = data.getInt("distance", 0);
                    // 开始测量时的里程
                    if (start_distance == 0) {
                        start_distance = mileage;
                    }
                    // 总里程
                    distance = mileage;

                    speed = (distance - start_distance) / 1000.0 / ((System.currentTimeMillis() - start_time) / 3600000.0);

                    current_steps = stepAll;

                    int heartRate = data.getInt("heart_rate", 0);
                    if (heartRate == 0) {
                        return;
                    }
                    current_heart_rate = heartRate;
                    sum_heartbeats += current_heart_rate;
                    num_heartbeats ++;
                    if (current_heart_rate > max_heart_rate)
                        max_heart_rate = current_heart_rate;
                    if (current_heart_rate < min_heart_rate)
                        min_heart_rate = current_heart_rate;

                    // 本地存储心率数据
                    if (currentMilli == 0){
                        currentMilli = System.currentTimeMillis();
                        HeartBeatsHistory history = new HeartBeatsHistory(null, DateUtil.getCurrentTime(), current_heart_rate);
                        HeartBeatsHistoryDao historyDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHeartBeatsHistoryDao();
                        historyDao.insert(history);
                    } else if (System.currentTimeMillis() - currentMilli > 5*60*1000){
                        currentMilli = System.currentTimeMillis();
                        HeartBeatsHistory history = new HeartBeatsHistory(null, DateUtil.getCurrentTime(), current_heart_rate);
                        HeartBeatsHistoryDao historyDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHeartBeatsHistoryDao();
                        historyDao.insert(history);
                    }
                }

                @Override
                public void OnFailed() {

                }

                @Override
                public void OnFinished() {

                }
            });
        else
            BlackDeviceManager.getInstance().startHeartSport(false, null);
//        //获取运动数据
//        BlackDeviceManager.getInstance().getDayData(new DataCallback<byte[]>() {
//            @Override
//            public void OnSuccess(byte[] data) {
//                // 总里程
//                int stepAll = FormatUtils.byte2Int(data, 4);
//                int mileage = FormatUtils.byte2Int(data, 12);
//                if (start_distance == 0) {
//                    start_distance = mileage;
//                    return;
//                }
//                distance = mileage;
//
//                speed = (distance - start_distance) / 1000.0 / ((System.currentTimeMillis() - start_time) / 3600000.0);
//
//                current_steps = stepAll;
//            }
//
//            @Override
//            public void OnFailed() {
//
//            }
//
//            @Override
//            public void OnFinished() {
//
//            }
//        });
    }


    /**************************************************************************/
}
