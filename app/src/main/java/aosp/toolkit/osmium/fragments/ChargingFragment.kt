package aosp.toolkit.osmium.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import aosp.toolkit.osmium.R

import aosp.toolkit.osmium.base.BaseOperation.Companion.checkFilePresent

import aosp.toolkit.osmium.base.BaseIndex.*
import kotlinx.android.synthetic.main.fragment_charging.*

/*
 * OsToolkit - Kotlin
 *
 * Date : 1/1/2019
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


class ChargingFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aosp.toolkit.osmium.base.BaseManager.getInstance().setChargingFragment(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_charging, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setWarning()

        charging_allow.init(this, Charging_Allow, type_shell, index_charging, CHARGE_ALLOW)
        charging_qc3.init(this, Charging_QC3, type_shell, index_charging, CHARGE_QC3)
        charging_usbqc.init(this, Charging_USBQC, type_shell, index_charging, CHARGE_USBQC)
    }

    private fun setWarning() {
        if (checkFilePresent(Charging_Allow) and checkFilePresent(Charging_QC3) and checkFilePresent(Charging_USBQC)) {
            charging_cardview.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        aosp.toolkit.osmium.base.BaseManager.getInstance().restartChargingFragment()
    }
}