package com.earth.OsToolkit.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.*
import com.earth.OsToolkit.R
import com.earth.OsToolkit.base.BaseManager
import com.earth.OsToolkit.base.BaseFetching
import com.earth.OsToolkit.view.CoreCardView
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_core, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val list: List<String> = listOf(
            "impulse",
            "zzmoove",
            "cafactive",
            "elementalx",
            "interactive",
            "conservative",
            "dndemand",
            "userspace",
            "powersave",
            "bioshock",
            "performance"
        )

        val dialog = Dialog(activity as Context)

        dialog.setContentView(LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null))
        dialog.setCancelable(false)
        dialog.show()


        Thread {
            for (i: Int in 0 until BaseFetching.getAvaliableCore()) {
                val coreCardView = CoreCardView(activity, i)
                coreCardView.setGovernor(list)
                activity!!.runOnUiThread {
                    core_rootView.addView(coreCardView)
                }
            }
            Timer().schedule(object : TimerTask(){
                override fun run() {
                    activity!!.runOnUiThread {
                        dialog.cancel()
                    }
                }
            }, 1000)
        }.start()
    }
}