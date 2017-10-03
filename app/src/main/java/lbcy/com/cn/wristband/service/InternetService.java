package lbcy.com.cn.wristband.service;

import lbcy.com.cn.wristband.entity.LoginBean;
import lbcy.com.cn.wristband.entity.LoginTo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by chenjie on 2017/9/30.
 */

public interface InternetService {
    //登录请求
    @POST("login")
    Call<LoginBean> login(@Body LoginTo loginTo);
}
