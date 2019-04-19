package aosp.toolkit.perseus.base

import aosp.toolkit.perseus.DownloadActivity
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception

/*
 * @File:   DownloadUtil
 * @Author: 1552980358
 * @Time:   6:04 PM
 * @Date:   15 Apr 2019
 * 
 */

@Suppress("SENSELESS_COMPARISON")
class DownloadUtil(
    private val url: String,
    private val filePath: String,
    private val fileName: String,
    private val serviceListener: DownloadActivity.DownloadInterface
) {
    private var thread: Thread

    private lateinit var inputStream: InputStream

    init {
        thread = Thread {
            //activityListener.onStartTask()
            serviceListener.onStartTask()
            var proc = 0
            val request = Request.Builder().url(url).build()
            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    serviceListener.onTaskFail(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val file = File(filePath, fileName)
                    if (file.exists()) {
                        file.delete()
                    }

                    try {
                        inputStream = response.body()!!.byteStream()
                        val byteArray = ByteArray(102400)
                        val size = response.body()!!.contentLength()
                        val fileOutputStream = FileOutputStream(file)

                        var sum = 0
                        var len = inputStream.read(byteArray)
                        while (len != -1) {
                            fileOutputStream.write(byteArray, 0, len)
                            sum += len
                            len = inputStream.read(byteArray)
                            val p = (sum * 1.0f / size * 100).toInt()
                            if (proc != p) {
                                serviceListener.onProcessChange(p)
                                proc = p
                            }
                        }
                        fileOutputStream.flush()
                        fileOutputStream.close()
                        inputStream.close()
                        //activityListener.onTaskFinished()
                        serviceListener.onTaskFinished(filePath + fileName, size)
                    } catch (e: Exception) {
                        serviceListener.onTaskFail(e)
                    }

                }
            })
        }
    }
    fun start() {
        if (thread != null) {
            thread.start()
        } else {
            serviceListener.onTaskFail(Exception("NullDownloadThread"))
        }
    }

}
