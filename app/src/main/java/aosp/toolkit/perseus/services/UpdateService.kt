package aosp.toolkit.perseus.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import aosp.toolkit.perseus.BuildConfig
import aosp.toolkit.perseus.R
import aosp.toolkit.perseus.base.BaseManager
import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast
import org.jsoup.Jsoup
import java.lang.StringBuilder


/*
 * @File:   UpdateService
 * @Author: 1552980358
 * @Time:   7:11 PM
 * @Date:   10 Apr 2019
 * 
 */

@SuppressLint("Registered")
class UpdateService : Service() {
    private lateinit var listener: UpdateServiceListener
    //private lateinit var thread: Thread

    override fun onCreate() {
        super.onCreate()
        listener = BaseManager.getInstance().mainActivity as UpdateServiceListener
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        listener.onUpdateChecking()
        Thread {
            try {
                val document = Jsoup.connect("https://toolkitperseus.github.io/perseus.html").get()
                //Log.e("document", document.toString())

                val c = document.getElementById("currentCode").select("div").text()
                if (c == BuildConfig.VERSION_CODE.toString()) {
                    listener.onNewest(
                        c,
                        document.getElementById("currentVersion").select("a").text()
                    )
                } else {
                    val v = document.getElementById("currentVersion").select("a")
                    val vName = v.text()
                    val url = v.attr("href")

                    val zh = StringBuilder()
                    val en = StringBuilder()
                    for (i in document.getElementById("changelogZh").select("li")) {
                        zh.append(i.text().plus("\n"))
                    }
                    for (i in document.getElementById("changelogEn").select("li")) {
                        en.append(i.text().plus("\n"))
                    }

                    listener.onUpdate(
                        vName,
                        url,
                        document.getElementById("date").select("div").text(),
                        if (zh.isEmpty()) {
                            getText(R.string.toast_failed).toString()
                        } else {
                            zh.toString()
                        },
                        if (en.isEmpty()) {
                            getText(R.string.toast_failed).toString()
                        } else {
                            en.toString()
                        }
                    )
                }
            } catch (e: Exception) {
                ShortToast(BaseManager.getInstance().mainActivity, e, false)
            }
        }.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    interface UpdateServiceListener {
        fun onUpdateChecking() {
        }

        /*
         * interface onUpdate
         * @param version
         * @param url
         * @param date
         * @param changelogZh
         * @param changelogEn
         *
         * return void
         *
         */
        fun onUpdate(
            version: String, url: String, date: String, changelogZh: String, changelogEn: String
        ) {

        }

        fun onNewest(version: String, code: String) {
        }
    }
}