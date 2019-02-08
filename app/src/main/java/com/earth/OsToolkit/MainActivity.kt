package com.earth.OsToolkit

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
 * 23/1/2019
 *
 */

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.LinearLayout

import com.earth.OsToolkit.base.BaseManager
import com.earth.OsToolkit.fragments.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // 定义fragments
    private var mainFragment: Fragment = MainFragment()
    private var aboutFragment: Fragment? = null
    private var deviceInfoFragment: Fragment? = null
    private var chargingFragment: Fragment? = null
    private var coreFragment: Fragment? = null
    private var applyYCFragment: Fragment? = null
    private var romIOFragment: Fragment? = null
    private var extendsFragment: Fragment? = null

    // 显示的fragment
    private var currentFragment: Fragment = mainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 移除上一个activity
        BaseManager.getInstance().finishActivities()
        BaseManager.instance.setMainActivity(this)

        val dialog = Dialog(this)
        dialog.setContentView(LayoutInflater.from(this).inflate(R.layout.dialog_loading, null))
        dialog.show()

        initUI()
        addFragment()

        Timer().schedule(object : TimerTask() {
            override fun run() {
                dialog.cancel()
            }
        }, 1000)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    drawer_layout.openDrawer(GravityCompat.START)
                }
            }
        }, 1000)
    }

    private fun initUI() {
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        val view: View = nav_view.getHeaderView(0)
        toggle.syncState()

        val navAbout: LinearLayout = view.findViewById(R.id.nav_about)
        // 监听 listeners
        navAbout.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (aboutFragment == null) {
                aboutFragment = AboutFragment()
                fragmentTransaction.add(R.id.frameLayout_main, aboutFragment!!)
            } else {
                fragmentTransaction.show(aboutFragment!!)
            }
            fragmentTransaction.hide(currentFragment).commit()
            currentFragment = aboutFragment!!
        }
        val navDeviceInfo: LinearLayout = view.findViewById(R.id.nav_deviceinfo)
        navDeviceInfo.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (deviceInfoFragment == null) {
                deviceInfoFragment = DeviceInfoFragment()
                fragmentTransaction.add(R.id.frameLayout_main, deviceInfoFragment!!)
            } else {
                fragmentTransaction.show(deviceInfoFragment!!)
            }
            fragmentTransaction.hide(currentFragment).commit()
            currentFragment = deviceInfoFragment!!
        }

        if (getSharedPreferences("ui", Context.MODE_PRIVATE).getBoolean("navBar", true)) {
            // ContextCompact通用包
            window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

            // 5.0+可过编译 6.0+弃用 无法更改颜色
            // window.navigationBarColor = resources.getColor(R.color.colorPrimaryDark)
            // 6.0加入的API 无法适配 5.0 / 5.1
            // window.navigationBarColor = resources.getColor(R.color.colorPrimary, null)
        }

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun addFragment() {
        supportFragmentManager.beginTransaction().add(R.id.frameLayout_main, mainFragment).commit()
        currentFragment = mainFragment
    }

    fun onRecreateChargingFragment(chargingFragment: ChargingFragment) {
        this.chargingFragment = chargingFragment
        this.currentFragment = this.chargingFragment!!
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        drawer_layout.closeDrawer(GravityCompat.START)
        val frag: Fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val title: Int
        when (item.itemId) {
            R.id.nav_charging -> {
                title = R.string.nav_charging
                if (chargingFragment != null) {
                    fragmentTransaction.show(chargingFragment!!)
                } else {
                    chargingFragment = ChargingFragment()
                    fragmentTransaction.add(R.id.frameLayout_main, chargingFragment!!)
                }
                frag = chargingFragment!!
            }
            R.id.nav_cores -> {
                title = R.string.nav_processor
                if (coreFragment != null) {
                    fragmentTransaction.show(coreFragment!!)
                } else {
                    coreFragment = CoreFragment()
                    fragmentTransaction.add(R.id.frameLayout_main, coreFragment!!)
                }
                frag = coreFragment!!
            }
            R.id.nav_applyyc -> {
                title = R.string.nav_yc
                if (applyYCFragment != null) {
                    fragmentTransaction.show(applyYCFragment!!)
                } else {
                    applyYCFragment = ApplyYCFragment()
                    fragmentTransaction.add(R.id.frameLayout_main, applyYCFragment!!)
                }
                frag = applyYCFragment!!
            }
            R.id.nav_romio -> {
                title = R.string.nav_romio
                if (romIOFragment != null) {
                    fragmentTransaction.show(romIOFragment!!)
                } else {
                    romIOFragment = RomIOFragment()
                    fragmentTransaction.add(R.id.frameLayout_main, romIOFragment!!)
                }
                frag = romIOFragment!!
            }
            R.id.nav_others -> {
                title = R.string.nav_other
                if (extendsFragment != null) {
                    fragmentTransaction.show(extendsFragment!!)
                } else {
                    extendsFragment = ExtendsFragment()
                    fragmentTransaction.add(R.id.frameLayout_main, extendsFragment!!)
                }
                frag = extendsFragment!!
            }
            else -> {
                frag = mainFragment
                fragmentTransaction.show(mainFragment)
                title = R.string.nav_main
            }
        }

        toolbar.setTitle(title)
        if (frag != currentFragment) {
            fragmentTransaction.hide(currentFragment).commit()
        }
        currentFragment = frag
        //Log.i("currentFragment", currentFragment.toString())
        return true
    }
}
