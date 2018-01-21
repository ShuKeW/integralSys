package com.skw.integralsys.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @创建人 weishukai
 * @创建时间 16/1/18 下午5:57
 * @类描述 键盘操作工具类
 */
public class KeyBoardUtil {

    /**
     * 弹出输入法
     *
     * @param editText
     */
    public static final void popInputMethod(final EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 500);
    }

    /**
     * 隐藏键盘
     */
    public static final void hideInputMethod(FragmentActivity fragmentActivity) {
        if (fragmentActivity != null) {
            InputMethodManager imm = (InputMethodManager) fragmentActivity.getSystemService(Context.INPUT_SERVICE);
            if (fragmentActivity.getWindow() != null && fragmentActivity.getWindow().getDecorView() != null) {
                imm.hideSoftInputFromWindow(fragmentActivity.getWindow().getDecorView().getApplicationWindowToken(), 2);
            }
        }
    }
}
