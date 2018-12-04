package com.earth.OsToolkit.Working.BaseClass;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@SuppressWarnings("all")
public class Copy {
    public static boolean copyAssets2Cache(Context context, String fileName) {
        File file = new File(context.getCacheDir()
                .getAbsolutePath()
                + File.separator
                + fileName);
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            if (!file.exists() || file.length() == 0) {
                FileOutputStream fos = new FileOutputStream(file);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1)
                    fos.write(buffer, 0, len);
                fos.flush();
                inputStream.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean setScriptPermission(Context context, String fileName) {
        String path = context.getCacheDir().getAbsolutePath() + File.separator + fileName;
        File file = new File(path);
        file.setReadable(true);
        file.setWritable(true);
        file.setExecutable(true);
        if (file.canExecute()) {
            return true;
        } else {
            return false;
        }
    }
}