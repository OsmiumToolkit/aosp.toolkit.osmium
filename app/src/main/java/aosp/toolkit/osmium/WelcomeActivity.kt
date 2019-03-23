package aosp.toolkit.osmium

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
 */


import android.content.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup

import kotlinx.android.synthetic.main.activity_welcome.*


import aosp.toolkit.osmium.base.BaseOperation.Companion.checkRoot

import kotlinx.android.synthetic.main.fragment_welcome_4.*

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        aosp.toolkit.osmium.base.BaseManager.getInstance().setWelcomeActivity(this)

        val option = (SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or SYSTEM_UI_FLAG_LAYOUT_STABLE)

        window.decorView.systemUiVisibility = option

        val fragments = listOf(
            Welcome1Fragment(), Welcome2Fragment(),
            Welcome3Fragment(), Welcome4Fragment()
        )

        val tabList = listOf(
            getString(R.string.welcome_tab_1), getString(R.string.welcome_tab_2),
            getString(R.string.welcome_tab_3), getString(R.string.welcome_tab_4)
        )

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, fragments, tabList)
        tabLayout.setupWithViewPager(viewPager)

    }

    class ViewPagerAdapter(
        fragmentManager: FragmentManager?,
        private var fragmentList: List<Fragment>,
        private var tabList: List<String>
    ) : FragmentPagerAdapter(fragmentManager) {
        override fun getItem(p0: Int): Fragment {
            return fragmentList[p0]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabList[position]
        }
    }

    class Welcome1Fragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_welcome_1, container, false)
        }
    }

    class Welcome2Fragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_welcome_2, container, false)
        }
    }

    class Welcome3Fragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_welcome_3, container, false)
        }
    }

    class Welcome4Fragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_welcome_4, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            start_su.setOnClickListener {
                Thread {
                    if (checkRoot()) {
                        activity!!.getSharedPreferences("splash", Context.MODE_PRIVATE).edit()
                            .putBoolean("welcome", true).apply()
                        startActivity(Intent(activity, MainActivity::class.java))
                    }
                }.start()
            }
        }
    }
}