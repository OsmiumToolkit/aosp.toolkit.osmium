package com.earth.OsToolkit

import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.*
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.getAvailableCore
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.readFile

import kotlinx.android.synthetic.main.activity_usage.*
import kotlinx.android.synthetic.main.view_corefreq_text.view.*

import java.util.*


/*
 * OsToolkit - Kotlin
 *
 * Date : 25 Jan 2019
 *
 * By   : 1552980358
 *
 */

class UsageActivity : AppCompatActivity() {
    var batteryReceiver : BatteryReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage)
        initialize()

        val dialog = Dialog(this)
        dialog.setContentView(LayoutInflater.from(this).inflate(R.layout.dialog_loading, null))
        dialog.setCancelable(false)
        dialog.show()

        Thread {
            for (i: Int in 0 until getAvailableCore()) {
                val coreFreqViewText = CoreFreqViewText(this, i)
                runOnUiThread {
                    root.addView(coreFreqViewText)
                }
            }
            Timer().schedule(object : TimerTask(){
                override fun run() {
                    dialog.cancel()
                }
            }, 1000)
        }.start()

        Thread {
            /*
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            Log.i("battery",int.toString())
            runOnUiThread {
                textView.text = int.toString()
                progressBar.progress = int
            }
            */

            val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            batteryReceiver = BatteryReceiver(this, progressBar, textView)
            registerReceiver(batteryReceiver, intentFilter)

        }.start()
    }

    private fun initialize() {
        setSupportActionBar(toolbar)

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (getSharedPreferences("ui", Context.MODE_PRIVATE).getBoolean("navBar", true)) {
            // ContextCompact通用包
            window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

            // 5.0+可过编译 6.0+弃用 无法更改颜色
            // window.navigationBarColor = resources.getColor(R.color.colorPrimaryDark)
            // 6.0加入的API 无法适配 5.0 / 5.1
            // window.navigationBarColor = resources.getColor(R.color.colorPrimary, null)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    class BatteryReceiver(activity: UsageActivity, progressBar: ProgressBar, textView: TextView) : BroadcastReceiver() {
        private var activity : UsageActivity? = null
        private var progressBar : ProgressBar? = null
        private var textView : TextView? = null
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
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
    }

    @Suppress("all")
    class CoreFreqViewText(activity: UsageActivity, core: Int) : LinearLayout(activity) {
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

            Thread {
                while (true) {
                    val freq = readFile("/sys/devices/system/cpu/cpu"
                            + core + "/cpufreq/scaling_cur_freq")
                    val u = (freq.toFloat() / maxFreq * 100).toInt()

                    activity.runOnUiThread {
                        cur_freq.text = freq
                        usage.text = "$u%"
                    }
                    Thread.sleep(1000)
                }
            }.start()
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