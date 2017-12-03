package lbcy.com.cn.wristband.activity;

import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.entity.MessageDetailBean;
import lbcy.com.cn.wristband.manager.NetManager;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chenjie on 2017/11/10.
 */

public class MyMessageDetailActivity extends BaseActivity {

    int id;
    SPUtil spUtil;
    String token;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_message_detail;
    }

    @Override
    protected void initData() {
        id = getIntent().getIntExtra("id", -1);
        if (id == -1){
            Toast.makeText(mActivity, "没有该条记录", Toast.LENGTH_SHORT).show();
            return;
        }
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
        token = spUtil.getString("token", "");
    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.fragment_me_news_detail));
    }

    @Override
    protected void loadData() {
        getMessageData();
    }

    // 根据id获取消息内容
    private void getMessageData(){
        if (id == -1)
            return;
        // 批量操作是否正常执行
        NetManager.getMessageDataAction(token, id, new NetManager.NetCallBack<MessageDetailBean>() {
            @Override
            public void onResponse(Call<MessageDetailBean> call, Response<MessageDetailBean> response) {
                final MessageDetailBean detailBean = response.body();
                if ((detailBean != null ? detailBean.getCode() : 0) == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTitle.setText(detailBean.getData().getAbstracts());
                            tvTime.setText(detailBean.getData().getTime());
                            tvContent.setText(detailBean.getData().getDetail());
                        }
                    });
                } else {
                    Toast.makeText(mActivity, detailBean != null ? detailBean.getMessage().toString() : "数据获取失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageDetailBean> call, Throwable t) {

            }
        });
    }

}
