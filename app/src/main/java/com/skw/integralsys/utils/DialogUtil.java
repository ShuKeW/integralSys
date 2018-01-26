package com.skw.integralsys.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.skw.integralsys.R;
import com.skw.integralsys.dialog.LoadingDialogFragment;

import java.util.List;

/**
 * @创建人 weishukai
 * @创建时间 18/1/21 下午2:17
 * @类描述 一句话说明这个类是干什么的
 */

public class DialogUtil {

    public interface OnSureClickListener {
        void onSureClick(String value, int type);
    }

    public static void dialogOKorCancel(Context context, int title, int msg, int sOk, int sCancel, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.KemaidDialogTheme);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(sOk, clickListener);
        builder.setNegativeButton(sCancel, clickListener);
        builder.show();
    }

    public static void showEditDialog(Context context, String title, int sOk, int sCancel, final OnSureClickListener listener, String oldValue, final int type, int inputType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit, null);
        final EditText editText = (EditText) view.findViewById(R.id.input);
        editText.setInputType(inputType);
        KeyBoardUtil.popInputMethod(editText);
        if (!TextUtils.isEmpty(oldValue)) {
            editText.setText(oldValue);
            editText.setSelection(oldValue.length());
        }
        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -1:
                        dialog.cancel();
                        listener.onSureClick(editText.getText().toString(), type);
                        break;
                }
            }
        };
        builder.setPositiveButton(sOk, clickListener);
        builder.setNegativeButton(sCancel, clickListener);
        AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void dialogLoading(FragmentManager fragmentManager, String msg) {
        if (fragmentManager != null) {
            LoadingDialogFragment loadingDialogFragment = (LoadingDialogFragment) fragmentManager.findFragmentByTag(LoadingDialogFragment.class.getName());
            if (loadingDialogFragment == null) {
                loadingDialogFragment = LoadingDialogFragment.getInstance(msg);
            }
            if (loadingDialogFragment.isAdded()) {
                loadingDialogFragment.setMsg(msg);
            } else {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(android.R.id.content, loadingDialogFragment, LoadingDialogFragment.class.getName()).commitNowAllowingStateLoss();
//                loadingDialogFragment.show(fragmentManager, LoadingDialogFragment.class.getName());
            }
        }
    }

    public static void dialogLoadingDismiss(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            LoadingDialogFragment loadingDialogFragment = (LoadingDialogFragment) fragmentManager.findFragmentByTag(LoadingDialogFragment.class.getName());
            if (loadingDialogFragment != null) {
//                loadingDialogFragment.dismissAllowingStateLoss();
                fragmentManager.beginTransaction().remove(loadingDialogFragment).commitNowAllowingStateLoss();
            }
        }

    }
}
