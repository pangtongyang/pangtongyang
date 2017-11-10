package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.adapter.MyMessageAdapter;
import lbcy.com.cn.wristband.adapter.MyMessageListAdapter;
import lbcy.com.cn.wristband.adapter.SecondaryListAdapter;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.entity.MessageDetailData;
import lbcy.com.cn.wristband.entity.MessageDetailDataDao;
import lbcy.com.cn.wristband.entity.MessageListBean;
import lbcy.com.cn.wristband.entity.MessageListData;
import lbcy.com.cn.wristband.entity.MessageListDataDao;
import lbcy.com.cn.wristband.manager.NetManager;
import lbcy.com.cn.wristband.utils.ScreenUtil;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chenjie on 2017/11/10.
 */

public class MyMessageActivity extends BaseActivity {

    @BindView(R.id.rv_message)
    EasyRecyclerView rvMessage;
    MyMessageListAdapter adapter;
    SPUtil spUtil;
    String token;
    List<MessageListData> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
        token = spUtil.getString("token", "");
    }

    @Override
    protected void initView() {
        adapter = new MyMessageListAdapter(mActivity);
        //设置列表样式
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(mActivity, R.color.mtpLightGray), ScreenUtil.dip2px(this, 1f), 0, 0);//color & height & paddingLeft & paddingRight
        itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
        itemDecoration.setDrawHeaderFooter(false);//whether draw divider for header and footer,default is false.
        rvMessage.addItemDecoration(itemDecoration);
        rvMessage.setLayoutManager(new LinearLayoutManager(mActivity));
        rvMessage.setAdapter(adapter);
        adapter.addAll(list);

        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mActivity, MyMessageDetailActivity.class);
                intent.putExtra("id", adapter.getItem(position).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData() {
        getDataFromDisk();
        getDataFromNetwork();
    }

    private void getDataFromDisk(){
        MessageListDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getMessageListDataDao();
        if (dataDao.count() != 0){
            setText(dataDao.loadAll());
        }
    }

    private void getDataFromNetwork(){
        NetManager.getMessageListAction(token, new NetManager.NetCallBack<MessageListBean>() {
            @Override
            public void onResponse(Call<MessageListBean> call, Response<MessageListBean> response) {
                MessageListBean listBean = response.body();
                if ((listBean != null ? listBean.getCode() : 0) == 200){
                    setText(listBean.getData());
                    saveData(listBean.getData());
                } else {
                    rvMessage.setRefreshing(false);
                    Toast.makeText(mActivity, listBean != null ? listBean.getMessage().toString() : "数据获取失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageListBean> call, Throwable t) {
                rvMessage.setRefreshing(false);
            }
        });
    }

    // 按照id排序
    private void sortById(){
        Comparator<MessageListData> comparator = new Comparator<MessageListData>() {
            @Override
            public int compare(MessageListData t1, MessageListData t2) {
                String time1 = String.valueOf(t1.getTime());
                String time2 = String.valueOf(t2.getTime());
                return time2.compareTo(time1);
            }
        };
        Collections.sort(list, comparator);
    }

    private void setText(List<MessageListData> datas){
        list.clear();
        list.addAll(datas);
        adapter.addAll(list);
    }

    private void saveData(List<MessageListData> datas){
        MessageListDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getMessageListDataDao();
        dataDao.deleteAll();
        for (MessageListData data: datas) {
            dataDao.insert(data);
        }
    }
}
