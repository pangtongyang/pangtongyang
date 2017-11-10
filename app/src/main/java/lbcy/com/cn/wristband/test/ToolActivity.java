package lbcy.com.cn.wristband.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huichenghe.bleControl.Ble.BleDataForQQAndOtherRemine;
import com.huichenghe.bleControl.Ble.BleForPhoneAndSmsRemind;
import com.huichenghe.bleControl.Ble.BleForQQWeiChartFacebook;
import com.huichenghe.bleControl.Ble.BleReadDeviceMenuState;
import com.huichenghe.bleControl.Utils.FormatUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import lbcy.com.cn.blacklibrary.ble.DataCallback;
import lbcy.com.cn.blacklibrary.manager.BlackDeviceManager;
import lbcy.com.cn.wristband.R;

import static lbcy.com.cn.wristband.utils.Util.getDataBinString;

/**
 * Created by chenjie on 2017/8/7.
 */

public class ToolActivity extends AppCompatActivity implements View.OnClickListener {

    private Button butgetdevice, butsettime, butgetsleep, devicebatter, butgetalldaidata, butishow, geteachhouedata, butgeteachsleep;
    private TextView text, textheartrate;
    private Button butqqremain, butphone;

    private byte[] dataContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool);
        initview();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        BluetoothLeService.getInstance().addCallback(
//                BleGattHelper.getInstance(getApplicationContext(), new gattHelperListener()));
        BlackDeviceManager.getInstance().startHeartRateListener(new DataCallback<byte[]>() {
            @Override
            public void OnSuccess(byte[] data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String showResult = "当前心率：";
                        int liveHR = data[1] & 0xff;
                        if (liveHR != 0) {
                            showResult += String.valueOf(liveHR);
                        } else {
                            showResult += "--";
                        }
                        textheartrate.setText(showResult);
                    }
                });
            }

            @Override
            public void OnFailed() {

            }

            @Override
            public void OnFinished() {

            }
        });
    }

    private void initview() {
        textheartrate = (TextView) findViewById(R.id.heartrate);
        text = (TextView) findViewById(R.id.text);
        butgetdevice = (Button) findViewById(R.id.getdevice);
        butsettime = (Button) findViewById(R.id.settime);
        butgetsleep = (Button) findViewById(R.id.getsleepdata);
        devicebatter = (Button) findViewById(R.id.getbatter);
        butgetalldaidata = (Button) findViewById(R.id.getdaydate);
        butishow = (Button) findViewById(R.id.isshow);
        geteachhouedata = (Button) findViewById(R.id.geteachhouedata);
        butgeteachsleep = (Button) findViewById(R.id.geteachsleep);
        butqqremain = (Button) findViewById(R.id.qqremain);
        butphone = (Button) findViewById(R.id.phone);


        butqqremain.setOnClickListener(this);
        butgetdevice.setOnClickListener(this);
        butsettime.setOnClickListener(this);
        butgetsleep.setOnClickListener(this);
        devicebatter.setOnClickListener(this);
        butgetalldaidata.setOnClickListener(this);
        butishow.setOnClickListener(this);
        geteachhouedata.setOnClickListener(this);
        butgeteachsleep.setOnClickListener(this);
        butphone.setOnClickListener(this);


    }


    private void showdata(final byte[] receveData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(FormatUtils.bytesToHexString(receveData));
            }
        });
    }

    private void showdatastring(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(s);
            }
        });
    }

    @Override
    public void onClick(View v) {
        text.setText("");
        switch (v.getId()) {
            case R.id.getdevice:
                if (butgetdevice.getText().toString().equals("查找设备")) {
                    butgetdevice.setText("取消查找");
                    BlackDeviceManager.getInstance().findDevice(true, new DataCallback<byte[]>() {
                        @Override
                        public void OnSuccess(byte[] data) {
                            showdata(data);
                        }

                        @Override
                        public void OnFailed() {

                        }

                        @Override
                        public void OnFinished() {

                        }
                    });
                } else {
                    butgetdevice.setText("查找设备");
                    BlackDeviceManager.getInstance().findDevice(false, null);
                }
                break;
            case R.id.getdaydate:

                BlackDeviceManager.getInstance().getDayData(new DataCallback<byte[]>() {
                    @Override
                    public void OnSuccess(byte[] data) {
                        int stepAll = FormatUtils.byte2Int(data, 4);
                        int calorie = FormatUtils.byte2Int(data, 8);
                        int mileage = FormatUtils.byte2Int(data, 12);
                        int movementTime = FormatUtils.byte2Int(data, 16);
                        int moveCalorie = FormatUtils.byte2Int(data, 20);
                        int sitTime = FormatUtils.byte2Int(data, 24);
                        int sitCalorie = FormatUtils.byte2Int(data, 28);
                        showdatastring("总步数：" + stepAll + "," + "总卡路里：" + calorie + "," + "总里程：" + mileage + "," + "活动时间：" + movementTime + "," + "活动消耗热量：" + moveCalorie + "," + "静坐时间：" + sitTime + "," + "静坐卡路里：" + sitCalorie);
                    }

                    @Override
                    public void OnFailed() {

                    }

                    @Override
                    public void OnFinished() {

                    }
                });

                break;
            case R.id.getbatter:
                BlackDeviceManager.getInstance().getBattery(new DataCallback<byte[]>() {
                    @Override
                    public void OnSuccess(byte[] data) {
                        showdata(data);
                    }

                    @Override
                    public void OnFailed() {

                    }

                    @Override
                    public void OnFinished() {

                    }
                });

                break;
            case R.id.isshow:
                sendNewDataToDevice(false, 0);  //不明白做什么的
                break;
            case R.id.geteachhouedata:
                BlackDeviceManager.getInstance().getEachHourStep(new DataCallback<byte[]>() {
                    @Override
                    public void OnSuccess(byte[] data) {
                        int step1 = FormatUtils.byte2Int(data, 4);
                        int step2 = FormatUtils.byte2Int(data, 8);
                        int step3 = FormatUtils.byte2Int(data, 12);
                        int step4 = FormatUtils.byte2Int(data, 16);
                        int step9 = FormatUtils.byte2Int(data, 36);
                        showdatastring("第一小时步数：" + step1 + "，" + "第八小时步数：" + step9 + "；");
                    }

                    @Override
                    public void OnFailed() {

                    }

                    @Override
                    public void OnFinished() {

                    }
                });

                break;
            case R.id.geteachsleep:
                BlackDeviceManager.getInstance().getSleepData(new DataCallback<String>() {
                    @Override
                    public void OnSuccess(String data) {
//                        String stralldata = getsleepdata(data);
//
//                        // 截取昨天的数据, 从数据末尾截取12个字符，代表两个小时
//                        String y = "";
//                        if (stralldata.length() > 1) {
//                            String res = stralldata.replaceAll("\\d{12}$", "");
//                            y = stralldata.replaceAll(res, "");
//                        }
//                        // 截取今天的数据， 从数据开头截取60个字符，代表10小时
//                        String t = "";
//                        if (stralldata.length() > 0) {
//                            t = stralldata.substring(0, 60);
//                        }
//                        String allData = y + t;
//                        int start = 0;
//                        String first = allData.replaceAll("^[0, 3]+", "");
//                        start = allData.length() - first.length();
//                        String second = allData.replaceAll("[0, 3]+$", "");
//                        int end = allData.length() - second.length();
//                        final String finalString = first.replaceAll("[0, 3]+$", "");
//                        Log.i("huang", "全部数据开始位置：" + start);
//                        Log.i("huang", "全部数据结束位置：" + end);
//                        //05-11 11:05:27.119: I/SleepDataHelper(25991): 全部数据最终数据：22322222222222222222233222212222232322222222
//
//                        showdatastring(finalString);
                    }

                    @Override
                    public void OnFailed() {

                    }

                    @Override
                    public void OnFinished() {

                    }
                });

                break;

            case R.id.qqremain:
                BleForQQWeiChartFacebook.getInstance().openRemind((byte) 0x0a, (byte) 0x01);
                getContentFromWeiChartOther("你哈", "测试");
                BleDataForQQAndOtherRemine.getIntance().sendMessageToDevice((byte) 0x02, dataContent);
                break;
            case R.id.phone:
                if (butphone.getText().toString().equals("电话提醒")) {
                    butphone.setText("取消电话提醒");
                    BleForPhoneAndSmsRemind.getInstance().openPhoneRemine((byte) 0x03, (byte) 0x01);
                    BleForPhoneAndSmsRemind.getInstance().beginPhoneRemind("18010148023", "陈", BleForPhoneAndSmsRemind.startPhoneRemindToDevice);
                } else {
                    butphone.setText("电话提醒");
//                    BleForPhoneAndSmsRemind.getInstance().closeTheRemind();
                    BleForPhoneAndSmsRemind.getInstance().beginPhoneRemind("18010148023", "陈", BleForPhoneAndSmsRemind.phoneRemindFromDevice);
                }
                break;
        }
    }

    private void getContentFromWeiChartOther(String title, String text) {
        String texts = title + ":" + text;
        if (texts != null && texts.equals("")) {
            return;
        }
        getCanSendByte(texts);
    }

    private void getCanSendByte(String text) {
        int max = 32;
        try {
            dataContent = text.getBytes("utf-8");
            if (dataContent.length > max) {
                text = text.substring(0, max / 3);
//                getCanSendByte(text);
                dataContent = text.getBytes("utf-8");
            } else {
                dataContent = text.getBytes("utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void sendNewDataToDevice(boolean isOpen, int position) {
        int number = position;
        int allData = 0;
        String all = getDataBinString(allData, number, isOpen);
        int dataInt = Integer.parseInt(all, 2);
        BleReadDeviceMenuState.getInstance().sendUpdateSwitchData32(dataInt);
    }

    public String getsleepdata(byte[] sleepData) {
        int d = sleepData[0] & 0xff;
        int m = sleepData[1] & 0xff;
        int y = sleepData[2] & 0xff;
        Calendar calendarFromSleepData = Calendar.getInstance(TimeZone.getDefault());
        calendarFromSleepData.set(y + 2000, m - 1, d);
        Date dateOne = calendarFromSleepData.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String curr = format.format(dateOne);
        String dataString = "";
        for (int i = 4; i < 40; i++)        // 遍历字节数组
        {
            byte buffer = sleepData[i];     // 取出一个字节赋值给buffer

            for (int j = 0; j < 4; j++)     // 循环取出两位
            {
                byte a = (byte) ((buffer >> (j * 2)) & (byte) 0x03);   // 取出两位
                int ai = a & (byte) 0x03;
                dataString = dataString + String.valueOf(ai);
            }
        }
        return dataString;
    }

//    class gattHelperListener implements BleGattHelperListener {
//        @Override
//        public void onDeviceStateChangeUI(LocalDeviceEntity device,
//                                          BluetoothGatt gatt,
//                                          final String uuid, final byte[] value) {
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String showResult = "当前心率：";
//                    int liveHR = value[1] & 0xff;
//                    if (liveHR != 0) {
//                        showResult += String.valueOf(liveHR);
//                    } else {
//                        showResult += "--";
//                    }
//                    textheartrate.setText(showResult);
//                }
//            });
//
//        }
//
//        @Override
//        public void onDeviceConnectedChangeUI(final LocalDeviceEntity device,
//                                              boolean showToast,
//                                              final boolean fromServer) {
//
//        }
//    }

}
