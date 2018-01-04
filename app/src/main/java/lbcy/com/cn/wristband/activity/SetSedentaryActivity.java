package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huichenghe.bleControl.Ble.BluetoothLeService;

import butterknife.BindView;
import butterknife.OnClick;
import lbcy.com.cn.blacklibrary.ble.DataCallback;
import lbcy.com.cn.blacklibrary.manager.BlackDeviceManager;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.settingitemlibrary.SetItemView;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.popup.SedentarySpaceTimeSettingPopup;
import lbcy.com.cn.wristband.utils.StringUtil;
import razerdp.basepopup.BasePopupWindow;
import rx.functions.Action1;

public class SetSedentaryActivity extends BaseActivity {

    @BindView(R.id.rl_sedentary)
    SetItemView rlSedentary;
    @BindView(R.id.startSedentary)
    Button startSedentary;
    @BindView(R.id.endSedentary)
    Button endSedentary;
    @BindView(R.id.spaceSedentary)
    Button spaceSedentary;

    SPUtil spUtil;
    BasePopupWindow popupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_sedentary;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_Sedentary));

        getDataFromSP();

        rlSedentary.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
                if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                    Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                    rlSedentary.setChecked(!state);
                    return;
                }
                b_getSettings_setRemind(state ? 1 : 0, startSedentary.getText().toString(), endSedentary.getText().toString(),
                        Integer.valueOf(spaceSedentary.getText().toString().replace("分钟", "")));
            }
        });

        mRxManager.on(Consts.ACTIVITY_SEDENTARY_LISTENER, new Action1<Message>() {
            @Override
            public void call(Message message) {
                switch (message.what) {
                    case Consts.UPDATE_SEDENTARY_SPACE_TIME:
                        spaceSedentary.setText(message.obj.toString());
                        break;
                }
            }
        });

    }

    @Override
    protected void loadData() {

    }

    protected void getDataFromSP() {
        startSedentary.setText(spUtil.getString("startSedentary", "09:00"));
        endSedentary.setText(spUtil.getString("endSedentary", "18:00"));
        spaceSedentary.setText(spUtil.getString("spaceSedentary", "30分钟"));
        rlSedentary.setChecked(spUtil.getString("sedentary_checked", "0").equals("1"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveData();
        b_getSettings_setRemind(rlSedentary.isChecked() ? 1 : 0, startSedentary.getText().toString(), endSedentary.getText().toString(),
                StringUtil.getNumFromString(spaceSedentary.getText().toString()));
    }

    @OnClick({R.id.endSedentary, R.id.startSedentary, R.id.spaceSedentary})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.startSedentary:
                if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                    Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                    return;
                }

                intent = new Intent(mActivity, SetSedentaryTimeActivity.class);
                intent.putExtra("time", 0);
                intent.putExtra("sedentary_time", startSedentary.getText().toString());
                startActivityForResult(intent, Consts.SEDENTARY_TIME_ACTIVITY_REQUEST);
                break;
            case R.id.endSedentary:
                if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                    Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                    return;
                }

                intent = new Intent(mActivity, SetSedentaryTimeActivity.class);
                intent.putExtra("time", 1);
                intent.putExtra("sedentary_time", endSedentary.getText().toString());
                startActivityForResult(intent, Consts.SEDENTARY_TIME_ACTIVITY_REQUEST);
                break;
            case R.id.spaceSedentary:
                if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()) {
                    Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                    return;
                }

                popupWindow = getPopup();
                popupWindow.showPopupWindow();
                break;
        }
    }

    BasePopupWindow getPopup() {
        return new SedentarySpaceTimeSettingPopup(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String time;
        switch (resultCode) {
            case Consts.SEDENTARY_TIME_ACTIVITY_RESULT_UP:
                time = data.getStringExtra("time");
                startSedentary.setText(time);
                break;
            case Consts.SEDENTARY_TIME_ACTIVITY_RESULT_DOWN:
                time = data.getStringExtra("time");
                endSedentary.setText(time);
                break;
        }
    }

    protected void saveData() {
        spUtil.putString("startSedentary", startSedentary.getText().toString());
        spUtil.putString("endSedentary", endSedentary.getText().toString());
        spUtil.putString("spaceSedentary", spaceSedentary.getText().toString());
        spUtil.putString("sedentary_checked", rlSedentary.isChecked() ? "1" : "0");
    }

    /**************************************************************************/
    //黑色手环相关
    private void b_getSettings_setRemind(int isOpen, String startTime, String endTime, int duration) {
        BlackDeviceManager.getInstance().setSitRemind(0, isOpen, startTime, endTime, duration);
    }

    /**************************************************************************/
}
