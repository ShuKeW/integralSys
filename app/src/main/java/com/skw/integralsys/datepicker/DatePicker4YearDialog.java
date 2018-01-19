package com.skw.integralsys.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.skw.integralsys.R;
import com.skw.integralsys.utils.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @创建人 wangtao
 * @创建时间 15/10/09 14:57
 * @类描述 日期弹窗
 */
public class DatePicker4YearDialog extends Dialog {

    private TextView tv_sure;

    private TextView tv_cancel;

    private TextView select_date;

    private TextView date_picker_title;

    private onDateListener listener;

    private Calendar c = Calendar.getInstance();

    private CustomDatePickerForYear cdp;

    private String title;                            // 弹窗标题

    private String selectDate;                        // 选择的日期

    private int type;

    public DatePicker4YearDialog(Context context, Calendar c, String title, int type) {
        super(context, R.style.CustomDialog);
        this.c = c;
        this.title = title;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_datepicker_dialog_for_year);
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        // 设置dialog宽度等于屏幕的宽
        int width = ScreenUtils.getScreenSize(getContext()).x;
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = width;
        getWindow().setAttributes(attributes);

        setCanceledOnTouchOutside(true);
        tv_sure = (TextView) findViewById(R.id.tv_sure);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        select_date = (TextView) findViewById(R.id.select_date);
        date_picker_title = (TextView) findViewById(R.id.date_picker_title);
        date_picker_title.setText(title);
        cdp = (CustomDatePickerForYear) findViewById(R.id.cdp);
        cdp.setDate(c);

        initDate();

        cdp.addChangingListener(new CustomDatePickerForYear.ChangingListener() {

            @Override
            public void onChange(Calendar c1) {
                c = c1;
                initDate();
            }
        });
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v == tv_sure) {
                    if (listener != null) {
                        listener.dateFinishYear(c, type);
                    }
                }
                dismiss();
            }
        };

        tv_sure.setOnClickListener(clickListener);
        tv_cancel.setOnClickListener(clickListener);

    }

    /**
     * 设置限制日期，设置后，不能选择设置的开始日期以前的日期
     *
     * @param c
     */
    public void setNowData(Calendar c) {
        cdp.setNowData(c);
    }

    /**
     * 设置点击确认的事件
     *
     * @param listener
     */
    public void addDateListener(onDateListener listener) {
        this.listener = listener;
    }

    public interface onDateListener {

        void dateFinishYear(Calendar c, int type);
    }

    /**
     * 初始化日期
     */
    private void initDate() {
        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy年MM月dd日 E");
        selectDate = sdfFrom.format(c.getTime());
        select_date.setText(selectDate);
    }
}
