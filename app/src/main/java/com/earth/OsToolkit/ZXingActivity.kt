package com.earth.OsToolkit

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_zxing.*

/*
 * OsToolkit - Kotlin
 *
 * Date : 13 Mar 2019
 *
 * By   : 1552980358
 *
 */
 
class ZXingActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zxing)
        initialize()

        scan.setOnClickListener {

        }
    }
    private fun initialize() {
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        window.statusBarColor = ContextCompat.getColor(this, android.R.color.black)

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }




}