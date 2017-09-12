package lbcy.com.cn.wristband.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.adapter.HelpAdapter;
import lbcy.com.cn.wristband.adapter.SecondaryListAdapter;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.utils.ScreenUtil;

/**
 * Created by chenjie on 2017/9/11.
 */

public class HelpActivity extends BaseActivity {
    @BindView(R.id.rv_help)
    EasyRecyclerView rvHelp;
    HelpAdapter adapter;

    private List<SecondaryListAdapter.DataTree<String, String>> datas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    protected void initData() {

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
        for (int i = 0; i < 3; i++) {

            datas.add(new SecondaryListAdapter.DataTree<String, String>("如何连接蓝牙设备?", new
                    ArrayList<String>(){{add("首先开启蓝牙权限，b并在手环10米范围内,点击设备连接的状态，APP将自行查找范围内手环b并进行配对连接。");}}));

        }

        adapter.setData(datas);
        rvHelp.setAdapter(adapter);


    }


    @Override
    protected void loadData() {

    }

}
