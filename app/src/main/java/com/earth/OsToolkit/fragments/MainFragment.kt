package com.earth.OsToolkit.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import com.earth.OsToolkit.DisableAppActivity

import com.earth.OsToolkit.R
import com.earth.OsToolkit.UsageActivity
import com.earth.OsToolkit.base.Accessing
import com.earth.OsToolkit.base.Accessing.Companion.accessCoolapkRelease
import com.earth.OsToolkit.base.BaseIndex.PackageName
import kotlinx.android.synthetic.main.fragment_main.*


/*
 * OsToolkit - Kotlin
 *
 * Date : 31/12/2018 [Modify : 9/1/2019]
 *
 * By   : 1552980358
 *
 */

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        header.setOnClickListener {
            if (content.visibility == View.GONE) {
                content.visibility = View.VISIBLE
                indicator.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_open))
            } else {
                content.visibility = View.GONE
                indicator.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_hide))
            }
        }

        re_system.setOnClickListener {
            val process : Process = Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot"))
            process.waitFor()
        }

        re_rec.setOnClickListener {
            val process : Process = Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot", "recovery"))
            process.waitFor()
        }

        re_soft.setOnClickListener {
            val process : Process = Runtime.getRuntime().exec(arrayOf("su","-c","killall","zygote"))
            process.waitFor()
        }

        coolapk.setOnClickListener {
            accessCoolapkRelease(activity, PackageName)
        }
        github.setOnClickListener {
            Accessing.accessGibHubRelease(activity)
        }
        jump.setOnClickListener {
            startActivity(Intent(activity, UsageActivity::class.java))
        }
        jump2.setOnClickListener {
            startActivity(Intent(activity, DisableAppActivity::class.java))
        }
    }
}