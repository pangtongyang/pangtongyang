package lbcy.com.cn.wristband.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import lbcy.com.cn.blacklibrary.ble.DataCallback;
import lbcy.com.cn.blacklibrary.manager.BlackDeviceManager;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.manager.PurpleDeviceManagerNew;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.settingitemlibrary.SetItemView;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.entity.LoginData;
import lbcy.com.cn.wristband.entity.LoginDataDao;
import lbcy.com.cn.wristband.entity.MessageBean;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.manager.NetManager;
import lbcy.com.cn.wristband.popup.SlideFromBottomPopup;
import lbcy.com.cn.wristband.rx.RxBus;
import lbcy.com.cn.wristband.rx.RxManager;
import razerdp.basepopup.BasePopupWindow;
import retrofit2.Call;
import retrofit2.Response;
import rx.functions.Action1;

public class MeActivity extends TakePhotoActivity {
    Activity mActivity;

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
    @BindView(R.id.tv_mac)
    TextView tvMac;
    @BindView(R.id.tv_macid)
    TextView tvMacid;
    @BindView(R.id.ll_mac)
    LinearLayout llMac;
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
    LinearLayout rootLayout;
    BasePopupWindow popupWindow;
    SPUtil spUtil;

    //当前连接的设备
    String which_device = "2";
    String token = "";
    String footPrintUrl = "";
    String isUploaded = "0";

    LoginDataDao loginDataDao;
    LoginData loginData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.NoActionBarTheme);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);
        mActivity = this;
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);

        initView();

        which_device = spUtil.getString("which_device", "2");
        token = spUtil.getString("token", "");
        isUploaded = spUtil.getString("is_uploaded", "0");

        // 获取个人足迹链接地址
        getFootPrintUrl();

        itemClick();

        if (which_device.equals("2")) {
            rlDisturb.setVisibility(View.GONE);
//            b_getSettings();

        } else {
            rlDisturb.setVisibility(View.VISIBLE);

//            p_getSettings();
        }

        RxManager mRxManager = new RxManager();

        //监听关闭所有activity事件
        mRxManager.on(Consts.CLOSE_ALL_ACTIVITY_LISTENER, new Action1<Message>() {
            @Override
            public void call(Message message) {
                switch (message.what) {
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
        if (which_device.equals("2")) {
            b_getSettings();
        } else {
            p_getSettings();
        }
        isConnectHandler.post(runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isConnectHandler.removeCallbacks(runnable);
    }

    // 是否连接设备，每隔1秒执行一次
    private Handler isConnectHandler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (which_device.equals("2")) {
                b_getSettings();
            } else {
                p_getSettings();
            }
            isConnectHandler.postDelayed(runnable, 5000);
        }
    };

    private void initView() {
        getState();

        rlFootprint.setVisibility(View.GONE);

        loginDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getLoginDataDao();
        if (loginDataDao.count() == 0) {
            return;
        }
        loginData = loginDataDao.loadAll().get(0);

        tvName.setText(loginData.getName());
        tvId.setText(loginData.getAccount_no());
        tvMacid.setText(loginData.getMac_address());
        Glide.with(mActivity)
                .load(loginData.getLogo())
                .apply(RequestOptions.bitmapTransform(new CenterCrop()).placeholder(R.mipmap.watch).error(R.mipmap.watch))
                .into(ivHeader);
    }

    private void itemClick() {
        rlDisturb.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(final boolean state) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String is_connected = spUtil.getString("is_connected", "0");
                        if (is_connected.equals("0")) {
                            rlDisturb.setChecked(!state);
                            Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 实时保存状态
                        saveState();
                        PurpleDeviceManagerNew.getInstance().setDisturb(state);
                    }
                });

            }
        });

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
                if (footPrintUrl.equals("")) {
                    Toast.makeText(mActivity, "个人足迹尚未获取，请稍后重试", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mActivity, WebActivity.class);
                    intent.putExtra("url", footPrintUrl);
                    startActivity(intent);
                }
            }
        });

        rlBody.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, BasicBodyActivity.class);
                intent.putExtra("login", false);
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
    public void butterOnClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_header:
                // 已上传头像，不能修改
                isUploaded = spUtil.getString("is_uploaded", "0");
                if (isUploaded.equals("1"))
                    return;
                if (!spUtil.getString("logo", "").equals("")) {
                    return;
                }

                popupWindow = getPopup();
                popupWindow.showPopupWindow();
                break;
            case R.id.btn_quit:
                intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                Message message = new Message();
                message.what = Consts.CLOSE_ACTIVITY;
                RxBus.getInstance().post(Consts.ACTIVITY_MANAGE_LISTENER, message);
                finish();
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
            uploadAvatarAction(images.get(0).getCompressPath());
        }
    }

    private void getState() {
        rlDisturb.setChecked(spUtil.getString("rl_disturb", "0").equals("1"));
    }

    private void saveState() {
        spUtil.putString("rl_disturb", rlDisturb.isChecked() ? "1" : "0");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveState();
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

    private void getFootPrintUrl() {
        NetManager.getUserPathAction(token, new NetManager.NetCallBack<MessageBean>() {
            @Override
            public void onResponse(Call<MessageBean> call, Response<MessageBean> response) {
                MessageBean message = response.body();
                if ((message != null ? message.getCode() : 0) == 200) {
                    footPrintUrl = message.getData().toString();
                } else {
                    Toast.makeText(BaseApplication.getBaseApplication(), "个人足迹获取失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageBean> call, Throwable t) {

            }
        });
    }

    // 上传头像
    private void uploadAvatarAction(final String path) {
        NetManager.uploadAvatarAction(token, path, new NetManager.NetCallBack<MessageBean>() {
            @Override
            public void onResponse(Call<MessageBean> call, Response<MessageBean> response) {
                final MessageBean message = response.body();
                if ((message != null ? message.getCode() : 0) == 200) {
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.get(mActivity).clearDiskCache();
                                if (loginData == null || loginDataDao == null) {
                                    loginDataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getLoginDataDao();
                                    loginData = loginDataDao.loadAll().get(0);
                                }
                                loginData.setLogo(message.getData().toString());
                                loginDataDao.update(loginData);
                            }
                        }).start();
                    }
                    Toast.makeText(mActivity, "头像上传成功！", Toast.LENGTH_SHORT).show();
                    Bitmap bitmap = getLoacalBitmap(path);
                    ivHeader.setImageBitmap(bitmap);

                    spUtil.putString("is_uploaded", "1");
                } else {
                    Toast.makeText(mActivity, "头像上传失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageBean> call, Throwable t) {

            }
        });
    }

    /**************************************************************************/
    //紫色手环连接相关
    private void p_getSettings() {
        String is_connected = spUtil.getString("is_connected", "0");
        if (is_connected.equals("1")) {
            tvLink.setText("设备已经连接");
            tvLink.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mActivity, R.drawable.app_circle_green), null, null, null);
        } else {
            tvLink.setText("设备尚未连接");
            tvLink.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mActivity, R.drawable.app_circle_red), null, null, null);
        }
    }

    /**************************************************************************/
    //黑色手环相关
    private void b_getSettings() {
        if (BluetoothLeService.getInstance() != null && BluetoothLeService.getInstance().isConnectedDevice()) {
            tvLink.setText("设备已经连接");
            tvLink.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mActivity, R.drawable.app_circle_green), null, null, null);
        } else {
            tvLink.setText("设备尚未连接");
            tvLink.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mActivity, R.drawable.app_circle_red), null, null, null);
        }

    }

    /**************************************************************************/
}
