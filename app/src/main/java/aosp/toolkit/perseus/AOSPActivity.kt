package aosp.toolkit.perseus

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast
import aosp.toolkit.perseus.base.ViewPagerAdapter

import kotlinx.android.synthetic.main.activity_aosp.*
import kotlinx.android.synthetic.main.activity_aospselectrom.*
import kotlinx.android.synthetic.main.fragment_lineageos.*
import kotlinx.android.synthetic.main.view_losbrand.view.*
import kotlinx.android.synthetic.main.item_losdevice.view.*
import kotlinx.android.synthetic.main.view_devicedownload.view.*

import org.jsoup.Jsoup

/*
 * @File:   AOSPActivity
 * @Author: 1552980358
 * @Time:   4:56 PM
 * @Date:   24 May 2019
 * 
 */

class AOSPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aosp)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val tabList = listOf("LineageOS")
        val fragmentList = listOf(LineageOSFragment())
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, fragmentList, tabList)
        tabLayout.setupWithViewPager(viewPager)
    }

    class AOSPSelectRomActivity : AppCompatActivity() {

        companion object {
            val head = mapOf("los" to "https://download.lineageos.org")
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_aospselectrom)

            toolbarAOSPSelection.title =
                "${intent.getStringExtra("brand")} ${intent.getStringExtra("model")} (${intent.getStringExtra(
                    "device"
                )})"

            setSupportActionBar(toolbarAOSPSelection)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            toolbarAOSPSelection.setNavigationOnClickListener { finish() }

            Thread {
                val href = intent.getStringExtra("href")
                val rom = intent.getStringExtra("rom")

                val document =
                    Jsoup.connect(head[rom] + href).get().getElementsByTag("tbody")[0].getElementsByTag(
                        "tr"
                    )

                for (i in document) {
                    val td = i.getElementsByTag("td")

                    val buildType = td[0].select("td").text()   // Build Type
                    val version = td[1].select("td").text()     // Version
                    val a = td[2].select("td").select("a")[0]
                    val fileName = a.text()                             // File Name
                    val url = a.attr("href")                  // URL
                    val size = td[3].select("td").text()        // Size
                    val date = td[4].select("td").text()        // Date

                    val deviceDownloadView =
                        DeviceDownloadView(this, url, fileName, date, buildType, size, version)
                    runOnUiThread { aospSelectROMContainer.addView(deviceDownloadView) }

                }

            }.start()

        }

        @SuppressLint("ViewConstructor", "SetTextI18n")
        class DeviceDownloadView(
            context: Context,
            url: String,
            fileName: String,
            date: String,
            type: String,
            size: String,
            version: String
        ) : LinearLayout(context) {
            init {
                LayoutInflater.from(context).inflate(R.layout.view_devicedownload, this)
                romFile.text = fileName
                romSize.text = size
                romDate.text = date
                romVersion.text = "$version-$type"
                romDownload.setOnClickListener {
                    Thread {
                        val path = context.externalCacheDir!!.absolutePath
                        context.startActivity(
                            Intent(context, DownloadActivity::class.java).putExtra(
                                "fileName", fileName
                            ).putExtra("url", url).putExtra("filePath", path)
                        )
                    }.start()
                }
                romLinkCopy.setOnClickListener {
                    Thread {
                        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip =
                            ClipData.newPlainText("text", url)

                    }.start()
                }
            }
        }
    }

    class LineageOSFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_lineageos, container, false)
        }

        private var created = false
        override fun setUserVisibleHint(isVisibleToUser: Boolean) {
            super.setUserVisibleHint(isVisibleToUser)
            if (isVisibleToUser && !created) {
                Thread {
                    try {
                        val document = Jsoup.connect("https://download.lineageos.org/").get()

                        for (i in document.getElementsByClass("no-padding")) {
                            // Brand
                            val brand =
                                i.getElementsByClass("collapsible-header waves-effect waves-teal bold")
                                    .select("a").text()
                            val losBrandView = LOSBrandView(
                                context!!, brand
                            )

                            for (j in i.getElementsByClass("device-link")) {
                                val href = j.select("a").attr("href")     // Href
                                val info = j.getElementsByTag("div")
                                val model = info[0].select("div").text()            // Model
                                val device = info[1].select("div").text()           // Device

                                losBrandView.addView(
                                    LOSDeviceItem(
                                        context!!,
                                        brand,
                                        model,
                                        device,
                                        href
                                    )
                                )
                            }

                            activity!!.runOnUiThread { losRoot.addView(losBrandView) }
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
    class LOSBrandView(context: Context, brand: String) : LinearLayout(context) {
        init {
            LayoutInflater.from(context).inflate(R.layout.view_losbrand, this)
            losViewTitle.text = brand
            losViewRoot.setOnClickListener {
                losViewContainer.visibility = if (losViewContainer.visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }

        override fun addView(view: View) {
            losViewContainer.addView(view)
        }
    }

    @SuppressLint("ViewConstructor")
    class LOSDeviceItem(
        context: Context,
        brand: String,
        model: String,
        device: String,
        href: String
    ) : LinearLayout(context) {
        init {
            LayoutInflater.from(context).inflate(R.layout.item_losdevice, this)
            losModel.text = model
            losdevice.text = device
            losDeviceRoot.setOnClickListener {
                val intent = Intent().setClass(context, AOSPSelectRomActivity::class.java)
                    .putExtra("rom", "los").putExtra("href", href).putExtra("device", device)
                    .putExtra("model", model).putExtra("brand", brand)
                context.startActivity(intent)
            }
        }
    }
}