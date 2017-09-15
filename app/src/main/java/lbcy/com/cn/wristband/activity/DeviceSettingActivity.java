package lbcy.com.cn.wristband.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lbcy.com.cn.settingitemlibrary.SetItemView;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.popup.SlideFromBottomPopup;
import lbcy.com.cn.wristband.widget.ImageViewPlus;
import razerdp.basepopup.BasePopupWindow;

public class DeviceSettingActivity extends TakePhotoActivity {
    Activity mActivity;

    BasePopupWindow popupWindow;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @BindView(R.id.iv_header)
    ImageViewPlus ivHeader;
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
    @BindView(R.id.rl_version)
    RelativeLayout rlVersion;
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
    RelativeLayout rootLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);
        ButterKnife.bind(this);
        mActivity = this;
        itemClick();

    }

    private void itemClick() {
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
}
