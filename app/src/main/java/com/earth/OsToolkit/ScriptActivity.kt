package com.earth.OsToolkit

import android.app.Activity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.setPermission
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.ShortToast

import com.topjohnwu.superuser.Shell
import kotlinx.android.synthetic.main.activity_script.*

import java.io.*
import java.lang.Exception
import java.net.URL

/*
 * OsToolkit - Kotlin
 *
 * Date : 24 Jan 2019
 *
 * By   : 1552980358
 *
 */

class ScriptActivity : AppCompatActivity() {
    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_script)

        val type = intent.getStringExtra("type")
        val index = intent.getStringExtra("index")
        val name = if (intent.getStringExtra("name").endsWith(".sh")) {
            intent.getStringExtra("name")
        } else {
            intent.getStringExtra("name").plus(".sh")
        }

        //Log.i("script", "$type + $index + $name")

        file = File(cacheDir.absolutePath + File.separator + name)

        initialize()
        download(type, index, name)
    }

    private fun initialize() {
        setSupportActionBar(toolbar)

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        window.statusBarColor = ContextCompat.getColor(this, android.R.color.black)

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun download(type: String, index: String, name: String) {
        Thread {
            try {
                val url = URL(
                    "https://raw.githubusercontent.com/osmiumtoolkit/scripts/master" +
                            type + File.separator +
                            index + File.separator +
                            name
                )

                val inputStream = url.openStream()

                if (!file!!.exists() || file!!.length() < -1) {

                    val fileOutputStream = FileOutputStream(file)

                    val buffer = ByteArray(10240)

                    // 输出到文件 Output to file
                    var len: Int = inputStream.read(buffer)
                    while (len != -1) {
                        fileOutputStream.write(buffer, 0, len)
                        len = inputStream.read(buffer)
                    }

                    // 释放资源 release resources
                    fileOutputStream.flush()
                    inputStream.close()
                    fileOutputStream.close()
                }
            } catch (e: Exception) {
                ShortToast(this, e.toString())
            }

            setPermission()
        }.start()
    }

    private fun setPermission() {
        if (file!!.length() > 1) {
            runOnUiThread {
                script_download.append(file!!.length().toString() + "Bytes\n")
                script_download.append(getString(R.string.script_download_done))
                script_target_title.visibility = View.VISIBLE
                script_target.text = file.toString()
                script_permission_title.visibility = View.VISIBLE
            }

            if (setPermission(file!!.toString())) {
                runOnUiThread {
                    script_permission.setText(R.string.script_permission_done)
                    script_command_title.visibility = View.VISIBLE
                    val string = "su -c /system/bin/sh $file"
                    script_command.text = string
                }
                runScript()
            } else {
                runOnUiThread {
                    script_permission.setText(R.string.script_permission_fail)
                }
            }
        } else {
            runOnUiThread { script_download.setText(R.string.script_download_fail) }
        }
    }

    private fun runScript() {
        val command = Shell.su("/system/bin/sh ${file.toString()}").exec()
        val outputList = command.out

        // 内容输出 output process
        if (outputList.size > 0) {
            runOnUiThread { script_process_title.visibility = View.VISIBLE }
            Thread {
                for (i in outputList) {
                    runOnUiThread { script_process.append(i + "\n") }
                }
            }.start()
        }

        // 错误输出 output error
        val errorList = command.err
        if (errorList.size > 0) {
            runOnUiThread { script_error_title.visibility = View.VISIBLE }
            Thread {
                for (i in outputList) {
                    runOnUiThread { script_error.append(i + "\n") }
                }
            }.start()
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }
}