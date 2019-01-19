package com.earth.OsToolkit.fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.*
import android.view.View.GONE
import com.earth.OsToolkit.R
import com.earth.OsToolkit.base.BaseFetching
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getABI32
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getABI64
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getABIs
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getAndroidVersion
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getAndroidVersionName
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.unitConvert
import com.earth.OsToolkit.base.BaseJavaOperation
import com.earth.OsToolkit.view.DeviceInfoView.ChildView
import kotlinx.android.synthetic.main.fragment_deviceinfo.*

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
 *
 */
 
class DeviceInfoFragment : Fragment() {
    private val handler = Handler()

    private var manufacturer : String? = null
    private var brand : String? = null
    private var model : String? = null
    private var device : String? = null
    private var product : String? = null

    private var version : String? = null
    private var versionName : String? = null
    private var sdk : String? = null
    private var type : String? = null

    private var hardware : String? = null
    private var board : String? = null
    private var cores : String? = null
    private var abis : String? = null
    private var abi64 : String? = null
    private var abi32 : String? = null

    private var totalMem : String? = null
    private var thresholdMem : String? = null

    private var maxMemory : String? = null
    private var totalMemory : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deviceinfo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Thread {
            manufacturer = Build.MANUFACTURER[0].toUpperCase() + Build.MANUFACTURER.substring(1)
            brand = Build.BRAND[0].toUpperCase() + Build.BOARD.substring(1)
            model = Build.MODEL
            device = Build.DEVICE
            product = Build.PRODUCT
            hardware = Build.HARDWARE
            handler.post(general)
        }.start()
        Thread{
            version = getAndroidVersion()
            versionName = getAndroidVersionName()
            sdk = Build.VERSION.SDK_INT.toString()
            type = Build.TYPE
            handler.post(android)
        }.start()
        Thread {
            board = Build.BOARD
            cores = BaseFetching.getAvaliableCore().toString()
            abis = getABIs()
            abi64 = getABI64()
            abi32 = getABI32()
            handler.post(soc)
        }.start()
        Thread {
            val memoryInfo = BaseJavaOperation.getMemory(activity)
            totalMem = unitConvert(memoryInfo.totalMem)
            thresholdMem = unitConvert(memoryInfo.threshold)

            handler.post(mem)
        }.start()
        Thread {
            val runtime = Runtime.getRuntime()
            maxMemory = unitConvert(runtime.maxMemory())
            totalMemory = unitConvert(runtime.totalMemory())

            handler.post(jvm)
        }.start()
    }

    override fun onStart() {
        super.onStart()
        progressBar.visibility = GONE
    }

    private val general = Runnable {
        val m = ChildView(activity, R.string.deviceinfo_general_manufacturer, manufacturer)
        val b = ChildView(activity, R.string.deviceinfo_general_brand, brand)
        val mo = ChildView(activity, R.string.deviceinfo_general_model, model)
        val d = ChildView(activity, R.string.deviceinfo_general_device, device)
        val p = ChildView(activity, R.string.deviceinfo_general_product, product)
        val h = ChildView(activity, R.string.deviceinfo_general_hardware, hardware)

        generalRoot.addViews(m, b, mo, d, p, h)
    }

    private val android = Runnable {
        val v = ChildView(activity, R.string.deviceinfo_android_version, version)
        val vN = ChildView(activity, R.string.deviceinfo_android_versionName, versionName)
        val s = ChildView(activity, R.string.deviceinfo_android_sdk, sdk)
        val t = ChildView(activity, R.string.deviceinfo_android_type, type)

        androidRoot.addViews(v, vN, s, t)
    }

    private val soc = Runnable {
        val b = ChildView(activity, R.string.deviceinfo_soc_board, board)
        val c = ChildView(activity, R.string.deviceinfo_soc_cores, cores)
        val aa = ChildView(activity, R.string.deviceinfo_soc_abis, abis)
        val a64 = ChildView(activity, R.string.deviceinfo_soc_abis64, abi64)
        val a32 = ChildView(activity, R.string.deviceinfo_soc_abis32, abi32)

        socRoot.addViews(b, c, aa, a64, a32)
    }

    private val mem = Runnable {
        val to = ChildView(activity, R.string.deviceinfo_ram_totalMem, totalMem)
        val th = ChildView(activity, R.string.deviceinfo_ram_threshold, thresholdMem)

        ramRoot.addViews(to, th)
    }

    private val jvm = Runnable {
        val m = ChildView(activity, R.string.deviceinfo_jvm_maxmem, maxMemory)
        val t = ChildView(activity, R.string.deviceinfo_jvm_totalmem, totalMemory)

        jvmRoot.addViews(m, t)
    }

}