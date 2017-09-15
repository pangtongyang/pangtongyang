package lbcy.com.cn.wristband.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);
        mActivity = this;
        itemClick();

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

    @OnClick({R.id.iv_header})
    public void butterOnClick(View v){
        switch (v.getId()){
            case R.id.iv_header:
                popupWindow = getPopup();
                popupWindow.showPopupWindow();
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

}
