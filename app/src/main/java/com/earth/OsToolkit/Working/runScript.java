package com.earth.OsToolkit.Working;

import android.content.Context;

import java.io.File;

import static com.earth.OsToolkit.Working.BaseClass.Copy.copyAssets2Cache;

public class runScript {
    public static int run(Context context, String fileName) {
        int copy = copyAssets2Cache(context,fileName);
        if (copy != 0) {
            String path = context.getCacheDir().getAbsolutePath()
                    + File.separator
                    + fileName;
            try {
                Process process = Runtime.getRuntime().exec("chmod a+x " + path);
                if (process.waitFor() == 1) {
                    return 0;
                } else {
                    process = Runtime.getRuntime().exec(new String[]{"su -c",". " + path});
                    if (process.waitFor() == 0) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }
}