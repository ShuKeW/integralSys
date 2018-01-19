package com.skw.integralsys.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skw.integralsys.R;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/16 16:47
 * @类描述 一句话描述 你的类
 */

public class MemBasicMsgFragment extends Fragment {

    public static MemBasicMsgFragment getInstance(long MId) {
        MemBasicMsgFragment fragment = new MemBasicMsgFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_basic_msg, container, false);
        return view;
    }
}
