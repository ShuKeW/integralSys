package com.skw.integralsys.holder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skw.integralsys.R;
import com.skw.integralsys.db.Integral;
import com.skw.integralsys.db.Members;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/23 12:45
 * @类描述 一句话描述 你的类
 */

public class LoadMoreHolder extends RVHolder {
    private ProgressBar progressBar;
    private TextView msg;

    public LoadMoreHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
        msg = (TextView) itemView.findViewById(R.id.progressMsg);
    }

    @Override
    public void bindData(Object model, int position) {
        if (model instanceof Integral) {
            Integral integral = (Integral) model;
            if (integral.getId() == -1) {
                progressBar.setVisibility(View.GONE);
                msg.setText(R.string.noMore);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                msg.setText(R.string.loadingMore);
            }
        } else if (model instanceof Members) {
            Members members = (Members) model;
            if (members.getId() == -1) {
                progressBar.setVisibility(View.GONE);
                msg.setText(R.string.noMore);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                msg.setText(R.string.loadingMore);
            }
        }
    }

    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void destory() {

    }
}
