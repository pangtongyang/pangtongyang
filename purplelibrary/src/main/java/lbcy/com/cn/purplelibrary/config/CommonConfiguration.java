package lbcy.com.cn.purplelibrary.config;

/**
 *
 * 配置文件
 */
public class CommonConfiguration {
    //服务地址
    public static String HTTP_SERVICE_ADDRESS="http://hc.bj35.com/";
    //登录地址
    public static String HTTP_LOGIN_ADDRESS="ashxapis/UserAuth.ashx";
    //获取人员地址
    public static String HTTP_PERSONINFO_ADDRESS="ashxapis/Users.ashx";
    //上传数据至服务器地址
    public static String HTTP_SYNCH_DATA_ADDRESS="RingSensorData.ashx";
    //排行
    public static String HTTP_RANKING_COUNT_ADDRESS="mobilepages/ranking.aspx";
    //专家
//    public static String HTTP_EXPERT_ADDRESS="demopage/mobilepage/expert.html";
    public static String HTTP_EXPERT_ADDRESS="MobilePages/expert.aspx";

    //更新设备
    public static String HTTP_UPDATEDEVICE_ADDRESS="ashxAPIs/AppVersion.ashx";

    //sharedpreferences数据库名
    public static String SHAREDPREFERENCES_NAME="wisgen_health";
    //sqlite数据库名
    public static String SQLITE_DB_NAME="wisgen_health.db";
    //sqlite数据库版本号
    public static int SQLITE_DBVERSION=4;
    //数据库表
//    public static Class<?>[] ENTITY_CLAZZ = { SportInfo.class,
//            SportTarget.class,
//            SportDetails.class,
//            SleepInfo.class,
//            AppPushInfo.class,
//            AlarmClockInfo.class,
//            TargetRemind.class,
//            NotFazeTime.class,
//            HeartRateInfo.class
//    };

    //获取运动数据
    public static String GET_BLE_SPORTSDATA_NOTIFICATION="GET_BLE_SPORTSDATA_NOTIFICATION";
    //刷新运动数据
    public static String RESULT_BLE_SPORTSDATA_NOTIFICATION="RESULT_BLE_SPORTSDATA_NOTIFICATION";
    //刷新睡眠数据
    public static String RESULT_BLE_SLEEPDATA_NOTIFICATION="RESULT_BLE_SLEEPDATA_NOTIFICATION";
    //刷新心率数据
    public static String RESULT_BLE_HEART_RATE_DATA_NOTIFICATION="RESULT_BLE_HEART_RATE_DATA_NOTIFICATION";
    //创建设备连接
    public static String CREATE_CONNECT_DEVICE_NOTIFICATION="CREATE_CONNECT_DEVICE_NOTIFICATION";
    //连接设备
    public static String CONNECT_DEVICE_NOTIFICATION="CONNECT_DEVICE_NOTIFICATION";
    //断开设备
    public static String DIS_CONNECT_DEVICE_NOTIFICATION="DIS_CONNECT_DEVICE_NOTIFICATION";
    //刷新连接设备
    public static String RESULT_CONNECT_DEVICE_NOTIFICATION="RESULT_CONNECT_DEVICE_NOTIFICATION";
    //连接设备失败
    public static String RESULT_FAIL_CONNECT_DEVICE_NOTIFICATION="RESULT_FAIL_CONNECT_DEVICE_NOTIFICATION";
    //推送消息
    public static String PUSH_MESSAGE_NOTIFICATION="PUSH_MESSAGE_NOTIFICATION";
    //进程通知
    public static String TARGET_REMIND_NOTIFICATION="TARGET_REMIND_NOTIFICATION";
    //闹钟提醒
    public static String ALARM_CLOCK_NOTIFICATION="ALARM_CLOCK_NOTIFICATION";
    //设备升级
    public static String TARGET_UPDATEDEVICE_NOTIFICATION="TARGET_UPDATEDEVICE_NOTIFICATION";
    //是否链接
    public static String ISLINK_DEVICE_NOTIFICATION="ISLINK_DEVICE_NOTIFICATION";
    //返回链接结果
    public static String RESULT_ISLINK_DEVICE_NOTIFICATION="RESULT_ISLINK_DEVICE_NOTIFICATION";
    //启动服务
    public static String START_SERVICE_DEVICE_NOTIFICATION="START_SERVICE_DEVICE_NOTIFICATION";
    //升级服务返回值
    public static String RESULT_UPDATE_DEVICE_NOTIFICATION="RESULT_UPDATE_DEVICE_NOTIFICATION";


    //判断走步行，跑步
    public static int STEP_600=600;
    public static int STEP_900=900;

    //运动类型系数
    public static float SPORT_TYPE_WALK=0.25f;
    public static float SPORT_TYPE_SLOW_RUN=0.5f;
    public static float SPORT_TYPE_RUN=0.7f;

    //身高
    public static float PERSON_HEIGHT=1.7f;

    //步行，跑步时间系数
    public static float SPORT_WALK_TIME_COEFFICIENT=0.011f;
    public static float SPORT_RUN_TIME_COEFFICIENT=0.0075f;

    public static long SYNC_DATA_TIME=5*60*1000l;


}
