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
                return 1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            return 0;
        }
    }
}
