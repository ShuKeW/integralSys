package com.skw.integralsys.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.skw.integralsys.R;
import com.skw.integralsys.db.Members;
import com.skw.integralsys.holder.MemberListHolder;

import java.util.List;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/18 17:28
 * @类描述 一句话描述 你的类
 */

public class MemberListAdapter extends RVAdapter<Members, MemberListHolder> {


    public MemberListAdapter(Context context, List<Members> mDataList) {
        super(context, mDataList);
    }

    @Override
    public MemberListHolder newViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_member_list, parent, false);
        return new MemberListHolder(view);
    }
}
