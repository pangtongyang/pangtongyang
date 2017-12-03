package lbcy.com.cn.wristband.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import lbcy.com.cn.wristband.R;

public class DialogUtil {
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    // 定义一个显示消息的对话框
    public static void showDialog(final Context ctx
            , String msg, boolean closeSelf) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setMessage(msg).setCancelable(false);
        if (closeSelf) {
            builder.setPositiveButton("确定", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // 结束当前Activity
                    ((Activity) ctx).finish();
                }
            });
        } else {
            builder.setPositiveButton("确定", null);
        }
        builder.create().show();
    }

    // 定义一个显示消息的对话框,添加listener
    public static void showDialog(final Context ctx
            , String msg, final DialogListener listener) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setMessage(msg).setCancelable(true);
        builder.setPositiveButton("确定", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listener.submit();
            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.cancel();
            }
        });
        builder.create().show();
    }

    // 定义一个显示标题、消息的对话框,添加listener
    public static void showDialog(final Context ctx, String title
            , String msg, final DialogListener listener) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setTitle(title).setMessage(msg).setCancelable(true);
        builder.setPositiveButton("确定", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listener.submit();
            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.cancel();
            }
        });
        builder.create().show();
    }

    public interface DialogListener {
        void submit();

        void cancel();
    }

    // 显示缺失权限提示
    public static void showMissingPermissionDialog(final Context ctx, final String permissionName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(R.string.help);
        builder.setMessage(R.string.string_help_text);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ctx, "未获取" + permissionName + "权限！", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings(ctx);
            }
        });

        builder.setCancelable(false);

        builder.create().show();
    }

    // 启动应用的设置
    private static void startAppSettings(Context ctx) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + ctx.getPackageName()));
        ctx.startActivity(intent);
    }

    // 定义一个显示指定组件的对话框
    public static void showDialog(Context ctx, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setView(view).setCancelable(false)
                .setPositiveButton("确定", null);
        builder.create()
                .show();
    }

}