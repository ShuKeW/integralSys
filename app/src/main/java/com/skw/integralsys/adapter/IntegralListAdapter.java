package com.skw.integralsys.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.skw.integralsys.R;
import com.skw.integralsys.db.Integral;
import com.skw.integralsys.holder.IntegralListHolder;
import com.skw.integralsys.holder.LoadMoreHolder;
import com.skw.integralsys.holder.RVHolder;

import java.util.List;

/**
 * @创建人 weishukai
 * @创建时间 18/1/21 上午10:15
 * @类描述 一句话说明这个类是干什么的
 */

public class IntegralListAdapter extends RVAdapter<Integral, RVHolder> {
    public IntegralListAdapter(Context context, List<Integral> mDataList) {
        super(context, mDataList);
    }

    @Override
    public RVHolder newViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        RVHolder holder = null;
        switch (viewType) {
            case 0:
                itemView = mInflater.inflate(R.layout.item_load_more, parent, false);
                holder = new LoadMoreHolder(itemView);
                break;
            case 1:
                itemView = mInflater.inflate(R.layout.item_integral_list, parent, false);
                holder = new IntegralListHolder(itemView);
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        int lastPosition = getItemCount() - 1;
        if (position == lastPosition) {
            return 0;
        } else {
            return 1;
        }
    }
}
