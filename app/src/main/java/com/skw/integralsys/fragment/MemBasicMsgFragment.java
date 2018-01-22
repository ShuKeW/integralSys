package com.skw.integralsys.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skw.integralsys.App;
import com.skw.integralsys.R;
import com.skw.integralsys.datepicker.DatePicker4YearDialog;
import com.skw.integralsys.db.Members;
import com.skw.integralsys.db.Members_;
import com.skw.integralsys.utils.DatePickerUtil;
import com.skw.integralsys.utils.DateUtil;
import com.skw.integralsys.utils.DialogUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/16 16:47
 * @类描述 一句话描述 你的类
 */

public class MemBasicMsgFragment extends Fragment implements View.OnClickListener, DialogUtil.OnSureClickListener, DatePicker4YearDialog.onDateListener, DialogInterface.OnDismissListener {
    private static final String key_MId = "key_MId";
    private long MId;
    private Members member;
    private TextView cardId, name, carNumber, phoneNumber, joinDate, joinDateLength, totalLNumber, totalIntegral;

    public static MemBasicMsgFragment getInstance(long MId) {
        MemBasicMsgFragment fragment = new MemBasicMsgFragment();
        Bundle args = new Bundle();
        args.putLong(key_MId, MId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        MId = args.getLong(key_MId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_basic_msg, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMemberBasicMsg();
    }

    private void getMemberBasicMsg() {
        Box<Members> membersBox = ((App) getActivity().getApplicationContext()).getBoxStore().boxFor(Members.class);
        QueryBuilder<Members> queryBuilder = membersBox.query();
        Query<Members> query = queryBuilder.equal(Members_.__ID_PROPERTY, MId).build();
        member = query.findFirst();
        cardId.setText(member.getCardId());
        name.setText(member.getName());
        carNumber.setText(member.getCarNumber());
        phoneNumber.setText(member.getPhoneNumber());
        joinDate.setText(DateUtil.parceDateToStr(member.getCreateTime()));
        if (member.getCreateTime() != null) {
            Date now = new Date();
            joinDateLength.setText((now.getTime() - member.getCreateTime().getTime()) / (1000 * 60 * 60 * 24) + "天");
        }
        totalLNumber.setText("" + member.getLTotalNumber());
        totalIntegral.setText("" + member.getTotalIntegral());
    }

    private void initView(View view) {
        cardId = (TextView) view.findViewById(R.id.cardId);
        name = (TextView) view.findViewById(R.id.name);
        carNumber = (TextView) view.findViewById(R.id.carNumber);
        phoneNumber = (TextView) view.findViewById(R.id.phoneNumber);
        joinDate = (TextView) view.findViewById(R.id.joinDate);
        joinDateLength = (TextView) view.findViewById(R.id.joinDateLength);
        totalLNumber = (TextView) view.findViewById(R.id.LNumberTotal);
        totalIntegral = (TextView) view.findViewById(R.id.integralTotal);
        cardId.setOnClickListener(this);
        name.setOnClickListener(this);
        carNumber.setOnClickListener(this);
        phoneNumber.setOnClickListener(this);
        joinDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardId:
                DialogUtil.showEditDialog(this.getActivity(), "修改卡号", R.string.sOk, R.string.sCancel, this, cardId.getText().toString(), 1, InputType.TYPE_CLASS_NUMBER);
                break;
            case R.id.name:
                DialogUtil.showEditDialog(this.getActivity(), "修改名字", R.string.sOk, R.string.sCancel, this, name.getText().toString(), 2, InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.carNumber:
                DialogUtil.showEditDialog(this.getActivity(), "修改车牌号", R.string.sOk, R.string.sCancel, this, carNumber.getText().toString(), 3, InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.phoneNumber:
                DialogUtil.showEditDialog(this.getActivity(), "修改电话", R.string.sOk, R.string.sCancel, this, phoneNumber.getText().toString(), 4, InputType.TYPE_CLASS_PHONE);
                break;
            case R.id.joinDate:
                Calendar calendar = Calendar.getInstance();
                if (member.getCreateTime() != null) {
                    calendar.setTime(member.getCreateTime());
                }
                DatePickerUtil.showDatePicker(getActivity(), calendar, this, this);
                break;
        }

    }

    @Override
    public void onSureClick(String value, int type) {
        switch (type) {
            case 1:
                member.setCardId(value);
                if (updateMember(member)) {
                    cardId.setText(member.getCardId());
                } else {
                    member.setCardId(cardId.getText().toString());
                }
                break;
            case 2:
                member.setName(value);
                if (updateMember(member)) {
                    name.setText(member.getName());
                } else {
                    member.setName(cardId.getText().toString());
                }
                break;
            case 3:
                member.setCarNumber(value);
                if (updateMember(member)) {
                    carNumber.setText(member.getCarNumber());
                } else {
                    member.setCarNumber(cardId.getText().toString());
                }
                break;
            case 4:
                member.setPhoneNumber(value);
                if (updateMember(member)) {
                    phoneNumber.setText(member.getPhoneNumber());
                } else {
                    member.setPhoneNumber(cardId.getText().toString());
                }
                break;
        }
    }

    private boolean updateMember(Members members) {
        Box<Members> membersBox = ((App) (getActivity().getApplication())).getBoxStore().boxFor(Members.class);
        members.setUpdateTime(new Date());
        long id = membersBox.put(members);
        if (id <= 0) {
            return false;
        }
        Toast.makeText(getActivity().getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
        return true;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void dateFinishYear(Calendar c, int type) {
        Date oldDate = member.getCreateTime();
        member.setCreateTime(c.getTime());
        if (updateMember(member)) {
            joinDate.setText(DateUtil.parceDateToStr(member.getCreateTime()));
            Date now = new Date();
            joinDateLength.setText((now.getTime() - member.getCreateTime().getTime()) / (1000 * 60 * 60 * 24) + "天");
        } else {
            member.setCreateTime(oldDate);
        }
    }
}
