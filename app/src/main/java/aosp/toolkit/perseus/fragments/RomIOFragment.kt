package aosp.toolkit.perseus.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.AppCompatSeekBar
import android.view.*
import android.widget.SeekBar

import aosp.toolkit.perseus.R
import aosp.toolkit.perseus.base.BaseIndex.*
import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast
import aosp.toolkit.perseus.base.BaseOperation.Companion.checkFilePresent
import aosp.toolkit.perseus.base.BaseOperation.Companion.readFile

import kotlinx.android.synthetic.main.fragment_romio.*
import kotlinx.android.synthetic.main.fragment_romio_emmc.*
import kotlinx.android.synthetic.main.fragment_romio_ufs.*
import java.lang.Exception

/*
 * OsToolkit - Kotlin
 *
 * Date : 19 Jan 2019
 *
 * By   : 1552980358
 *
 */

class RomIOFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_romio, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val romIOeMMCFragment = RomIOeMMCFragment()
        val romIOUFSFragment = RomIOUFSFragment()

        val fragments = listOf(romIOeMMCFragment, romIOUFSFragment)
        val tabs = listOf("eMMC", "UFS")
        viewPager.adapter = ViewPagerAdapter(fragmentManager, fragments, tabs)
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

    class RomIOeMMCFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_romio_emmc, container, false)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            initViews()
        }

        fun initViews() {
            emmc_addrandom.init(this, io_random_emmc, type_shell, index_romio, IO_RANDOM_EMMC)
            emmc_iostats.init(this, io_iostats_emmc, type_shell, index_romio, IO_IOSTATS_EMMC)
            emmc_nomerges.init(this, io_nomerges_emmc, type_shell, index_romio, IO_NOMERGES_EMMC)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val seekBar: AppCompatSeekBar = view.findViewById(R.id.seekBar)
            Thread {
                if (checkFilePresent("/sys/block/mmcblk0/queue/rq_affinity")) {
                    val status = readFile("/sys/block/mmcblk0/queue/rq_affinity")
                    activity!!.runOnUiThread {
                        seekBar.progress = if (status == "Fail") {
                            1
                        } else {
                            status.toInt()
                        }
                        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                Thread {
                                    try {
                                        Runtime.getRuntime().exec(
                                            arrayOf(
                                                "su",
                                                "-c",
                                                "echo",
                                                "\"" + progress + "\"",
                                                ">",
                                                "/sys/block/mmcblk0/queue/rq_affinity"
                                            )
                                        )
                                    } catch (e: Exception) {
                                        ShortToast(activity!!, e, false)
                                    }
                                }.start()
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                            }
                        })
                    }

                } else {
                    activity!!.runOnUiThread { seekBar.isEnabled = false }
                }
            }.start()
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            initViews()
        }
    }

    class RomIOUFSFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_romio_ufs, container, false)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            initViews()
        }

        fun initViews() {
            ufs_addrandom.init(this, io_random_ufs, type_shell, index_romio, IO_RANDOM_UFS)
            ufs_iostats.init(this, io_iostats_ufs, type_shell, index_romio, IO_IOSTATS_UFS)
            ufs_nomerges.init(this, io_nomerges_ufs, type_shell, index_romio, IO_NOMERGES_UFS)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val seekBar: AppCompatSeekBar = view.findViewById(R.id.seekBar)
            Thread {
                if (checkFilePresent("/sys/block/sda/queue/rq_affinity")) {
                    val status = readFile("/sys/block/sda/queue/rq_affinity")
                    activity!!.runOnUiThread {
                        seekBar.progress = if (status == "Fail") {
                            1
                        } else {
                            status.toInt()
                        }
                        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                Thread {
                                    try {
                                        Runtime.getRuntime().exec(
                                            arrayOf(
                                                "su", "-c",
                                                "echo", "\"" + progress + "\"", ">", "/sys/block/sda/queue/rq_affinity"
                                            )
                                        )
                                    } catch (e: Exception) {
                                        ShortToast(activity!!, e, false)
                                    }
                                }.start()
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            }
                        })
                    }

                } else {
                    activity!!.runOnUiThread { seekBar.isEnabled = false }
                }
            }.start()
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            initViews()
        }
    }
}