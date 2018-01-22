package com.skw.integralsys.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skw.integralsys.App;
import com.skw.integralsys.R;
import com.skw.integralsys.constants.Constants;
import com.skw.integralsys.datepicker.DatePicker4YearDialog;
import com.skw.integralsys.db.Integral;
import com.skw.integralsys.db.Integral_;
import com.skw.integralsys.db.Members;
import com.skw.integralsys.utils.DatePickerUtil;
import com.skw.integralsys.utils.DateUtil;
import com.skw.integralsys.utils.DialogUtil;
import com.skw.integralsys.utils.Utils;

import java.util.Calendar;
import java.util.Date;

import io.objectbox.Box;

/**
 * @创建人 weishukai
 * @创建时间 18/1/21 下午12:38
 * @类描述 一句话说明这个类是干什么的
 */

public class IntegralDetailActivity extends FragmentActivity implements View.OnClickListener, DatePicker4YearDialog.onDateListener, DialogInterface.OnDismissListener, DialogInterface.OnClickListener, DialogUtil.OnSureClickListener {
    private static final String key_id = "key_id";
    private Integral integral;
    private TextView LNumberText, integralText, createTimeText;

    public static void intent(Context context, Integral integral) {
        Intent intent = new Intent(context, IntegralDetailActivity.class);
        intent.putExtra(key_id, integral.getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_detail);
        getIntentData();
        initView();
        initData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        long id = intent.getLongExtra(key_id, 0);
        Box<Integral> integralBox = ((App) getApplication()).getBoxStore().boxFor(Integral.class);
        integral = integralBox.query().equal(Integral_.__ID_PROPERTY, id).build().findFirst();
        if (integral == null) {
            Toast.makeText(getApplicationContext(), R.string.err, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initView() {
        ImageView back = (ImageView) findViewById(R.id.back);
        TextView delete = (TextView) findViewById(R.id.delete);
        createTimeText = (TextView) findViewById(R.id.lDate);
        LNumberText = (TextView) findViewById(R.id.lNumber_i);
        integralText = (TextView) findViewById(R.id.integralNumber);
        back.setOnClickListener(this);
        delete.setOnClickListener(this);
        createTimeText.setOnClickListener(this);
        LNumberText.setOnClickListener(this);
    }

    private void initData() {
        createTimeText.setText(DateUtil.parceDateToStr(integral.getCreateTime()));
        LNumberText.setText("" + integral.getLNumber());
        integralText.setText("" + integral.getIntegral());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.delete:
                DialogUtil.dialogOKorCancel(this, R.string.hit, R.string.sureDele, R.string.sOk, R.string.sCancel, this);
                break;
            case R.id.lDate:
                Calendar calendar = Calendar.getInstance();
                if (integral.getCreateTime() != null) {
                    calendar.setTime(integral.getCreateTime());
                }
                DatePickerUtil.showDatePicker(this, calendar, this, this);
                break;
            case R.id.lNumber_i:
                DialogUtil.showEditDialog(this, "修改加油量", R.string.sOk, R.string.sCancel, this, LNumberText.getText().toString(), 1, InputType.TYPE_CLASS_NUMBER);
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void dateFinishYear(Calendar c, int type) {
        Date oldDate = integral.getCreateTime();
        integral.setCreateTime(c.getTime());
        if (updateIntegral(integral)) {
            createTimeText.setText(DateUtil.parceDateToStr(integral.getCreateTime()));
        } else {
            integral.setCreateTime(oldDate);
        }
    }

    private boolean updateIntegral(Integral integral) {
        Box<Integral> integralBox = ((App) getApplication()).getBoxStore().boxFor(Integral.class);
        integral.setUpdateTime(new Date());
        long id = integralBox.put(integral);
        if (id <= 0) {
            return false;
        }
        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
        return true;
    }


    private void deleteIntegral() {
        DialogUtil.dialogLoading(this, "正在删除...");
        Box<Integral> integralBox = ((App) getApplication()).getBoxStore().boxFor(Integral.class);
        integralBox.remove(integral.getId());
        DialogUtil.dialogLoadingDismiss(this);
        Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
        Utils.delayFinish(this, Constants.delayFinishTime);
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                deleteIntegral();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.cancel();
                break;
        }
    }

    @Override
    public void onSureClick(String value, int type) {
        switch (type) {
            case 1:
                if (value == null) {
                    value = "";
                }
                integral.setLNumber(Long.parseLong(value));
                integral.setIntegral(Long.parseLong(value));
                if (updateIntegral(integral)) {
                    LNumberText.setText("" + integral.getLNumber());
                    integralText.setText("" + integral.getIntegral());
                } else {
                    integral.setLNumber(Long.parseLong(LNumberText.getText().toString()));
                    integral.setIntegral(Long.parseLong(integralText.getText().toString()));
                }
                break;
        }
    }
}
