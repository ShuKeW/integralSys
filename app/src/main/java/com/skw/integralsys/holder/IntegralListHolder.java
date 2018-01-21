package com.skw.integralsys.holder;

import android.view.View;
import android.widget.TextView;

import com.skw.integralsys.R;
import com.skw.integralsys.activity.IntegralDetailActivity;
import com.skw.integralsys.db.Integral;
import com.skw.integralsys.utils.DateUtil;

/**
 * @创建人 weishukai
 * @创建时间 18/1/21 上午10:16
 * @类描述 一句话说明这个类是干什么的
 */

public class IntegralListHolder extends RVHolder<Integral> {
    private Integral model;
    private TextView lTime, lNumber, integralNumber;

    public IntegralListHolder(View itemView) {
        super(itemView);
        lTime = (TextView) itemView.findViewById(R.id.lTime);
        lNumber = (TextView) itemView.findViewById(R.id.lNumber);
        integralNumber = (TextView) itemView.findViewById(R.id.integralNumber);
    }

    @Override
    public void bindData(Integral model, int position) {
        this.model = model;
        lTime.setText(DateUtil.parceDateToStr(model.getCreateTime()));
        lNumber.setText("" + model.getLNumber());
        integralNumber.setText("" + model.getIntegral());
    }

    @Override
    public void onItemClick(View view) {
        IntegralDetailActivity.intent(view.getContext(), model);
    }

    @Override
    public void destory() {

    }
}
