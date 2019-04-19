package aosp.toolkit.perseus

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import aosp.toolkit.perseus.base.BaseManager
import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast
import aosp.toolkit.perseus.base.DownloadUtil
import kotlinx.android.synthetic.main.activity_download.*
import java.lang.Exception


/*
 * @File:   DownloadActivity
 * @Author: 1552980358
 * @Time:   5:12 PM
 * @Date:   15 Apr 2019
 * 
 */

class DownloadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)


        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startDownload()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 0
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        startDownload()
    }

    private fun startDownload() {
        DownloadUtil(intent!!.getStringExtra("url"),
            intent!!.getStringExtra("filePath"),
            intent!!.getStringExtra("fileName"),
            object : DownloadInterface {
                override fun onProcessChange(p: Int) {
                    super.onProcessChange(p)
                    runOnUiThread {
                        progressBar.progress = p
                        progress.text = p.toString()
                    }
                }

                @SuppressLint("SetTextI18n")
                override fun onStartTask() {
                    super.onStartTask()
                    runOnUiThread { status.text = "Downloading" }
                }

                @SuppressLint("SetTextI18n")
                override fun onTaskFail(e: Exception) {
                    super.onTaskFail(e)
                    ShortToast(this@DownloadActivity, e, false)
                    runOnUiThread { status.text = "Failed" }
                }

                @SuppressLint("SetTextI18n")
                override fun onTaskFinished(file: String, size: Long) {
                    super.onTaskFinished(file, size)
                    ShortToast(this@DownloadActivity, "$$size: $file", false)
                    runOnUiThread { status.text = "Finished" }
                }
            }).start()


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
    ) {

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