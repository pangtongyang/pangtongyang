package lbcy.com.cn.wristband.test;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.entity.AlarmClockInfo;
import lbcy.com.cn.purplelibrary.manager.PurpleDeviceManager;
import lbcy.com.cn.purplelibrary.service.ManagerDeviceService;
import lbcy.com.cn.purplelibrary.service.MyNotificationService;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;

/**
 * Created by chenjie on 2017/8/29.
 */

public class PurpleLogin extends AppCompatActivity {
    @BindView(R.id.btn_connect)
    Button btnConnect;
    @BindView(R.id.btn_disconnect)
    Button btnDisconnect;
    SPUtil spUtil;
    @BindView(R.id.btn_setClock)
    Button btnSetClock;

    private ManagerDeviceService managerDeviceService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purple_login);
        ButterKnife.bind(this);
        startService(new Intent(this, MyNotificationService.class));
        managerDeviceService = new ManagerDeviceService(this);
        spUtil = new SPUtil(this, CommonConfiguration.SHAREDPREFERENCES_NAME);
        spUtil.putString("deviceAddress", "FE:54:B9:7C:CB:FA");
        spUtil.putString("deviceName", "123");
    }

    @OnClick({R.id.btn_connect, R.id.btn_disconnect, R.id.btn_setClock})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                new ConnectDeviceTask().execute();
                break;
            case R.id.btn_disconnect:
                //断开连接
                Intent intent = new Intent();
                //设置Intent的action属性
                intent.setAction(CommonConfiguration.DIS_CONNECT_DEVICE_NOTIFICATION);
                //发出广播
                sendBroadcast(intent);
//                try{
////                    managerDeviceService.stopService();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                break;
            case R.id.btn_setClock:
                PurpleDeviceManager manager = new PurpleDeviceManager();
                AlarmClockInfo alarmClockInfo = new AlarmClockInfo();
                alarmClockInfo.setId(1L);
                alarmClockInfo.setDid("1");
                manager.setClock(alarmClockInfo);
                AlarmClockInfo alarmClockInfo1 = new AlarmClockInfo();
                alarmClockInfo1.setId(1L);
                alarmClockInfo1.setDid("2");
                manager.setClock(alarmClockInfo1);
                break;
        }
    }

    class ConnectDeviceTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                Thread.sleep(1000l);
                managerDeviceService.startService();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 0;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
//            if(integer==1){
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
//                Toast.makeText(mActivity, "没有要连接的设备", Toast.LENGTH_SHORT).show();
//            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
}
