package com.earth.OsToolkit.Working.BaseClass;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@SuppressWarnings("all")
public class Copy {
    public static int copyAssets2Cache(Context context, String fileName){
        File file = new File(context.getCacheDir()
                + File.separator
                + fileName);
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            if (!file.exists() || file.length() == 0) {
                FileOutputStream fos = new FileOutputStream(file);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1)
                    fos.write(buffer,0,len);
                fos.flush();
                inputStream.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            return 1;
        } else {
            return 0;
        }
    }

    public static boolean setScriptPermission(Context context, String fileName) {
        String path = context.getCacheDir()+ File.separator + fileName;
        File file = new File(path);
            file.setReadable(true);
            file.setWritable(true);
            file.setExecutable(true);
            return true;
    }
}