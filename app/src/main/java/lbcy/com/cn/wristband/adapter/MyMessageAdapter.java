package lbcy.com.cn.wristband.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import lbcy.com.cn.wristband.R;

/**
 * Created by chenjie on 2017/9/11.
 */

public class MyMessageAdapter extends SecondaryListAdapter<MyMessageAdapter.GroupItemViewHolder, MyMessageAdapter.SubItemViewHolder>  {
    private Context context;

    private List<DataTree<String, String>> dts = new ArrayList<>();

    public MyMessageAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DataTree<String, String>> datas) {
        dts = datas;
        notifyNewData(dts);
    }

    @Override
    public RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_message, parent, false);

        return new GroupItemViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder subItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_message_subitem, parent, false);

        return new SubItemViewHolder(v);
    }

    @Override
    public void onGroupItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex) {

        ((GroupItemViewHolder) holder).tvMessageContent.setText(dts.get(groupItemIndex).getGroupItem());

    }

    @Override
    public void onSubItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex, int subItemIndex) {

        ((SubItemViewHolder) holder).tvMessageContent.setText(dts.get(groupItemIndex).getSubItems().get(subItemIndex));

    }

    @Override
    public void onGroupItemClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {

//        Toast.makeText(context, "group item " + String.valueOf(groupItemIndex) + " is expand " +
//                String.valueOf(isExpand), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSubItemClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {

//        Toast.makeText(context, "sub item " + String.valueOf(subItemIndex) + " in group item " +
//                String.valueOf(groupItemIndex), Toast.LENGTH_SHORT).show();

    }

    public static class GroupItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvMessageContent;

        public GroupItemViewHolder(View itemView) {
            super(itemView);
            tvMessageContent = itemView.findViewById(R.id.tv_message_content);

        }
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvMessageContent;

        public SubItemViewHolder(View itemView) {
            super(itemView);

            tvMessageContent = itemView.findViewById(R.id.tv_message_content);
        }
    }
}
