package aosp.toolkit.osmium.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import aosp.toolkit.osmium.DisableAppActivity

import aosp.toolkit.osmium.R
import aosp.toolkit.osmium.ZXingActivity
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disabletower.setOnClickListener {
            startActivity(Intent(activity, DisableAppActivity::class.java))
        }
        click.setOnClickListener {
            startActivity(Intent(activity, ZXingActivity::class.java))
        }
    }
}