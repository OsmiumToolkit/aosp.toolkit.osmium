package aosp.toolkit.perseus

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast
import aosp.toolkit.perseus.base.BaseOperation.Companion.getAvailableCore
import aosp.toolkit.perseus.base.BaseOperation.Companion.javaFileReadLine
import aosp.toolkit.perseus.base.BaseOperation.Companion.suFileReadLine
import com.topjohnwu.superuser.Shell

import kotlinx.android.synthetic.main.activity_usage.*
import kotlinx.android.synthetic.main.view_corefreq.view.*
import kotlinx.android.synthetic.main.view_sensordata.view.*
import kotlin.collections.ArrayList

import java.io.File
import java.io.FileInputStream
import java.lang.StringBuilder
import java.util.Arrays


/*
 * OsToolkit - Kotlin
 *
 * Date : 25 Jan 2019
 *
 * By   : 1552980358
 *
 */

@Suppress("all", "NAME_SHADOWING")
class UsageActivity : AppCompatActivity() {
    private var batteryReceiver: BatteryReceiver? = null
    private val coreFreqViewList = mutableListOf<CoreFreqView>()
    private val sensorDataChildViewList = mutableListOf<SensorDataChildView>()
    private val threadList = mutableListOf<Thread>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage)
        initialize()

        val t1 = Thread {
            val root =
                javaFileReadLine("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq") == "Fail"

            for (i: Int in 0 until getAvailableCore()) {
                val coreFreqView = CoreFreqView(this, i, root)
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

            /*
             *
             * 计算CPU总使用率 Calculate total CPU usage
             * 抓取/proc/stat保存第一行数据，然后再等待一段时间后再次抓取/proc/stat，再用公式计算出使用率，再将此次抓取的数据保存，然后无限重复
             * Catch /proc/stat and save first line in specific order, and catch /proc/stat again after waiting a period of time
             * then calculate usage by following formula, then save new data, finally repeat and repeat...
             *
             * /proc/stat第一行参考(Array.asList的话中间会因为有个空白，所以会导致List失去目录1，所以user的目录是2)
             * First line example of /proc/stat (due to a space, list will lost index 1, so index of user is 2)
             * cpu  user nice system idle iowait irq softirq 0 0 0
             *
             * 总使用率 TotalUsage
             * user+nice+system+iowait+irq+softirq
             *
             * 空闲 WaitingTime
             * idle
             *
             * 公式 Formula
             * ((新WaitingTime - 新TotalUsage) - (旧WaitingTime - 旧TotalUsage)) / (新WaitingTime - 旧WaitingTime) * 100
             * ((New WaitingTime - New TotalUsage) - (Old WaitingTime - Old TotalUsage)) / (New WaitingTime - Old WaitingTime) * 100
             *
             */

            val thread = Thread {
                var lastIdle: Float
                var lastTotal: Float
                var root: Boolean // 是否使用root Whether use ROOT
                var stat: ArrayList<String>

                try {
                    val fileInputStream = FileInputStream(File("/proc/stat"))
                    stat = ArrayList(
                        Arrays.asList(
                            *fileInputStream.bufferedReader(Charsets.UTF_8).readLine().split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        )
                    )
                    fileInputStream.close()
                    root = false
                } catch (e: Exception) {
                    // 文件权限不足时会抛出IOException, 从而判断是否需要ROOT
                    // IOException thrown when "Permission Denied", applied to consider using ROOT
                    ShortToast(this, e, false)
                    stat = ArrayList(
                        Arrays.asList(
                            *Shell.su("cat /proc/stat").exec().out[0].split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        )
                    )
                    root = true
                }

                try {
                    lastIdle = stat[5].toFloat()
                    lastTotal =
                        stat[2].toFloat() + stat[3].toFloat() + stat[4].toFloat() + stat[6].toFloat() + stat[7].toFloat() + stat[8].toFloat() + stat[9].toFloat()
                } catch (e: Exception) {
                    runOnUiThread { ShortToast(this, e, false) }
                    lastIdle = 0f
                    lastTotal = 0f
                }

                try {
                    Thread.sleep(1000)
                } catch (e: Exception) {
                    //
                }

                if (root) {
                    while (true) {
                        try {
                            val stat = ArrayList(
                                Arrays.asList(
                                    *Shell.su("cat /proc/stat").exec().out[0].split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                )
                            )
                            val nowIdle = stat[5].toFloat()
                            val nowTotal =
                                stat[2].toFloat() + stat[3].toFloat() + stat[4].toFloat() + stat[6].toFloat() + stat[7].toFloat() + stat[8].toFloat() + stat[9].toFloat()
                            val u =
                                100f * ((nowIdle - nowTotal) - (lastIdle - lastTotal)) / (nowIdle - lastIdle)
                            runOnUiThread { usage.text = u.toString() }
                            lastIdle = nowIdle
                            lastTotal = nowTotal
                        } catch (e: Exception) {
                            runOnUiThread { ShortToast(this, e, false) }
                            break
                        }
                        try {
                            Thread.sleep(1000)
                        } catch (e: Exception) {
                            //
                        }
                    }
                } else {
                    while (true) {
                        try {
                            val fileInputStream = FileInputStream(File("/proc/stat"))
                            val stat = ArrayList(
                                Arrays.asList(
                                    *fileInputStream.bufferedReader(Charsets.UTF_8).readLine().split(
                                        (" ").toRegex()
                                    ).dropLastWhile { it.isEmpty() }.toTypedArray()
                                )
                            )
                            fileInputStream.close()
                            val nowIdle = stat[5].toFloat()
                            val nowTotal =
                                stat[2].toFloat() + stat[3].toFloat() + stat[4].toFloat() + stat[6].toFloat() + stat[7].toFloat() + stat[8].toFloat() + stat[9].toFloat()
                            val u =
                                100f * ((nowIdle - nowTotal) - (lastIdle - lastTotal)) / (nowIdle - lastIdle)
                            runOnUiThread { usage.text = u.toString() }
                            lastIdle = nowIdle
                            lastTotal = nowTotal
                        } catch (e: Exception) {
                            runOnUiThread { ShortToast(this, e, false) }
                            break
                        }
                        try {
                            Thread.sleep(1000)
                        } catch (e: Exception) {
                            //
                        }
                    }
                }

            }
            thread.start()
            threadList.add(thread)

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
                    c =
                        0 - batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) / 1000
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
            threadList.add(currentThread)
        }

        t2.start()
        val t3 = Thread {
            val root = javaFileReadLine("/sys/class/thermal/thermal_zone0/temp") == "Fail"
            val dir = File("/sys/class/thermal")
            val file = dir.listFiles().size
            for (i: Int in 0 until file) {
                val sensorDataChildView = SensorDataChildView(this, i, root)
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

        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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
        for (i in threadList) {
            i.interrupt()
        }
        super.onBackPressed()
    }

    class BatteryReceiver(
        activity: UsageActivity,
        progressBar: ProgressBar,
        level: TextView,
        voltage: TextView
    ) : BroadcastReceiver() {
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

    class CoreFreqView(activity: Activity, core: Int, root: Boolean) : LinearLayout(activity) {
        var thread: Thread? = null

        init {
            LayoutInflater.from(activity).inflate(R.layout.view_corefreq, this)

            if (!root) {
                thread = Thread {
                    var f: String
                    var lastFreq = ""
                    while (true) {
                        f = javaFileReadLine("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_cur_freq")
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
            } else {
                thread = Thread {
                    var f: String
                    var lastFreq = ""
                    while (true) {
                        f =
                            suFileReadLine("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_cur_freq")
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
            }

            thread!!.start()
        }

        fun interruptThread() {
            this.thread!!.interrupt()
        }
    }

    class SensorDataChildView(activity: Activity, no: Int, root: Boolean) : LinearLayout(activity) {
        var thread: Thread? = null

        init {
            LayoutInflater.from(activity).inflate(R.layout.view_sensordata, this)
            if (!root) {
                val t = javaFileReadLine("/sys/class/thermal/thermal_zone$no/type")
                title.text = t

                thread = Thread {
                    var d: StringBuilder
                    var lastData = ""
                    while (true) {
                        d = StringBuilder(javaFileReadLine("/sys/class/thermal/thermal_zone$no/temp"))
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
            } else {
                val t = suFileReadLine("/sys/class/thermal/thermal_zone$no/type")
                title.text = t

                thread = Thread {
                    var d: StringBuilder
                    var lastData = ""
                    while (true) {
                        d = StringBuilder(suFileReadLine("/sys/class/thermal/thermal_zone$no/temp"))
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
            }
            thread!!.start()
        }

        fun interruptThread() {
            this.thread!!.interrupt()
        }
    }

}