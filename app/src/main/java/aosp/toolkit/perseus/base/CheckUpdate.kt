package aosp.toolkit.perseus.base

import aosp.toolkit.perseus.base.BaseIndex.*
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
                // 设置来源 Set source
                val url = URL(CheckUpdate_Version)

                // 连接&获取 Connect and fetch
                val inputStream: InputStream = url.openStream()

                // 读取&保存 Read and save
                version = inputStream.bufferedReader(Charsets.UTF_8).readLine()
                inputStream.close()
            } catch (e: Exception) {
                //
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
            try {
                // 设置来源 Set source
                val url =
                    URL(CheckUpdate_ChangelogZh)

                // 连接&获取 Connect and fetch
                val inputStream: InputStream = url.openStream()

                // 读取 Read
                val lines = inputStream.bufferedReader(Charsets.UTF_8).readLines()
                inputStream.close()

                // 保存 Save
                for (i in lines) {
                    data.append("$i\n")
                }
            } catch (e: Exception) {
                //
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
            try {
                // 设置来源 Set source
                val url =
                    URL(CheckUpdate_ChangelogEn)
                // 连接&获取 Connect and fetch
                val inputStream: InputStream = url.openStream()

                // 读取 Read
                val lines = inputStream.bufferedReader(Charsets.UTF_8).readLines()
                inputStream.close()

                // 保存 Save
                for (i in lines) {
                    data.append("$i\n")
                }
            } catch (e: Exception) {
                //
            }
        }

        fun returnData(): String {
            return data.toString()
        }
    }

    class GetDate : Thread() {
        private var date: String? = null
        override fun run() {
            super.run()

            try {
                // 设置来源 Set source
                val url = URL(CheckUpdate_Date)

                // 连接&获取 Connect and fetch
                val inputStream = url.openStream()

                // 读取&保存 Read and save
                date = inputStream.bufferedReader(Charsets.UTF_8).readLine()
                inputStream.close()
            } catch (e: Exception) {
                //
            }
        }

        fun returnData(): String? {
            start()
            while (isAlive) {
                sleep(1)
            }
            return date
        }
    }

}
