package aosp.toolkit.osmium

/*
 * OsToolkit - Kotlin
 *
 * Date : 31/12/2018
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

import android.content.*
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permission = arrayOf(android.Manifest.permission.INTERNET)

        ActivityCompat.requestPermissions(this, permission, 1)

        if (getSharedPreferences("splash", Context.MODE_PRIVATE)
                .getBoolean("welcome", false)
        ) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }

        finish()
    }
}