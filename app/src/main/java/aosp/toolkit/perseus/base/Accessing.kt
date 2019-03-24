package aosp.toolkit.perseus.base

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.FragmentActivity
import aosp.toolkit.perseus.base.BaseIndex.*
import java.lang.Exception

/*
 * OsToolkit - Kotlin
 *
 * Date : 6/1/2019
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


class Accessing {
    companion object {
        fun accessGitHub(fragmentActivity: FragmentActivity?, source: String) {
            fragmentActivity!!.startActivity(
                Intent().setData(Uri.parse(source)).setAction(
                    Intent.ACTION_VIEW
                )
            )
        }

        fun openPackage(activity: Activity, packageManager: PackageManager, packageName: String) {
            activity.startActivity(packageManager.getLaunchIntentForPackage(packageName))
        }

        fun accessCoolapkRelease(fragmentActivity: FragmentActivity?, name: String) {
            val packageInfoList: List<PackageInfo> = fragmentActivity!!.packageManager.getInstalledPackages(0)
            val packageName: ArrayList<String> = ArrayList()

            for (i: Int in 0 until packageInfoList.size) {
                packageName.add(packageInfoList[i].packageName)
            }

            if (packageName.contains(CoolapkPackageName)) {
                try {
                    fragmentActivity.startActivity(
                        Intent().setData(Uri.parse("$Market_Head$name")).setPackage(
                            CoolapkPackageName
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    fragmentActivity.startActivity(
                        Intent().setData(Uri.parse("$Coolapk_Apk$name")).setAction(
                            Intent.ACTION_VIEW
                        )
                    )
                }
            } else {
                fragmentActivity.startActivity(
                    Intent().setData(Uri.parse("$Coolapk_Apk$name")).setAction(
                        Intent.ACTION_VIEW
                    )
                )
            }
        }

        fun accessCoolapkAccount(fragmentActivity: FragmentActivity?, account: String) {
            val packageInfoList: List<PackageInfo> = fragmentActivity!!.packageManager.getInstalledPackages(0)
            val packageName: ArrayList<String> = ArrayList()

            for (i: Int in 0 until packageInfoList.size) {
                packageName.add(packageInfoList[i].packageName)
            }

            if (packageName.contains(CoolapkPackageName)) {
                try {
                    fragmentActivity.startActivity(
                        Intent().setData(Uri.parse("$Coolapk_User$account")).setPackage(
                            CoolapkPackageName
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    fragmentActivity.startActivity(
                        Intent().setData(Uri.parse("$Coolapk_User$account")).setAction(
                            Intent.ACTION_VIEW
                        )
                    )
                }
            } else {
                fragmentActivity.startActivity(
                    Intent().setData(Uri.parse("$Coolapk_User$account")).setAction(
                        Intent.ACTION_VIEW
                    )
                )
            }
        }
    }
}