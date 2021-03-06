package lbcy.com.cn.blacklibrary.manager;

import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.huichenghe.bleControl.Ble.BleDataForBattery;
import com.huichenghe.bleControl.Ble.BleDataForCustomRemind;
import com.huichenghe.bleControl.Ble.BleDataForDayData;
import com.huichenghe.bleControl.Ble.BleDataForDayHeartReatData;
import com.huichenghe.bleControl.Ble.BleDataForEachHourData;
import com.huichenghe.bleControl.Ble.BleDataForFactoryReset;
import com.huichenghe.bleControl.Ble.BleDataForHRWarning;
import com.huichenghe.bleControl.Ble.BleDataForHardVersion;
import com.huichenghe.bleControl.Ble.BleDataForOnLineMovement;
import com.huichenghe.bleControl.Ble.BleDataForQQAndOtherRemine;
import com.huichenghe.bleControl.Ble.BleDataForRingDelay;
import com.huichenghe.bleControl.Ble.BleDataForSettingArgs;
import com.huichenghe.bleControl.Ble.BleDataForSettingParams;
import com.huichenghe.bleControl.Ble.BleDataForSleepData;
import com.huichenghe.bleControl.Ble.BleDataForTarget;
import com.huichenghe.bleControl.Ble.BleDataforSyn;
import com.huichenghe.bleControl.Ble.BleForFindDevice;
import com.huichenghe.bleControl.Ble.BleForLIftUpRemind;
import com.huichenghe.bleControl.Ble.BleForLostRemind;
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
import com.huichenghe.bleControl.Utils.FormatUtils;
import com.huichenghe.bleControl.upgrande.UpdateVersionTask;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    public void findDevice(boolean isFind, final DataCallback<byte[]> callback) {
        if (BleForFindDevice.getBleForFindDeviceInstance() == null)
            return;
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
        BleForFindDevice.getBleForFindDeviceInstance().findConnectedDevice(isFind ? (byte) 0x00 : (byte) 0x01);
    }

    @Override
    public void getDayData(final DataCallback<byte[]> callback) {

        if (BleDataForDayData.getDayDataInstance(mContext) == null)
            return;
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
    public void getBattery(final DataCallback<byte[]> callback) {
//        Log.e("aaaaaa", Looper.myLooper() == Looper.getMainLooper() ? "1" : "0");
        if (BleDataForBattery.getInstance() == null)
            return;
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
    public void getEachHourStep(final DataCallback<byte[]> callback) {
        if (BleDataForEachHourData.getEachHourDataInstance() == null)
            return;
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
    public void getSleepData(final DataCallback<String> callback) {
        if (BleDataForSleepData.getInstance(mContext) == null)
            return;
        BleDataForSleepData.getInstance(mContext).setOnSleepDataRecever(new DataSendCallback() {
            @Override
            public void sendSuccess(byte[] receiveData) {

                StringBuilder dataString = new StringBuilder();
                for (int i = 4; i < 40; i++)        // 遍历字节数组
                {
                    byte buffer = receiveData[i];     // 取出一个字节赋值给buffer

                    for (int j = 0; j < 4; j++)     // 循环取出两位
                    {
                        byte a = (byte) ((buffer >> (j * 2)) & (byte) 0x03);   // 取出两位
                        int ai = a & (byte) 0x03;
                        dataString.append(String.valueOf(ai));
                    }
                }

                // 截取昨天的数据, 从数据末尾截取12个字符，代表两个小时
                String y = "";
                if (dataString.length() > 1) {
                    String res = dataString.toString().replaceAll("\\d{12}$", "");
                    y = dataString.toString().replaceAll(res, "");
                }
                // 截取今天的数据， 从数据开头截取60个字符，代表10小时
                String t = "";
                if (dataString.length() > 0) {
                    t = dataString.substring(0, 60);
                }
                String allData = y + t;

                int day = (receiveData[0] & 0xff) - 1;
                int month = (receiveData[1] & 0xff);
                int year = (receiveData[2] & 0xff) + 2000;
                Calendar calendarData = Calendar.getInstance();
                calendarData.set(year, (month - 1), day);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String yes = format.format(calendarData.getTime());
                calendarData.set(year, (month - 1), day+1);
                String today = format.format(calendarData.getTime());


                callback.OnSuccess(allData + "###" + yes + "###" + today);

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
        if (BleForPhoneAndSmsRemind.getInstance() == null)
            return;
        if (isOn) {
            BleForPhoneAndSmsRemind.getInstance().openPhoneRemine((byte) 0x03, (byte) 0x01);
        } else {
            BleForPhoneAndSmsRemind.getInstance().openPhoneRemine((byte) 0x03, (byte) 0x00);
        }
    }

    @Override
    public void isPhoneRemind(boolean isCalled, String phoneNum, String name) {
        if (BleForPhoneAndSmsRemind.getInstance() == null)
            return;
        if (isCalled) {
            BleForPhoneAndSmsRemind.getInstance().beginPhoneRemind(phoneNum, name, BleForPhoneAndSmsRemind.startPhoneRemindToDevice);
        } else {
            BleForPhoneAndSmsRemind.getInstance().beginPhoneRemind(phoneNum, name, BleForPhoneAndSmsRemind.phoneRemindFromDevice);
        }
    }

    @Override
    public void synTime(final DataCallback<byte[]> callback) {
        BleDataforSyn syn = BleDataforSyn.getSynInstance();
        if (syn == null)
            return;
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
        if (setArgs == null)
            return;
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
        if (BleDataForFactoryReset.getBleDataInstance() == null)
            return;
        BleDataForFactoryReset.getBleDataInstance().settingFactoryReset();
    }

    @Override
    public void startHeartRateListener(DataCallback<byte[]> callback) {
        if (BluetoothLeService.getInstance() == null)
            return;
        BluetoothLeService.getInstance().addCallback(
                BleGattHelper.getInstance(mContext, new gattHelperListener(callback)));
    }

    private class gattHelperListener implements BleGattHelperListener {
        DataCallback<byte[]> callback;

        private gattHelperListener(DataCallback<byte[]> callback) {
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
        if (BleDataForTarget.getInstance() == null)
            return;
        BleDataForTarget.getInstance().sendTargetToDevice(stepTarget, sleepTimes, sleepHour, sleepMinute);
    }

    @Override
    public void setHeartRateFreq(int time) {
        if (BleDataForSettingArgs.getInstance(mContext) == null) {
            return;
        }
        BleDataForSettingArgs.getInstance(mContext).setHeartReatArgs((byte) (int) time);
    }

    @Override
    public void setHeartRateWarning(int maxHR, int minHR) {
        if (BleDataForHRWarning.getInstance() == null)
            return;
        BleDataForHRWarning.getInstance().closeOrOpenWarning(maxHR, minHR, (byte) 0x00);
    }

    @Override
    public void setBodyItem(String hei, String wei, String gen, String bir) {
        new BleDataForSettingParams(mContext)
                .settingTheStepParamsToBracelet(hei, wei, gen, bir);
    }

    @Override
    public void setDeviceMenuState(boolean isOpen, int position) {
        int allData = 0;
        String all = getDataBinString(allData, position, isOpen);
        int dataInt = Integer.parseInt(all, 2);
        if (BleReadDeviceMenuState.getInstance() == null)
            return;
        BleReadDeviceMenuState.getInstance().sendUpdateSwitchData32(dataInt);

    }

    @Override
    public void setAllMenuState(boolean[] data) {
        for (int i = 0; i < data.length; i++) {
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
        if (BleDataForRingDelay.getDelayInstance() == null)
            return;
        BleDataForRingDelay.getDelayInstance().settingDelayData(time);
    }

    @Override
    public void setSitRemind(int number, int isOpen, String beginTime, String endTime, int duration) {
        if (BleForSitRemind.getInstance() == null)
            return;
        BleForSitRemind.getInstance().setSitData(new sitRemindEntity(number, isOpen, beginTime, endTime, duration));
    }

    @Override
    public void deleteSitRemind(int number) {
        if (BleForSitRemind.getInstance() == null)
            return;
        BleForSitRemind.getInstance().deleteThisItem(number);
    }

    @Override
    public void setAppNotification(String type, String title, String content) {
        if (BleForQQWeiChartFacebook.getInstance() == null)
            return;

        byte mType;
        switch (type) {
            case FACEBOOK:
                //使能开关，0x01代表开
                BleForQQWeiChartFacebook.getInstance().openRemind((byte) 0x0b, (byte) 0x01);
                mType = BleDataForQQAndOtherRemine.facebook;
                break;
            case WEIXIN:
                //使能开关，0x01代表开
                BleForQQWeiChartFacebook.getInstance().openRemind((byte) 0x09, (byte) 0x01);
                mType = BleDataForQQAndOtherRemine.weichart;
                break;
            case QQ:
                //使能开关，0x01代表开
                BleForQQWeiChartFacebook.getInstance().openRemind((byte) 0x0a, (byte) 0x01);
                mType = BleDataForQQAndOtherRemine.qq;
                break;
            default:
                mType = (byte) 0xfe;
                break;
        }

        String texts;
        if (TextUtils.isEmpty(title)) {
            texts = content;
        } else if (TextUtils.isEmpty(content)) {
            texts = title;
        } else {
            texts = title + ":" + content;
        }

        if (TextUtils.isEmpty(texts)) {
            return;
        }
        byte[] dataContent = getCanSendByte(texts);
        if (BleDataForQQAndOtherRemine.getIntance() == null) {
            return;
        }
        if (dataContent != null && mType != 0x00) {
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

    @Override
    public void setClock(int num, String type, int hour, int minute, List<String> repeat_days) {
        byte[] byteToSend; //发送的数据
        byte[] typeNameBytes = null; //闹钟类型byte数组
        int len; //闹钟类型存储空间
        byte weekByte = 0; //判别星期
        byte typeInByte;

        //闹钟类型字符串转byte数组
        try {
            typeNameBytes = type.getBytes("Unicode");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (typeNameBytes == null)
            return;

        //开辟闹钟类型存储空间
        if (typeNameBytes[0] == (byte) 0xff && typeNameBytes[1] == (byte) 0xfe) {
            len = typeNameBytes.length - 2;
        } else {
            len = typeNameBytes.length;
        }

        //闹钟类型byte标识
        switch (type) {
            case "迟到提醒":
                typeInByte = (byte) 0x06;
                break;
            case "运动":
                typeInByte = (byte) 0x01;
                break;
            case "约会":
                typeInByte = (byte) 0x02;   //不确定？？？
                break;
            case "喝水":
                typeInByte = (byte) 0x03;
                break;
            case "吃药":
                typeInByte = (byte) 0x04;
                break;
            case "睡眠":
                typeInByte = (byte) 0x05;
                break;
            default:
                typeInByte = (byte) 0x06;
                break;
        }

        //判别星期
        for (int i = 0; i < 7; i++) {
            String s = repeat_days.get(i);
            if (!s.equals("无")) {
                weekByte |= ((byte) 0x01 << i);
            }
        }

        //闹钟类型是否为自定义，是的话
        if (typeInByte == 6)
            byteToSend = new byte[2 + len + 3 + 2];
        else
            byteToSend = new byte[2 + 3 + 2];

        //整合数据
        int index = 0; //标志位
        byteToSend[index] = 0x01;
        index++;
        //整合此条提醒的编号
        byteToSend[index] = (byte) (num & 0xff);
        index++;
        // 整合提醒类型
        byteToSend[index] = (byte) (typeInByte & 0xff);
        index++;
        // 整合提醒时间总数，可以有多条数据，这里只需要一条
        byteToSend[index] = 1;
        index++;
        // 整合提醒时间
        byteToSend[index] = (byte) hour;
        index++;
        byteToSend[index] = (byte) minute;
        index++;
        // 整合重复天
        byteToSend[index] = weekByte;
        index++;
        // 整合提醒名称，此部分数据只在自定义提醒名称时发送
        if (typeInByte == 6) {
            if (typeNameBytes[0] == (byte) 0xff && typeNameBytes[1] == (byte) 0xfe) {
                System.arraycopy(typeNameBytes, 2, byteToSend, index, typeNameBytes.length - 2);
            } else {
                System.arraycopy(typeNameBytes, 0, byteToSend, index, typeNameBytes.length);
            }
        }

        if (BleDataForCustomRemind.getCustomRemindDataInstance() == null)
            return;
        BleDataForCustomRemind.getCustomRemindDataInstance().setOnRequesCallback(new DataSendCallback() {
            @Override
            public void sendSuccess(byte[] receveData) {
            }

            @Override
            public void sendFailed() {

            }

            @Override
            public void sendFinished() {
                //Do something
            }
        });
        // 数据整合完成，向手环发送
        BleDataForCustomRemind.getCustomRemindDataInstance().setCustomRingSettings(byteToSend);


    }

    @Override
    public void deleteClock(int num) {
        if (BleDataForCustomRemind.getCustomRemindDataInstance() == null)
            return;
        BleDataForCustomRemind.getCustomRemindDataInstance().deletePx((byte) num);
    }

    @Override
    public void getHardwareVersion(final DataCallback<String> callback) {
        if (BleDataForHardVersion.getInstance() == null){
            return;
        }
        BleDataForHardVersion.getInstance().setDataSendCallback(new DataSendCallback() {
            @Override
            public void sendSuccess(byte[] bufferTmp) {
                byte bluetooth, soft;
                byte[] hardVersion = new byte[bufferTmp.length - 4];
                System.arraycopy(bufferTmp, 0, hardVersion, 0, bufferTmp.length - 4);
                hardVersion = reversionBytes(hardVersion);
                bluetooth = bufferTmp[bufferTmp.length - 4];
                soft = bufferTmp[bufferTmp.length - 3];

                String versionString = String.valueOf(Integer.valueOf(FormatUtils.bytesToHexString(hardVersion), 16)) + "/"
                        + formatTheVersion(parseTheHexString(bluetooth)) + "/"
                        + formatTheVersion(parseTheHexString(soft));
                callback.OnSuccess(versionString);
            }

            @Override
            public void sendFailed() {
            }

            @Override
            public void sendFinished() {
            }
        });
        BleDataForHardVersion.getInstance().requestHardVersion();
    }

    private byte[] reversionBytes(byte[] hardVersion)
    {
        byte[] reV = new byte[hardVersion.length];
        for (int i = 0; i < hardVersion.length; i++)
        {
            reV[i] = hardVersion[hardVersion.length - i - 1];
        }
        return reV;
    }

    private static String parseTheHexString(byte hard)
    {
        byte a = (byte)(hard >> 4);
        byte b = (byte)(hard & 0x0f);
        String a1 = Integer.toHexString(a);
        String b1 = Integer.toHexString(b);
        return a1 + "" + b1;
    }

    private String formatTheVersion(String version)
    {
        String one = version.substring(0, 1);
        String two = version.substring(1);
        int twoInt = Integer.parseInt(two, 16);
        if(twoInt < 10)
        {
            two = "0" + twoInt;
        }
        else
        {
            two = twoInt + "";
        }
        return one + "." + two;
    }

    @Override
    public void updateHardwareVersion(final Context context, final String filepath, final UpdateVersionTask.UpdateListener listener) {
        UpdateVersionTask updateTask;
        updateTask = new UpdateVersionTask(context, listener);
        updateTask.execute(filepath);
    }

    @Override
    public void stopUpdate(UpdateVersionTask updateTask) {
        updateTask.setTaskCancel();
    }

    @Override
    public void getDayHeartRateData(final DataCallback<byte[]> callback) {
        BleDataForDayHeartReatData.getHRDataInstance(mContext).setOnHrDataRecever(new DataSendCallback() {
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
        BleDataForDayHeartReatData.getHRDataInstance(mContext).requestHeartReatDataAll();
    }

    @Override
    public void heartScan(boolean toScan) {
        if (BleDataForOnLineMovement.getBleDataForOutlineInstance() != null){
            BleDataForOnLineMovement.getBleDataForOutlineInstance().setOnSendRecever(new DataSendCallback() {
                @Override
                public void sendSuccess(byte[] bytes) {

                }

                @Override
                public void sendFailed() {

                }

                @Override
                public void sendFinished() {

                }
            });
            BleDataForOnLineMovement.getBleDataForOutlineInstance().sendHRDataToDevice((byte) (toScan?0x01:0x02));
        }
    }

    @Override
    public void lostRemind(boolean isOpen) {
        if (BleForLostRemind.getInstance() != null){
            BleForLostRemind.getInstance().openAndHandler(isOpen);
//            BleForLostRemind.getInstance().setLostListener(new DataSendCallback() {
//                @Override
//                public void sendSuccess(byte[] bytes) {
//
//                }
//
//                @Override
//                public void sendFailed() {
//
//                }
//
//                @Override
//                public void sendFinished() {
//
//                }
//            });
//            BleForLostRemind.getInstance().requestAndHandler();
        }
    }

    @Override
    public void sendHeartSportRequestForData() {
        if (BleDataForOnLineMovement.getBleDataForOutlineInstance() != null) {
            BleDataForOnLineMovement.getBleDataForOutlineInstance().sendHRDataToDevice((byte) 0x00);
        }
    }

    @Override
    public void startHeartSport(boolean isOpen, final DataCallback<Bundle> callback) {
        if (BleDataForOnLineMovement.getBleDataForOutlineInstance() != null) {
            if (isOpen){
                BleDataForOnLineMovement.getBleDataForOutlineInstance().setOnSendRecever(new DataSendCallback() {
                    @Override
                    public void sendSuccess(byte[] bytes) {
                        if (bytes[0] == 0) {
                            // 实时心率
                            int hrValue = bytes[1] & 0xff;
                            // 实时步数
                            int stepValue = FormatUtils.byte2Int(bytes, 2);
                            // 实时里程
                            int mileValue = FormatUtils.byte2Int(bytes, 6);
//                        int kcalValue = FormatUtils.byte2Int(bytes, 10);
//                        int paceValue = bytes[14] & 0xff;
                            Bundle bundle = new Bundle();
                            bundle.putInt("heart_rate", hrValue);
                            bundle.putInt("distance", mileValue);
                            bundle.putInt("step", stepValue);
                            callback.OnSuccess(bundle);
                        }

                    }

                    @Override
                    public void sendFailed() {

                    }

                    @Override
                    public void sendFinished() {

                    }
                });
                BleDataForOnLineMovement.getBleDataForOutlineInstance().sendHRDataToDevice((byte) 0x01);
            } else {
                BleDataForOnLineMovement.getBleDataForOutlineInstance().sendHRDataToDevice((byte) 0x02);
            }


        }
    }

    @Override
    public void smsRemind(boolean isOn) {
        if (BleForPhoneAndSmsRemind.getInstance() == null)
            return;
        if (isOn) {
            BleForPhoneAndSmsRemind.getInstance().openPhoneRemine((byte) 0x02, (byte) 0x01);
        } else {
            BleForPhoneAndSmsRemind.getInstance().openPhoneRemine((byte) 0x02, (byte) 0x00);
        }
    }

    @Override
    public void smsNotification(String content) {
        if (BleForPhoneAndSmsRemind.getInstance() == null)
            return;
        BleForPhoneAndSmsRemind.getInstance().startSMSRemine(content, (byte) 0x05);
    }

    @Override
    public void isLiftUp(boolean isOpen) {
        if (BleForLIftUpRemind.getInstance() == null){
            return;
        }
        BleForLIftUpRemind.getInstance().openLiftUp(isOpen);
    }

    @Override
    public void getHeartRateScanningState(final DataCallback<Bundle> callback) {
        new Handler(mContext.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (BleDataForSettingArgs.getInstance(mContext) == null)
                    return;
                BleDataForSettingArgs.getInstance(mContext).setOnArgsBackListener(new DataSendCallback() {
                    @Override
                    public void sendSuccess(byte[] bytes) {
                        if (bytes[1] == (byte) 0x02 && bytes[3] == (byte) 0x04) {
                            int sw = bytes[4];  //0代表心率监测关闭  1代表开启
                            int heartData = bytes[2];
                            Bundle bundle = new Bundle();
                            bundle.putInt("state", sw);
                            bundle.putInt("scanFreq", heartData);
                            callback.OnSuccess(bundle);
                        }
                    }

                    @Override
                    public void sendFailed() {

                    }

                    @Override
                    public void sendFinished() {
                        callback.OnFinished();
                    }
                });
                BleDataForSettingArgs.getInstance(mContext).readHeartAndFatigue();
            }
        });
    }

    @Override
    public void getHeartRateWarningState(final DataCallback<Bundle> callback) {
        new Handler(mContext.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BleDataForHRWarning.getInstance().setDataSendCallback(new DataSendCallback() {
                    @Override
                    public void sendSuccess(byte[] bytes) {
                        int maxString = bytes[2] & 0xff;
                        int minString = bytes[3] & 0xff;
                        int switchState = bytes[1] & 0xff; //0开 1关
                        Bundle bundle = new Bundle();
                        bundle.putInt("max", maxString);
                        bundle.putInt("min", minString);
                        bundle.putInt("state", switchState);
                        callback.OnSuccess(bundle);
                    }

                    @Override
                    public void sendFailed() {

                    }

                    @Override
                    public void sendFinished() {
                        callback.OnFinished();
                    }
                });
                BleDataForHRWarning.getInstance().requestWarningData();
            }
        });
    }

    @Override
    public void getLostState(final DataCallback<Boolean> callback) {
        new Handler(mContext.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BleForLostRemind.getInstance().setLostListener(new DataSendCallback() {
                    @Override
                    public void sendSuccess(byte[] bytes) {
                        if(bytes[0] == (byte)0x01) {
                            callback.OnSuccess(bytes[2] == 1);
                        }
                    }

                    @Override
                    public void sendFailed() {

                    }

                    @Override
                    public void sendFinished() {
                        callback.OnFinished();
                    }
                });
                BleForLostRemind.getInstance().requestAndHandler();
            }
        });
    }

    @Override
    public void getAwakeState(final DataCallback<Boolean> callback) {
        new Handler(mContext.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BleForLIftUpRemind.getInstance().setLiftUpListener(new DataSendCallback() {
                    @Override
                    public void sendSuccess(byte[] bytes) {
                        if(bytes[0] == 1) {
                            if (bytes[1] == 6) {
                                callback.OnSuccess(bytes[2] == 1);
                            }
                        }
                    }

                    @Override
                    public void sendFailed() {

                    }

                    @Override
                    public void sendFinished() {
                        callback.OnFinished();
                    }
                });
                BleForLIftUpRemind.getInstance().requestLiftUpData();
            }
        });

    }

    @Override
    public void getSitRemindState(final DataCallback<Bundle> callback) {
        new Handler(mContext.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BleForSitRemind.getInstance().setOnSitDataRecever(new DataSendCallback() {
                    @Override
                    public void sendSuccess(byte[] bytes) {
                        if(bytes[0] == (byte)0x00)
                        {
                            if(bytes.length >= 4)
                            {
                                int count = (bytes.length - 3)/7;
                                for (int i = 0; i < count; i++)
                                {
                                    int number = bytes[1 + i * 7] & 0xff;
                                    if (number == 0){
                                        int open = bytes[2 + i * 7] & 0xff;
                                        int beginM = bytes[3 + i * 7] & 0xff;
                                        int beginH = bytes[4 + i * 7] & 0xff;
                                        int endM = bytes[5 + i * 7] & 0xff;
                                        int endH = bytes[6 + i * 7] & 0xff;
                                        int duration = bytes[7 + i * 7] & 0xff;
                                        String beginTime = ((beginH < 10) ? ("0" + beginH) : beginH) + ":" + ((beginM < 10) ? ("0" + beginM) : beginM);
                                        String endTime = ((endH < 10) ? ("0" + endH) :endH) + ":" + ((endM < 10) ? ("0" + endM) :endM);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("number", number);
                                        bundle.putInt("isOpen", open);
                                        bundle.putString("beginTime", beginTime);
                                        bundle.putString("endTime", endTime);
                                        bundle.putInt("duration", duration);
                                        callback.OnSuccess(bundle);
                                        return;
                                    }

                                }
                            }
                        }
                    }

                    @Override
                    public void sendFailed() {

                    }

                    @Override
                    public void sendFinished() {
                        callback.OnFinished();
                    }
                });
                BleForSitRemind.getInstance().sendToGetSitData();
            }
        });
    }
}
