package lbcy.com.cn.wristband.manager;

import lbcy.com.cn.wristband.entity.LoginBean;
import lbcy.com.cn.wristband.entity.LoginTo;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.service.InternetService;
import lbcy.com.cn.wristband.utils.http.OkHttp3Utils;
import okhttp3.OkHttpClient;
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

    //登录服务
    public static Call<LoginBean> loginAction(LoginTo loginTo){

        return getService().login(loginTo);
    }
}
