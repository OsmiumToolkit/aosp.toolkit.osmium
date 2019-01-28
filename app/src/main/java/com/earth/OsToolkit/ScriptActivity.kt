package com.earth.OsToolkit

import android.app.Activity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.earth.OsToolkit.base.BaseKotlinOperation
import kotlinx.android.synthetic.main.activity_script.*
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URL
import java.nio.charset.StandardCharsets

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

        Log.i("script", "$type + $index + $name")

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
                    "https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/" +
                            type + File.separator +
                            index + File.separator +
                            name
                )

                val inputStream = url.openStream()

                if (!file!!.exists() || file!!.length() < -1) {

                    val fileOutputStream = FileOutputStream(file)

                    val buffer = ByteArray(10240)

                    var len: Int = inputStream.read(buffer)
                    while (len != -1) {
                        fileOutputStream.write(buffer, 0, len)
                        len = inputStream.read(buffer)
                    }

                    fileOutputStream.flush()
                    inputStream.close()
                    fileOutputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
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

            if (BaseKotlinOperation.setPermission(file!!.toString())) {
                runOnUiThread {
                    script_permission.setText(R.string.script_download_fail)
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
        val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "/system/bin/sh", file.toString()))
        val inputStream = process.inputStream
        val inputStreamError = process.errorStream

        val inputStreamReader = InputStreamReader(inputStream, StandardCharsets.UTF_8)
        val inputStreamReaderError = InputStreamReader(inputStreamError, StandardCharsets.UTF_8)

        val bufferedReader = BufferedReader(inputStreamReader)
        val bufferedReaderError = BufferedReader(inputStreamReaderError)

        Thread {
            val output = StringBuilder()
            var string = bufferedReader.readLine()
            while (string != null) {
                output.append(string)
                string = bufferedReader.readLine()
                if (string != null) {
                    output.append("\n")
                }
            }
            runOnUiThread {
                script_process_title.visibility = View.VISIBLE
                script_process.text = output.toString()
            }
        }.start()

        Thread {
            val output = StringBuilder()
            var string = bufferedReaderError.readLine()
            while (string != null) {
                output.append(string)
                string = bufferedReader.readLine()
                if (string != null) {
                    output.append("\n")
                }
            }
            runOnUiThread {
                script_error_title.visibility = View.VISIBLE
                script_error.text = output.toString()
            }
        }.start()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }
}