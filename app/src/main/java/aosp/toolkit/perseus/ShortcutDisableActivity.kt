package aosp.toolkit.perseus

import android.app.Activity
import android.content.Context
import android.os.Bundle

import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast

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
            }
            ShortToast(this, String.format(getString(R.string.ss_toast), savedList.size), false)
        }.start()
    }

    override fun onResume() {
        finish()
        super.onResume()
    }
}
