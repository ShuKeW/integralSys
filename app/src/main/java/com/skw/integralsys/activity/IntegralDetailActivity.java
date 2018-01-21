package com.skw.integralsys.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skw.integralsys.App;
import com.skw.integralsys.R;
import com.skw.integralsys.constants.Constants;
import com.skw.integralsys.datepicker.DatePicker4YearDialog;
import com.skw.integralsys.db.Integral;
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

public class IntegralDetailActivity extends FragmentActivity implements View.OnClickListener, DatePicker4YearDialog.onDateListener, DialogInterface.OnDismissListener, DialogInterface.OnClickListener {
    private static final String key_id = "key_id";
    private static final String key_lnumber = "key_lnumber";
    private static final String key_integral = "key_integral";
    private static final String key_createTime = "key_createTime";
    private long integralId;
    private float LNumber, integral;
    private Date createTime;

    private TextView LNumberText, integralText, createTimeText;

    public static void intent(Context context, Integral integral) {
        Intent intent = new Intent(context, IntegralDetailActivity.class);
        intent.putExtra(key_id, integral.getId());
        intent.putExtra(key_lnumber, integral.getLNumber());
        intent.putExtra(key_integral, integral.getIntegral());
        if (integral.getCreateTime() != null) {
            intent.putExtra(key_createTime, integral.getCreateTime());
        }
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
        integralId = intent.getLongExtra(key_id, 0);
        LNumber = intent.getFloatExtra(key_lnumber, 0);
        integral = intent.getFloatExtra(key_integral, 0);
        if (intent.getSerializableExtra(key_createTime) != null) {
            createTime = (Date) intent.getSerializableExtra(key_createTime);
        }
    }

    private void initView() {
        ImageView back = (ImageView) findViewById(R.id.back);
        TextView delete = (TextView) findViewById(R.id.delete);
        createTimeText = (TextView) findViewById(R.id.lDate);
        LNumberText = (TextView) findViewById(R.id.lNumber);
        integralText = (TextView) findViewById(R.id.integralNumber);
        back.setOnClickListener(this);
        delete.setOnClickListener(this);
        createTimeText.setOnClickListener(this);
        LNumberText.setOnClickListener(this);
    }

    private void initData() {
        createTimeText.setText(DateUtil.parceDateToStr(createTime));
        LNumberText.setText("" + LNumber);
        integralText.setText("" + integral);
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
                if (createTime != null) {
                    calendar.setTime(createTime);
                }
                DatePickerUtil.showDatePicker(this, calendar, this, this);
                break;
            case R.id.LNumber:

                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void dateFinishYear(Calendar c, int type) {
        createTime = c.getTime();
        createTimeText.setText(DateUtil.parceDateToStr(createTime));
    }


    private void deleteIntegral() {
        DialogUtil.dialogLoading(this, "正在删除...");
        Box<Integral> integralBox = ((App) getApplication()).getBoxStore().boxFor(Integral.class);
        integralBox.remove(integralId);
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
}
