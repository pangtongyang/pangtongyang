package lbcy.com.cn.wristband.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huichenghe.bleControl.Ble.BluetoothLeService;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.manager.PurpleDeviceManager;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.settingitemlibrary.SetItemView;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.popup.SlideFromBottomPopup;
import lbcy.com.cn.wristband.rx.RxBus;
import lbcy.com.cn.wristband.widget.ImageViewPlus;
import razerdp.basepopup.BasePopupWindow;

public class MeActivity extends TakePhotoActivity {
    Activity mActivity;

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
    @BindView(R.id.tv_mac)
    TextView tvMac;
    @BindView(R.id.tv_macid)
    TextView tvMacid;
    @BindView(R.id.rl_mac)
    RelativeLayout rlMac;
    @BindView(R.id.tv_link)
    TextView tvLink;
    @BindView(R.id.rl_me)
    LinearLayout rlMe;
    @BindView(R.id.rl_statistics)
    SetItemView rlStatistics;
    @BindView(R.id.rl_setup)
    SetItemView rlSetup;
    @BindView(R.id.rl_disturb)
    SetItemView rlDisturb;
    @BindView(R.id.rl_footprint)
    SetItemView rlFootprint;
    @BindView(R.id.rl_body)
    SetItemView rlBody;
    @BindView(R.id.rl_phone)
    SetItemView rlPhone;
    @BindView(R.id.rl_news)
    SetItemView rlNews;
    @BindView(R.id.rl_help)
    SetItemView rlHelp;
    @BindView(R.id.btn_quit)
    Button btnQuit;
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;
    BasePopupWindow popupWindow;
    SPUtil spUtil;

    //当前连接的设备
    String which_device = "1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);
        mActivity = this;
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
        itemClick();


        which_device = spUtil.getString("which_device", "1");
        if (which_device.equals("1")){
            b_getSettings();
        } else {
            p_getSettings();
        }
    }

    private void itemClick(){
        rlStatistics.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, SportStatisticsActivity.class);
                startActivity(intent);
            }
        });

        rlSetup.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, DeviceSettingActivity.class);
                startActivity(intent);
            }
        });

        rlFootprint.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {

            }
        });

        rlBody.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, BasicBodyActivity.class);
                startActivity(intent);
            }
        });

        rlPhone.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, BindPhoneActivity.class);
                startActivity(intent);
            }
        });

        rlNews.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, MyMessageActivity.class);
                startActivity(intent);
            }
        });

        rlHelp.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, HelpActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.iv_header, R.id.btn_quit})
    public void butterOnClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.iv_header:
                popupWindow = getPopup();
                popupWindow.showPopupWindow();
                break;
            case R.id.btn_quit:
                spUtil.putString("is_login", "0");
                intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                finish();
                Message message = new Message();
                message.what = Consts.CLOSE_ACTIVITY;
                RxBus.getInstance().post(Consts.ACTIVITY_MANAGE_LISTENER, message);
                break;
        }

    }

    BasePopupWindow getPopup(){
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

    private void showImg(ArrayList<TImage> images){
        popupWindow.dismiss();
        if (images.size() == 1) {
            Bitmap bitmap = getLoacalBitmap(images.get(0).getCompressPath());
            ivHeader.setImageBitmap(bitmap);
        }
    }

    /**
     * 加载本地图片
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
                        if (data.toString().equals("设备已经连接"))
                            tvLink.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mActivity, R.drawable.app_circle_green), null, null, null);
                        else
                            tvLink.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mActivity, R.drawable.app_circle_red), null, null, null);
                    }
                });
            }
        });
    }

    /**************************************************************************/
    //黑色手环相关
    private void b_getSettings(){
        if (BluetoothLeService.getInstance() != null && BluetoothLeService.getInstance().isConnectedDevice()){
            tvLink.setText("设备已经连接");
            tvLink.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mActivity,R.drawable.app_circle_green), null, null, null);
        } else {
            tvLink.setText("设备尚未连接");
            tvLink.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mActivity,R.drawable.app_circle_red), null, null, null);
        }

    }

    /**************************************************************************/
}
