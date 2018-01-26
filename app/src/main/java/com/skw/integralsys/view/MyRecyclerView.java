package com.skw.integralsys.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/23 12:56
 * @类描述 一句话描述 你的类
 */

public class MyRecyclerView extends RecyclerView {

    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoadMoreComplete = true;

    public interface OnLoadMoreListener {

        void onLoadMore();
    }

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == SCROLL_STATE_IDLE) {
            if (onLoadMoreListener != null) {
                LayoutManager layoutManager = getLayoutManager();
                if (layoutManager != null) {
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                        if (linearLayoutManager.findLastVisibleItemPosition() == (linearLayoutManager.getItemCount() - 1) && isLoadMoreComplete && onLoadMoreListener != null) {
                            isLoadMoreComplete = false;
                            onLoadMoreListener.onLoadMore();
                        }
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        // TODO: 17/1/4
                    }
                }
            }
        }
    }

    /**
     * 设置加载更多监听
     *
     * @param onLoadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    /**
     * 当加载更多完成的时候调用
     */
    public void setLoadMoreComplete() {
        isLoadMoreComplete = true;
    }
}
