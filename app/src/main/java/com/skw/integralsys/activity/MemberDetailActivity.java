package com.skw.integralsys.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skw.integralsys.App;
import com.skw.integralsys.R;
import com.skw.integralsys.adapter.MemDetailAdapter;
import com.skw.integralsys.utils.ScreenUtils;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/16 17:15
 * @类描述 一句话描述 你的类
 */

public class MemberDetailActivity extends FragmentActivity implements View.OnClickListener {
    private long MId;
    private static final String key_mid = "key_mid";
    private TextView tabBasicMsg, tabIntegralMsg;
    private LinearLayout indexLine;
    private ViewPager viewPager;
    private MemDetailAdapter adapter;

    public static void intent(Context context, long MId) {
        Intent intent = new Intent(context, MemberDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(key_mid, MId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);
        getBundle();
        initView();
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        MId = bundle.getLong(key_mid);
    }

    private void initView() {
        ImageView back = (ImageView) findViewById(R.id.back);
        tabBasicMsg = (TextView) findViewById(R.id.basisMsg);
        tabIntegralMsg = (TextView) findViewById(R.id.integralMsg);
        indexLine = (LinearLayout) findViewById(R.id.indexLine);
        indexLine.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) indexLine.getLayoutParams();
                if (layoutParams != null && layoutParams.width != ScreenUtils.getScreenSize(getApplicationContext()).x / 2) {
                    layoutParams.width = ScreenUtils.getScreenSize(getApplicationContext()).x / 2;
                    indexLine.setLayoutParams(layoutParams);
                }
            }
        });

        viewPager = (ViewPager) findViewById(R.id.vpMemberDetail);
        back.setOnClickListener(this);
        tabBasicMsg.setOnClickListener(this);
        tabIntegralMsg.setOnClickListener(this);
        tabBasicMsg.setSelected(true);
        adapter = new MemDetailAdapter(getSupportFragmentManager(), MId);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffsetPixels == 0 && position != 0) {
                    return;
                }
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) indexLine.getLayoutParams();
                layoutParams.leftMargin = positionOffsetPixels / 2;
                indexLine.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.basisMsg:
                tabIntegralMsg.setSelected(false);
                tabBasicMsg.setSelected(true);
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.integralMsg:
                tabBasicMsg.setSelected(false);
                tabIntegralMsg.setSelected(true);
                viewPager.setCurrentItem(1, true);
                break;
        }
    }
}
