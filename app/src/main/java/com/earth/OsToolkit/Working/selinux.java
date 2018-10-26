package com.earth.OsToolkit.Working;

import android.content.Context;

import java.io.File;

import static com.earth.OsToolkit.Working.BaseClass.BaseIndex.SELINUX;
import static com.earth.OsToolkit.Working.BaseClass.Copy.copyAssets2Cache;

public class selinux {
    public static int run(Context context) {
        int copy = copyAssets2Cache(context,SELINUX);
        if (copy != 0) {
            String path = context.getCacheDir() + File.separator + SELINUX;
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
