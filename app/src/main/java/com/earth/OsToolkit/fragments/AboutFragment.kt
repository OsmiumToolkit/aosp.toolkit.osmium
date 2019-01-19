package com.earth.OsToolkit.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import kotlinx.android.synthetic.main.fragment_about.*

import com.earth.OsToolkit.MainActivity
import com.earth.OsToolkit.R
import com.earth.OsToolkit.base.BaseFetching.Companion.getPackageVersion
import com.earth.OsToolkit.base.Accessing.Companion.accessCoolapkAccount
import com.earth.OsToolkit.base.Accessing.Companion.accessCoolapkRelease
import com.earth.OsToolkit.base.Accessing.Companion.accessGitHubSource
import com.earth.OsToolkit.base.CheckUpdate
import com.earth.OsToolkit.base.BaseIndex.*

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


class AboutFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        maintainer.setOnClickListener {
            accessCoolapkAccount(activity, MaintainerCoolapkID)
        }

        release.setSummary(activity!!.packageManager.getPackageInfo(PackageName, 0).versionName)

        update.setOnClickListener {
            val version = CheckUpdate.CheckVersion().getVersion()
            if (version != (getPackageVersion(activity)) && version != null) {
                val getChangelog = CheckUpdate.GetChangelog()
                getChangelog.onFetching()
                val updateDialogFragment = UpdateDialogFragment()
                updateDialogFragment.setData(version, CheckUpdate.GetDate().returnData(),
                    getChangelog.changelogZh, getChangelog.changelogEn)
            }
        }


        /*
        update.setOnClickListener {
            val checkUpdate = CheckUpdate()
            checkUpdate.waitFor()

            val v = checkUpdate.getVersion()
            if (v != getPackageVersion(activity)) {
                val updateDialogFragment = UpdateDialogFragment()
                updateDialogFragment.setData(checkUpdate.getVersion(),
                    checkUpdate.getDate(), checkUpdate.getChangelogZh(),
                    checkUpdate.getChangelogEn())
                updateDialogFragment.show(fragmentManager, "updateDialogFragment")
            }
        }
        */

        source.setOnClickListener {
            accessGitHubSource(activity)
        }

        xzr.setOnClickListener {
            accessCoolapkAccount(activity, XzrID)
        }
        yc.setOnClickListener {
            accessCoolapkAccount(activity, YcID)
        }

        val sharedPreferences = activity!!.getSharedPreferences("ui", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("navBar", true)) {
            switchCompact.isChecked = true
        }

        switchCompact.setOnCheckedChangeListener { _ , isChecked ->
            if (isChecked) {
                sharedPreferences.edit().putBoolean("navBar", true).apply()
            } else {
                sharedPreferences.edit().putBoolean("navBar", false).apply()
            }
            startActivity(Intent(activity, MainActivity::class.java))
        }

        navBar.setOnClickListener {
            if (switchCompact.isChecked) {
                sharedPreferences.edit().putBoolean("navBar", false).apply()
            } else {
                sharedPreferences.edit().putBoolean("navBar", true).apply()
            }
            startActivity(Intent(activity, MainActivity::class.java))
        }

        theme.setOnClickListener {
            val list : List<PackageInfo> = activity!!.packageManager.getInstalledPackages(0)
            val names : ArrayList<String> = ArrayList()

            if (!list.isEmpty()) {
                for (i : Int in 0 until list.size)
                    names.add(list[i].packageName)
            }

            if (names.contains(OsToolkitSubstratumName)) {
                if (names.contains(SubstratumName)) {
                    startActivity(activity!!.packageManager.getLaunchIntentForPackage(SubstratumName))
                } else {
                    accessCoolapkRelease(activity, SubstratumName)
                }
            } else {
                accessCoolapkRelease(activity, OsToolkitSubstratumName)
            }
        }
    }
}