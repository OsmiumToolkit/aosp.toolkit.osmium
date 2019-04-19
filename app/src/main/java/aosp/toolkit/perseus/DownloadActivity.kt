package aosp.toolkit.perseus

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

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
    var downloading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        initialize()

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

    private fun initialize() {
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        startDownload()
    }

    private fun startDownload() {
        val n = intent!!.getStringExtra("fileName")
        DownloadUtil(intent!!.getStringExtra("url"),
            intent!!.getStringExtra("filePath"),
            n,
            object : DownloadInterface {
                @SuppressLint("SetTextI18n")
                override fun onProcessChange(p: Int) {
                    super.onProcessChange(p)
                    runOnUiThread {
                        progressBar.progress = p
                        progress.text = "$p%"
                    }
                }

                override fun onStartTask() {
                    super.onStartTask()
                    runOnUiThread {
                        status.text = getString(R.string.downloading)
                        name.text = n
                        size.append(intent.getStringExtra("size"))
                    }
                }

                override fun onTaskFail(e: Exception) {
                    super.onTaskFail(e)
                    ShortToast(
                        this@DownloadActivity,
                        getString(R.string.download_failed) + e.toString(),
                        false
                    )
                    runOnUiThread { status.text = getString(R.string.status_failed) }
                    downloading = false
                }

                @SuppressLint("SetTextI18n")
                override fun onTaskFinished(file: String, size: Long) {
                    super.onTaskFinished(file, size)
                    ShortToast(
                        this@DownloadActivity,
                        getString(R.string.download_success) + "$size: $file",
                        false
                    )
                    runOnUiThread {
                        status.text = getString(R.string.status_finish)
                        progress.text = "100%"
                        progressBar.progress = 100
                    }
                    downloading = false
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

    override fun onBackPressed() {
        if (!downloading) {
            super.onBackPressed()
        }
    }
}