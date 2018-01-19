package com.skw.integralsys.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.skw.integralsys.fragment.MemBasicMsgFragment;
import com.skw.integralsys.fragment.MemIntegralMsgFragment;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/19 17:05
 * @类描述 一句话描述 你的类
 */

public class MemDetailAdapter extends FragmentPagerAdapter {
    private long MId;

    public MemDetailAdapter(FragmentManager fm, long MId) {
        super(fm);
        this.MId = MId;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MemBasicMsgFragment.getInstance(MId);
            case 1:
                return MemIntegralMsgFragment.getInstance(MId);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
