package aosp.toolkit.perseus.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import kotlinx.android.synthetic.main.fragment_about.*

import aosp.toolkit.perseus.MainActivity
import aosp.toolkit.perseus.R
import aosp.toolkit.perseus.base.Accessing.Companion.accessCoolapkAccount
import aosp.toolkit.perseus.base.Accessing.Companion.accessCoolapkRelease
import aosp.toolkit.perseus.base.Accessing.Companion.accessGitHub
import aosp.toolkit.perseus.base.CheckUpdate
import aosp.toolkit.perseus.base.BaseIndex.*
import aosp.toolkit.perseus.base.BaseOperation.Companion.getPackageVersion
import aosp.toolkit.perseus.fragments.dialog.UpdateDialogFragment

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
                    .setData(version, CheckUpdate.GetDate().returnData(),
                    getChangelog.changelogZh, getChangelog.changelogEn)
                updateDialogFragment.show(fragmentManager, "updateChecking")
            }
        }

        source.setOnClickListener {
            accessGitHub(activity, Repo_Source)
        }

        xzr.setOnClickListener {
            accessCoolapkAccount(activity, XzrID)
        }
        yc.setOnClickListener {
            accessCoolapkAccount(activity, YcID)
        }
        pc.setOnClickListener {

        }
        libsu.setOnClickListener {
            accessGitHub(activity, Repo_libsu)
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