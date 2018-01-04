package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.widget.Toast;

import com.huichenghe.bleControl.Ble.BluetoothLeService;

import butterknife.BindView;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.widget.timepicker.TimePickView;

/**
 * Created by chenjie on 2017/9/28.
 */

public class SetSedentaryTimeActivity extends BaseActivity {
    int upOrDown;
    String time;
    int hour;
    int minute;

    @BindView(R.id.timepicker)
    TimePickView timepicker;
    private String which_device;
    private SPUtil spUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_sedentary_time;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
        which_device = spUtil.getString("which_device", "2");
    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.activity_device_sedentary_time));
        setRightIcon(R.mipmap.checkmark);

        upOrDown = getIntent().getIntExtra("time", 0);
        time = getIntent().getStringExtra("sedentary_time");
        hour = Integer.valueOf(time.split(":")[0]);
        minute = Integer.valueOf(time.split(":")[1]);

        timepicker.setTime(hour, minute);

        rightClick(new OnRightClickListener() {
            @Override
            public void click() {
                if (which_device.equals("2")) {
                    if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    String is_connected = spUtil.getString("is_connected", "0");
                    if (is_connected.equals("0")) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                int[] time = timepicker.getSelectTime();
                String hour = String.valueOf(time[0]).length() == 1 ? (0 + String.valueOf(time[0])) : String.valueOf(time[0]);
                String minute = String.valueOf(time[1]).length() == 1 ? (0 + String.valueOf(time[1])) : String.valueOf(time[1]);

                Intent intent = new Intent();
                intent.putExtra("time", hour + ":" + minute);

                setResult(upOrDown == 0 ? Consts.SEDENTARY_TIME_ACTIVITY_RESULT_UP : Consts.SEDENTARY_TIME_ACTIVITY_RESULT_DOWN, intent);
                finish();
            }
        });
    }

    @Override
    protected void loadData() {

    }

}
