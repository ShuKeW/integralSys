package com.skw.integralsys.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skw.integralsys.App;
import com.skw.integralsys.R;
import com.skw.integralsys.adapter.MemDetailVpAdapter;
import com.skw.integralsys.constants.Constants;
import com.skw.integralsys.db.Integral;
import com.skw.integralsys.db.Members;
import com.skw.integralsys.popwindow.MainMoreWindow;
import com.skw.integralsys.popwindow.MemMoreWindow;
import com.skw.integralsys.popwindow.OnWinMenuItemClickListener;
import com.skw.integralsys.utils.DialogUtil;
import com.skw.integralsys.utils.Utils;

import java.util.List;

import io.objectbox.Box;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/16 17:15
 * @类描述 一句话描述 你的类
 */

public class MemberDetailActivity extends FragmentActivity implements View.OnClickListener, DialogInterface.OnClickListener, OnWinMenuItemClickListener {
    private long MId;
    private static final String key_mid = "key_mid";
    private RelativeLayout tabLayout;
    private TextView tabBasicMsg, tabIntegralMsg;
    private TextView indexLine;
    private ViewPager viewPager;
    private MemDetailVpAdapter adapter;
    private MemMoreWindow popWindow;

    public static void intent(Context context, long MId) {
        Intent intent = new Intent(context, MemberDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(key_mid, MId);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        ImageView more = (ImageView) findViewById(R.id.memMore);
        tabLayout = (RelativeLayout) findViewById(R.id.tabLayout);
        tabBasicMsg = (TextView) findViewById(R.id.basisMsg);
        tabIntegralMsg = (TextView) findViewById(R.id.integralMsg);
        indexLine = (TextView) findViewById(R.id.indexLine);
        indexLine.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (indexLine.getWidth() != tabLayout.getWidth() / 2) {
                    indexLine.setWidth(tabLayout.getWidth() / 2);
                }
            }
        });

        viewPager = (ViewPager) findViewById(R.id.vpMemberDetail);
        back.setOnClickListener(this);
        more.setOnClickListener(this);
        tabBasicMsg.setOnClickListener(this);
        tabIntegralMsg.setOnClickListener(this);
        tabBasicMsg.setSelected(true);
        adapter = new MemDetailVpAdapter(getSupportFragmentManager(), MId);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffsetPixels == 0 && position != 0) {
                    return;
                }
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) indexLine.getLayoutParams();
                layoutParams.leftMargin = (int) (indexLine.getWidth() * positionOffset);
                indexLine.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tabIntegralMsg.setSelected(false);
                        tabBasicMsg.setSelected(true);
                        break;
                    case 1:
                        tabBasicMsg.setSelected(false);
                        tabIntegralMsg.setSelected(true);
                        break;
                }

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
            case R.id.memMore:
                if (popWindow == null) {
                    popWindow = new MemMoreWindow(this);
                }
                popWindow.showPopWindow(getApplicationContext(), v);
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

    private void deleteMember() {
        DialogUtil.dialogLoading(this, "正在删除...");
        Box<Integral> integralBox = ((App) getApplication()).getBoxStore().boxFor(Integral.class);
        List<Integral> integralList = integralBox.query().build().find();
        integralBox.remove(integralList);
        Box<Members> membersBox = ((App) getApplication()).getBoxStore().boxFor(Members.class);
        membersBox.remove(MId);
        DialogUtil.dialogLoadingDismiss(this);
        Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
        Utils.delayFinish(this, Constants.delayFinishTime);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                deleteMember();
                break;
        }
    }

    @Override
    public void onWinMenuItemClick(int whitch) {
        switch (whitch) {
            case 0:
                popWindow.dismiss();
                AddIntegralActivity.intent(getApplicationContext(),MId);
                break;
            case 1:
                popWindow.dismiss();
                DialogUtil.dialogOKorCancel(this, R.string.hit, R.string.sureDele, R.string.sOk, R.string.sCancel, this);
                break;
        }
    }
}
