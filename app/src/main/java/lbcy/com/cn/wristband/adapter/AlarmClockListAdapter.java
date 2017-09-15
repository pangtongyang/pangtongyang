package lbcy.com.cn.wristband.adapter;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.entity.ClockBean;

/**
 * Created by chenjie on 2017/9/11.
 */

public class AlarmClockListAdapter extends RecyclerArrayAdapter<ClockBean> {
    MyItemClickListener itemClickListener;
    MyItemLongClickListener itemLongClickListener;
    List<ClockBean> list;

    public AlarmClockListAdapter(Context context) {
        super(context);
    }

    public AlarmClockListAdapter(Context context, MyItemClickListener listener) {
        super(context);
        this.itemClickListener = listener;
    }

    public void setMyOnItemClickListener(MyItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setMyOnItemLongClickListener(MyItemLongClickListener listener){
        this.itemLongClickListener = listener;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlarmClockListHolder(parent);
    }


    private class AlarmClockListHolder extends BaseViewHolder<ClockBean> implements View.OnClickListener, View.OnLongClickListener {

        TextView tvTime;
        TextView tvRepeat;
        SwitchCompat switchCom;
        RelativeLayout rlClock;
        TextView tvType;

        AlarmClockListHolder(ViewGroup parent) {
            super(parent, R.layout.item_alarm_clock);
            tvTime = $(R.id.tv_time);
            tvRepeat = $(R.id.tv_repeat);
            switchCom = $(R.id.switch_com);
            rlClock = $(R.id.rl_clock);
            tvType = $(R.id.tv_type);
            rlClock.setOnClickListener(this);
            rlClock.setOnLongClickListener(this);
        }

        @Override
        public void setData(ClockBean data) {
            super.setData(data);
            tvTime.setText(data.getHour() + ":" + data.getMinute());
            tvRepeat.setText(data.getText());
            switchCom.setChecked(data.getSwitchState());
            tvType.setText(data.getType());
            list = getAllData();

            switchCom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    list.get(getDataPosition()).setSwitchState(b);
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getDataPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (itemLongClickListener != null) {
                itemLongClickListener.onItemLongClick(view, getDataPosition());
            }
            return false;
        }
    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int position);
    }

    public interface MyItemLongClickListener {
        public void onItemLongClick(View view, int position);
    }

    public List<ClockBean> mGetAllData(){
        return list;
    }
}
