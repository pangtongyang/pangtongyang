package lbcy.com.cn.wristband.service;

import lbcy.com.cn.wristband.entity.BasicBodyBean;
import lbcy.com.cn.wristband.entity.BasicBodyData;
import lbcy.com.cn.wristband.entity.HardwareUpdateBean;
import lbcy.com.cn.wristband.entity.HelpBean;
import lbcy.com.cn.wristband.entity.LoginBean;
import lbcy.com.cn.wristband.entity.LoginTo;
import lbcy.com.cn.wristband.entity.MessageDetailBean;
import lbcy.com.cn.wristband.entity.MessageListBean;
import lbcy.com.cn.wristband.entity.SportStatisticsBean;
import lbcy.com.cn.wristband.entity.MessageBean;
import lbcy.com.cn.wristband.entity.SportStepsTo;
import lbcy.com.cn.wristband.entity.UserInfoBean;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    // 获取黑色手环升级列表
    @GET("bracelet_version")
    Call<HardwareUpdateBean> getUpdateList(@Query("product")String product,
                                           @Query("softversion")String softversion,
                                           @Query("hardversion")String hardversion,
                                           @Query("blueversion")String blueversion,
                                           @Query("language")String language);
}
