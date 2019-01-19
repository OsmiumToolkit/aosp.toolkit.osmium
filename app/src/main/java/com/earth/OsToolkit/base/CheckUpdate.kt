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
                val url = URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/OsToolkit")
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
                URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/OsToolkitChangelogZh")
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
            Log.i("changelogZh", data.toString())
            return data.toString()
        }
    }

    private class GetChangelogEn : Thread() {
        var data: StringBuilder = StringBuilder()
        override fun run() {
            super.run()
            // 设置来源
            val url =
                URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/OsToolkitChangelogEn")
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
            val url = URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/OsToolkitDate")
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


/*

class CheckUpdate : Thread() {

    private var version : String? = null
    private var date : String? = null
    private val changelogZh = StringBuilder()
    private val changelogEn = StringBuilder()

    override fun run() {
        super.run()
        try {
            var url = URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/OsToolkit")
            val inputStream : InputStream = url.openStream()
            val inputStreamReader = InputStreamReader(inputStream, "utf-8")
            val bufferedReader = BufferedReader(inputStreamReader)

            version = bufferedReader.readLine()
            date = bufferedReader.readLine()

            if (bufferedReader.readLine() == "<ENG>") {
                var string: String? = bufferedReader.readLine()
                while (!string.equals(null) and !string.equals("<CN>")) {
                    changelogEn.append("$string\n")
                    string = bufferedReader.readLine()
                }

                while (!string.equals(null)) {
                    changelogZh.append("$string\n")
                    string = bufferedReader.readLine()
                }
            }

            inputStream.close()
            inputStreamReader.close()
            bufferedReader.close()
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    // Thread blocking / 线程堵塞
    fun waitFor() : Boolean  {
        start()
        if (!isInterrupted) {
            while (isAlive) {
                try {
                    sleep(10)
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
            return true
        }
        return false
    }

    // return data / 返回数据
    fun getVersion() : String? {
        Log.i("version", version)
        return this.version
    }
    fun getDate() : String? {
        Log.i("date", date)
        return this.date
    }
    fun getChangelogEn() : String? {
        Log.i("changelogEn", changelogEn.toString())
        return changelogEn.toString()
    }
    fun getChangelogZh() : String? {
        Log.i("changelogZh", changelogZh.toString())
        return changelogZh.toString()
    }
}

        */