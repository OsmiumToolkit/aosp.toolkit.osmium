package aosp.toolkit.perseus.services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat

import aosp.toolkit.perseus.DownloadActivity
import aosp.toolkit.perseus.R
import aosp.toolkit.perseus.base.DownloadUtil

import java.lang.Exception


/*
 * @File:   DownloadService
 * @Author: 1552980358
 * @Time:   5:43 PM
 * @Date:   15 Apr 2019
 * 
 */

class DownloadService : Service() {

    companion object {
        const val NOTIFY_ID = 100
    }

    override fun onBind(intent: Intent?): IBinder? {
        return ServiceBinder(this)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val builder = NotificationCompat.Builder(applicationContext, "Notify")
        // 构建通知
        builder.setContentTitle(applicationContext.getString(R.string.downloading))
        builder.setSmallIcon(R.drawable.ic_download)
        builder.setOngoing(true)
        builder.priority = NotificationCompat.PRIORITY_MAX

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        builder.setContentText("0%")
        builder.setProgress(100, 0, false)

        Thread {
            val data = intent!!.getSerializableExtra("data") as DownloadActivity.Data
            val fileName = data.getFileName()
            builder.setSubText(fileName)
            val filePath = data.getFilePath()
            val url = data.getUrl()
            val listener = data.getListener()

            DownloadUtil(url, filePath, fileName, object : DownloadActivity.DownloadInterface {
                override fun onStartTask() {
                    super.onStartTask()
                    listener.onStartTask()

                    notificationManager.notify(NOTIFY_ID, builder.build())
                }

                override fun onProcessChange(p: Int) {
                    super.onProcessChange(p)
                    listener.onProcessChange(p)

                    builder.setContentText("$p%")
                    builder.setProgress(100, p, false)
                    notificationManager.notify(NOTIFY_ID, builder.build())
                }

                override fun onTaskFail(e: Exception) {
                    super.onTaskFail(e)
                    listener.onTaskFail(e)
                }

                override fun onTaskFinished(file: String, size: Long) {
                    super.onTaskFinished(file, size)
                    // 数据返回Activity
                    listener.onTaskFinished(file, size)

                    builder.setContentText("Finished")
                    builder.setOngoing(false)
                    builder.setProgress(100, 0, false)
                    notificationManager.notify(NOTIFY_ID, builder.build())
                }
            }).start()
        }.start()
        return super.onStartCommand(intent, flags, startId)
    }

    class ServiceBinder(private val service: DownloadService): Binder() {
        fun getService(): DownloadService {
            return this.service
        }
    }
}