package aosp.toolkit.perseus

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import aosp.toolkit.perseus.base.*
import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast

import kotlinx.android.synthetic.main.activity_downloadmiui.*
import kotlinx.android.synthetic.main.activity_selectdownload.*
import kotlinx.android.synthetic.main.fragment_download.*
import kotlinx.android.synthetic.main.item_selectdownload.view.*
import kotlinx.android.synthetic.main.view_device.view.*
import kotlinx.android.synthetic.main.view_selectdownload.view.*

import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.*

import java.lang.Exception
import java.net.URL

/*
 * @File:   DownloadMIUIActivity
 * @Author: 1552980358
 * @Time:   6:28 PM
 * @Date:   5 Apr 2019
 * 
 */

class DownloadMIUIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloadmiui)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val tabList = listOf(getString(R.string.ver_cn), getString(R.string.ver_global))
        val fragmentList = listOf(MIUIChinaFragment(), MIUIGlobalFragment())
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, fragmentList, tabList)
        tabLayout.setupWithViewPager(viewPager)

    }

    class MIUIChinaFragment : Fragment() {
        private var created = false
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_download, container, false)
        }

        override fun setUserVisibleHint(isVisibleToUser: Boolean) {
            super.setUserVisibleHint(isVisibleToUser)
            if (!created && isVisibleToUser) {
                Thread {
                    try {
                        val document = Jsoup.connect("http://www.miui.com/download.html").get()

                        var tables = ""
                        val variables = document.getElementsByTag("script")
                        for (i in variables) {
                            val tmp = i.data().toString()
                            if (tmp.contains("phones")) {
                                tables = "{\n\"phone\":" + tmp.substring(
                                    tmp.indexOf("=") + 1, tmp.indexOf(";")
                                ) + "\n}"
                                break
                            }
                        }

                        if (!tables.isEmpty()) {

                            // 解析 Decode
                            val jsonArray = JSONObject(tables).getJSONArray("phone")
                            for (i: Int in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)

                                val bitmap =
                                    BitmapFactory.decodeStream(URL(jsonObject.getString("pic")).openStream())
                                val deviceView = DeviceView(
                                    activity!!,
                                    jsonObject.getString("name"),
                                    jsonObject.getString("version"),
                                    bitmap,
                                    jsonObject.getString("pid"),
                                    ""
                                )
                                val layoutParams = GridLayout.LayoutParams()
                                layoutParams.columnSpec = GridLayout.spec(i % 2, 1f)
                                layoutParams.rowSpec = GridLayout.spec(i / 2, 1f)

                                activity!!.runOnUiThread {
                                    gridLayout.addView(
                                        deviceView, layoutParams
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        ShortToast(activity!!, e, false)
                    }
                    created = true
                }.start()
            }
        }
    }

    class MIUIGlobalFragment : Fragment() {
        private var created = false
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_download, container, false)
        }

        override fun setUserVisibleHint(isVisibleToUser: Boolean) {
            super.setUserVisibleHint(isVisibleToUser)
            if (!created && isVisibleToUser) {
                Thread {
                    try {
                        val document = Jsoup.connect("http://en.miui.com/download.html").get()
                        val file =
                            File(context!!.externalCacheDir!!.absolutePath + File.separator + "a.txt")
                        if (file.exists()) file.delete()
                        val fileWriter = FileWriter(file)
                        fileWriter.write(document.toString())
                        fileWriter.close()

                        var tables = ""
                        val variables = document.getElementsByTag("script")
                        for (i in variables) {
                            val tmp = i.data().toString()
                            if (tmp.contains("phones")) {
                                tables = "{\n\"phone\":" + tmp.substring(
                                    tmp.indexOf("=") + 1, tmp.indexOf(";")
                                ) + "\n}"
                                break
                            }
                        }


                        if (!tables.isEmpty()) {
                            // 解析 Decode
                            val jsonArray = JSONObject(tables).getJSONArray("phone")
                            for (i: Int in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val s = jsonObject.getString("pic")
                                val bitmap = try {
                                    BitmapFactory.decodeStream(URL(s).openStream())
                                } catch (e: Exception) {
                                    val drawable = ContextCompat.getDrawable(context!! ,R.drawable.ic_phone_android)
                                    val b = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                                    val canvas = Canvas(b)
                                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                                    drawable.draw(canvas)
                                    b
                                }

                                val deviceView = DeviceView(
                                    activity!!,
                                    jsonObject.getString("name"),
                                    jsonObject.getString("version"),
                                    bitmap,
                                    jsonObject.getString("pid"),
                                    "en"
                                )
                                val layoutParams = GridLayout.LayoutParams()
                                layoutParams.columnSpec = GridLayout.spec(i % 2, 1f)
                                layoutParams.rowSpec = GridLayout.spec(i / 2, 1f)

                                activity!!.runOnUiThread {
                                    gridLayout.addView(
                                        deviceView, layoutParams
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        ShortToast(activity!!, e, false)
                    }
                    created = true
                }.start()
            }
        }
    }

    @SuppressLint("ViewConstructor")
    class DeviceView(context: Context, m: String, v: String, b: Bitmap, pid: String, site: String) :
        LinearLayout(context) {
        init {
            LayoutInflater.from(context).inflate(R.layout.view_device, this)
            model.text = m
            version.text = if (v.endsWith("1")) {
                v.plus("0")
            } else {
                v
            }
            imageView.setImageBitmap(b)
            root.setOnClickListener {
                startActivity(
                    context, Intent(context, SelectDownloadActivity::class.java).putExtra(
                        "model", m
                    ).putExtra("pid", pid).putExtra("site", site), null
                )
            }

        }
    }

    class SelectDownloadActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_selectdownload)
            toolbar_sd.title = intent.getStringExtra("model")
            setSupportActionBar(toolbar_sd)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            toolbar_sd.setNavigationOnClickListener { finish() }

            Thread {
                val site = intent.getStringExtra("site")
                val pid = intent.getStringExtra("pid")
                val getVersion1: String
                val getVersion1Diff: Int
                val getVersion2: String
                val getVersion2Diff: Int
                val getSize: String
                val getSizeDiff: Int
                val downloadBtn: String

                val url = if (!site.isEmpty()) {
                    getVersion1 = "n: "
                    getVersion1Diff = 3
                    getVersion2 = ") "
                    getVersion2Diff = 1
                    getSize = "e: "
                    getSizeDiff = 3
                    downloadBtn = "btn_5"
                    "http://$site.miui.com/download-$pid.html"
                } else {
                    getVersion1 = "本："
                    getVersion1Diff = 2
                    getVersion2 = "）"
                    getVersion2Diff = 1
                    getSize = "小："
                    getSizeDiff = 2
                    downloadBtn = "download_btn"
                    "http://miui.com/download-$pid.html"
                }

                val document = Jsoup.connect(url).get()
                /*
                                val file = File(externalCacheDir!!.absolutePath + File.separator + "a.txt")
                                if (file.exists()) {
                                    file.delete()
                                }
                                val fileWriter = FileWriter(file)
                                fileWriter.write(document.toString())
                                fileWriter.close()
                */
                val span = document.getElementsByClass("tab").select("span")
                if (span.size > 0) {
                    for (i in span) {
                        val name = document.getElementById(i.attr("id")).text()
                        val selectDownloadView = SelectDownloadView(this, name)

                        val id = document.getElementById("content_${i.attr("id")}")

                        for (j in id.getElementsByClass("rom_list_div")) {

                            val tmp = j.getElementsByClass("download_nv").select("div").text()

                            val vN = tmp.substring(0, tmp.indexOf(">"))

                            val support = j.getElementsByClass("supports").select("p")[0].text()
                            val v = support.substring(
                                support.indexOf(getVersion1) + getVersion1Diff,
                                support.indexOf(getVersion2) + getVersion2Diff
                            )
                            val size = support.substring(support.indexOf(getSize) + getSizeDiff)

                            val u =j.getElementsByClass(downloadBtn).select("a").attr("href")
                            Log.e("downloadurl", u)

                            val selectDownloadItem = SelectDownloadItem(this, vN, v, size, u)
                            selectDownloadView.addView(selectDownloadItem)
                        }
                        runOnUiThread { sd_root.addView(selectDownloadView) }
                    }
                } else {
                    val name = document.getElementsByClass("phone_hd")[0].select("span").text()
                    val selectDownloadView = SelectDownloadView(this, name)

                    val listDiv = document.getElementsByClass("rom_list_div")
                    for (i in listDiv) {
                        val tmp = i.getElementsByClass("download_nv").select("div").text()

                        val vN = tmp.substring(0, tmp.indexOf(">"))

                        val support = i.getElementsByClass("supports").select("p")[0].text()
                        val v = support.substring(
                            support.indexOf(getVersion1) + getVersion1Diff,
                            support.indexOf(getVersion2) + getVersion2Diff
                        )
                        val size = support.substring(support.indexOf(getSize) + getSizeDiff)

                        val u = i.getElementsByClass(downloadBtn).select("a").attr("href")
                        Log.e("downloadurl", u)
                        val selectDownloadItem = SelectDownloadItem(this, vN, v, size, u)
                        selectDownloadView.addView(selectDownloadItem)
                    }
                    runOnUiThread { sd_root.addView(selectDownloadView) }
                }

            }.start()
        }
    }

    @SuppressLint("ViewConstructor")
    class SelectDownloadView(context: Context, title: String) : LinearLayout(context) {
        init {
            LayoutInflater.from(context).inflate(R.layout.view_selectdownload, this)
            select_title.text = title
        }

        override fun addView(view: View) {
            select_root.addView(view)
        }
    }

    @SuppressLint("ViewConstructor")
    class SelectDownloadItem(
        context: Context, vName: String, v: String, s: String, u: String
    ) : LinearLayout(context) {
        init {
            LayoutInflater.from(context).inflate(R.layout.item_selectdownload, this)
            versionName.append(vName)
            versionRom.append(v)
            size.text = s
            root_linear.setOnClickListener {
                Thread {

                    val file = u.substring(u.indexOf("/m") + 1)
                    context.startActivity(Intent(context, DownloadActivity::class.java)
                        .putExtra("url", u)
                        .putExtra("filePath", Environment.getExternalStorageDirectory().absolutePath)
                        .putExtra("fileName", file))

/*
                    val id = 0x3
                    val builder = NotificationCompat.Builder(context, "Notify")
                    builder.setContentTitle(context.getString(R.string.downloading))
                    builder.setSubText(v)
                    builder.setSmallIcon(R.mipmap.ic_launcher)
                    builder.setOngoing(true)
                    builder.priority = NotificationCompat.PRIORITY_MAX

                    val notificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(id, builder.build())

                    val file = u.substring(u.indexOf("/m") + 1)

                    val downloadFile = DownloadFile(
                        u, Environment.getExternalStorageDirectory().absolutePath, file
                    )
                    downloadFile.download(object : DownloadFile.OnDownLoadListener {
                        override fun onDownloadFailed(e: Exception) {
                            ShortToast(
                                BaseManager.getInstance().mainActivity,
                                BaseManager.getInstance().mainActivity.getString(R.string.download_failed) + e,
                                false
                            )
                            notificationManager.cancelAll()
                        }

                        override fun onDownloadSuccess(file: File) {
                            ShortToast(
                                BaseManager.getInstance().mainActivity,
                                BaseManager.getInstance().mainActivity.getString(R.string.download_success) + file.toString(),
                                false
                            )
                            notificationManager.cancelAll()
                        }

                        override fun onDownloading(progress: Int) {
                            builder.setContentText("下载 $progress %")
                            builder.setProgress(100, progress, false)
                            notificationManager.notify(id, builder.build())
                        }
                    })*/

                }.start()

            }
        }
    }
}