package aosp.toolkit.perseus

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast
import aosp.toolkit.perseus.services.DownloadService
import kotlinx.android.synthetic.main.activity_download.*
import java.io.Serializable
import java.lang.Exception


/*
 * @File:   DownloadActivity
 * @Author: 1552980358
 * @Time:   5:12 PM
 * @Date:   15 Apr 2019
 * 
 */

class DownloadActivity : AppCompatActivity() {
    private lateinit var downloadService: DownloadService
    private var binded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        val data = Data(
            object : DownloadInterface {
                override fun onProcessChange(p: Int) {
                    super.onProcessChange(p)
                    progressBar.progress = p
                    progress.text = p.toString()
                }

                @SuppressLint("SetTextI18n")
                override fun onStartTask() {
                    super.onStartTask()
                    status.text = "Downloading"
                }

                @SuppressLint("SetTextI18n")
                override fun onTaskFail(e: Exception) {
                    super.onTaskFail(e)
                    ShortToast(this@DownloadActivity, e)
                    status.text = "Failed"
                }

                @SuppressLint("SetTextI18n")
                override fun onTaskFinished(file: String, size: Long) {
                    super.onTaskFinished(file, size)
                    ShortToast(this@DownloadActivity, "$$size: $file")
                    status.text = "Finished"
                }
            },
            intent!!.getStringExtra("url"),
            intent!!.getStringExtra("filePath"),
            intent!!.getStringExtra("fileName")
        )

        // 绑定Service
        // Bind service
        val intent = Intent(this, DownloadService::class.java).putExtra("data", data)
        startService(intent)
        bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                downloadService = (service as DownloadService.ServiceBinder).getService()
                binded = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                binded = false
            }
        }, Context.BIND_AUTO_CREATE)
    }

    /* 监听 listener */
    interface DownloadInterface {
        // 开始下载 Start Download
        fun onStartTask() {

        }

        // 进度调整 Process change
        fun onProcessChange(p: Int) {

        }

        // 完成下载 Finish
        fun onTaskFinished(file: String, size: Long) {

        }

        // 下载错误 Exception occurs
        fun onTaskFail(e: Exception) {

        }
    }

    class Data(
        private val listener: DownloadInterface,
        private val url: String,
        private val filePath: String,
        private val fileName: String
    ) : Serializable {

        fun getListener(): DownloadInterface {
            return this.listener
        }

        fun getUrl(): String {
            return this.url
        }

        fun getFilePath(): String {
            return this.filePath
        }

        fun getFileName(): String {
            return this.fileName
        }
    }
}