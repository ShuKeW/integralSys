package com.skw.integralsys.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

public class LoadingDialogFragment extends Fragment {
    private TextView msg;

    public static LoadingDialogFragment getInstance(String msg) {
        LoadingDialogFragment fragment = new LoadingDialogFragment();
        Bundle args = new Bundle();
        args.putString("key_msg", msg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_progress, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        msg = (TextView) view.findViewById(R.id.msg_loading);
        msg.setText(getArguments().getString("key_msg"));
    }

    public void setMsg(String msg) {
        if (this.msg != null) {
            this.msg.setText(msg);
        }
    }
}
