package com.skw.integralsys.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skw.integralsys.App;
import com.skw.integralsys.R;
import com.skw.integralsys.adapter.MemberListAdapter;
import com.skw.integralsys.db.Members;
import com.skw.integralsys.db.Members_;
import com.skw.integralsys.decoration.DividerLinearItemDecoration;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;

public class MainActivity extends Activity implements View.OnClickListener {
    private PopupWindow popupWindow;
    private ImageView orderJoinDate;
    private ImageView orderTotalIntegral;
    private static final int desc = -1;
    private static final int asc = 1;
    private int order;
    private int pageCount = 20;
    private int pageNumber = 0;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MemberListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        queryMembersNormal();
    }

    private void initView() {
        ImageView more = (ImageView) findViewById(R.id.more);
        LinearLayout search = (LinearLayout) findViewById(R.id.search);
        TextView name = (TextView) findViewById(R.id.name);
        RelativeLayout joinDate = (RelativeLayout) findViewById(R.id.joinDate);
        orderJoinDate = (ImageView) findViewById(R.id.orderJoinDate);
        RelativeLayout totalIntegral = (RelativeLayout) findViewById(R.id.totalIntegral);
        orderTotalIntegral = (ImageView) findViewById(R.id.ordertotalIntegral);
        recyclerView = (RecyclerView) findViewById(R.id.rvMemberList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerLinearItemDecoration(Color.parseColor("#dfdfdf"), getResources().getDimensionPixelSize(R.dimen.dp1), Color.parseColor("#dfdfdf"), getResources().getDimensionPixelSize(R.dimen.dp1)));
        adapter = new MemberListAdapter(getApplicationContext(), null);
        recyclerView.setAdapter(adapter);
        more.setOnClickListener(this);
        name.setOnClickListener(this);
        joinDate.setOnClickListener(this);
        totalIntegral.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                break;
            case R.id.name:
                if (orderJoinDate.getVisibility() == View.VISIBLE || orderTotalIntegral.getVisibility() == View.VISIBLE) {
                    pageNumber = 0;
                    orderJoinDate.setVisibility(View.GONE);
                    orderTotalIntegral.setVisibility(View.GONE);
                    queryMembersNormal();
                }
                break;
            case R.id.joinDate:
                pageNumber = 0;
                if (orderJoinDate.getVisibility() == View.GONE) {
                    order = desc;
                    orderJoinDate.setBackgroundResource(R.mipmap.arrow_order_desc);
                    orderTotalIntegral.setVisibility(View.GONE);
                    orderJoinDate.setVisibility(View.VISIBLE);
                } else {
                    if (order == desc) {
                        order = asc;
                        orderJoinDate.setBackgroundResource(R.mipmap.arrow_order_asc);
                    } else {
                        order = desc;
                        orderJoinDate.setBackgroundResource(R.mipmap.arrow_order_desc);
                    }
                }
                queryMembersDate();
                break;
            case R.id.totalIntegral:
                pageNumber = 0;
                if (orderTotalIntegral.getVisibility() == View.GONE) {
                    order = desc;
                    orderTotalIntegral.setBackgroundResource(R.mipmap.arrow_order_desc);
                    orderJoinDate.setVisibility(View.GONE);
                    orderTotalIntegral.setVisibility(View.VISIBLE);
                } else {
                    if (order == desc) {
                        order = asc;
                        orderTotalIntegral.setBackgroundResource(R.mipmap.arrow_order_asc);
                    } else {
                        order = desc;
                        orderTotalIntegral.setBackgroundResource(R.mipmap.arrow_order_desc);
                    }
                }
                queryMembersIntegral();
                break;
            case R.id.more:
                popMoreMenu(v);
                break;
            case R.id.addMember:
                AddMemberActivity.intent(getApplicationContext());
                popupWindow.dismiss();
                break;
            case R.id.theImport:
                break;
            case R.id.theExport:
                break;
        }
    }

    private void popMoreMenu(View targetView) {
        View view = getLayoutInflater().inflate(R.layout.layout_menu_main, null);
        TextView addMember = (TextView) view.findViewById(R.id.addMember);
        TextView theImport = (TextView) view.findViewById(R.id.theImport);
        TextView theExport = (TextView) view.findViewById(R.id.theExport);
        addMember.setOnClickListener(this);
        theImport.setOnClickListener(this);
        theExport.setOnClickListener(this);
        popupWindow = new PopupWindow(view, getResources().getDimensionPixelSize(R.dimen.dp150), getResources().getDimensionPixelSize(R.dimen.dp120), true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAsDropDown(targetView);
    }

    private void queryMembersNormal() {
        Box<Members> membersBox = ((App) getApplication()).getBoxStore().boxFor(Members.class);
        QueryBuilder<Members> queryBuilder = membersBox.query();
        Query<Members> query = queryBuilder.build();
        List<Members> membersList = query.find(pageNumber * pageCount, pageCount);
        setData(membersList);
    }

    private void queryMembersDate() {
        Box<Members> membersBox = ((App) getApplication()).getBoxStore().boxFor(Members.class);
        QueryBuilder<Members> queryBuilder = membersBox.query();
        Query<Members> query = null;
        switch (order) {
            case asc:
                query = queryBuilder.order(Members_.createTime).build();
                break;
            case desc:
                query = queryBuilder.orderDesc(Members_.createTime).build();
                break;
        }
        List<Members> membersList = query.find(pageNumber * pageCount, pageCount);
        setData(membersList);
    }

    private void queryMembersIntegral() {
        Box<Members> membersBox = ((App) getApplication()).getBoxStore().boxFor(Members.class);
        QueryBuilder<Members> queryBuilder = membersBox.query();
        Query<Members> query = null;
        switch (order) {
            case asc:
                query = queryBuilder.order(Members_.totalIntegral).build();
                break;
            case desc:
                query = queryBuilder.orderDesc(Members_.totalIntegral).build();
                break;
        }
        List<Members> membersList = query.find(pageNumber * pageCount, pageCount);
        setData(membersList);

    }

    private void setData(List<Members> membersList) {
        if (pageNumber > 0) {
            adapter.addDataList(membersList);
        } else {
            adapter.setDataList(membersList);
        }
    }
}
