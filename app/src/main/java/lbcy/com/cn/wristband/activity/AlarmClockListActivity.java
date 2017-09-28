package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.adapter.AlarmClockListAdapter;
import lbcy.com.cn.wristband.app.BaseActivity;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.entity.ClockBean;
import lbcy.com.cn.wristband.entity.ClockBeanDao;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.utils.DialogUtil;
import lbcy.com.cn.wristband.utils.ScreenUtil;
import lbcy.com.cn.wristband.utils.ToastUtil;
import rx.functions.Action1;

public class AlarmClockListActivity extends BaseActivity {

    @BindView(R.id.recycler_clock)
    EasyRecyclerView recyclerClock;
    AlarmClockListAdapter adapter;
    List<ClockBean> list = new ArrayList<>();
    int clock_num;
    ClockBeanDao clockBeanDao;
    SPUtil spUtil;
    Intent getIntent;
    boolean isLateClock;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_clock_list;
    }

    @Override
    protected void initData() {
        getIntent = getIntent();
        isLateClock = getIntent.getBooleanExtra("is_late_clock", false);

        spUtil = new SPUtil(mActivity, Consts.SETTING_DB_NAME);
        clockBeanDao = BaseApplication.getBaseApplication().getBaseDaoSession().getClockBeanDao();
        if (isLateClock){
            list = clockBeanDao.queryBuilder().where(ClockBeanDao.Properties.Type.eq("迟到提醒")).build().list();
        } else {
            list = clockBeanDao.queryBuilder().where(ClockBeanDao.Properties.Type.notEq("迟到提醒")).build().list();
        }


    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.activity_device_clock));
        setRightIcon(R.mipmap.add);

        //新增闹钟
        rightClick(new OnRightClickListener() {
            @Override
            public void click() {
                clock_num = adapter.getCount();
                int clock_sum = 0;
                if (isLateClock){
                    clock_sum = clock_num + Integer.valueOf(spUtil.getString("clock_num", "0"));
                } else {
                    clock_sum = clock_num + Integer.valueOf(spUtil.getString("late_clock_num", "0"));
                }
                if (clock_sum < Consts.CLOCK_MAX_NUM){
                    clockBeanDao = BaseApplication.getBaseApplication().getBaseDaoSession().getClockBeanDao();
                    Intent intent = new Intent(mActivity, SetAlarmClockActivity.class);
                    intent.putExtra("TAG", "-1");
                    intent.putExtra("is_late_clock", isLateClock);
                    startActivity(intent);
                } else {
                    ToastUtil.toast("闹钟数量超限，最多设置7个闹钟（包括迟到闹钟）");
                }
            }
        });

        //闹钟点击事件
        adapter = new AlarmClockListAdapter(mActivity, new AlarmClockListAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                list.clear();
                list = adapter.getAllData();

                Intent intent = new Intent(mActivity, SetAlarmClockActivity.class);
                intent.putExtra("TAG", String.valueOf(list.get(position).getPosition()));
                intent.putExtra("is_late_clock", isLateClock);
                startActivity(intent);
            }
        });

        //闹钟长按删除
        adapter.setMyOnItemLongClickListener(new AlarmClockListAdapter.MyItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                DialogUtil.showDialog(mActivity, "是否删除当前闹钟？", new DialogUtil.DialogListener() {
                    @Override
                    public void submit() {
                        clockBeanDao = BaseApplication.getBaseApplication().getBaseDaoSession().getClockBeanDao();
                        list.clear();
                        list = adapter.getAllData();
                        clockBeanDao.delete(list.get(position));
                        list.remove(position);
                        for (int i = position; i < list.size(); i++){
                            list.get(i).setPosition(list.get(i).getPosition() - 1);
                        }
                        clockBeanDao.updateInTx(list);
                        adapter.remove(position);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void cancel() {

                    }
                });


            }
        });

        //设置列表样式
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(mActivity, R.color.mtpLightGray), ScreenUtil.dip2px(this, 1f), 0, 0);//color & height & paddingLeft & paddingRight
        itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
        itemDecoration.setDrawHeaderFooter(false);//whether draw divider for header and footer,default is false.
        recyclerClock.addItemDecoration(itemDecoration);
        recyclerClock.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerClock.setAdapter(adapter);
        adapter.addAll(list);

    }

    @Override
    protected void loadData() {

        mRxManager.on(Consts.CLOCK_LISTENER, new Action1<Message>() {
            @Override
            public void call(Message message) {
                switch (message.what) {
                    case Consts.UPDATE_ALL_CLOCK_DATA:
                        ClockBean clockBean = (ClockBean) message.obj;
                        int pos = clockBean.getPosition();
                        if (pos == adapter.getCount()) {
                            adapter.add(clockBean);
                            clockBeanDao = BaseApplication.getBaseApplication().getBaseDaoSession().getClockBeanDao();
                            clockBeanDao.insert(clockBean);
                        } else {
                            adapter.update(clockBean, pos);
                            clockBeanDao = BaseApplication.getBaseApplication().getBaseDaoSession().getClockBeanDao();
                            clockBeanDao.update(clockBean);
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity销毁前更新所有数据
        list.clear();
        if (adapter.mGetAllData() != null){
            list = adapter.mGetAllData();
        }
        clockBeanDao = BaseApplication.getBaseApplication().getBaseDaoSession().getClockBeanDao();
        clockBeanDao.updateInTx(list);

        if (isLateClock)
            spUtil.putString("late_clock_num", String.valueOf(adapter.getCount()));
        else
            spUtil.putString("clock_num", String.valueOf(adapter.getCount()));
    }

}
