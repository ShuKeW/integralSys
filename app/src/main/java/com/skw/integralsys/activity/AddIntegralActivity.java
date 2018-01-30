package com.skw.integralsys.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skw.integralsys.App;
import com.skw.integralsys.R;
import com.skw.integralsys.constants.Constants;
import com.skw.integralsys.datepicker.DatePicker4YearDialog;
import com.skw.integralsys.db.Integral;
import com.skw.integralsys.db.Members;
import com.skw.integralsys.db.Members_;
import com.skw.integralsys.dialog.LoadingDialogFragment;
import com.skw.integralsys.eventbus.AddIntegralEvent;
import com.skw.integralsys.eventbus.LNumberChangeEvent;
import com.skw.integralsys.utils.DatePickerUtil;
import com.skw.integralsys.utils.DateUtil;
import com.skw.integralsys.utils.DialogUtil;
import com.skw.integralsys.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

import io.objectbox.Box;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/22 16:19
 * @类描述 一句话描述 你的类
 */

public class AddIntegralActivity extends FragmentActivity implements View.OnClickListener, TextWatcher, DatePicker4YearDialog.onDateListener, DialogInterface.OnDismissListener {
    private static final String key_mem_id = "key_mem_id";
    private Members members;
    private TextView LNumberDate;
    private EditText LNumber;
    private TextView integralNumber;
    private Date choiceLNumberDate;

    public static void intent(Context applicationContext, long memId) {
        Intent intent = new Intent(applicationContext, AddIntegralActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(key_mem_id, memId);
        applicationContext.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_integral);
        getIntentData();
        initView();
    }

    private void getIntentData() {
        long memId = getIntent().getLongExtra(key_mem_id, 0);
        Box<Members> membersBox = ((App) getApplication()).getBoxStore().boxFor(Members.class);
        members = membersBox.query().equal(Members_.__ID_PROPERTY, memId).build().findFirst();
        if (members == null) {
            Toast.makeText(getApplicationContext(), R.string.err, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initView() {
        ImageView back = (ImageView) findViewById(R.id.back);
        TextView save = (TextView) findViewById(R.id.save);
        LNumberDate = (TextView) findViewById(R.id.LNumberDate);
        LNumber = (EditText) findViewById(R.id.LNumber);
        integralNumber = (TextView) findViewById(R.id.integralNumber);
        choiceLNumberDate = new Date();
        LNumberDate.setText(DateUtil.parceDateToStr(choiceLNumberDate));
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        LNumberDate.setOnClickListener(this);
        LNumber.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                saveIntegral();
                break;
            case R.id.LNumberDate:
                Calendar calendar = Calendar.getInstance();
                if (choiceLNumberDate != null) {
                    calendar.setTime(choiceLNumberDate);
                }
                DatePickerUtil.showDatePicker(this, calendar, this, this);
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
        integralNumber.setText(s.toString());
    }


    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void dateFinishYear(Calendar c, int type) {
        choiceLNumberDate = c.getTime();
        LNumberDate.setText(DateUtil.parceDateToStr(choiceLNumberDate));
    }

    private void saveIntegral() {
        if (TextUtils.isEmpty(LNumber.getText().toString())) {
            Toast.makeText(getApplicationContext(), "请填写数据", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialogFragment loadingDialogFragment = DialogUtil.dialogLoading(getSupportFragmentManager(), "保存中...");
        float LNumberValue = Float.parseFloat(LNumber.getText().toString());
        float integralValue = Float.parseFloat(integralNumber.getText().toString());
        members.setLTotalNumber(members.getLTotalNumber() + LNumberValue);
        members.setTotalIntegral(members.getTotalIntegral() + integralValue);
        members.setUpdateTime(new Date());
        Box<Members> membersBox = ((App) getApplication()).getBoxStore().boxFor(Members.class);
        long memberId = membersBox.put(members);
        if (memberId <= 0) {
            Toast.makeText(getApplicationContext(), "保存失败，请检查重试", Toast.LENGTH_SHORT).show();
            return;
        }

        Integral integral = new Integral();
        integral.getMembersToOne().setTarget(members);
        integral.setLNumber(LNumberValue);
        integral.setIntegral(integralValue);
        integral.setCreateTime(choiceLNumberDate);
        integral.setUpdateTime(choiceLNumberDate);

        Box<Integral> integralBox = ((App) getApplication()).getBoxStore().boxFor(Integral.class);
        long id = integralBox.put(integral);
        DialogUtil.dialogLoadingDismiss(loadingDialogFragment);
        if (id <= 0) {
            Toast.makeText(getApplicationContext(), "保存失败，请检查重试", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new AddIntegralEvent());
        EventBus.getDefault().post(new LNumberChangeEvent(members.getId(), members.getLTotalNumber(), members.getTotalIntegral()));
        Utils.delayFinish(this, Constants.delayFinishTime);
    }

}
