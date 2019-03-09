package com.earth.OsToolkit.fragments

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.earth.OsToolkit.R
import com.earth.OsToolkit.base.BaseIndex.type_pixelcat
import com.earth.OsToolkit.base.BaseOperation.Companion.ShortToast
import com.earth.OsToolkit.view.CPUFreqOptView

import kotlinx.android.synthetic.main.fragment_applypixelcat.*
import java.net.URL

/*
 * OsToolkit - Kotlin
 *
 * Date : 9 Mar 2019
 *
 * By   : 1552980358
 *
 */

class ApplyPixelCatFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_applypixelcat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val dialog = Dialog(activity!!)
        dialog.setContentView(LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null))
        dialog.setCancelable(false)
        dialog.show()
        Thread {
            try {
                val url = URL("https://raw.githubusercontent.com/osmiumtoolkit/scripts/master/PixelCat_processor_table")
                val inputStream = url.openStream()

                val lines = inputStream.bufferedReader(Charsets.UTF_8).readLines()
                inputStream.close()

                val date = lines[0]

                for (i: Int in 1 until lines.size) {
                    val cpuFreqOptView = CPUFreqOptView(activity!!, type_pixelcat, date, lines[i])
                    when {
                        lines[i].startsWith("sd") -> activity!!.runOnUiThread { snap.addView(cpuFreqOptView) }
                        else -> activity!!.runOnUiThread { exynos.addView(cpuFreqOptView) }
                    }
                }

            } catch (e: Exception) {
                ShortToast(activity!!, e.toString(), false)
            }

            try {
                Thread.sleep(1000)
            } catch (e: Exception) {
                //
            } finally {
                activity!!.runOnUiThread { dialog.cancel() }
            }
        }.start()
    }
}