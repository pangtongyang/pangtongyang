package lbcy.com.cn.wristband.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.adapter.MyMessageAdapter;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.utils.ScreenUtil;

public class MyMessageActivity extends BaseActivity {

    List<String> data = new ArrayList<>();
    @BindView(R.id.rv_message)
    EasyRecyclerView rvMessage;
    MyMessageAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initData() {
        data.add("hello");
        data.add("哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈");
        data.add(" clipToPadding就是说控件的绘制区域是否在padding里面的,true的情况下如果你设置了padding那么绘制的区域就往里缩,clipChildren是指子控件是否超过");
        data.add(" clipToPadding就是说控件的绘制区域是否在padding里面的,true的情况下如果你设置了padding那么绘制的区域就往里缩,clipChildren是指子控件是否超过");
        data.add(" clipToPadding就是说控件的绘制区域是否在padding里面的,true的情况下如果你设置了padding那么绘制的区域就往里缩,clipChildren是指子控件是否超过");
        data.add(" clipToPadding就是说控件的绘制区域是否在padding里面的,true的情况下如果你设置了padding那么绘制的区域就往里缩,clipChildren是指子控件是否超过");
        data.add(" clipToPadding就是说控件的绘制区域是否在padding里面的,true的情况下如果你设置了padding那么绘制的区域就往里缩,clipChildren是指子控件是否超过");
        data.add(" clipToPadding就是说控件的绘制区域是否在padding里面的,true的情况下如果你设置了padding那么绘制的区域就往里缩,clipChildren是指子控件是否超过");
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
        rvMessage.setAdapter(adapter);
        adapter.addAll(data);

        rvMessage.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rvMessage.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvMessage.setRefreshing(false);
                        //adapter.clear();
                        // adapter.addAll(DataProvider.getPictures(0));
                    }
                },1000);

            }
        });
    }

    @Override
    protected void loadData() {

    }

}
