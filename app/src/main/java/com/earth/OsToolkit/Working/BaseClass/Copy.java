package com.earth.OsToolkit.Working.BaseClass;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@SuppressWarnings("all")
public class Copy {
    public static int copyAssets2Cache(Context context, String fileName){
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            File file = new File(context.getCacheDir().getAbsolutePath()
                    + File.separator
                    + fileName);

            if (!file.exists() || file.length() == 0) {
                FileOutputStream fos = new FileOutputStream(file);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1)
                    fos.write(buffer,0,len);
                fos.flush();
                inputStream.close();
                fos.close();

                // 过程完成
                // Process succeed
                return 1;
            } else {
                // 过程失败
                // Process failed
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 出现错误
            // Error occur
            return -1;
        }
    }

    public static int setScriptPermission(Context context, String fileName) {
        try {
            String path = context.getCacheDir().getAbsolutePath() + File.separator + fileName;
            Process process = Runtime.getRuntime().exec(new String[]{"chmod 777 ",path});
            if (process.waitFor() == 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}