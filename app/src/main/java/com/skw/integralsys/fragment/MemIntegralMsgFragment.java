package com.skw.integralsys.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skw.integralsys.App;
import com.skw.integralsys.R;
import com.skw.integralsys.adapter.IntegralListAdapter;
import com.skw.integralsys.db.Integral;
import com.skw.integralsys.db.Integral_;
import com.skw.integralsys.decoration.DividerLinearItemDecoration;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/16 16:47
 * @类描述 一句话描述 你的类
 */

public class MemIntegralMsgFragment extends Fragment {
    private long MId;
    private static final String key_mid = "key_mid";
    private RecyclerView rvIntegralList;
    private LinearLayoutManager layoutManager;
    private IntegralListAdapter adapter;
    private int pageCount = 20;

    private int pageNumber = 0;

    public static MemIntegralMsgFragment getInstance(long MId) {
        MemIntegralMsgFragment fragment = new MemIntegralMsgFragment();
        Bundle args = new Bundle();
        args.putLong(key_mid, MId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        MId = args.getLong(key_mid);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mem_integral_msg, container, false);
        inteView(view);
        return view;
    }

    private void inteView(View view) {
        rvIntegralList = (RecyclerView) view.findViewById(R.id.rvIntegralList);
        rvIntegralList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvIntegralList.setLayoutManager(layoutManager);
        rvIntegralList.addItemDecoration(new DividerLinearItemDecoration(Color.parseColor("#dfdfdf"), getResources().getDimensionPixelSize(R.dimen.dp1), Color.parseColor("#dfdfdf"),
                getResources().getDimensionPixelSize(R.dimen.dp1)));
        adapter = new IntegralListAdapter(getActivity().getApplicationContext(), null);
        rvIntegralList.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMemIntegralList();
    }

    private void getMemIntegralList() {
        Box<Integral> integralBox = ((App) getActivity().getApplicationContext()).getBoxStore().boxFor(Integral.class);
        QueryBuilder<Integral> queryBuilder = integralBox.query();
        Query<Integral> query = queryBuilder.equal(Integral_.membersToOneId, MId).build();
        List<Integral> integralList = query.find(pageNumber * pageCount, pageCount);
        if (pageNumber > 0) {
            adapter.addDataList(integralList);
        } else {
            adapter.setDataList(integralList);
        }

    }
}
