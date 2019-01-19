package com.earth.OsToolkit.fragments

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
    }

    var handler = Handler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

        val list: List<String> = listOf("impulse",
            "zzmoove",
            "cafactive",
            "elementalx",
            "interactive",
            "conservative",
            "dndemand",
            "userspace",
            "powersave",
            "bioshock",
            "performance")

        for (i: Int in 0 until BaseFetching.getAvaliableCore()) {
            val coreCardView = CoreCardView(activity, i)
            coreCardView.setGovernor(list)
            core_rootView.addView(coreCardView)
        }

        val timer1 = Timer()
        timer1.schedule(object : TimerTask() {
            override fun run() {
                handler.post(hideProgress)
            }
        }, 3000)
    }

    val hideProgress = Runnable {
        progressBar.visibility = View.GONE
    }

}