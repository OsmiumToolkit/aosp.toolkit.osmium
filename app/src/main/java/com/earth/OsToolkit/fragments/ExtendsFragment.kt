package com.earth.OsToolkit.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.earth.OsToolkit.R
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.checkFilePresent
import com.earth.OsToolkit.base.BaseKotlinOperation.Companion.readFile
import kotlinx.android.synthetic.main.fragment_extends.*
import java.lang.Exception

/*
 * OsToolkit - Kotlin
 *
 * Date : 21 Jan 2019
 *
 * By   : 1552980358
 *
 */
 
class ExtendsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_extends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMac()
    }

    private fun setMac() {
        if (checkFilePresent("/sys/class/net/wlan0/address")) {
            editText.setText(readFile("/sys/class/net/wlan0/address"))
            editText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    editText.removeTextChangedListener(this)

                    if (before == 0) {
                        when (s.toString().length) {
                            2, 5, 8, 11, 14 -> {
                                editText.setText(s.toString().plus(":").toLowerCase())
                            }
                            17 -> {
                                editText.setText(s.toString().toUpperCase())
                            }
                        }
                    }
                    editText.addTextChangedListener(this)
                }
                override fun afterTextChanged(s: Editable?) {
                    try {
                        editText.setSelection(s.toString().length.plus(1))
                    } catch (e: Exception) {
                        try {
                            editText.setSelection(s.toString().length)
                        } catch (e : Exception) {
                            //
                        }
                    }

                }
            })
            val pattern = "(([a-z]|\\d){2}:){5}([a-z]|\\d){2}".toRegex()
            done.setOnClickListener {
                val mac = editText.text.toString()
                if (mac.length == 17 && pattern.matches(mac)) {
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "chmod", "644", "/sys/class/net/wlan0/address"))
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "svc", "wifi", "disable"))
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "ifconfig", "wlan0", "down"))
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "echo", mac,">", "/sys/class/net/wlan0/address"))
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "ifconfig", "wlan0", "hw", "ether", mac))
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "ifconfig", "wlan0", "up"))
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "svc", "wifi", "enable"))
                }
            }
        }
    }
}