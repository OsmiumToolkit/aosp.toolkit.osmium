package aosp.toolkit.perseus.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import aosp.toolkit.perseus.R
import aosp.toolkit.perseus.ScriptActivity

/*
 * OsToolkit - Kotlin
 *
 * Date : 9 Mar 2019
 *
 * By   : 1552980358
 *
 */
 
class CPUFreqOptView(activity: Activity, type: String, index: String, board: String) :
    LinearLayout(activity) {
    init {
        LayoutInflater.from(activity).inflate(R.layout.view_cpufreqopt, this)

        val textView = findViewById<TextView>(R.id.title)
        textView.text = board
        val relativeLayout = findViewById<RelativeLayout>(R.id.root)
        relativeLayout.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(board).setMessage(String.format(activity.getString(R.string.apply_confirm), board))
                .setPositiveButton(R.string.cont) { _, _ ->
                    activity.startActivity(
                        Intent(activity, ScriptActivity::class.java)
                            .putExtra("path", "$type/$index/$board")
                    )
                }
                .setNegativeButton(R.string.cancel) { _, _ ->

                }.show()
        }
    }
}