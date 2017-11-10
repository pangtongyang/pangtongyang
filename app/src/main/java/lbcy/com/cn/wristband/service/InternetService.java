package lbcy.com.cn.wristband.service;

import org.json.JSONObject;

import java.util.List;

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
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by chenjie on 2017/9/30.
 */

public interface InternetService {
    // 登录请求
    @POST("login")
    Call<LoginBean> login(@Body LoginTo loginTo);

    // 获取运动统计信息
    @GET("sport/statistics")
    Call<SportStatisticsBean> getSportHistory(@Header("Authorization") String authorization);

    // 运动目标设定
    @POST("sport/goal")
    Call<MessageBean> stepsSet(@Header("Authorization") String authorization, @Body SportStepsTo sportStepsTo);

    // 获取身高体重
    @GET("body/detail")
    Call<BasicBodyBean> getBodyData(@Header("Authorization") String authorization);

    // 设置身高体重
    @POST("body/detail")
    Call<MessageBean> setBodyData(@Header("Authorization") String authorization, @Body BasicBodyData basicBodyData);

    // 获取用户基本信息
    @GET("user/info")
    Call<UserInfoBean> getUserInfo(@Header("Authorization") String authorization);

    // 获取帮助说明
    @GET("question/list")
    Call<HelpBean> getHelpInfo(@Header("Authorization") String authorization);

    // 获取消息列表
    @GET("message")
    Call<MessageListBean> getMessageList(@Header("Authorization") String authorization);

    // 获取消息详情
    @GET("message")
    Call<MessageDetailBean> getMessageData(@Header("Authorization") String authorization, @Query("id")int id);

    // 获取手机验证码
    @GET("MobileBinding")
    Call<MobileBean> getMobileCode(@Header("Authorization") String authorization, @Query("mobile")String phone);

    // 绑定手机号
    @POST("MobileBinding")
    Call<MobileBean> bindPhone(@Header("Authorization") String authorization, @Body MobileTo mobileTo);

    // 获取黑色手环升级列表
    @GET("bracelet_version")
    Call<HardwareUpdateBean> getUpdateList(@Query("product")String product,
                                           @Query("softversion")String softversion,
                                           @Query("hardversion")String hardversion,
                                           @Query("blueversion")String blueversion,
                                           @Query("language")String language);

    // 上传运动数据
    @POST("sport/info/current")
    Call<MessageBean> uploadSportData(@Header("Authorization") String authorization,  @Body SportTo sportTo);

    // 上传运动模式坐标详情
    @POST("heartbeats/sport/history/coordinates")
    Call<MessageBean> uploadSportLocations(@Header("Authorization") String authorization, @Body SportLocationTo sportLocationTo);

    // 上传睡眠数据
    @POST("sleep/info/current")
    Call<MessageBean> uploadSleepData(@Header("Authorization") String authorization, @Body SleepTo sleepTo);

    // 获取个人足迹网络链接地址
    @GET("UserInArea")
    Call<MessageBean> getUserPath(@Header("Authorization") String authorization);

    // 新建一条运动模式记录
    @POST("heartbeats/sport/history")
    Call<SportCodeBean> getSportCode(@Header("Authorization") String authorization, @Body SportCodeTo codeTo);

    // 上传头像
    @Multipart
    @POST("user/logo/form")
    Call<MessageBean> uploadAvatar(@Header("Authorization") String authorization, @Part List<MultipartBody.Part> partList);

    // 上传指定日期心率数据
    @POST("heartbeats/info/current")
    Call<MessageBean> uploadAllDayHeartRate(@Header("Authorization") String authorization, @Body HeartBeatsAllDayDataTo dayDataTo);
}
