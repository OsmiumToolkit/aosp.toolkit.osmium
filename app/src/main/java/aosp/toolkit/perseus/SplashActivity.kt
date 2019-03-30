package aosp.toolkit.perseus

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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permission = arrayOf(android.Manifest.permission.INTERNET)

        ActivityCompat.requestPermissions(this, permission, 1)

        startActivity(
            Intent(
                this, if (getSharedPreferences("splash", Context.MODE_PRIVATE)
                        .getBoolean("welcome", false)
                ) {
                    MainActivity::class.java
                } else {
                    WelcomeActivity::class.java
                }
            )
        )

        finish()
    }
}