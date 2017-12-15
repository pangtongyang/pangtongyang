package lbcy.com.cn.wristband.global;

/**
 * Created by chenjie on 2017/8/7.
 */

public class Consts {
    //服务器地址
    public static final String BASE_URL = "http://220.180.54.11:8090";
    //固件升级服务器根地址
    public static final String BASE_HARDWARE_URL = "http://bracelet.cositea.com:8089/bracelet/";
    //token前缀
    public static final String PRE_TOKEN_STR = "X-Auth-Token ";
    //临时文件存储文件夹
    public static final String FILE_DOWNLOAD_CACHE_DIR = "cache";
    //固件MCU升级临时文件名
    public static final String FILE_MCU_NAME = "mcu.bin";
    //固件蓝牙升级临时文件名
    public static final String FILE_BLUETOOTH_NAME = "bluetooth.bin";
    //固件硬件升级临时文件名
    public static final String FILE_HARDWARE_NAME = "hardware.bin";

    //紫色手环服务名称
    public static final String PURPLE_SERVICE_ACTION_NAME = "lbcy.com.cn.purplelibrary.service.PurpleBLEService";

    public static final String BLACK_WRISTBAND_NAME = "BC_";
    public static final String PURPLE_WRISTBAND_NAME = "BC-02B";
    public static final String SETTING_DB_NAME = "wisgen_health";
    public static final String USER_DB_NAME = "LBCY_USER_DB";

    public static final String URL_KEY = "url_key";

    //闹钟最大数量
    public static final int CLOCK_MAX_NUM_BLACK = 7;
    public static final int CLOCK_MAX_NUM_PURPLE = 2;


    //Activity request
    public final static int SEDENTARY_TIME_ACTIVITY_REQUEST = 1111;

    //Activity result
    public final static int SEDENTARY_TIME_ACTIVITY_RESULT_UP = 111;
    public final static int SEDENTARY_TIME_ACTIVITY_RESULT_DOWN = 112;


    //订阅消息
    public static final String CLOSE_ALL_ACTIVITY_LISTENER = "CloseAllActivityListener";
    public static final String CLOCK_LISTENER = "ClockListener";
    public static final String ACTIVITY_MANAGE_LISTENER = "ActivityListener";
    public static final String ACTIVITY_SEDENTARY_LISTENER = "SedentaryActivityListener";
    public static final String ACTIVITY_SCAN_HEART_RATE_LISTENER = "ScanHeartRateActivityListener";
    public static final String NOTIFICATION_LISTENER = "NotificationListener";
    // 关闭所有activity
    public static final int CLOSE_ALL_ACTIVITY = 1023;
    // 更新编辑闹钟页文本信息
    public static final int UPDATE_CLOCK_DATA = 1024;
    // 更新闹钟设置页
    public static final int UPDATE_ALL_CLOCK_DATA = 1025;
    // 更新编辑闹钟页类型信息
    public static final int UPDATE_CLOCK_TYPE_DATA = 1026;
    // 关闭Activity
    public static final int CLOSE_ACTIVITY = 1027;
    // 连接手环
    public static final int CONNECT_DEVICE = 1028;
    // 更新久坐提醒时间间隔
    public static final int UPDATE_SEDENTARY_SPACE_TIME = 1029;
    // 更新心率监测时间间隔
    public static final int UPDATE_SCAN_HEART_RATE = 1030;
    // 推送消息通知
    public static final int SHOW_MESSAGE_FROM_APPS = 1031;
    // 打开蓝牙请求
    public static final int REQUEST_ENABLE_BT = 1032;
    // 刷新页面、数据
    public static final int RELOAD_DATA = 1033;
    // 刷新页面
    public static final int REFRESH_PAGE = 1034;

    //推送消息包名
    public final static String QQ = "com.tencent.mobileqq";
    public final static String WEIXIN = "com.tencent.mm";
    public final static String MMS = "com.android.mms";
    public final static String FACEBOOK = "com.facebook.katana";
    public final static String INCALL = "com.android.incallui";
    public final static String NOTRECEIVECALL = "com.android.server.telecom";

    //web页面链接
    public static final String WEB_BASE = "file:///android_asset/www/index.html#";
    //js加载页
    public static final String WEB_TEST = "file:///android_asset/test.html";
    //首页
    public static final String WEB_INDEX = WEB_BASE + "/";
    //运动排名页
    public static final String WEB_SEQUENCE = WEB_BASE + "/sequence";
    //运动历史页
    public static final String WEB_HISTORY = WEB_BASE + "/sport/info/history";
    //心率首页
    public static final String WEB_HEART_RATE_INDEX = WEB_BASE + "/heartbeats/info/current";
    //心率运动模式首页
    public static final String WEB_HEART_RATE_SPORT = WEB_BASE + "/heartbeats/info/sport";
    //心率运动模式实时测心率页
    public static final String WEB_HEART_RATE_TEST = WEB_BASE + "/heartbeats/info/sport/start";
    //心率运动模式详情页
    public static final String WEB_HEART_RATE_DETAIL = WEB_BASE + "/heartbeats/info/sport/detail";
    //周心率数据页
    public static final String WEB_HEART_RATE_HISTORY = WEB_BASE + "/heartbeats/info/history";
    //睡眠首页
    public static final String WEB_SLEEP_INDEX = WEB_BASE + "/sleep/info/current";
    //周睡眠数据页
    public static final String WEB_SLEEP_HISTORY = WEB_BASE + "/sleep/info/history";
    //今日课程页
    public static final String WEB_CLASS_TODAY = WEB_BASE + "/attence";
    //某课程学生考勤情况页
    public static final String WEB_CLASS_ATTEND = WEB_BASE + "/attence/list";
    //本周课程页
    public static final String WEB_CLASS_WEEK = WEB_BASE + "/attence/week";
    //本月考勤页
    public static final String WEB_CLASS_MONTH = WEB_BASE + "/attence/month";
    //本月最早到校时间排名页
    public static final String WEB_CLASS_RANKING = WEB_BASE + "/attence/ranking";
    //运动建议页
    public static final String WEB_EXPERT = WEB_BASE + "/expert";
    //运动视频页
    public static final String WEB_VIDEO = WEB_BASE + "/video/list";
    //健康建议页
    public static final String WEB_HEALTH = WEB_BASE + "/health";
    //健康文章详情页（id为文章id）/health/:id
    //星座页
    public static final String WEB_STAR = WEB_BASE + "/constellation";

    public static final String []WEB_INDEXES = {WEB_INDEX, WEB_HEART_RATE_INDEX, WEB_SLEEP_INDEX,
            WEB_CLASS_TODAY, WEB_CLASS_WEEK, WEB_CLASS_MONTH, WEB_EXPERT, WEB_HEALTH, WEB_STAR};


}
