package com.angle.netupdatademo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * @author : Angle
 * 创建时间 : 2019/4/22 23:08
 * 描述 : 工具包
 */
public class UriparseUtils {
    /**
     * 创建一个图片文件输出路径的uri
     *
     * @param context 上线文
     * @param file    文件路径
     * @return uri地址
     */
    private static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context, getFileProvider(context), file);
    }

    /**
     * 获取fileProvider路径 适配6.0
     *
     * @param context 上下文
     * @return fileProvider路径
     */
    private static String getFileProvider(Context context) {
        return context.getApplicationInfo().packageName + ".fileprovider";
    }

    /**
     * 安装apk
     */
    public static void installApk(Activity activity, File apkFile) {
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Uri fileUri = getUriForFile(activity, apkFile);
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        activity.startActivity(intent);
    }

}
