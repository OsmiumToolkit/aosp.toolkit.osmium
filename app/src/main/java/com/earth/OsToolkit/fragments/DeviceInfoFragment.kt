package com.earth.OsToolkit.fragments

import android.app.Dialog
import android.content.Context
import android.os.*
import android.support.v4.app.Fragment
import android.view.*

import com.earth.OsToolkit.R
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getAvailableCore
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getABI32
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getABI64
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getABIs
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getAndroidVersion
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getAndroidVersionName
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.unitConvert
import com.earth.OsToolkit.base.BaseJavaOperation
import com.earth.OsToolkit.view.DeviceInfoView.ChildView

import kotlinx.android.synthetic.main.fragment_deviceinfo.*

import java.util.*

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

//@SuppressWarnings("all")
class DeviceInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deviceinfo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dialog = Dialog(activity as Context)
        dialog.setCancelable(false)
        dialog.setContentView(LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null))
        dialog.show()

        val t1 = Thread {
            val m = ChildView(
                activity!!,
                R.string.deviceinfo_general_manufacturer,
                Build.MANUFACTURER[0].toUpperCase() + Build.MANUFACTURER.substring(1)
            )
            val b = ChildView(
                activity!!,
                R.string.deviceinfo_general_brand,
                Build.BRAND[0].toUpperCase() + Build.BOARD.substring(1)
            )
            val mo = ChildView(activity!!, R.string.deviceinfo_general_model, Build.MODEL)
            val d = ChildView(activity!!, R.string.deviceinfo_general_device, Build.DEVICE)
            val p = ChildView(activity!!, R.string.deviceinfo_general_product, Build.PRODUCT)
            val h = ChildView(activity!!, R.string.deviceinfo_general_hardware, Build.HARDWARE)

            activity!!.runOnUiThread { generalRoot.addViews(m, b, mo, d, p, h) }
        }
        t1.start()

        val t2 = Thread {
            val v = ChildView(activity!!, R.string.deviceinfo_android_version, getAndroidVersion())
            val vN = ChildView(activity, R.string.deviceinfo_android_versionName, getAndroidVersionName())
            val s = ChildView(activity, R.string.deviceinfo_android_sdk, Build.VERSION.SDK_INT.toString())
            val t = ChildView(activity, R.string.deviceinfo_android_type, Build.TYPE)

            activity!!.runOnUiThread { androidRoot.addViews(v, vN, s, t) }
        }
        t2.start()

        val t3 = Thread {
            val b = ChildView(activity, R.string.deviceinfo_soc_board, Build.BOARD)
            val c = ChildView(activity, R.string.deviceinfo_soc_cores, getAvailableCore().toString())
            val aa = ChildView(activity, R.string.deviceinfo_soc_abis, getABIs())
            val a64 = ChildView(activity, R.string.deviceinfo_soc_abis64, getABI64())
            val a32 = ChildView(activity, R.string.deviceinfo_soc_abis32, getABI32())

            activity!!.runOnUiThread { socRoot.addViews(b, c, aa, a64, a32) }
        }

        t3.start()

        val t4 = Thread {
            val memoryInfo = BaseJavaOperation.getMemory(activity)
            val to = ChildView(activity, R.string.deviceinfo_ram_totalMem, unitConvert(memoryInfo.totalMem))
            val th = ChildView(activity, R.string.deviceinfo_ram_threshold, unitConvert(memoryInfo.threshold))

            activity!!.runOnUiThread { ramRoot.addViews(to, th) }
        }
        t4.start()

        val t5 = Thread {
            val runtime = Runtime.getRuntime()

            val m = ChildView(activity, R.string.deviceinfo_jvm_maxmem, unitConvert(runtime.maxMemory()))
            val t = ChildView(activity, R.string.deviceinfo_jvm_totalmem, unitConvert(runtime.totalMemory()))

            activity!!.runOnUiThread { jvmRoot.addViews(m, t) }
        }
        t5.start()

        val t6 = Thread {

            val displayMatrix = activity!!.resources.displayMetrics
            val x = displayMatrix.widthPixels
            val y = displayMatrix.heightPixels

            val r = ChildView(activity, R.string.deviceinfo_display_resolution, y.toString() + "x" + x.toString())

            val density = displayMatrix.density
            val densityDpi = displayMatrix.densityDpi

            val d = ChildView(activity, R.string.deviceinfo_display_density, density.toString())
            val dd = ChildView(activity, R.string.deviceinfo_display_densityDpi, densityDpi.toString())

            val scaledDensity = displayMatrix.scaledDensity
            val s =
                ChildView(activity, R.string.deviceinfo_display_scaledDensity, scaledDensity.toString())

            activity!!.runOnUiThread { displayRoot.addViews(r, d, dd, s) }
        }
        t6.start()

        Thread {
            while (t1.isAlive || t2.isAlive || t3.isAlive || t4.isAlive || t5.isAlive || t6.isAlive) {
                Thread.sleep(1)
            }
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    activity!!.runOnUiThread { dialog.cancel() }
                }
            }, 1000)
        }.start()
    }

}