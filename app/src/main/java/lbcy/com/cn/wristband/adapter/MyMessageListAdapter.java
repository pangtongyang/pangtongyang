package lbcy.com.cn.wristband.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.entity.MessageListData;

/**
 * Created by chenjie on 2017/9/11.
 */

public class MyMessageListAdapter extends RecyclerArrayAdapter<MessageListData> {
    private List<MessageListData> list;

    public MyMessageListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyMessageListHolder(parent);
    }


    private class MyMessageListHolder extends BaseViewHolder<MessageListData> {

        TextView title;

        MyMessageListHolder(ViewGroup parent) {
            super(parent, R.layout.item_my_message);
            title = $(R.id.tv_message_content);
        }

        @Override
        public void setData(MessageListData data) {
            super.setData(data);
            title.setText(data.getAbstracts());
        }

    }

}
