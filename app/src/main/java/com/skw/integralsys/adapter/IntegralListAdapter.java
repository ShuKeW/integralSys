package com.skw.integralsys.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.skw.integralsys.R;
import com.skw.integralsys.db.Integral;
import com.skw.integralsys.holder.IntegralListHolder;

import java.util.List;

/**
 * @创建人 weishukai
 * @创建时间 18/1/21 上午10:15
 * @类描述 一句话说明这个类是干什么的
 */

public class IntegralListAdapter extends RVAdapter<Integral, IntegralListHolder> {
    public IntegralListAdapter(Context context, List<Integral> mDataList) {
        super(context, mDataList);
    }

    @Override
    public IntegralListHolder newViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_integral_list, parent, false);
        return new IntegralListHolder(itemView);
    }
}
