package com.skw.integralsys.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.skw.integralsys.R;
import com.skw.integralsys.activity.MemberDetailActivity;
import com.skw.integralsys.db.Members;
import com.skw.integralsys.utils.DateUtil;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/18 17:31
 * @类描述 一句话描述 你的类
 */

public class MemberListHolder extends RVHolder<Members> {
    private Members model;
    private TextView name, cardId, joinDate, totalIntegral;

    public MemberListHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        cardId = (TextView) itemView.findViewById(R.id.cardId);
        joinDate = (TextView) itemView.findViewById(R.id.joinDate);
        totalIntegral = (TextView) itemView.findViewById(R.id.totalIntegral);
    }

    @Override
    public void bindData(Members model, int position) {
        this.model = model;
        name.setText(model.getName());
        if (TextUtils.isEmpty(model.getCardId())) {
            cardId.setText("");
        } else {
            cardId.setText("(" + model.getCardId() + ")");
        }

        joinDate.setText(DateUtil.parceDateToStr(model.getCreateTime()));
        totalIntegral.setText("" + model.getTotalIntegral());

    }

    @Override
    public void onItemClick(View view) {
        MemberDetailActivity.intent(view.getContext(), model.getId());
    }

    @Override
    public void destory() {
//        model = null;
//        name = null;
//        joinDate = null;
//        totalIntegral = null;
    }

}
