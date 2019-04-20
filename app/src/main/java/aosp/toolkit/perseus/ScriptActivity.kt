package aosp.toolkit.perseus

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View

import aosp.toolkit.perseus.base.BaseIndex.Script_Head
import aosp.toolkit.perseus.base.BaseOperation
import aosp.toolkit.perseus.base.BaseOperation.Companion.checkFilePresent

import kotlinx.android.synthetic.main.activity_script.*

import java.io.File
import java.io.FileWriter
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
    private lateinit var script: String
    private lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_script)
        script = if (intent.getStringExtra("script").endsWith(".sh")) {
            intent.getStringExtra("script")
        } else {
            intent.getStringExtra("script").plus(".sh")
        }
        toolbar.title = script
        initialize()
        checkPermission()
    }

    private fun initialize() {
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        window.statusBarColor = ContextCompat.getColor(this, android.R.color.black)

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun checkPermission() {
        Thread {
            /* 检查限权 Check permission */
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                checkFile()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 0
                )
            }
        }.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkPermission()
    }

    private fun checkFile() {
        path = cacheDir.absolutePath + File.separator + script
        if (checkFilePresent(externalCacheDir!!.absolutePath + File.separator + script)) {
            copying()
        } else {
            downloading()
        }
    }

    private fun copying() {
        try {
            val text = File(externalCacheDir!!.absolutePath + File.separator + script).inputStream()
                .bufferedReader().readText()
            val fileWriter = FileWriter(File(path))
            fileWriter.write(text)
            fileWriter.flush()
            fileWriter.close()
            if (File(path).length() > 0) {
                runOnUiThread {
                    onPrep.append(getString(R.string.script_copy_done))
                    onPrep.append(
                        String.format(
                            getString(R.string.script_size), File(path).length()
                        )
                    )
                }
                setPermission()
            } else {
                runOnUiThread {
                    onPrep.append(getString(R.string.script_copy_fail))
                }
            }
        } catch (e: Exception) {
            runOnUiThread {
                onPrep.append(getString(R.string.script_copy_fail))
                onExce.visibility = View.VISIBLE
                onExce.append(e.toString())
            }
        }
    }

    private fun downloading() {
        try {
            val text = URL(Script_Head + intent.getStringExtra("path") + script).openStream()
                .bufferedReader().readText()
            val fileWriter = FileWriter(File(path))
            fileWriter.write(text)
            fileWriter.flush()
            fileWriter.close()
            if (File(path).length() > 0) {
                runOnUiThread {
                    onPrep.append(getString(R.string.script_download_done))
                    onPrep.append(
                        String.format(
                            getString(R.string.script_size), File(path).length()
                        )
                    )
                }
                setPermission()
            } else {
                runOnUiThread {
                    onPrep.append(getString(R.string.script_download_fail))
                }
            }
        } catch (e: Exception) {
            runOnUiThread {
                onPrep.append(getString(R.string.script_download_fail))
                onExce.visibility = View.VISIBLE
                onExce.append(e.toString())
            }
        }
    }


    private fun setPermission() {
        runOnUiThread {
            onPrep.visibility = View.VISIBLE
            onPrep.append(getString(R.string.script_permission))
        }
        if (BaseOperation.setPermission(path)) {
            runOnUiThread {
                onPrep.append(getString(R.string.script_permission_done))
            }
            runScript()
        } else {
            // 设置失败
            runOnUiThread { onPrep.append(getString(R.string.script_permission_fail)) }
        }
    }

    private fun runScript() {
        try {
            val p = Runtime.getRuntime().exec("su -c /system/bin/sh $path")
            val i = p.inputStream.bufferedReader().readText()
            if (!i.isEmpty()) {
                runOnUiThread {
                    onProcess.visibility = View.VISIBLE
                    onProcess.append(i)
                }
            }
            val j = p.errorStream.bufferedReader().readText()
            if (!j.isEmpty()) {
                runOnUiThread {
                    onError.visibility = View.VISIBLE
                    onError.append(j)
                }
            }
        } catch (e: Exception) {
            // 发生错误
            runOnUiThread {
                onExce.visibility = View.VISIBLE
                onExce.append(e.toString())
            }
        }
        removeFile()
    }

    private fun removeFile() {
        runOnUiThread {
            onRemove.visibility = View.VISIBLE
        }
        try {
            val res = if (File(path).delete()) {
                getString(R.string.script_remove_success)
            } else {
                getString(R.string.script_remove_failed)
            }
            runOnUiThread {
                onRemove.text = res
            }
        } catch (e: Exception) {
            // 发生异常
            runOnUiThread {
                onRemove.visibility = View.VISIBLE
                onRemove.append(getString(R.string.script_remove_failed))
                onExce.visibility = View.VISIBLE
                onExce.append(e.toString())
            }
        }

    }
}


/*
class ScriptActivity : AppCompatActivity() {
    private lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_script)

        var script = intent.getStringExtra("script")

        val path = if (script.endsWith(".sh")) {
            intent.getStringExtra("path") + script
        } else {
            script += ".sh"
            intent.getStringExtra("path") + script
        }

        file = File("${cacheDir.absolutePath}${File.separator}$script")

        initialize()
        download(path)
    }

    private fun initialize() {
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        window.statusBarColor = ContextCompat.getColor(this, android.R.color.black)

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun download(path: String) {
        /*
         *
         * 下载脚本文件到cache
         * Download script to cache
         *
         * @param type: String
         * @param index: String
         * @param name: String
         *
         */

        Thread {
            try {
                val url = URL(
                    "$Script_Head$path"
                )

                val inputStream = url.openStream()

                if (file.exists()) {
                    file.delete()
                }

                val fileOutputStream = FileOutputStream(file)

                val buffer = ByteArray(
                    10240
                )

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
            } catch (e: Exception) {
                ShortToast(this, e, false)
            }

            setPermission()
        }.start()
    }

    private fun setPermission() {
        /*
         *
         * 设置限权
         * set script permission
         *
         * 使它成为可执行文件
         * make it executable
         *
         */

        if (file.length() > 1) {
            runOnUiThread {
                script_download.append(file.length().toString() + "Bytes\n")
                script_download.append(getString(R.string.script_download_done))
                script_target_title.visibility = View.VISIBLE
                script_target.text = file.toString()
                script_permission_title.visibility = View.VISIBLE
            }

            if (setPermission(file.toString())) {
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
        /*
         * 运行脚本
         * run script
         *
         */

        val stdOut = mutableListOf<String>()
        val stdError = mutableListOf<String>()
        Shell.su("/system/bin/sh $file").to(stdOut, stdError).exec()

        // 内容输出 output process
        if (stdOut.size > 0) {
            runOnUiThread { script_process_title.visibility = View.VISIBLE }
            Thread {
                for (i in stdOut) {
                    runOnUiThread { script_process.append(i + "\n") }
                }
            }.start()
        }

        // 错误输出 output error
        if (stdError.size > 0) {
            runOnUiThread { script_error_title.visibility = View.VISIBLE }
            Thread {
                for (i in stdError) {
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
        */