package com.skw.integralsys.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skw.integralsys.R;

/**
 * @创建人 weishukai
 * @创建时间 18/1/21 下午9:20
 * @类描述 一句话说明这个类是干什么的
 */

public class LoadingDialogFragment extends DialogFragment {
    private TextView msg;

    public static LoadingDialogFragment getInstance() {
        return new LoadingDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_progress, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        msg = (TextView) view.findViewById(R.id.msg);
    }

    public void setMessage(String msgValue) {
        msg.setText(msgValue);
    }

    public DialogFragment showAllowingStateLoss(FragmentManager fragmentManager) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, this.getClass().getName());
        ft.commitAllowingStateLoss();
        return this;
    }

}
