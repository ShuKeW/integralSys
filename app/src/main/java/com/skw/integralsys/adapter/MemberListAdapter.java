package com.skw.integralsys.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.skw.integralsys.R;
import com.skw.integralsys.db.Members;
import com.skw.integralsys.holder.IntegralListHolder;
import com.skw.integralsys.holder.LoadMoreHolder;
import com.skw.integralsys.holder.MemberListHolder;
import com.skw.integralsys.holder.RVHolder;

import java.util.List;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/18 17:28
 * @类描述 一句话描述 你的类
 */

public class MemberListAdapter extends RVAdapter<Members, RVHolder> {
    private boolean isLoadMore;


    public MemberListAdapter(Context context, List<Members> mDataList) {
        super(context, mDataList);
        isLoadMore = true;
    }

    public MemberListAdapter(Context context, List<Members> mDataList, boolean isLoadMore) {
        super(context, mDataList);
        this.isLoadMore = isLoadMore;
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
                itemView = mInflater.inflate(R.layout.item_member_list, parent, false);
                holder = new MemberListHolder(itemView);
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        int lastPosition = getItemCount() - 1;
        if (position == lastPosition) {
            if (isLoadMore) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
}
