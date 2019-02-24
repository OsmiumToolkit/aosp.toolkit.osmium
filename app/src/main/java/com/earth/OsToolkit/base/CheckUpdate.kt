package com.earth.OsToolkit.base

import android.util.Log
import java.io.*
import java.lang.*
import java.net.URL

/*
 * OsToolkit - Kotlin
 *
 * Date : 6/1/2019
 *
 * By   : 1552980358
 *
 */

class CheckUpdate {
    class CheckVersion : Thread() {
        private var version: String? = null

        override fun run() {
            super.run()
            try {
                val url = URL("https://raw.githubusercontent.com/osmiumtoolkit/update/master/Version")
                val inputStream: InputStream = url.openStream()
                val inputStreamReader = InputStreamReader(inputStream, "utf-8")
                val bufferedReader = BufferedReader(inputStreamReader)

                version = bufferedReader.readLine()

                inputStream.close()
                inputStreamReader.close()
                bufferedReader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getVersion(): String? {
            start()
            while (isAlive) {
                sleep(1)
            }
            return version
        }
    }

    class GetChangelog {
        var changelogZh: String? = null
        var changelogEn: String? = null

        fun onFetching() {
            val zh = GetChangelogZh()
            val en = GetChangelogEn()

            zh.start()
            en.start()

            while (zh.isAlive or en.isAlive) {
                Thread.sleep(1)
            }
            this.changelogZh = zh.returnData()
            this.changelogEn = en.returnData()
        }
    }

    private class GetChangelogZh : Thread() {
        var data: StringBuilder = StringBuilder()
        override fun run() {
            super.run()
            // 设置来源
            val url =
                URL("https://raw.githubusercontent.com/osmiumtoolkit/update/master/ChangelogZh")
            try {
                // 连接&获取
                val inputStream: InputStream = url.openStream()
                // 读取
                val inputStreamReader = InputStreamReader(inputStream, "utf-8")
                val bufferedReader = BufferedReader(inputStreamReader)

                // 复制到变量
                var line: String? = bufferedReader.readLine()
                while (line != null) {
                    data.append(line)
                    line = bufferedReader.readLine()
                    if (line != null) {
                        data.append("\n")
                    }
                }

                // 释放资源
                inputStream.close()
                inputStreamReader.close()
                bufferedReader.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun returnData(): String {
            return data.toString()
        }
    }

    private class GetChangelogEn : Thread() {
        var data: StringBuilder = StringBuilder()
        override fun run() {
            super.run()
            // 设置来源
            val url =
                URL("https://raw.githubusercontent.com/osmiumtoolkit/update/master/ChangelogEn")
            try {
                // 连接&获取
                val inputStream: InputStream = url.openStream()
                // 读取
                val inputStreamReader = InputStreamReader(inputStream, "utf-8")
                val bufferedReader = BufferedReader(inputStreamReader)

                // 复制到变量
                var line: String? = bufferedReader.readLine()
                while (line != null) {
                    data.append(line)
                    line = bufferedReader.readLine()
                    if (line != null) {
                        data.append("\n")
                    }
                }

                // 释放资源
                inputStream.close()
                inputStreamReader.close()
                bufferedReader.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun returnData(): String {
            Log.i("changelogEn", data.toString())
            return data.toString()
        }
    }

    class GetDate : Thread() {
        var data: String? = null
        override fun run() {
            super.run()
            val url = URL("https://raw.githubusercontent.com/osmiumtoolkit/update/master/Date")
            try {
                val inputStream = url.openStream()
                val inputStreamReader = InputStreamReader(inputStream, "utf-8")
                val bufferedReader = BufferedReader(inputStreamReader)

                this.data = bufferedReader.readLine()

                inputStream.close()
                inputStreamReader.close()
                bufferedReader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun returnData(): String? {
            start()
            while (isAlive) {
                sleep(1)
            }
            return data
        }
    }

}
