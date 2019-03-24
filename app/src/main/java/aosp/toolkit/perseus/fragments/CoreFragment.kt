package aosp.toolkit.perseus.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.*

import aosp.toolkit.perseus.R
import aosp.toolkit.perseus.base.BaseOperation
import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast
import aosp.toolkit.perseus.base.BaseOperation.Companion.getAvailableCore

import com.topjohnwu.superuser.Shell

import kotlinx.android.synthetic.main.fragment_core.*
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

class CoreFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_core, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val dialog = Dialog(activity as Context)
        dialog.setContentView(LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null))
        dialog.setCancelable(false)
        dialog.show()

        Thread {
            try {
                for (i: Int in 0 until getAvailableCore()) {
                    val coreCardView = CoreView(activity!!, i)
                    activity!!.runOnUiThread {
                        core_rootView.addView(coreCardView)
                    }
                }
            } catch (e: Exception) {
                ShortToast(activity!!, e.toString(), false)
                activity!!.runOnUiThread {
                    aosp.toolkit.perseus.base.BaseManager.getInstance().exceptionBreaker(this)
                }
            } finally {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        activity!!.runOnUiThread {
                            dialog.cancel()
                        }
                    }
                }, 1000)
            }
        }.start()
    }

    @Suppress("ViewConstructor")
    class CoreView(private val activity: Activity, private val core: Int) : LinearLayout(activity) {

        init {
            LayoutInflater.from(activity).inflate(R.layout.view_core, this)

            val textView = findViewById<TextView>(R.id.ccv_title)
            textView.append("CPU$core")

            Thread(Runnable { this.setMaxCurrentFreq() }).start()
            Thread(Runnable { this.setMinCurrentFreq() }).start()
            Thread(Runnable { this.setGovernor() }).start()
        }

        private fun setMaxCurrentFreq() {
            val spinner = findViewById<Spinner>(R.id.ccv_max_freq)

            val list = ArrayList(
                Arrays.asList(
                    *BaseOperation.readFile(
                        "/sys/devices/system/cpu/cpu"
                                + core + "/cpufreq/scaling_available_frequencies"
                    )
                        .split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                )
            )

            val freq = BaseOperation.readFile(
                ("/sys/devices/system/cpu/cpu"
                        + core + "/cpufreq/" + "scaling_max_freq")
            )

            var i = 0

            if (list.contains(freq)) {
                list.add(freq)
                i = list.size - 1
            } else {
                while (i < list.size) {
                    if (list[i] == freq) {
                        break
                    } else {
                        i++
                    }
                }
            }
            val a = i

            val arrayAdapter = ArrayAdapter(
                activity,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )

            activity.runOnUiThread {
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = arrayAdapter
                spinner.setSelection(a)
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        try {
                            Shell.su("echo \"${list[position]}\" > sys/devices/system/cpu/cpu$core/cpufreq/scaling_max_freq")
                        } catch (e: Exception) {
                            BaseOperation.ShortToast(activity, e.toString(), true)
                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            }

        }

        private fun setMinCurrentFreq() {
            val spinner = findViewById<Spinner>(R.id.ccv_min_freq)

            val list = ArrayList(
                Arrays.asList(
                    *BaseOperation.readFile(
                        ("/sys/devices/system/cpu/cpu"
                                + core + "/cpufreq/scaling_available_frequencies")
                    )
                        .split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                )
            )

            val freq = BaseOperation.readFile("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_min_freq")

            var j = 0

            if (!list.contains(freq)) {
                list.add(freq)
                j = list.size - 1
            } else {
                while (j < list.size) {
                    if (list[j] == freq) {
                        break
                    } else {
                        j++
                    }
                }
            }

            val a = j

            val arrayAdapter = ArrayAdapter(
                activity,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )

            activity.runOnUiThread {
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = arrayAdapter
                spinner.setSelection(a)
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        try {
                            Shell.su("echo \"${list[position]}\" > sys/devices/system/cpu/cpu$core/cpufreq/scaling_min_freq")
                                .exec()
                        } catch (e: Exception) {
                            BaseOperation.ShortToast(activity, e.toString(), true)
                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            }

        }

        private fun setGovernor() {

            val spinner = findViewById<Spinner>(R.id.ccv_governor)

            val list = ArrayList(
                Arrays.asList(
                    *BaseOperation.readFile(
                        ("/sys/devices/system/cpu/cpu"
                                + core + "/cpufreq/scaling_available_governors")
                    )
                        .split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                )
            )

            val governor = BaseOperation.readFile(
                ("sys/devices/system/cpu/cpu"
                        + core + "/cpufreq/scaling_governor")
            )

            var k = 0

            while (k < list.size) {
                if (list[k] == governor) {
                    break
                } else {
                    k++
                }
            }

            val a = k

            val arrayAdapter = ArrayAdapter(
                activity,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )

            activity.runOnUiThread {
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = arrayAdapter
                spinner.setSelection(a)
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        try {
                            Shell.su("echo \"${list[position]}\" > sys/devices/system/cpu/cpu$core/cpufreq/scaling_governor")
                                .exec()
                        } catch (e: Exception) {
                            BaseOperation.ShortToast(activity, e.toString(), true)
                        }

                    }

                    override fun onNothingSelected(arg0: AdapterView<*>) {}
                }
            }
        }
    }

}