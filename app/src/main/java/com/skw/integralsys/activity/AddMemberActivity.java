package com.skw.integralsys.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
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
import com.skw.integralsys.dialog.LoadingDialogFragment;
import com.skw.integralsys.eventbus.AddMemberEvent;
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
 * @创建时间 2018/1/16 15:50
 * @类描述 一句话描述 你的类
 */

public class AddMemberActivity extends FragmentActivity implements View.OnClickListener, DatePicker4YearDialog.onDateListener, DialogInterface.OnDismissListener {

    private EditText cardId, name, carNumber, phoneNumber, LNumber;

    private TextView createTime;

    private Date choiceCreateTime;

    public static void intent(Context context) {
        Intent intent = new Intent(context, AddMemberActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        initView();
    }

    private void initView() {
        ImageView back = (ImageView) findViewById(R.id.back);
        TextView save = (TextView) findViewById(R.id.save);
        cardId = (EditText) findViewById(R.id.cardId);
        name = (EditText) findViewById(R.id.name);
        carNumber = (EditText) findViewById(R.id.carNumber);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        LNumber = (EditText) findViewById(R.id.LNumber);
        createTime = (TextView) findViewById(R.id.createTime);
        choiceCreateTime = new Date();
        createTime.setText(DateUtil.parceDateToStr(choiceCreateTime));

        back.setOnClickListener(this);
        save.setOnClickListener(this);
        createTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                saveMember();
                break;
            case R.id.createTime:
                DatePickerUtil.showDatePicker(this, Calendar.getInstance(), this, this);
                break;
        }

    }

    private void saveMember() {
        LoadingDialogFragment loadingDialogFragment = DialogUtil.dialogLoading(getSupportFragmentManager(), "保存中...");
        Members member = new Members();
        member.setCardId(cardId.getText().toString());
        member.setName(name.getText().toString());
        member.setCarNumber(carNumber.getText().toString());
        member.setPhoneNumber(phoneNumber.getText().toString());
        String lNumber = LNumber.getText().toString();
        if (!TextUtils.isEmpty(lNumber)) {
            member.setLTotalNumber(Float.parseFloat(lNumber));
            member.setTotalIntegral(Float.parseFloat(lNumber));
        }
        member.setCreateTime(choiceCreateTime);
        member.setUpdateTime(new Date());

        if (TextUtils.isEmpty(member.getCardId()) && TextUtils.isEmpty(member.getName()) && TextUtils.isEmpty(member.getCarNumber()) && TextUtils.isEmpty(member.getPhoneNumber())) {
            Toast.makeText(getApplicationContext(), "请填写有效数据", Toast.LENGTH_SHORT).show();
            return;
        }

        Integral integral = new Integral();
        integral.getMembersToOne().setTarget(member);
        integral.setLNumber(member.getLTotalNumber());
        integral.setIntegral(member.getTotalIntegral());
        integral.setCreateTime(new Date());
        integral.setUpdateTime(new Date());

        Box<Integral> integralBox = ((App) getApplication()).getBoxStore().boxFor(Integral.class);
        long id = integralBox.put(integral);
        DialogUtil.dialogLoadingDismiss(loadingDialogFragment);
        if (id <= 0) {
            Toast.makeText(getApplicationContext(), "保存失败，请检查重试", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new AddMemberEvent());
        Utils.delayFinish(this, Constants.delayFinishTime);
    }


    @Override
    public void dateFinishYear(Calendar c, int type) {
        Log.d("aa", c.toString());
        choiceCreateTime = c.getTime();
        createTime.setText(DateUtil.parceDateToStr(choiceCreateTime));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.d("aa", "onDismiss");
    }

}
