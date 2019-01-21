package com.earth.OsToolkit.base

import android.os.Build
import android.util.Log
import java.io.*

/*
 * OsToolkit - Kotlin
 *
 * Date : 31/12/2018
 *
 * By   : 1552980358
 *
 */

class BaseKotlinOperation {
    companion object {
        fun setPermission(filePath: String): Boolean {
            val file = File(filePath)

            file.setReadable(true)
            file.setWritable(true)
            file.setExecutable(true)

            return file.canExecute()
        }

        fun readFile(filePath: String): String {
            Log.i("readFile_in_$filePath", filePath)
            try {
                val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "cat", filePath))
                process.waitFor()
                val inputStream : InputStream = process.inputStream
                val inputStreamReader = InputStreamReader(inputStream, "utf-8")
                val bufferedReader = BufferedReader(inputStreamReader)

                val string = bufferedReader.readLine()
                Log.i("readFile_$filePath", string)

                inputStream.close()
                inputStreamReader.close()
                bufferedReader.close()

                return string
            } catch (e: Exception) {
                e.printStackTrace()
                return "Fail"
            }
        }

        fun checkFilePresent(filePath: String): Boolean {
            val file = File(filePath)
            return file.exists()
        }

        fun getAndroidVersion(): String {
            when (Build.VERSION.SDK_INT) {
                21 -> return "5.0"
                22 -> return "5.1"
                23 -> return "6.0"
                24 -> return "7.0"
                25 -> return "7.1"
                26 -> return "8.0"
                27 -> return "8.1"
                28 -> return "9.0"
            }
            return "Fail"
        }

        fun getAndroidVersionName(): String {
            when (Build.VERSION.SDK_INT) {
                21, 22 -> return "Lollipop"
                23 -> return "Marshmallow"
                24, 25 -> return "Nougat"
                26, 27 -> return "Oreo"
                28 -> return "Pie"
            }
            return "Fail"
        }

        fun getABIs(): String {
            val stringBuilder = StringBuilder()
            for (i: Int in 0 until Build.SUPPORTED_ABIS.size) {
                stringBuilder.append(Build.SUPPORTED_ABIS[i])
                if (i < Build.SUPPORTED_ABIS.size - 1) {
                    stringBuilder.append("\n")
                }
            }
            return stringBuilder.toString()
        }

        fun getABI64(): String {
            val stringBuilder = StringBuilder()
            for (i: Int in 0 until Build.SUPPORTED_64_BIT_ABIS.size) {
                stringBuilder.append(Build.SUPPORTED_64_BIT_ABIS[i])
                if (i < Build.SUPPORTED_64_BIT_ABIS.size - 1) {
                    stringBuilder.append("\n")
                }
            }
            return stringBuilder.toString()
        }

        fun getABI32(): String {
            val stringBuilder = StringBuilder()
            for (i: Int in 0 until Build.SUPPORTED_32_BIT_ABIS.size) {
                stringBuilder.append(Build.SUPPORTED_32_BIT_ABIS[i])
                if (i < Build.SUPPORTED_32_BIT_ABIS.size - 1) {
                    stringBuilder.append("\n")
                }
            }
            return stringBuilder.toString()
        }

        fun unitConvert(long: Long): String {
            val tmp: String?
            if (long > 1024 * 1024) {
                tmp = (long / 1024 / 1024).toString() + " MB"
            } else if (long > 1024) {
                tmp = (long / 1024).toString() + "KB"
            } else {
                tmp = long.toString() + "B"
            }
            return tmp
        }

    }
}