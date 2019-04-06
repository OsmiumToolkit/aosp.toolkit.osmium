package aosp.toolkit.perseus

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import aosp.toolkit.perseus.base.DownloadFile

import aosp.toolkit.perseus.base.ViewPagerAdapter

import kotlinx.android.synthetic.main.activity_downloadmiui.*
import kotlinx.android.synthetic.main.activity_selectdownload.*
import kotlinx.android.synthetic.main.fragment_miuichina.*
import kotlinx.android.synthetic.main.item_selectdownload.view.*
import kotlinx.android.synthetic.main.view_device.view.*
import kotlinx.android.synthetic.main.view_selectdownload.view.*

import org.json.JSONObject
import org.jsoup.Jsoup

import java.io.File
import java.io.FileWriter
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
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_miuichina, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            Thread {
                val document =
                    Jsoup.connect("http://www.miui.com/download.html")
                        .get()

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

                        activity!!.runOnUiThread { gridLayout.addView(deviceView, layoutParams) }

                    }
                }
            }.start()
        }
    }

    class MIUIGlobalFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_miuichina, container, false)
        }
    }

    @SuppressLint("ViewConstructor")
    class DeviceView(context: Context, m: String, v: String, b: Bitmap, pid: String, site: String) :
        LinearLayout(context) {
        init {
            LayoutInflater.from(context).inflate(R.layout.view_device, this)
            model.text = m
            version.text = v
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
                val url = if (!site.isEmpty()) {
                    "http://$site.miui.com/download-$pid.html"
                } else {
                    "http://miui.com/download-$pid.html"
                }

                val document = Jsoup.connect(url).get()

                val file = File(externalCacheDir!!.absolutePath + File.separator + "a.txt")
                if (file.exists()) {
                    file.delete()
                }
                val fileWriter = FileWriter(file)
                fileWriter.write(document.toString())
                fileWriter.close()

                val span = document.getElementsByClass("tab").select("span")
                if (span.size > 1) {
                    for (i in span) {
                        val name = document.getElementById(i.attr("id")).text()
                        val selectDownloadView = SelectDownloadView(this, name)

                        val id = document.getElementById("content_${i.attr("id")}")

                        for (j in id.getElementsByClass("rom_list_div")) {

                            val vN = j.getElementsByClass("download_nv").select("div").text()

                            val support = j.getElementsByClass("supports").select("p")[0].text()
                            val v = support.substring(
                                support.indexOf("本：") + 2, support.indexOf("）") + 1
                            )
                            val size = support.substring(support.indexOf("小：") + 2)

                            val u = id.getElementsByClass("download_btn").select("a").attr("href")

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

                        val vN = i.getElementsByClass("download_nv").select("div").text()

                        val support = i.getElementsByClass("supports").select("p")[0].text()
                        val v =
                            support.substring(support.indexOf("本：") + 2, support.indexOf("）") + 1)
                        val size = support.substring(support.indexOf("小：") + 2)

                        val u = i.getElementsByClass("download_btn").select("a").attr("href")
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
            versionName.text = vName
            versionRom.text = v
            size.append(s)
            root_linear.setOnClickListener {
                Thread {
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
                            notificationManager.cancelAll()
                        }

                        override fun onDownloadSuccess(file: File) {

                        }

                        override fun onDownloading(progress: Int) {
                            builder.setContentText("下载 $progress %")
                            builder.setProgress(100, progress, false)
                            notificationManager.notify(id, builder.build())
                        }
                    })
                }.start()
            }
        }
    }
}