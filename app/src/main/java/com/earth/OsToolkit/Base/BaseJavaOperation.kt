package com.earth.OsToolkit.base

/*
 * OsToolkit - Kotlin
 *
 * Date : 11/1/2019
 *
 * By   : 1552980358
 *
 */

import android.app.ActivityManager
import android.content.Context

object BaseJavaOperation {
    fun getMemory(context: Context): ActivityManager.MemoryInfo {
        val memoryInfo = ActivityManager.MemoryInfo()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo
    }
}
