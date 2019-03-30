package aosp.toolkit.perseus

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

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.*

import aosp.toolkit.perseus.fragments.*

import com.topjohnwu.superuser.Shell

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    /*
    val onNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener{item ->
        drawer_layout.closeDrawer(GravityCompat.START)

        val id = item.itemId

        when (id) {
            R.id.nav_monitor -> startActivity(Intent(this, UsageActivity::class.java))
            R.id.nav_tower -> startActivity(Intent(this, DisableAppActivity::class.java))
            R.id.nav_zxing -> startActivity(Intent(this, ZXingActivity::class.java))
            else -> exchangeFragment(id)
        }
        return@OnNavigationItemSelectedListener true
    }
    */

    // 定义fragments
    private var mainFragment: MainFragment = MainFragment()
    private var aboutFragment: Fragment? = null
    private var deviceInfoFragment: Fragment? = null
    private var chargingFragment: Fragment? = null
    private var coreFragment: Fragment? = null
    private var applyYCFragment: Fragment? = null
    private var applyPixelCatFragment: Fragment? = null
    private var romIOFragment: Fragment? = null
    private var otherFragment: Fragment? = null

    // 显示的fragment
    private var currentFragment: Fragment = mainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 移除上一个activity
        aosp.toolkit.perseus.base.BaseManager.getInstance().finishActivities()
        aosp.toolkit.perseus.base.BaseManager.instance.setMainActivity(this, mainFragment)

        setContentView(R.layout.activity_main)

        initUI()
        addFragment()
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
        toggle.syncState()

        if (getSharedPreferences("ui", Context.MODE_PRIVATE).getBoolean("navBar", true)) {
            // ContextCompact通用包
            window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

            // 5.0+可过编译 6.0+弃用 无法更改颜色
            // window.navigationBarColor = resources.getColor(R.color.colorPrimaryDark)
            // 6.0加入的API 无法适配 5.0 / 5.1
            // window.navigationBarColor = resources.getColor(R.color.colorPrimary, null)
        }

        /*
        val sideNavigationView = SideNavigationView(
            this,
            R.layout.nav_header_main,
            null,
            R.menu.activity_main_drawer,
            this,
            R.color.colorPrimaryDark
        )
        val layoutParams =
            DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.MATCH_PARENT)
        layoutParams.gravity = Gravity.START
        drawer_layout.addView(sideNavigationView, layoutParams)
        */
    }

    fun exceptionBeaker() {
        this.currentFragment = mainFragment
    }

    private fun addFragment() {
        supportFragmentManager.beginTransaction().add(R.id.frameLayout_main, mainFragment).commit()
        currentFragment = mainFragment
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            Thread {
                val fragmentManager = supportFragmentManager.beginTransaction()
                for (i in supportFragmentManager.fragments) {
                    fragmentManager.remove(i)
                }
                fragmentManager.commit()
            }.start()

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
            R.id.reb -> Shell.su("reboot").exec()
            R.id.soft -> Shell.su("killall zygote").exec()
            R.id.bl -> Shell.su("reboot bootloader").exec()
            R.id.rec -> Shell.su("reboot recovery").exec()
            R.id.re9008 -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.warn_9008_title)
                    .setMessage(R.string.warn_9008_msg)
                    .setPositiveButton(R.string.cont) { _, _ ->
                        Shell.su("reboot edl").exec()
                    }.setNegativeButton(R.string.cancel) { _, _ -> }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        when (id) {
            R.id.nav_monitor -> startActivity(Intent(this, UsageActivity::class.java))
            R.id.nav_tower -> startActivity(Intent(this, DisableAppActivity::class.java))
            R.id.nav_zxing -> startActivity(Intent(this, ZXingActivity::class.java))
            else -> exchangeFragment(id)
        }
        return true
    }

    private fun exchangeFragment(id: Int) {
        drawer_layout.closeDrawer(GravityCompat.START)
        Thread {
            val frag: Fragment
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val title: Int
            when (id) {
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
                R.id.nav_applypc -> {
                    title = R.string.nav_pc
                    if (applyPixelCatFragment != null) {
                        fragmentTransaction.show(applyPixelCatFragment!!)
                    } else {
                        applyPixelCatFragment = ApplyPixelCatFragment()
                        fragmentTransaction.add(R.id.frameLayout_main, applyPixelCatFragment!!)
                    }
                    frag = applyPixelCatFragment!!
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
                    if (otherFragment != null) {
                        fragmentTransaction.show(otherFragment!!)
                    } else {
                        otherFragment = OtherFragment()
                        fragmentTransaction.add(R.id.frameLayout_main, otherFragment!!)
                    }
                    frag = otherFragment!!
                }
                R.id.nav_about -> {
                    title = R.string.nav_about
                    if (aboutFragment != null) {
                        fragmentTransaction.show(aboutFragment!!)
                    } else {
                        aboutFragment = AboutFragment()
                        fragmentTransaction.add(R.id.frameLayout_main, aboutFragment!!)
                    }
                    frag = aboutFragment!!
                }
                R.id.nav_deviceinfo -> {
                    title = R.string.nav_deviceinfo
                    if (deviceInfoFragment != null) {
                        fragmentTransaction.show(aboutFragment!!)
                    } else {
                        deviceInfoFragment = DeviceInfoFragment()
                        fragmentTransaction.add(R.id.frameLayout_main, deviceInfoFragment!!)
                    }
                    frag = deviceInfoFragment!!
                }
                else -> {
                    frag = mainFragment
                    fragmentTransaction.show(mainFragment)
                    title = R.string.nav_main
                }
            }

            runOnUiThread { toolbar.setTitle(title) }
            if (frag != currentFragment) {
                fragmentTransaction.hide(currentFragment).commit()
            }
            currentFragment = frag
        }.start()

    }

}
