package com.skw.integralsys.popwindow;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.skw.integralsys.R;
import com.skw.integralsys.utils.ScreenUtils;


/**
 * @创建人 weishukai
 * @创建时间 16/1/29 下午7:29
 * @类描述 会员详情页右上角弹出的window
 */
public class MemMoreWindow implements View.OnClickListener {

    private PopupWindow popupWindow;

    private OnWinMenuItemClickListener listener;

    private MemMoreWindow() {

    }

    public MemMoreWindow(OnWinMenuItemClickListener listener) {
        this.listener = listener;
    }

    public void showPopWindow(Context context, View targetView) {
        if (popupWindow == null) {
            View popView = LayoutInflater.from(context).inflate(R.layout.layout_menu_mem, null);
            TextView addIntegral = (TextView) popView.findViewById(R.id.addIntegral);
            TextView deleteMem = (TextView) popView.findViewById(R.id.deleteMem);
            addIntegral.setOnClickListener(this);
            deleteMem.setOnClickListener(this);
            int width = getWindowWidth(context);
            popupWindow = new PopupWindow(popView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setAnimationStyle(R.style.popWinAnim);
        }
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(targetView);
    }

    public void dismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    private int getWindowWidth(Context context) {
        Point point = ScreenUtils.getScreenSize(context);
        return point.x / 3;
    }

    @Override
    public void onClick(View v) {
        int index = -1;
        switch (v.getId()) {
            case R.id.addIntegral:
                index = 0;
                break;
            case R.id.deleteMem:
                index = 1;
                break;
        }
        if (listener != null) {
            listener.onWinMenuItemClick(index);
        }
    }
}
