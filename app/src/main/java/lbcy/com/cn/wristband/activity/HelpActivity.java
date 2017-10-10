package lbcy.com.cn.wristband.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.adapter.HelpAdapter;
import lbcy.com.cn.wristband.adapter.SecondaryListAdapter;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.entity.HelpBean;
import lbcy.com.cn.wristband.entity.HelpData;
import lbcy.com.cn.wristband.entity.HelpDataDao;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.manager.NetManager;
import lbcy.com.cn.wristband.utils.ScreenUtil;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chenjie on 2017/9/11.
 */

public class HelpActivity extends BaseActivity {
    @BindView(R.id.rv_help)
    EasyRecyclerView rvHelp;
    HelpAdapter adapter;
    SPUtil spUtil;
    String token;

    private List<SecondaryListAdapter.DataTree<String, String>> datas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, Consts.SETTING_DB_NAME);
        token = spUtil.getString("token", "");
        try {
            byte[] bt0 = "迟到".getBytes("Unicode");
            byte[] bt1 = "迟到提醒".getBytes("Unicode");
            byte[] bt2 = "运动".getBytes("Unicode");
            byte[] bt3 = "约会".getBytes("Unicode");
            byte[] bt4 = "喝水".getBytes("Unicode");
            byte[] bt5 = "吃药".getBytes("Unicode");
            byte[] bt6 = "睡眠".getBytes("Unicode");
            byte[] bt7 = "自定义".getBytes("Unicode");
            List<String> listWeek = new ArrayList<>();
            listWeek.add("星期一");
            listWeek.add("星期二");
            listWeek.add("星期三");
            listWeek.add("星期四");
            listWeek.add("无");
            listWeek.add("星期六");
            listWeek.add("星期日");
            // 星期的一个字节
            byte b = 0;
            int a = 0;
            for (int i = 0; i < 7; i ++)
            {
                String s = listWeek.get(i);
                if(!s.equals("无"))
                {
                    if(i >= 0)
                    {
                        if(i == 7)
                        {
                            b |= (byte)0x01;
                        }
                        else
                        {
                            b |= ((byte)0x01 << i);
                        }
                    }
                }
            }
            Log.e("aaaa", String.valueOf(b));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.fragment_me_help));
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(mActivity, R.color.mtpLightGray), ScreenUtil.dip2px(this,5f), 0,0);//color & height & paddingLeft & paddingRight
        itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
        itemDecoration.setDrawHeaderFooter(false);//whether draw divider for header and footer,default is false.
        rvHelp.addItemDecoration(itemDecoration);
        rvHelp.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new HelpAdapter(mActivity);
        adapter.setData(datas);
        rvHelp.setAdapter(adapter);
        rvHelp.getSwipeToRefresh().setColorSchemeResources(R.color.blue);
        rvHelp.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        HelpDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHelpDataDao();
        if (dataDao.count() != 0){
            setText(dataDao.loadAll());
        }
    }

    private void getDataFromNetwork(){
        NetManager.getHelpInfoAction(token, new NetManager.NetCallBack<HelpBean>() {
            @Override
            public void onResponse(Call<HelpBean> call, Response<HelpBean> response) {
                HelpBean helpBean = response.body();
                if ((helpBean != null ? helpBean.getCode() : 0) == 200){
                    setText(helpBean.getData());
                    saveData(helpBean.getData());
                } else {
                    Toast.makeText(mActivity, helpBean != null ? helpBean.getMessage().toString() : "数据获取失败！", Toast.LENGTH_SHORT).show();
                }
                rvHelp.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<HelpBean> call, Throwable t) {
                rvHelp.setRefreshing(false);
            }
        });
    }

    private void setText(List<HelpData> datas){
        this.datas.clear();
        for (HelpData data: datas){
            this.datas.add(new SecondaryListAdapter.DataTree<String, String>(data.getQuestion(),
                    new ArrayList<String>(){{add(data.getAnswer());}}));
        }
        adapter.setData(this.datas);
    }

    private void saveData(List<HelpData> datas){
        HelpDataDao dataDao = BaseApplication.getBaseApplication().getBaseDaoSession().getHelpDataDao();
        dataDao.deleteAll();
        for (HelpData data: datas) {
            dataDao.insert(data);
        }
    }

}
