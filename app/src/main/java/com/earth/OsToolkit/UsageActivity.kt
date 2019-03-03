package com.earth.OsToolkit

import android.app.Activity
import android.content.*
import android.os.*
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.*
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getAvailableCore
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.readFile

import kotlinx.android.synthetic.main.activity_usage.*
import kotlinx.android.synthetic.main.view_corefreq.view.*
import kotlinx.android.synthetic.main.view_sensordata.view.*

import java.io.File
import java.lang.StringBuilder

/*
 * OsToolkit - Kotlin
 *
 * Date : 25 Jan 2019
 *
 * By   : 1552980358
 *
 */

@Suppress("all")
class UsageActivity : AppCompatActivity() {
    private var batteryReceiver: BatteryReceiver? = null
    private val coreFreqViewList = mutableListOf<CoreFreqView>()
    private val sensorDataChildViewList = mutableListOf<SensorDataChildView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage)
        initialize()

        val t1 = Thread {
            for (i: Int in 0 until getAvailableCore()) {
                val coreFreqView = CoreFreqView(this, i)
                coreFreqViewList.add(coreFreqView)
                // 设置控件位置 Position of view in grid
                val layoutParams = GridLayout.LayoutParams()
                layoutParams.rowSpec = GridLayout.spec(i / 2, 1f)
                layoutParams.columnSpec = GridLayout.spec(i % 2, 1f)
                coreFreqView.layoutParams = layoutParams
                runOnUiThread {
                    grid.addView(coreFreqView)
                }
            }
        }
        t1.start()

        val t2 = Thread {
            val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            batteryReceiver = BatteryReceiver(this, progressBar, battery_level, battery_voltage)
            registerReceiver(batteryReceiver, intentFilter)

            val currentThread = Thread {
                val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
                var c: Int
                var lastC = 0
                while (true) {
                    c =  0 - batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) / 1000
                    if (lastC != c) {
                        runOnUiThread {
                            battery_current.text = "$c mA"
                        }
                        lastC = c
                    }
                    try {
                        Thread.sleep(1000)
                    } catch (e: Exception) {
                        //
                    }
                }

            }
            currentThread.start()
        }

        t2.start()
        val t3 = Thread {
            val dir = File("/sys/class/thermal")
            val file = dir.listFiles().size
            for (i: Int in 0 until file) {
                val sensorDataChildView = SensorDataChildView(this, i)
                runOnUiThread { rootThermal.addView(sensorDataChildView) }
                sensorDataChildViewList.add(sensorDataChildView)
            }
        }
        t3.start()
        Thread {
            while (t1.isAlive || t2.isAlive || t3.isAlive) {
                try {
                    Thread.sleep(1)
                } catch (e: java.lang.Exception) {
                    //
                }
            }
            runOnUiThread { scrollView.visibility = View.VISIBLE }
        }.start()
    }

    private fun initialize() {
        setSupportActionBar(toolbar)

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (getSharedPreferences("ui", Context.MODE_PRIVATE).getBoolean("navBar", true)) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        unregisterReceiver(batteryReceiver)
        for (i in coreFreqViewList) {
            i.interruptThread()
        }
        for (i in sensorDataChildViewList) {
            i.interruptThread()
        }
        super.onBackPressed()
    }

    class BatteryReceiver(activity: UsageActivity, progressBar: ProgressBar, level: TextView, voltage: TextView) : BroadcastReceiver() {
        private var activity: UsageActivity? = null
        private var progressBar: ProgressBar? = null
        private var level: TextView? = null
        private var voltage: TextView? = null

        init {
            this.activity = activity
            this.progressBar = progressBar
            this.level = level
            this.voltage = voltage
        }

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) {
                return
            }

            val l = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)

            val v = StringBuilder(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1).toString())
            v.insert(1, ".").append(" V")

            activity!!.runOnUiThread {
                progressBar!!.progress = l
                level!!.text = l.toString()
                voltage!!.text = v
            }
        }
    }

    class CoreFreqView(activity: Activity, core: Int) : LinearLayout(activity) {
        var thread: Thread? = null

        init {
            LayoutInflater.from(activity).inflate(R.layout.view_corefreq, this)

            thread = Thread {
                var f: String
                var lastFreq = ""
                while (true) {
                    f = readFile("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_cur_freq")
                    // 减低UI线程使用,对比上一秒频率
                    // For reduce usage of UI Thread, compare freq of last second
                    if (lastFreq != f) {
                        activity.runOnUiThread { freq.text = "$f KHz" }
                        // 改变后更新 Update after change
                        lastFreq = f
                    }
                    try {
                        Thread.sleep(1000)
                    } catch (e: Exception) {
                        // 防止线程停止后继续执行Sleep而导致应用崩溃
                        // Prevent exception occur after interrupt
                    }
                }
            }
            thread!!.start()
        }

        fun interruptThread() {
            this.thread!!.interrupt()
        }
    }

    class SensorDataChildView(activity: Activity, no: Int) : LinearLayout(activity) {
        var thread: Thread? = null

        init {
            LayoutInflater.from(activity).inflate(R.layout.view_sensordata, this)
            val t = readFile("/sys/class/thermal/thermal_zone$no/type")
            title.text = t

            thread = Thread {
                var d: StringBuilder
                var lastData = ""
                while (true) {
                    d = StringBuilder(readFile("/sys/class/thermal/thermal_zone$no/temp"))
                    d.insert(2, ".")
                    if (lastData != d.toString()) {
                        activity.runOnUiThread { content.text = d }
                        lastData = d.toString()
                    }

                    try {
                        Thread.sleep(1000)
                    } catch (e: Exception) {
                        //
                    }
                }
            }
            thread!!.start()
        }

        fun interruptThread() {
            this.thread!!.interrupt()
        }
    }

}