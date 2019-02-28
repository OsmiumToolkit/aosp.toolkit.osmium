package com.earth.OsToolkit

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log

import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.ShortToast

import com.topjohnwu.superuser.Shell

/*
 * OsToolkit - Kotlin
 *
 * Date : 26 Feb 2019
 *
 * By   : 1552980358
 *
 */

class ShortcutDisableActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread {
            val savedList = getSharedPreferences("disabledApp", Context.MODE_PRIVATE)
                .getStringSet("added", setOf<String>())
            for (i in savedList!!) {
                Shell.su("pm disable $i").exec()
                Log.i("SDA", i)
            }
            runOnUiThread {
                ShortToast(this, "Finish")
            }
        }.start()
    }

    override fun onResume() {
        finish()
        super.onResume()
    }
}
