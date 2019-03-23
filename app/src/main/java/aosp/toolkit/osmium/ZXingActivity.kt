package aosp.toolkit.osmium

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

import aosp.toolkit.osmium.base.BaseOperation.Companion.ShortToast

import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader

import kotlinx.android.synthetic.main.activity_zxing.*
/*
 * OsToolkit - Kotlin
 *
 * Date : 13 Mar 2019
 *
 * By   : 1552980358
 *
 */

class ZXingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zxing)
        initialize()


        scan.setOnClickListener {
            Thread {
                startCamera()
            }.start()
        }

        select.setOnClickListener {
            Thread {
                startGallery()
            }.start()
        }


    }

    private fun initialize() {
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        toolbar.setNavigationOnClickListener { finish() }
    }


    private fun startCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // 启动相机 Start camera
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0)
        } else {
            // 获取限权 Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA), // 相机限权 Camera permission)
                0   // 请求码 Request Code
            )
        }
    }


    private fun startGallery() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                ),
                1
            )
        } else {
            // 获取限权 Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), // 相机限权 Camera permission)
                1   // 请求码 Request Code
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            //0 -> startCamera()
            1 -> startGallery()
            else -> return
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> {
                Thread {if (resultCode == Activity.RESULT_OK) {
                    //val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data!!.data!!)
                    val bitmap = data!!.extras!!.get("data") as Bitmap
                    val width = bitmap.width
                    val height = bitmap.height
                    val d = IntArray(width * height)
                    bitmap.getPixels(d, 0, width, 0, 0, width, height)
                    val rgbLuminanceSource = RGBLuminanceSource(width, height, d)
                    val binaryBitmap = BinaryBitmap(HybridBinarizer(rgbLuminanceSource))
                    val qrCodeReader = QRCodeReader()

                    var r = "fail"
                    try {
                        r = qrCodeReader.decode(binaryBitmap).toString()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ShortToast(this, e.toString(), false)
                    }

                    runOnUiThread { result.text = r }

                }
                }.start()
            }
            1 -> {
                Thread {
                    if (resultCode == Activity.RESULT_OK) {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data!!.data!!)
                        val width = bitmap.width
                        val height = bitmap.height
                        val d = IntArray(width * height)
                        bitmap.getPixels(d, 0, width, 0, 0, width, height)
                        val rgbLuminanceSource = RGBLuminanceSource(width, height, d)
                        val binaryBitmap = BinaryBitmap(HybridBinarizer(rgbLuminanceSource))
                        val qrCodeReader = QRCodeReader()

                        var r = "fail"
                        try {
                            r = qrCodeReader.decode(binaryBitmap).toString()
                        } catch (e: Exception) {
                            ShortToast(this, e.toString(), false)
                        }

                        runOnUiThread { result.text = r }

                    }
                }.start()
            }
            else -> return
        }
    }
}