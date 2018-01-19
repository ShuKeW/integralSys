package com.skw.integralsys.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.Window;

import com.skw.integralsys.datepicker.DatePicker4YearDialog;

import java.util.Calendar;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/19 11:09
 * @类描述 一句话描述 你的类
 */

public class DatePickerUtil {
    public static void showDatePicker(Context context, Calendar calendar, DatePicker4YearDialog.onDateListener listener, DialogInterface.OnDismissListener onDismissListener) {
        DatePicker4YearDialog datePicker4YearDialog = new DatePicker4YearDialog(context, calendar, "选择日期", 1);
        datePicker4YearDialog.addDateListener(listener);
        datePicker4YearDialog.setOnDismissListener(onDismissListener);
        Window window = datePicker4YearDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        datePicker4YearDialog.show();
    }
}
