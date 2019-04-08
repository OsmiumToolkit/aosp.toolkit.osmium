package aosp.toolkit.perseus.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.AdapterView

import aosp.toolkit.perseus.R
import aosp.toolkit.perseus.base.BaseOperation
import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast
import aosp.toolkit.perseus.base.BaseOperation.Companion.getAvailableCore
import aosp.toolkit.perseus.base.BaseOperation.Companion.javaFileReadLine
import aosp.toolkit.perseus.base.BaseOperation.Companion.suFileReadLine

import com.topjohnwu.superuser.Shell

import kotlinx.android.synthetic.main.fragment_core.*
import kotlinx.android.synthetic.main.view_core.view.*

import java.util.TimerTask
import java.util.Timer
import java.util.Arrays


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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_core, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val dialog = Dialog(activity as Context)
        dialog.setContentView(LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null))
        dialog.setCancelable(false)
        dialog.show()

        Thread {
            try {
                val root =
                    javaFileReadLine("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies") == "Fail"
                Log.e("checkRoot", root.toString())


                for (i: Int in 0 until getAvailableCore()) {
                    val coreCardView = CoreView(activity!!, i, root)
                    activity!!.runOnUiThread {
                        core_rootView.addView(coreCardView)
                    }
                }
            } catch (e: Exception) {
                ShortToast(activity!!, e, false)
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

    @SuppressLint("ViewConstructor")
    class CoreView(
        activity: Activity, core: Int, root: Boolean
    ) : LinearLayout(activity as Context) {

        init {
            LayoutInflater.from(activity).inflate(R.layout.view_core, this)
            title.append(core.toString())

            val list = if (!root) {
                ArrayList(
                    Arrays.asList(
                        *javaFileReadLine(
                            "/sys/devices/system/cpu/cpu$core/cpufreq/scaling_available_frequencies"
                        ).split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    )
                )
            } else {
                Arrays.asList(
                    *suFileReadLine(
                        "/sys/devices/system/cpu/cpu$core/cpufreq/scaling_available_frequencies"
                    ).split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                )
            }

            Thread {
                val tmp = list
                val freq = if (!root) {
                    javaFileReadLine(
                        "/sys/devices/system/cpu/cpu$core/cpufreq/scaling_max_freq"
                    )
                } else {
                    suFileReadLine("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_max_freq")
                }

                val i = if (tmp.contains(freq)) {
                    tmp.indexOf(freq)
                } else {
                    tmp.add(freq)
                    list.size
                }

                val arrayAdapter = ArrayAdapter(
                    activity, android.R.layout.simple_spinner_dropdown_item, tmp
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                activity.runOnUiThread {
                    max_freq.adapter = arrayAdapter
                    max_freq.setSelection(i)
                    max_freq.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View, position: Int, id: Long
                        ) {
                            Thread {
                                try {
                                    Shell.su("echo \"${tmp[position]}\" > sys/devices/system/cpu/cpu$core/cpufreq/scaling_max_freq")
                                } catch (e: Exception) {
                                    BaseOperation.ShortToast(activity, e, true)
                                }
                            }.start()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {

                        }
                    }
                }
            }.start()
            Thread {
                val tmp = list
                val freq = if (!root) {
                    javaFileReadLine(
                        "/sys/devices/system/cpu/cpu$core/cpufreq/scaling_min_freq"
                    )
                } else {
                    suFileReadLine("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_min_freq")
                }

                val i = if (tmp.contains(freq)) {
                    tmp.indexOf(freq)
                } else {
                    tmp.add(freq)
                    list.size
                }

                val arrayAdapter = ArrayAdapter(
                    activity, android.R.layout.simple_spinner_dropdown_item, tmp
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                activity.runOnUiThread {
                    min_freq.adapter = arrayAdapter
                    min_freq.setSelection(i)
                    min_freq.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View, position: Int, id: Long
                        ) {
                            Thread {
                                try {
                                    Shell.su("echo \"${tmp[position]}\" > sys/devices/system/cpu/cpu$core/cpufreq/scaling_min_freq")
                                } catch (e: Exception) {
                                    BaseOperation.ShortToast(activity, e, true)
                                }
                            }.start()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {

                        }
                    }
                }
            }.start()
            Thread {
                val gList = if (!root) {
                    ArrayList(
                        Arrays.asList(
                            *javaFileReadLine(
                                ("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_available_governors")
                            ).split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        )
                    )
                } else {
                    ArrayList(
                        Arrays.asList(
                            *suFileReadLine(
                                ("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_available_governors")
                            ).split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        )
                    )
                }

                val gov = if (!root) {
                    javaFileReadLine(
                        "sys/devices/system/cpu/cpu$core/cpufreq/scaling_governor"
                    )
                } else {
                    suFileReadLine(
                        "sys/devices/system/cpu/cpu$core/cpufreq/scaling_governor"
                    )
                }

                val i = gList.indexOf(gov)

                val arrayAdapter = ArrayAdapter(
                    activity, android.R.layout.simple_spinner_dropdown_item, gList
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                activity.runOnUiThread {
                    governor.adapter = arrayAdapter
                    governor.setSelection(i)
                    governor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View, position: Int, id: Long
                        ) {
                            try {
                                Shell.su("echo \"${list[position]}\" > sys/devices/system/cpu/cpu$core/cpufreq/scaling_governor")
                                    .exec()
                            } catch (e: Exception) {
                                BaseOperation.ShortToast(activity, e, true)
                            }

                        }

                        override fun onNothingSelected(arg0: AdapterView<*>) {}
                    }
                }


            }.start()
        }
    }

    /*
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

            val i = if (!list.contains(freq)) {
                list.add(freq)
                list.size - 1
            } else {
                list.indexOf(freq)
            }

            val arrayAdapter = ArrayAdapter(
                activity,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )

            activity.runOnUiThread {
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = arrayAdapter
                spinner.setSelection(i)
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        try {
                            Shell.su("echo \"${list[position]}\" > sys/devices/system/cpu/cpu$core/cpufreq/scaling_max_freq")
                        } catch (e: Exception) {
                            BaseOperation.ShortToast(activity, e, true)
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

            val i = if (!list.contains(freq)) {
                list.add(freq)
                list.size - 1
            } else {
                list.indexOf(freq)
            }

            val arrayAdapter = ArrayAdapter(
                activity,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )

            activity.runOnUiThread {
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = arrayAdapter
                spinner.setSelection(i)
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        try {
                            Shell.su("echo \"${list[position]}\" > sys/devices/system/cpu/cpu$core/cpufreq/scaling_min_freq")
                                .exec()
                        } catch (e: Exception) {
                            BaseOperation.ShortToast(activity, e, true)
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

            val i = list.indexOf(governor)

            val arrayAdapter = ArrayAdapter(
                activity,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )

            activity.runOnUiThread {
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = arrayAdapter
                spinner.setSelection(i)
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        try {
                            Shell.su("echo \"${list[position]}\" > sys/devices/system/cpu/cpu$core/cpufreq/scaling_governor")
                                .exec()
                        } catch (e: Exception) {
                            BaseOperation.ShortToast(activity, e, true)
                        }

                    }

                    override fun onNothingSelected(arg0: AdapterView<*>) {}
                }
            }
        }
    }
    */

}