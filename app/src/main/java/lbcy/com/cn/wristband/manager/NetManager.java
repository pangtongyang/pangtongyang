package lbcy.com.cn.wristband.manager;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.activity.LoginActivity;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.entity.BasicBodyBean;
import lbcy.com.cn.wristband.entity.BasicBodyData;
import lbcy.com.cn.wristband.entity.HardwareUpdateBean;
import lbcy.com.cn.wristband.entity.HeartBeatsAllDayDataTo;
import lbcy.com.cn.wristband.entity.HelpBean;
import lbcy.com.cn.wristband.entity.LoginBean;
import lbcy.com.cn.wristband.entity.LoginTo;
import lbcy.com.cn.wristband.entity.MessageDetailBean;
import lbcy.com.cn.wristband.entity.MessageListBean;
import lbcy.com.cn.wristband.entity.MobileBean;
import lbcy.com.cn.wristband.entity.MobileTo;
import lbcy.com.cn.wristband.entity.SleepTo;
import lbcy.com.cn.wristband.entity.SportCodeBean;
import lbcy.com.cn.wristband.entity.SportCodeTo;
import lbcy.com.cn.wristband.entity.SportLocationTo;
import lbcy.com.cn.wristband.entity.SportStatisticsBean;
import lbcy.com.cn.wristband.entity.MessageBean;
import lbcy.com.cn.wristband.entity.SportStepsTo;
import lbcy.com.cn.wristband.entity.SportTo;
import lbcy.com.cn.wristband.entity.UserInfoBean;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxBus;
import lbcy.com.cn.wristband.service.InternetService;
import lbcy.com.cn.wristband.utils.DateUtil;
import lbcy.com.cn.wristband.utils.HandlerTip;
import lbcy.com.cn.wristband.utils.http.OkHttp3Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chenjie on 2017/9/30.
 */

public class NetManager {
    private static Retrofit mRetrofit;
    private static OkHttpClient mOkHttpClient;
    private static InternetService service;

    // 服务方法
    private static InternetService getService() {

        if (null == mRetrofit) {

            if (null == mOkHttpClient) {
                mOkHttpClient = OkHttp3Utils.getOkHttpClient();
            }

            synchronized (NetManager.class){
                if (null == mRetrofit){
                    //Retrofit2后使用build设计模式
                    mRetrofit = new Retrofit.Builder()
                            //设置服务器路径
                            .baseUrl(Consts.BASE_URL)
                            //添加转化库，默认是Gson
                            .addConverterFactory(GsonConverterFactory.create())
                            //添加回调库，采用RxJava
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            //设置使用okhttp网络请求
                            .client(mOkHttpClient)
                            .build();

                    service = mRetrofit.create(InternetService.class);
                }
            }



        }

        return service;
    }

    /**
     * 回调接口
     * @param <T> 模板
     */
    public interface NetCallBack<T>{
        void onResponse(Call<T> call, Response<T> response);
        void onFailure(Call<T> call, Throwable t);
    }

    /**
     * 回调模板类
     * @param <T> 模板
     */
    private static class CallBack<T> implements Callback<T>{
        NetCallBack<T> callBack;
        private CallBack(NetCallBack<T> callBack){
            this.callBack = callBack;
        }

        @Override
        public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
            if (response.isSuccessful()){
                if (isTokenTimeout(response))
                    return;
                callBack.onResponse(call, response);
            } else {
                Toast.makeText(BaseApplication.getBaseApplication(), BaseApplication.getBaseApplication().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
            Toast.makeText(BaseApplication.getBaseApplication(), BaseApplication.getBaseApplication().getString(R.string.network_timeout), Toast.LENGTH_SHORT).show();
            callBack.onFailure(call, t);
        }
    }

    /**
     * 判断token是否有效
     * @param response 回调值
     * @return true -> 有效， false -> 失效
     */
    private static boolean isTokenTimeout(Response response){
        if (response.code() == 401 && response.message().equals("Unauthorized")){
            Toast.makeText(BaseApplication.getBaseApplication(), "登录失效，请重新登录！", Toast.LENGTH_SHORT).show();

            HandlerTip.getInstance().postDelayed(300, () -> {
                Message message = new Message();
                message.what = Consts.CLOSE_ALL_ACTIVITY;
                RxBus.getInstance().post(Consts.CLOSE_ALL_ACTIVITY_LISTENER, message);
                Intent intent = new Intent(BaseApplication.getBaseApplication(), LoginActivity.class);
                BaseApplication.getBaseApplication().startActivity(intent);
            });
            return true;
        }
        return false;
    }

    /**
     * 登录
     * @param loginTo 用户名密码
     * @param callBack 回调
     */
    public static void loginAction(LoginTo loginTo, NetCallBack<LoginBean> callBack){
        getService().login(loginTo).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(@NonNull Call<LoginBean> call, @NonNull Response<LoginBean> response) {
                if (response.isSuccessful()){
                    callBack.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginBean> call, @NonNull Throwable t) {
                Toast.makeText(BaseApplication.getBaseApplication(), BaseApplication.getBaseApplication().getString(R.string.network_timeout), Toast.LENGTH_SHORT).show();
                callBack.onFailure(call, t);
            }
        });
    }

    /**
     * 获取运动统计信息
     * @param token 登录标识
     * @param callBack 回调
     */
    public static void getSportHistoryAction(String token, NetCallBack<SportStatisticsBean> callBack){
        getService().getSportHistory(token).enqueue(new CallBack<>(callBack));
    }

    /**
     * 设置运动目标
     * @param token 登录标识
     * @param sportStepsTo 运动目标
     * @param callBack 回调
     */
    public static void stepsSetAction(String token, SportStepsTo sportStepsTo, NetCallBack<MessageBean> callBack){
        getService().stepsSet(token, sportStepsTo).enqueue(new CallBack<>(callBack));
    }

    /**
     * 获取身高体重
     * @param token 登录标识
     * @param callBack 回调
     */
    public static void getBodyDataAction(String token, NetCallBack<BasicBodyBean> callBack){
        CallBack<BasicBodyBean> mCallBack = new CallBack<>(callBack);
        getService().getBodyData(token).enqueue(mCallBack);
    }

    /**
     * 设置身高体重
     * @param token 登录标识
     * @param basicBodyData 身高体重
     * @param callBack 回调
     */
    public static void setBodyDataAction(String token, BasicBodyData basicBodyData, NetCallBack<MessageBean> callBack){
        getService().setBodyData(token, basicBodyData).enqueue(new CallBack<>(callBack));
    }

    /**
     * 获取用户基本信息
     * @param token 登录标识
     * @param callBack 回调
     */
    public static void getUserInfoAction(String token, NetCallBack<UserInfoBean> callBack){
        getService().getUserInfo(token).enqueue(new CallBack<>(callBack));
    }

    /**
     * 获取帮助说明
     * @param token 登录标识
     * @param callBack 回调
     */
    public static void getHelpInfoAction(String token, NetCallBack<HelpBean> callBack){
        getService().getHelpInfo(token).enqueue(new CallBack<>(callBack));
    }

    /**
     * 获取消息列表
     * @param token 登录标识
     * @param callBack 回调
     */
    public static void getMessageListAction(String token, NetCallBack<MessageListBean> callBack){
        getService().getMessageList(token).enqueue(new CallBack<>(callBack));
    }

    /**
     * 获取消息数据
     * @param token 登录标识
     * @param id 消息id
     * @param callBack 回调
     */
    public static void getMessageDataAction(String token, int id, NetCallBack<MessageDetailBean> callBack){
        getService().getMessageData(token,id).enqueue(new CallBack<>(callBack));
    }

    /**
     *
     * @param token 登录标识
     * @param phoneNum 手机号
     * @param callBack 回调
     */
    public static void getMobileCodeAction(String token, String phoneNum, NetCallBack<MobileBean> callBack){
        getService().getMobileCode(token, phoneNum).enqueue(new CallBack<>(callBack));
    }

    /**
     *
     * @param token 登录标识
     * @param mobileTo 手机号、验证码
     * @param callBack 回调
     */
    public static void bindPhoneAction(String token, MobileTo mobileTo, NetCallBack<MobileBean> callBack){
        getService().bindPhone(token, mobileTo).enqueue(new CallBack<>(callBack));
    }

    /**
     * 获取黑色手环升级列表
     * @param product 产品名，默认bjhc
     * @param softversion 当前MCU版本
     * @param hardversion 当前硬件版本
     * @param blueversion 当前蓝牙版本
     * @param language 语言，默认zh-cn
     * @param callBack 回调函数
     */
    public static void getUpdateListAction(String product,String softversion,
                                           String hardversion,String blueversion,
                                           String language,NetCallBack<HardwareUpdateBean> callBack){
        Retrofit retrofit = new Retrofit.Builder()
                //设置服务器路径
                .baseUrl(Consts.BASE_HARDWARE_URL)
                //添加转化库，默认是Gson
                .addConverterFactory(GsonConverterFactory.create())
                //添加回调库，采用RxJava
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //设置使用okhttp网络请求
                .client(mOkHttpClient)
                .build();

        InternetService mService = retrofit.create(InternetService.class);
        mService.getUpdateList(product, softversion, hardversion, blueversion, language).enqueue(new CallBack<>(callBack));
    }

    /**
     * 上传运动数据
     * @param token 登录标识
     * @param sportTo 运动数据
     * @param callBack 回调
     */
    public static void uploadSportDataAction(String token, SportTo sportTo, NetCallBack<MessageBean> callBack){
        // 每晚0点到1点，不上传数据
        if (DateUtil.getCurrentTime_H_m().compareTo("00:00") >= 0 && DateUtil.getCurrentTime_H_m().compareTo("01:00") <= 0)
            return;
        getService().uploadSportData(token, sportTo).enqueue(new CallBack<>(callBack));
    }

    /**
     * 上传运动模式坐标详情
     * @param token 登录标识
     * @param locations 距离
     * @param callBack 回调
     */
    public static void uploadSportLocationsAction(String token, SportLocationTo locations, NetCallBack<MessageBean> callBack){
        // 每晚0点到1点，不上传数据
        if (DateUtil.getCurrentTime_H_m().compareTo("00:00") >= 0 && DateUtil.getCurrentTime_H_m().compareTo("01:00") <= 0)
            return;
        getService().uploadSportLocations(token, locations).enqueue(new CallBack<>(callBack));
    }

    /**
     * 上传睡眠数据
     * @param token 登录标识
     * @param sleepTo 睡眠数据
     * @param callBack 回调
     */
    public static void uploadSleepDataAction(String token, SleepTo sleepTo, NetCallBack<MessageBean> callBack){
        // 每晚0点到1点，不上传数据
        if (DateUtil.getCurrentTime_H_m().compareTo("00:00") >= 0 && DateUtil.getCurrentTime_H_m().compareTo("01:00") <= 0)
            return;
        getService().uploadSleepData(token, sleepTo).enqueue(new CallBack<>(callBack));
    }

    /**
     * 获取个人足迹网络链接地址
     * @param token 登录标识
     * @param callBack 回调
     */
    public static void getUserPathAction(String token, NetCallBack<MessageBean> callBack){
        getService().getUserPath(token).enqueue(new CallBack<>(callBack));
    }

    /**
     * 新建一条运动模式记录
     * @param token 登录标识
     * @param callBack 回调
     */
    public static void getSportCodeAction(String token, NetCallBack<SportCodeBean> callBack){
        String date = DateUtil.getCurrentTime();
        SportCodeTo sportCodeTo = new SportCodeTo();
        sportCodeTo.setDate(date);
        getService().getSportCode(token, sportCodeTo).enqueue(new CallBack<>(callBack));
    }

    /**
     * 上传头像（表单）
     * @param token 登录标识
     * @param path 头像本地存储路径
     * @param callBack 回调
     */
    public static void uploadAvatarAction(String token, String path, NetCallBack<MessageBean> callBack){
        File file = new File(path);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // 生成表单
        builder.addFormDataPart("img", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        List<MultipartBody.Part> parts = builder.build().parts();
        getService().uploadAvatar(token, parts).enqueue(new CallBack<>(callBack));
    }

    /**
     * 上传指定日期心率数据
     * @param token 登录标识
     * @param dayDataTo 上传数据
     * @param callBack 回调
     */
    public static void uploadAllDayHeartBeats(String token, HeartBeatsAllDayDataTo dayDataTo, NetCallBack<MessageBean> callBack){
        // 每晚0点到1点，不上传数据
        if (DateUtil.getCurrentTime_H_m().compareTo("00:00") >= 0 && DateUtil.getCurrentTime_H_m().compareTo("01:00") <= 0)
            return;
        getService().uploadAllDayHeartRate(token, dayDataTo).enqueue(new CallBack<>(callBack));
    }
}
