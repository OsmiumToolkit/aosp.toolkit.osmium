package aosp.toolkit.perseus.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import aosp.toolkit.perseus.R

import aosp.toolkit.perseus.base.BaseIndex
import aosp.toolkit.perseus.base.BaseManager
import aosp.toolkit.perseus.base.BaseOperation
import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast

import java.io.File
import java.lang.Exception
import java.net.URL
import java.util.zip.ZipFile


/*
 * @File:   ImportOfflinePackageService
 * @Author: 1552980358
 * @Time:   8:26 PM
 * @Date:   24 Apr 2019
 * 
 */

class ImportOfflinePackageService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    // onCreate

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ShortToast(applicationContext, getString(R.string.backgroundOperation))
        Thread {
            val zip: String = try {
                if (intent!!.getBooleanExtra("download", true)) {
                    val tmp = applicationContext.cacheDir.absolutePath + File.separator + "tmp.zip"
                    // 利用输入流下载
                    File(tmp).writeBytes(
                        URL("https://raw.githubusercontent.com/ToolkitPerseus/scripts/master/Perseus_Offline_${BaseIndex.OffLinePack}.zip").openStream().readBytes()
                    )
                    tmp
                } else {
                    intent.getStringExtra("zipLocation")
                }
            } catch (e: Exception) {
                // 防炸
                ShortToast(
                    if (BaseManager.getInstance().welcomeActivity != null) {
                        BaseManager.getInstance().welcomeActivity
                    } else {
                        BaseManager.getInstance().mainActivity
                    }, e, false
                )
                onDestroy()
                ""
            }
            unzip(zip)
            ShortToast(
                if (BaseManager.getInstance().welcomeActivity != null) {
                    BaseManager.getInstance().welcomeActivity
                } else {
                    BaseManager.getInstance().mainActivity
                }, getString(R.string.toast_succeed), false
            )
            onDestroy()
        }.start()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun unzip(zip: String) {
        val target = (applicationContext.externalCacheDir)!!.absolutePath
        try {

            val zipFile = ZipFile(zip)
            val entries = zipFile.entries()
            for (i in entries) {
                val file = BaseOperation.File(target, i.name)
                if (file.isExists()) {
                    file.delete()
                }
                // 输出
                file.createNewFile().writeBytes(zipFile.getInputStream(i).readBytes())
            }
            // 删除tmp
            File(zip).delete()

        } catch (e: Exception) {
            ShortToast(
                if (BaseManager.getInstance().welcomeActivity != null) {
                    BaseManager.getInstance().welcomeActivity
                } else {
                    BaseManager.getInstance().mainActivity
                }, e, false
            )
            return
        }
    }
}