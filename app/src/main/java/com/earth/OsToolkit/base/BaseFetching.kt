package com.earth.OsToolkit.base

import android.content.Context
import com.earth.OsToolkit.base.BaseIndex.PackageName
import java.io.*
import java.util.regex.Pattern

/*
 * OsToolkit - Kotlin
 *
 * Date : 31/12/2018
 *
 * By   : 1552980358
 *
 */

class BaseFetching {

    companion object {
        fun checkRoot() : Boolean {
            try {
                val process : Process = Runtime.getRuntime().exec("su")
                val dataOutPutStream = DataOutputStream(process.outputStream)
                dataOutPutStream.writeBytes("exit\n")
                dataOutPutStream.flush()
                dataOutPutStream.close()
                val i = process.waitFor()
                if (i == 1)
                    return false
                return true
            } catch (e : Exception) {
                e.printStackTrace()
            }
            return false
        }

        fun getPackageVersion(context: Context?) : String {
            try {
                return context?.packageManager!!.getPackageInfo(PackageName,0 ).versionName
            } catch (e : java.lang.Exception) {
                e.printStackTrace()
            }
            return ""
        }

        fun getAvaliableCore() : Int {
            try {
                val dir = File("/sys/devices/system/cpu/")
                val file : Array<File> = dir.listFiles(FileFilter {
                    Pattern.matches("cpu[0-9]", it.getName())
                })
                return file.size
            } catch (e : java.lang.Exception) {
                e.printStackTrace()
            }
            return 0
        }
    }
}