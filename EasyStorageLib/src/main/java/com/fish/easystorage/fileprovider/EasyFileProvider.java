package com.fish.easystorage.fileprovider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;

public class EasyFileProvider {
    private static final String TAG = EasyFileProvider.class.getSimpleName();

    public static Uri getUri(Context context, File file) {
        if (context == null || file == null)
            return null;

        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = MyFileProvider.getUriForFile(context, context.getPackageName() + ".easy.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static Uri getUri(Context context, String filePath) {
        if (context == null || TextUtils.isEmpty(filePath))
            return null;
        try {
            File file = new File(filePath);
            return getUri(context, file);
        } catch (Exception e) {
            Log.e(TAG, "error file:" + e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * @param context
     * @param file
     * @param intent
     * @param canWrite 赋予uri接收方写文件的权限
     */
    public static void fillIntent(Context context, File file, Intent intent, boolean canWrite) {
        if (context == null || file == null || intent == null)
            return;

        Uri uri = getUri(context, file);
        if (uri != null) {
            String fileName = file.getName();
            String extension = null;
            if (!TextUtils.isEmpty(fileName)) {
                int index = fileName.lastIndexOf(".");
                if (index >= 0) {
                    extension = fileName.substring(index + 1);
                }
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (canWrite) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            String mimeType = "*/*";
            if (!TextUtils.isEmpty(extension)) {
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }

            intent.setDataAndType(uri, mimeType);
        }
    }

    /**
     *
     * @param context
     * @param file
     * @param intent
     * @param mimeType 指定的文件类型
     * @param canWrite
     */
    public static void fillIntent(Context context, File file, Intent intent, String mimeType, boolean canWrite) {
        if (context == null || file == null || intent == null)
            return;

        Uri uri = getUri(context, file);
        if (uri != null) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (canWrite) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            if (TextUtils.isEmpty(mimeType)) {
                intent.setData(uri);
            } else {
                intent.setDataAndType(uri, mimeType);
            }
        }
    }

}
