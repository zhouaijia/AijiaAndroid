package com.aijia.framework.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;


/**
 * 文件资源尽量先往外部存储空间存放，如Android/data/包名/files。
 * 若外部存储空间不可用，可试着往内部中存放，如data/data/包名/files。
 * 但是，内部存储空间是有限制的
 */
public class PathUtils {
    public static final String TAG = PathUtils.class.getSimpleName();

    public static final String CRM_ONLINE = "/online/";
    public static final String OFFLINE = "/offline/";
    public static final String TEMP = "temp";
    public static final String OFFLINE_TEMP = "offlineTemp";
    public static final String REKTEC = "/rektec/";
    public static final String OFFLINE_H5 = "/www/";
    public static final String CRM_ONLINE_H5 = "/crmwww/";


    /**
     * 判断根目录是否已挂载
     * @return 如果已挂载，则返回true，否则返回false
     */
    public static boolean isMounted() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    /**
     * 获取存文件储位置
     * @return 目标位置file对象
     */
    public static File getStorageFileDir() {
        return getStorageFileDir(Utils.getApp(), null);
    }

    /**
     * 获取rektec文件存储位置
     * @return 目标位置file对象
     */
    public static File getRektecExternalFile() {
        return getStorageFileDir(Utils.getApp(), REKTEC);
    }

    /**
     * 先从Android/data/包名/....下面获取路径，如果失败，再从data/data/包名/...下获取路径
     * @param context 应用上下文
     * @param subPath 子路径
     * @return 目标位置file对象
     */
    public static File getStorageFileDir(Context context, String subPath) {
        File storageFile = getExternalFileDirectory(context, subPath);

        if (storageFile == null) {
            storageFile = getInternalFileDirectory(context, subPath);
        }
        if (storageFile == null) {
            Log.e(TAG, "getStorageFileDir fail , ExternalFile and InternalFile both unavailable ");
        } else if (!storageFile.exists() && !storageFile.mkdirs()) {
            Log.e(TAG, "getStorageFileDir fail ,the reason is make directory fail !");
        }

        return storageFile;
    }

    /**
     * 外部存储目录：Android/data/包名/files
     * @param context 应用上下文
     * @param subPath 子路径
     * @return 目标file对象
     */
    public static File getExternalFileDirectory(Context context, String subPath) {
        File externalFile = null;
        if (isMounted()) {
            if (TextUtils.isEmpty(subPath)) {
                externalFile = context.getExternalFilesDir(null);
            } else {
                externalFile = context.getExternalFilesDir(subPath);
            }
            if (externalFile == null) {
                externalFile = new File(Environment.getExternalStorageDirectory(),
                        "Android/data/" + context.getPackageName() + "/files/" + subPath);
            }

            if (!externalFile.exists() && !externalFile.mkdirs()) {
                Log.e(TAG, "getExternalFileDirectory fail ,the reason is make directory fail ");
            }
        } else {
            Log.e(TAG, "getExternalFileDirectory fail ,the reason is sdCard unMounted ");
        }

        return externalFile;
    }

    /**
     * 内部存储目录：data/data/包名/files
     * @param context 应用上下文
     * @param subPath 子路径
     * @return 目标file对象
     */
    public static File getInternalFileDirectory(Context context, String subPath) {
        File internalFile;
        if (TextUtils.isEmpty(subPath)) {
            internalFile = context.getFilesDir();
        } else {
            internalFile = new File(context.getFilesDir(), subPath);
        }
        if (!internalFile.exists() && !internalFile.mkdirs()) {
            Log.e(TAG, "getInternalFileDirectory fail ,the reason is make directory fail !");
        }

        return internalFile;
    }

    /***
     * Online模式H5压缩包解压缩的位置：data/data/[包名]/files/crmwww/...
     * @param context 上下文
     * @return 目录路径
     */
    public static String getCrmH5Dir(Context context) {
        return context.getFilesDir().getAbsolutePath() + CRM_ONLINE_H5;
    }

    /***
     * Offline模式H5压缩包所处的位置：data/data/[包名]/files/www/...
     * @param context 上下文
     * @return 目录路径
     */
    public static String getOfflineH5Dir(Context context) {
        return context.getFilesDir().getAbsolutePath() + OFFLINE_H5;
    }

    public static File getCacheDir(Context context) {
        return context.getExternalCacheDir();
    }



    public static File getDefaultTempDir(Context context) {
        return context.getExternalFilesDir(TEMP);
    }

    public static File getOfflineTempDir(Context context) {
        return context.getExternalFilesDir(OFFLINE_TEMP);
    }

    public static File getDownloadDir(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
    }

    public static String getJPushSoundRelativeDir() {
        return ""; //"/sound/";直接存储在Download目录下
    }

    public static String getJPushSoundDir(Context context) {
        return getDownloadDir(context).getAbsolutePath() + getJPushSoundRelativeDir();
    }

    public static String getOfflineDownloadDir(Context context) {
        return getDownloadDir(context).getAbsolutePath() + OFFLINE;
    }

    public static String getCrmOnlineDownloadDir(Context context, boolean checkCacheSize) {
        String path = getCrmOnlineDownloadDir(context);
        if (checkCacheSize) checkCacheOut(path);

        return path;
    }

    public static String getCrmOnlineDownloadDir(Context context) {
        return getDownloadDir(context).getAbsolutePath() + CRM_ONLINE;
    }

    /**
     * 检查预览文件的目录下文件数量是否超过了最大缓存值 100，
     * 若超过了，则删除时间靠前的50张图片
     * @param dir 目标目录
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void checkCacheOut(String dir) {
        File fileDir = FileUtils.getFileByPath(dir);
        if (!FileUtils.isDir(fileDir)) {
            return;
        }

        File[] files = fileDir.listFiles();
        if (files != null && files.length > 100) {
            //按时间从远到近排序
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            //超过100后清除时间考前的50张图片
            for (int i = 0; i < 50; i++) {
                FileUtils.delete(files[i]);
            }
        }
    }

    public static String combine(String path1, String path2) {
        if (path1 == null) {
            path1 = "";
        } else if (path1.endsWith("/")) {
            path1 = path1.substring(path1.length() - 1);
        }

        if (path2 == null) {
            path2 = "";
        } else if (!path2.startsWith("/")) {
            path2 = "/" + path2;
        }

        return path1 + path2;
    }
}
