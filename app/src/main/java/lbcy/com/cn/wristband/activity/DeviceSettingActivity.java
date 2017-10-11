package lbcy.com.cn.wristband.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.huichenghe.bleControl.Ble.BluetoothLeService;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import lbcy.com.cn.blacklibrary.ble.DataCallback;
import lbcy.com.cn.blacklibrary.manager.BlackDeviceManager;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.manager.PurpleDeviceManager;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.settingitemlibrary.SetItemView;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.entity.HardwareUpdateBean;
import lbcy.com.cn.wristband.entity.LoginData;
import lbcy.com.cn.wristband.entity.LoginDataDao;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.manager.NetManager;
import lbcy.com.cn.wristband.popup.SlideFromBottomPopup;
import lbcy.com.cn.wristband.popup.UpdatePopup;
import lbcy.com.cn.wristband.rx.RxManager;
import lbcy.com.cn.wristband.utils.DialogUtil;
import lbcy.com.cn.wristband.utils.HandlerTip;
import razerdp.basepopup.BasePopupWindow;
import retrofit2.Call;
import retrofit2.Response;
import rx.functions.Action1;

public class DeviceSettingActivity extends TakePhotoActivity {
    Activity mActivity;

    BasePopupWindow popupWindow;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_version_content)
    TextView tvVersionContent;
    @BindView(R.id.ll_version)
    LinearLayout llVersion;
    @BindView(R.id.tv_link)
    TextView tvLink;
    @BindView(R.id.iv_battery)
    ImageView ivBattery;
    @BindView(R.id.tv_battery)
    TextView tvBattery;
    @BindView(R.id.rl_battery)
    RelativeLayout rlBattery;
    @BindView(R.id.rl_me)
    LinearLayout rlMe;
    @BindView(R.id.rl_late_alarm)
    SetItemView rlLateAlarm;
    @BindView(R.id.rl_hand_up)
    SetItemView rlHandUp;
    @BindView(R.id.rl_loss)
    SetItemView rlLoss;
    @BindView(R.id.rl_vibrate)
    SetItemView rlVibrate;
    @BindView(R.id.rl_push)
    SetItemView rlPush;
    @BindView(R.id.rl_clock_setting)
    SetItemView rlClockSetting;
    @BindView(R.id.rl_heart_rate_scan)
    SetItemView rlHeartRateScan;
    @BindView(R.id.rl_long_sitting_alarm)
    SetItemView rlLongSittingAlarm;
    @BindView(R.id.rl_upgrade)
    SetItemView rlUpgrade;
    @BindView(R.id.root_layout)
    LinearLayout rootLayout;

    SPUtil spUtil;

    //设备是否连接
    boolean isLinked = false;
    //当前连接的设备
    String which_device = "2";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);
        ButterKnife.bind(this);
        mActivity = this;
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);

        initView();
        itemClick();

        which_device = spUtil.getString("which_device", "2");
        if (which_device.equals("2")){
            rlHandUp.setVisibility(View.GONE);
            rlLoss.setVisibility(View.VISIBLE);
            rlVibrate.setVisibility(View.GONE);
            rlLongSittingAlarm.setVisibility(View.VISIBLE);
            rlHeartRateScan.setVisibility(View.VISIBLE);

            b_getSettings();
        } else {
            rlHandUp.setVisibility(View.VISIBLE);
            rlLoss.setVisibility(View.GONE);
            rlVibrate.setVisibility(View.VISIBLE);
            rlLongSittingAlarm.setVisibility(View.GONE);
            rlHeartRateScan.setVisibility(View.GONE);

            p_getSettings();
        }

        RxManager mRxManager = new RxManager();

        //监听关闭所有activity事件
        mRxManager.on(Consts.CLOSE_ALL_ACTIVITY_LISTENER, new Action1<Message>() {
            @Override
            public void call(Message message) {
                switch (message.what){
                    case Consts.CLOSE_ALL_ACTIVITY:
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (which_device.equals("2")){
            b_getSettings();
        } else {
            p_getSettings();
        }
    }

    private void initView(){
        LoginDataDao loginDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getLoginDataDao();
        if (loginDataDao.count() == 0){
            return;
        }
        LoginData loginData = loginDataDao.loadAll().get(0);

        tvId.setText(loginData.getAccount_no());
        tvVersionContent.setText(loginData.getMac_address());
        Glide.with(mActivity)
                .load(loginData.getLogo())
                .apply(RequestOptions
                        .bitmapTransform(new CenterCrop())
                        .placeholder(R.mipmap.watch)
                        .error(R.mipmap.watch))
                .into(ivHeader);
    }

    private void itemClick() {
        rlLoss.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
                BlackDeviceManager.getInstance().findDevice(state, null);
            }
        });

        rlLateAlarm.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, AlarmClockListActivity.class);
                intent.putExtra("is_late_clock", true);
                startActivity(intent);
            }
        });

        rlPush.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, SetMessagePushActivity.class);
                startActivity(intent);
            }
        });

        rlClockSetting.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, AlarmClockListActivity.class);
                intent.putExtra("is_late_clock", false);
                startActivity(intent);
            }
        });

        rlHeartRateScan.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, ScanHeartRateActivity.class);
                startActivity(intent);
            }
        });

        rlLongSittingAlarm.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, SetSedentaryActivity.class);
                startActivity(intent);
            }
        });

        rlUpgrade.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                if (which_device.equals("2")){
                    b_getVersion();
                } else {
                    p_getVersion();
                }
            }
        });
    }

    @OnClick({R.id.iv_header})
    public void butterOnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header:
                popupWindow = getPopup();
                popupWindow.showPopupWindow();
                break;
        }

    }

    BasePopupWindow getPopup() {
        return new SlideFromBottomPopup(this, getTakePhoto());
    }

    BasePopupWindow getUpdatePopup(HardwareUpdateBean updateBean) {
        return new UpdatePopup(this, updateBean);
    }

    @Override
    public void takeCancel() {
        popupWindow.dismiss();
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        popupWindow.dismiss();
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
        popupWindow.dismiss();
        if (images.size() == 1) {
            Bitmap bitmap = getLoacalBitmap(images.get(0).getCompressPath());
            ivHeader.setImageBitmap(bitmap);
        }
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onBackPressed(View view) {
        finish();
    }

    /**************************************************************************/
    //紫色手环连接相关
    private void p_getSettings(){
        PurpleDeviceManager.getInstance().isLinked(new PurpleDeviceManager.DataListener() {
            @Override
            public void getData(Object data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvLink.setText(data.toString());
                        if (data.toString().equals("设备已经连接")){
                            tvLink.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mActivity, R.drawable.app_circle_green), null, null, null);
                        } else {
                            tvLink.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mActivity, R.drawable.app_circle_red), null, null, null);
                        }
                    }
                });
            }
        });

        PurpleDeviceManager.getInstance().getBattery(new PurpleDeviceManager.DataListener() {

            @Override
            public void getData(Object data) {
                int power = Integer.valueOf(data.toString());
                tvBattery.setText(power + "%");
                ivBattery.setImageResource(power <= 33
                        ? R.mipmap.power_red : (power <= 66
                        ? R.mipmap.power_yellow : R.mipmap.power_green));
            }
        });
    }

    // 获取固件版本号
    private void p_getVersion(){

    }

    /**************************************************************************/
    //黑色手环相关
    private void b_getSettings(){
        if (BluetoothLeService.getInstance()!= null && BluetoothLeService.getInstance().isConnectedDevice()){
            tvLink.setText("设备已经连接");
            tvLink.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mActivity,R.drawable.app_circle_green), null, null, null);
        } else {
            tvLink.setText("设备尚未连接");
            tvLink.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mActivity,R.drawable.app_circle_red), null, null, null);
        }

        if (BlackDeviceManager.getInstance() != null){
            //心率实时获取方法，包含其他方法回调，需要提前调用
            BlackDeviceManager.getInstance().startHeartRateListener(new DataCallback<byte[]>() {
                @Override
                public void OnSuccess(byte[] data) {

                }

                @Override
                public void OnFailed() {

                }

                @Override
                public void OnFinished() {

                }
            });
        }

        if (BlackDeviceManager.getInstance() != null){
            BlackDeviceManager.getInstance().getBattery(new DataCallback<byte[]>() {
                @Override
                public void OnSuccess(byte[] data) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int power = data[0];
                            tvBattery.setText(power + "%");
                            ivBattery.setImageResource(power <= 33
                                    ? R.mipmap.power_red : (power <= 66
                                    ? R.mipmap.power_yellow : R.mipmap.power_green));
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

    }

    // 获取固件版本号
    private void b_getVersion(){
        if (BluetoothLeService.getInstance()== null || !BluetoothLeService.getInstance().isConnectedDevice()){
            Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        BlackDeviceManager.getInstance().getHardwareVersion(new DataCallback<String>() {
            @Override
            public void OnSuccess(String data) {
                String softVersion = data.split("/")[2];
                String hardVersion = data.split("/")[0];
                String blueVersion = data.split("/")[1];
                HandlerTip.getInstance().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        b_getUpdateListFromNet(softVersion, hardVersion, blueVersion);
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

    private void b_getUpdateListFromNet(String soft, String hard, String blue){
        NetManager.getUpdateListAction("bjhc", soft, hard, blue, "zh-cn", new NetManager.NetCallBack<HardwareUpdateBean>() {
            @Override
            public void onResponse(Call<HardwareUpdateBean> call, Response<HardwareUpdateBean> response) {
                HardwareUpdateBean updateBean = response.body();
                if ((updateBean != null ? updateBean.getResult() : 0) == 1){
                    StringBuilder content = new StringBuilder();
                    for (HardwareUpdateBean.ParamBean param : updateBean.getParam()){
                        content.append(String.format(Locale.getDefault(), "发现%s新版本，\n当前版本%s，新版本%s，\n", param.getType(), param.getNewVersion(), param.getOldVersion()));
                    }
                    content.append("是否现在进行更新？");
                    DialogUtil.showDialog(mActivity, "固件更新", content.toString(), new DialogUtil.DialogListener() {
                        @Override
                        public void submit() {
                            popupWindow = getUpdatePopup(updateBean);
                            popupWindow.showPopupWindow();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                } else {
                    Toast.makeText(mActivity, "获取更新列表失败!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HardwareUpdateBean> call, Throwable t) {

            }
        });
    }

    /**************************************************************************/
}
