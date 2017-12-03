package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huichenghe.bleControl.Ble.BluetoothLeService;

import butterknife.BindView;
import butterknife.OnClick;
import lbcy.com.cn.blacklibrary.manager.BlackDeviceManager;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.entity.BasicBodyBean;
import lbcy.com.cn.wristband.entity.BasicBodyData;
import lbcy.com.cn.wristband.entity.BasicBodyDataDao;
import lbcy.com.cn.wristband.entity.MessageBean;
import lbcy.com.cn.wristband.entity.UserInfoData;
import lbcy.com.cn.wristband.entity.UserInfoDataDao;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.manager.NetManager;
import lbcy.com.cn.wristband.rx.RxBus;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chenjie on 2017/9/11.
 */

public class BasicBodyActivity extends BaseActivity {
    @BindView(R.id.et_height)
    EditText etHeight;
    @BindView(R.id.et_weight)
    EditText etWeight;
    @BindView(R.id.weight)
    RelativeLayout weight;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    SPUtil spUtil;
    String token;
    //当前连接的设备
    String which_device = "2";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_basicbody;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
        token = spUtil.getString("token", "");
        which_device = spUtil.getString("which_device", "2");
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.simplebody));

        if (getIntent().getBooleanExtra("login", false)){
            Toast.makeText(mActivity, "身高体重尚未设置，请设置身高体重！", Toast.LENGTH_SHORT).show();
        } else {
            getDataFromDisk();
            getDataFromNetwork();
        }

        // 只能输入小数点后两位
        etHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2)
                {
                    editable.delete(posDot + 3, posDot + 4);
                }
            }
        });

        etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2)
                {
                    editable.delete(posDot + 3, posDot + 4);
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_submit})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_submit:
                //注意接口回调中保存数据，可以toast保存成功或保存失败
                if (validate()){
                    setBodyData();
                }
                break;
        }
    }

    // 向服务器上传身高体重
    private void setBodyData(){
        BasicBodyData data = new BasicBodyData();
        data.setHeight(Double.valueOf(etHeight.getText().toString().trim()));
        data.setWeight(Double.valueOf(etWeight.getText().toString().trim()));
        NetManager.setBodyDataAction(token, data, new NetManager.NetCallBack<MessageBean>() {
            @Override
            public void onResponse(Call<MessageBean> call, Response<MessageBean> response) {
                MessageBean messageBean = response.body();
                if ((messageBean != null ? messageBean.getCode() : 0) == 200){
//                    if (which_device.equals("2")){
//                        b_setBodyDataToDevice();
//                    }
                    Toast.makeText(BaseApplication.getBaseApplication(), "身高体重设置成功！", Toast.LENGTH_SHORT).show();
                    if (getIntent().getBooleanExtra("login", false)){
                        spUtil.putString("body_data", "1");
                        Intent intent = new Intent(mActivity, MainActivity.class);
                        intent.putExtra("isSplashed", true);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Toast.makeText(mActivity, messageBean != null ? messageBean.getMessage() : null, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageBean> call, Throwable t) {

            }
        });
    }

    /**************************************************************************/
    //紫色手环连接相关

    /**************************************************************************/
    //黑色手环相关
    // 设置手环参数
    private void b_setBodyDataToDevice(){
        UserInfoDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getUserInfoDataDao();
        if (dataDao.count() == 0){
            Toast.makeText(mActivity, "用户基本信息获取失败！", Toast.LENGTH_SHORT).show();
            return;
        }
        UserInfoData data = dataDao.loadAll().get(0);
        String height = etHeight.getText().toString().trim();
        String weight = etWeight.getText().toString().trim();
        String gender = data.getSex() == 0 ? "1" : "0";
        String birth = data.getBirth();
        if (BluetoothLeService.getInstance() != null && BluetoothLeService.getInstance().isConnectedDevice()){
            BlackDeviceManager.getInstance().setBodyItem(String.valueOf(Double.valueOf(height).intValue()), String.valueOf(Double.valueOf(weight).intValue()), gender, birth);
            Toast.makeText(mActivity, "身高体重设置成功！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mActivity, "手环未连接！", Toast.LENGTH_SHORT).show();
        }
    }
    /**************************************************************************/


    private boolean validate(){
        if (etHeight.getText().toString().trim().equals("")){
            Toast.makeText(mActivity, "身高为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etWeight.getText().toString().trim().equals("")){
            Toast.makeText(mActivity, "体重为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Double.valueOf(etHeight.getText().toString().trim()) < 50 || Double.valueOf(etHeight.getText().toString().trim()) > 300){
            Toast.makeText(mActivity, "身高超出范围！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Double.valueOf(etWeight.getText().toString().trim()) <= 0 || Double.valueOf(etWeight.getText().toString().trim()) > 300){
            Toast.makeText(mActivity, "体重超出范围！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // 页面写入数据
    private void setText(BasicBodyData data) {
        etHeight.setText(String.valueOf(data.getHeight()));
        etWeight.setText(String.valueOf(data.getWeight()));
    }

    private void getDataFromDisk() {
        BasicBodyDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getBasicBodyDataDao();
        if (dataDao.count() != 0){
            setText(dataDao.loadAll().get(0));
        }
    }

    private void getDataFromNetwork() {
        NetManager.getBodyDataAction(token, new NetManager.NetCallBack<BasicBodyBean>() {
            @Override
            public void onResponse(Call<BasicBodyBean> call, Response<BasicBodyBean> response) {
                BasicBodyBean data = response.body();
                if ((data != null ? data.getCode() : 0) == 200){
                    setText(data.getData());
                    saveData(data.getData());
                } else {
                    Toast.makeText(mActivity, data != null ? data.getMessage().toString() : "数据获取失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasicBodyBean> call, Throwable t) {

            }
        });
    }

    private void saveData(BasicBodyData data) {
        BasicBodyDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getBasicBodyDataDao();
        if (dataDao.count() == 0) {
            dataDao.insert(data);
        } else {
            data.setId(dataDao.loadAll().get(0).getId());
            dataDao.update(data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getIntent().getBooleanExtra("login", false) && spUtil.getString("body_data", "0").equals("1")){
            // 连接设备
            Message message = new Message();
            message.what = Consts.CONNECT_DEVICE;
            RxBus.getInstance().post(Consts.ACTIVITY_MANAGE_LISTENER, message);
            finish();
        }
    }
}
