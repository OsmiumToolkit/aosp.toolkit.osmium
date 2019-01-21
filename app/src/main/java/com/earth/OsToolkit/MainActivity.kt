package com.earth.OsToolkit

/*
 * OsToolkit - Kotlin
 *
 * Date : 31/12/2018
 *
 * By   : 1552980358
 *
 */

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.LinearLayout

import com.earth.OsToolkit.base.BaseManager
import com.earth.OsToolkit.fragments.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mainFragment : Fragment = MainFragment()
    private var aboutFragment : Fragment = AboutFragment()
    private var deviceInfoFragment : Fragment = DeviceInfoFragment()
    private var chargingFragment : Fragment = ChargingFragment()
    private var coreFragment : Fragment = CoreFragment()
    private var applyYCFragment : Fragment = ApplyYCFragment()
    private var romIOFragment : Fragment = RomIOFragment()
    private var extendsFragment : Fragment = ExtendsFragment()

    private var currentFragment : Fragment = mainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BaseManager.getInstance().finishActivities()

        BaseManager.instance.setMainActivity(this)

        initUI()
        addFragment()
    }

    private fun initUI() {
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)

        drawer_layout.addDrawerListener(toggle)
        val view : View = nav_view.getHeaderView(0)
        toggle.syncState()

        val navAbout : LinearLayout = view.findViewById(R.id.nav_about)
        navAbout.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            supportFragmentManager.beginTransaction().hide(currentFragment).show(aboutFragment).commit()
            currentFragment = aboutFragment
        }
        val navDeviceInfo : LinearLayout = view.findViewById(R.id.nav_deviceinfo)
        navDeviceInfo.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            supportFragmentManager.beginTransaction().hide(currentFragment).show(deviceInfoFragment).commit()
            currentFragment = deviceInfoFragment
        }

        if (getSharedPreferences("ui", Context.MODE_PRIVATE).getBoolean("navBar", true)) {
            // ContextCompact通用包
            window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

            // 5.0+可过编译 6.0+弃用 无法更改颜色
            // window.navigationBarColor = resources.getColor(R.color.colorPrimaryDark)
            // 6.0+API 无法适配 5.0 / 5.1
            // window.navigationBarColor = resources.getColor(R.color.colorPrimary, null)
        }

        nav_view.setNavigationItemSelectedListener(this)

        val handler = Handler()
        val timer = Timer()
        timer.schedule(object : TimerTask(){
            override fun run() {
                handler.post(runnable)
            }
        }, 3000)
    }

    val runnable = Runnable {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    private fun addFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout_main, aboutFragment).hide(aboutFragment)
            .add(R.id.frameLayout_main, deviceInfoFragment).hide(deviceInfoFragment)
            .add(R.id.frameLayout_main, chargingFragment).hide(chargingFragment)
            .add(R.id.frameLayout_main, coreFragment).hide(coreFragment)
            .add(R.id.frameLayout_main, applyYCFragment).hide(applyYCFragment)
            .add(R.id.frameLayout_main, romIOFragment).hide(romIOFragment)
            .add(R.id.frameLayout_main, extendsFragment).hide(extendsFragment)
            .add(R.id.frameLayout_main, mainFragment)
            .commit()

        currentFragment = mainFragment

    }

    fun onRecreateChargingFragment(chargingFragment: ChargingFragment) {
        this.chargingFragment = chargingFragment
        this.currentFragment = this.chargingFragment
    }

    /*
    fun onRecreateRomIOFragment(romIOFragment : RomIOFragment) {
        this.romIOFragment = romIOFragment
        this.currentFragment = this.romIOFragment
    }
    */

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
        var frag : Fragment = mainFragment
        var title = R.string.app_name
        when (item.itemId) {
            R.id.nav_charging -> {
                title = R.string.nav_charging
                frag = chargingFragment
            }
            R.id.nav_cores -> {
                title = R.string.nav_processor
                frag = coreFragment
            }
            R.id.nav_applyyc -> {
                title = R.string.nav_yc
                frag = applyYCFragment
            }
            R.id.nav_romio -> {
                title = R.string.nav_romio
                frag = romIOFragment
            }
            R.id.nav_others -> {
                title = R.id.nav_others
                frag = extendsFragment
            }
        }

        toolbar.setTitle(title)
        supportFragmentManager.beginTransaction().hide(currentFragment).show(frag).commit()
        currentFragment = frag
        Log.i("currentFragment", currentFragment.toString())
        return true
    }
}
