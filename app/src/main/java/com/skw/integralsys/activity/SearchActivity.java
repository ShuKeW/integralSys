package com.skw.integralsys.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.skw.integralsys.App;
import com.skw.integralsys.R;
import com.skw.integralsys.adapter.MemberListAdapter;
import com.skw.integralsys.bean.MemberBeanAndPosition;
import com.skw.integralsys.db.Members;
import com.skw.integralsys.db.Members_;
import com.skw.integralsys.decoration.DividerLinearItemDecoration;
import com.skw.integralsys.eventbus.DeleteMemberEvent;
import com.skw.integralsys.eventbus.EditMemberEvent;
import com.skw.integralsys.eventbus.LNumberChangeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.objectbox.Box;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/22 11:21
 * @类描述 一句话描述 你的类
 */

public class SearchActivity extends FragmentActivity implements View.OnClickListener, TextWatcher {
    private RecyclerView recyclerView;
    private TextView hint;
    private EditText inputKey;
    private MemberListAdapter adapter;
    List<Members> membersList;

    public static void intent(Context applicationContext) {
        Intent intent = new Intent(applicationContext, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        applicationContext.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        TextView cancel = (TextView) findViewById(R.id.cancel_search);
        inputKey = (EditText) findViewById(R.id.search_input);
        hint = (TextView) findViewById(R.id.searchHint);
        recyclerView = (RecyclerView) findViewById(R.id.rvSearch);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerLinearItemDecoration(Color.parseColor("#dfdfdf"), getResources().getDimensionPixelSize(R.dimen.dp1), Color.parseColor("#dfdfdf"),
                getResources().getDimensionPixelSize(R.dimen.dp1)));
        adapter = new MemberListAdapter(getApplicationContext(), null, false);
        recyclerView.setAdapter(adapter);
        cancel.setOnClickListener(this);
        inputKey.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_search:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        quaryForKey(s.toString());
    }

    private void quaryForKey(String keyValue) {
        if (!TextUtils.isEmpty(keyValue)) {
            Box<Members> membersBox = ((App) getApplication()).getBoxStore().boxFor(Members.class);
            membersList = membersBox.query().startsWith(Members_.cardId, keyValue).or().startsWith(Members_.name, keyValue).or().startsWith(Members_.carNumber, keyValue).or().startsWith(Members_.phoneNumber, keyValue).build().find();
        } else {
            membersList = null;
        }
        adapter.setDataList(membersList);
        if (membersList != null && membersList.size() > 0) {
            hint.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(keyValue)) {
                hint.setText("未找到和 " + keyValue + " 相关的信息");
            } else {
                hint.setText(R.string.searchHint);
            }
            hint.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EditMemberEvent event) {
        quaryForKey(inputKey.getText().toString().trim());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteMemberEvent event) {
        quaryForKey(inputKey.getText().toString().trim());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LNumberChangeEvent event) {
        quaryForKey(inputKey.getText().toString().trim());
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
