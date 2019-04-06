package aosp.toolkit.perseus.base

import android.util.Log

import okhttp3.*

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception


/*
 * @File:   DownloadFile
 * @Author: 1552980358
 * @Time:   11:32 AM
 * @Date:   6 Apr 2019
 * 
 */


class DownloadFile(
    private val url: String, private val targetFilePath: String, private val targetFileName: String
) {

    fun download(listener: OnDownLoadListener) {
        val request = Request.Builder().url(url).build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                listener.onDownloadFailed(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val byte = ByteArray(40960)
                val filePath = File(targetFilePath)

                if (!filePath.exists()) {
                    filePath.mkdirs()
                }

                val file = File(filePath, targetFileName)
                if (file.exists()) {
                    file.delete()
                }
                try {
                    val inputStream = response.body()!!.byteStream()
                    val size = response.body()!!.contentLength()
                    val fileOutputStream = FileOutputStream(file)

                    var sum = 0
                    var len = inputStream.read(byte)
                    while (len != -1) {
                        fileOutputStream.write(byte, 0, len)
                        sum += len
                        len = inputStream.read(byte)
                        val p = (sum * 1.0f / size * 100).toInt()
                        listener.onDownloading(p)
                        Log.e("progressLoad", p.toString())
                    }
                    fileOutputStream.flush()
                    listener.onDownloadSuccess(file)
                    fileOutputStream.close()
                    inputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    interface OnDownLoadListener {
        fun onDownloadSuccess(file: File) {

        }

        fun onDownloading(progress: Int) {

        }

        fun onDownloadFailed(e: Exception) {

        }
    }
}