package com.practise.cq.weibotest.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.practise.cq.weibotest.R;

import java.util.List;

/**
 * Created by CQ on 2016/5/23 0023.
 */
public class DialogUtils {

    public static Dialog createLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.DialogCommon);
        return progressDialog;
    }

    /**
     * 提示信息dialog
     *
     * @param context
     * @param title
     *            标题名称,内容为空时即不设置标题
     * @param msg
     *            提示信息内容
     * @return
     */
    public static AlertDialog showMsgDialog(Context context, String title,
                                            String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        AlertDialog dialog = builder.setMessage(msg)
                .setNegativeButton("确定", null).show();
        return dialog;
    }

    /**
     * 确认dialog
     *
     * @param context
     * @param title
     *            标题名称,内容为空时即不设置标题
     * @param msg
     *            确认信息内容
     * @param onClickListener
     *            确定按钮监听
     * @return
     */
    public static AlertDialog showConfirmDialog(Context context, String title,
                                                String msg, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        AlertDialog dialog = builder.setMessage(msg)
                .setPositiveButton("确认", onClickListener)
                .setNegativeButton("取消", null).show();
        return dialog;
    }

    /**
     * 列表型dialog
     *
     * @param context
     * @param title
     *            标题名称,内容为空时即不设置标题
     * @param items
     *            所有item选项的名称
     * @param onClickListener
     *            确定按钮监听
     * @return
     */
    public static AlertDialog showListDialog(Context context, String title,
                                             List<String> items, DialogInterface.OnClickListener onClickListener) {
        return showListDialog(context, title,
                items.toArray(new String[items.size()]), onClickListener);
    }

    /**
     * 列表型dialog
     *
     * @param context
     * @param title
     *            标题名称,内容为空时即不设置标题
     * @param items
     *            所有item选项的名称
     * @param onClickListener
     *            确定按钮监听
     * @return
     */
    public static AlertDialog showListDialog(Context context, String title,
                                             String[] items, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        AlertDialog dialog = builder.setItems(items, onClickListener).show();
        return dialog;
    }

    /**选择照片弹出框*/
    public static void showPickImgDialog(final Activity activity){
        String title = "选择照片";
        String[] items = new String[]{"拍照", "相册"};
        showListDialog(activity, title, items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();

                switch (which){
                    case 0:
                        ImgUtils.picFromCamera(activity);
                        break;
                    case 1:
                        ImgUtils.picFromAlbum(activity);
                }
            }
        });
    }
}
