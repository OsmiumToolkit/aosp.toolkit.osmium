package com.earth.OsToolkit.base;
/*
 * OsToolkit - Kotlin
 *
 * Date : 11/1/2019
 *
 * By   : 1552980358
 *
 */

import android.app.ActivityManager;
import android.content.Context;

public class BaseJavaOperation {
    public static ActivityManager.MemoryInfo getMemory(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }
}
