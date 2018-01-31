package com.skw.integralsys.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skw.integralsys.App;
import com.skw.integralsys.R;
import com.skw.integralsys.adapter.MemberListAdapter;
import com.skw.integralsys.bean.MemberBeanAndPosition;
import com.skw.integralsys.db.Members;
import com.skw.integralsys.db.Members_;
import com.skw.integralsys.decoration.DividerLinearItemDecoration;
import com.skw.integralsys.dialog.LoadingDialogFragment;
import com.skw.integralsys.eventbus.AddMemberEvent;
import com.skw.integralsys.eventbus.DeleteMemberEvent;
import com.skw.integralsys.eventbus.EditMemberEvent;
import com.skw.integralsys.eventbus.LNumberChangeEvent;
import com.skw.integralsys.popwindow.MainMoreWindow;
import com.skw.integralsys.popwindow.OnWinMenuItemClickListener;
import com.skw.integralsys.utils.DialogUtil;
import com.skw.integralsys.utils.FileUtil;
import com.skw.integralsys.utils.Utils;
import com.skw.integralsys.view.MyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;

public class MainActivity extends FragmentActivity implements View.OnClickListener, OnWinMenuItemClickListener, MyRecyclerView.OnLoadMoreListener {

    private ImageView orderJoinDate;

    private ImageView orderTotalIntegral;

    private static final int desc = -1;

    private static final int asc = 1;

    private int order;

    private int pageCount = 15;

    private MyRecyclerView recyclerView;

    private LinearLayoutManager layoutManager;

    private MemberListAdapter adapter;

    private MainMoreWindow popWindow;

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 100;
    private static final int READ_EXTERNAL_STORAGE_CODE = 101;
    private static final int CHOICE_FILE_CODE = 102;
    private Uri choiceFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initView();
        getMemberList(true);
    }

    private void initView() {
        ImageView more = (ImageView) findViewById(R.id.more);
        LinearLayout search = (LinearLayout) findViewById(R.id.search);
        TextView name = (TextView) findViewById(R.id.name);
        RelativeLayout joinDate = (RelativeLayout) findViewById(R.id.joinDate);
        orderJoinDate = (ImageView) findViewById(R.id.orderJoinDate);
        RelativeLayout totalIntegral = (RelativeLayout) findViewById(R.id.totalIntegral);
        orderTotalIntegral = (ImageView) findViewById(R.id.ordertotalIntegral);
        recyclerView = (MyRecyclerView) findViewById(R.id.rvMemberList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerLinearItemDecoration(Color.parseColor("#dfdfdf"), getResources().getDimensionPixelSize(R.dimen.dp1), Color.parseColor("#dfdfdf"),
                0));
        adapter = new MemberListAdapter(getApplicationContext(), null);
        recyclerView.setAdapter(adapter);
        more.setOnClickListener(this);
        search.setOnClickListener(this);
        name.setOnClickListener(this);
        joinDate.setOnClickListener(this);
        totalIntegral.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setOnLoadMoreListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerView.setOnLoadMoreListener(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                SearchActivity.intent(getApplicationContext());
                break;
            case R.id.name:
                if (orderJoinDate.getVisibility() == View.VISIBLE || orderTotalIntegral.getVisibility() == View.VISIBLE) {
                    orderJoinDate.setVisibility(View.GONE);
                    orderTotalIntegral.setVisibility(View.GONE);
                    LoadingDialogFragment loadingDialogFragment1 = DialogUtil.dialogLoading(getSupportFragmentManager(), "加载中...");
                    getMemberList(true);
                    DialogUtil.dialogLoadingDismiss(loadingDialogFragment1);
                }
                break;
            case R.id.joinDate:
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
                LoadingDialogFragment loadingDialogFragment2 = DialogUtil.dialogLoading(getSupportFragmentManager(), "加载中...");
                getMemberList(true);
                DialogUtil.dialogLoadingDismiss(loadingDialogFragment2);
                break;
            case R.id.totalIntegral:
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
                LoadingDialogFragment loadingDialogFragment3 = DialogUtil.dialogLoading(getSupportFragmentManager(), "加载中...");
                getMemberList(true);
                DialogUtil.dialogLoadingDismiss(loadingDialogFragment3);
                break;
            case R.id.more:
                if (popWindow == null) {
                    popWindow = new MainMoreWindow(this);
                }
                popWindow.showPopWindow(getApplicationContext(), v);
                break;
        }
    }

    @Override
    public void onLoadMore() {
        getMemberList(false);
    }

    private void getMemberList(boolean isForstPage) {
        int count = adapter.getItemCount();
        if (isForstPage) {
            count = 0;
            adapter.setDataList(null);
            recyclerView.scrollToPosition(0);
        } else {
            if (count > 0) {
                count--;
            }
        }

        List<Members> membersList = null;
        if (orderJoinDate.getVisibility() == View.VISIBLE) {
            membersList = queryMembersDate(count);
        } else if (orderTotalIntegral.getVisibility() == View.VISIBLE) {
            membersList = queryMembersIntegral(count);
        } else {
            membersList = queryMembersNormal(count);
        }
        if (isForstPage) {
            Members members = new Members();
            if (membersList == null || membersList.size() < pageCount) {
                members.setId(-1);
            }
            membersList.add(members);
            adapter.setDataList(membersList);
        } else {
            recyclerView.setLoadMoreComplete();
            if (membersList != null && membersList.size() > 0) {
                if (membersList.size() < pageCount) {
                    Members members = adapter.getItem(adapter.getItemCount() - 1);
                    members.setId(-1);
                }
                adapter.addDataList(membersList, adapter.getItemCount() - 1);
            } else {
                Members members = adapter.getItem(adapter.getItemCount() - 1);
                members.setId(-1);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private List<Members> queryMembersNormal(int count) {
        Box<Members> membersBox = ((App) getApplication()).getBoxStore().boxFor(Members.class);
        QueryBuilder<Members> queryBuilder = membersBox.query();
        Query<Members> query = queryBuilder.build();
        return query.find(count, pageCount);
    }

    private List<Members> queryMembersDate(int count) {
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
        return query.find(count, pageCount);
    }

    private List<Members> queryMembersIntegral(int count) {
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
        return query.find(count, pageCount);
    }

    @Override
    public void onWinMenuItemClick(int whitch) {
        switch (whitch) {
            case 0:
                popWindow.dismiss();
                AddMemberActivity.intent(getApplicationContext());
                break;
            case 1:
                popWindow.dismiss();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType(“image/*”);//选择图片
                //intent.setType(“audio/*”); //选择音频
                //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
                //intent.setType(“video/*;image/*”);//同时选择视频和图片
                intent.setType("application/x-msaccess");//无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, CHOICE_FILE_CODE);
                break;
            case 2:
                popWindow.dismiss();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        DialogUtil.dialogOKorCancel(this, "提示", "打开权限", R.string.sOk, R.string.sCancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
                                        break;
                                }
                            }
                        });
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
                    }
                } else {
                    LoadingDialogFragment loadingDialogFragment = DialogUtil.dialogLoading(getSupportFragmentManager(), "正在复制，请稍后");
                    FileUtil.exportDb(getApplicationContext());
                    DialogUtil.dialogLoadingDismiss(loadingDialogFragment);
                    Toast.makeText(getApplicationContext(), "导出完成，文件夹名称为<zhongyouxiyingvip>", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddMemberEvent event) {
        if (adapter.getItemCount() - 1 < pageCount) {
            getMemberList(true);
        } else if (adapter.getItem(adapter.getItemCount() - 1).getId() == -1) {
            getMemberList(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EditMemberEvent event) {
        MemberBeanAndPosition memberBeanAndPosition = findMember(event.members.getId());
        if (memberBeanAndPosition.members != null) {
            memberBeanAndPosition.members.setName(event.members.getName());
            memberBeanAndPosition.members.setCardId(event.members.getCardId());
            memberBeanAndPosition.members.setCarNumber(event.members.getCarNumber());
            memberBeanAndPosition.members.setPhoneNumber(event.members.getPhoneNumber());
            memberBeanAndPosition.members.setLTotalNumber(event.members.getLTotalNumber());
            memberBeanAndPosition.members.setTotalIntegral(event.members.getTotalIntegral());
            memberBeanAndPosition.members.setCreateTime(event.members.getCreateTime());
            memberBeanAndPosition.members.setUpdateTime(event.members.getUpdateTime());
            adapter.notifyItemChanged(memberBeanAndPosition.position);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteMemberEvent event) {
        MemberBeanAndPosition memberBeanAndPosition = findMember(event.MemberId);
        if (memberBeanAndPosition.members != null) {
            adapter.removeItem(memberBeanAndPosition.position);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LNumberChangeEvent event) {
        MemberBeanAndPosition memberBeanAndPosition = findMember(event.MemberId);
        if (memberBeanAndPosition.members != null) {
            memberBeanAndPosition.members.setLTotalNumber(event.LNumberTotal);
            memberBeanAndPosition.members.setTotalIntegral(event.integralNumberTotal);
            adapter.notifyItemChanged(memberBeanAndPosition.position);
        }
    }

    private MemberBeanAndPosition findMember(long memId) {
        MemberBeanAndPosition memberBeanAndPosition = new MemberBeanAndPosition();
        int count = adapter.getItemCount();
        for (int i = 0; i < count; i++) {
            memberBeanAndPosition.members = adapter.getItem(i);
            if (memberBeanAndPosition.members != null && memberBeanAndPosition.members.getId() == memId) {
                memberBeanAndPosition.position = i;
                break;
            }
        }
        return memberBeanAndPosition;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  permission has been granted, preview can be displayed
                LoadingDialogFragment loadingDialogFragment = DialogUtil.dialogLoading(getSupportFragmentManager(), "正在复制，请稍后");
                FileUtil.exportDb(getApplicationContext());
                DialogUtil.dialogLoadingDismiss(loadingDialogFragment);
                Toast.makeText(getApplicationContext(), "导出完成，文件夹名称为<zhongyouxiyingvip>", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "没有获取到写权限", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoadingDialogFragment loadingDialogFragment = DialogUtil.dialogLoading(getSupportFragmentManager(), "正在复制，请稍后");
                FileUtil.importDb(getApplicationContext(), choiceFileUri);
                DialogUtil.dialogLoadingDismiss(loadingDialogFragment);
                Toast.makeText(getApplicationContext(), "导入完成，请在所有文件导入后重新打开app查看", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "没有获取到写权限", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                choiceFileUri = data.getData();
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    DialogUtil.dialogOKorCancel(this, "提示", "打开权限", R.string.sOk, R.string.sCancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
                                    break;
                            }
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
                }
            } else {
                LoadingDialogFragment loadingDialogFragment = DialogUtil.dialogLoading(getSupportFragmentManager(), "正在复制，请稍后");
                FileUtil.importDb(getApplicationContext(), data.getData());
                DialogUtil.dialogLoadingDismiss(loadingDialogFragment);
                Toast.makeText(getApplicationContext(), "导入完成，请在所有文件导入后重新打开app查看", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Utils.sysExit(getApplicationContext(), Calendar.getInstance().getTimeInMillis());
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
