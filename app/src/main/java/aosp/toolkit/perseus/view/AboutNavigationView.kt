package aosp.toolkit.perseus.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import aosp.toolkit.perseus.BuildConfig
import aosp.toolkit.perseus.MainActivity

import aosp.toolkit.perseus.R
import aosp.toolkit.perseus.base.Accessing
import aosp.toolkit.perseus.base.BaseIndex
import aosp.toolkit.perseus.base.BaseManager
import kotlinx.android.synthetic.main.view_about_navigation.view.*

/*
 * OsToolkit - Kotlin
 *
 * Date : 30 Mar 2019
 *
 * By   : 1552980358
 *
 */

@Suppress("UNUSED_PARAMETER")
class AboutNavigationView : LinearLayout {
    constructor(context: Context, attributeSet: AttributeSet?): this(context)
    constructor(context: Context) : super(context) {
        LayoutInflater.from(context).inflate(R.layout.view_about_navigation, this)
        val mainActivity = BaseManager.getInstance().mainActivity
        maintainer.setOnClickListener {
            Accessing.accessCoolapkAccount(mainActivity, BaseIndex.MaintainerCoolapkID)
        }
        update.setOnClickListener {
            BaseManager.getInstance().mainActivity.checkUpdate()
        }
        source.setOnClickListener {
            Accessing.accessGitHub(mainActivity, BaseIndex.Repo_Source)
        }
        val array = resources.getStringArray(R.array.version)
        version.text = array[BaseIndex.versionIndex]
        version.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        release.setSummary(
            BuildConfig.VERSION_NAME
        )
        val sharedPreferences = mainActivity.getSharedPreferences("ui", Context.MODE_PRIVATE)
        switchCompact.isChecked = sharedPreferences.getBoolean("navBar", true)
        switchCompact.setOnCheckedChangeListener { _, isChecked ->
            Thread {
                sharedPreferences.edit().putBoolean("navBar", isChecked).apply()
                mainActivity.startActivity(Intent(mainActivity, MainActivity::class.java))
            }.start()
        }
        navBar.setOnClickListener {
            Thread {
                sharedPreferences.edit().putBoolean("navBar", !switchCompact.isChecked).apply()
                mainActivity.startActivity(Intent(mainActivity, MainActivity::class.java))
            }.start()
        }
        theme.setOnClickListener {
            Thread {
                val list: List<PackageInfo> = mainActivity.packageManager.getInstalledPackages(0)
                val names: ArrayList<String> = ArrayList()

                if (!list.isEmpty()) {
                    for (i: Int in 0 until list.size) names.add(list[i].packageName)
                }

                if (names.contains(BaseIndex.OsToolkitSubstratumName)) {
                    if (names.contains(BaseIndex.SubstratumName)) {
                        mainActivity.startActivity(
                            mainActivity.packageManager.getLaunchIntentForPackage(
                                BaseIndex.SubstratumName
                            )
                        )
                    } else {
                        Accessing.accessCoolapkRelease(mainActivity, BaseIndex.SubstratumName)
                    }
                } else {
                    Accessing.accessCoolapkRelease(mainActivity, BaseIndex.OsToolkitSubstratumName)
                }
            }.start()
        }
        licence.setOnClickListener { BaseManager.getInstance().mainActivity.showLicenceList() }
    }
}