package com.yueyue.rxjavademo.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * author : yueyue on 2018/4/19 10:24
 * desc   :
 */
public class FileUtil {

    private FileUtil() {

    }


    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
}
