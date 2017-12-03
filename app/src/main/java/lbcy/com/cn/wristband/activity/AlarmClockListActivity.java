package lbcy.com.cn.wristband.activity;

import android.content.Intent;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.huichenghe.bleControl.Ble.BluetoothLeService;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lbcy.com.cn.blacklibrary.manager.BlackDeviceManager;
import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.manager.PurpleDeviceManagerNew;
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
    String which_device = "2";
    long mUpdateTime; //更新switch时间(防止重复更新)

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_clock_list;
    }

    @Override
    protected void initData() {
        spUtil = new SPUtil(mActivity, CommonConfiguration.SHAREDPREFERENCES_NAME);
        which_device = spUtil.getString("which_device", "2");

        getIntent = getIntent();
        isLateClock = getIntent.getBooleanExtra("is_late_clock", false);

        clockBeanDao = BaseApplication.getBaseApplication().getBaseDaoSession().getClockBeanDao();
        if (isLateClock){
            list = clockBeanDao.queryBuilder().where(ClockBeanDao.Properties.Type.eq("迟到提醒")).build().list();
        } else {
            list = clockBeanDao.queryBuilder().where(ClockBeanDao.Properties.Type.notEq("迟到提醒")).build().list();
        }

        for (ClockBean clock : list){
            if (clock.getText().equals("只响一次")){
                long mCurrentTime = System.currentTimeMillis();
                if (clock.getBookTime() <= mCurrentTime && clock.getSwitchState()){
                    clock.setSwitchState(false);
                }
            }
        }
    }

    @Override
    protected void initView() {
        if (isLateClock){
            setTitle(getResources().getString(R.string.activity_device_late));
        } else {
            setTitle(getResources().getString(R.string.activity_device_clock));
        }

        setRightIcon(R.mipmap.add);

        //新增闹钟
        rightClick(new OnRightClickListener() {
            @Override
            public void click() {
                if (which_device.equals("2")){
                    if (BluetoothLeService.getInstance() == null || !BluetoothLeService.getInstance().isConnectedDevice()){
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    String is_connected = spUtil.getString("is_connected", "0");
                    if (is_connected.equals("0")){
                        Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                clock_num = adapter.getCount();
                int clock_sum = 0;
                if (isLateClock){
                    clock_sum = clock_num + Integer.valueOf(spUtil.getString("clock_num", "0"));
                } else {
                    clock_sum = clock_num + Integer.valueOf(spUtil.getString("late_clock_num", "0"));
                }
                if (clock_sum < (which_device.equals("2")?Consts.CLOCK_MAX_NUM_BLACK:Consts.CLOCK_MAX_NUM_PURPLE)){
                    clockBeanDao = BaseApplication.getBaseApplication().getBaseDaoSession().getClockBeanDao();
                    Intent intent = new Intent(mActivity, SetAlarmClockActivity.class);
                    intent.putExtra("TAG", "-1");
                    intent.putExtra("is_late_clock", isLateClock);
                    startActivity(intent);
                } else {
                    Toast.makeText(mActivity,"闹钟数量超限，最多设置"+
                            (which_device.equals("2")?Consts.CLOCK_MAX_NUM_BLACK:Consts.CLOCK_MAX_NUM_PURPLE)+
                            "个闹钟（包括迟到闹钟）", Toast.LENGTH_SHORT).show();
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
            public void onItemLongClick(View view, final int position) {
                DialogUtil.showDialog(mActivity, "是否删除当前闹钟？", new DialogUtil.DialogListener() {
                    @Override
                    public void submit() {
                        if (which_device.equals("2")){
                            if (BluetoothLeService.getInstance() != null && BluetoothLeService.getInstance().isConnectedDevice()){
                                clockBeanDao = BaseApplication.getBaseApplication().getBaseDaoSession().getClockBeanDao();
                                list.clear();
                                list = adapter.getAllData();

                                BlackDeviceManager.getInstance().deleteClock(list.get(position).getNumber());
                                clockBeanDao.delete(list.get(position));

                                list.remove(position);
                                for (int i = position; i < list.size(); i++){
                                    list.get(i).setPosition(list.get(i).getPosition() - 1);
                                }
                                clockBeanDao.updateInTx(list);
                                adapter.remove(position);
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String is_connected = spUtil.getString("is_connected", "0");
                            if (is_connected.equals("0")){
                                Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            clockBeanDao = BaseApplication.getBaseApplication().getBaseDaoSession().getClockBeanDao();
                            list.clear();
                            list = adapter.getAllData();

                            PurpleDeviceManagerNew.getInstance().setAlarm(list.get(position).getNumber(),
                                    0, 0, "0-0-0-0-0-0-0", false);

                            clockBeanDao.delete(list.get(position));

                            list.remove(position);
                            for (int i = position; i < list.size(); i++){
                                list.get(i).setPosition(list.get(i).getPosition() - 1);
                            }
                            clockBeanDao.updateInTx(list);
                            adapter.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });


            }
        });

        adapter.setOnSwitchCheckedChangeListener(new AlarmClockListAdapter.OnSwitchCheckChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked, int position) {
                if (which_device.equals("2")){
                    if (BluetoothLeService.getInstance() != null && BluetoothLeService.getInstance().isConnectedDevice()){
                        list.clear();
                        list = adapter.getAllData();
                        if (isChecked){
                            b_setClock(position);
                        } else {
                            BlackDeviceManager.getInstance().deleteClock(list.get(position).getNumber());
                        }
                    } else {
                        if (System.currentTimeMillis() - mUpdateTime > 300){
                            Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                            ClockBean clock = adapter.getItem(position);
                            clock.setSwitchState(!isChecked);
                            adapter.update(clock, position);
                            mUpdateTime = System.currentTimeMillis();
                        }
                    }
                } else {
                    String is_connected = spUtil.getString("is_connected", "0");
                    if (is_connected.equals("0")){
                        if (System.currentTimeMillis() - mUpdateTime > 300){
                            Toast.makeText(mActivity, "手环未连接", Toast.LENGTH_SHORT).show();
                            ClockBean clock = adapter.getItem(position);
                            clock.setSwitchState(!isChecked);
                            adapter.update(clock, position);
                            mUpdateTime = System.currentTimeMillis();
                        }
                        return;
                    }

                    list.clear();
                    list = adapter.getAllData();
                    p_setClock(position, isChecked);
                }


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
        list = adapter.getAllData();
        clockBeanDao = BaseApplication.getBaseApplication().getBaseDaoSession().getClockBeanDao();
        clockBeanDao.updateInTx(list);

        if (isLateClock)
            spUtil.putString("late_clock_num", String.valueOf(adapter.getCount()));
        else
            spUtil.putString("clock_num", String.valueOf(adapter.getCount()));
    }

    /**************************************************************************/
    /**
     * 紫色手环相关
     * @param position 列表item位置
     * @param state 开关状态
     */
    private void p_setClock(int position, boolean state){
        list.clear();
        list = adapter.getAllData();
        ClockBean clock = list.get(position);
        String str = clock.getText();
        int w1 = 0;
        int w2 = 0;
        int w3 = 0;
        int w4 = 0;
        int w5 = 0;
        int w6 = 0;
        int w7 = 0;
        String week;

        switch (str){
            case "只响一次":
                // 手环bug，不设置重复时间，闹钟不响
                w1=w2=w3=w4=w5=w6=w7=1;
                break;
            case "每天":
                w1=w2=w3=w4=w5=w6=w7=1;
                break;
            default:
                if (str.contains("周一"))
                    w1 = 1;
                if (str.contains("周二"))
                    w2 = 1;
                if (str.contains("周三"))
                    w3 = 1;
                if (str.contains("周四"))
                    w4 = 1;
                if (str.contains("周五"))
                    w5 = 1;
                if (str.contains("周六"))
                    w6 = 1;
                if (str.contains("周日"))
                    w7 = 1;
                break;
        }
        week = w6+"-"+w5+"-"+w4+"-"+w3+"-"+w2+"-"+w1+"-"+w7;

        PurpleDeviceManagerNew.getInstance().setAlarm(clock.getNumber(),
                Integer.valueOf(clock.getHour()), Integer.valueOf(clock.getMinute()), week, state);
    }

    /**************************************************************************/
    //黑色手环相关
    private void b_setClock(int position){
        list.clear();
        list = adapter.getAllData();
        List<String> repeat_days = new ArrayList<>();
        String str = list.get(position).getText();
        switch (str){
            case "只响一次":
                for (int i=0;i<7;i++){
                    repeat_days.add("无");
                }
                break;
            case "每天":
                for (int i=0;i<7;i++){
                    repeat_days.add("有");
                }
                break;
            default:
                repeat_days.add(str.contains("周一") ? "有" : "无");
                repeat_days.add(str.contains("周二") ? "有" : "无");
                repeat_days.add(str.contains("周三") ? "有" : "无");
                repeat_days.add(str.contains("周四") ? "有" : "无");
                repeat_days.add(str.contains("周五") ? "有" : "无");
                repeat_days.add(str.contains("周六") ? "有" : "无");
                repeat_days.add(str.contains("周日") ? "有" : "无");
                break;
        }
        BlackDeviceManager.getInstance().
                setClock(list.get(position).getNumber(),
                        list.get(position).getType(),
                        (Integer.valueOf(list.get(position).getHour())),
                        (Integer.valueOf(list.get(position).getMinute())),
                        repeat_days);
    }

    /**************************************************************************/
}
