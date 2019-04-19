package aosp.toolkit.perseus.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup

import aosp.toolkit.perseus.R
import aosp.toolkit.perseus.base.BaseManager
import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast

import kotlinx.android.synthetic.main.fragment_main.*

/*
 * OsToolkit - Kotlin
 *
 * Date : 31/12/2018 [Modify : 9/1/2019]
 *
 * By   : 1552980358
 *
 */

class MainFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        root.setOnClickListener {
            Thread {
                try {
                    Runtime.getRuntime().exec("su").inputStream.bufferedReader(Charsets.UTF_8).readLines()
                    activity!!.runOnUiThread {
                        result.setTextColor(ContextCompat.getColor(activity!!, R.color.green))
                        result.text = getString(R.string.check_root_pass)
                    }
                    ShortToast(activity!!, R.string.check_root_pass, false)
                } catch (e: Exception) {
                    activity!!.runOnUiThread {
                        result.setTextColor(ContextCompat.getColor(activity!!, R.color.red))
                        result.text = getString(R.string.check_root_fail)
                    }
                    ShortToast(activity!!, R.string.check_root_fail, false)
                }
            }.start()
        }
        left.setOnClickListener {
            BaseManager.getInstance().mainActivity.openDrawerLayoutLeft()
        }
        right.setOnClickListener {
            BaseManager.getInstance().mainActivity.openDrawerLayoutRight()
        }
    }
}