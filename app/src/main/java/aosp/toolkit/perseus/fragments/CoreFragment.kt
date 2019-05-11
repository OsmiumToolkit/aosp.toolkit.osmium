package aosp.toolkit.perseus.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.AdapterView

import aosp.toolkit.perseus.R

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

@Suppress("SpellCheckingInspection")
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
                for (i: Int in 0 until getAvailableCore()) {
                    val coreView = CoreView(activity!!, i)
                    activity!!.runOnUiThread {
                        core_rootView.addView(coreView)
                    }
                }


                val coreView = CoreView(activity!!, -1)
                activity!!.runOnUiThread {
                    core_rootView.addView(coreView)
                }

            } catch (e: Exception) {
                ShortToast(activity!!, e, false)
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

    @Suppress("SpellCheckingInspection")
    @SuppressLint("ViewConstructor", "SetTextI18n")
    class CoreView(
        activity: Activity, core: Int
    ) : LinearLayout(activity as Context) {

        init {
            LayoutInflater.from(activity).inflate(R.layout.view_core, this)
            if (core != -1) {
                // cpu

                title.text = "CPU$core"

                val l = ArrayList(
                    Arrays.asList(
                        *javaFileReadLine(
                            "/sys/devices/system/cpu/cpu$core/cpufreq/scaling_available_frequencies"
                        ).split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    )
                )

                val list = if (l.size > 1) {
                    l
                } else {
                    Arrays.asList(
                        *suFileReadLine(
                            "/sys/devices/system/cpu/cpu$core/cpufreq/scaling_available_frequencies"
                        ).split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    )
                }

                Thread {
                    @Suppress("UnnecessaryVariable") val tmp = list

                    val f =
                        javaFileReadLine("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_max_freq")
                    val freq = if (f != "Fail") {
                        f
                    } else {
                        suFileReadLine("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_max_freq")
                    }

                    val i = if (tmp.contains(freq)) {
                        tmp.indexOf(freq)
                    } else {
                        tmp.add(freq)
                        tmp.indexOf(freq)
                    }

                    val arrayAdapter = ArrayAdapter(
                        activity, android.R.layout.simple_spinner_dropdown_item, tmp
                    )
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    activity.runOnUiThread {
                        max_freq.adapter = arrayAdapter
                        max_freq.setSelection(i)
                        max_freq.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>, view: View, position: Int, id: Long
                                ) {
                                    Thread {
                                        try {
                                            Shell.su("echo \"${tmp[position]}\" > /sys/devices/system/cpu/cpu$core/cpufreq/scaling_max_freq")
                                        } catch (e: Exception) {
                                            ShortToast(activity, e, true)
                                        }
                                    }.start()
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {

                                }
                            }
                    }
                }.start()
                Thread {
                    @Suppress("UnnecessaryVariable") val tmp = list

                    val f =
                        javaFileReadLine("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_min_freq")
                    val freq = if (f != "Fail") {
                        f
                    } else {
                        suFileReadLine("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_min_freq")
                    }

                    val i = if (tmp.contains(freq)) {
                        tmp.indexOf(freq)
                    } else {
                        tmp.add(freq)
                        tmp.indexOf(freq)
                    }

                    val arrayAdapter = ArrayAdapter(
                        activity, android.R.layout.simple_spinner_dropdown_item, tmp
                    )
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    activity.runOnUiThread {
                        min_freq.adapter = arrayAdapter
                        min_freq.setSelection(i)
                        min_freq.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>, view: View, position: Int, id: Long
                                ) {
                                    Thread {
                                        try {
                                            Shell.su("echo \"${tmp[position]}\" > /sys/devices/system/cpu/cpu$core/cpufreq/scaling_min_freq")
                                        } catch (e: Exception) {
                                            ShortToast(activity, e, true)
                                        }
                                    }.start()
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {

                                }
                            }
                    }
                }.start()
                Thread {
                    val gL = ArrayList(
                        Arrays.asList(
                            *javaFileReadLine(
                                ("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_available_governors")
                            ).split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        )
                    )

                    val gList = if (gL.size > 1) {
                        gL
                    } else {
                        ArrayList(
                            Arrays.asList(
                                *suFileReadLine(
                                    ("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_available_governors")
                                ).split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            )
                        )
                    }

                    val g =
                        javaFileReadLine("sys/devices/system/cpu/cpu$core/cpufreq/scaling_governor")
                    val gov = if (g != "Fail") {
                        g
                    } else {
                        suFileReadLine("sys/devices/system/cpu/cpu$core/cpufreq/scaling_governor")
                    }

                    val i = gList.indexOf(gov)

                    val arrayAdapter = ArrayAdapter(
                        activity, android.R.layout.simple_spinner_dropdown_item, gList
                    )
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    activity.runOnUiThread {
                        governor.adapter = arrayAdapter
                        governor.setSelection(i)
                        governor.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>, view: View, position: Int, id: Long
                                ) {
                                    try {
                                        Shell.su("echo \"${list[position]}\" > /sys/devices/system/cpu/cpu$core/cpufreq/scaling_governor")
                                            .exec()
                                    } catch (e: Exception) {
                                        ShortToast(activity, e, true)
                                    }

                                }

                                override fun onNothingSelected(arg0: AdapterView<*>) {}
                            }
                    }
                }.start()
            } else {
                title.text = "GPU"

                val l = ArrayList(
                    Arrays.asList(
                        *javaFileReadLine(
                            "/sys/class/kgsl/kgsl-3d0/available_frequencies"
                        ).split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    )
                )

                val list = if (l.size > 1) {
                    l
                } else {
                    Arrays.asList(
                        *suFileReadLine(
                            "/sys/class/kgsl/kgsl-3d0/available_frequencies"
                        ).split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    )
                }

                Thread {
                    @Suppress("UnnecessaryVariable") val tmp = list

                    val f = javaFileReadLine("/sys/class/kgsl/kgsl-3d0/max_freq")
                    val freq = if (f != "Fail") {
                        f
                    } else {
                        suFileReadLine("/sys/class/kgsl/kgsl-3d0/max_freq")
                    }

                    val i = if (tmp.contains(freq)) {
                        tmp.indexOf(freq)
                    } else {
                        tmp.add(freq)
                        tmp.indexOf(freq)
                    }

                    val arrayAdapter = ArrayAdapter(
                        activity, android.R.layout.simple_spinner_dropdown_item, tmp
                    )
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    activity.runOnUiThread {
                        max_freq.adapter = arrayAdapter
                        max_freq.setSelection(i)
                        max_freq.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>, view: View, position: Int, id: Long
                                ) {
                                    Thread {
                                        try {
                                            Shell.su("echo \"${tmp[position]}\" > /sys/class/kgsl/kgsl-3d0/max_freq")
                                        } catch (e: Exception) {
                                            ShortToast(activity, e, true)
                                        }
                                    }.start()
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {

                                }
                            }
                    }
                }.start()
                Thread {
                    @Suppress("UnnecessaryVariable") val tmp = list

                    val f = javaFileReadLine("/sys/class/kgsl/kgsl-3d0/min_freq")
                    val freq = if (f != "Fail") {
                        f
                    } else {
                        suFileReadLine("/sys/class/kgsl/kgsl-3d0/min_freq")
                    }

                    val i = if (tmp.contains(freq)) {
                        tmp.indexOf(freq)
                    } else {
                        tmp.add(freq)
                        tmp.indexOf(freq)
                    }

                    val arrayAdapter = ArrayAdapter(
                        activity, android.R.layout.simple_spinner_dropdown_item, tmp
                    )
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    activity.runOnUiThread {
                        min_freq.adapter = arrayAdapter
                        min_freq.setSelection(i)
                        min_freq.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>, view: View, position: Int, id: Long
                                ) {
                                    Thread {
                                        try {
                                            Shell.su("echo \"${tmp[position]}\" > /sys/class/kgsl/kgsl-3d0/min_freq")
                                        } catch (e: Exception) {
                                            ShortToast(activity, e, true)
                                        }
                                    }.start()
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {

                                }
                            }
                    }
                }.start()
                Thread {
                    val gL = ArrayList(
                        Arrays.asList(
                            *javaFileReadLine(
                                ("/sys/class/kgsl/kgsl-3d0/available_governors")
                            ).split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        )
                    )

                    val gList = if (gL.size > 1) {
                        gL
                    } else {
                        ArrayList(
                            Arrays.asList(
                                *suFileReadLine(
                                    ("/sys/class/kgsl/kgsl-3d0/available_governors")
                                ).split((" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            )
                        )
                    }

                    val g = javaFileReadLine("/sys/class/kgsl/kgsl-3d0/governor")
                    val gov = if (g != "Fail") {
                        g
                    } else {
                        suFileReadLine("/sys/class/kgsl/kgsl-3d0/governor")
                    }

                    val i = gList.indexOf(gov)

                    val arrayAdapter = ArrayAdapter(
                        activity, android.R.layout.simple_spinner_dropdown_item, gList
                    )
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    activity.runOnUiThread {
                        governor.adapter = arrayAdapter
                        governor.setSelection(i)
                        governor.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>, view: View, position: Int, id: Long
                                ) {
                                    try {
                                        Shell.su("echo \"${list[position]}\" > /sys/class/kgsl/kgsl-3d0/governor")
                                            .exec()
                                    } catch (e: Exception) {
                                        ShortToast(activity, e, true)
                                    }

                                }

                                override fun onNothingSelected(arg0: AdapterView<*>) {}
                            }
                    }
                }.start()


            }
        }
    }
}