package aosp.toolkit.perseus.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


/*
 * @File:   ViewPagerAdapter
 * @Author: 1552980358
 * @Time:   6:38 PM
 * @Date:   5 Apr 2019
 * 
 */

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