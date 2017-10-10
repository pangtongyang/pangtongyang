package lbcy.com.cn.wristband.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.adapter.MyMessageAdapter;
import lbcy.com.cn.wristband.adapter.SecondaryListAdapter;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.entity.MessageDetailBean;
import lbcy.com.cn.wristband.entity.MessageDetailData;
import lbcy.com.cn.wristband.entity.MessageDetailDataDao;
import lbcy.com.cn.wristband.entity.MessageListBean;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.manager.NetManager;
import lbcy.com.cn.wristband.utils.ScreenUtil;
import retrofit2.Call;
import retrofit2.Response;

public class MyMessageActivity extends BaseActivity {

    @BindView(R.id.rv_message)
    EasyRecyclerView rvMessage;
    MyMessageAdapter adapter;
    SPUtil spUtil;
    String token;
    // 消息数量
    int msg_size = 0;

    // 消息详情列表
    List<MessageDetailData> dataList = new ArrayList<>();

    // 消息二维列表
    private List<SecondaryListAdapter.DataTree<String, String>> datas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, Consts.SETTING_DB_NAME);
        token = spUtil.getString("token", "");
    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.fragment_me_news));
        adapter = new MyMessageAdapter(mActivity);
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(mActivity, R.color.mtpLightGray), ScreenUtil.dip2px(this,5f), 0,0);//color & height & paddingLeft & paddingRight
        itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
        itemDecoration.setDrawHeaderFooter(false);//whether draw divider for header and footer,default is false.
        rvMessage.addItemDecoration(itemDecoration);
        rvMessage.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter.setData(datas);
        rvMessage.setAdapter(adapter);

        rvMessage.getSwipeToRefresh().setColorSchemeResources(R.color.blue);

        rvMessage.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNetwork();
            }
        });
    }

    @Override
    protected void loadData() {
        getDataFromDisk();
        getDataFromNetwork();
    }

    private void getDataFromDisk(){
        MessageDetailDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getMessageDetailDataDao();
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
                    getMessageDatas(listBean);
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

    // 根据id批量获取消息内容
    private void getMessageDatas(MessageListBean listBean){
        // 批量操作是否正常执行
        final boolean[] isRunning = {true};
        dataList.clear();
        for (MessageListBean.DataBean data : listBean.getData()){
            NetManager.getMessageDataAction(token, data.getId(), new NetManager.NetCallBack<MessageDetailBean>() {
                @Override
                public void onResponse(Call<MessageDetailBean> call, Response<MessageDetailBean> response) {
                    // 判断数据批量获取是否正常
                    if (!isRunning[0]){
                        return;
                    }
                    // 数据临时存储
                    MessageDetailBean detailBean = response.body();
                    if ((detailBean != null ? detailBean.getCode() : 0) == 200){
                        msg_size++;
                        dataList.add(detailBean.getData());
                    } else {
                        isRunning[0] = false;
                        rvMessage.setRefreshing(false);
                        Toast.makeText(mActivity, detailBean != null ? detailBean.getMessage().toString() : "数据获取失败！", Toast.LENGTH_SHORT).show();
                    }
                    // 数据获取完成，写入页面
                    if (msg_size == listBean.getData().size()){
                        rvMessage.setRefreshing(false);
                        sortById();
                        setText(dataList);
                        saveData(dataList);
                    }
                }

                @Override
                public void onFailure(Call<MessageDetailBean> call, Throwable t) {
                    isRunning[0] = false;
                    rvMessage.setRefreshing(false);
                }
            });
        }
    }

    // 按照id排序
    private void sortById(){
        Comparator<MessageDetailData> comparator = new Comparator<MessageDetailData>() {
            @Override
            public int compare(MessageDetailData t1, MessageDetailData t2) {
                String id1 = String.valueOf(t1.getId());
                String id2 = String.valueOf(t2.getId());
                return id2.compareTo(id1);
            }
        };
        Collections.sort(dataList, comparator);
    }

    private void setText(List<MessageDetailData> datas){
        this.datas.clear();
        for (MessageDetailData data: datas){
            this.datas.add(new SecondaryListAdapter.DataTree<String, String>(data.getAbstracts(),
                    new ArrayList<String>(){{add(data.getDetail());}}));
        }
        adapter.setData(this.datas);
        adapter.notifyDataSetChanged();
    }

    private void saveData(List<MessageDetailData> datas){
        MessageDetailDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getMessageDetailDataDao();
        dataDao.deleteAll();
        for (MessageDetailData data: datas) {
            dataDao.insert(data);
        }
    }
}
