package lbcy.com.cn.wristband.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import lbcy.com.cn.wristband.R;

/**
 * Created by chenjie on 2017/9/11.
 */

public class MyMessageAdapter extends RecyclerArrayAdapter<String> {
    public MyMessageAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyMessageViewHolder(parent);
    }

    private class MyMessageViewHolder extends BaseViewHolder<String> {
        TextView message;

        MyMessageViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_my_message);
            message = $(R.id.tv_message_content);
        }

        @Override
        public void setData(String data) {
            super.setData(data);
            message.setText(data);
        }
    }

}
