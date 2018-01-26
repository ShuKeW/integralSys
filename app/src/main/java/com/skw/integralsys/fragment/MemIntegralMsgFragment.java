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
import com.skw.integralsys.bean.IntegralBeanAndPosition;
import com.skw.integralsys.bean.MemberBeanAndPosition;
import com.skw.integralsys.db.Integral;
import com.skw.integralsys.db.Integral_;
import com.skw.integralsys.db.Members;
import com.skw.integralsys.decoration.DividerLinearItemDecoration;
import com.skw.integralsys.eventbus.AddIntegralEvent;
import com.skw.integralsys.eventbus.DeleteIntegralEvent;
import com.skw.integralsys.eventbus.EditIntegralEvent;
import com.skw.integralsys.view.MyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/16 16:47
 * @类描述 一句话描述 你的类
 */

public class MemIntegralMsgFragment extends Fragment implements MyRecyclerView.OnLoadMoreListener {
    private long MId;
    private static final String key_mid = "key_mid";
    private MyRecyclerView rvIntegralList;
    private LinearLayoutManager layoutManager;
    private IntegralListAdapter adapter;
    private int pageCount = 15;

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
        EventBus.getDefault().register(this);
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
        rvIntegralList = (MyRecyclerView) view.findViewById(R.id.rvIntegralList);
        rvIntegralList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvIntegralList.setLayoutManager(layoutManager);
        rvIntegralList.addItemDecoration(new DividerLinearItemDecoration(Color.parseColor("#dfdfdf"), getResources().getDimensionPixelSize(R.dimen.dp1), Color.parseColor("#dfdfdf"),
                0));
        adapter = new IntegralListAdapter(getActivity().getApplicationContext(), null);
        rvIntegralList.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMemIntegralList(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        rvIntegralList.setOnLoadMoreListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        rvIntegralList.setOnLoadMoreListener(null);
    }

    private void getMemIntegralList(boolean isFirstPage) {
        Box<Integral> integralBox = ((App) getActivity().getApplicationContext()).getBoxStore().boxFor(Integral.class);
        QueryBuilder<Integral> queryBuilder = integralBox.query();
        Query<Integral> query = queryBuilder.equal(Integral_.membersToOneId, MId).orderDesc(Integral_.createTime).build();
        int count = adapter.getItemCount();
        if (!isFirstPage) {
            if (count > 0) {
                count--;
            }
        } else {
            count = 0;
        }
        List<Integral> integralList = query.find(count, pageCount);
        if (isFirstPage) {
            Integral integral = new Integral();
            if (integralList == null || integralList.size() < pageCount) {
                integral.setId(-1);
            }
            integralList.add(integral);
            adapter.setDataList(integralList);
        } else {
            rvIntegralList.setLoadMoreComplete();
            if (integralList != null && integralList.size() > 0) {
                if (integralList.size() < pageCount) {
                    Integral integral = adapter.getItem(adapter.getItemCount() - 1);
                    integral.setId(-1);
                }
                adapter.addDataList(integralList, adapter.getItemCount() - 1);
            } else {
                Integral integral = adapter.getItem(adapter.getItemCount() - 1);
                integral.setId(-1);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoadMore() {
        getMemIntegralList(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EditIntegralEvent event) {
        IntegralBeanAndPosition beanAndPosition = findIntegral(event.integral.getId());
        if (beanAndPosition.integral != null) {
            beanAndPosition.integral.setLNumber(event.integral.getLNumber());
            beanAndPosition.integral.setIntegral(event.integral.getIntegral());
            beanAndPosition.integral.setCreateTime(event.integral.getCreateTime());
            beanAndPosition.integral.setUpdateTime(event.integral.getUpdateTime());
            adapter.notifyItemChanged(beanAndPosition.position);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteIntegralEvent event) {
        IntegralBeanAndPosition beanAndPosition = findIntegral(event.integralId);
        if (beanAndPosition.integral != null) {
            adapter.removeItem(beanAndPosition.position);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddIntegralEvent event) {
        getMemIntegralList(true);
    }

    private IntegralBeanAndPosition findIntegral(long integralId) {
        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        int lastPosition = layoutManager.findLastVisibleItemPosition();
        IntegralBeanAndPosition beanAndPosition = new IntegralBeanAndPosition();
        for (int i = firstPosition; i <= lastPosition; i++) {
            beanAndPosition.integral = adapter.getItem(i);
            if (beanAndPosition.integral != null && beanAndPosition.integral.getId() == integralId) {
                beanAndPosition.position = i;
                break;
            }
        }
        return beanAndPosition;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


}
