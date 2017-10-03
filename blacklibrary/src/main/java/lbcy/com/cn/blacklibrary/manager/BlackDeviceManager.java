package lbcy.com.cn.blacklibrary.manager;

import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.text.TextUtils;

import com.huichenghe.bleControl.Ble.BleDataForBattery;
import com.huichenghe.bleControl.Ble.BleDataForDayData;
import com.huichenghe.bleControl.Ble.BleDataForEachHourData;
import com.huichenghe.bleControl.Ble.BleDataForFactoryReset;
import com.huichenghe.bleControl.Ble.BleDataForHRWarning;
import com.huichenghe.bleControl.Ble.BleDataForQQAndOtherRemine;
import com.huichenghe.bleControl.Ble.BleDataForRingDelay;
import com.huichenghe.bleControl.Ble.BleDataForSettingArgs;
import com.huichenghe.bleControl.Ble.BleDataForSettingParams;
import com.huichenghe.bleControl.Ble.BleDataForSleepData;
import com.huichenghe.bleControl.Ble.BleDataForTarget;
import com.huichenghe.bleControl.Ble.BleDataforSyn;
import com.huichenghe.bleControl.Ble.BleForFindDevice;
import com.huichenghe.bleControl.Ble.BleForPhoneAndSmsRemind;
import com.huichenghe.bleControl.Ble.BleForQQWeiChartFacebook;
import com.huichenghe.bleControl.Ble.BleForSitRemind;
import com.huichenghe.bleControl.Ble.BleGattHelperListener;
import com.huichenghe.bleControl.Ble.BleReadDeviceMenuState;
import com.huichenghe.bleControl.Ble.BluetoothLeService;
import com.huichenghe.bleControl.Ble.DataSendCallback;
import com.huichenghe.bleControl.Ble.LocalDeviceEntity;
import com.huichenghe.bleControl.BleGattHelper;
import com.huichenghe.bleControl.Entity.sitRemindEntity;

import java.io.UnsupportedEncodingException;

import lbcy.com.cn.blacklibrary.ble.DataCallback;
import lbcy.com.cn.blacklibrary.ctl.DeviceController;

import static lbcy.com.cn.blacklibrary.utils.Util.getDataBinString;

/**
 * Created by chenjie on 2017/8/6.
 */

public class BlackDeviceManager implements DeviceController {
    private final static String QQ = "com.tencent.mobileqq";
    private final static String WEIXIN = "com.tencent.mm";
    private final static String MMS = "com.android.mms";
    private final static String FACEBOOK = "com.facebook.katana";

    private static volatile DeviceController manager;
    private Context mContext;

    private BlackDeviceManager() {

    }

    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    public static DeviceController getInstance() {
        if (manager == null) {
            synchronized (BlackDeviceManager.class) {
                if (manager == null) {
                    manager = new BlackDeviceManager();
                }
            }
        }
        return manager;
    }

    @Override
    public void findDevice(boolean isFind, final DataCallback callback) {
        BleForFindDevice.getBleForFindDeviceInstance().setListener(new DataSendCallback() {
            @Override
            public void sendSuccess(byte[] bytes) {
                callback.OnSuccess(bytes);
            }

            @Override
            public void sendFailed() {

            }

            @Override
            public void sendFinished() {

            }
        });
        BleForFindDevice.getBleForFindDeviceInstance().findConnectedDevice(isFind?(byte)0x00:(byte)0x01);
    }

    @Override
    public void getDayData(final DataCallback callback) {
        BleDataForDayData.getDayDataInstance(mContext).setOnDayDataListener(new DataSendCallback() {
            @Override
            public void sendSuccess(byte[] receiveData) {

                callback.OnSuccess(receiveData);
//                System.out.println("hhp:" + FormatUtils.bytesToHexString(receiveData));
//                int stepAll = FormatUtils.byte2Int(receiveData, 4);
//                int calorie = FormatUtils.byte2Int(receiveData, 8);
//                int mileage = FormatUtils.byte2Int(receiveData, 12);
//                int movementTime = FormatUtils.byte2Int(receiveData, 16);
//                int moveCalorie = FormatUtils.byte2Int(receiveData, 20);
//                int sitTime = FormatUtils.byte2Int(receiveData, 24);
//                int sitCalorie = FormatUtils.byte2Int(receiveData, 28);

//                showdatastring("总步数：" + stepAll + "," + "总卡路里：" + calorie + "," + "总里程：" + mileage + "," + "活动时间：" + movementTime + "," + "活动消耗热量：" + moveCalorie + "," + "静坐时间：" + sitTime + "," + "静坐卡路里：" + sitCalorie);
            }

            @Override
            public void sendFailed() {

            }

            @Override
            public void sendFinished() {

            }
        });
        BleDataForDayData.getDayDataInstance(mContext).getDayData();
    }

    @Override
    public void getBattery(final DataCallback callback) {
        BleDataForBattery.getInstance().setBatteryListener(new DataSendCallback() {
            @Override
            public void sendSuccess(byte[] receiveData) {
                callback.OnSuccess(receiveData);
            }

            @Override
            public void sendFailed() {

            }

            @Override
            public void sendFinished() {

            }
        });
//        BleDataForBattery.getmCurrentBattery();
        BleDataForBattery.getInstance().getBatteryPx();
    }

    @Override
    public void getEachHourStep(final DataCallback callback) {
        BleDataForEachHourData.getEachHourDataInstance().setOnBleDataReceListener(new DataSendCallback() {
            @Override
            public void sendSuccess(byte[] receiveData) {
                callback.OnSuccess(receiveData);
//                Int step1= FormatUtils.byte2Int(eachData, 4);   step1获取凌晨到1点的步数
//                Int step2=FormatUtils.byte2Int(eachData, 8);    step2获取1点到2点的步数
//                Int step3=FormatUtils.byte2Int(eachData, 12);
//                Int step4=FormatUtils.byte2Int(eachData, 16);
//.............................................
//                Int cal1= FormatUtils.byte2Int(eachData, 100);  获取凌晨点到1点的卡路里
//                Int cal2= FormatUtils.byte2Int(eachData, 104);
//                Int cal3= FormatUtils.byte2Int(eachData, 108);

//                showdatastring("第一小时步数：" + step1 + "，" + "第八小时步数：" + step9 + "；" + "第一小时卡路里：" + cal1);

            }

            @Override
            public void sendFailed() {
            }

            @Override
            public void sendFinished() {
//                handler.sendEmptyMessageDelayed(2, 600);
            }
        });
        BleDataForEachHourData.getEachHourDataInstance().getEachData();
    }

    @Override
    public void getSleepData(final DataCallback callback) {
        BleDataForSleepData.getInstance(mContext).setOnSleepDataRecever(new DataSendCallback() {
            @Override
            public void sendSuccess(byte[] receiveData) {
                callback.OnSuccess(receiveData);
//                String stralldata = getsleepdata(receveData);
//
//                // 截取昨天的数据, 从数据末尾截取12个字符，代表两个小时
//                String y = "";
//                if (stralldata.length() > 1) {
//                    String res = stralldata.replaceAll("\\d{12}$", "");
//                    y = stralldata.replaceAll(res, "");
//                }
//                // 截取今天的数据， 从数据开头截取60个字符，代表10小时
//                String t = "";
//                if (stralldata.length() > 0) {
//                    t = stralldata.substring(0, 60);
//                }
//                String allData = y + t;
//                int start = 0;
//                String first = allData.replaceAll("^[0, 3]+", "");
//                start = allData.length() - first.length();
//                String second = allData.replaceAll("[0, 3]+$", "");
//                int end = allData.length() - second.length();
//                final String finalString = first.replaceAll("[0, 3]+$", "");
//                Log.i("huang", "全部数据开始位置：" + start);
//                Log.i("huang", "全部数据结束位置：" + end);
//                //05-11 11:05:27.119: I/SleepDataHelper(25991): 全部数据最终数据：22322222222222222222233222212222232322222222
//
//                showdatastring(finalString);

            }

            @Override
            public void sendFailed() {
            }

            @Override
            public void sendFinished() {

            }
        });
        BleDataForSleepData.getInstance(mContext).getSleepingData();
    }

    @Override
    public void phoneRemind(boolean isOn) {
        if (isOn) {
            BleForPhoneAndSmsRemind.getInstance().openPhoneRemine((byte) 0x03, (byte) 0x01);
        } else {
            BleForPhoneAndSmsRemind.getInstance().closeTheRemind();
        }
    }

    @Override
    public void isPhoneRemind(boolean isCalled, String phoneNum, String name) {
        if (isCalled) {
            BleForPhoneAndSmsRemind.getInstance().beginPhoneRemind(phoneNum, name, BleForPhoneAndSmsRemind.startPhoneRemindToDevice);
        } else {
            BleForPhoneAndSmsRemind.getInstance().beginPhoneRemind(phoneNum, name, BleForPhoneAndSmsRemind.phoneRemindFromDevice);
        }
    }

    @Override
    public void synTime(final DataCallback callback) {
        BleDataforSyn syn = BleDataforSyn.getSynInstance();
        syn.setDataSendCallback(new DataSendCallback() {
            @Override
            public void sendSuccess(byte[] receveData) {
                callback.OnSuccess(receveData);
            }

            @Override
            public void sendFailed() {
            }

            @Override
            public void sendFinished() {
            }
        });
        syn.syncCurrentTime();

    }

    @Override
    public void setTimeStyle(boolean is24, DataCallback callback) {
        BleDataForSettingArgs setArgs = BleDataForSettingArgs.getInstance(mContext);
        setArgs.setDataSendCallback(new DataSendCallback() {
            @Override
            public void sendSuccess(byte[] receveData) {
            }

            @Override
            public void sendFailed() {
            }

            @Override
            public void sendFinished() {
            }
        });
        setArgs.setArgs("0x00", is24);
    }

    @Override
    public void factoryReset() {
        BleDataForFactoryReset.getBleDataInstance().settingFactoryReset();
    }

    @Override
    public void startHeartRateListener(DataCallback callback) {

        BluetoothLeService.getInstance().addCallback(
                BleGattHelper.getInstance(mContext, new gattHelperListener(callback)));
    }

    private class gattHelperListener implements BleGattHelperListener {
        DataCallback callback;
        private gattHelperListener(DataCallback callback){
            this.callback = callback;
        }

        @Override
        public void onDeviceStateChangeUI(LocalDeviceEntity device,
                                          BluetoothGatt gatt,
                                          final String uuid, final byte[] value) {
            callback.OnSuccess(value);
        }

        @Override
        public void onDeviceConnectedChangeUI(final LocalDeviceEntity device,
                                              boolean showToast,
                                              final boolean fromServer) {

        }
    }

    @Override
    public void setTarget(int stepTarget, int sleepTimes, int sleepHour, int sleepMinute) {
        BleDataForTarget.getInstance().sendTargetToDevice(stepTarget, sleepTimes, sleepHour, sleepMinute);
    }

    @Override
    public void setHeartRateFreq(int time) {
        BleDataForSettingArgs.getInstance(mContext).setHeartReatArgs((byte)(int)time);
    }

    @Override
    public void setHeartRateWarning(int maxHR, int minHR) {
        BleDataForHRWarning.getInstance().closeOrOpenWarning( maxHR, minHR, (byte)0x00);
    }

    @Override
    public void setBodyItem(String hei, String wei, String gen, String bir) {
        new BleDataForSettingParams(mContext)
                .settingTheStepParamsToBracelet(hei, wei, gen, bir);
    }

    @Override
    public void setDeviceMenuState(boolean isOpen, int position) {
        int allData =  0;
        String all =getDataBinString(allData, position, isOpen);
        int dataInt = Integer.parseInt(all, 2);
        BleReadDeviceMenuState.getInstance().sendUpdateSwitchData32(dataInt);

    }

    @Override
    public void setAllMenuState(boolean []data) {
        for (int i = 0; i< data.length; i++) {
            setDeviceMenuState(data[i], i);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void ringDelay(int time) {
        BleDataForRingDelay.getDelayInstance().settingDelayData(time);
    }

    @Override
    public void setSitRemind(int number, int isOpen, String beginTime, String endTime, int duration) {
        BleForSitRemind.getInstance().setSitData(new sitRemindEntity(number, isOpen, beginTime, endTime, duration));
    }

    @Override
    public void deleteSitRemind(int number) {
        BleForSitRemind.getInstance().deleteThisItem(number);
    }

    @Override
    public void setAppNotification(String type, String title, String content) {
        //使能开关，0x01代表开
        BleForQQWeiChartFacebook.getInstance().openRemind((byte)0x0a, (byte)0x01);
        byte mType;
        switch (type){
            case FACEBOOK:
                mType = 0x03;
                break;
            case MMS:
                mType = 0x04;
                break;
            case WEIXIN:
                mType = 0x01;
                break;
            case QQ:
                mType = 0x02;
                break;
            default:
                mType = 0x00;
                break;
        }

        String texts;
        if (TextUtils.isEmpty(title)){
            texts = content;
        } else if (TextUtils.isEmpty(content)){
            texts = title;
        } else {
            texts = title + ":" + content;
        }

        if (mType == 0x01){
            texts = "来短信啦";
        }

        if (TextUtils.isEmpty(texts)) {
            return;
        }
        byte[] dataContent = getCanSendByte(texts);
        if (dataContent != null && mType != 0x00){
            BleDataForQQAndOtherRemine.getIntance().sendMessageToDevice(mType, dataContent);
        }

    }

    private byte[] getCanSendByte(String text) {
        int max = 32;
        byte[] dataContent = null;
        try {
            dataContent = text.getBytes("utf-8");
            if (dataContent.length > max) {
                text = text.substring(0, max / 3);
                dataContent = text.getBytes("utf-8");
            } else {
                dataContent = text.getBytes("utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return dataContent;
    }
}
