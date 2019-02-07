package com.earth.OsToolkit

import android.app.Activity
import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.*
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.ShortToast
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getAvailableCore
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.readFile

import kotlinx.android.synthetic.main.activity_usage.*
import kotlinx.android.synthetic.main.view_corefreq_text.view.*
import kotlinx.android.synthetic.main.view_sensordata.view.*
import java.io.File

import java.util.*

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
    private val coreFreqViewTextList = mutableListOf<CoreFreqViewText>()
    private val sensorDataChildViewList = mutableListOf<SensorDataChildView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage)
        initialize()

        val dialog = Dialog(this)
        dialog.setContentView(LayoutInflater.from(this).inflate(R.layout.dialog_loading, null))
        dialog.setCancelable(false)
        dialog.show()

        val t1 = Thread {
            for (i: Int in 0 until getAvailableCore()) {
                val coreFreqViewText = CoreFreqViewText(this, i)
                coreFreqViewTextList.add(coreFreqViewText)
                runOnUiThread {
                    rootFreq.addView(coreFreqViewText)
                }
            }
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    dialog.cancel()
                }
            }, 1000)
        }
        t1.start()

        val t2 = Thread {
            val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            batteryReceiver = BatteryReceiver(this, progressBar, textView)
            registerReceiver(batteryReceiver, intentFilter)
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
                dialog.cancel()
            }
        }
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
        finish()
        super.onBackPressed()
    }

    class BatteryReceiver(activity: UsageActivity, progressBar: ProgressBar, textView: TextView) : BroadcastReceiver() {
        private var activity: UsageActivity? = null
        private var progressBar: ProgressBar? = null
        private var textView: TextView? = null

        init {
            this.activity = activity
            this.progressBar = progressBar
            this.textView = textView
        }

        override fun onReceive(context: Context?, intent: Intent?) {
            val level = intent!!.getIntExtra("level", 0)
            val total = intent.getIntExtra("scale", 100)
            val per = level * 100 / total
            activity!!.runOnUiThread {
                progressBar!!.progress = per
                textView!!.text = per.toString()
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(batteryReceiver)
        for (i: Int in 0 until coreFreqViewTextList.size) {
            coreFreqViewTextList[i].interruptThread()
        }
        for (i: Int in 0 until sensorDataChildViewList.size) {
            sensorDataChildViewList[i].interruptThread()
        }
        super.onDestroy()
    }

    @Suppress("all")
    class CoreFreqViewText(activity: UsageActivity, core: Int) : LinearLayout(activity) {
        private var thread: Thread? = null

        init {
            LayoutInflater.from(activity).inflate(R.layout.view_corefreq_text, this)
            val array = ArrayList(
                Arrays.asList(
                    *readFile(
                        "/sys/devices/system/cpu/cpu"
                                + core + "/cpufreq/scaling_available_frequencies"
                    ).split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                )
            )
            val maxFreq = array[array.size - 1].toFloat()

            cpu.text = "CPU$core"

            thread = Thread {
                while (true) {
                    val tmp = readFile(
                        "/sys/devices/system/cpu/cpu"
                                + core + "/cpufreq/scaling_cur_freq"
                    )
                    val freq = when (tmp) {
                        "Fail" -> "0"
                        else -> tmp
                    }

                    val u = (freq.toFloat() / maxFreq * 100).toInt()

                    activity.runOnUiThread {
                        cur_freq.text = freq
                        usage.text = "$u%"
                    }
                    try {
                        Thread.sleep(1000)
                    } catch (e: Exception) {
                        ShortToast(activity, e.toString())
                    }
                }
            }
            thread!!.start()
        }

        fun interruptThread() {
            thread!!.interrupt()
        }
    }

    class SensorDataChildView(activity: Activity, no: Int) : LinearLayout(activity) {
        var thread: Thread? = null

        init {
            LayoutInflater.from(activity).inflate(R.layout.view_sensordata, this)
            val t = readFile("/sys/class/thermal/thermal_zone$no/type")
            title.text = t

            thread = Thread {
                while (true) {
                    var d = readFile("/sys/class/thermal/thermal_zone$no/temp")
                    if (d.length == 5) {
                        d = """${d[0]}${d[1]}.${d[2]}${d[3]}${d[4]}"""
                    }
                    activity.runOnUiThread { content.text = d }
                    try {
                        Thread.sleep(1000)
                    } catch (e: Exception) {
                        ShortToast(activity as Context, e.toString())
                    }
                }
            }
            thread!!.start()
        }

        fun interruptThread() {
            thread!!.interrupt()
        }
    }


/*
    private fun graph() {
        lineChartView.isInteractive = true
        lineChartView.isScrollEnabled = true
        val viewPort = Viewport()
        viewPort.left = 0f
        viewPort.right = 5f
        viewPort.top = 4f
        viewPort.bottom = 0f
        lineChartView.currentViewport = viewPort

        val pointValue = mutableListOf<PointValue>()
        val axisValue = mutableListOf<AxisValue>()
        val lines = mutableListOf<Line>()
        val maxSizePoint = 10

        Thread {
            var i = 0
            while (true) {
                val float =
                    readFile("/sys/devices/system/cpu/cpu" + 1 + "/cpufreq/" + "scaling_cur_freq").toFloat() / 1000 / 1000
                Log.i("float", float.toString())

                if (pointValue.size < maxSizePoint) {
                    pointValue.add(PointValue(pointValue.size.plus(1).toFloat(), float))
                    axisValue.add(AxisValue(i.toFloat()).setLabel(i.toString() + "s"))
                } else {
                    for (j: Int in 0 until maxSizePoint) {
                        pointValue[j] = if (j < maxSizePoint - 1) {
                            pointValue[j + 1]
                        } else {
                            PointValue(j.toFloat(), float)
                        }
                    }
                }
                i += 2

                val line = Line(pointValue)
                line.setHasLabels(false)
                line.shape = ValueShape.DIAMOND
                line.isCubic = true
                line.setHasLines(true)
                line.isFilled = true
                line.setHasPoints(false)
                line.color = R.color.colorPrimaryDark
                line.strokeWidth = 1
                lines.add(line)

                val lineChartData = LineChartData()
                lineChartData.lines = lines
                lineChartData.baseValue = Float.NEGATIVE_INFINITY

                val axisX = Axis()
                axisX.values = axisValue
                axisX.setHasLines(true)
                lineChartData.axisXBottom = axisX

                val axisY = Axis()
                axisY.setHasLines(true)
                lineChartData.axisYLeft = axisY

                runOnUiThread {
                    lineChartView.isInteractive = true
                    lineChartView.isScrollEnabled = true
                    lineChartView.lineChartData = lineChartData
                }

                Thread.sleep(2000)
            }

        }
    }

    */
}