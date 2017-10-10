package lbcy.com.cn.wristband.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import butterknife.BindView;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.settingitemlibrary.SetItemView;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.utils.DialogUtil;
import lbcy.com.cn.wristband.utils.ToastUtil;

/**
 * Created by chenjie on 2017/9/8.
 */

public class SetMessagePushActivity extends BaseActivity {
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final int NOTIFICATION_PERMISSION_REQUEST = 1001;
    @BindView(R.id.rl_sms_push)
    SetItemView rlSmsPush;
    @BindView(R.id.rl_qq_push)
    SetItemView rlQqPush;
    @BindView(R.id.rl_wechat_push)
    SetItemView rlWechatPush;
    @BindView(R.id.rl_facebook_push)
    SetItemView rlFacebookPush;
    @BindView(R.id.rl_telephone_push)
    SetItemView rlTelephonePush;

    SPUtil spUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_message_push;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, Consts.SETTING_DB_NAME);
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_device_push));

        if (!isEnabled()) {
            DialogUtil.showDialog(mActivity, getString(R.string.notification_request), new DialogUtil.DialogListener() {
                @Override
                public void submit() {
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivityForResult(intent, NOTIFICATION_PERMISSION_REQUEST);
                }

                @Override
                public void cancel() {
                    rlFacebookPush.setEnable(false);
                    rlQqPush.setEnable(false);
                    rlSmsPush.setEnable(false);
                    rlTelephonePush.setEnable(false);
                    rlWechatPush.setEnable(false);
                }
            });

        }

        getDataFromSP();
    }

    @Override
    protected void loadData() {
        rlFacebookPush.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
                spUtil.putString("facebook_switch", state?"1":"0");
            }
        });
        rlQqPush.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
                spUtil.putString("qq_switch", state?"1":"0");
            }
        });
        rlSmsPush.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
                spUtil.putString("sms_switch", state?"1":"0");
            }
        });
        rlTelephonePush.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
                spUtil.putString("telephone_switch", state?"1":"0");
            }
        });
        rlWechatPush.setmOnCheckedChangeListener(new SetItemView.OnmCheckedChange() {
            @Override
            public void change(boolean state) {
                spUtil.putString("wechat_switch", state?"1":"0");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveData();
    }

    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NOTIFICATION_PERMISSION_REQUEST:
                if (!isEnabled()) {
                    Toast.makeText(mActivity, "未获取权限，通知功能将无法使用！", Toast.LENGTH_SHORT).show();
                    rlFacebookPush.setEnable(false);
                    rlQqPush.setEnable(false);
                    rlSmsPush.setEnable(false);
                    rlTelephonePush.setEnable(false);
                    rlWechatPush.setEnable(false);
                }
                break;
        }
    }

    private void getDataFromSP(){
        rlFacebookPush.setChecked(spUtil.getString("facebook_switch", "0").equals("1"));
        rlQqPush.setChecked(spUtil.getString("qq_switch", "0").equals("1"));
        rlSmsPush.setChecked(spUtil.getString("sms_switch", "0").equals("1"));
        rlTelephonePush.setChecked(spUtil.getString("telephone_switch", "0").equals("1"));
        rlWechatPush.setChecked(spUtil.getString("wechat_switch", "0").equals("1"));
    }

    private void saveData(){
        spUtil.putString("facebook_switch", rlFacebookPush.isChecked()?"1":"0");
        spUtil.putString("qq_switch", rlQqPush.isChecked()?"1":"0");
        spUtil.putString("sms_switch", rlSmsPush.isChecked()?"1":"0");
        spUtil.putString("telephone_switch", rlTelephonePush.isChecked()?"1":"0");
        spUtil.putString("wechat_switch", rlWechatPush.isChecked()?"1":"0");
    }

}
