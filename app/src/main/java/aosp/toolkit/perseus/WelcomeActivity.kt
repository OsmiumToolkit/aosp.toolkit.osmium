package aosp.toolkit.perseus

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup

import aosp.toolkit.perseus.base.BaseIndex.versionIndex
import aosp.toolkit.perseus.base.BaseManager
import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast
import aosp.toolkit.perseus.base.ViewPagerAdapter
import aosp.toolkit.perseus.services.ImportOfflinePackageService

import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.fragment_importofflinepackage.*
import kotlinx.android.synthetic.main.fragment_license.*
import kotlinx.android.synthetic.main.fragment_ready.*
import kotlinx.android.synthetic.main.fragment_welcome.*

import java.lang.Exception

/*
 * OsToolkit - Kotlin
 *
 * Date : 31/12/2018
 *
 * By   : 1552980358
 *
 */

/*
 * Modify
 *
 * 9/1/2019
 *
 * Re-write
 * 25 Mar 2019
 *
 */
class WelcomeActivity : AppCompatActivity() {
    private lateinit var t: Thread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        BaseManager.getInstance().welcomeActivity = this

        val option =
            (SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.decorView.systemUiVisibility = option

        val tabList = listOf(
            getString(R.string.welcome_tab_welcome),
            getString(R.string.welcome_tab_license),
            getString(R.string.welcome_tab_offline),
            getString(R.string.welcome_tab_ready)
        )
        val fragmentList = listOf(
            WelcomeFragment(), LicenseFragment(), ImportOfflinePackageFragment(), ReadyFragment()
        )

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, fragmentList, tabList)
        tabLayout.setupWithViewPager(viewPager)

        t = Thread {
            val colors = listOf(
                ContextCompat.getColor(this, R.color.red),
                ContextCompat.getColor(this, R.color.orange),
                ContextCompat.getColor(this, R.color.yellow),
                ContextCompat.getColor(this, R.color.green),
                ContextCompat.getColor(this, R.color.cyan),
                ContextCompat.getColor(this, R.color.blue),
                ContextCompat.getColor(this, R.color.purple)
            )
            var i = 0
            while (true) {
                if (i != 6) {
                    runOnUiThread { root.setBackgroundColor(colors[i]) }
                    i++
                } else {
                    runOnUiThread { root.setBackgroundColor(colors[0]) }
                    i = 1
                }
                try {
                    Thread.sleep(500)
                } catch (e: Exception) {
                    //
                }
            }
        }
        t.start()
    }

    private fun checkPermission0() {
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startService(
                Intent(this, ImportOfflinePackageService::class.java).putExtra(
                    "download", true
                )
            )
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 0
            )
        }
    }

    private fun checkPermission1() {
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startActivityForResult(
                Intent(Intent.ACTION_GET_CONTENT).setType("*/*").addCategory(Intent.CATEGORY_OPENABLE),
                0
            )
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 0
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> checkPermission0()
            1 -> checkPermission1()
            else -> return
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val url = data!!.data

            val cursor = contentResolver.query(
                url!!, arrayOf(MediaStore.Images.Media.DATA), null, null, null
            )
            cursor!!.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))

            startService(
                Intent(this, ImportOfflinePackageService::class.java).putExtra(
                    "download", false
                ).putExtra(
                    "zipLocation",
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                )
            )
            cursor.close()
        } else {
            // 取消操作
            ShortToast(this, R.string.cancelled)
        }
    }

    override fun finish() {
        try {
            t.interrupt()
        } catch (e: Exception) {
            //
        }
        super.finish()
    }

    class WelcomeFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_welcome, container, false)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            val array = resources.getStringArray(R.array.version)
            version.text = array[versionIndex]
            version.paint.flags = Paint.UNDERLINE_TEXT_FLAG
            author.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        }
    }

    class LicenseFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_license, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            webView.loadUrl("https://raw.githubusercontent.com/1552980358/aosp.toolkit.perseus/master/LICENSE")
        }
    }

    class ImportOfflinePackageFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_importofflinepackage, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rb_2 -> {
                        BaseManager.getInstance().welcomeActivity.checkPermission0()
                    }
                    R.id.rb_3 -> {
                        BaseManager.getInstance().welcomeActivity.checkPermission1()
                    }
                    R.id.rb_4 -> {
                        AlertDialog.Builder(context!!).setTitle(R.string.welcome_offline_5)
                            .setPositiveButton(R.string.ok) { _, _ -> }
                            .setView(R.layout.dialog_rb_4).setCancelable(false).show()

                    }
                }
            }
        }
    }

    class ReadyFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_ready, container, false)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            root_S.setOnClickListener {
                context!!.getSharedPreferences("launch", Context.MODE_PRIVATE).edit()
                    .putBoolean("welcome", true).apply()
                startActivity(Intent(context!!, MainActivity::class.java))
            }
        }
    }
}