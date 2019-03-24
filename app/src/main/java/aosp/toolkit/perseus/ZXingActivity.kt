package aosp.toolkit.perseus

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

import aosp.toolkit.perseus.base.BaseOperation.Companion.ShortToast

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

/*
 * Changelog
 *
 * Scan through camera : 23 Mar 2019
 * Scan through gallery : 23 Mar 2019
 * Generate : 23 Mar 2019
 *
 */

class ZXingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zxing)
        initialize()


        camera.setOnClickListener {
            Thread {
                startCamera()
            }.start()
        }

        gallery.setOnClickListener {
            Thread {
                startGallery()
            }.start()
        }

        copyUri.setOnClickListener {
            editText.text?.let {
                Thread {
                    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip =
                        ClipData.newPlainText("Label", it.toString())
                }.start()
            }
        }

        generate.setOnClickListener {
            val t = editText1.text.toString()
            Thread {
                try {
                    // 生成二进制、设置宽高 Generate in form of binary, set width and height
                    val bitMatrix = MultiFormatWriter().encode(t, BarcodeFormat.QR_CODE, 500, 500)
                    val width = 500
                    val height = 500

                    // 像素格数量 No. of pixels
                    val pixels = IntArray(width * height)

                    // 写入像素格 Write pixels
                    for (y: Int in 0 until height) {
                        val offset = y * width
                        for (x: Int in 0 until width) {
                            pixels[offset + x] = if (bitMatrix.get(x, y)) {
                                ContextCompat.getColor(this, android.R.color.black)
                            } else {
                                ContextCompat.getColor(this, android.R.color.white)
                            }
                        }

                        // 转换成Bitmap格式 Convert into Bitmap format
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

                        runOnUiThread { imageView.setImageBitmap(bitmap) }
                    }
                } catch (e: Exception) {
                    ShortToast(this, e.toString(), false)
                }
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
            0 -> startCamera()
            1 -> startGallery()
            else -> return
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> {
                Thread {
                    if (resultCode == Activity.RESULT_OK) {
                        // 直接接收Bitmap Get Bitmap directly
                        val bitmap = data!!.extras!!.get("data") as Bitmap

                        // 获取宽高 Get width and height
                        val width = bitmap.width
                        val height = bitmap.height

                        // 获取像素格数量 Get no. of pixels
                        val d = IntArray(width * height)
                        bitmap.getPixels(d, 0, width, 0, 0, width, height)

                        val binaryBitmap = BinaryBitmap(HybridBinarizer(RGBLuminanceSource(width, height, d)))

                        var r = "fail"
                        try {
                            // 解析 decode
                            r = QRCodeReader().decode(binaryBitmap).toString()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            ShortToast(this, e.toString(), false)
                        }

                        runOnUiThread { editText.setText(r) }

                        // 释放 release
                        bitmap.recycle()
                    }
                }.start()
            }
            1 -> {
                Thread {

                    if (resultCode == Activity.RESULT_OK) {
                        // 返回的Uri使用媒体储存空间获取Bitmap
                        // Apply uri to get Bitmap through MediaStore
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data!!.data!!)

                        // 以下内容与以上相同 Following is same as above
                        val width = bitmap.width
                        val height = bitmap.height
                        val d = IntArray(width * height)
                        bitmap.getPixels(d, 0, width, 0, 0, width, height)
                        val binaryBitmap = BinaryBitmap(HybridBinarizer(RGBLuminanceSource(width, height, d)))

                        var r = "fail"
                        try {
                            r = QRCodeReader().decode(binaryBitmap).toString()
                        } catch (e: Exception) {
                            ShortToast(this, e.toString(), false)
                        }

                        runOnUiThread { editText.setText(r) }

                        bitmap.recycle()
                    }
                }.start()
            }
            else -> return
        }
    }
}