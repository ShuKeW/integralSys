package com.skw.integralsys.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @创建人 weishukai
 * @创建时间 17/1/6 下午7:07
 * @类描述 一句话说明这个类是干什么的
 */

public abstract class RVHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    public RVHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    public abstract void bindData(T model, int position);

    @Override
    public void onClick(View v) {
        onItemClick(v);
    }


    public abstract void onItemClick(View view);

    /**
     * 销毁资源
     */
    public abstract void destory();

}
