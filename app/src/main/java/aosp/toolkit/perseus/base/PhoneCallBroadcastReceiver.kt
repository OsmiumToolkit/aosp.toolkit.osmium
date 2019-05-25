package aosp.toolkit.perseus.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import aosp.toolkit.perseus.SplashActivity
import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast


/*
 * @File:   PhoneCallBroadcastReceiver
 * @Author: 1552980358
 * @Time:   8:28 PM
 * @Date:   7 May 2019
 * 
 */

@Suppress("unused")
class PhoneCallBroadcastReceiver {
    class OnStartAppBroadcastReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                context!!.startActivity(Intent(Intent.ACTION_MAIN).setClass(context, SplashActivity::class.java).setFlags(FLAG_ACTIVITY_NEW_TASK))
            } catch (e: Exception) {
                ShortToast(context!!, e)
            }
        }
    }
}