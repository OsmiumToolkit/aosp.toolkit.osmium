package aosp.toolkit.perseus.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import aosp.toolkit.perseus.BuildConfig
import org.jsoup.Jsoup


/*
 * @File:   UpdateService
 * @Author: 1552980358
 * @Time:   7:11 PM
 * @Date:   10 Apr 2019
 * 
 */

@SuppressLint("Registered")
class UpdateService: Service() {
    private lateinit var listener: UpdateServiceListener
    private lateinit var thread: Thread
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        listener = intent!!.extras!!.get("listener") as UpdateServiceListener
        Thread {
            val document = Jsoup.connect("").get()

            val v = document.getElementById("currentVersion").select("a").text()
            if (v == BuildConfig.VERSION_NAME) {

            }

        }.start()
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    interface UpdateServiceListener{
        fun onFinish() {
        }
        fun onUpdate(version: String,
                     date: String,
                     changelogZh: String,
                     changelogEn: String) {
        }
        fun onNewest() {
        }
    }
}