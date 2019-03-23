package aosp.toolkit.osmium

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

import aosp.toolkit.osmium.base.BaseOperation.Companion.ShortToast

import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer

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
                    val bitmap = data!!.extras!!.get("data") as Bitmap
                    val multiFormatReader = MultiFormatReader()
                    val x = bitmap.width
                    val y = bitmap.height
                    val pixels = IntArray(x * y)
                    bitmap.getPixels(pixels, 0, x, 0, 0, x, y)
                    val luminanceSource = RGBLuminanceSource(x, y, pixels)
                    val binaryBitmap = BinaryBitmap(HybridBinarizer(luminanceSource))

                    try {
                        val hint = hashMapOf<DecodeHintType, Any>(
                            DecodeHintType.CHARACTER_SET to Charsets.UTF_8,
                            DecodeHintType.POSSIBLE_FORMATS to BarcodeFormat.QR_CODE,
                            DecodeHintType.TRY_HARDER to true
                        )
                        val r = multiFormatReader.decode(binaryBitmap, hint)
                        runOnUiThread { result.text = r.text }
                    } catch (e: Exception) {
                        ShortToast(this, e.toString(), false)
                    }
                }.start()
            }
            1 -> {
                Thread {
                    if (resultCode == Activity.RESULT_OK) {
                        val img = data!!.data
                        val array = arrayOf(MediaStore.Images.Media.DATA)

                        val cursor = contentResolver.query(img!!, array,
                            null, null, null)
                        val path = cursor?.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                        cursor?.close()

                        val bitmap = BitmapFactory.decodeFile(path)
                        val multiFormatReader = MultiFormatReader()
                        val x = bitmap.width
                        val y = bitmap.height
                        val pixels = IntArray(x * y)
                        bitmap.getPixels(pixels, 0, x, 0, 0, x, y)
                        val luminanceSource = RGBLuminanceSource(x, y, pixels)
                        val binaryBitmap = BinaryBitmap(HybridBinarizer(luminanceSource))

                        try {
                            val hint = hashMapOf<DecodeHintType, Any>(
                                DecodeHintType.CHARACTER_SET to Charsets.UTF_8,
                                DecodeHintType.POSSIBLE_FORMATS to BarcodeFormat.QR_CODE,
                                DecodeHintType.TRY_HARDER to true
                            )
                            val r = multiFormatReader.decode(binaryBitmap, hint)
                            runOnUiThread { result.text = r.text }
                        } catch (e: Exception) {
                            ShortToast(this, e.toString(), false)
                        }
                    }
                }.start()
            }
            else -> return
        }
    }
}