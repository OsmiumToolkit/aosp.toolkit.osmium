package aosp.toolkit.perseus.fragments

import android.content.*
import android.content.ClipboardManager
import android.os.*
import android.support.v4.app.Fragment
import android.text.*
import android.view.*

import aosp.toolkit.perseus.R
import aosp.toolkit.perseus.base.BaseIndex.*
import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast
import aosp.toolkit.perseus.base.BaseOperation.Companion.checkFilePresent
import aosp.toolkit.perseus.base.BaseOperation.Companion.readFile

import com.topjohnwu.superuser.Shell

import kotlinx.android.synthetic.main.fragment_extends.*
import java.net.URL
import kotlin.Exception

/*
 * OsToolkit - Kotlin
 *
 * Date : 21 Jan 2019
 *
 * By   : 1552980358
 *
 */

/*
 * Changelog:
 * - detectserver :  1 Mar 2019
 * - Short URL    : 13 Mar 2019
 */

class ExtendsFragment : Fragment() {
    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aosp.toolkit.osmium.base.BaseManager.getInstance().setExtendsFragment(this)
    }
    */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_extends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMac()
        freeform.init(
            this,
            extends_freeform,
            type_shell,
            index_extends,
            EXTENDS_FREE_FORM,
            Build.VERSION.SDK_INT >= 24
        )
        detectserver.init(this, type_shell, index_extends, EXTENDS_DETECT_SERVER)
        setShortUrl()
    }

    private fun setMac() {
        if (checkFilePresent("/sys/class/net/wlan0/address")) {
            editText.setText(readFile("/sys/class/net/wlan0/address"))
            editText.addTextChangedListener(object : TextWatcher {
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
                                editText.setText(s.toString().toLowerCase())
                            }
                        }
                    }
                    editText.addTextChangedListener(this)
                }

                override fun afterTextChanged(s: Editable?) {
                    try {
                        editText.setSelection(s.toString().length.plus(1))
                    } catch (e: Exception) {
                        ShortToast(activity!!, e.toString(), true)
                        try {
                            editText.setSelection(s.toString().length)
                        } catch (e: Exception) {
                            ShortToast(activity!!, e.toString(), true)
                        }
                    }

                }
            })
            val pattern = "(([a-z]|\\d){2}:){5}([a-z]|\\d){2}".toRegex()
            done.setOnClickListener {
                val mac = editText.text.toString()
                if (mac.length == 17 && pattern.matches(mac)) {
                    Thread {
                        try {
                            Shell.su(
                                "chmod 644 /sys/class/net/wlan0/address",
                                "svc wifi disable", "ifconfig wlan0 down",
                                "echo $mac > /sys/class/net/wlan0/address",
                                "ifconfig wlan0 hw ether $mac",
                                "ifconfig wlan0 up",
                                "svc wifi enable"
                            ).exec()
                        } catch (e: Exception) {
                            ShortToast(activity!!, e.toString(), false)
                        }
                    }.start()
                }
            }

        }
    }

    private fun setShortUrl() {
        get.setOnClickListener {
            val b = "http://suo.im/api.php?url=${before.text}"
            Thread {
                try {
                    val url = URL(b)
                    val inputStream = url.openStream()
                    val result = inputStream.bufferedReader(Charsets.UTF_8).readLine()
                    activity!!.runOnUiThread { result?.let { after.setText(it) } }
                    inputStream.close()
                } catch (e: Exception) {
                    ShortToast(activity!!, e.toString(), false)
                }
            }.start()
        }
        copy.setOnClickListener {
            try {
                val clipboardManager = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.primaryClip = ClipData.newPlainText("Label", after.text)
            } catch (e: Exception) {
                ShortToast(activity!!, e.toString(), false)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        detectserver.init(this, type_shell, index_extends, EXTENDS_DETECT_SERVER)
    }
}