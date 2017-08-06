package lbcy.com.cn.blacklibrary.manager;

import android.content.Context;
import android.util.Log;

import com.huichenghe.bleControl.Ble.BleDataForBattery;
import com.huichenghe.bleControl.Ble.BleDataForDayData;
import com.huichenghe.bleControl.Ble.BleDataForEachHourData;
import com.huichenghe.bleControl.Ble.BleDataForSleepData;
import com.huichenghe.bleControl.Ble.BleForFindDevice;
import com.huichenghe.bleControl.Ble.BleForPhoneAndSmsRemind;
import com.huichenghe.bleControl.Ble.DataSendCallback;
import com.huichenghe.bleControl.Utils.FormatUtils;

import lbcy.com.cn.blacklibrary.ble.DataCallback;
import lbcy.com.cn.blacklibrary.ctl.DeviceController;

/**
 * Created by chenjie on 2017/8/6.
 */

public class DeviceManager implements DeviceController {

    private static DeviceController manager;
    private static Context mContext;

    private DeviceManager() {
        if (mContext == null) {
            throw new NullPointerException("have not init");
        }
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static DeviceController getInstance() {
        if (manager == null) {
            synchronized (DeviceManager.class) {
                if (manager == null) {
                    manager = new DeviceManager();
                }
            }
        }
        return manager;
    };

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
        BleDataForBattery.getInstance().getBatteryPx();
    }

    @Override
    public void getEachHourStep(final DataCallback callback) {
        BleDataForEachHourData.getEachHourDataInstance().setOnBleDataReceListener(new DataSendCallback() {
            @Override
            public void sendSuccess(byte[] receiveData) {
                callback.OnSuccess(receiveData);
//                int step1 = FormatUtils.byte2Int(receveData, 4);
//                int step2 = FormatUtils.byte2Int(receveData, 8);
//                int step3 = FormatUtils.byte2Int(receveData, 12);
//                int step4 = FormatUtils.byte2Int(receveData, 16);
//                int step9 = FormatUtils.byte2Int(receveData, 36);
//                showdatastring("第一小时步数：" + step1 + "，" + "第八小时步数：" + step9 + "；");

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
}
