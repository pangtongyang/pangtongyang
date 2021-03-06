package lbcy.com.cn.wristband.activity;

import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huichenghe.bleControl.Ble.BluetoothLeService;

import butterknife.BindView;
import butterknife.OnClick;
import lbcy.com.cn.blacklibrary.manager.BlackDeviceManager;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.settingitemlibrary.SetItemView;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.popup.ScanHeartRatePopup;
import lbcy.com.cn.wristband.utils.HandlerTip;
import lbcy.com.cn.wristband.utils.StringUtil;
import razerdp.basepopup.BasePopupWindow;
import rx.functions.Action1;

public class ScanHeartRateActivity extends BaseActivity {

    @BindView(R.id.rl_scan_heart_rate)
    SetItemView rlScanHeartRate;
    @BindView(R.id.set_heartrate_content)
    TextView setHeartrateContent;
    @BindView(R.id.task_item_arrow)
    ImageView taskItemArrow;
    @BindView(R.id.rl_scan_rate)
    RelativeLayout rlScanRate;
    @BindView(R.id.rl_predict_heart_rate)
    SetItemView rlPredictHeartRate;
    @BindView(R.id.rl_predict_range)
    SetItemView rlPredictRange;
    @BindView(R.id.et_max_heart_rate)
    EditText etMaxHeartRate;
    @BindView(R.id.et_min_heart_rate)
    EditText etMinHeartRate;

    SPUtil spUtil;
    BasePopupWindow popupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_heart_rate;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
        popupWindow = getPopUp();
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_device_heartrate));

        getDataFromSP();

        rlScanHeartRate.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
                if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                    Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                    rlScanHeartRate.setChecked(!state);
                    return;
                }

                BlackDeviceManager.getInstance().setHeartRateFreq(state
                        ? StringUtil.getNumFromString(setHeartrateContent.getText().toString())
                        : 1440);
            }
        });


        rlPredictHeartRate.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
                if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                    Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                    rlPredictHeartRate.setChecked(!state);
                    return;
                }

                if (validate()) {
                    BlackDeviceManager.getInstance().setHeartRateWarning(state
                            ? Integer.valueOf(etMaxHeartRate.getText().toString()) : 200, state
                            ? Integer.valueOf(etMinHeartRate.getText().toString()) : 0);
                } else {
                    rlPredictHeartRate.setChecked(false);
                }

            }
        });

        etMaxHeartRate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        etMaxHeartRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                if (temp.length() > 3) {
                    editable.delete(3, 4);
                }
            }
        });

        etMinHeartRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                if (temp.length() > 3) {
                    editable.delete(3, 4);
                }
            }
        });

        etMinHeartRate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void loadData() {
        mRxManager.on(Consts.ACTIVITY_SCAN_HEART_RATE_LISTENER, new Action1<Message>() {
            @Override
            public void call(Message message) {
                switch (message.what) {
                    case Consts.UPDATE_SCAN_HEART_RATE:
                        setHeartrateContent.setText(message.obj.toString());
                        break;
                }
            }
        });
    }

    private boolean validate() {
        if (etMaxHeartRate.getText().toString().equals("") || etMinHeartRate.getText().toString().equals("")) {
            Toast.makeText(mActivity, "心率预警值为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Integer.valueOf(etMaxHeartRate.getText().toString()) > 200) {
            Toast.makeText(mActivity, "最大心率超限！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Integer.valueOf(etMinHeartRate.getText().toString()) < 40) {
            Toast.makeText(mActivity, "最小心率超限！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Integer.valueOf(etMaxHeartRate.getText().toString()) < Integer.valueOf(etMinHeartRate.getText().toString())) {
            Toast.makeText(mActivity, "最大心率小于最小心率！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @OnClick({R.id.rl_scan_rate})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_scan_rate:
                if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                    Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                    return;
                }
                popupWindow.showPopupWindow();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
            return;
        }

        saveData();

        BlackDeviceManager.getInstance().setHeartRateFreq(rlScanHeartRate.isChecked()
                ? StringUtil.getNumFromString(setHeartrateContent.getText().toString())
                : 1440);

        HandlerTip.getInstance().postDelayed(300, new HandlerTip.HandlerCallback() {
            @Override
            public void postDelayed() {
                BlackDeviceManager.getInstance().setHeartRateWarning(rlPredictHeartRate.isChecked()
                        ? Integer.valueOf(etMaxHeartRate.getText().toString()) : 200, rlPredictHeartRate.isChecked()
                        ? Integer.valueOf(etMinHeartRate.getText().toString()) : 0);
            }
        });
    }

    private void getDataFromSP() {
        setHeartrateContent.setText(spUtil.getString("scan_heart_rate_time", "10分钟/次"));
        etMaxHeartRate.setText(spUtil.getString("scan_heart_rate_max_heart_rate", ""));
        etMinHeartRate.setText(spUtil.getString("scan_heart_rate_min_heart_rate", ""));
        rlScanHeartRate.setChecked(spUtil.getString("scan_heart_rate_scan_switch", "0").equals("1"));
        rlPredictHeartRate.setChecked(spUtil.getString("scan_heart_rate_predict_switch", "0").equals("1"));
    }

    private void saveData() {
        spUtil.putString("scan_heart_rate_time", setHeartrateContent.getText().toString());
        spUtil.putString("scan_heart_rate_max_heart_rate", etMaxHeartRate.getText().toString());
        spUtil.putString("scan_heart_rate_min_heart_rate", etMinHeartRate.getText().toString());
        spUtil.putString("scan_heart_rate_scan_switch", rlScanHeartRate.isChecked() ? "1" : "0");
        spUtil.putString("scan_heart_rate_predict_switch", rlPredictHeartRate.isChecked() ? "1" : "0");
    }

    private BasePopupWindow getPopUp() {
        return new ScanHeartRatePopup(mActivity);
    }

}
