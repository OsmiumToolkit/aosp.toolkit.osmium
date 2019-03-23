package aosp.toolkit.osmium.fragments

/*
 * OsToolkit - Kotlin
 *
 * Date : 1/1/2019
 *
 * By   : 1552980358
 *
 */

/*
 * Modify
 *
 * 9/1/2019
 * 23/1/2019
 *
 */

import android.app.Dialog
import android.os.*
import android.support.v4.app.Fragment
import android.view.*

import aosp.toolkit.osmium.R

import java.net.URL

import aosp.toolkit.osmium.base.BaseIndex.type_yc
import aosp.toolkit.osmium.base.BaseOperation.Companion.ShortToast
import aosp.toolkit.osmium.view.CPUFreqOptView

import kotlinx.android.synthetic.main.fragment_applyyc.*

class ApplyYCFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_applyyc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialog = Dialog(activity!!)
        dialog.setContentView(LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null))
        dialog.setCancelable(false)
        dialog.show()

        Thread {
            try {
                val url =
                    URL("https://raw.githubusercontent.com/osmiumtoolkit/scripts/master/yc_processor_table")
                val inputStream = url.openStream()

                val lines = inputStream.bufferedReader(Charsets.UTF_8).readLines()
                inputStream.close()

                val date = lines[0]

                for (i: Int in 1 until lines.size) {
                    val cpuFreqOptView = CPUFreqOptView(activity!!, type_yc, date, lines[i])
                    when {
                        lines[i].startsWith("sd") -> activity!!.runOnUiThread { snap.addView(cpuFreqOptView) }
                        lines[i].startsWith("exynos") -> activity!!.runOnUiThread { exynos.addView(cpuFreqOptView) }
                        lines[i].startsWith("kirin") -> activity!!.runOnUiThread { kirin.addView(cpuFreqOptView) }
                        lines[i].startsWith("helio") -> activity!!.runOnUiThread { mtk.addView(cpuFreqOptView) }
                        else -> activity!!.runOnUiThread { atom.addView(cpuFreqOptView) }
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