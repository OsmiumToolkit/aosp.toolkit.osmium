package com.earth.OsToolkit;

import android.Manifest;
import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import static com.earth.OsToolkit.Base.Checking.checkRoot;

@SuppressWarnings("all")
public class SplashActivity extends Activity {

	/*
	 * 27 Dec 2018
	 *
	 * By 1552980358
	 *
	 */

	@Override
	public void onCreate(Bundle savedInstanceSatate) {
		super.onCreate(savedInstanceSatate);
		setContentView(R.layout.activity_welcome);

		SharedPreferences sp = getSharedPreferences("save", MODE_PRIVATE);
		int welcome = sp.getInt("welcome", 0);

		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
				Manifest.permission.ACCESS_NETWORK_STATE}, 1);

		Intent intent = new Intent();
		if (welcome == 0 || !checkRoot()) {
			intent.setClass(this, WelcomeActivity.class);
		} else {
			intent.setClass(this, MainActivity.class);
		}

		startActivity(intent);
		this.finish();
	}

}
